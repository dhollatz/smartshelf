package de.haw.smartshelf.reader;

import java.util.Collection;

import de.haw.smartshelf.reader.tags.RFIDTag;

public interface ShelfReader {
	// Specification of a (RFID-)Reader needed
	public abstract Collection<RFIDTag> gatherTags();
}
