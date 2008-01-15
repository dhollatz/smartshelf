package de.haw.smartshelf.reader;

public abstract class AbstractReader implements ShelfReader {

	protected String port;

	protected boolean isInit;
	
	public void setPort(String port) {
		this.port = port;
	}

	protected boolean isInit() {
		return isInit;
	}

}
