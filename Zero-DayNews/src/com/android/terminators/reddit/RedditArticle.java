package com.android.terminators.reddit;

import com.android.terminators.Article;

/**
 * RedditArticle class
 * Derived class of Article for all Reddit posts
 * @author Brian
 * @author Derrick
 * 
 */

public class RedditArticle extends Article
{
	private String subreddit;
	private String author;
	private String permalink;
	private String domain;
  private String bodyText;
  private String redditId;
  
  public RedditArticle()
  {
    super();
  }

	/*
	 * Getters and setters
	 */
  @Override
	public String getSubreddit()
	{
		return subreddit;
	}

  @Override
	public String getAuthor()
	{
		return author;
	}

  @Override
	public String getDomain()
	{
		return domain;
	}

  @Override
	public String getPermalink()
	{
		return permalink;
	}
	
  @Override
  public String getBodyText()
  {
    return bodyText;
  }
  
  @Override
	public void setSubreddit(String subreddit)
	{
		this.subreddit = subreddit;
	}

  @Override
	public void setAuthor(String author)
	{
		this.author = author;
	}

  @Override
	public void setDomain(String domain)
	{
		this.domain = domain;
	}

  @Override
	public void setPermalink(String permalink)
	{
		this.permalink = permalink;
	}
	
  @Override
  public void setBodyText(String bodyText)
  {
    this.bodyText = bodyText;
  }
  
  public String getRedditId()
  {
    return redditId;
  }

  public void setRedditId(String redditId)
  {
    this.redditId = redditId;
  }
  
  @Override
  public String getDetails() 
  {
    return "Posted in /r/" + this.getSubreddit();
  }
  
}