package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class ItemDAO
{
	private static final String DB_URL = "jdbc:derby://localhost:64413/EECS;user=student;password=secret";
	private static final String REGEX_CATID = "\\d+"; // 1 or more digits
	private static final String REGEX_NUMBER_ID = "\\d\\d\\d\\d[a-zA-Z]\\d\\d\\d"; //ex. 0905A112
	private static final String REGEX_SQL_INJECTION_CHARACTERS = "[;'=,\\*]";
	private static final String SORTBY_DELIM = "-";
	private static final String REGEX_SORTBY = "(number|name|price)("+SORTBY_DELIM+"(asc|desc))?";
	
	public static final String OMIT_STRING = "";
	public static final int OMIT_INT = -1;

	public ItemDAO()
	{
		
	}
	
	public List<ItemBean> retrieve(String idOrName) throws Exception
	{
		return retrieve(OMIT_STRING, idOrName, OMIT_STRING, OMIT_INT, OMIT_INT);
	}
	
	public List<ItemBean> retrieve(String categoryId, String idOrName, String sortBy, int startFromRow, int numRowsGet) throws Exception
	{
		// connect to database
		Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
		Connection con = DriverManager.getConnection(DB_URL);
		Statement s = con.createStatement();
		s.executeUpdate("set schema roumani");
		
		String query = formQueryString(categoryId, idOrName, sortBy, startFromRow, numRowsGet);
		
		ResultSet r = s.executeQuery(query);
		//System.out.println("query = " + query);
		
		//retrieve result of query
		List<ItemBean> list = new ArrayList<ItemBean>();
		while (r.next()) 
		{
			// Price 2nd decimal place rounded up by...
			// - moving decimal point 2 places (*100)
			// - rounding (ceiling)
			// - moving decimal back (/100)
			ItemBean b = new ItemBean(
					r.getString("NUMBER"),
					r.getString("NAME"),
					Math.ceil(r.getDouble("PRICE") * 100.0) / 100.0,
					r.getInt("QTY"),
					r.getInt("ONORDER"),
					r.getInt("REORDER"),
					r.getString("CATID"),
					r.getString("SUPID"),
					r.getDouble("COSTPRICE"),
					r.getString("UNIT"));
			
			list.add(b);
		}
		r.close(); s.close(); con.close();
			
		return list;
	}
	
	public int countRetrieve(String categoryId, String idOrName, String sortBy, int startFromRow, int numRowsGet) throws Exception
	{
		// connect to database
		Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
		Connection con = DriverManager.getConnection(DB_URL);
		Statement s = con.createStatement();
		s.executeUpdate("set schema roumani");
		
		String query = "";
		query += "select count(*) from";
		query += "("; 
		query += formQueryString(categoryId, idOrName, sortBy, startFromRow, numRowsGet);
		query += ") alias";

		ResultSet r = s.executeQuery(query);
		//System.out.println("query = " + query);
		
		//retrieve result of query
		int result = 0;
		if (r.next()) 
			result = r.getInt(1);
		
		r.close(); s.close(); con.close();
		
		return result;
	}
	
	private String formQueryString(String categoryId, String idOrName, String sortBy, int startFromRow, int numRowsGet)
	{
		// form SQL query
		//========================================================
		// - Table: Item 
		// - Columns: NUMBER, NAME, PRICE, QTY, ONORDER, REORDER, CATID, SUPID, COSTPRICE, UNIT
		String query = "select * from Item"; // entire table
		
		// where
		//--------------------------------------------------------
		List<String> where_list = new ArrayList<String>();
		
		if(categoryId!=null && categoryId.length()>0 && Pattern.compile(REGEX_CATID).matcher(categoryId).matches())
			where_list.add("CATID = " + categoryId);

		if(idOrName != null)
		{
			// remove SQL injection characters
			idOrName = idOrName.replaceAll(REGEX_SQL_INJECTION_CHARACTERS, "");
			
			if(idOrName.length() > 0)
			{
				if(Pattern.compile(REGEX_NUMBER_ID).matcher(idOrName).matches())
					where_list.add("NUMBER = " + "'" + idOrName + "'");
				else
					where_list.add("upper(NAME) like " + "upper('%" + idOrName + "%')");
			}
		}
		
		// form where portion of query
		
		Iterator<String> iterator;
		if(( iterator = where_list.iterator() ).hasNext()) {
			query = query + " where " + iterator.next();
			while(iterator.hasNext()) {
				query = query + " and " + iterator.next();
			}
		}
		
		// sortBy (format: "column" or "column-order")
		//--------------------------------------------------------
		if(sortBy!=null && sortBy.length()>0 && Pattern.compile(REGEX_SORTBY).matcher(sortBy).matches())
		{
			String[] sb = sortBy.split(SORTBY_DELIM);
			query = query + " order by " + sb[0];
		
			if(sb.length>1)
				query = query + " " + sb[1];
		}
		
		// offset, fetch (retrieve sub-list of items)
		//--------------------------------------------------------
		if(startFromRow>=0 && numRowsGet>=0)
			query = query + " offset " + startFromRow + " rows fetch next " + numRowsGet + " rows only";
		
		return query;
	}
}
