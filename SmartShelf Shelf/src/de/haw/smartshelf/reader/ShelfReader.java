package de.haw.smartshelf.reader;

import java.util.Collection;

import de.haw.smartshelf.reader.tags.RFIDTag;

public interface ShelfReader {
	// Specification of a (RFID-)Reader needed
	public abstract Collection<RFIDTag> gatherTags();

	public abstract boolean isTagInRange(String id);
	
	public abstract RFIDTag searchTag(String id);

	public abstract void setPort(String port);
	
	public abstract void initialize();
}
