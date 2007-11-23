package de.haw.smartshelf.eha;

import iwork.eheap2.Event;
import iwork.eheap2.EventHeap;
import iwork.eheap2.EventHeapException;

import java.io.Serializable;
import java.util.Map;

import de.haw.smartshelf.eha.events.EventFactory;

public class EventHeapAdapter {
	private static EventHeapAdapter instance = null;
	
	private EventHeap eh = null;

	public EventHeapAdapter(String eventHeapHostString) {
		this.eh = new EventHeap(eventHeapHostString);
	}
	
//	public static EventHeapAdapter instance() {
//		if (instance == null) {
//			instance = new EventHeapAdapter();
//		}
//		
//		return instance;
//	}
	
	public boolean putEvent(String name, Map<String, Serializable> values) {
		Event event = EventFactory.createEventFactory().getEvent(name);
		
		// Schlechter Stil... ich werde mich bessern.... ^^
		try {
			for (String key : values.keySet()) {
				event.addField(key, values.get(key));
			}
		
			eh.putEvent(event);
			
		} catch (EventHeapException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
}
