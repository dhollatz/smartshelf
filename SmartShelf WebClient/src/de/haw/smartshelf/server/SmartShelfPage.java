package de.haw.smartshelf.server;

import org.apache.wicket.markup.html.WebPage;

public class SmartShelfPage extends WebPage
{
	/** title of the current page. */
	private String pageTitle = "(no title)";
	
	/**
	 * Gets the title.
	 * 
	 * @return title
	 */
	public final String getPageTitle()
	{
		return pageTitle;
	}

	/**
	 * Sets the title.
	 * 
	 * @param title
	 *            title
	 */
	public final void setPageTitle(String title)
	{
		this.pageTitle = title;
	}

	/**
	 * @see wicket.Component#isVersioned()
	 */
	public boolean isVersioned()
	{
		return false;
	}
}
