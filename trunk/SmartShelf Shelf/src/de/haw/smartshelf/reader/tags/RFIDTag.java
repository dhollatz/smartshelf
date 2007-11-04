package de.haw.smartshelf.reader.tags;

public abstract class RFIDTag {

	protected String id;
	protected String type;
	protected static String TYPE = "Common RFID Tag";

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

}
