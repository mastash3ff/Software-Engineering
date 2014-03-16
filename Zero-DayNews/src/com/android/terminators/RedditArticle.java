package com.android.terminators;

/**
 * RedditArticle class
 * Derived class of Article for all Reddit posts
 * @author Brian
 */

public class RedditArticle extends Article {

	private String subreddit;
	private String author;
	private String permalink;
	private String domain;

	/*
	 * Function that returns data to be displayed. To be implemented..
	 * @see com.android.terminators.Article#getDetails()
	 */
	public String getDetails()
	{
		return "";
	}


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
}