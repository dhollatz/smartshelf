package de.haw.smartshelf.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.load.Persister;

@Root
public class ShelfConfig {
	@Attribute
	private String id;

	@ElementList
	private List<ReaderConfig> readerConfigs;

	public String getId() {
		return id;
	}

	public List<ReaderConfig> getReaderConfigs() {
		return readerConfigs;
	}

	public void createDummy() {
		readerConfigs = new ArrayList<ReaderConfig>();
		readerConfigs.add(new ReaderConfig());
	}

	public static ShelfConfig getConfig(String fileName)
			throws ConfigurationException {
		Serializer serializer = new Persister();
		File source = new File(fileName);
		ShelfConfig shelfConfig = null;
		try {
			shelfConfig = serializer.read(ShelfConfig.class, source);
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
		return shelfConfig;
	}

	public String toString() {
		String aString = "<Shelf> id: " + id;
		for (ReaderConfig readerConfig : readerConfigs) {
			aString = aString + " - " + readerConfig.toString();
		}
		return aString;
	}

}
