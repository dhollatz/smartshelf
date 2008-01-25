package de.haw.smartshelf.reader.tags;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public abstract class RFIDTag {

	@Attribute
	protected String id = "";

	protected String type = "";
	protected static String TYPE = "Common RFID Tag";

	@Attribute
	protected String imageURL = "";
	
	public RFIDTag() {
		initialize();
	}

	public RFIDTag(String id) {
		this();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String toString() {
		return type + " - id: " + id;
	}

	protected void initialize() {
		this.type = TYPE;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RFIDTag) {
			return id.equals(((RFIDTag) obj).getId());
		}
		return false;
	}

	public String getType() {
		return type;
	}

}
