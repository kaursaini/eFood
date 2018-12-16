package analytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import model.Item;
import model.Order;

@WebListener
public class CheckoutTime implements HttpSessionAttributeListener, HttpSessionListener
{

	private List<Long> timeForCheckout;

	public CheckoutTime()
	{
		timeForCheckout = new ArrayList<Long>();
	}

	public void sessionCreated(HttpSessionEvent se)
	{
		long creationTime = System.currentTimeMillis();
		se.getSession().setAttribute("creationTime", creationTime);
		// System.out.println("creationTime: " + creationTime);
	}

	public void sessionDestroyed(HttpSessionEvent se)
	{
	}

	public void attributeAdded(HttpSessionBindingEvent se)
	{
	}

	public void attributeRemoved(HttpSessionBindingEvent se)
	{
	}

	public void attributeReplaced(HttpSessionBindingEvent se)
	{
		if (se.getName().equals("cartData"))
		{
			String confirmDone = (String) se.getSession().getAttribute("confirmDone");
			if (confirmDone.equals("true"))
			{
				long checkoutTime = System.currentTimeMillis();
				long creationTime = Long.parseLong(se.getSession().getAttribute("creationTime").toString());
				timeForCheckout.add(checkoutTime - creationTime);
				double timeValue = timeForCheckout.stream().mapToLong(Long::longValue).average().getAsDouble();
				se.getSession().getServletContext().setAttribute("timeForCheckout", timeValue);
			}
		}
	}

}
