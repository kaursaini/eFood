package ctrl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Engine;

@WebServlet("/Index")
public class Index extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.getSession(true).setMaxInactiveInterval(Engine.SESSION_ALIVE_TIME);

//		// For testing purposes
//		//----------------------
//		// NOTE: errors will occur if server runs after save
//		// (must stop and run it again fresh)
//		Middleware mw = new Middleware();
//		
//		Timer timer = new Timer();
//		Calendar date = Calendar.getInstance(TimeZone.getTimeZone("EST"));
//
//		// millisec -> sec -> min -> hour -> day
//		timer.schedule(mw,date.getTime(),1000 * 30); 
//		//timer.schedule(mw,date.getTime(),1000 * 60 * 60 * 24); // millisec -> sec -> min -> hour -> day
//		------------------------
		try
		{
			Engine engine = Engine.getInstance();
			request.setAttribute("result", engine.doCategory());
		} catch (Exception e)
		{
			request.setAttribute("error", e.getMessage());
		}
		this.getServletContext().getRequestDispatcher("/Category.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

}
