package de.haw.smartshelf.eha.events;

import iwork.eheap2.Event;
import iwork.eheap2.EventHeapException;
import iwork.eheap2.FieldValueTypes;

public class SearchIDEventTemplate extends Event {
	
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_ID   = "id";
	
	public SearchIDEventTemplate() throws EventHeapException {
		super("SearchID");
		this.addField("Type", String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		this.addField("TagID", String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
	}
	
	public void setType(String type) throws EventHeapException {
		this.setFieldValue(FIELD_TYPE, type);
	}
	
	public void setID(String id) throws EventHeapException {
		this.setFieldValue(FIELD_ID, id);
	}

}
