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
	private IArticlesHolder _articlesHolder;
	
	public ArticleSearchManager(IArticlesHolder articlesHolder)
	{
		_articlesHolder = articlesHolder;
	}
	
	public void searchArticles(Article inputArticle)
	{
		new EventManagerSimulator(_articlesHolder, inputArticle).searchArticles(inputArticle);
//		try
//		{
//			_articlesHolder.setArticles(null);
//			new ArticleSearchManagerWithIROS(_articlesHolder, inputArticle).findArticles(inputArticle);
//			while(_articlesHolder.getArticles() == null)
//			{
//				/* wait for answer from DB */
//				try
//				{
//					Thread.sleep(500);
//				}
//				catch (InterruptedException e)
//				{
//					
//				}
//			}
//		}
//		catch (EventHeapException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
