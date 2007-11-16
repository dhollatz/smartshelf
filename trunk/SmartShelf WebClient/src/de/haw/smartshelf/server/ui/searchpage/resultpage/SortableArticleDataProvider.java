package de.haw.smartshelf.server.ui.searchpage.resultpage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import wicket.extensions.markup.html.repeater.util.SortParam;
import wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import wicket.model.IModel;
import de.haw.smartshelf.bo.Article;
import de.haw.smartshelf.server.logic.IArticlesHolder;

public class SortableArticleDataProvider extends SortableDataProvider
{
	private static final long serialVersionUID = 1231296502023976453L;

	private IArticlesHolder _articlesHolder;
	
	private static final Comparator<Article> _rfidAscComparator;
	private static final Comparator<Article> _rfidDescComparator;
	
	private static final Comparator<Article> _articleTypeAscComparator;
	private static final Comparator<Article> _articleTypeDescComparator;
	
	static
	{
		_rfidAscComparator = new Comparator<Article>()
		{
			public int compare(Article artcl1, Article artcl2)
			{
				return artcl1.getRfid().compareTo(artcl2.getRfid());
			}

		};
		
		_rfidDescComparator = new Comparator<Article>()
		{
			public int compare(Article artcl1, Article artcl2)
			{
				return artcl2.getRfid().compareTo(artcl1.getRfid());
			}

		};
		
		_articleTypeAscComparator = new Comparator<Article>()
		{
			public int compare(Article artcl1, Article artcl2)
			{
				return artcl1.getArticleType().compareTo(artcl2.getArticleType());
			}

		};
		
		_articleTypeDescComparator = new Comparator<Article>()
		{
			public int compare(Article artcl1, Article artcl2)
			{
				return artcl2.getArticleType().compareTo(artcl1.getArticleType());
			}

		};
		
		
	}
	

	/**
	 * constructor
	 */
	public SortableArticleDataProvider(IArticlesHolder articlesHolder)
	{
		_articlesHolder = articlesHolder;

		setSort("rfid", true);
		setSort("articleType", true);
	}

	protected List<Article> getArticles()
	{
		return _articlesHolder.getArticles();
	}

	/**
	 * @see wicket.extensions.markup.html.repeater.data.IDataProvider#iterator(int,
	 *      int)
	 */
	public Iterator iterator(int first, int count)
	{
		return sort().subList(first, first + (count)).iterator();		
	}

	private List<Article> sort()
	{
		SortParam sp = getSort();
		String prop = sp.getProperty();
		boolean asc = sp.isAscending();
		Comparator<Article> comparator = null;
		
		if (prop == null)
        {
            return getArticles();
        }
        if (prop.equals("rfid"))
        {
            comparator = (asc) ? _rfidAscComparator : _rfidDescComparator;
        }
        else if (prop.equals("articleType"))
        {
            comparator = (asc) ? _articleTypeAscComparator : _articleTypeDescComparator;
        }
        else
        {
        	throw new RuntimeException("uknown sort option [" + prop +
            "]. valid options: [rfid] , [articleType]");
        }
        
        List<Article> articles = new ArrayList<Article>(getArticles());
        Collections.sort(articles, comparator);
        
        return articles;		
	}

	/**
	 * @see wicket.extensions.markup.html.repeater.data.IDataProvider#size()
	 */
	public int size()
	{
		try
		{
			return getArticles().size();
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	/**
	 * @see wicket.extensions.markup.html.repeater.data.IDataProvider#model(java.lang.Object)
	 */
	public IModel model(Object object)
	{
		return new DetachableArticleModel((Article) object);
	}
}
