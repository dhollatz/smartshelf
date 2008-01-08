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

public class SearchIDEvent extends Event {
	
	public static final String FIELD_TYPE     = "type";
	public static final String FIELD_TITLE    = "title";
	public static final String FIELD_TAG_ID   = "tagID";
	public static final String FIELD_SHELF_ID = "shelfID";
	public static final String FIELD_RACK_ID  = "rackID";
	
	public SearchIDEvent() throws EventHeapException {
		super(SearchIDEvent.class.getCanonicalName());
//		this.addField(FIELD_TYPE,     String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		this.addField(FIELD_TITLE,    String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		this.addField(FIELD_SHELF_ID, String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		this.addField(FIELD_RACK_ID,  String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		
		this.setTimeToLive(50000);
	}
	
	public String getType() throws EventHeapException {
		return this.getPostValueString(FIELD_TYPE);
	}
	
	public void setType(String type) throws EventHeapException {
		this.setFieldValue(FIELD_TYPE, type);
	}
	
	public String getTitle() throws EventHeapException {
		return this.getPostValueString(FIELD_TITLE);
	}
	
	public void setTitle(String title) throws EventHeapException {
		this.setFieldValue(FIELD_TITLE, title);
	}

	public String getShelfID() throws EventHeapException {
		return this.getPostValueString(FIELD_SHELF_ID);
	}
	
	public void setShelfID(String shelfID) throws EventHeapException {
		this.setFieldValue(FIELD_SHELF_ID, shelfID);
	}

	public String getRackID() throws EventHeapException {
		return this.getPostValueString(FIELD_RACK_ID);
	}
	
	public void setRackID(String rackID) throws EventHeapException {
		this.setFieldValue(FIELD_RACK_ID, rackID);
	}

}
