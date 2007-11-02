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
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import de.haw.smartshelf.db.data.DataFactory;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 02.11.2007 13:44:04 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class ArticleImporter
{
	private DataFactory _dataFactory;
	
	public ArticleImporter()
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
}
