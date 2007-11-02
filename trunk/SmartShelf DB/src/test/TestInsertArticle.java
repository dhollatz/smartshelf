/*
 * $HeadURL: TestInsertArticle.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 02.11.2007 14:12:50 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package test;

import java.io.File;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.JDOMException;

import de.haw.smartshelf.commonutils.xml.XmlUtil;
import de.haw.smartshelf.db.data.xml.ArticleImporter;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 02.11.2007 14:12:50 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class TestInsertArticle
{
	public static void main(String[] args) throws JDOMException, IOException
	{
		Document articlesDoc = XmlUtil.readXml(System.getProperty("user.dir") + File.separator + "test" + File.separator + "Articles.xml");
		
		ArticleImporter ai = new ArticleImporter();
		ai.insertArticles(articlesDoc);
	}
}
