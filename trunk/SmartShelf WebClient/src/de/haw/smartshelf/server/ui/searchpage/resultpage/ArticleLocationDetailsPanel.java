/*
 * $HeadURL: ArticleLocationDetailPanel.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 16.11.2007 19:28:09 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.server.ui.searchpage.resultpage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.haw.smartshelf.bo.ArticleLocation;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 16.11.2007 19:28:09 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class ArticleLocationDetailsPanel extends Panel
{
	private static final long serialVersionUID = 1263651252095611901L;

	private ArticleLocation _articleLocation;
	
	public ArticleLocationDetailsPanel(String id)
	{
		super(id);
		init();
	}

	public ArticleLocationDetailsPanel(String id, IModel model)
	{
		super(id, model);
		init();
	}
	
	public ArticleLocationDetailsPanel(String id, ArticleLocation articleLocation)
	{
		super(id);
		_articleLocation = articleLocation;
		init();
	}	
	
	
	private void init()
	{
		try
		{
			add(new Label("rfid", _articleLocation.getArticle().getRfid()));
		}
		catch (Exception e)
		{
			add(new Label("rfid", "NONE"));
		}
		
		try
		{
			add(new Label("shelfId", _articleLocation.getShelf().getId()));
		}
		catch (Exception e)
		{
			add(new Label("shelfId", "NONE"));
		}
		
		try
		{
			add(new Label("cell", _articleLocation.getCell()));
		}
		catch (Exception e)
		{
			add(new Label("cell", "NONE"));
		}
		
		try
		{
			add(new Label("position", _articleLocation.getPosition()));
		}
		catch (Exception e)
		{
			add(new Label("position", "NONE"));
		}
	}

	public ArticleLocation getArticleLocation()
	{
		return this._articleLocation;
	}

	public void setArticleLocation(ArticleLocation articleLocation)
	{
		this._articleLocation = articleLocation;
	}
	
	


	public void reinit()
	{
		remove("rfid");
		remove("articleType");
		remove("extensionsTable");
		
		init();
	}
	
}
