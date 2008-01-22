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

public class FoundIDEventFacade extends SimpleEventFacade{
	
	public static final String TYPE_NAME      = "SearchID";
	
	public static final String FIELD_TITLE    = "title";
	public static final String FIELD_TAG_ID   = "tagID";
	public static final String FIELD_SHELF_ID = "shelfID";
	public static final String FIELD_RACK_ID  = "rackID";

	
	public FoundIDEventFacade(Event event) throws EventHeapException {
		super(event);
		if (!event.getEventType().equals(TYPE_NAME)) {
			throw new RuntimeException("Illegal event type:" + event.getEventType());
		}
	}
	
	public String getTitle() throws EventHeapException {
		return getEvent().getPostValueString(FIELD_TITLE);
	}
	
	public void setTitle(Event event, String title) throws EventHeapException {
		event.setFieldValue(FIELD_TITLE, title);
	}

	public String getShelfID(Event event) throws EventHeapException {
		return getEvent().getPostValueString(FIELD_SHELF_ID);
	}
	
	public void setShelfID(String shelfID) throws EventHeapException {
		getEvent().setFieldValue(FIELD_SHELF_ID, shelfID);
	}

	public String getRackID() throws EventHeapException {
		return getEvent().getPostValueString(FIELD_RACK_ID);
	}
	
	public void setRackID(String rackID) throws EventHeapException {
		getEvent().setFieldValue(FIELD_RACK_ID, rackID);
	}

}

