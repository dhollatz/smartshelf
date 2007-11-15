package de.haw.smartshelf.server;

import wicket.Session;
import wicket.markup.html.ServerAndClientTimeFilter;
import wicket.protocol.http.WebApplication;
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

	@Override
	public Session newSession()
	{
		return new SmartShelfSession(this);
	}
	
	
	
	

}