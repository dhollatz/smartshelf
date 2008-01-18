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
package de.haw.smartshelf.eha.events;

import iwork.eheap2.Event;
import iwork.eheap2.EventHeapException;

public class FoundIDEventFacade extends AbstractEventFacade{
	
	public static final String TYPE_NAME  = "FoundIDEvent";
	
	public static final String FIELD_ID   = "id";
	
	public FoundIDEventFacade(Event event) throws EventHeapException {
		if (!event.getEventType().equals(TYPE_NAME)) {
			throw new RuntimeException("Illegal event type:" + event.getEventType());
		}
		this.event = event;
	}
	
	public String getID() throws EventHeapException {
		return event.getPostValueString(FIELD_ID);
	}
		
	public void setID(String id) throws EventHeapException {
		event.setFieldValue(FIELD_ID, id);
	}

}
