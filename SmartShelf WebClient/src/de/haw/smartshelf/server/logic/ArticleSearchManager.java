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

import java.util.List;

import de.haw.smartshelf.bo.Article;
import de.haw.smartshelf.db.bo.BoFactory;

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
//		new EventManagerSimulator(_articlesHolder, inputArticle).searchArticles(inputArticle);
		try
		{
			new ArticleSearchManagerWithIROS(_articlesHolder, inputArticle).findArticles(inputArticle);
		}
		catch (EventHeapException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
