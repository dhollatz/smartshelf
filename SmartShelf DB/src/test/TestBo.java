/*
 * $HeadURL: TestBo.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 09.11.2007 15:00:58 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package test;

import java.util.ArrayList;
import java.util.List;

import de.haw.smartshelf.bo.Article;
import de.haw.smartshelf.bo.ArticleExtension;
import de.haw.smartshelf.db.bo.BoFactory;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 09.11.2007 15:00:58 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class TestBo
{
	public static void main(String[] args)
	{
		BoFactory boFactory = BoFactory.getInstance();
		try
		{
//			boFactory.addArticle(createArticle());
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
		}
		
		System.out.println(boFactory.findArticle(createSearchArticle()));
	}

	public static Article createSearchArticle()
	{
		Article boArticle = new Article(null);
		boArticle.setArticleType("");
		List<ArticleExtension> extensions = new ArrayList<ArticleExtension>();
		extensions.add(new ArticleExtension("", ""));
						
		boArticle.setArticleExtensions(extensions);
		
		return boArticle;
	}
	
	public static Article createArticle()
	{
		Article boArticle = new Article("0159");
		boArticle.setArticleType("DVD");
		List<ArticleExtension> extensions = new ArrayList<ArticleExtension>();
		extensions.add(new ArticleExtension("Title", "Matrix"));
		extensions.add(new ArticleExtension("Director", "Wachowski Brothers"));
		extensions.add(new ArticleExtension("Music", "Don Davis"));
				
		boArticle.setArticleExtensions(extensions);
	
		return boArticle;
	}
}
