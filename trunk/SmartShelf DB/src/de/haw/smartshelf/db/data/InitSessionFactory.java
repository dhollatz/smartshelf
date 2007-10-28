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

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 28.10.2007 10:48:49 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class InitSessionFactory
{
	/** The single instance of hibernate SessionFactory */
	private static SessionFactory sessionFactory;

	private InitSessionFactory()
	{
	}

	static
	{
		final AnnotationConfiguration cfg = new AnnotationConfiguration();
//		final Configuration cfg = new Configuration();
		String configPath = File.separator + "hibernate.cfg.xml";
		cfg.configure(configPath);
		sessionFactory = cfg.buildSessionFactory();
	}

	public static SessionFactory getInstance()
	{
		return sessionFactory;
	}
}
