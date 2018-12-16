package model;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;					   
import java.text.SimpleDateFormat;				   
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;						
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;	   

public class Engine
{
	public static final int SESSION_ALIVE_TIME = 30*60; // 30min
	public static final String NUM_ITEMS_PER_PAGE = "5";
	public static final String REGEX_POSITIVE_UNSIGNED_INT = "\\d+"; // 1 or more digits

	private static Engine instance = null;
	private static CategoryDAO categoryDAO = null;
	private static ItemDAO itemDAO = null;					

	private Engine()
	{
		categoryDAO = new CategoryDAO();
		itemDAO = new ItemDAO();
	}

	public synchronized static Engine getInstance()
	{
		if (instance == null)
			instance = new Engine();
		return instance;
	}

	// Catalog Methods
	//================
	private int getValidNumRowsGet(String numOfItemsPerPage)
	{
		int numRowsGet = ItemDAO.OMIT_INT;
		if(numOfItemsPerPage!=null && numOfItemsPerPage.length()>0 && Pattern.compile(REGEX_POSITIVE_UNSIGNED_INT).matcher(numOfItemsPerPage).matches())
			numRowsGet = Integer.parseInt(numOfItemsPerPage);
		
		return numRowsGet;
	}
	private int getValidStartFromRow(String onPageNum, int numRowsGet)
	{
		int startFromRow = ItemDAO.OMIT_INT;
		if(onPageNum!=null && onPageNum.length()>0 && Pattern.compile(REGEX_POSITIVE_UNSIGNED_INT).matcher(onPageNum).matches())
			startFromRow = (Integer.parseInt(onPageNum)-1) * numRowsGet;
		
		return startFromRow;
	}
	
	public List<CategoryBean> doCategory() throws Exception
	{
		return categoryDAO.retrieve();
	}

	public int doCatalogNumItems(String categoryId, String idOrName, String sortBy, String onPageNum, String numOfItemsPerPage) throws Exception
	{
		int numRowsGet = getValidNumRowsGet(numOfItemsPerPage);
		int startFromRow = getValidStartFromRow(onPageNum, numRowsGet);
		return itemDAO.countRetrieve(categoryId, idOrName, sortBy, startFromRow, numRowsGet);
	}
	
	public List<ItemBean> doCatalog(String categoryId, String idOrName, String sortBy, String onPageNum, String numOfItemsPerPage) throws Exception
	{
		int numRowsGet = getValidNumRowsGet(numOfItemsPerPage);
		int startFromRow = getValidStartFromRow(onPageNum, numRowsGet);
		return itemDAO.retrieve(categoryId, idOrName, sortBy, startFromRow, numRowsGet);
	}
	//================
	
	// Checkout Methods
	//================
	// Checkout creates files in current directory (System.getProperty("user.dir")?)
	public static String getPONamingConvention(String account_code, String orderID)
	{
		return "(.*_)?po" + account_code + "_" + orderID + ".xml"; // <processed_>po<account code>_<P/O number>.xml
	}
	private File[] getAccountPOFiles(String directoryPath, String account_code)
	{
		File dir = new File(directoryPath);
		FileFilter fileFilter = new FileFilter() {
	        @Override
	        public boolean accept(File file) 
	        {
	        	boolean nameMatch = Pattern.compile(getPONamingConvention(account_code, "(\\d+)")).matcher(file.getName()).matches();
	        	
	        	//long fileModifiedTime = file.lastModified(); // the time the file was last modified, in milliseconds
	        	//boolean fileCreatedBeforeProcessStart = fileModifiedTime < processStartTime;
	        	
//	        	try {
//	        		System.out.println("dir.getCanonicalPath() = " + dir.getCanonicalPath());
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
	        	//System.out.println("ACCEPT RETURNING = " + (nameMatch && fileCreatedBeforeProcessStart));
	            //return nameMatch && fileCreatedBeforeProcessStart;
	        	
	        	return nameMatch;
	        }
	    };
		return dir.listFiles(fileFilter);
	}
	
