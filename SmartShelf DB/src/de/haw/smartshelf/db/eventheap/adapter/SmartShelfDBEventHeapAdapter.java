/*
 * $HeadURL: SmartShelfEventHeapAdapter.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: Jan 15, 2008 7:41:07 AM $
 *
 * Copyright 2008 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.db.eventheap.adapter;

import iwork.eheap2.Event;
import iwork.eheap2.EventCallback;
import iwork.eheap2.EventHeapException;

import java.io.File;
import java.util.List;

import org.jdom.Element;

import de.haw.smartshelf.bo.Article;
import de.haw.smartshelf.commonutils.xml.XmlUtil;
import de.haw.smartshelf.db.bo.BoFactory;
import de.haw.smartshelf.db.data.DataFactory;
import de.haw.smartshelf.db.data.xml.DataExporter;
import de.haw.smartshelf.eha.EventHeapAdapter;
import de.haw.smartshelf.eha.EventHeapAdapterConfig;
import de.haw.smartshelf.eha.events.EventFactory;
import de.haw.smartshelf.eha.events.ResultListEventFacade;
import de.haw.smartshelf.eha.events.SearchItemEventFacade;

/**
 * This class ... Copyright (c) 2008 SmartShelf
 * 
 * @version $ Date: Jan 15, 2008 7:41:07 AM $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class SmartShelfDBEventHeapAdapter implements EventCallback
{
	EventHeapAdapter eha;
	private Article _inputArticle;
	
	
	public SmartShelfDBEventHeapAdapter() throws EventHeapException
	{
		try
		{
			this.eha = new EventHeapAdapter(new EventHeapAdapterConfig(System.getProperty("user.dir") + File.separator + "config" + File.separator + "eventheapadapter.properties"));
		}
		catch (Exception e)
		{
			throw new EventHeapException("Could not initialize EventHeapAdapter. " + e.getMessage());
		}
		this.eha.registerForEvent(EventFactory.createSearchItemEvent(), this);
		this.eha.registerForEvent(EventFactory.createFoundIDEvent(), this);
	}
	
	public boolean returnEvent(Event[] events)
	{
		try {
			for (Event event : events) {
				if (ResultListEventFacade.TYPE_NAME.equals(event.getEventType())) {
					ResultListEventFacade rlEvent = new ResultListEventFacade(event);
					// TODO etwas mit der Liste machen ;)
					System.out.println("Liste empfangen!");
					//System.out.println(rlEvent.toStringComplete());
				} else 	if (SearchItemEventFacade.TYPE_NAME.equals(event.getEventType())) {
					
					//SearchItemEvent siEvent = (SearchItemEvent) event;
					// TODO etwas mit der Liste machen ;)
					System.out.println("SearchItem empfangen!");
					System.out.println(event.toStringComplete());
					System.out.println("ARTICLE: " + new SearchItemEventFacade(event).getArticle());
//					String string = (String)event.getPostValue("test.key");
//					System.out.println("string: " + string);
				
				
					Article inputArticle = (Article) new SearchItemEventFacade(event).getArticle();
					List<de.haw.smartshelf.db.data.pers.Article> foundPersArticles = findArticles(inputArticle);
					
					sendArticles(foundPersArticles, event);
				} else {
					System.out.println("unknown event: " + event.getEventType());
				}
			}
		} catch (EventHeapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return true; // weitere Events empfangen
	}
	
	private void sendArticles(List<de.haw.smartshelf.db.data.pers.Article> foundPersArticles, Event requestEvent)
	{ 
		try
		{
			Event resultListEvent = EventFactory.createResultListEvent();
			ResultListEventFacade rleFacade = new ResultListEventFacade(resultListEvent);
			Element articlesElm = new DataExporter().export(foundPersArticles);
			String xml = XmlUtil.xmlToString(articlesElm);
			rleFacade.setListXML(xml);
			
			eha.putEvent(resultListEvent);
			
		}
		catch (EventHeapException e)
		{
			e.printStackTrace();
		}
	}

	public List<de.haw.smartshelf.db.data.pers.Article>  findArticles(Article inputArticle)
	{
		return DataFactory.getInstance().findArticle(BoFactory.getInstance().convertToPers(inputArticle));
	}
	
	public void run()
	{
		while(true)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public static void main(String[] args) throws EventHeapException
	{
		SmartShelfDBEventHeapAdapter dbAdapter = new SmartShelfDBEventHeapAdapter();
//		dbAdapter.run();
	}
}