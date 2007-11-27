
package de.haw.smartshelf.server.ui.searchpage.resultpage;

import org.apache.wicket.model.AbstractReadOnlyModel;

import de.haw.smartshelf.bo.Article;


public class DetachableArticleModel extends AbstractReadOnlyModel
{
	private static final long serialVersionUID = 3034362987066805158L;

	private Article _article;
	
	public DetachableArticleModel(Article article)
	{
		this._article = article;
	}

	@Override
	public Object getObject()
	{
		return _article;
	}
	
}
