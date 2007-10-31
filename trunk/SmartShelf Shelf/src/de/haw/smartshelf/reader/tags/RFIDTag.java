package de.haw.smartshelf.reader.tags;

public abstract class RFIDTag {
	
	protected String id;
	protected String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String toString(){
		return type + " - id: " + id;
	}

}
