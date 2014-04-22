package com.android.terminators.rss;

import com.android.terminators.Article;

/**
 * RssArticle class
 * Used to instantiate object
 * for RSS Article types
 * since Article is abstract
 * @author Derrick
 *
 */

public class RssArticle extends Article
{
  public RssArticle()
  {
    super();
  }

  @Override
  public String getSubreddit()
  {
    return null;
  }

  @Override
  public String getAuthor()
  {
    return null;
  }

  @Override
  public String getDomain()
  {
    return null;
  }

  @Override
  public String getPermalink()
  {
    return null;
  }

  @Override
  public String getBodyText()
  {
    return null;
  }

  @Override
  public void setSubreddit(String subreddit)
  {    
  }

  @Override
  public void setAuthor(String author)
  {    
  }

  @Override
  public void setDomain(String domain)
  {
  }

  @Override
  public void setPermalink(String permalink)
  {
  }

  @Override
  public void setBodyText(String bodyText)
  {
  }

  @Override
  public void setRedditId(String id)
  {
  }
  
}
