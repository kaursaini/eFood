package middleware;

public class ConsolidatedItem
{
	private String number;
//	private String name;
	private int quantity;
	
	public ConsolidatedItem()
	{
		super();
		// TODO Auto-generated constructor stub
	}

//	public ConsolidatedItem(String number, String name, int quantity)
//	{
//		super();
//		this.number = number;
//		//this.name = name;
//		this.quantity = quantity;
//	}

	public String getNumber()
	{
		return number;
	}

	public ConsolidatedItem(String number, int quantity)
	{
		super();
		this.number = number;
		this.quantity = quantity;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

//	public String getName()
//	{
//		return name;
//	}
//
//	public void setName(String name)
//	{
//		this.name = name;
//	}

	public int getQuantity()
	{
		return quantity;
	}

	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}

	@Override
	public String toString()
	{
		return "ConsolidatedItem [number=" + number + ", quantity=" + quantity + "]";
	}

//	@Override
//	public String toString()
//	{
//		return "[number=" + number + ", name=" + name + ", quantity=" + quantity + "]";
//	}
	

}
