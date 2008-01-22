package de.haw.smartshelf.eha.events;

import iwork.eheap2.Event;
import iwork.eheap2.EventHeapException;

public class SimpleEventFacade
{
	public static final String FIELD_EVENT_ID   = "eventId";
	
	private Event event;
	
	public SimpleEventFacade(Event event) {
		this.event = event;
	}
	
	public Event getEvent() {
		return event;
	}
	
	public void setEvent(Event event) {
		this.event = event;
	}
	
	public String getEventId() throws EventHeapException {
		return event.getPostValueString(FIELD_EVENT_ID);
	}
	
	public void setEventId(String eventId) throws EventHeapException {
		event.setFieldValue(FIELD_EVENT_ID, eventId);
	}
}
