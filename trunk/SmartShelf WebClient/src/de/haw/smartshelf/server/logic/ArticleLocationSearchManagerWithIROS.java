/*
 * $HeadURL: ArticleLocationSearchManagerWithIROS.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: Jan 25, 2008 11:30:55 AM $
 *
 * Copyright 2008 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.server.logic;

import iwork.eheap2.Event;
import iwork.eheap2.EventCallback;
import iwork.eheap2.EventHeapException;

import java.io.File;

import de.haw.smartshelf.bo.Article;
import de.haw.smartshelf.bo.ArticleLocation;
import de.haw.smartshelf.bo.Shelf;
import de.haw.smartshelf.eha.EventHeapAdapter;
import de.haw.smartshelf.eha.EventHeapAdapterConfig;
import de.haw.smartshelf.eha.events.EventFactory;
import de.haw.smartshelf.eha.events.FoundIDEventFacade;
import de.haw.smartshelf.eha.events.SearchIDEventFacade;

/**
 * This class ... Copyright (c) 2008 SmartShelf
 * 
 * @version $ Date: Jan 25, 2008 11:30:55 AM $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class ArticleLocationSearchManagerWithIROS implements EventCallback
{
	EventHeapAdapter eha;
	private IArticleLocationHolder _articleLocHolder;
	private Article _inputArticle;
	
//	private String _eventId;
	
	
	private ArticleLocationSearchManagerWithIROS() throws EventHeapException
	{
		try
		{
			this.eha = new EventHeapAdapter(new EventHeapAdapterConfig(System.getProperty("user.dir") + File.separator + "config" + File.separator + "eventheapadapter.properties"));
		}
		catch (Exception e)
		{
			throw new EventHeapException("Could not initialize EventHeapAdapter. " + e.getMessage());
		}
	}
	
	public ArticleLocationSearchManagerWithIROS(IArticleLocationHolder articleLocHolder, Article inputArticle) throws EventHeapException
	{
		this();
		
		_articleLocHolder = articleLocHolder;
		_inputArticle = inputArticle;
	}
	
	public void sendSearchRequest(Article inputArticle)
	{ 
		try
		{
			Event searchEvent = EventFactory.createSearchIDEvent();
			SearchIDEventFacade sideFacade = new SearchIDEventFacade(searchEvent);
			sideFacade.setID(inputArticle.getRfid());

			eha.putEventAndRegisterForRespponse(searchEvent, EventFactory.createFoundIDEvent(), this);
		}
		catch (EventHeapException e)
		{
			e.printStackTrace();
		}
	}

	public boolean returnEvent(Event[] events)
	{
		try {
			for (Event event : events) {
				if (FoundIDEventFacade.TYPE_NAME.equals(event.getEventType())) {
					System.out.println("RFID empfangen!");
					FoundIDEventFacade fidEvent = new FoundIDEventFacade(event);
								
					
					String shelfId = null;
					try
					{
						shelfId = fidEvent.getShelfID();
					}
					catch (Exception e)
					{
						/* workaround: events have been received twice */
						shelfId = null;
					}
					System.out.println("SHELF: " + shelfId);
					if(shelfId == null)
					{
						continue;
					}
					else 
					{
						
						//TODO: many responses from several DBs
						ArticleLocation articleLocation = _inputArticle.getArticleLocation();
						Shelf shelf = new Shelf();
						shelf.setId(shelfId);
						articleLocation.setShelf(shelf);
						articleLocation.setCell(fidEvent.getCellID());
						articleLocation.setPosition(fidEvent.getPosition());
						
						articleLocation.setArticle(_inputArticle);
						
						_articleLocHolder.setArticleLocation(articleLocation);
					}
				} else {
					System.out.println("unknown event: " + event.getEventType());
				}
			}
		} catch (EventHeapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return true; // weitere Events empfangen
	}

	public void findArticleLocation(Article inputArticle)
	{
		sendSearchRequest(inputArticle);
	}
	
}
