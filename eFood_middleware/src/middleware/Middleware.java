package middleware;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.TimerTask;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/*
consolidate by forming union of P/O's such that
- each item appears only once with 
  + a quantity equal to the sum of its quantities in all the P/O's
  
- reporting engine
- implemented with JSE (not JEE) as a headless (no GUI) console app
- runs asynchronously with B2C and B2B (possibly on a differentbox) as a scheduled job

- come up with a "sensible" process that ensures no P/O is missed and each is processed just once
  + "sensible" means it should be flexible enough to allow the three systems to be loosely coupled and avoids concurrency issues
 */

public class Middleware extends TimerTask
{
	public Middleware()
	{
		super();
	}
	
	@Override
	public void run() // TimerTask
	{
		consolidate();
	}

	private void consolidate()
	{
		try
		{
			HashMap<String, Integer> consolidatedItemMap = new HashMap<String, Integer>(); // id, quantity
			
			File[] poFiles = getPOFiles (System.getProperty("user.home")); // current directory

			// consolidate po items into map
			// =============================
			for(File f : poFiles)
			{
		    	//System.out.println("name of file >> " + f.getName());

				// unmarshal (xml to java)
		    	//-------------------------
				JAXBContext jaxbContext = JAXBContext.newInstance(Order.class);
				Unmarshaller jaxbUnmarshaller;
				jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Order order = (Order) jaxbUnmarshaller.unmarshal(f);
				
				//System.out.println(order.getItems().getItemList());
				
				// consolidate
		    	//-------------------------
				List<Item> items = order.getItems().getItemList();
				// read the P/O items
				for(Item i : items)
				{
					// >> get their ID, name, and quanity
					String id = i.getNumber();
					int quantity = i.getQuantity();

					// >> add them to a collection
					// >> >> (look out for item repeats -- add their quantities together)
					if(consolidatedItemMap.containsKey(id))
					{
						int existingQty = consolidatedItemMap.get(id);
						consolidatedItemMap.put(id, existingQty + quantity);
					}
					else
					{
						consolidatedItemMap.put(id, quantity);
					}
				}		
				// >> rename the item / mark as processed
				Path source = Paths.get(f.getCanonicalPath());
				Files.move(source, source.resolveSibling("processed_" + f.getName()));
				
				// continue reading files
			}
			
			// create consolidated order
			// =============================
			String submittedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			
			// transform map into consolidated java beans
			//--------------------------------------------
			List<ConsolidatedItem> consolidatedItemList = new ArrayList<ConsolidatedItem>();
			for(String s : consolidatedItemMap.keySet())
			{
				ConsolidatedItem item = new ConsolidatedItem(s, consolidatedItemMap.get(s));
				//System.out.println("[" + s + "," + consolidatedItemMap.get(s) + "]");
				consolidatedItemList.add(item);
			}
			ConsolidatedItems items = new ConsolidatedItems(consolidatedItemList);
			ConsolidatedOrder consolidatedOrder = new ConsolidatedOrder("Foods R US", submittedDate, items);

			// create consolidated order file (assumes MW runs once a day)
			//--------------------------------------------
			String filename = "cpofoodsrus_"+submittedDate+".xml";
			File file = new File(filename);
			file.createNewFile(); // will do nothing if file already exists
			
			// Marshall to XML
			//--------------------------------------------
			FileOutputStream fos = new FileOutputStream(file);

			JAXBContext context = JAXBContext.newInstance(ConsolidatedOrder.class);
			Marshaller m = context.createMarshaller();
	
			// For "human consumption", add this:
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	
			m.marshal(consolidatedOrder, fos); // replace System.out with socket or file output stream
	
			// In lieu of annotation, replace the above statement with these two:
			// JAXBElement<Library> root = new JAXBElement<Library>(new QName("library"), Library.class, lib);
	        // m.marshal(root, System.out);
			fos.close();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private File[] getPOFiles (String directory)
	{
		File dir = new File(directory); 

		long processStartTime = System.currentTimeMillis(); // Returns the current time in milliseconds
		FileFilter fileFilter = new FileFilter() {
	        @Override
	        public boolean accept(File file) 
	        {
	        	boolean nameMatch = Pattern.compile("po(.+)_(\\d+).xml").matcher(file.getName()).matches();
	        	
	        	long fileModifiedTime = file.lastModified(); // the time the file was last modified, in milliseconds
	        	boolean fileCreatedBeforeProcessStart = fileModifiedTime < processStartTime;
	        	
	            return nameMatch && fileCreatedBeforeProcessStart;
	        }
	    };
	    
	    return dir.listFiles(fileFilter);
	}
}
