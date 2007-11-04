package de.haw.smartshelf.reader.tags;

public class ICodeTag extends RFIDTag {

	protected static String TYPE = "ICodeTag";

	public ICodeTag(String id) {
		super(id);
	}

	protected void initialize() {
		this.type = TYPE;
	}
}
