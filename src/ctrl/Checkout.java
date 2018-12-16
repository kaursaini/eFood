package ctrl;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Customer;
import model.Engine;
import model.Item;
import model.ItemBean;
import model.Items;
import model.Order;

@WebServlet("/Checkout")
public class Checkout extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession s = request.getSession();
		
		// get cart data
		Order order = (Order) s.getAttribute("order");
		
		// retrieve and set returnURL for Continue Shopping button (TODO: will probably be lost on Auth redirect, fix if time, else stick with Catalog)
		String returnURL = request.getParameter("returnURL");
		if(returnURL == null || returnURL.equals(""))
			request.setAttribute("returnURL", "Catalog");
		else
			request.setAttribute("returnURL", request.getParameter("returnURL"));
		
//		// set Header attribute (TODO: not part of requirement, do if time)
//		if(s.getAttribute("authorizedUser")==null)
//			request.setAttribute("loggedIn", false);
//		else
//			request.setAttribute("loggedIn", (boolean)s.getAttribute("authorizedUser"));
		
		
		//System.out.println("authorizedUser null or false >>>> " + (s.getAttribute("authorizedUser")==null || (boolean)s.getAttribute("authorizedUser")==false));
		// >>> STEP 1: ensure client logged in
		//-------------------------------------
		if(order==null || order.getItems().getItemList().size()==0) // cart empty
		{
			this.getServletContext().getRequestDispatcher("/Cart").forward(request, response);
		}
		else if(s.getAttribute("authorizedUser")==null || (boolean)s.getAttribute("authorizedUser")==false)
		{
			//the servlet redirects the user (via response.sendRedirect)
			//and provide a parameter named 'back' containing the URL of this servlet
			String loginURL = "https://www.eecs.yorku.ca/~roumani/servers/auth/oauth.cgi" 
								+ "?back=" + request.getRequestURL();
			response.sendRedirect(loginURL);
			
			s.setAttribute("authorizedUser", true);
			//System.out.println("REDIRECT COMPLETE");
		}
		else // order != null and user has logged in
		{
			if(request.getParameter("cancel") != null) // Cancel Order clicked
			{
				// delete cart object from session and go back to Catalog
				s.removeAttribute("order");
				//---
				s.removeAttribute("cartData");
				s.setAttribute("confirmDone", "false");
				//---
				this.getServletContext().getRequestDispatcher("/Catalog").forward(request, response);
			}	
			else if(request.getParameter("confirm") != null) // Confirm Order clicked
			{
				//HashMap<String, String> cartData = (HashMap) s.getAttribute("cartData");
				// >>> STEP 3: followed by acknowledgement order accepted and being processed
				//-------------------------------------
				// * create the XML P/O file on disk
				//System.out.println("confirm");
				try
				{
					Engine engine = Engine.getInstance();
					
					//---
					s.setAttribute("confirmDone", "true");
					s.setAttribute("cartData", order.getItems().getItemList());
					//---

					order.setCustomer((Customer)s.getAttribute("customer")); // set customer data
					File result = engine.doCheckout(order); // create xml file
					s.removeAttribute("order"); // delete processed order from session
					
					// show acknowledgement page and provide filename so it can make PO url	
					request.setAttribute("view", result.getName());
					this.getServletContext().getRequestDispatcher("/OrderAccepted.jspx").forward(request, response);
				}
				catch(Exception e)
				{
					request.setAttribute("error", e.getMessage());
					//System.out.println("error >> " + e.getMessage());
				}
			}
			else // Cancel nor Confirm clicked
			{
				if(request.getParameter("user")!=null && request.getParameter("name")!=null) // fresh from logging in, save data
				{
					String account_code = request.getParameter("user");
					String account_name = request.getParameter("name");
					Customer customer = new Customer(account_code, account_name);
					s.setAttribute("customer", customer);
				}
				
				// >>> STEP 2: then display a confirmation screen
				//-------------------------------------
				// NOTE: order == null (cart empty) checked in previous (else) if
				request.setAttribute("result", order);
				
				//---
				s.setAttribute("confirmDone", "false");
				s.setAttribute("cartData", order.getItems().getItemList());
				//---
				
				this.getServletContext().getRequestDispatcher("/Checkout.jspx").forward(request, response);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
}
