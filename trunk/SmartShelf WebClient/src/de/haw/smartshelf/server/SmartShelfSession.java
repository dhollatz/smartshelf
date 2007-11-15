/*
 * $HeadURL: SmartShelfSession.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 15.11.2007 16:11:05 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.server;

import java.util.ArrayList;
import java.util.List;

import de.haw.smartshelf.bo.Article;

import wicket.Request;
import wicket.protocol.http.WebApplication;
import wicket.protocol.http.WebSession;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 15.11.2007 16:11:05 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public final class SmartShelfSession extends WebSession
{
    List<Article> _articles = new ArrayList<Article>();


	/**
     * Constructor
     * 
     * @param application
     *            The application
     */
    protected SmartShelfSession(final WebApplication application)
    {
        super(application);
        
    }

    
    public List<Article> getArticles()
	{
		return this._articles;
	}

	public void setArticles(List<Article> articles)
	{
		this._articles = articles;
	}
}