package de.haw.smartshelf.server.mainpage;

import wicket.markup.html.panel.Panel;
import wicket.model.IModel;

public abstract class MainPanel extends Panel
{
	/**
	 * Construct.
	 * 
	 * @param id
	 *            component id
	 */
	public MainPanel(String id)
	{
		super(id);
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 *            component id
	 * @param model
	 *            the model
	 */
	public MainPanel(String id, IModel model)
	{
		super(id, model);
	}

}
