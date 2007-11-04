package de.haw.smartshelf.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import de.haw.smartshelf.reader.ShelfReader;
import de.haw.smartshelf.reader.mock.ICodeReaderMock;
import de.haw.smartshelf.reader.tags.RFIDTag;

public class Shelf {

	protected Collection<ShelfReader> reader;
	protected HashMap<String, RFIDTag> cache;

	public Shelf() {
		initialize();
	}

	protected void initialize() {
		reader = new ArrayList<ShelfReader>();
		reader.add(new ICodeReaderMock());
	}

	public Collection<RFIDTag> getAllItems() {
		Collection<RFIDTag> items = new ArrayList<RFIDTag>();
		for (ShelfReader aReader : reader) {
			items = aReader.gatherTags();
			// TODO: organize cache
		}
		return items;
	}

}
