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

import de.haw.smartshelf.eha.events.SimpleEventFacade;
import iwork.eheap2.Event;
import iwork.eheap2.EventCallback;
import iwork.eheap2.EventHeap;
import iwork.eheap2.EventHeapException;

public class EventHeapAdapter {	
	
	public static int event_counter = 0;
	
	private IEventHeapAdapterConfig _config;
	private EventHeap eh = null;
	
	public EventHeapAdapter(IEventHeapAdapterConfig config) throws EventHeapException
	{
		_config = config;
		this.reconnect();
	}
	
	/**
	 * Send an <tt>Event</tt> to the <tt>EventHeap</tt>.
	 * 
	 * @param event The Event that will be send.
	 * @throws EventHeapException If an error occurs with EventHeap communication.
	 */
	public void putEvent(Event event) throws EventHeapException {
		eh.putEvent(event);
	}
	
	/**
	 * Put an <tt>Event</tt> to the <tt>EventHeap</tt> and registers for the answer <tt>Event</tt>.
	 * <p>
	 * The sending client is responsible for setting the appropriate response template. In this template only 
	 * identifying fields should be set. 
	 * </p><p>  
	 * This method generates a client specific ID for each event. Both on the Event and the Template this ID will be set. 
	 * @see SimpleEventFacade#setEventId(String).
	 * 
	 * @param event The event to be send.
	 * @param responseTemplate The template for the response.
	 * @param callback The callback class to be registered
	 * @return The generated ID for this communication
	 * 
	 * @throws EventHeapException If an error occurs with EventHeap communication.
	 */
	public String putEventAndRegisterForRespponse(Event event, Event responseTemplate, EventCallback callback) throws EventHeapException {
		String msgID = _config.getClientName() + "-" + System.currentTimeMillis() + "-" + event_counter++;
		
		SimpleEventFacade template = new SimpleEventFacade(responseTemplate);
		template.setEventId(msgID);
		// TODO dho Cleanup Thread for outdated registrations
		this.registerForEvent(template.getEvent(), callback);

		SimpleEventFacade simpleEventFacade = new SimpleEventFacade(event);
		simpleEventFacade.setEventId(msgID);
		
		/* sending event */
		putEvent(simpleEventFacade.getEvent());
		
		return msgID;
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
		Event[] templateEvents = new Event[] {templateEvent};
		this.eh.registerForEvents(templateEvents, callback);
	}
	
	/**
	 * Reconnect to the event heap server.
	 */
	public void reconnect() {
		this.eh = new EventHeap(_config.getEventHeapURL());
	}
}
