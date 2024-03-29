package de.haw.smartshelf.shelf;

import iwork.eheap2.EventHeapException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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

	protected List<RFIDTag> tags;

	protected ShelfEventHeapAdapter eha;
	protected Thread shelfThread;
	private StyledDocument doc = new DefaultStyledDocument();
	private int updateInterval;
	
	protected ShelfConfig shelfConfig;

	public Shelf() {
		initialize();
	}

	protected void initialize() {
		shelfConfig = null;
		try {
			shelfConfig = ShelfConfig.getConfig(SHELF_CONFIG);
			LOG.debug("Shelf configuration successfully obtained");
			LOG.trace(shelfConfig.toString());
			eha = new ShelfEventHeapAdapter();
			eha.addObserver(this);
		} catch (ConfigurationException e) {
			LOG.fatal("Configuration failed " + e.getMessage());
			throw new RuntimeException("Fatal - Configuration failed", e);
		} catch (EventHeapException e) {
			LOG.fatal("Configuration failed " + e.getMessage());
			throw new RuntimeException("Fatal - Configuration failed", e);
		}

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

	public List<RFIDTag> getAllItems() {
		List<RFIDTag> items = new ArrayList<RFIDTag>();
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
				RFIDTag tag = aReader.searchTag(id);
				tag.setShelfID(shelfConfig.getId());
				LOG.debug("Tag " + tag.getId() + " found in shelf: " + tag.getShelfID());
				return tag;
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

	public List<RFIDTag> getTags() {
		return tags;
	}

	public void setTags(List<RFIDTag> tags) {
		boolean update;
		// update = this.tags == null || !this.tags.containsAll(tags);
		update = this.tags == null || !this.tags.equals(tags);
		this.tags = tags;
		if (update) {
			setChanged();
			notifyObservers();
			
			// TODO Stefan: Magst du das hier noch mal sauber machen? Ich bin dazu heute nicht in der Lage... :(
			String[] tagIds = new String[this.tags.size()];
			for (int i = 0; i < tagIds.length; i++) {
				tagIds[i] = this.tags.get(i).getId();
			}
			this.eha.sendShelfInventoryEvent(tagIds, shelfConfig.getId());
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
}