	public File doCheckout(Order order) throws Exception
	{		
		//System.out.println("order = " + order);
		String directoryPath = "."; // current
		
		String account_code = order.getCustomer().getAccount();
		//System.out.println("account_code = " + account_code);
		File[] pos = getAccountPOFiles(directoryPath, account_code);
		//System.out.println("pos.length = " + pos.length);
		
		// get order ID and submitted date (<order id="120" submitted="2018-11-29">)
		String orderID = pos.length+1 + ""; // pos.length files exists, add 1 to ID
		String submittedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		
		order.setId(orderID);
		order.setSubmitted(submittedDate);
		
		String filename = "po" + account_code + "_" + orderID + ".xml";
		//System.out.println("filename = " + filename);

		//*Errors*/
		//System.out.println(filename);
		//System.out.println((new File("/"+date)).mkdir());
		//File xmlfile = new File(date+"/"+filename);
		
		File xmlfile = new File(directoryPath+"/"+filename);
		xmlfile.createNewFile(); // will do nothing if file already exists
		
		//System.out.println("xmlfile.getPath() = " + xmlfile.getPath());
		
		FileOutputStream fos = new FileOutputStream(xmlfile);
		
		//----------------------------------------->> Marshall to XML :
		JAXBContext context = JAXBContext.newInstance(Order.class);
		Marshaller m = context.createMarshaller();

		// For "human consumption", add this:
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		m.marshal(order, fos); // replace System.out with socket or file output stream

		// In lieu of annotation, replace the above statement with these two:
		// JAXBElement<Library> root = new JAXBElement<Library>(new QName("library"), Library.class, lib);
        // m.marshal(root, System.out);
		
		fos.close();
		
		return xmlfile;
	}
	//================
	
	//PODisplay methods
	//================
	public Order doPODisplay(String filename) throws Exception
	{	
		// get the file
		File dir = new File(".");
		FileFilter fileFilter = new FileFilter() {
	        @Override
	        public boolean accept(File file) 
	        {
	        	String searchFor = "(.*_)?" + filename; // allow for marked order to be found
	        	//boolean nameMatch = file.getName().equals(filename) || file.getName().equals("processed_"+filename);
	        	boolean nameMatch = Pattern.compile(searchFor).matcher(file.getName()).matches();
	        	//System.out.println(">>> nameMatch = " + nameMatch);
	        	return nameMatch;
	        }
	    };
		File[] results = dir.listFiles(fileFilter);
		
		if(results == null || results.length==0)
			throw new Exception("Order not found");
		
		File file = results[0];
		
		// unmarshal (xml to java)
		JAXBContext jaxbContext = JAXBContext.newInstance(Order.class);
		Unmarshaller jaxbUnmarshaller;
		jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		return (Order) jaxbUnmarshaller.unmarshal(file);
	}
	//================

	public Item doCart(String itemId, String quantity) throws Exception
	{
		// get item from database
		List<ItemBean> itemList = itemDAO.retrieve(itemId); 
		ItemBean itemBean = itemList.get(0);
		
		int qty = Integer.parseInt(quantity);
		
		// instantiate an Item from the ItemBean
		Item item = new Item();
		item.setNumber(itemBean.getNumber()); 
		item.setName(itemBean.getName()); 
		item.setPrice(itemBean.getPrice()); 

		item.setQuantity(qty);
		
		item.setExtended(item.getPrice() * item.getQuantity());
		return item;
	}

	public double doSubTotal(List<Item> items)
	{
		double subTotal = 0.0;

		for (Item ib : items)
		{
			int qty = ib.getQuantity();
			subTotal = subTotal + (ib.getPrice() * qty);
		}

		return subTotal;
	}

	public double doHst(double amtHst)
	{
		double gst = 0.0;
		if (amtHst == 0.0)
		{
			gst = 0.0;
		} else
		{
			gst = 0.13 * amtHst;
		}

		return gst;
	}

	public double doShippingCost(double subTotal)
	{
		double shippingCost = 0.0;
		if (subTotal < 100)
		{
			shippingCost = 5.00;
		} else
		{
			shippingCost = 0.00;
		}
		return shippingCost;
	}

	public double doTotal(double subTotal, double hst, double ship)
	{
		double total = 0.0;
		total = subTotal + hst + ship;
		return total;
	}

	public void validateQty(String value) throws Exception
	{
		try
		{
			int qty = Integer.parseInt(value);
			if (qty < 0)
				throw new Exception("Negative Quantity");

		} catch (NumberFormatException e)
		{
			throw new Exception("Invalid quantity");
		}

	}
}
