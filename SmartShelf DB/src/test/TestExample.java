package test;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.haw.smartshelf.db.data.InitSessionFactory;
import de.haw.smartshelf.db.data.pers.Article;
import de.haw.smartshelf.db.data.pers.ArticleExtensions;
import de.haw.smartshelf.db.data.pers.ArticleExtensionsId;

public class TestExample
{
	private static Logger log = Logger.getLogger(TestExample.class);

	public static void main(String[] args)
	{
		try
		{
//			clean();
//			createArticle();
			createRelation();
//			delete();
//			update();
			query();
		}
		catch (RuntimeException e)
		{
			try
			{
				Session session = InitSessionFactory.getInstance().getCurrentSession();
				if (session.getTransaction().isActive())
					session.getTransaction().rollback();
			}
			catch (HibernateException e1)
			{
				log.error("Error rolling back transaction");
			}
			// throw the exception again
			throw e;
		}
	}

	private static Article createArticle()
	{
		Session session = InitSessionFactory.getInstance().getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		Article article = new Article();
		article.setRfid("123676");
		article.setArticleType("book");
		
		session.save(article);
		tx.commit();
		return article;
	}

	private static void update()
	{
		Article article = createArticle();
		Session session = InitSessionFactory.getInstance().getCurrentSession();
		Transaction tx = session.beginTransaction();
		article.setArticleType("CD");
		session.update(article);
		tx.commit();
	}

	private static void delete()
	{
		Article article = createArticle();
		Session session = InitSessionFactory.getInstance().getCurrentSession();
		Transaction tx = session.beginTransaction();
		session.delete(article);
		tx.commit();
	}

	private static void clean()
	{
		Session session = InitSessionFactory.getInstance().getCurrentSession();
		Transaction tx = session.beginTransaction();
		session.createQuery("delete from ARTICLE").executeUpdate();
		session.createQuery("delete from ARTICLE_EXTENSIONS").executeUpdate();
		session.flush();
		session.clear();
		tx.commit();
	}

	private static void createRelation()
	{
		Session session = InitSessionFactory.getInstance().getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		Article article = new Article("2345");
		article.setArticleType("CD");
		session.save(article);
		ArticleExtensions extns = new ArticleExtensions(
				new ArticleExtensionsId(article.getRfid(), "Title", "The Best"), article);
		session.save(extns);

		/* Wir setzen die Beziehung auf BEIDEN Seiten */
		article.getArticleExtensionses().add(extns);
		tx.commit();
	}

	private static void query()
	{
		Session session = InitSessionFactory.getInstance().getCurrentSession();
		Transaction tx = session.beginTransaction();
		List honeys = session.createCriteria(Article.class).list();
		for (Iterator iter = honeys.iterator(); iter.hasNext();)
		{
			Article element = (Article) iter.next();
			log.debug(element);
		}
		tx.commit();
	}
}
