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

import iwork.eheap2.Event;
import iwork.eheap2.EventHeapException;

import java.io.Serializable;
import java.util.List;

import de.haw.smartshelf.bo.Article;

public class ResultListEventFacade extends AbstractEventFacade{
	
	public static final String TYPE_NAME      = "ResultListEvent";
	public static final String FIELD_TITLE    = "title";
	public static final String FIELD_LIST_XML = "listXML";
	public static final String FIELD_ARTICLES_LIST = "articlesList";
	
	
	public ResultListEventFacade(Event event) throws EventHeapException {
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
	
	public String getListXML() throws EventHeapException {
		return event.getPostValueString(FIELD_LIST_XML);
	}
	
	public void setListXML(String listXML) throws EventHeapException {
		event.setFieldValue(FIELD_LIST_XML, listXML);
	}
	
	public void setArticles(List<Article> articles) throws EventHeapException
	{
		event.setFieldValue(FIELD_ARTICLES_LIST, (Serializable) articles);
	}
	
	public List<Article> getArticles() throws EventHeapException
	{
		return (List<Article>) event.getPostValue(FIELD_ARTICLES_LIST);
	}

}
