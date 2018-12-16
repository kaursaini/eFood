package ctrl;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Engine;
import model.Order;

@WebServlet("/PODisplay")
public class PODisplay extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String filename = request.getParameter("view");
		//System.out.println("filename = " + filename);
		if(filename!=null && Pattern.compile(Engine.getPONamingConvention("(.+)", "(\\d+)")).matcher(filename).matches())
		{
			try
			{
				Engine engine = Engine.getInstance();
				request.setAttribute("result", engine.doPODisplay(filename));
				//request.setAttribute("result", engine.doPODisplay(filename));
			} catch (Exception e)
			{
				request.setAttribute("error", e.getMessage());
			}
			this.getServletContext().getRequestDispatcher("/PODisplay.jspx").forward(request, response);
		}
		else
		{
			
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

}
