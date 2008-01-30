/*
 * $HeadURL: EventHeapAdapter.java $
 *
 * $Author: Dennis Hollatz $
 * $Date: 18.12.2007 16:04:40 $
 *
 * Copyright 2008 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.eha.events;

import java.util.Collection;

import iwork.eheap2.Event;
import iwork.eheap2.EventHeapException;
import iwork.eheap2.FieldValueTypes;
import de.haw.smartshelf.bo.Article;

/**
 * Die Event Factory erzeugt <tt>Event</tt>s eines Typs. 
 * Der Typ eines Events wird &uuml;ber das Feld <code>TYPE</code> des <tt>Event</tt>s bestimmt.
 * 
 * @author EventHeapServer
 *
 */
public class EventFactory {
	
	public static Event createFoundIDEvent() throws EventHeapException {
		Event event = new Event(FoundIDEventFacade.TYPE_NAME);

		event.addField(FoundIDEventFacade.FIELD_EVENT_ID, String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		event.addField(FoundIDEventFacade.FIELD_ID, String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		event.addField(FoundIDEventFacade.FIELD_TITLE,    String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		event.addField(FoundIDEventFacade.FIELD_SHELF_ID, String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		event.addField(FoundIDEventFacade.FIELD_CELL_ID,  String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		event.addField(FoundIDEventFacade.FIELD_POSITION,  String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		
		event.setTimeToLive(50000);
				
		return event;
	}
	
	public static Event createResultListEvent() throws EventHeapException {
		Event event = new Event(ResultListEventFacade.TYPE_NAME);

		event.addField(ResultListEventFacade.FIELD_EVENT_ID, String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		event.addField(ResultListEventFacade.FIELD_TITLE,    String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		event.addField(ResultListEventFacade.FIELD_LIST_XML, String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		
		event.setTimeToLive(50000);
				
		return event;
	}
	
	public static Event createSearchIDEvent() throws EventHeapException {
		Event event = new Event(SearchIDEventFacade.TYPE_NAME);

		event.addField(SearchIDEventFacade.FIELD_EVENT_ID, String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		event.addField(SearchIDEventFacade.FIELD_ID, String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		event.setTimeToLive(50000);
				
		return event;
	}
	
	public static Event createSearchItemEvent() throws EventHeapException {
		Event event = new Event(SearchItemEventFacade.TYPE_NAME);

		event.addField(SearchItemEventFacade.FIELD_EVENT_ID,    String.class,  FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		event.addField(SearchItemEventFacade.FIELD_TITLE,    String.class,  FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		event.addField(SearchItemEventFacade.FIELD_INPUT_ARTICLE,  Article.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		
		event.setTimeToLive(50000);
				
		return event;
	}
	
	public static Event createShelfInventoryEvent() throws EventHeapException {
		Event event = new Event(ShelfInventoryEventFacade.TYPE_NAME);

		event.addField(ShelfInventoryEventFacade.FIELD_SHELF_ID,  String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		event.addField(ShelfInventoryEventFacade.FIELD_RFID_TAGS, Collection.class,  FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		
		event.setTimeToLive(50000);
				
		return event;
	}
	
	protected static void setEventId(Event event) throws EventHeapException
	{
		String eventid = String.valueOf(System.currentTimeMillis()) + "_" +  String.valueOf(event.hashCode());
		event.setFieldValue(SimpleEventFacade.FIELD_EVENT_ID, eventid);
	}
}
