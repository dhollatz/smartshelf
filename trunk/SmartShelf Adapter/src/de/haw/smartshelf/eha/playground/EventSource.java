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
import iwork.eheap2.EventHeap;
import iwork.eheap2.EventHeapException;

public class EventSource {
	public static void main(String[] args) throws EventHeapException {
		EventHeap eh = new EventHeap("192.168.14.240");
		
		Event template = EventStore.instance().getDummyTemplate();
		Event e = new Event(template.getEventType());
		e.addField("AGE", new Integer(27));
		e.addField("NAME", "Tico Ballagas");
		
		eh.putEvent(e);
	}
}
