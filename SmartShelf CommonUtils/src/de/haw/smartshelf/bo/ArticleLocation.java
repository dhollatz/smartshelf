/*
 * $HeadURL: ArticleLocation.java $
 *
 * $Author: Jaroslaw Urich $
 * $Date: 09.11.2007 12:35:52 $
 *
 * Copyright 2007 by SmartShelf,
 * Hamburg, Germany.
 * All rights reserved.
 */
package de.haw.smartshelf.bo;

import java.io.Serializable;

/**
 * This class ... Copyright (c) 2007 SmartShelf
 * 
 * @version $ Date: 09.11.2007 12:35:52 $
 * @author <a href="mailto:j_urich@freenet.de">j_urich@freenet.de</a>
 */
public class ArticleLocation implements Serializable
{
	private static final long serialVersionUID = -3687340913536078411L;

	private Shelf _shelf;
	private Article _article;
	private String _cell;
	private String _position;

	public ArticleLocation()
	{

	}

	public ArticleLocation(Article article, Shelf shelf)
	{
		this();
		_article = article;
		_shelf = shelf;
	}

	public ArticleLocation(Article article, Shelf shelf, String cell, String position)
	{
		this(article, shelf);
		_cell = cell;
		_position = position;
	}

	public String getCell()
	{
		return _cell;
	}

	public void setCell(String cell)
	{
		this._cell = cell;
	}

	public String getPosition()
	{
		return _position;
	}

	public void setPosition(String position)
	{
		this._position = position;
	}

	public void setShelf(Shelf shelf)
	{
		this._shelf = shelf;
	}

	public Shelf getShelf()
	{
		return _shelf;
	}

	public void setArticle(Article article)
	{
		this._article = article;
	}

	public Article getArticle()
	{
		return _article;
	}
}
