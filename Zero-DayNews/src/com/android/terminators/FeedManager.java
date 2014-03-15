package com.android.terminators;

import java.util.ArrayList;

/**
 * FeedManager class
 * @author Derrick
 */
public class FeedManager
{
  private static FeedManager feedManager = null;
  private ArrayList<Feed> rssFeedList;
  private ArrayList<Feed> redditFeedList;
  
  public FeedManager()
  {
    rssFeedList = new ArrayList<Feed>();
    redditFeedList = new ArrayList<Feed>();
    addRssFeed(new Feed("http://feeds.feedburner.com/TechCrunch/"));
    addRssFeed(new Feed("http://rss.slashdot.org/Slashdot/slashdot/"));
    addRssFeed(new Feed("http://feeds.wired.com/wired27b/"));
    addRssFeed(new Feed("http://feeds.feedburner.com/cnet/tcoc/"));
    addRedditFeed(new Feed("technology"));
    addRedditFeed(new Feed("science"));
    addRedditFeed(new Feed("privacy"));
  }

  public static FeedManager getFeed()
  {
    // Singleton pattern
    if (feedManager == null)
      feedManager = new FeedManager();
    return feedManager;
  }

  public ArrayList<Feed> getRssFeed()
  {
    return rssFeedList;
  }

  public ArrayList<Feed> getRedditFeed()
  {
    return redditFeedList;
  }

  public void addRssFeed(Feed feed)
  {
    if (feed.isEnabled())
      rssFeedList.add(feed);
  }

  public void addRedditFeed(Feed feed)
  {
    if (feed.isEnabled())
      redditFeedList.add(feed);
  }

}
