/*
 * $HeadURL: DataExporter.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 02.11.2007 18:38:07 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.db.data.xml;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jdom.Element;

import de.haw.smartshelf.db.data.pers.Article;
import de.haw.smartshelf.db.data.pers.ArticleExtension;
import de.haw.smartshelf.db.data.pers.ArticleLocation;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 02.11.2007 18:38:07 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class DataExporter
{
	public Element export(List<Article> articles)
	{
		Element articlesElm = new Element(Constants.ELEMENT_ARTICLES);
		for (Article article : articles)
		{
			Element articleElm = export(article);
			articlesElm.addContent(articleElm);
		}
		
		return articlesElm;
	}
	
	public Element export(Article article)
	{
		Element articleElm = new Element(Constants.ELEMENT_ARTICLE);
		articleElm.setAttribute(Constants.ATTR_RFID, article.getRfid());
		
		Set extensions = article.getArticleExtensions();
		for (Iterator iterator = extensions.iterator(); iterator.hasNext();)
		{
			ArticleExtension extension = (ArticleExtension) iterator.next();
			articleElm.addContent(export(extension));
		}
		
		Set locations = article.getArticleLocations();
		for (Iterator iterator = locations.iterator(); iterator.hasNext();)
		{
			ArticleLocation location = (ArticleLocation) iterator.next();
			articleElm.addContent(export(location));
		}
		
		return articleElm;
	}
	
	public Element export(ArticleExtension extension)
	{
		Element extensionElm = new Element(Constants.ELEMENT_ARTICLE_EXTENSION);
		extensionElm.setAttribute(Constants.ATTR_NAME, extension.getId().getName());
		extensionElm.setText(extension.getId().getValue());
		
		return extensionElm;
	}
	
	public Element export(ArticleLocation location)
	{
		Element locationElm = new Element(Constants.ELEMENT_ARTICLE_EXTENSION);
		locationElm.setAttribute(Constants.ATTR_SHELF, location.getId().getShelf());
		locationElm.setAttribute(Constants.ATTR_CELL, location.getId().getCell());
		locationElm.setAttribute(Constants.ATTR_POSITION, location.getId().getPosition());
		
		return locationElm;
	}
}
