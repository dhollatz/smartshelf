package de.haw.smartshelf.server.ui.searchpage.resultpage;

import java.util.ArrayList;
import java.util.List;

import de.haw.smartshelf.server.ui.searchpage.SearchInputModel;

import wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import wicket.extensions.markup.html.repeater.refreshing.Item;
import wicket.model.IModel;
import wicket.model.Model;


public class AjaxDataTablePage extends BasePage
{

	private static final long serialVersionUID = -1923717594325941207L;

	
	
	
	public AjaxDataTablePage(SearchInputModel searchInputModel)
	{
		super(searchInputModel);
		
		initialize();
	}




	private void initialize()
	{
		List columns = new ArrayList();

		columns.add(new AbstractColumn(new Model("Actions"))
		{
			private static final long serialVersionUID = -4957503549965022013L;

			public void populateItem(Item cellItem, String componentId, IModel model)
			{
				cellItem.add(new ActionPanel(componentId, model));
			}
		});

		columns.add(new PropertyColumn(new Model("RFID"), "rfid", "rfid"));
		columns.add(new PropertyColumn(new Model("Article Type"), "articleType", "articleType"));

		add(new AjaxFallbackDefaultDataTable("table", columns, new SortableArticleDataProvider(this), 8));
	}




	
}
