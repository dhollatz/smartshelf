
package de.haw.smartshelf.server.ui.searchpage.resultpage;

import wicket.Component;
import wicket.model.AbstractReadOnlyDetachableModel;
import wicket.model.IModel;
import de.haw.smartshelf.bo.Article;


public class DetachableArticleModel extends AbstractReadOnlyDetachableModel
{
	private static final long serialVersionUID = 3034362987066805158L;

	private Article _article;
	
	public DetachableArticleModel(Article article)
	{
		this._article = article;
	}

	@Override
	public IModel getNestedModel()
	{
		return null;
	}

	@Override
	protected void onAttach()
	{
		
	}

	@Override
	protected void onDetach()
	{
				
	}

	@Override
	protected Object onGetObject(Component arg0)
	{
		return _article;
	}
	
	
}
