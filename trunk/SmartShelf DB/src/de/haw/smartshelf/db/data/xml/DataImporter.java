/*
 * $HeadURL: ArticleImporter.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 02.11.2007 13:44:04 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.db.data.xml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;

import de.haw.smartshelf.db.data.DataFactory;
import de.haw.smartshelf.db.data.pers.Article;
import de.haw.smartshelf.db.data.pers.ArticleExtension;
import de.haw.smartshelf.db.data.pers.ArticleExtensionId;
import de.haw.smartshelf.db.data.pers.ArticleLocation;
import de.haw.smartshelf.db.data.pers.ArticleLocationId;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 02.11.2007 13:44:04 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class DataImporter
{
	private DataFactory _dataFactory;
	
	public DataImporter()
	{
		_dataFactory = DataFactory.getInstance();
	}
	
	public void insertArticles(Document doc)
	{
		List articlesElms = doc.getRootElement().getChildren(Constants.ELEMENT_ARTICLE);
		for (Iterator iterator = articlesElms.iterator(); iterator.hasNext();)
		{
			Element articleElm = (Element) iterator.next();
			insertArticle(articleElm);
		}
	}
	
	public void insertArticle(Element articleElm)
	{
		String rfid = articleElm.getAttributeValue(Constants.ATTR_RFID);
		String type = articleElm.getAttributeValue(Constants.ATTR_TYPE);	
		
		List<String[]> extensions = new ArrayList<String[]>();
		List extensionElms = articleElm.getChildren(Constants.ELEMENT_ARTICLE_EXTENSION);
		if(extensionElms != null)
		{
			for (Iterator iterator = extensionElms.iterator(); iterator.hasNext();)
			{
				Element extensionElm = (Element) iterator.next();
				extensions.add(new String[]{extensionElm.getAttributeValue(Constants.ATTR_NAME), extensionElm.getText()});
			} 
		}
		
		_dataFactory.addArticle(rfid, type, (String[][]) extensions.toArray(new String[2][extensions.size()]));
	}
	
	public Article createArticle(Element articleElm)
	{
		Article article = new Article();
		
		article.setRfid(articleElm.getAttributeValue(Constants.ATTR_RFID));
		article.setArticleType(articleElm.getAttributeValue(Constants.ATTR_TYPE));
		
		List extensionElms = articleElm.getChildren(Constants.ELEMENT_ARTICLE_EXTENSION);
		if(extensionElms != null)
		{
			Set articleExtensions = new HashSet();
			for (Iterator iterator = extensionElms.iterator(); iterator.hasNext();)
			{
				Element extensionElm = (Element) iterator.next();
				ArticleExtension articleExtension = new ArticleExtension(new ArticleExtensionId(article.getRfid(),
						extensionElm.getAttributeValue(Constants.ATTR_NAME), extensionElm.getText()), article);
				articleExtensions.add(articleExtension);
			}
			article.setArticleExtensions(articleExtensions);
		}
		
		List locationElms = articleElm.getChildren(Constants.ELEMENT_ARTICLE_LOCATION);
		if(extensionElms != null)
		{
			Set articleLocations = new HashSet();
			for (Iterator iterator = extensionElms.iterator(); iterator.hasNext();)
			{
				Element extensionElm = (Element) iterator.next();
				ArticleLocation articleLocation = new ArticleLocation(new ArticleLocationId(article.getRfid(),
						extensionElm.getAttributeValue(Constants.ATTR_SHELF), extensionElm
								.getAttributeValue(Constants.ATTR_CELL), extensionElm
								.getAttributeValue(Constants.ATTR_POSITION)));
				articleLocations.add(articleLocation);
			}
			article.setArticleLocations(articleLocations);
		}
		
		return article;
	}
}
