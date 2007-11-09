/*
 * $HeadURL: Shelf.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 09.11.2007 12:33:57 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.bo;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 09.11.2007 12:33:57 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class Shelf
{
	private String _id;
	private String _name;
	private String _location;
	
	public String getId()
	{
		return this._id;
	}
	
	public void setId(String id)
	{
		this._id = id;
	}
	public String getName()
	{
		return this._name;
	}
	public void setName(String name)
	{
		this._name = name;
	}
	public String getLocation()
	{
		return this._location;
	}
	public void setLocation(String location)
	{
		this._location = location;
	}
}
