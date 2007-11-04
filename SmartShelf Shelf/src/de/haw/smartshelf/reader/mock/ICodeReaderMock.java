package de.haw.smartshelf.reader.mock;

import java.util.ArrayList;
import java.util.Collection;

import de.haw.smartshelf.reader.ShelfReader;
import de.haw.smartshelf.reader.tags.ICodeTag;
import de.haw.smartshelf.reader.tags.RFIDTag;

public class ICodeReaderMock implements ShelfReader {

	private Collection<String> range;

	public ICodeReaderMock() {
		initialize();
	}

	protected void initialize() {
		range = new ArrayList<String>();
		range.add("12345");
		range.add("00001");
		range.add("AnotherID");
	}

	public Collection<RFIDTag> gatherTags() {
		Collection<RFIDTag> tags = new ArrayList<RFIDTag>();
		for (String id : range) {
			RFIDTag tag = new ICodeTag(id);
			tags.add(tag);
		}
		return tags;
	}

	public boolean isTagInRange(String id) {
		for (String tagID : range) {
			if (tagID == id) {
				return true;
			}
		}
		return false;
	}

}
