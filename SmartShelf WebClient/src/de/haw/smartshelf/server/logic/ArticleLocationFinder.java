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

import de.haw.smartshelf.bo.Article;
import de.haw.smartshelf.bo.ArticleLocation;
import de.haw.smartshelf.server.ui.searchpage.resultpage.ArticleLocationDetailsPanel;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 14.12.2007 20:21:00 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class ArticleLocationFinder
{
	public ArticleLocationFinder()
	{
		
	}
	
	public ArticleLocation findArticleLocation(Article inputArticle)
	{
		// sleep for 5 seconds to show the behavior
        try
        {
        	//TODO: determine article location
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
        return inputArticle.getArticleLocation();
	}
}
