package com.android.terminators;

import java.util.Iterator;
import com.android.terminators.ZeroDayNews.R;
import com.android.terminators.reddit.PostFragment;
import com.android.terminators.rss.ITCutiesReaderAppActivity;
import com.android.terminators.storage.StorageLinks;
import com.google.android.gms.ads.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Zero Day News
 * @author Brandon
 * @author Brian
 * @author Derrick
 */
public class MainActivity extends Activity
{
  private Button rssBtn, redditBtn, addFeedBtn, configureRssFeedsBtn, configureRedditFeedsBtn;
  private AdView adView;
  private static final String AD_UNIT_ID = "ca-app-pub-5178282085023497/1033225563";
  private int feedType = -1;

  @Override
  public void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    addAd();

    rssBtn = (Button)findViewById(R.id.rssButton);
    rssBtn.setOnClickListener(rssListener);

    redditBtn = (Button)findViewById(R.id.redditButton);
    redditBtn.setOnClickListener(redditListener);

    addFeedBtn = (Button)findViewById(R.id.addFeedButton);
    addFeedBtn.setOnClickListener(addFeedListener);

    configureRssFeedsBtn = (Button)findViewById(R.id.configureRssFeedsButton);
    configureRssFeedsBtn.setOnClickListener(configureRssFeedsListener);

    configureRedditFeedsBtn = (Button)findViewById(R.id.configureRedditFeedsButton);
    configureRedditFeedsBtn.setOnClickListener(configureRedditFeedsListener);
  }

  //makes use of custom action bar
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu items for use in the action bar
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.activity_action_bar, menu);
    return super.onCreateOptionsMenu(menu);
  }

  //opens the appropriate dialogs when option items are selected
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch (item.getItemId())
    {
      case R.id.action_configureFeeds:
        configureRedditFeeds();
        break;
      case R.id.action_addFeed:
        addFeed();
        break;
      default:
        break;
    }
    return true;
  }

  OnClickListener rssListener = new OnClickListener()
  {
    public void onClick(View v)
    {
      Intent intent = new Intent(getApplicationContext(), ITCutiesReaderAppActivity.class);
      startActivity(intent);
    }
  };

  OnClickListener redditListener = new OnClickListener()
  {
    public void onClick(View v)
    {
      Intent intent = new Intent(getApplicationContext(), PostFragment.class);
      startActivity(intent);
    }
  };

  OnClickListener addFeedListener = new OnClickListener()
  {
    public void onClick(View v)
    {
      addFeed();
    }
  };

  OnClickListener configureRssFeedsListener = new OnClickListener()
  {
    public void onClick(View v)
    {
      configureRssFeeds();
    }
  };

  OnClickListener configureRedditFeedsListener = new OnClickListener()
  {
    public void onClick(View v)
    {
      configureRedditFeeds();
    }
  };

  @Override
  public void onResume() 
  {
    super.onResume();
    if (adView != null) 
      adView.resume();
  }

  @Override
  public void onPause() 
  {
    if (adView != null)
      adView.pause();
    super.onPause();
  }

  /** Called before the activity is destroyed. */
  @Override
  public void onDestroy() 
  {
    // Destroy the AdView.
    if (adView != null)
      adView.destroy();
    super.onDestroy();
  }

  public void addFeed()
  {
    StorageLinks.storageNotification( getApplicationContext() );
    feedType = -1;
    final CharSequence[] items = {"RSS Feed", "Reddit Feed"};
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Add New Feed");
    final EditText input = new EditText(this);
    builder.setView(input);
    builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int id)
      {       
        //TODO: add bounds/error checking
        if (id == 0)
          feedType = Feed.RSS_FEED;
        if (id == 1)
          feedType = Feed.REDDIT_FEED;
      }
    });
    builder.setPositiveButton("Add Feed", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int id)
      {
        if (feedType == -1)
          Toast.makeText(getApplicationContext(), "Error: Feed type required" , Toast.LENGTH_SHORT).show();
        else
        {
          FeedManager.getInstance().addFeed(new Feed(input.getText().toString(), feedType));
          Toast.makeText(getApplicationContext(), "Feed added" , Toast.LENGTH_SHORT).show();      
        }
      }
    });
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int id)
      {
        dialog.dismiss();
        Toast.makeText(getApplicationContext(), "Action canceled" , Toast.LENGTH_SHORT).show();
      }
    });
    AlertDialog dialog = builder.create();
    dialog.show();
  }

  public void configureRssFeeds()
  {
    CharSequence[] items = new CharSequence[FeedManager.getInstance().getFeedList(Feed.RSS_FEED).size()];
    Iterator<Feed> itr = FeedManager.getInstance().getFeedList(Feed.RSS_FEED).listIterator();
    Feed feed = null;
    boolean[] checkedItems = new boolean[items.length];
    for (int i = 0; itr.hasNext(); ++i)
    {
      feed = itr.next();
      items[i] = feed.toString();
      checkedItems[i] = feed.isEnabled();
    }
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Configure RSS Feeds");
    builder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener()
    {
      public void onClick(DialogInterface dialog, int id, boolean isChecked)
      {
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

  public void configureRedditFeeds()
  {
    CharSequence[] items = new CharSequence[FeedManager.getInstance().getFeedList(Feed.REDDIT_FEED).size()];
    Iterator<Feed> itr = FeedManager.getInstance().getFeedList(Feed.REDDIT_FEED).listIterator();
    Feed feed = null;
    boolean[] checkedItems = new boolean[items.length];
    for (int i = 0; itr.hasNext(); ++i)
    {
      feed = itr.next();
      items[i] = feed.toString();
      checkedItems[i] = feed.isEnabled();
    }
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Configure Reddit Feeds");
    builder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener()
    {
      public void onClick(DialogInterface dialog, int id, boolean isChecked)
      {
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
  
  /**
   * Ads an ad to  fragment_holder
   */
  public void addAd()
  {
    // Create an ad.
    adView = new AdView(this);
    adView.setAdSize(AdSize.BANNER);
    adView.setAdUnitId(AD_UNIT_ID);

    // Add the AdView to the view hierarchy. The view will have no size
    // until the ad is loaded.
    RelativeLayout layout = (RelativeLayout)findViewById(R.id.fragment_holder);

    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
      RelativeLayout.LayoutParams.WRAP_CONTENT,
      RelativeLayout.LayoutParams.WRAP_CONTENT);

    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
    layout.addView(adView, params);

    // Create an ad request. Check logcat output for the hashed device ID to
    // get test ads on a physical device.
    AdRequest adRequest = new AdRequest.Builder()
      .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
      .addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB")
      .build();

    // Start loading the ad in the background.
    adView.loadAd(adRequest);
  }
}
