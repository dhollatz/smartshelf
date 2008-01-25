/*
 * $HeadURL: ArticleSearchManager.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 16.11.2007 12:34:50 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.server.logic;

import java.util.ArrayList;

import iwork.eheap2.EventHeapException;
import de.haw.smartshelf.bo.Article;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 16.11.2007 12:34:50 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class ArticleSearchManager
{
	public static long TIME_TO_WAIT = 5000;
	private IArticlesHolder _articlesHolder;
	
	public ArticleSearchManager(IArticlesHolder articlesHolder)
	{
		_articlesHolder = articlesHolder;
	}
	
	public void searchArticles(Article inputArticle)
	{
//		new EventManagerSimulator(_articlesHolder, inputArticle).searchArticles(inputArticle);
		try
		{
			_articlesHolder.setArticles(null);
			new ArticleSearchManagerWithIROS(_articlesHolder, inputArticle).findArticles(inputArticle);
			
			long startTime = System.currentTimeMillis();
			long currentTime = System.currentTimeMillis();
			while(_articlesHolder.getArticles() == null)
			{
				/* wait for answer from DB */
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException e)
				{
					
				}
				currentTime = System.currentTimeMillis();
		        if((currentTime - startTime) > TIME_TO_WAIT)
		        {
		        	/* timeout */
		        	_articlesHolder.setArticles(new ArrayList<Article>());
		        }
			}
		}
		catch (EventHeapException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
