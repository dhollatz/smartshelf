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

import wicket.markup.html.basic.Label;
import wicket.markup.html.panel.FeedbackPanel;
import de.haw.smartshelf.bo.Article;
import de.haw.smartshelf.bo.ArticleExtension;
import de.haw.smartshelf.commonutils.Util;
import de.haw.smartshelf.server.logic.ArticleSearchManager;
import de.haw.smartshelf.server.logic.IArticlesHolder;
import de.haw.smartshelf.server.ui.mainpage.MainPage;
import de.haw.smartshelf.server.ui.searchpage.SearchInputModel;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 15.11.2007 11:17:36 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class ResultPage extends MainPage implements IArticlesHolder, IPageWithArticleSelection
{
	private static final long serialVersionUID = -6759662221146641593L;

	SearchInputModel _searchInputModel = null;
	
	
	private Article _selectedArticle;
	private ArticleDetailsPanel _articleDetailsPanel;
	private AjaxDataTablePanel _ajaxDataTablePanel;
	private ArticleLocationDetailsPanel _articleLocationDetailsPanel;
	protected List<Article> _articles = new ArrayList<Article>();

	private ResultPage()
	{
		super();
		
	}
	
	public ResultPage(SearchInputModel searchInputModel)
	{
		this();
		_searchInputModel = searchInputModel;

		searchInDb();
		initPage();
	}
	
	private void initPage()
	{
		setPageTitle("SmartShelf Search Result Page");
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		
		_ajaxDataTablePanel = new AjaxDataTablePanel("foundArticlesPanel", this);
		add(_ajaxDataTablePanel);
		
//		_articleDetailsPanel = new ArticleDetailsPanel("articleDetailsPanel");
//		add(_articleDetailsPanel);
		add(new Label("articleDetailsPanel", ""));
		
//		_articleLocationDetailsPanel = new ArticleLocationDetailsPanel("articleLocationDetailsPanel");
//		add(_articleLocationDetailsPanel);
		add(new Label("articleLocationDetailsPanel", ""));
	}

	

	private void searchInDb()
	{
		Article inputArt = createInputArticle();
		new ArticleSearchManager(this).searchArticles(inputArt);
	}

	public Article createInputArticle()
	{
		Article inputArticle = new Article(_searchInputModel.getRfidProperty());
		inputArticle.setArticleType(_searchInputModel.getArticleTypeProperty());
		List<ArticleExtension> articleExtensions = new ArrayList<ArticleExtension>();
		if (!Util.isEmpty(_searchInputModel.getFreeTextSearchProperty()))
		{
			articleExtensions.add(new ArticleExtension("", _searchInputModel.getFreeTextSearchProperty()));
		}

		inputArticle.setArticleExtensions(articleExtensions);

		return inputArticle;
	}

	public List<Article> getArticles()
	{
		return this._articles;
	}

	public void setArticles(List<Article> articles)
	{
		this._articles = articles;
		refresh();
	}

	private void refresh()
	{
		// TODO Auto-generated method stub
		
	}

	protected void refreshArticleDetailsPanel()
	{
		if(_articleDetailsPanel != null)
		{
			_articleDetailsPanel.setArticle(_selectedArticle);
			_articleDetailsPanel.reinit();
		}
		else 
		{
			remove("articleDetailsPanel");
			_articleDetailsPanel = new ArticleDetailsPanel("articleDetailsPanel");
			_articleDetailsPanel.setArticle(_selectedArticle);
			add(_articleDetailsPanel);
			_articleDetailsPanel.reinit();
		}
	}
	
	public void refreshPage()
	{
		refreshArticleDetailsPanel();
	}


	public void setArticle(Article selectedArticle)
	{
		_selectedArticle = selectedArticle;
	}

	public String getSelectActionLinkId()
	{
		return "select";
	}
	public String getFindActionLinkId()
	{
		return "determineLocation";
	}

	public void determineArticleLocation(Article article)
	{
		// TODO determine location
		remove("articleLocationDetailsPanel");
		_articleLocationDetailsPanel = new ArticleLocationDetailsPanel("articleLocationDetailsPanel");
		add(_articleLocationDetailsPanel);
	}

	

}
