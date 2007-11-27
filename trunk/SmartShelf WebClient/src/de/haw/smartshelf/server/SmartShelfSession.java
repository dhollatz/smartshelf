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

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebSession;

import de.haw.smartshelf.bo.Article;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 15.11.2007 16:11:05 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public final class SmartShelfSession extends WebSession
{
    List<Article> _articles = new ArrayList<Article>();

    public SmartShelfSession(Request request)
	{
		super(request);
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