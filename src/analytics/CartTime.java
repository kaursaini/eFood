package analytics;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class CartTime implements HttpSessionListener, HttpSessionAttributeListener
{

	private List<Long> timeForCart;

	public CartTime()
	{
		timeForCart = new ArrayList<Long>();
	}

	public void sessionCreated(HttpSessionEvent se)
	{
		long creationTime = System.currentTimeMillis();
		se.getSession().setAttribute("creationTime", creationTime);
	}

	public void sessionDestroyed(HttpSessionEvent se)
	{
	}

	public void attributeAdded(HttpSessionBindingEvent se)
	{
		if (se.getName().equals("cartData"))
		{
			long addedTime = System.currentTimeMillis();
			long creationTime = Long.parseLong(se.getSession().getAttribute("creationTime").toString());
			timeForCart.add(addedTime - creationTime);
			double timeValue = timeForCart.stream().mapToLong(Long::longValue).average().getAsDouble();
			se.getSession().getServletContext().setAttribute("timeForCart", timeValue);
		}
	}

	public void attributeRemoved(HttpSessionBindingEvent se)
	{
	}

	public void attributeReplaced(HttpSessionBindingEvent se)
	{
	}

}
