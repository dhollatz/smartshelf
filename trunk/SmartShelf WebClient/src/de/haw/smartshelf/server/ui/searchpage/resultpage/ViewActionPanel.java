/*
 * $HeadURL: ActionPanel.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 16.11.2007 15:02:33 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.server.ui.searchpage.resultpage;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.haw.smartshelf.bo.Article;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 16.11.2007 15:02:33 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class ViewActionPanel extends Panel
{
	private static final long serialVersionUID = 8522001292702540628L;
	
	private IPageWithArticleSelection _pwas;
	
	/**
	 * @param id
	 *            component id
	 * @param model
	 *            model for contact
	 */
	public ViewActionPanel(String id, IModel model, IPageWithArticleSelection pwas)
	{
		super(id, model);
		_pwas = pwas;
		add(new Link(_pwas.getSelectActionLinkId())
		{
			private static final long serialVersionUID = 4487156871183918350L;

			public void onClick()
			{
				Article article = (Article)getParent().getModelObject();
				_pwas.setArticle(article);
				_pwas.refreshPage();
			}
		});
	}
}
