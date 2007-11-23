package de.haw.smartshelf.reader;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractReaderFactory {

	protected List<AbstractReader> reader = new ArrayList<AbstractReader>();

	public AbstractReaderFactory() {
		this.createReader();
	}

	public List<AbstractReader> getReader() {
		return reader;
	}

	protected abstract void createReader();

}
