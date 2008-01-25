/*
 * $HeadURL: SmartShelfEventHeapAdapter.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: Jan 15, 2008 7:41:07 AM $
 *
 * Copyright 2008 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.facade;

import iwork.eheap2.Event;
import iwork.eheap2.EventCallback;
import iwork.eheap2.EventHeapException;

import java.util.Observable;

import org.apache.log4j.Logger;

import de.haw.smartshelf.eha.EventHeapAdapter;
import de.haw.smartshelf.eha.EventHeapAdapterConfig;
import de.haw.smartshelf.eha.events.EventFactory;
import de.haw.smartshelf.eha.events.FoundIDEventFacade;
import de.haw.smartshelf.eha.events.SearchIDEventFacade;
import de.haw.smartshelf.reader.tags.RFIDTag;
import de.haw.smartshelf.shelf.SearchAction;
import de.haw.smartshelf.shelf.Shelf;

public class ShelfEventHeapAdapter extends Observable implements EventCallback {
	private static final Logger LOG = Logger.getLogger(Shelf.class);
	private static final String EHA_CONFIG = "config/ehaProperties.xml";
	EventHeapAdapter eha;

	public ShelfEventHeapAdapter() throws EventHeapException {
		EventHeapAdapterConfig ehaConfig = null;
		try {
			ehaConfig = EventHeapAdapterConfig.getConfig(EHA_CONFIG);
			LOG.debug("EventHeapAdapter configuration successfully obtained");
			LOG.trace(ehaConfig.toString());
			eha = new EventHeapAdapter(ehaConfig);
		} catch (Exception e) {
			LOG.fatal("Configuration failed " + e.getMessage());
			throw new EventHeapException(
					"Could not initialize EventHeapAdapter. " + e.getMessage());
		}
		eha.registerForEvent(EventFactory.createSearchIDEvent(), this);
	}

	public boolean returnEvent(Event[] events) {
		try {
			for (Event event : events) {
				if (SearchIDEventFacade.TYPE_NAME.equals(event.getEventType())) {
					LOG.debug("Received " + event.getEventType() + ": " + event);
					SearchIDEventFacade searchIDEvent = new SearchIDEventFacade(
							event);
					String id = searchIDEvent.getID();
					if (id.length() > 0) {
						setChanged();
						notifyObservers(new SearchAction(id, searchIDEvent
								.getEventId()));
					}
				} else {
					LOG.trace("Event unknown: " + event.getEventType());
				}
			}
		} catch (EventHeapException e) {
			LOG.error("Error while receiving events");
		}

		return true; // weitere Events empfangen
	}

	public void sendTagFoundEvent(RFIDTag tagFound, String eventID) {
		try {
			Event fIDEvent = EventFactory.createFoundIDEvent();
			FoundIDEventFacade foundIDEvent = new FoundIDEventFacade(fIDEvent);

			/* set the same event id as from the received event */
			foundIDEvent.setEventId(eventID);
			foundIDEvent.setShelfID("1");
			foundIDEvent.setCellID("1");
			foundIDEvent.setID(tagFound.getId());
			foundIDEvent.setPosition("middle");
			foundIDEvent.setTitle(tagFound.getType());
			
			LOG.debug("Sending " + fIDEvent.getEventType()+ ": " + fIDEvent);

			eha.putEvent(fIDEvent);

		} catch (EventHeapException e) {
			LOG.error("Error while sending event");
		}
	}

}
