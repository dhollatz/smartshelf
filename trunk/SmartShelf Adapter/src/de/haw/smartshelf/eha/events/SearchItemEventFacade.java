/*
 * $HeadURL: EventHeapAdapter.java $
 *
 * $Author: Dennis Hollatz $
 * $Date: 18.12.2007 16:04:40 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.eha.events;

import de.haw.smartshelf.bo.Article;
import iwork.eheap2.Event;
import iwork.eheap2.EventHeapException;

public class SearchItemEventFacade extends AbstractEventFacade {
	
	public static final String TYPE_NAME      = "SearchItemEvent";
	
	public static final String FIELD_TITLE    = "title";
	public static final String FIELD_INPUT_ARTICLE  = "article";
	
	public SearchItemEventFacade(Event event) throws EventHeapException {
		if (!event.getEventType().equals(TYPE_NAME)) {
			throw new RuntimeException("Illegal event type:" + event.getEventType());
		}
		this.event = event;
	}
	
	public String getTitle() throws EventHeapException {
		return event.getPostValueString(FIELD_TITLE);
	}
	
	public void setTitle(String title) throws EventHeapException {
		event.setFieldValue(FIELD_TITLE, title);
	}
	
	public String getProperty(String propertyName) throws EventHeapException {
		return event.getPostValueString(propertyName);
	}
	
	public void setProperty(String propertyName, String propertyValue) throws EventHeapException {
		event.setFieldValue(propertyName, propertyValue);
	}

	public void setArticle(Article article) throws EventHeapException {
		event.setFieldValue(FIELD_INPUT_ARTICLE, article);
	}
	
	public Object getArticle() throws EventHeapException {
		return event.getPostValue(FIELD_INPUT_ARTICLE);
	}
}
