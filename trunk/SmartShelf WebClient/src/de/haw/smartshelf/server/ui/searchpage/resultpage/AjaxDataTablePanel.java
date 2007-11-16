package de.haw.smartshelf.server.ui.searchpage.resultpage;

import java.util.ArrayList;
import java.util.List;

import wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import wicket.extensions.markup.html.repeater.refreshing.Item;
import wicket.markup.html.panel.Panel;
import wicket.model.IModel;
import wicket.model.Model;
import de.haw.smartshelf.bo.Article;
import de.haw.smartshelf.server.logic.IArticlesHolder;


public class AjaxDataTablePanel extends Panel implements IArticlesHolder
{

	private static final long serialVersionUID = -1923717594325941207L;
	private IPageWithArticleSelection _pwas;
	
	private boolean _initialized;
	
	

	public AjaxDataTablePanel(String id, IPageWithArticleSelection pwas)
	{
		super(id);
		_pwas = pwas;
		initialize();
	}


	public AjaxDataTablePanel(String id, IModel model, IPageWithArticleSelection pwas)
	{
		super(id, model);
		_pwas = pwas;
		initialize();
	}


	private void initialize()
	{
		if (!_initialized)
		{
			List columns = new ArrayList();

			columns.add(new AbstractColumn(new Model("Actions"))
			{
				private static final long serialVersionUID = -4957503549965022013L;

				public void populateItem(Item cellItem, String componentId, IModel model)
				{
					cellItem.add(new ViewActionPanel(componentId, model, _pwas));
				}
			});

			columns.add(new PropertyColumn(new Model("RFID"), "rfid", "rfid"));
			columns.add(new PropertyColumn(new Model("Article Type"), "articleType", "articleType"));
			
			columns.add(new AbstractColumn(new Model("Actions"))
			{
				private static final long serialVersionUID = -4957503549965022013L;

				public void populateItem(Item cellItem, String componentId, IModel model)
				{
					cellItem.add(new FindActionPanel(componentId, model, _pwas));
				}
			});
			
			add(new AjaxFallbackDefaultDataTable("table", columns, new SortableArticleDataProvider(this), 5));
			_initialized = true;
		}
	}


	public List<Article> getArticles()
	{
		return _pwas.getArticles();
	}


	public void setArticles(List<Article> articles)
	{
		
	}


//	protected void refresh()
//	{
// super.refresh();
		
// if(_initialized)
// {
// remove("table");
// _initialized = false;
// }
// initialize();
//		System.out.println(getSession());
//		renderPage();
//	}


}
