package de.haw.smartshelf.eha.events;

import iwork.eheap2.Event;
import iwork.eheap2.EventHeapException;

public class AbstractEventFacade
{
	public static final String FIELD_EVENT_ID   = "eventId";
	
	protected Event event;
	
	public String getEventId() throws EventHeapException {
		return event.getPostValueString(FIELD_EVENT_ID);
	}
	
	public void setEventId(String eventId) throws EventHeapException {
		event.setFieldValue(FIELD_EVENT_ID, eventId);
	}
}
