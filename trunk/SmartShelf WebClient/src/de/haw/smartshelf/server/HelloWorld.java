
package de.haw.smartshelf.server;

import de.haw.smartshelf.server.mainpage.WellcomePage;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.BookmarkablePageLink;



public class HelloWorld extends SmartShelfPage
{
	/**
	 * Constructor
	 */
	public HelloWorld()
	{
		add(new Label("message", "Hello World!"));
		add(new BookmarkablePageLink("pageSmartshelf", WellcomePage.class));
	}
}