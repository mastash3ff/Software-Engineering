package com.android.terminators.reddit;

import com.android.terminators.Article;

/**
 * RedditArticle class
 * Derived class of Article for all Reddit posts
 * @author Brian
 * @author Derrick
 */

public class RedditArticle extends Article
{
	private String subreddit;
	private String author;
	private String permalink;
	private String domain;
  private String bodyText;
  private String redditId;

	/*
	 * Getters and setters
	 */
	public String getSubreddit()
	{
		return subreddit;
	}

	public String getAuthor()
	{
		return author;
	}

	public String getDomain()
	{
		return domain;
	}

	public String getPermalink()
	{
		return permalink;
	}
	
  public String getBodyText()
  {
    return bodyText;
  }
  
  public String getRedditId()
  {
    return redditId;
  }

	public void setSubreddit(String subreddit)
	{
		this.subreddit = subreddit;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public void setDomain(String domain)
	{
		this.domain = domain;
	}

	public void setPermalink(String permalink)
	{
		this.permalink = permalink;
	}
	
  public void setBodyText(String bodyText)
  {
    this.bodyText = bodyText;
  }

  public void setId(String redditId)
  {
    this.redditId = redditId;
  }
  
  @Override
  public String getDetails() 
  {
    return "Posted in /r/" + this.getSubreddit();
  }
  
}