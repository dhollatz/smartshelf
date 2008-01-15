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

public class ResultListEventFacade {
	
	public static final String TYPE_NAME      = "ResultListEvent";
	public static final String FIELD_TITLE    = "title";
	public static final String FIELD_LIST_XML = "listXML";
	
	private Event event;
	
	public ResultListEventFacade(Event event) throws EventHeapException {
		if (!event.getEventType().equals(TYPE_NAME)) {
			throw new RuntimeException("Illegal event type:" + event.getEventType());
		}
		this.event = event;
	}
	
	public String getTitle() throws EventHeapException {
		return event.getPostValueString(FIELD_TITLE);
	}
	
	public void setTitle(String title) throws EventHeapException {
		event.setFieldValue(FIELD_TITLE, title);
	}
	
	public String getListXML() throws EventHeapException {
		return event.getPostValueString(FIELD_LIST_XML);
	}
	
	public void setListXML(String listXML) throws EventHeapException {
		event.setFieldValue(FIELD_LIST_XML, listXML);
	}

}
