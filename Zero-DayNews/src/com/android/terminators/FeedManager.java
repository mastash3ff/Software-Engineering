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
  
  public ArrayList<Feed> getRssFeedList()
  {
    return rssFeedList;
  }

  public ArrayList<Feed> getRedditFeedList()
  {
    return redditFeedList;
  }
  
  public void setRssFeedList(ArrayList<Feed> rssFeedList)
  {
    this.rssFeedList.addAll(rssFeedList);
  }
  
  public void setRedditFeedList(ArrayList<Feed> redditFeedList)
  {
    this.redditFeedList.addAll(redditFeedList);
  }

  public Feed getRssFeed(Integer i)
  {
    return rssFeedList.get(i);
  }

  public Feed getRedditFeed(Integer i)
  {
    return redditFeedList.get(i);
  }

  public void addRssFeed(Feed feed)
  {
	//input validation for feeds; checks for duplicates and exits if one is found
	for (int i = 0; i < rssFeedList.size(); ++i)
	  if (rssFeedList.get(i).getFeedSite().toLowerCase().equals(feed.getFeedSite().toLowerCase()))
	    return;
    rssFeedList.add(feed);
  }

  public void addRedditFeed(Feed feed)
  {
	//input validation for feeds; checks for duplicates and exits if one is found
	for (int i = 0; i < redditFeedList.size(); ++i)
	  if (redditFeedList.get(i).getFeedSite().toLowerCase().equals(feed.getFeedSite().toLowerCase()))
		return;
    redditFeedList.add(feed);
  }

}
