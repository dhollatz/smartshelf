package de.haw.smartshelf.reader;

import java.util.Collection;

import de.haw.smartshelf.reader.tags.RFIDTag;

public abstract class AbstractReader implements ShelfReader {

	protected String port;

	protected boolean isInit;

	public void setPort(String port) {
		this.port = port;
	}

	protected boolean isInit() {
		return isInit;
	}

	public boolean isTagInRange(String id) {
		return searchTag(id) != null;
	}

	public RFIDTag searchTag(String id) {
		Collection<RFIDTag> tags = gatherTags();
		if (tags.size() > 0) {
			for (RFIDTag tag : tags) {
				if (tag.getId().equals(id)) {
					return tag;
				}
			}
		}
		return null;
	}
}
