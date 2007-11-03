/*
 * $HeadURL: InitSessionFactory.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 28.10.2007 10:48:49 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.db.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import de.haw.smartshelf.db.data.pers.Article;
import de.haw.smartshelf.db.data.pers.ArticleExtension;
import de.haw.smartshelf.db.data.pers.ArticleLocation;
import de.haw.smartshelf.db.data.pers.Shelf;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 28.10.2007 10:48:49 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class InitSessionFactory
{
	private static Logger _log = Logger.getLogger("SmartShelfDb/InitSessionFactory");
	
	private static final String CONFIG_PATH = System.getProperty("user.dir") + File.separator + "config"
			+ File.separator + "hibernate.cfg.xml";
	private static final String PROPERTIES_PATH = System.getProperty("user.dir") + File.separator + "config"
	+ File.separator + "smartshelfdb.properties";
	
	/** The single instance of hibernate SessionFactory */
	private static SessionFactory sessionFactory;

	
	private InitSessionFactory()
	{
	}

	static
	{
//		final AnnotationConfiguration cfg = new AnnotationConfiguration();
//		cfg.configure(CONFIG_PATH);
		
		final Configuration cfg = new Configuration();
		Properties properties = null;
		try
		{
			properties = loadProperties(PROPERTIES_PATH);
		}
		catch (Exception e)
		{
			_log.fatal("Could not load properties: " + e.getMessage());
		}
		
		cfg.addClass(Article.class);
		cfg.addClass(ArticleExtension.class);
		cfg.addClass(Shelf.class);		
		cfg.addClass(ArticleLocation.class);
				
		cfg.setProperty("hibernate.connection.driver_class", properties.getProperty("hibernate.connection.driver_class"));
		cfg.setProperty("hibernate.connection.url", properties.getProperty("hibernate.connection.url"));
		cfg.setProperty("hibernate.connection.username", properties.getProperty("hibernate.connection.username"));
		cfg.setProperty("hibernate.connection.password", properties.getProperty("hibernate.connection.password"));
		
		cfg.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.EhCacheProvider");
		cfg.setProperty("hibernate.current_session_context_class", "thread");
		cfg.setProperty("hibernate.transaction.factory_class", "org.hibernate.transaction.JDBCTransactionFactory");
		
		cfg.setProperty("hibernate.connection.provider_class", "org.hibernate.connection.C3P0ConnectionProvider");
		cfg.setProperty("hibernate.c3p0.min_size", properties.getProperty("hibernate.c3p0.min_size"));
		cfg.setProperty("hibernate.c3p0.max_size", properties.getProperty("hibernate.c3p0.max_size"));
		cfg.setProperty("hibernate.c3p0.timeout", properties.getProperty("hibernate.c3p0.timeout"));
		cfg.setProperty("hibernate.c3p0.max_statements", properties.getProperty("hibernate.c3p0.max_statements"));
		
		cfg.setProperty("hibernate.dialect", properties.getProperty("hibernate.dialect"));
		cfg.setProperty("hibernate.show_sql", properties.getProperty("hibernate.show_sql"));
		cfg.setProperty("hibernate.use_outer_join", properties.getProperty("hibernate.use_outer_join"));
						
		sessionFactory = cfg.buildSessionFactory();
	}

	public static SessionFactory getInstance()
	{
		return sessionFactory;
	}
	
	private static Properties loadProperties(String filename) throws FileNotFoundException, IOException
	{
		Properties properties = new Properties();
		properties.load(new FileInputStream(filename));

		return properties;
	}
}
