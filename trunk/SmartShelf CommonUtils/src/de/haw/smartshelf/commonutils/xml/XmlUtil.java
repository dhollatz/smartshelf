/*
 * $HeadURL: XmlUtil.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 30.10.2007 14:37:15 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.commonutils.xml;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 30.10.2007 14:37:15 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class XmlUtil
{

	public static Document readXml(String fileName) throws JDOMException, IOException
	{
		return readXml(new File(fileName));
	}
	
	public static Document readXml(File xmlFile) throws JDOMException, IOException
	{
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(xmlFile);

		return doc;
	}

	public static void writeXmlDocuments(Document doc, OutputStream out) throws IOException
	{
		XMLOutputter fmt = new XMLOutputter();
		fmt.setFormat(Format.getPrettyFormat()); // only for nicer formatting
		fmt.output(doc, out);
	}
	
	public static void writeXmlDocuments(Document doc, Writer out) throws IOException
	{
		XMLOutputter fmt = new XMLOutputter();
		fmt.setFormat(Format.getPrettyFormat()); // only for nicer formatting
		fmt.output(doc, out);
	}
}
