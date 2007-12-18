/*
 * $HeadURL: EventHeapAdapter.java $
 *
 * $Author: Dennis Hollatz $
 * $Date: 18.12.2007 16:04:40 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.eha.playground;

import iwork.eheap2.Event;

/**
 * @author dennis
 *
 */
public class EventFactory {
	private static EventFactory instance = null;
	
	
	public static EventFactory createEventFactory() {
		if (instance == null) {
			instance = new EventFactory();
		}
		
		return instance;
	}

	private EventFactory() {
		// TODO Auto-generated constructor stub
	}
	
	public Event getEvent(String Event) {
		return null;
	}
}
