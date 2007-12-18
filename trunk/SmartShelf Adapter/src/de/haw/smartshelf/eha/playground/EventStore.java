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
import iwork.eheap2.EventHeapException;
import iwork.eheap2.FieldValueTypes;


/**
 * @author dennis
 *
 */
public class EventStore {

	private static EventStore es = null;
	
	private Event dummyTemplate = null;
	
	private EventStore() throws EventHeapException {
		this.dummyTemplate = new Event("dummyType");
		dummyTemplate.addField("AGE", Integer.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		dummyTemplate.addField("NAME", String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
				
	}
	
	public static EventStore instance() throws EventHeapException {
		if (es == null) {
			es = new EventStore();
		}
		
		return es;
	}
	
	public Event getDummyTemplate() {
		return dummyTemplate;
	}
}
