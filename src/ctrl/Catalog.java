package ctrl;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Engine;

@WebServlet("/Catalog")
public class Catalog extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Engine engine = Engine.getInstance();
		
		String onPageNum = request.getParameter("page"); // "page" in url
		if(onPageNum == null || onPageNum.equals("") || !Pattern.compile(Engine.REGEX_POSITIVE_UNSIGNED_INT).matcher(onPageNum).matches())
			onPageNum = "1";
		
		String numOfItemsPerPage = request.getParameter("numOfItemsPerPage");
		if(numOfItemsPerPage == null || numOfItemsPerPage.equals("") || !Pattern.compile(Engine.REGEX_POSITIVE_UNSIGNED_INT).matcher(numOfItemsPerPage).matches())
			numOfItemsPerPage = Engine.NUM_ITEMS_PER_PAGE;
		
		String sortBy = request.getParameter("sortBy");

		// set data in view
		request.setAttribute("onPageNum", onPageNum);
		request.setAttribute("numOfItemsPerPage", numOfItemsPerPage);
		request.setAttribute("sortBy", sortBy);

		if(request.getParameter("searchKeyword") != null) // searching for keyword (id or name)
		{
			String searchKeyword = request.getParameter("searchKeyword").trim();

			request.setAttribute("searchKeyword", searchKeyword);

			try
			{
				request.setAttribute("result", engine.doCatalog("", searchKeyword, sortBy, onPageNum, numOfItemsPerPage));
				request.setAttribute("numOfItems", engine.doCatalogNumItems("", searchKeyword, sortBy, "", ""));
			}
			catch(Exception e)
			{
				request.setAttribute("error", e.getMessage());
			}
			this.getServletContext().getRequestDispatcher("/Catalog.jspx").forward(request, response);
		}
		else if(request.getParameter("categoryID") != null) // searching category
		{
			// get data of clicked category 
			String categoryName = request.getParameter("categoryName");
			String categoryID = request.getParameter("categoryID");
			
			// set data in view
			request.setAttribute("categoryName", categoryName);
			request.setAttribute("categoryID", categoryID);

			try
			{
				request.setAttribute("result", engine.doCatalog(categoryID, "", sortBy, onPageNum, numOfItemsPerPage));
				request.setAttribute("numOfItems", engine.doCatalogNumItems(categoryID, "", sortBy, "", ""));
			}
			catch(Exception e)
			{
				request.setAttribute("error", e.getMessage());
			}
			this.getServletContext().getRequestDispatcher("/Catalog.jspx").forward(request, response);
		}
		else // show categories
		{
			try
			{
				request.setAttribute("result", engine.doCategory());
			}
			catch(Exception e)
			{
				request.setAttribute("error", e.getMessage());
			}
			this.getServletContext().getRequestDispatcher("/Category.jspx").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

}
