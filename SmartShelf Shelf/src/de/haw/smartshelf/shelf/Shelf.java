package de.haw.smartshelf.shelf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import de.haw.smartshelf.config.ConfigurationException;
import de.haw.smartshelf.config.ReaderConfig;
import de.haw.smartshelf.config.ShelfConfig;
import de.haw.smartshelf.reader.NoSuchReaderException;
import de.haw.smartshelf.reader.ReaderFactory;
import de.haw.smartshelf.reader.ShelfReader;
import de.haw.smartshelf.reader.tags.RFIDTag;

public class Shelf {

	private static final Logger LOG = Logger.getLogger(Shelf.class);
	// private static final String SHELF_PROPS = "shelf.properties";
	private static final String SHELF_CONFIG = "config/shelfProperties.xml";

	protected Collection<ShelfReader> reader;
	protected HashMap<String, RFIDTag> cache;

	public Shelf() {
		initialize();
	}

	protected void initialize() {
		ShelfConfig shelfConfig = null;
		try {
			shelfConfig = ShelfConfig.getConfig(SHELF_CONFIG);
		} catch (ConfigurationException e) {
			LOG.fatal("Configuration failed " + e.getMessage());
			throw new RuntimeException("Fatal - Configuration failed", e);
		}
		LOG.debug("Configuration successfully obtained");
		LOG.trace(shelfConfig.toString());

		reader = new ArrayList<ShelfReader>();

		for (ReaderConfig readerConfig : shelfConfig.getReaderConfigs()) {
			try {
				reader.add(ReaderFactory.getInstance().createReader(
						readerConfig));
				LOG.debug("Created reader: " + readerConfig);
			} catch (NoSuchReaderException e) {
				LOG.warn("Reader type not supported for config: "
						+ readerConfig);
			}
		}
	}

	public Collection<RFIDTag> getAllItems() {
		Collection<RFIDTag> items = new ArrayList<RFIDTag>();
		for (ShelfReader aReader : reader) {
			items = aReader.gatherTags();
		}
		return items;
	}

}
