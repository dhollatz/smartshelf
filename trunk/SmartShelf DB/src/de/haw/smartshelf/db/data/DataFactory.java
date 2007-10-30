/*
 * $HeadURL: DataFactory.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 27.10.2007 23:42:39 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.db.data;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.haw.smartshelf.db.data.pers.Article;
import de.haw.smartshelf.db.data.pers.ArticleExtensions;
import de.haw.smartshelf.db.data.pers.ArticleExtensionsId;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 27.10.2007 23:42:39 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class DataFactory
{
	private static DataFactory _instance = null;
	
	private DataFactory()
	{
		
	}
	
	public static DataFactory getInstance()
	{
		if(_instance == null)
		{
			_instance = new DataFactory();
		}
		
		return _instance;
	}
	
	public List findArticle(String extensionName, String extensionValue)
	{
		Session session = InitSessionFactory.getInstance().getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		Query query = session.createQuery("select extension.id.rfid from " + ArticleExtensions.class.getName()
				+ " extension where lower(extension.id.name) like '%" + extensionName.toLowerCase() + "%' and lower(extension.id.value) like '%"
				+ extensionValue.toLowerCase() + "%'");
				
		List rfids = query.list();
		
		tx.commit();
		
		return rfids;
	}
	
	/**
	 * adds the given article to DB
	 * @param rfid
	 * @param type
	 * @param extensions
	 * @return
	 */
	public Article addArticle(String rfid, String type, Map<String, String> extensions)
	{
		Session session = InitSessionFactory.getInstance().getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		Article article = new Article(rfid);
		article.setArticleType(type);
		session.save(article);
		for (Iterator<String> iterator = extensions.keySet().iterator(); iterator.hasNext();)
		{
			String key = (String) iterator.next();
			String value = (String) extensions.get(key);
			
			ArticleExtensions extns = new ArticleExtensions(
					new ArticleExtensionsId(article.getRfid(), key, value), article);
			session.save(extns);
			
			article.getArticleExtensionses().add(extns);
		}
		tx.commit();
		
		return article;
	}
	
	/**
	 * adds the given article to DB
	 * @param rfid
	 * @param type
	 * @param extensions
	 * @return
	 */
	public Article addArticle(String rfid, String type, String[][] extensions)
	{
		Session session = InitSessionFactory.getInstance().getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		Article article = new Article(rfid);
		article.setArticleType(type);
		session.save(article);
		for (int i = 0; i < extensions.length; i++)
		{
			String[] extension = extensions[i];
			
			ArticleExtensions extns = new ArticleExtensions(
					new ArticleExtensionsId(article.getRfid(), extension[0], extension[1]), article);
			session.save(extns);
			
			article.getArticleExtensionses().add(extns);
		}		
		tx.commit();
		
		return article;
	}
	
	public Article getArticle(String rfid)
	{
		Session session = InitSessionFactory.getInstance().getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		Article article = (Article) session.get(Article.class, rfid);
		tx.commit();
				
		return article;
	}
	
	public void removeArticle(Article article) throws DataFactoryException
	{
		if (article != null)
		{
			Session session = InitSessionFactory.getInstance().getCurrentSession();
			Transaction tx = session.beginTransaction();

			session.delete(article);

			tx.commit();
		}
		else
		{
			throw new DataFactoryException("Article is NULL");
		}
	}
	
	public void removeArticle(String rfid) throws DataFactoryException
	{
		removeArticle(getArticle(rfid));
	}
	
	public static void main(String[] args)
	{
		DataFactory df = DataFactory.getInstance();
//		df.addArticle("54313", "CD", new String[][]{{"Title", "The Best"}, {"Interpret", "The Doors"}, {"Track1", "Riding On The Storm"}});
		System.out.println(df.getArticle("54313").getArticleExtensionses());
		try
		{
			df.removeArticle("2345");
		}
		catch (DataFactoryException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List rfids = df.findArticle("InTer", "The");
		System.out.println(rfids);
	}

}
