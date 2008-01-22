package de.haw.smartshelf.shelf;

import de.haw.smartshelf.shelf.action.ShelfAction;

public class SearchAction extends ShelfAction {
	String rfid;
	String eventID;

	public SearchAction(String rfid, String eventID) {
		this.rfid = rfid;
		this.eventID = eventID;
	}

	public String getRfid() {
		return rfid;
	}

	public String getEventID() {
		return eventID;
	}

}
