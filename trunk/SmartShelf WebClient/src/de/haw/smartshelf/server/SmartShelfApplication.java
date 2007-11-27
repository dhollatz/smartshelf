package de.haw.smartshelf.server;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.ServerAndClientTimeFilter;
import org.apache.wicket.protocol.http.WebApplication;

import de.haw.smartshelf.server.ui.mainpage.WellcomePage;

public class SmartShelfApplication extends WebApplication
{	
	/**
	 * Constructor.
	 */
	public SmartShelfApplication()
	{

	}

	/**
	 * @see wicket.Application#getHomePage()
	 */
	public Class getHomePage()
	{
		return WellcomePage.class;
	}
	
	/**
	 * @see wicket.protocol.http.WebApplication#init()
	 */
	protected void init()
	{
		getRequestCycleSettings().addResponseFilter(new ServerAndClientTimeFilter());
	}

	
	
	

}