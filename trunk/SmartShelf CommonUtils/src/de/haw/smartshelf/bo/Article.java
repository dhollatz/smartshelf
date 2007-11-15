/*
 * $HeadURL: Article.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 09.11.2007 12:33:38 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.bo;

import java.io.Serializable;
import java.util.List;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 09.11.2007 12:33:38 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class Article implements Serializable
{
	private static final long serialVersionUID = 2767174680787699639L;

	private String _rfid;
	private String _articleType;
	private List<ArticleExtension> _articleExtensions;
	private ArticleLocation _articleLocation;

	private Article()
	{

	}

	public Article(String rfid)
	{
		this();
		_rfid = rfid;
	}

	public Article(String rfid, String articleType)
	{
		this(rfid);
		_articleType = articleType;
	}

	public String getRfid()
	{
		return this._rfid;
	}

	public String getArticleType()
	{
		return this._articleType;
	}

	public void setArticleType(String articleType)
	{
		this._articleType = articleType;
	}

	public List<ArticleExtension> getArticleExtensions()
	{
		return this._articleExtensions;
	}

	public void setArticleExtensions(List<ArticleExtension> articleExtensions)
	{
		this._articleExtensions = articleExtensions;
	}

	public ArticleLocation getArticleLocation()
	{
		return this._articleLocation;
	}

	public void setArticleLocation(ArticleLocation articleLocation)
	{
		this._articleLocation = articleLocation;
		articleLocation.setArticle(this);
	}

	@Override
	public String toString()
	{
		String result = "";

		result += "ARTICLE RFID: " + getRfid() + " TYPE: " + getArticleType();
		result += " " + getArticleExtensions();

		return result;
	}

	public void setRfid(String rfid)
	{
		this._rfid = rfid;
	}
}