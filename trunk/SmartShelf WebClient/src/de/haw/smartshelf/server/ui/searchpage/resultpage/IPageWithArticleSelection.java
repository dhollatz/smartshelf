/*
 * $HeadURL: IPageWithArticleSelection.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 16.11.2007 15:04:09 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.server.ui.searchpage.resultpage;

import java.util.List;

import de.haw.smartshelf.bo.Article;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 16.11.2007 15:04:09 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public interface IPageWithArticleSelection
{
	public void setArticle(de.haw.smartshelf.bo.Article selectedArticle);
	public void refreshPage();
	public String getSelectActionLinkId();
	public List<Article> getArticles();
}
