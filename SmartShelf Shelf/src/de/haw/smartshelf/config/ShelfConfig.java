package de.haw.smartshelf.config;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class ShelfConfig {
	@Attribute
	private int id;

	@ElementList
	private List<ReaderConfig> readerConfigs;

	public int getId() {
		return id;
	}

	public List<ReaderConfig> getReaderConfigs() {
		return readerConfigs;
	}

	public void createDummy() {
		readerConfigs = new ArrayList<ReaderConfig>();
		readerConfigs.add(new ReaderConfig());
	}

}
