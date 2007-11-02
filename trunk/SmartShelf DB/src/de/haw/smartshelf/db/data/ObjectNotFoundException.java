/*
 * $HeadURL: ObjectNotFoundException.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 02.11.2007 16:01:52 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.db.data;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 02.11.2007 16:01:52 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class ObjectNotFoundException extends DataFactoryException
{
	private static final long serialVersionUID = 6757421842951640901L;

	public ObjectNotFoundException()
	{
		super();
	}

	public ObjectNotFoundException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ObjectNotFoundException(String message)
	{
		super(message);
	}

	public ObjectNotFoundException(Throwable cause)
	{
		super(cause);
	}

	
}
