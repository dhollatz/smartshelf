/*
 * $HeadURL: Executer.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: Jan 15, 2008 8:22:48 AM $
 *
 * Copyright 2008 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.db.eventheap.adapter;

import iwork.eheap2.EventHeapException;

/**
 * This class ... Copyright (c) 2008 SmartShelf
 * 
 * @version $ Date: Jan 15, 2008 8:22:48 AM $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class Executer
{
	public static void main(String[] args) throws EventHeapException
	{
		new SmartShelfDBEventHeapAdapter();
		while(true)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
