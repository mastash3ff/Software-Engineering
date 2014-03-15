package com.android.terminators.reddit;

import com.android.terminators.Article;

/**
 * Class that holds data for JSON objects returned by Reddit API.
 * @author Hathy
 *
 */

//represents data structure of JSON
//https://github.com/reddit/reddit/wiki/JSON
public class Post extends Article
{
  private String subreddit;
  private String author;
  private String permalink;
  private String domain;
  private String id;
  private String body;

  public String getSubreddit()
  {
    return subreddit;
  }
  
  public void setSubreddit(String subreddit)
  {
    this.subreddit = subreddit;
  }

  public String getAuthor()
  {
    return author;
  }
  
  public void setAuthor(String author)
  {
    this.author = author;
  }

  public String getPermalink()
  {
    return permalink;
  }
  
  public void setPermalink(String permalink)
  {
    this.permalink = permalink;
  }
  
  public String getDomain()
  {
    return domain;
  }
  
  public void setDomain(String domain)
  {
    this.domain = domain;
  }
  
  public String getId()
  {
    return id;
  }
  
  public void setId(String id)
  {
    this.id = id;
  }

  public String getBodyText()
  {
    return body;
  }
  
  public void setBodyText(String body)
  {
    this.body = body;
  }
  
  @Override
  public String getDetails() 
  {
    return "Posted in /r/" + this.getSubreddit();
  }

}