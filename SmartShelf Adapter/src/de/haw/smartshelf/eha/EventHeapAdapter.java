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
package de.haw.smartshelf.eha;

import iwork.eheap2.Event;
import iwork.eheap2.EventCallback;
import iwork.eheap2.EventHeap;
import iwork.eheap2.EventHeapException;
import de.haw.smartshelf.properties.GlobalProperties;

public class EventHeapAdapter {
	
	public static final String RETURN_TO_CLIENT = "RETURN_TO_CLIENT";
	
	private EventHeap eh = null;

	private EventHeapAdapter() {
		this.reconnect();
	}
	
	public void putEvent(Event event) throws EventHeapException {
		eh.putEvent(this.cloneAndPrepare(event));
	}
	
	/**
	 * Register for events. 
	 * 
	 * @param templateEvent The Template to register for.
	 * @param callback The callback class. This one is called, when an event is received.
	 *  
	 * @throws EventHeapException If anything goes wrong.
	 */
	public void registerForEvent(Event templateEvent, EventCallback callback) throws EventHeapException {
		// Template-Event kopieren 
		Event newTemplateEvent = this.cloneAndPrepare(templateEvent);
		
		Event[] templateEvents = new Event[] {newTemplateEvent};
		this.eh.registerForEvents(templateEvents, callback);
	}
	
	/**
	 * Reconnect to the event heap server.
	 */
	public void reconnect() {
		this.eh = new EventHeap(GlobalProperties.getEventHeapURL());
	}
	
	/**
	 * Copies the given <tt>Event</tt> and adds the senders name.
	 * The client field is Named <code>RETURN_TO_CLIENT</code>.
	 * 
	 * @param event The event to be cloned
	 * @return The cloned event with the client name attached.
	 * 
	 * @throws EventHeapException
	 */
	private Event cloneAndPrepare(Event event) throws EventHeapException {
		Event newEvent = new Event(event.getEventType());
		for (String field : event.getFieldNames()) {
			newEvent.addField(
					field, 
					event.getFieldClass(field), 
					event.getPostValueType(field), 
					event.getTemplateValueType(field));
		}
		
		// Absender einfuegen
		newEvent.addField(RETURN_TO_CLIENT, GlobalProperties.getClientName());
		
		return newEvent;
	}
}
