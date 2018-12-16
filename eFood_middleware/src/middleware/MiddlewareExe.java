package middleware;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;


public class MiddlewareExe
{
	public static void main(String[] args) throws Exception
	{
		// NOTE: errors will occur if server runs after save
		// (must stop and run it again fresh)
		Middleware mw = new Middleware();
		
		// create a thread and save so it can be retrived and stopped later
		Timer timer = new Timer();
		//ServletContext sc = sce.getServletContext();
		//sc.setAttribute(MW_THREAD, timer);
		
		// get current date and time
		Calendar date = Calendar.getInstance(TimeZone.getTimeZone("EST"));
		
		// set to midnight
		date.set(Calendar.HOUR, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);

		// run every day at midnight
		// (millisec -> sec -> min -> hour = day)
		timer.schedule(mw, date.getTime(), 1000 * 60 * 60 * 24);
	}
}
