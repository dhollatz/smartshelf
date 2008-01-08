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
package de.haw.smartshelf.eha.playground;

import iwork.eheap2.Event;
import iwork.eheap2.EventCallback;
import iwork.eheap2.EventHeap;
import iwork.eheap2.EventHeapException;

/**
 * @author dennis
 *
 */
public class EventReceiver implements EventCallback{

	public EventReceiver() throws EventHeapException {
		EventHeap eh = new EventHeap("192.168.14.240");
		
		Event template = EventStore.instance().getDummyTemplate();
		Event[] templates = new Event[1];
		templates[0] = template;
		
		// register for a specific event template
		// this will only be matched when EventType = MyEventType
		// and Age = 27 and Name = Tico Ballagas
		eh.registerForEvents(templates, this);
	}
	
	/**
	 * @param args
	 * @throws EventHeapException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws EventHeapException, InterruptedException {
		new EventReceiver();
		while (true) {
			Thread.sleep(500);
		}
	}

	public boolean returnEvent(Event[] retEvents) {
		for (int i = 0; i < retEvents.length; i++) {
			System.out.println("received: " + retEvents[i].toStringComplete());
		}
		return true;
	}

}
