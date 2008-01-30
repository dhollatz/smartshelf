package de.haw.smartshelf.eha.events;

import iwork.eheap2.Event;
import iwork.eheap2.EventHeapException;

import java.io.Serializable;
import java.util.List;

//import de.haw.smartshelf.reader.tags.RFIDTag;


public class ShelfInventoryEventFacade extends SimpleEventFacade {
	
	
	public static final String TYPE_NAME       = "shelfInventoryEvent"; 
	public static final String FIELD_SHELF_ID  = "shelfID";
	public static final String FIELD_RFID_TAGS = "RFIDTags";

	
	public ShelfInventoryEventFacade(Event event) throws EventHeapException {
		super(event);
		if (!event.getEventType().equals(TYPE_NAME)) {
			throw new RuntimeException("Illegal event type:" + event.getEventType());
		}
	}
	
	public void setShelfID(String iD) throws EventHeapException {
		getEvent().setFieldValue(FIELD_SHELF_ID, iD);
	}
	
	public Object getShelfID() throws EventHeapException {
		return getEvent().getPostValue(FIELD_SHELF_ID);
	}
	
	/**
	 * Returns a List of RFIDTags.
	 * (de.haw.smartshelf.reader.tags.RFIDTag)
	 * 
	 * @param tags
	 * @throws EventHeapException
	 */
	public void setRFIDTags(String[] tags) throws EventHeapException {
		getEvent().setFieldValue(FIELD_RFID_TAGS, tags);
	}
	
	
	/**
	 * Returns a List of RFIDTags.
	 * (de.haw.smartshelf.reader.tags.RFIDTag)
	 * 
	 * @return de.haw.smartshelf.reader.tags.RFIDTag
	 * @throws EventHeapException
	 */
	@SuppressWarnings("unchecked")
	public String[] getRFIDTags() throws EventHeapException {
		return (String[]) getEvent().getPostValue(FIELD_RFID_TAGS);
	}

}
