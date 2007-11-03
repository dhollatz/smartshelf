/*
 * $HeadURL: TestExportArticle.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 02.11.2007 18:54:35 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package test;

import de.haw.smartshelf.commonutils.xml.XmlUtil;
import de.haw.smartshelf.db.data.DataFactory;
import de.haw.smartshelf.db.data.ObjectNotFoundException;
import de.haw.smartshelf.db.data.xml.DataExporter;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 02.11.2007 18:54:35 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class TestExportArticle
{
	public static void main(String[] args) throws ObjectNotFoundException
	{
		DataFactory df = DataFactory.getInstance();
		System.out.println(XmlUtil.xmlToString(new DataExporter().export(df.getArticle("0123"))));
	}
}
