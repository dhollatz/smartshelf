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
import de.haw.smartshelf.bo.Article;
import de.haw.smartshelf.eha.EventHeapAdapter;
import de.haw.smartshelf.eha.events.ResultListEvent;
import de.haw.smartshelf.eha.events.FoundIDEvent;
import de.haw.smartshelf.eha.events.SearchItemEvent;
import de.haw.smartshelf.eha.events.SearchIDEvent;

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
	
	
	private ArticleSearchManagerWithIROS() throws EventHeapException
	{
		this.eha = new EventHeapAdapter();
		this.eha.registerForEvent(new FoundIDEvent(), this);
		this.eha.registerForEvent(new SearchItemEvent(), this);
	}
	
	public ArticleSearchManagerWithIROS(IArticlesHolder articlesHolder, Article inputArticle) throws EventHeapException
	{
		this();
		
		_articlesHolder = articlesHolder;
		_inputArticle = inputArticle;
	}
	
	public void searchRequest(Article inputArticle)
	{ 
		try
		{
			SearchItemEvent searchEvent = new SearchItemEvent();
			//searchEvent.setArticle(inputArticle);
			eha.putEvent(searchEvent);
		}
		catch (EventHeapException e)
		{
			e.printStackTrace();
		}
	}

	public boolean returnEvent(Event[] events)
	{
		for (Event event : events)
		{
			if (event instanceof ResultListEvent) {
				ResultListEvent rlEvent = (ResultListEvent) event;
				// TODO etwas mit der Liste machen ;)
				System.out.println("Liste empfangen!");
				System.out.println(rlEvent.toStringComplete());
			} else 	if (event instanceof SearchItemEvent) {
				SearchItemEvent siEvent = (SearchItemEvent) event;
				// TODO etwas mit der Liste machen ;)
				System.out.println("SearchItem empfangen!");
				System.out.println(siEvent.toStringComplete());
			} 
		}
		
		return true; // weitere Events empfangen
	}

	public void findArticles(Article inputArticle)
	{
		searchRequest(inputArticle);
	}
	
}
