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
	
	public static final String TYPE_NAME      = "FoundIDEvent";
	
	public static final String FIELD_ID   = "id";
	public static final String FIELD_TITLE    = "title";
	public static final String FIELD_TAG_ID   = "tagID";
	public static final String FIELD_SHELF_ID = "shelfID";
	public static final String FIELD_CELL_ID  = "cellID";
	public static final String FIELD_POSITION  = "position";

	
	public FoundIDEventFacade(Event event) throws EventHeapException {
		super(event);
		if (!event.getEventType().equals(TYPE_NAME)) {
			throw new RuntimeException("Illegal event type:" + event.getEventType());
		}
	}
	
	public String getID() throws EventHeapException {
		return getEvent().getPostValueString(FIELD_ID);
	}
		
	public void setID(String id) throws EventHeapException {
		getEvent().setFieldValue(FIELD_ID, id);
	}
	
	public String getTitle() throws EventHeapException {
		return getEvent().getPostValueString(FIELD_TITLE);
	}
	
	public void setTitle(String title) throws EventHeapException {
		getEvent().setFieldValue(FIELD_TITLE, title);
	}

	public String getShelfID() throws EventHeapException {
		return getEvent().getPostValueString(FIELD_SHELF_ID);
	}
	
	public void setShelfID(String shelfID) throws EventHeapException {
		getEvent().setFieldValue(FIELD_SHELF_ID, shelfID);
	}

	public String getCellID() throws EventHeapException {
		return getEvent().getPostValueString(FIELD_CELL_ID);
	}
	
	public void setCellID(String cellID) throws EventHeapException {
		getEvent().setFieldValue(FIELD_CELL_ID, cellID);
	}

	public String getPosition() throws EventHeapException {
		return getEvent().getPostValueString(FIELD_POSITION);
	}
	
	/**
	 * sets position
	 * @param position: for example 'left' oder 'right'
	 * @throws EventHeapException
	 */
	public void setPosition(String position) throws EventHeapException {
		getEvent().setFieldValue(FIELD_POSITION, position);
	}
}

