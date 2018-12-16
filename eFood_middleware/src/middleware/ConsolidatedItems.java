package middleware;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ConsolidatedItems
{
	@XmlElement(name = "item")
	private List<ConsolidatedItem> itemList;

	public ConsolidatedItems()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public ConsolidatedItems(List<ConsolidatedItem> itemList)
	{
		super();
		this.itemList = itemList;
	}

	public List<ConsolidatedItem> getItemList()
	{
		return itemList;
	}

	public void setItemList(List<ConsolidatedItem> itemList)
	{
		this.itemList = itemList;
	}
}
