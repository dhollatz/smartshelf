/*
 * $HeadURL: SearchInputModel.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 14.11.2007 21:46:52 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.server.ui.searchpage;

import java.io.Serializable;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 14.11.2007 21:46:52 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class SearchInputModel implements Serializable
{
	private static final long serialVersionUID = -5554156321734308814L;

	private String _rfidProperty = "";
	private String _articleTypeProperty = "";
	private String _freeTextSearchProperty = "";
	public String getRfidProperty()
	{
		return this._rfidProperty;
	}
	public void setRfidProperty(String rfidProperty)
	{
		this._rfidProperty = rfidProperty;
	}
	public String getArticleTypeProperty()
	{
		return this._articleTypeProperty;
	}
	public void setArticleTypeProperty(String articleTypeProperty)
	{
		this._articleTypeProperty = articleTypeProperty;
	}
	public String getFreeTextSearchProperty()
	{
		return this._freeTextSearchProperty;
	}
	public void setFreeTextSearchProperty(String freeTextSearchProperty)
	{
		this._freeTextSearchProperty = freeTextSearchProperty;
	}

	public void reset()
	{
		setArticleTypeProperty("");
		setRfidProperty("");
		setFreeTextSearchProperty("");
	}
	
	@Override
	public String toString()
	{
		return "RFID: " + getRfidProperty() + " ARTICLE TYPE: " + getArticleTypeProperty();
	}
}
