package com.android.terminators;

/**
 * Feed class:
 * Each Feed object represents a site
 * that is scraped for Articles.
 * Realizes enableSiteFeed and
 * disableSiteFeed use-cases.
 * 
 * @author Derrick
 * @version 1.0
 * @since 3-13-2014
 * @see com.android.terminators.FeedManager
 * 
 */

public class Feed
{
  private String feedSite = null;
  private int feedType = -1; // dummy value
  private boolean enabled = true;
  public final static int RSS_FEED = 0, REDDIT_FEED = 1;
  public final static boolean DISABLED_FEED = false, ENABLED_FEED = true;
  
  public Feed(String feedSite, int feedType)
  {
    setFeedSite(feedSite);
    setFeedType(feedType);
  }
  
  // 3-parameter constructor used for loading sites from Storage
  public Feed(String feedSite, int feedType, boolean enabled)
  {
    setFeedSite(feedSite);
    setFeedType(feedType);
    this.enabled = enabled;
  }
  
  public String getFeedSite()
  {
    return feedSite;
  }

  public void setFeedSite(String feedSite)
  {
    this.feedSite = feedSite;
  }
  
  public int getFeedType()
  {
    return feedType;
  }
  
  public void setFeedType(int feedType)
  {
    this.feedType = feedType;
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
