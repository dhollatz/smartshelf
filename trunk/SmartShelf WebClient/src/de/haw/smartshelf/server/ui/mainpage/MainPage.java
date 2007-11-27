package de.haw.smartshelf.server.ui.mainpage;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.PropertyModel;

import de.haw.smartshelf.server.SmartShelfPage;
import de.haw.smartshelf.server.ui.searchpage.SearchPage;

public abstract class MainPage extends SmartShelfPage
{
	/**
	 * Constructor
	 */
	public MainPage()
	{
		add(new org.apache.wicket.markup.html.basic.Label("title", new PropertyModel(this, "pageTitle")));
		
		add(new BookmarkablePageLink("pageWelcome", WellcomePage.class));
        add(new BookmarkablePageLink("pageSearch", SearchPage.class));
        add(new BookmarkablePageLink("pageHelp", HelpPage.class));
	}
}