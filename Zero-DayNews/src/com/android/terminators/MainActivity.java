package com.android.terminators;

import java.util.ArrayList;
import com.android.terminators.ZeroDayNews.R;
import com.android.terminators.reddit.RedditBuilder;
import com.android.terminators.rss.RssBuilder;
import com.android.terminators.storage.StorageLinks;
import com.google.android.gms.ads.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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

  @Override
  public void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

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
    
    addAd();
  }

  //makes use of custom action bar
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu items for use in the action bar
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main, menu);
    return super.onCreateOptionsMenu(menu);
  }

  //opens the appropriate dialogs when option items are selected
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch (item.getItemId())
    {
      case R.id.action_configureFeeds:
        FeedManager.getInstance().configureFeeds(this);
        break;
      case R.id.action_addFeed:
        FeedManager.getInstance().addFeed(this);
        break;
      case R.id.action_loadFeed:
        StorageLinks.initializeStorage(getApplicationContext());
        Toast.makeText(getApplicationContext(), "Feeds Loaded", Toast.LENGTH_SHORT).show();
        break;
      case R.id.action_saveFeed:
        //TODO: this still needs work
        ArrayList<Feed> feedList = FeedManager.getInstance().getFeedList(Feed.RSS_FEED);
        for (int i = 4; i < feedList.size(); ++i)
          StorageLinks.writeStoredFeeds(feedList.get(i).getFeedSite() + " "
            + Integer.toString(feedList.get(i).getFeedType()) + " " + feedList.get(i).isEnabled().toString());

        feedList = FeedManager.getInstance().getFeedList(Feed.REDDIT_FEED);
        for (int i = 3; i < feedList.size(); ++i)
          StorageLinks.writeStoredFeeds(feedList.get(i).getFeedSite() + " "
            + Integer.toString(feedList.get(i).getFeedType()) + " " + feedList.get(i).isEnabled().toString());

        Toast.makeText(getApplicationContext(), "Feeds Saved", Toast.LENGTH_SHORT).show();
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
      Intent intent = new Intent(getApplicationContext(), RssBuilder.class);
      startActivity(intent);
    }
  };

  OnClickListener redditListener = new OnClickListener()
  {
    public void onClick(View v)
    {
      Intent intent = new Intent(getApplicationContext(), RedditBuilder.class);
      startActivity(intent);
    }
  };

  OnClickListener addFeedListener = new OnClickListener()
  {
    public void onClick(View v)
    {
      FeedManager.getInstance().addFeed(MainActivity.this);
    }
  };

  OnClickListener configureRssFeedsListener = new OnClickListener()
  {
    public void onClick(View v)
    {
      FeedManager.getInstance().configureRssFeeds(MainActivity.this);
    }
  };

  OnClickListener configureRedditFeedsListener = new OnClickListener()
  {
    public void onClick(View v)
    {
      FeedManager.getInstance().configureRedditFeeds(MainActivity.this);
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
    RelativeLayout layout = (RelativeLayout)findViewById(R.id.activity_main);

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
