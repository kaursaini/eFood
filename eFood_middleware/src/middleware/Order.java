package middleware;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

/*
<order id="120" submitted="2018-11-29">
	<customer account="xyz4567">
		<name>Nine Corp</name>
	</customer>
	<items>
		<item number="0905A708">
			<name>J0 Chicken Meat</name>
			<price>5.02</price>
			<quantity>100</quantity>
			<extended>502.0</extended>
		</item>
		<item number="1409S381">
			...(omitted for space)
		</item>
	</items>
	<total>1388.80</total>
	<shipping>0</shipping>
	<HST>180.54</HST>
	<grandTotal>1569.34</grandTotal>
</order>
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "order")
public class Order
{
	//<order id="120" submitted="2018-11-29">
	@XmlAttribute(name = "id")
	private String id;
	@XmlAttribute(name = "submitted")
	private String submitted;
	
    private Customer customer;
    private Items items;
    @XmlElement(name = "total")
    private double subtotal;
    private double shipping;
    @XmlElement(name = "HST")
    private double hst;
    private double grandTotal;
    
	public Order()
	{
		super();
		// TODO Auto-generated constructor stub
	}
	public Order(String id, String submitted, Customer customer, Items items, double subtotal, double shipping,
			double hst, double grandTotal)
	{
		super();
		this.id = id;
		this.submitted = submitted;
		this.customer = customer;
		this.items = items;
		this.subtotal = subtotal;
		this.shipping = shipping;
		this.hst = hst;
		this.grandTotal = grandTotal;
	}
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getSubmitted()
	{
		return submitted;
	}
	public void setSubmitted(String submitted)
	{
		this.submitted = submitted;
	}
	public Customer getCustomer()
	{
		return customer;
	}
	public void setCustomer(Customer customer)
	{
		this.customer = customer;
	}
	public Items getItems()
	{
		return items;
	}
	public void setItems(Items items)
	{
		this.items = items;
	}
	public double getSubtotal()
	{
		return subtotal;
	}
	public void setSubtotal(double subtotal)
	{
		this.subtotal = subtotal;
	}
	public double getShipping()
	{
		return shipping;
	}
	public void setShipping(double shipping)
	{
		this.shipping = shipping;
	}
	public double getHst()
	{
		return hst;
	}
	public void setHst(double hst)
	{
		this.hst = hst;
	}
	public double getGrandTotal()
	{
		return grandTotal;
	}
	public void setGrandTotal(double grandTotal)
	{
		this.grandTotal = grandTotal;
	}
}
