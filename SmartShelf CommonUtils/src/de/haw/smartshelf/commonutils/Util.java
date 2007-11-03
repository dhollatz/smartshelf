/*
 * $HeadURL: Util.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 03.11.2007 13:01:59 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.commonutils;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 03.11.2007 13:01:59 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class Util
{
	public static boolean isEmpty(String string)
	{
		return string == null || string.trim().equals("");
	}
}
