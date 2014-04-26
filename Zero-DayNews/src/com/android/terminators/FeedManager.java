package com.android.terminators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

/**
 * FeedManager class:
 * Used to manage the lists of Feed objects,
 * add new Feeds, and to configure Feeds.
 * Realizes updateSiteFeed use-case.
 * 
 * @author Derrick
 * @version 1.0
 * @since 3-14-2014
 * @see com.android.terminators.Feed
 * 
 */

public class FeedManager
{
  private static FeedManager feedManager = null;
  private ArrayList<Feed> rssFeedList = null;
  private ArrayList<Feed> redditFeedList = null;
  private int newFeedType = -1; // dummy value
  
  public FeedManager()
  {
    rssFeedList = new ArrayList<Feed>();
    redditFeedList = new ArrayList<Feed>();
    // add the default Feeds
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
    // Singleton design pattern
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
    //TODO: exception handling
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
    //TODO: exception handling
    return null;
  }
  
  public boolean addFeed(Feed feed)
  {
    // input validation for feeds; checks for duplicates and exits if one is found
    if (feed.getFeedType() == Feed.RSS_FEED)
    {
      for (int i = 0; i < rssFeedList.size(); ++i)
        if (rssFeedList.get(i).getFeedSite().toLowerCase(Locale.US).equals(feed.getFeedSite().toLowerCase(Locale.US)))
          return false;
      rssFeedList.add(feed);
    }
    if (feed.getFeedType() == Feed.REDDIT_FEED)
    {
      for (int i = 0; i < redditFeedList.size(); ++i)
        if (redditFeedList.get(i).getFeedSite().toLowerCase(Locale.US).equals(feed.getFeedSite().toLowerCase(Locale.US)))
          return false;
      redditFeedList.add(feed);
    }
    return true;
  }
  
  public void addFeed(final Context context)
  {
    newFeedType = -1; // reset dummy value
    final CharSequence[] items = {"RSS Feed", "Reddit Feed"};
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle("Add New Feed");
    final EditText input = new EditText(context);
    builder.setView(input);
    builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int id)
      {       
        //TODO: exception handling
        if (id == 0)
          newFeedType = Feed.RSS_FEED;
        if (id == 1)
          newFeedType = Feed.REDDIT_FEED;
      }
    });
    builder.setPositiveButton("Add Feed", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int id)
      {
        // check for dummy value; indicates no choice made
        if (newFeedType == -1)
          Toast.makeText(context, "Error: Feed type required", Toast.LENGTH_SHORT).show();
        else
        {
          if (FeedManager.getInstance().addFeed(new Feed(input.getText().toString(), newFeedType)))
            Toast.makeText(context, "Feed added", Toast.LENGTH_SHORT).show();
          else
            Toast.makeText(context, "Duplicate feed detected", Toast.LENGTH_SHORT).show();              
        }
      }
    });
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int id)
      {
        dialog.dismiss();
        Toast.makeText(context, "Action canceled", Toast.LENGTH_SHORT).show();
      }
    });
    AlertDialog dialog = builder.create();
    dialog.show();
  }
  
  /* 
   * This method is called via action bar and allows
   * the user to select the Feed type to configure.
   */
  public void configureFeeds(final Context context)
  {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle("Select feed type to configure:");
    builder.setPositiveButton("Configure RSS Feeds", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int id)
      {
        FeedManager.this.configureRssFeeds(context);
      }
    });
    builder.setNeutralButton("Configure Reddit Feeds", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int id)
      {
        FeedManager.this.configureRedditFeeds(context);
      }
    });
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int id)
      {
        dialog.dismiss();
        Toast.makeText(context, "Action canceled", Toast.LENGTH_SHORT).show();
      }
    });
    AlertDialog dialog = builder.create();
    dialog.show();
  }

  public void configureRssFeeds(final Context context)
  {
    // get items to populate the multi-selection dialog
    CharSequence[] items = new CharSequence[FeedManager.getInstance().getFeedList(Feed.RSS_FEED).size()];
    Iterator<Feed> itr = FeedManager.getInstance().getFeedList(Feed.RSS_FEED).listIterator();
    Feed feed = null;
    boolean[] checkedItems = new boolean[items.length];
    
    // determine which feeds are enabled/disabled so they can be displayed as checked/unchecked
    for (int i = 0; itr.hasNext(); ++i)
    {
      feed = itr.next();
      items[i] = feed.toString();
      checkedItems[i] = feed.isEnabled();
    }
    
    // build and display the alert dialog
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle("Configure RSS Feeds");
    builder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener()
    {
      public void onClick(DialogInterface dialog, int id, boolean isChecked)
      {
        // enable/disable feeds on user click
        if (isChecked)
          FeedManager.getInstance().getFeed(id, Feed.RSS_FEED).enableFeed();
        if (!isChecked)
          FeedManager.getInstance().getFeed(id, Feed.RSS_FEED).disableFeed();
      }
    });
    builder.setPositiveButton("Done", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int id)
      {
        dialog.dismiss();
      }
    });
    AlertDialog dialog = builder.create();
    dialog.show();
  }

  /* 
   * TODO: merge this with with configureRssFeeds()
   * or move most of the code to a utility method
   */
  public void configureRedditFeeds(final Context context)
  {
    // get items to populate the multi-selection dialog
    CharSequence[] items = new CharSequence[FeedManager.getInstance().getFeedList(Feed.REDDIT_FEED).size()];
    Iterator<Feed> itr = FeedManager.getInstance().getFeedList(Feed.REDDIT_FEED).listIterator();
    Feed feed = null;
    boolean[] checkedItems = new boolean[items.length];
    
    // determine which feeds are enabled/disabled so they can be displayed as checked/unchecked
    for (int i = 0; itr.hasNext(); ++i)
    {
      feed = itr.next();
      items[i] = feed.toString();
      checkedItems[i] = feed.isEnabled();
    }
    
    // build and display the alert dialog
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle("Configure Reddit Feeds");
    builder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener()
    {
      public void onClick(DialogInterface dialog, int id, boolean isChecked)
      {
        // enable/disable feeds on user click
        if (isChecked)
          FeedManager.getInstance().getFeed(id, Feed.REDDIT_FEED).enableFeed();
        if (!isChecked)
          FeedManager.getInstance().getFeed(id, Feed.REDDIT_FEED).disableFeed();
      }
    });
    builder.setPositiveButton("Done", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int id)
      {
        dialog.dismiss();
      }
    });
    AlertDialog dialog = builder.create();
    dialog.show();
  }

}
