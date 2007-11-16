package de.haw.smartshelf.server.ui.searchpage;

import wicket.markup.html.form.Form;
import wicket.markup.html.form.ImageButton;
import wicket.markup.html.form.RequiredTextField;
import wicket.markup.html.image.Image;
import wicket.markup.html.link.Link;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.CompoundPropertyModel;
import wicket.model.Model;
import de.haw.smartshelf.server.ui.mainpage.MainPage;
import de.haw.smartshelf.server.ui.searchpage.resultpage.ResultPage;

public class SearchPage extends MainPage
{
	private static final long serialVersionUID = -3289242755548307585L;

	public SearchPage()
	{
		super();
		setPageTitle("SmartShelf Search Page");
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		add(new InputForm("inputForm"));
	}
	
	/**
	 * Form for collecting input.
	 */
	private class InputForm extends Form
	{
		private static final long serialVersionUID = 8433053276108079121L;
		SearchInputModel inputModel = null;
		
		/**
		 * Construct.
		 * 
		 * @param name
		 *            Component name
		 */
		public InputForm(String name)
		{
			super(name);
			inputModel = new SearchInputModel();
			setModel(new CompoundPropertyModel(inputModel));
			
			RequiredTextField rfidField = new RequiredTextField("rfidProperty");
			rfidField.setRequired(false);
			rfidField.setLabel(new Model("RFID"));
			add(rfidField);
			
			RequiredTextField articleTypeField = new RequiredTextField("articleTypeProperty");
			articleTypeField.setRequired(false);
			articleTypeField.setLabel(new Model("Article Type"));
			add(articleTypeField);
			
			RequiredTextField freeTextSearch = new RequiredTextField("freeTextSearchProperty");
			freeTextSearch.setRequired(false);
			freeTextSearch.setLabel(new Model("Free Text Search"));
			add(freeTextSearch);
			

			add(new ImageButton("searchButton"));

			add(new Link("resetButtonLink")
			{
				private static final long serialVersionUID = 2854883336807353940L;

				public void onClick()
				{
					InputForm.this.inputModel.reset();
				}
			}.add(new Image("resetButtonImage")));
		}
		
		

		/**
		 * @see wicket.markup.html.form.Form#onSubmit()
		 */
		public void onSubmit()
		{
			// Form validation successful. Display message showing edited model.
//			info("Saved model " + getModelObject());
			setResponsePage(new ResultPage(inputModel));
		}
	}
}
