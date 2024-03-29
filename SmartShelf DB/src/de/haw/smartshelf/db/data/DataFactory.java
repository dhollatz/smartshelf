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
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.haw.smartshelf.commonutils.Util;
import de.haw.smartshelf.db.data.pers.Article;
import de.haw.smartshelf.db.data.pers.ArticleExtension;
import de.haw.smartshelf.db.data.pers.ArticleExtensionId;
import de.haw.smartshelf.db.data.pers.ArticleLocation;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 27.10.2007 23:42:39 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class DataFactory
{
	private static Logger _log = Logger.getLogger("SmartShelfDb/DataFactory");
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
	
	public List findArticleRfids(String extensionName, String extensionValue)
	{
		Session session = InitSessionFactory.getInstance().getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		Query query = session.createQuery("select extension.id.rfid from " + ArticleExtension.class.getName()
				+ " extension where lower(extension.id.name) like '%" + extensionName.toLowerCase() + "%' and lower(extension.id.value) like '%"
				+ extensionValue.toLowerCase() + "%'");
				
		List rfids = query.list();
		
		tx.commit();
		
		return rfids;
	}
	
	public List findArticle(Article inputArticle)
	{
		return findArticle(inputArticle, false);
	}
	
	public List findArticle(Article inputArticle, boolean withLocationInput)
	{
		boolean shouAddAND = false;
		boolean condition = false;
		
		Session session = InitSessionFactory.getInstance().getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		Set inputExtensions = inputArticle.getArticleExtensions();
		
		String queryAsString = "select distinct article from " + Article.class.getName() + " as article";
		if(!inputExtensions.isEmpty())
		{
			queryAsString += ", " + ArticleExtension.class.getName() + " as extension";
		}
		
		if(withLocationInput)
		{
			queryAsString +=  " , " + ArticleLocation.class.getName() + " as location";
		}
		queryAsString += " where";		
		if(!inputExtensions.isEmpty())
		{
			queryAsString += " extension.id.rfid like article.rfid"; 
			shouAddAND = true;
			condition = true;
		}
		if(withLocationInput)
		{
			if(shouAddAND)
			{
				queryAsString += " and";
			}
			queryAsString += " location.id.rfid like article.rfid";
			shouAddAND = true;
			condition = true;
		}
		
		if(!Util.isEmpty(inputArticle.getRfid()))
		{
			if(shouAddAND)
			{
				queryAsString += " and";
			}
			queryAsString += " article.rfid like '" + inputArticle.getRfid() + "'";
			shouAddAND = true;
			condition = true;
		}
		if(!Util.isEmpty(inputArticle.getArticleType()))
		{
			if(shouAddAND)
			{
				queryAsString += " and";
			}
			queryAsString += " lower(article.articleType) like '%" + inputArticle.getArticleType().toLowerCase() + "%'";
			shouAddAND = true;
			condition = true;
		}		
		
		if(!inputExtensions.isEmpty())
		{
			if(shouAddAND)
			{
				queryAsString += " and";
			}
			queryAsString += " (";
			int index = 0;
			for (Iterator iterator = inputExtensions.iterator(); iterator.hasNext();)
			{
				ArticleExtension inExtension = (ArticleExtension) iterator.next();
				String name = inExtension.getId().getName();
				String value = inExtension.getId().getValue();
				
				if((Util.isEmpty(name)) && (Util.isEmpty(value)))
				{
					continue;
				}
				condition = true;
				
				if(index > 0)
				{
					queryAsString += " or ";
				}
				
				
				if((!Util.isEmpty(name)) && (!Util.isEmpty(value)))
				{
					queryAsString += "(lower(extension.id.name) like '%" + name.toLowerCase() + "%'";
					queryAsString += " and lower(extension.id.value) like '%" + value.toLowerCase() + "%')";
				}
				else if(!Util.isEmpty(name))
				{
					queryAsString += "(lower(extension.id.name) like '%" + name.toLowerCase() + "%')";
				}
				else if(!Util.isEmpty(value))
				{
					queryAsString += "(lower(extension.id.value) like '%" + value.toLowerCase() + "%')";
				}
				
				index ++;
			}
			queryAsString += ")";
			shouAddAND = true;
		}
		
		if (withLocationInput)
		{
			Set inputLocations = inputArticle.getArticleLocations();
			if (!inputLocations.isEmpty())
			{
				if(shouAddAND)
				{
					queryAsString += " and";
				}
				queryAsString += " (";
				int index = 0;
				for (Iterator iterator = inputLocations.iterator(); iterator.hasNext();)
				{
					condition = true;
					if (index > 0)
					{
						queryAsString += " or (";
					}
					ArticleLocation inLocation = (ArticleLocation) iterator.next();
					boolean shouldAddAND = false;
					if (!Util.isEmpty(inLocation.getId().getShelf()))
					{
						queryAsString += "lower(location.id.shelf) like '%"
								+ inLocation.getId().getShelf().toLowerCase() + "%'";
						shouldAddAND = true;
					}
					if (!Util.isEmpty(inLocation.getId().getCell()))
					{
						if (shouldAddAND)
						{
							queryAsString += " and";
						}
						queryAsString += " lower(location.id.cell) like '%"
								+ inLocation.getId().getCell().toLowerCase() + "%'";
						shouldAddAND = true;
					}

					if (!Util.isEmpty(inLocation.getId().getPosition()))
					{
						if (shouldAddAND)
						{
							queryAsString += " and";
						}
						queryAsString += " lower(location.id.position) like '%"
								+ inLocation.getId().getPosition().toLowerCase() + "%'";
						shouldAddAND = true;
					}
					index++;
				}
				shouAddAND = true;
			}
			queryAsString += ")";
		}
		
		if(!condition)
		{
			/* delete where if no condition */
			queryAsString = queryAsString.replaceFirst("where", "");
		}
		
		_log.debug("HQL query for finding article: " + queryAsString);
		Query query = session.createQuery(queryAsString);
				
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
			
			ArticleExtension extns = new ArticleExtension(
					new ArticleExtensionId(article.getRfid(), key, value), article);
			session.save(extns);
			
			article.getArticleExtensions().add(extns);
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
			
			ArticleExtension extns = new ArticleExtension(
					new ArticleExtensionId(article.getRfid(), extension[0], extension[1]), article);
			session.save(extns);
			
			article.getArticleExtensions().add(extns);
		}		
		tx.commit();
		
		return article;
	}
	
	public void addArticle(Article inputArticle)
	{
		Session session = InitSessionFactory.getInstance().getCurrentSession();
		Transaction tx = session.beginTransaction();
				
		session.save(inputArticle);
		Set articleExtensions = inputArticle.getArticleExtensions();
		for (Iterator iterator = articleExtensions.iterator(); iterator.hasNext();)
		{
			ArticleExtension extn = (ArticleExtension) iterator.next();
			session.save(extn);
		}		
		tx.commit();
	}
	
	public Article getArticle(String rfid) throws ObjectNotFoundException
	{
		Session session = InitSessionFactory.getInstance().getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		Article article = (Article) session.get(Article.class, rfid);
		tx.commit();
		
		if(article != null)
		{		
			return article;
		}
		else
		{
			throw new ObjectNotFoundException("Article with RFID: '" + rfid + "' could not be found");
		}
	}
	
	public void removeArticle(Article article) throws DataFactoryException
	{
		if (article != null)
		{
			Session session = InitSessionFactory.getInstance().getCurrentSession();
			Transaction tx = session.beginTransaction();

			try
			{
				/* delete locations */
				Set locations = article.getArticleLocations();
				if((locations != null) && (!locations.isEmpty()))
				{
					for (Iterator iterator = locations.iterator(); iterator.hasNext();)
					{
						ArticleLocation artLocation = (ArticleLocation) iterator.next();
						session.delete(artLocation);
					}
				}
				
				/* delete extensions */
				Set extensions = article.getArticleExtensions();
				if((extensions != null) && (!extensions.isEmpty()))
				{
					for (Iterator iterator = extensions.iterator(); iterator.hasNext();)
					{
						ArticleExtension artExtension = (ArticleExtension) iterator.next();
						session.delete(artExtension);
					}
				}
				
				/* delete article */
				session.delete(article);

				tx.commit();
			}
			catch (HibernateException e)
			{
				tx.rollback();
			}
			
			
		}
		else
		{
			throw new DataFactoryException("Article is NULL");
		}
	}
	
	private void remove(Object object) throws DataFactoryException
	{
		if (object != null)
		{
			Session session = InitSessionFactory.getInstance().getCurrentSession();
			Transaction tx = session.beginTransaction();

			session.delete(object);

			tx.commit();
		}
		else
		{
			throw new DataFactoryException("Object to delete is NULL");
		}
	}
		
	public void removeArticle(String rfid) throws DataFactoryException
	{
		removeArticle(getArticle(rfid));
	}
	
	public static void main(String[] args) throws ObjectNotFoundException
	{
		DataFactory df = DataFactory.getInstance();
//		df.addArticle("54313", "CD", new String[][]{{"Title", "The Best"}, {"Interpret", "The Doors"}, {"Track1", "Riding On The Storm"}});
		System.out.println(df.getArticle("2345").getArticleExtensions());
		try
		{
			df.removeArticle("2345");
		}
		catch (DataFactoryException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List rfids = df.findArticleRfids("InTer", "The");
		System.out.println(rfids);
	}

}
