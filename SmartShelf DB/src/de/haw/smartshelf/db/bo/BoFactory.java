/*
 * $HeadURL: BoFactory.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 09.11.2007 13:46:52 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.db.bo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.haw.smartshelf.bo.Article;
import de.haw.smartshelf.bo.ArticleExtension;
import de.haw.smartshelf.bo.ArticleLocation;
import de.haw.smartshelf.bo.Shelf;
import de.haw.smartshelf.db.data.DataFactory;
import de.haw.smartshelf.db.data.pers.ArticleExtensionId;
import de.haw.smartshelf.db.data.pers.ArticleLocationId;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 09.11.2007 13:46:52 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class BoFactory
{
	private final DataFactory _dataFactory;
	private static BoFactory _instance = null;
	
	private BoFactory()
	{
		_dataFactory = DataFactory.getInstance();
	}
	
	public static BoFactory getInstance()
	{
		if(_instance == null)
		{
			_instance = new BoFactory();
		}
		
		return _instance;
	}
	
	public void addArticle(Article inputArticle)
	{
		_dataFactory.addArticle(convertToPers(inputArticle));
	}
	
	public List<Article> findArticle(Article inputArticle, boolean withLocationInput)
	{
		return convertToBo(_dataFactory.findArticle(convertToPers(inputArticle), withLocationInput));
	}
	
	public List<Article> findArticle(Article inputArticle)
	{
		return findArticle(inputArticle, false);
	}
	
	public List<Article> findArticle(String rfid)
	{
		return convertToBo(_dataFactory.findArticle(new de.haw.smartshelf.db.data.pers.Article(rfid)));
	}
	
	public List<Article> convertToBo(List<de.haw.smartshelf.db.data.pers.Article> persArticles)
	{
		List<Article> boArticles = new ArrayList<Article>();
		for (Iterator iterator = persArticles.iterator(); iterator.hasNext();)
		{
			boArticles.add(convertToBo((de.haw.smartshelf.db.data.pers.Article) iterator.next()));			
		}
		return boArticles;
	}
	
	public Article convertToBo(de.haw.smartshelf.db.data.pers.Article persArticle)
	{
		Article boArticle = new Article(persArticle.getRfid(), persArticle.getArticleType());
		List<ArticleExtension> extensions = new ArrayList<ArticleExtension>();
		Set persExtns = persArticle.getArticleExtensions();
		for (Iterator iterator = persExtns.iterator(); iterator.hasNext();)
		{
			extensions.add(convertToBo((de.haw.smartshelf.db.data.pers.ArticleExtension) iterator.next()));
		}
		boArticle.setArticleExtensions(extensions);
		
		Set persArtLocs = persArticle.getArticleLocations();
		if(!persArtLocs.isEmpty())
		{
			Iterator iterator1 = persArtLocs.iterator();
			de.haw.smartshelf.db.data.pers.ArticleLocation persLoc = (de.haw.smartshelf.db.data.pers.ArticleLocation) iterator1.next();
			ArticleLocation boArtLoc = convertToBo(persLoc);
			
			if(boArtLoc != null)
			{
				/* TODO: */
				try
				{
					de.haw.smartshelf.db.data.pers.Shelf persShelf = persLoc.getShelf();
					Shelf boShelf = convertToBo(persShelf);
					boArtLoc.setShelf(boShelf);
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			boArticle.setArticleLocation(boArtLoc);			
		}
		
		return boArticle;
	}
	
	public ArticleExtension convertToBo(de.haw.smartshelf.db.data.pers.ArticleExtension persExtn)
	{
		return new ArticleExtension(persExtn.getId().getName(), persExtn.getId().getValue());
	}
	
	public ArticleLocation convertToBo(de.haw.smartshelf.db.data.pers.ArticleLocation persArtLoctn)
	{
		if(persArtLoctn == null)
		{
			return null;
		}
		
		ArticleLocation boArtLoctn = new ArticleLocation();
		
		boArtLoctn.setCell(persArtLoctn.getId().getCell());
		boArtLoctn.setPosition(persArtLoctn.getId().getPosition());
		
		return boArtLoctn;
	}
	
	public Shelf convertToBo(de.haw.smartshelf.db.data.pers.Shelf persSchelf)
	{
		if(persSchelf == null)
		{
			return null;
		}
		
		Shelf boShelf =  new Shelf();
		boShelf.setId(persSchelf.getId());
		try
		{
			boShelf.setLocation(persSchelf.getLocation());
		}
		catch (RuntimeException e)
		{
			boShelf.setLocation(null);
		}
		try
		{
			boShelf.setName(persSchelf.getName());
		}
		catch (RuntimeException e)
		{
			boShelf.setName(null);
		}
	
		return boShelf;
	}
	
	public de.haw.smartshelf.db.data.pers.Article convertToPers(Article boArticle)
	{
		de.haw.smartshelf.db.data.pers.Article persArticle = new de.haw.smartshelf.db.data.pers.Article();
		persArticle.setRfid(boArticle.getRfid());
		persArticle.setArticleType(boArticle.getArticleType());
		
		Set persExtns = new HashSet();
		List<ArticleExtension> boExtensions = boArticle.getArticleExtensions();
		if(boExtensions != null)
		{
			for (Iterator iterator = boExtensions.iterator(); iterator.hasNext();)
			{
				ArticleExtension boExtension = (ArticleExtension) iterator.next();
				persExtns.add(convertToPers(boExtension, persArticle));
			}
		}
		persArticle.setArticleExtensions(persExtns);
		
		ArticleLocation boArticleLocation = boArticle.getArticleLocation();
		de.haw.smartshelf.db.data.pers.ArticleLocation persArtLoc = convertToPers(boArticleLocation);
		if(persArtLoc != null)
		{
			Set persArtLocs = new HashSet();
			persArtLocs.add(persArtLoc);
			persArticle.setArticleLocations(persArtLocs);
			persArtLoc.setArticle(persArticle);
		}
		
		return persArticle;
	}
	
	public de.haw.smartshelf.db.data.pers.ArticleExtension convertToPers(ArticleExtension boExtension, de.haw.smartshelf.db.data.pers.Article persArticle)
	{
		if(boExtension == null)
		{
			return null;
		}
		
		de.haw.smartshelf.db.data.pers.ArticleExtension persExtension = new de.haw.smartshelf.db.data.pers.ArticleExtension();
		ArticleExtensionId persExtensionId = new ArticleExtensionId();
		
		persExtensionId.setRfid(persArticle.getRfid());
		persExtensionId.setName(boExtension.getName());
		persExtensionId.setValue(boExtension.getValue());
	
		persExtension.setArticle(persArticle);
		persExtension.setId(persExtensionId);
		
		return persExtension;
	}
	
	public de.haw.smartshelf.db.data.pers.ArticleLocation convertToPers(ArticleLocation boArticleLocation)
	{
		if(boArticleLocation == null)
		{
			return null;
		}
		
		de.haw.smartshelf.db.data.pers.ArticleLocation persArtclLoc = new de.haw.smartshelf.db.data.pers.ArticleLocation();
		ArticleLocationId persArtclLocId = new ArticleLocationId();
		try
		{
			persArtclLocId.setShelf(boArticleLocation.getShelf().getId());
		}
		catch (RuntimeException e)
		{
		}
		try
		{
			persArtclLocId.setRfid(boArticleLocation.getArticle().getRfid());
		}
		catch (RuntimeException e)
		{
		}
		
		persArtclLocId.setCell(boArticleLocation.getCell());
		persArtclLocId.setPosition(boArticleLocation.getPosition());
		
		persArtclLoc.setId(persArtclLocId);
		
		return persArtclLoc;
	}
}
