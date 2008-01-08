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
import iwork.eheap2.FieldValueTypes;

public class SearchItemEvent extends Event {
	
	public static final String FIELD_TYPE     = "type";
	public static final String FIELD_TITLE    = "title";
	public static final String FIELD_ARTICLE = "article";
	
	public SearchItemEvent() throws EventHeapException {
		super(SearchItemEvent.class.getCanonicalName());
//		this.addField(FIELD_TYPE,     String.class,  FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		this.addField(FIELD_TITLE,    String.class,  FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		this.addField(FIELD_ARTICLE,  Article.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
		
		this.setTimeToLive(50000);
	}
	
	public String getType() throws EventHeapException {
		return this.getPostValueString(FIELD_TYPE);
	}
		
	public void setType(String type) throws EventHeapException {
		this.setFieldValue(FIELD_TYPE, type);
	}
	
	public String getTitle() throws EventHeapException {
		return this.getPostValueString(FIELD_TITLE);
	}
	
	public void setTitle(String title) throws EventHeapException {
		this.setFieldValue(FIELD_TITLE, title);
	}
	
	public String getProperty(String propertyName) throws EventHeapException {
		return this.getPostValueString(propertyName);
	}
	
	public void setProperty(String propertyName, String propertyValue) throws EventHeapException {
		this.setFieldValue(propertyName, propertyValue);
	}

	public void setArticle(Article article) throws EventHeapException {
		this.setFieldValue(FIELD_ARTICLE, article);
	}
}
