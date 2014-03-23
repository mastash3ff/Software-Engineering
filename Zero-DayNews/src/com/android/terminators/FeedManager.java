package com.android.terminators;

import java.util.ArrayList;

/**
 * FeedManager class
 * @author Derrick
 * @author Brian
 */
public class FeedManager
{
  private static FeedManager feedManager = null;
  private ArrayList<Feed> rssFeedList = null;
  private ArrayList<Feed> redditFeedList = null;
  
  public FeedManager()
  {
    rssFeedList = new ArrayList<Feed>();
    redditFeedList = new ArrayList<Feed>();
    addFeed(new Feed("http://feeds.feedburner.com/TechCrunch/", Feed.RSS_FEED));
    addFeed(new Feed("http://rss.slashdot.org/Slashdot/slashdot/", Feed.RSS_FEED));
    addFeed(new Feed("http://feeds.wired.com/wired27b/", Feed.RSS_FEED));
    addFeed(new Feed("http://feeds.feedburner.com/cnet/tcoc/", Feed.RSS_FEED));
    addFeed(new Feed("technology", Feed.REDDIT_FEED));
    addFeed(new Feed("science", Feed.REDDIT_FEED));
    addFeed(new Feed("privacy", Feed.REDDIT_FEED));
  }

  public static FeedManager getInstance()
  {
    // Singleton pattern
    if (feedManager == null)
      feedManager = new FeedManager();
    return feedManager;
  }
  
  public ArrayList<Feed> getFeedList(int feedType)
  {
    if (feedType == Feed.RSS_FEED)
      return rssFeedList;
    if (feedType == Feed.REDDIT_FEED)
      return redditFeedList;
    //TODO: error checking
    return null;
  }
  
  public void setFeedList(ArrayList<Feed> feedList, int feedType)
  {
    if (feedType == Feed.RSS_FEED)
      rssFeedList = feedList;
    if (feedType == Feed.REDDIT_FEED)
      redditFeedList = feedList;
  }
  
  public Feed getFeed(int i, int feedType)
  {
    if (feedType == Feed.RSS_FEED)
      return rssFeedList.get(i);
    if (feedType == Feed.REDDIT_FEED)
      return redditFeedList.get(i);
    //TODO: error checking
    return null;
  }
  
  public void addFeed(Feed feed)
  {
    //input validation for feeds; checks for duplicates and exits if one is found
    if (feed.getFeedType() == Feed.RSS_FEED)
    {
      for (int i = 0; i < rssFeedList.size(); ++i)
        if (rssFeedList.get(i).getFeedSite().toLowerCase().equals(feed.getFeedSite().toLowerCase()))
          return;
      rssFeedList.add(feed);
    }
    if (feed.getFeedType() == Feed.REDDIT_FEED)
    {
      for (int i = 0; i < redditFeedList.size(); ++i)
        if (redditFeedList.get(i).getFeedSite().toLowerCase().equals(feed.getFeedSite().toLowerCase()))
          return;
      redditFeedList.add(feed);
    }
  }

}
