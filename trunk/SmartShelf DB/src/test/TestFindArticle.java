/*
 * $HeadURL: TestFindArticle.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 03.11.2007 13:07:49 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.haw.smartshelf.db.data.DataFactory;
import de.haw.smartshelf.db.data.pers.Article;
import de.haw.smartshelf.db.data.pers.ArticleExtension;
import de.haw.smartshelf.db.data.pers.ArticleExtensionId;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 03.11.2007 13:07:49 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class TestFindArticle
{
	public static void main(String[] args)
	{
		Article inputArticle = new Article();
//		inputArticle.setRfid("0123");
//		inputArticle.setArticleType("CD");
		Set inputExtensions = new HashSet();
		ArticleExtensionId extId = new ArticleExtensionId();
		extId.setName("Track");
		extId.setValue("Storm");
		
		inputExtensions.add(new ArticleExtension(extId, inputArticle));
		extId = new ArticleExtensionId();
		extId.setName("Track");
		extId.setValue("Storm");
		
		inputExtensions.add(new ArticleExtension(extId, inputArticle));
		inputArticle.setArticleExtensions(inputExtensions);
		
		List result = DataFactory.getInstance().findArticle(inputArticle);
		System.out.println(result);
	}
}
