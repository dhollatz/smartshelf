package de.haw.smartshelf.server;

import wicket.protocol.http.WebApplication;

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
		return HelloWorld.class;
	}

}