package adhoc;

import java.io.IOException;
import java.util.List;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Engine;
import model.Item;
import model.Order;

@WebFilter(dispatcherTypes =
{ DispatcherType.FORWARD }, urlPatterns =
{ "/Cart.jspx" })
public class CrossSelling implements Filter
{

	public CrossSelling()
	{
	}

	public void destroy()
	{
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException
	{
		Order order = (Order) ((HttpServletRequest) request).getSession().getAttribute("order");
		Engine engine = Engine.getInstance();
		boolean present = false;

		if (order != null)
		{
			List<Item> list = order.getItems().getItemList();
			for (Item i : list)
			{
				if (i.getNumber().equals("1409S413"))
					present = true;
			}
		}
		if (present)
		{
			OutResponse resp = new OutResponse((HttpServletResponse) response);
			chain.doFilter(request, resp);
			try
			{
				Item crossSelling = engine.doCart("2002H712", "1");
				String html = "<hr/><h4 align=\"center\"  style=\"margin-top: 16px;\">Recommended Item for K5 Praline Ice Cream</h4>";
				html += "<div align=\"center\" >" + crossSelling.getName() + "</div>";
				html += "<div align=\"center\" >" + crossSelling.getNumber() + "</div>";
				html += String.format("<div align=\"center\" >$%.2f</div>", crossSelling.getPrice());
				String content = resp.getContent();
				content = content.replace("</table></form>", "</table></form>" + html);
				response.getWriter().write(content);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		} else
		{
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig fConfig) throws ServletException
	{
	}

}
