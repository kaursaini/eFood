package model;

/* Java Bean = record
 * - private attributes
 * - constructor (default and attrib init)
 * - getters and setters
 * - toString 
 */

public class ItemBean
{
	// map member variable in the class to columns in table
	private String number;
	private String name;
	private double price;
	private int quantity;
	private int onOrder;
	private int reOrder;
	private String categoryId;
	private String supplierId;
	private double costPrice;
	private String unit;

	public ItemBean()
	{
		super();
	}

	public ItemBean(String number, String name, double price, int quantity, int onOrder, int reOrder, String categoryId,
			String supplierId, double costPrice, String unit)
	{
		super();
		this.number = number;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.onOrder = onOrder;
		this.reOrder = reOrder;
		this.categoryId = categoryId;
		this.supplierId = supplierId;
		this.costPrice = costPrice;
		this.unit = unit;
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

	public int getOnOrder()
	{
		return onOrder;
	}

	public void setOnOrder(int onOrder)
	{
		this.onOrder = onOrder;
	}

	public int getReOrder()
	{
		return reOrder;
	}

	public void setReOrder(int reOrder)
	{
		this.reOrder = reOrder;
	}

	public String getCategoryId()
	{
		return categoryId;
	}

	public void setCategoryId(String categoryId)
	{
		this.categoryId = categoryId;
	}

	public String getSupplierId()
	{
		return supplierId;
	}

	public void setSupplierId(String supplierId)
	{
		this.supplierId = supplierId;
	}

	public double getCostPrice()
	{
		return costPrice;
	}

	public void setCostPrice(double costPrice)
	{
		this.costPrice = costPrice;
	}

	public String getUnit()
	{
		return unit;
	}

	public void setUnit(String unit)
	{
		this.unit = unit;
	}

	@Override
	public String toString()
	{
		return "[number=" + number + ", name=" + name + ", price=" + price + ", quantity=" + quantity + ", onOrder="
				+ onOrder + ", reOrder=" + reOrder + ", categoryId=" + categoryId + ", supplierId=" + supplierId
				+ ", costPrice=" + costPrice + ", unit=" + unit + "]";
	}
}
