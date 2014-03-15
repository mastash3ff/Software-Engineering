package com.android.terminators.rss;

import com.android.terminators.Article;

/**
 * This code encapsulates RSS item data.
 * Our application needs title and link data.
 * 
 * @author ITCuties
 *
 * Moved most of this class to Article.
 * @author Derrick
 */
public class RssItem extends Article
{
  @Override
  public String toString() 
  {
    return super.getTitle();
  }
  
  @Override
  public String getDetails()
  {
    return super.getLink();
  }

}
