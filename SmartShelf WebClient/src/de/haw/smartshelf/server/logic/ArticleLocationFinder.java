/*
 * $HeadURL: ArticleLocationFinder.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 14.12.2007 20:21:00 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.server.logic;

import iwork.eheap2.EventHeapException;
import de.haw.smartshelf.bo.Article;
import de.haw.smartshelf.bo.ArticleLocation;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 14.12.2007 20:21:00 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class ArticleLocationFinder implements IArticleLocationHolder
{
	public static long TIME_TO_WAIT = 5000;
	
	private ArticleLocation _articleLocation = null;
	public ArticleLocationFinder()
	{
		
	}
	
	public ArticleLocation findArticleLocation(Article inputArticle)
	{
		ArticleLocation emptyArticleLocation = new ArticleLocation();
		emptyArticleLocation.setArticle(inputArticle);
		try
		{
			new ArticleLocationSearchManagerWithIROS(this, inputArticle).findArticleLocation(inputArticle);
		}
		catch (EventHeapException e1)
		{
			e1.printStackTrace();
			return emptyArticleLocation;
		}
		
		long startTime = System.currentTimeMillis();
		long currentTime = System.currentTimeMillis();
		while(_articleLocation == null)
		{
			try
	        {
	            Thread.sleep(100);
	        }
	        catch (InterruptedException e)
	        {
	            throw new RuntimeException(e);
	        }
	        currentTime = System.currentTimeMillis();
	        if((currentTime - startTime) > TIME_TO_WAIT)
	        {
	        	return emptyArticleLocation;
	        }
		}
        
        return _articleLocation;
	}

	public ArticleLocation getArticleLocation()
	{
		return _articleLocation;
	}

	public void setArticleLocation(ArticleLocation articleLocation)
	{
		_articleLocation = articleLocation;	
	}
}
