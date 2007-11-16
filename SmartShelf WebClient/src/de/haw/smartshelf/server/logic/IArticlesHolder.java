/*
 * $HeadURL: IArticlesViewer.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 16.11.2007 12:40:12 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.server.logic;

import java.util.List;

import de.haw.smartshelf.bo.Article;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 16.11.2007 12:40:12 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public interface IArticlesHolder
{
	public void setArticles(List<Article> articles);
	public List<Article> getArticles();
}
