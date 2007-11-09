/*
 * $HeadURL: ArticleExtension.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 09.11.2007 12:33:48 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.bo;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 09.11.2007 12:33:48 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class ArticleExtension
{
	private String _name;
	private String _value;

	public ArticleExtension(String name, String value)
	{
		_name = name;
		_value = value;
	}

	public String getName()
	{
		return this._name;
	}

	public void setName(String name)
	{
		this._name = name;
	}

	public String getValue()
	{
		return this._value;
	}

	public void setValue(String value)
	{
		this._value = value;
	}

	@Override
	public String toString()
	{
		String result = "";

		result += "EXTENSION NAME:" + getName() + " VALUE: " + getValue();

		return result;
	}
}
