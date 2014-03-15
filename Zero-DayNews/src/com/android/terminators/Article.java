package com.android.terminators;

/**
 * Feed class
 * Abstracted RssItem and Post classes.
 * @author Derrick
 */
public abstract class Article
{  
  private String title;
  private String link;

  public String getTitle() 
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getLink()
  {
    return link;
  }

  public void setLink(String link)
  {
    this.link = link;
  }
  
  public abstract String getDetails();
}
