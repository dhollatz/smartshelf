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

import de.haw.smartshelf.bo.Article;
import iwork.eheap2.Event;
import iwork.eheap2.EventHeapException;
import iwork.eheap2.FieldValueTypes;

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
		event.setTimeToLive(50000);
		
		setEventId(event);
		
		return event;
	}
	
	public static Event createResultListEvent() throws EventHeapException {
		Event event = new Event(ResultListEventFacade.TYPE_NAME);

		event.addField(ResultListEventFacade.FIELD_EVENT_ID, String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		event.addField(ResultListEventFacade.FIELD_TITLE,    String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		event.addField(ResultListEventFacade.FIELD_LIST_XML, String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		
		event.setTimeToLive(50000);
		
		setEventId(event);
		
		return event;
	}
	
	public static Event createSearchIDEvent() throws EventHeapException {
		Event event = new Event(SearchIDEventFacade.TYPE_NAME);

		event.addField(SearchIDEventFacade.FIELD_EVENT_ID, String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		event.addField(SearchIDEventFacade.FIELD_TITLE,    String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		event.addField(SearchIDEventFacade.FIELD_SHELF_ID, String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		event.addField(SearchIDEventFacade.FIELD_RACK_ID,  String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		
		event.setTimeToLive(50000);
		
		setEventId(event);
		
		return event;
	}
	
	public static Event createSearchItemEvent() throws EventHeapException {
		Event event = new Event(SearchItemEventFacade.TYPE_NAME);

		event.addField(SearchItemEventFacade.FIELD_EVENT_ID,    String.class,  FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		event.addField(SearchItemEventFacade.FIELD_TITLE,    String.class,  FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		event.addField(SearchItemEventFacade.FIELD_INPUT_ARTICLE,  Article.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		
		event.setTimeToLive(50000);
		
		setEventId(event);
		
		return event;
	}
	
	private static void setEventId(Event event) throws EventHeapException
	{
		String eventid = String.valueOf(System.currentTimeMillis()) + "_" +  String.valueOf(event.hashCode());
		event.setFieldValue(AbstractEventFacade.FIELD_EVENT_ID, eventid);
	}
}
