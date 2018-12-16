package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/*
 * <customer account="xyz4567">
		<name>Nine Corp</name>
	</customer>
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class Customer
{
	@XmlAttribute(name = "account")
	private String account;
	private String name;
	
	public Customer()
	{
		super();
		// TODO Auto-generated constructor stub
	}
	public Customer(String account, String name)
	{
		super();
		this.account = account;
		this.name = name;
	}
	
	public String getAccount()
	{
		return account;
	}
	public void setAccount(String account)
	{
		this.account = account;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	@Override
	public String toString()
	{
		return "[account=" + account + ", name=" + name + "]";
	}
}
