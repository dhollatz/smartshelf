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
import iwork.eheap2.FieldValueTypes;

public class FoundIDEvent extends Event {
	
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_ID   = "id";
	
	public FoundIDEvent() throws EventHeapException {
		super(FoundIDEvent.class.getCanonicalName());
		
//		this.addField("Type", String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		this.addField("TagID", String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		
		this.setTimeToLive(50000);
	}
	
	public String getType() throws EventHeapException {
		return this.getPostValueString(FIELD_TYPE);
	}
		
	public void setType(String type) throws EventHeapException {
		this.setFieldValue(FIELD_TYPE, type);
	}
	
	public String getID() throws EventHeapException {
		return this.getPostValueString(FIELD_ID);
	}
		
	public void setID(String id) throws EventHeapException {
		this.setFieldValue(FIELD_ID, id);
	}

}
