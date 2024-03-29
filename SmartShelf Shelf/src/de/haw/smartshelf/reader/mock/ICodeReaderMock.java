package de.haw.smartshelf.reader.mock;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import de.haw.smartshelf.reader.AbstractReader;
import de.haw.smartshelf.reader.tags.ICodeTag;
import de.haw.smartshelf.reader.tags.RFIDTag;

public class ICodeReaderMock extends AbstractReader {

	private static final Logger LOG = Logger.getLogger(ICodeReaderMock.class);

	private Collection<String> range;

	public ICodeReaderMock() {
		initialize();
	}

	public void initialize() {
		range = new ArrayList<String>();
		range.add("ICodeReaderMockTag1");
		range.add("ICodeReaderMockTag2");
		range.add("ICodeReaderMockTag3");
	}

	public Collection<RFIDTag> gatherTags() {
		LOG.debug("gathering tags...");
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
