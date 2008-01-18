/*
 * $HeadURL: ArticleSearchManagerWithIROS.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: Jan 8, 2008 3:55:20 AM $
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
import java.util.List;

import de.haw.smartshelf.bo.Article;
import de.haw.smartshelf.eha.EventHeapAdapter;
import de.haw.smartshelf.eha.EventHeapAdapterConfig;
import de.haw.smartshelf.eha.events.EventFactory;
import de.haw.smartshelf.eha.events.ResultListEventFacade;
import de.haw.smartshelf.eha.events.SearchItemEventFacade;

/**
 * This class ... Copyright (c) 2008 SmartShelf
 * 
 * @version $ Date: Jan 8, 2008 3:55:20 AM $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class ArticleSearchManagerWithIROS implements EventCallback
{
	EventHeapAdapter eha;
	private IArticlesHolder _articlesHolder;
	private Article _inputArticle;
	
	private String _eventId;
	
	
	private ArticleSearchManagerWithIROS() throws EventHeapException
	{
		try
		{
			this.eha = new EventHeapAdapter(new EventHeapAdapterConfig(System.getProperty("user.dir") + File.separator + "config" + File.separator + "eventheapadapter.properties"));
		}
		catch (Exception e)
		{
			throw new EventHeapException("Could not initialize EventHeapAdapter. " + e.getMessage());
		}
		this.eha.registerForEvent(EventFactory.createFoundIDEvent(), this);
		this.eha.registerForEvent(EventFactory.createResultListEvent(), this);
//		this.eha.registerForEvent(EventFactory.createSearchItemEvent(), this);
	}
	
	public ArticleSearchManagerWithIROS(IArticlesHolder articlesHolder, Article inputArticle) throws EventHeapException
	{
		this();
		
		_articlesHolder = articlesHolder;
		_inputArticle = inputArticle;
	}
	
	public void sendSearchRequest(Article inputArticle)
	{ 
		try
		{
			Event searchEvent = EventFactory.createSearchItemEvent();
			SearchItemEventFacade sieFacade = new SearchItemEventFacade(searchEvent);
			sieFacade.setArticle(inputArticle);
//			searchEvent.setFieldValue("test.key", "TEST.VALUE");
			
			_eventId = sieFacade.getEventId();
			eha.putEvent(searchEvent);
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
				if (ResultListEventFacade.TYPE_NAME.equals(event.getEventType())) {
					System.out.println("Liste empfangen!");
					ResultListEventFacade rlEvent = new ResultListEventFacade(event);
									
					try
					{
						String resultEvenId = rlEvent.getEventId();
						if(!_eventId.equals(resultEvenId))
						{
							/* this event is not for me */
							continue;
						}
					}
					catch (Exception e1)
					{
						continue;
					}
					
					List<Article> articles;
					try
					{
						articles = rlEvent.getArticles();
					}
					catch (Exception e)
					{
						/* workaround: events have been received twice */
						articles = null;
					}
					System.out.println("ARTICLES: " + articles);
					if(articles == null)
					{
						continue;
					}
					else 
					{
						_articlesHolder.setArticles(articles);
					}
					//System.out.println(rlEvent.toStringComplete());
				} else 	if (SearchItemEventFacade.TYPE_NAME.equals(event.getEventType())) {
//					//SearchItemEvent siEvent = (SearchItemEvent) event;
//					// TODO etwas mit der Liste machen ;)
//					System.out.println("SearchItem empfangen!");
//					System.out.println(event.toStringComplete());
//					System.out.println("ARTICLE: " + new SearchItemEventFacade(event).getArticle());
////					String string = (String)event.getPostValue("test.key");
////					System.out.println("string: " + string);
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

	public void findArticles(Article inputArticle)
	{
		sendSearchRequest(inputArticle);
	}
	
}
