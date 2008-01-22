package de.haw.smartshelf.shelf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;

import org.apache.log4j.Logger;

import de.haw.smartshelf.config.ConfigurationException;
import de.haw.smartshelf.config.ReaderConfig;
import de.haw.smartshelf.config.ShelfConfig;
import de.haw.smartshelf.facade.ShelfEventHeapAdapter;
import de.haw.smartshelf.reader.NoSuchReaderException;
import de.haw.smartshelf.reader.ReaderFactory;
import de.haw.smartshelf.reader.ShelfReader;
import de.haw.smartshelf.reader.tags.RFIDTag;
import de.haw.smartshelf.shelf.action.ShelfAction;

public class Shelf extends Observable implements Observer {

	private static final Logger LOG = Logger.getLogger(Shelf.class);
	private static final String SHELF_CONFIG = "config/shelfProperties.xml";

	protected Collection<ShelfReader> reader;
	// TODO Caching? Wie?
	protected HashMap<String, RFIDTag> cache;

	protected Collection<RFIDTag> tags;

	protected ShelfEventHeapAdapter eha;
	protected Thread shelfThread;
	private StyledDocument doc = new DefaultStyledDocument();
	private int updateInterval;
	protected RFIDTag newTag;

	public Shelf() {
		initialize();
	}

	protected void initialize() {
		ShelfConfig shelfConfig = null;
		try {
			shelfConfig = ShelfConfig.getConfig(SHELF_CONFIG);
			LOG.debug("Shelf configuration successfully obtained");
			LOG.trace(shelfConfig.toString());
			// eha = new ShelfEventHeapAdapter();
			// eha.addObserver(this);
		} catch (ConfigurationException e) {
			LOG.fatal("Configuration failed " + e.getMessage());
			throw new RuntimeException("Fatal - Configuration failed", e);
		}
		// catch (EventHeapException e) {
		// LOG.fatal("Configuration failed " + e.getMessage());
		// throw new RuntimeException("Fatal - Configuration failed", e);
		// }

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
		return searchTag(id) != null;
	}

	public RFIDTag searchTag(String id) {
		LOG.debug("Searching for tag with id: " + id);
		for (ShelfReader aReader : reader) {
			if (aReader.isTagInRange(id)) {
				LOG.debug("Tag " + id + " found");
				return aReader.searchTag(id);
			}
		}
		LOG.debug("Tag " + id + " not found");
		return null;
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
		boolean update;
		update = this.tags == null || !this.tags.containsAll(tags);
		if (update){
			for (RFIDTag tag : tags) {
				if(this.tags == null || !this.tags.contains(tag)){
					newTag = tag;
				}
			}
		}
		this.tags = tags;
		if (update) {
			setChanged();
			notifyObservers();
		}
	}

	public StyledDocument getDoc() {
		return doc;
	}

	public void setUpdateInterval(int value) {
		if (value == 0) {
			updateInterval = 10;
		} else {
			updateInterval = value < 10 ? value * value + 10 : value * value;
		}
	}

	public int getUpdateInterval() {
		return updateInterval;
	}

	public void update(Observable o, Object arg) {
		if (arg instanceof ShelfAction) {
			if (arg instanceof SearchAction) {
				SearchAction search = (SearchAction) arg;
				if (containsTag(search.getRfid())) {
					((ShelfEventHeapAdapter) o).sendTagFoundEvent(
							searchTag(search.getRfid()), search.getEventID());
				}

			}
		} else {
			LOG.warn("Observer received unknown object");
		}
	}

	public RFIDTag getNewTag() {
		return newTag;
	}
}