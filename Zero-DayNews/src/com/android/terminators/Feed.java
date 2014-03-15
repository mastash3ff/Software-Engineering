package com.android.terminators;

/**
 * Feed class
 * @author Derrick
 */
public class Feed
{
  private String feedSite = "";
  private boolean enabled = true;
  
  public Feed(String feedSite)
  {
    this.setFeedSite(feedSite);
  }
  
  public String getFeedSite()
  {
    return feedSite;
  }

  public void setFeedSite(String feedSite)
  {
    this.feedSite = feedSite;
  }

  public Boolean isEnabled()
  {
    return enabled;
  }
  
  public void enableFeed()
  {
    enabled = true;
  }
  
  public void disableFeed()
  {
    enabled = false;
  }
  
  @Override
  public String toString()
  {
    return feedSite;
  }

}
