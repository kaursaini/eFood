package ctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Engine;
import model.Item;
import model.Items;
import model.Order;

@WebServlet("/Cart")
public class Cart extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession hs = request.getSession();

		try
		{
			Engine engine = Engine.getInstance();

			// check if item was being added from catalog or cart is directly called
			String addToCart = request.getParameter("addToCart");

			// get cart data -- get Order object, get list of items (cartData)
			// NOTE: cartData (and Order obj) always initialized here (cannot be null)
			Order order = (Order) hs.getAttribute("order");
			List<Item> cartData;
			if (order == null) // an Order obj DNE
			{
				order = new Order(); // create one

				// create a list of items, and store in this Order obj
				List<Item> itemList = new ArrayList<Item>();
				Items items = new Items(itemList);
				order.setItems(items);
				// save the Order obj to session
				hs.setAttribute("order", order);
			}
			cartData = order.getItems().getItemList();

			// if update button is clicked
			String update = request.getParameter("update");

			// retrieve and set returnURL for Continue Shopping button
			String returnURL = request.getParameter("returnURL");
			if (returnURL == null || returnURL.equals(""))
				request.setAttribute("returnURL", "Catalog");
			else
				request.setAttribute("returnURL", request.getParameter("returnURL"));

			if (addToCart != null)
			{
				String catalogQty = request.getParameter("quantity");
				String itemId = request.getParameter("itemID");

				engine.validateQty(catalogQty);
				Item b = engine.doCart(itemId, catalogQty); // get updated item

				// find match in cart
				boolean matchFound = false;
				int matchingIndex = 0;
				for (int i = 0; i < cartData.size(); i++) // iterate through list of items
				{
					Item item = cartData.get(i); // get item i in cart

					// match ID numbers of cart items with the one being added
					if (itemId.equals(item.getNumber()))
					{
						matchFound = true;
						matchingIndex = i;
						break;
					}
				}
				if (matchFound) // there was a match
					cartData.set(matchingIndex, b); // replace
				else
					cartData.add(b);

				priceCalc(request, cartData);

			} else if (update != null)
			{
				for (int i = 0; i < cartData.size(); i++) // iterate through list of items
				{
					Item item = cartData.get(i); // get item i in cart
					String itemID = item.getNumber(); // "key"

					String quantity = request.getParameter(itemID);
					engine.validateQty(quantity);
					if (Integer.parseInt(quantity) > 0)
					{
						Item a = engine.doCart(itemID, quantity);
						cartData.set(i, a); // update (replace)
					} else if (Integer.parseInt(quantity) == 0)
					{
						cartData.remove(i); // remove
					}
				}
				priceCalc(request, cartData);
			}

			if (cartData == null || cartData.size() == 0) // if (cartData == null)
			{
				// System.out.println("empty cart");
				// if cart is empty then all price calculation should be 0
				double amt = 0.0;
				request.setAttribute("emptyCart", "Your shopping cart is empty!");
				request.setAttribute("subTotal", amt);
				request.setAttribute("HST", amt);
				request.setAttribute("shippingCost", amt);
				request.setAttribute("total", amt);

				// modify order obj
				order.setSubtotal(amt);
				order.setHst(amt);
				order.setShipping(amt);
				order.setGrandTotal(amt);

			} else
			{
				// System.out.println("cart with items");
				priceCalc(request, cartData);
			}

		} catch (Exception e)
		{
			request.setAttribute("error", e.getMessage());
		}
		this.getServletContext().getRequestDispatcher("/Cart.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

	protected void priceCalc(HttpServletRequest request, List<Item> items) throws Exception
	{
		Engine engine = Engine.getInstance();
		HttpSession hs = request.getSession();
		
		//---
		hs.setAttribute("cartData", items);
		hs.setAttribute("confirmDone", "false");
		//---
		
		double subTotal = engine.doSubTotal(items);
		double ship = engine.doShippingCost(subTotal);
		double amtHst = subTotal + ship;
		double hst = engine.doHst(amtHst);

		request.setAttribute("result", items);
		request.setAttribute("subTotal", subTotal);
		request.setAttribute("HST", hst);
		request.setAttribute("shippingCost", ship);
		request.setAttribute("total", engine.doTotal(subTotal, hst, ship));

		Order order = (Order) hs.getAttribute("order");
		// modify order obj
		order.setSubtotal(subTotal);
		order.setHst(hst);
		order.setShipping(ship);
		order.setGrandTotal(engine.doTotal(subTotal, hst, ship));
	}

}
