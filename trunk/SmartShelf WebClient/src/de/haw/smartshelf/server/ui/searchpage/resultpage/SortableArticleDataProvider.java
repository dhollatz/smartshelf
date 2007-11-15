
package de.haw.smartshelf.server.ui.searchpage.resultpage;

import java.util.Iterator;
import java.util.List;

import wicket.extensions.markup.html.repeater.util.SortParam;
import wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import wicket.model.IModel;
import de.haw.smartshelf.bo.Article;


public class SortableArticleDataProvider extends SortableDataProvider
{
	private static final long serialVersionUID = 1231296502023976453L;
	
	private ResultPage _page;

	/**
	 * constructor
	 */
	public SortableArticleDataProvider(ResultPage page)
	{
		_page = page;
		// set default sort
		setSort("articleType", true);
	}

	protected List<Article> getArticles()
	{
		return _page.getArticles();
	}

	/**
	 * @see wicket.extensions.markup.html.repeater.data.IDataProvider#iterator(int,
	 *      int)
	 */
	public Iterator iterator(int first, int count)
	{
		SortParam sp = getSort();
		return getArticles().iterator();
	}

	/**
	 * @see wicket.extensions.markup.html.repeater.data.IDataProvider#size()
	 */
	public int size()
	{
		return getArticles().size();
	}

	/**
	 * @see wicket.extensions.markup.html.repeater.data.IDataProvider#model(java.lang.Object)
	 */
	public IModel model(Object object)
	{
		return new DetachableArticleModel((Article)object);
	}

}
