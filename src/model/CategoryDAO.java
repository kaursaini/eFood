package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO
{
	private static final String DB_URL = "jdbc:derby://localhost:64413/EECS;user=student;password=secret";

	public CategoryDAO()
	{

	}

	public List<CategoryBean> retrieve() throws Exception
	{
		// connect to database
		Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
		Connection con = DriverManager.getConnection(DB_URL);
		Statement s = con.createStatement();
		s.executeUpdate("set schema roumani");

		// form SQL query
		// ========================================================
		// - Table: Category
		// - Columns: ID, NAME, DESCRIPTION, PICTURE
		String query = "select * from Category"; // entire table

		ResultSet r = s.executeQuery(query);

		// retrieve result of query
		List<CategoryBean> list = new ArrayList<CategoryBean>();
		while (r.next())
		{
			CategoryBean sb = new CategoryBean(r.getString("ID"), r.getString("NAME"), r.getString("DESCRIPTION"));

			list.add(sb);
		}
		r.close();
		s.close();
		con.close();

		return list;
	}
}
