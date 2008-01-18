package de.haw.smartshelf.shelf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;

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
	// TODO Caching? Wie?
	protected HashMap<String, RFIDTag> cache;

	protected Collection<RFIDTag> tags;

	protected Thread shelfThread;
	private StyledDocument doc = new DefaultStyledDocument();
	private int updateInterval;;

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
			items.addAll(aReader.gatherTags());
		}
		if (LOG.isTraceEnabled()) {
			for (RFIDTag tag : items) {
				LOG.trace(tag.getId());
			}
		}
		return items;
	}

	public boolean containsTag(String id) {
		LOG.debug("Searching for tag with id: " + id);
		for (ShelfReader aReader : reader) {
			if (aReader.isTagInRange(id)) {
				LOG.debug("Tag " + id + " found");
				return true;
			}
		}
		LOG.debug("Tag " + id + " not found");
		return false;
	}

	public void startInventoryLoop() {
		if (shelfThread == null || !shelfThread.isAlive()) {
			shelfThread = new ShelfThread(this);
			shelfThread.start();
		}
	}

	public void stopInventoryLoop() {
		if (shelfThread.isAlive()) {
			shelfThread.interrupt();
		}
	}

	public Collection<RFIDTag> getTags() {
		return tags;
	}

	public void setTags(Collection<RFIDTag> tags) {
		this.tags = tags;
	}

	public StyledDocument getDoc() {
		return doc;
	}

	public void setUpdateInterval(int value) {
		if(value == 0){
			updateInterval = 10;
		}else if (value >= 50){
			updateInterval = value * 20;
		}else{
			updateInterval = value * 10;
		}
	}
	
	public int getUpdateInterval(){
		return updateInterval;
	}

}