/*
 * $HeadURL: ArticleDetailsPage.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 15.11.2007 19:43:33 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.server.ui.searchpage.resultpage;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.OddEvenItem;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.util.ModelIteratorAdapter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import de.haw.smartshelf.bo.Article;
import de.haw.smartshelf.bo.ArticleExtension;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 15.11.2007 19:43:33 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class ArticleDetailsPanel extends Panel
{
	private static final long serialVersionUID = 5505120794335472057L;

	private Article _article;
	
	
	
	public ArticleDetailsPanel(String id)
	{
		super(id);
		init();
	}

	public ArticleDetailsPanel(String id, IModel model)
	{
		super(id, model);
		init();
	}
	
	
	
	private void init()
	{
		if(_article != null)
		{
			add(new Label("rfid", _article.getRfid()));
			add(new Label("articleType", _article.getArticleType()));
		
			// create a repeater that will display the list of contacts.
		    RefreshingView refreshingView = new RefreshingView("extensionsTable")
		    {
		        protected Iterator getItemModels()
		        {
		            return new ModelIteratorAdapter(_article.getArticleExtensions().iterator())
		            {

						@Override
						protected IModel model(Object object)
						{
							return new DetachableArticleExtensionModel((ArticleExtension)object);
						}
		            	
		            };
		        }

		        protected void populateItem(final Item item)
		        {
		            // populate the row of the repeater
		            IModel extension = item.getModel();
		            
		            item.add(new Label("extensionName", new PropertyModel(extension, "name")));
		            item.add(new Label("extensionValue", new PropertyModel(extension, "value")));
		        }

		        protected Item newItem(String id, int index, IModel model)
		        {
		            // this item sets markup class attribute to either 'odd' or
		            // 'even' for decoration
		            return new OddEvenItem(id, index, model);
		        }
		    };
		    
		    add(refreshingView);
		}
		else
		{
			add(new Label("rfid", "NONE"));
			add(new Label("articleType", "NONE"));
		
			
			// create a repeater that will display the list of contacts.
		    RefreshingView refreshingView = new RefreshingView("extensionsTable")
		    {
		        protected Iterator getItemModels()
		        {
		            // for simplicity we only show the first 10 contacts
		            return new ArrayList<ArticleExtension>().iterator();
		        }

		        protected void populateItem(final Item item)
		        {
		            // populate the row of the repeater
		            IModel extension = item.getModel();
		            
		            item.add(new Label("extensionName", new PropertyModel(extension, "name")));
		            item.add(new Label("extensionValue", new PropertyModel(extension, "value")));
		        }

		        protected Item newItem(String id, int index, IModel model)
		        {
		            // this item sets markup class attribute to either 'odd' or
		            // 'even' for decoration
		            return new OddEvenItem(id, index, model);
		        }
		    };
			
			add(refreshingView);
		}
	}

	public Article getArticle()
	{
		return this._article;
	}

	public void setArticle(Article article)
	{
		this._article = article;
	}
	
	


	public void reinit()
	{
		remove("rfid");
		remove("articleType");
		remove("extensionsTable");
		
		init();
	}
	
}
