/*
 * $HeadURL: DetachableArticleExtensionModel.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 15.11.2007 22:11:30 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.server.ui.searchpage.resultpage;

import org.apache.wicket.model.AbstractReadOnlyModel;

import de.haw.smartshelf.bo.ArticleExtension;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 15.11.2007 22:11:30 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class DetachableArticleExtensionModel extends AbstractReadOnlyModel
{
	private static final long serialVersionUID = 1883050934001331195L;
	
	private ArticleExtension _articleExtension;
	
	public DetachableArticleExtensionModel(ArticleExtension articleExtension)
	{
		this._articleExtension = articleExtension;
	}

	@Override
	public Object getObject()
	{
		return _articleExtension;
	}
	
}
