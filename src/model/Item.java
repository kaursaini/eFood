package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/*
 * <item number="1409S381">
		<name>Brownie Ice Cream with Praline by MG</name>
		<price>7.39</price>
		<quantity>120</quantity>
		<extended>886.8</extended>
	</item>
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class Item
{
	@XmlAttribute(name = "number")
	private String number;
	private String name;
	private double price;
	private int quantity;
	private double extended;
	
	public Item()
	{
		super();
		// TODO Auto-generated constructor stub
	}
	public Item(String number, String name, double price, int quantity, double extended)
	{
		super();
		this.number = number;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.extended = extended;
	}
	
	public String getNumber()
	{
		return number;
	}
	public void setNumber(String number)
	{
		this.number = number;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public double getPrice()
	{
		return price;
	}
	public void setPrice(double price)
	{
		this.price = price;
	}
	public int getQuantity()
	{
		return quantity;
	}
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}
	public double getExtended()
	{
		return extended;
	}
	public void setExtended(double extended)
	{
		this.extended = extended;
	}
	
	@Override
	public String toString()
	{
		return "[number=" + number + ", name=" + name + ", price=" + price + ", quantity=" + quantity
				+ ", extended=" + extended + "]";
	}
}
