package middleware;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "order")
public class ConsolidatedOrder
{
	@XmlAttribute(name = "submitted")
	private String submitted;
	
	@XmlElement(name = "company")
	private String ourCompanyName;
	//private String id;

    private ConsolidatedItems items;

	public ConsolidatedOrder()
	{
		super();
		// TODO Auto-generated constructor stub
	}

//	public ConsolidatedOrder(String ourCompanyName, String id, String submitted, ConsolidatedItems items)
//	{
//		super();
//		this.ourCompanyName = ourCompanyName;
//		this.id = id;
//		this.submitted = submitted;
//		this.items = items;
//	}
	public ConsolidatedOrder(String ourCompanyName, String submitted, ConsolidatedItems items)
	{
		super();
		this.ourCompanyName = ourCompanyName;
		this.submitted = submitted;
		this.items = items;
	}

	public String getOurCompanyName()
	{
		return ourCompanyName;
	}

	public void setOurCompanyName(String ourCompanyName)
	{
		this.ourCompanyName = ourCompanyName;
	}

//	public String getId()
//	{
//		return id;
//	}
//
//	public void setId(String id)
//	{
//		this.id = id;
//	}

	public String getSubmitted()
	{
		return submitted;
	}

	public void setSubmitted(String submitted)
	{
		this.submitted = submitted;
	}

	public ConsolidatedItems getItems()
	{
		return items;
	}

	public void setItems(ConsolidatedItems items)
	{
		this.items = items;
	}
    
    
}
