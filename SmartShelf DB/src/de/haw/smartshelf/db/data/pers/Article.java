package de.haw.smartshelf.db.data.pers;

// Generated 28.10.2007 11:58:49 by Hibernate Tools 3.2.0.b11

import java.util.HashSet;
import java.util.Set;

/**
 * Article generated by hbm2java
 */
public class Article implements java.io.Serializable
{
	private static final long serialVersionUID = -8004614662418351473L;
	
	private int rfid;
	private String articleType;
	private Set articleExtensionses = new HashSet(0);

	public Article()
	{
	}

	public Article(int rfid)
	{
		this.rfid = rfid;
	}

	public Article(int rfid, String articleType, Set articleExtensionses)
	{
		this.rfid = rfid;
		this.articleType = articleType;
		this.articleExtensionses = articleExtensionses;
	}

	public int getRfid()
	{
		return this.rfid;
	}

	public void setRfid(int rfid)
	{
		this.rfid = rfid;
	}

	public String getArticleType()
	{
		return this.articleType;
	}

	public void setArticleType(String articleType)
	{
		this.articleType = articleType;
	}

	public Set getArticleExtensionses()
	{
		return this.articleExtensionses;
	}

	public void setArticleExtensionses(Set articleExtensionses)
	{
		this.articleExtensionses = articleExtensionses;
	}
	
	@Override
	public String toString()
	{
		return "RFID: " + getRfid() + " TYPE: " + getArticleType();
	}
}