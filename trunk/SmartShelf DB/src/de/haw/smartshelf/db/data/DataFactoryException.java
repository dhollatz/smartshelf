/*
 * $HeadURL: DataFactoryException.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 27.10.2007 23:42:54 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.db.data;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 27.10.2007 23:42:54 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class DataFactoryException extends Exception
{
	private static final long serialVersionUID = 8304473324629214797L;

	public DataFactoryException()
	{
		super();
	}

	public DataFactoryException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public DataFactoryException(String message)
	{
		super(message);
	}

	public DataFactoryException(Throwable cause)
	{
		super(cause);
	}

	
}
