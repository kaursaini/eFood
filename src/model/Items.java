package model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Items
{
	@XmlElement(name = "item")
	private List<Item> itemList;

	public Items()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public Items(List<Item> itemList)
	{
		super();
		this.itemList = itemList;
	}

	public List<Item> getItemList()
	{
		return itemList;
	}

	public void setItemList(List<Item> itemList)
	{
		this.itemList = itemList;
	}
	
}
