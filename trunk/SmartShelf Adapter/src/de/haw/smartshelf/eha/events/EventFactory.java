/**
 * 
 */
package de.haw.smartshelf.eha.events;

import iwork.eheap2.Event;

/**
 * @author dennis
 *
 */
public class EventFactory {
	private static EventFactory instance = null;
	
	
	public static EventFactory createEventFactory() {
		if (instance == null) {
			instance = new EventFactory();
		}
		
		return instance;
	}

	private EventFactory() {
		// TODO Auto-generated constructor stub
	}
	
	public Event getEvent(String Event) {
		return null;
	}
}
