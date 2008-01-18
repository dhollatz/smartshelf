/*
 * $HeadURL: SimulatorThread.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 16.11.2007 13:01:27 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.server.logic;

import java.util.List;

import de.haw.smartshelf.bo.Article;
import de.haw.smartshelf.db.bo.BoFactory;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 16.11.2007 13:01:27 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class EventManagerSimulator extends Thread
{
	private IArticlesHolder _articlesHolder;
	private Article _inputArticle;
	
	public EventManagerSimulator(IArticlesHolder articlesHolder, Article inputArticle)
	{
		_articlesHolder = articlesHolder;
		_inputArticle = inputArticle;
	}

	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				sleep(3000);
				searchArticles(_inputArticle);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void searchArticles(Article inputArticle)
	{
		List<Article> foundArticles = BoFactory.getInstance().findArticle(inputArticle);
		_articlesHolder.setArticles(foundArticles);
		try
		{
			Thread.sleep(3000);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
