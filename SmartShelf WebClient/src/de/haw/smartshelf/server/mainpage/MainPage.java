package de.haw.smartshelf.server.mainpage;

import wicket.markup.html.basic.Label;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.model.PropertyModel;
import de.haw.smartshelf.server.SmartShelfPage;

public abstract class MainPage extends SmartShelfPage
{
	/** title of the current page. */
	private String pageTitle = "(no title)";

	/**
	 * Constructor
	 */
	public MainPage()
	{
		add(new Label("title", new PropertyModel(this, "pageTitle")));
		
		add(new BookmarkablePageLink("pageWelcome", WellcomePage.class));
        add(new BookmarkablePageLink("pageSearch", SearchPage.class));
        add(new BookmarkablePageLink("pageHelp", HelpPage.class));
	}

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