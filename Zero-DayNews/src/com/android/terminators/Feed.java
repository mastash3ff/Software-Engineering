package com.android.terminators;

import java.util.ArrayList;

/**
 * Feed class
 * @author Derrick
 */
public class Feed
{
  private ArrayList<String> rssFeedList;
  private ArrayList<String> redditFeedList;
  private String techCrunch = "http://feeds.feedburner.com/TechCrunch/";
  private String slashDot = "http://rss.slashdot.org/Slashdot/slashdot/";
  private String wired = "http://feeds.wired.com/wired27b/";
  private String technology = "technology";
  private String science = "science";

  public Feed()
  {
    rssFeedList = new ArrayList<String>();
    redditFeedList = new ArrayList<String>();
    addRssFeed(techCrunch);
    addRssFeed(slashDot);
    addRssFeed(wired);
    addRedditFeed(technology);
    addRedditFeed(science);
  }

  public ArrayList<String> getRssFeed()
  {
    return rssFeedList;
  }

  public ArrayList<String> getRedditFeed()
  {
    return redditFeedList;
  }

  public void addRssFeed(String feed)
  {
    rssFeedList.add(feed);
  }

  public void addRedditFeed(String feed)
  {
    redditFeedList.add(feed);
  }

}
