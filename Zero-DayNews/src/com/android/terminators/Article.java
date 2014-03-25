package com.android.terminators;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Article class
 * Abstract class for use in RssItem and Post classes.
 * @author Derrick
 * @author Brian
 * 
 */

public abstract class Article
{
	static AtomicInteger nextId = new AtomicInteger();
	private int id;
	private String title;
	private String description;
	private String link;

	public Article()
	{
		id = nextId.incrementAndGet();
		title = "";
		description = "";
	}

	public Article(String title, String description)
	{
		id = nextId.incrementAndGet();
		this.title = title;
		this.description = description;
	}

	public void setId(int id)
	{
	  this.id = id;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public String getTitle()
	{
		return title;
	}

	public String getDescription()
	{
		return description;
	}

	public int getID()
	{
		return id;
	}

	public String getLink()
	{
		return link;
	}

  public String getDetails()
  {
    return getLink();
  }
  
  /**
   * used as virtual methods in order to
   * treat RssArticle and RedditArticle
   * objects as base class objects
   */
  public abstract String getSubreddit();
  public abstract String getAuthor();
  public abstract String getDomain();
  public abstract String getPermalink();
  public abstract String getBodyText();
  public abstract void setSubreddit(String subreddit);
  public abstract void setAuthor(String author);
  public abstract void setDomain(String domain);
  public abstract void setPermalink(String permalink);
  public abstract void setBodyText(String bodyText);
  public abstract void setRedditId(String id);

}
