
package de.haw.smartshelf.server.ui.searchpage.resultpage;

import java.util.Iterator;
import java.util.List;

import wicket.markup.html.basic.Label;
import wicket.markup.html.link.Link;
import wicket.markup.html.panel.Panel;
import wicket.model.IModel;
import wicket.model.PropertyModel;
import de.haw.smartshelf.bo.Article;
import de.haw.smartshelf.bo.ArticleExtension;
import de.haw.smartshelf.commonutils.Util;
import de.haw.smartshelf.server.ui.searchpage.SearchInputModel;


public class BasePage extends ResultPage
{
	private Article article;
	private ArticleDetailsPanel _articleDetailsPanel;
	
	
	public BasePage(SearchInputModel searchInputModel)
	{
		super(searchInputModel);
//		add(new Label("selectedLabel", new PropertyModel(this, "selectedArticleLabel")));
		
		_articleDetailsPanel = new ArticleDetailsPanel("articleDetailsPanel");
		add(_articleDetailsPanel);
	}
	
	
	protected void refreshArticleDetailsPanel()
	{
		_articleDetailsPanel.setArticle(article);
		_articleDetailsPanel.reinit();
	}

	/**
	 * @return string representation of selected contact property
	 */
	public String getSelectedArticleLabel()
	{
		if (article == null)
		{
			return "No Contact Selected";
		}
		else
		{
			String articlAsString = "";
			
			articlAsString += "Article RFID: " + article.getRfid() + " TYPE: " + article.getArticleType() + "\r\n";
			List<ArticleExtension> extensions = article.getArticleExtensions();
			if(!Util.isEmpty(extensions))
			{
				for (Iterator iterator = extensions.iterator(); iterator.hasNext();)
				{
					ArticleExtension articleExtension = (ArticleExtension) iterator.next();
					articlAsString += articleExtension.getName() + ": \t" + articleExtension.getValue();
				}
			}
			
			return articlAsString;
		}
	}

	/**
	 * 
	 */
	class ActionPanel extends Panel
	{
		/**
		 * @param id
		 *            component id
		 * @param model
		 *            model for contact
		 */
		public ActionPanel(String id, IModel model)
		{
			super(id, model);
			add(new Link("select")
			{
				public void onClick()
				{
					BasePage.this.article = (Article)getParent().getModelObject();
					BasePage.this.refreshArticleDetailsPanel();
				}
			});
		}
	}

	
}
