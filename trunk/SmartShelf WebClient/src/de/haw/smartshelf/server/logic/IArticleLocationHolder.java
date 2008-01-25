/*
 * $HeadURL: IArticleLocationHolder.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: Jan 25, 2008 11:43:58 AM $
 *
 * Copyright 2008 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.server.logic;

import java.util.List;

import de.haw.smartshelf.bo.Article;
import de.haw.smartshelf.bo.ArticleLocation;

/**
 * This class ... Copyright (c) 2008 SmartShelf
 * 
 * @version $ Date: Jan 25, 2008 11:43:58 AM $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public interface IArticleLocationHolder
{
	public void setArticleLocation(ArticleLocation articleLocation);
	public ArticleLocation getArticleLocation();
}
