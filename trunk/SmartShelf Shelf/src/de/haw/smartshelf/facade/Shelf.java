package de.haw.smartshelf.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.vladium.utils.PropertyLoader;

import de.haw.smartshelf.reader.ShelfReader;
import de.haw.smartshelf.reader.mock.ICodeReaderMock;
import de.haw.smartshelf.reader.tags.RFIDTag;

public class Shelf {

	private static final Logger LOG = Logger.getLogger(Shelf.class);
	private static final String SHELF_PROPS = "shelf.properties";

	protected Collection<ShelfReader> reader;
	protected HashMap<String, RFIDTag> cache;

	public Shelf() {
		initialize();
	}

	protected void initialize() {
		Properties shelfProps;
		reader = new ArrayList<ShelfReader>();
		try {
			shelfProps = PropertyLoader.loadProperties(SHELF_PROPS, this
					.getClass().getClassLoader());
			LOG.debug("Number of readers: "
					+ shelfProps.getProperty("shelf.numOfReaders"));
			try {
				int numOfReaders = Integer.parseInt(shelfProps
						.getProperty("shelf.numOfReaders"));
				for (int i = 0; i < numOfReaders; i++) {
					reader.add(new ICodeReaderMock());
				}
			} catch (NumberFormatException e) {
				LOG.error(e.getMessage());
			}

		} catch (IllegalArgumentException e) {
			LOG.error(e.getMessage());
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
