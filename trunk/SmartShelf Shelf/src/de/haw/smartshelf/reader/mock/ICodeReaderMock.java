package de.haw.smartshelf.reader.mock;

import java.util.ArrayList;
import java.util.Collection;

import de.haw.smartshelf.reader.ShelfReader;
import de.haw.smartshelf.reader.tags.ICodeTag;
import de.haw.smartshelf.reader.tags.RFIDTag;

public class ICodeReaderMock implements ShelfReader {

	public Collection<RFIDTag> gatherTags() {
		Collection<RFIDTag> tags = new ArrayList<RFIDTag>();
		RFIDTag tag = new ICodeTag();
		tag.setId("12345");
		tags.add(tag);
		return tags;
	}

}
