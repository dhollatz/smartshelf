/*
 * $HeadURL: ResultPage.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 15.11.2007 11:17:36 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.server.ui.searchpage.resultpage;

import java.util.ArrayList;
import java.util.List;

import wicket.markup.html.panel.FeedbackPanel;
import de.haw.smartshelf.bo.Article;
import de.haw.smartshelf.bo.ArticleExtension;
import de.haw.smartshelf.commonutils.Util;
import de.haw.smartshelf.db.bo.BoFactory;
import de.haw.smartshelf.server.SmartShelfSession;
import de.haw.smartshelf.server.ui.mainpage.MainPage;
import de.haw.smartshelf.server.ui.searchpage.SearchInputModel;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 15.11.2007 11:17:36 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class ResultPage extends MainPage
{
	private static final long serialVersionUID = -6759662221146641593L;

	SearchInputModel _searchInputModel = null;
	
	private ResultPage()
	{
		super();
		setPageTitle("SmartShelf Search Result Page");
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
	}
	
	public ResultPage(SearchInputModel searchInputModel)
	{
		this();
		_searchInputModel = searchInputModel;
		
		searchInDb();
	}
	
	private void searchInDb()
	{
		Article inputArt = createInputArticle();
		List<Article> foundArticles = BoFactory.getInstance().findArticle(inputArt);
		((SmartShelfSession)getSession()).setArticles(foundArticles);
	}
	
	public Article createInputArticle()
	{
		Article inputArticle = new Article(_searchInputModel.getRfidProperty());
		inputArticle.setArticleType(_searchInputModel.getArticleTypeProperty());
		List<ArticleExtension> articleExtensions = new ArrayList<ArticleExtension>();
		if(!Util.isEmpty(_searchInputModel.getFreeTextSearchProperty()))
		{
			articleExtensions.add(new ArticleExtension("", _searchInputModel.getFreeTextSearchProperty()));
		}
		
		inputArticle.setArticleExtensions(articleExtensions);
	
		return inputArticle;
	}
	
	/**
	 * Get downcast session object for easy access by subclasses
	 * 
	 * @return The session
	 */
	public java.util.List<Article> getArticles()
	{
		return ((SmartShelfSession)getSession()).getArticles();
	}
	
}
