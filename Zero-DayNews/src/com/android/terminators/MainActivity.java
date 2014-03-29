package com.android.terminators;

import com.android.terminators.ZeroDayNews.R;
import com.android.terminators.reddit.RedditBuilder;
import com.android.terminators.rss.RssBuilder;
import com.android.terminators.storage.StorageFeed;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Zero Day News Main Activity launcher page
 * @author Brandon
 * @author Brian
 * @author Derrick
 * @version 1.0
 * @since 3/28/14
 * 
 */

public class MainActivity extends Activity
{
  private Button articlesBtn, rssBtn, redditBtn, addFeedBtn, configureRssFeedsBtn, configureRedditFeedsBtn;
  private AdView adView = null;
  private static final String TAG = MainActivity.class.getSimpleName();
  
  //used to build the help menu, gives the current state of the application
  private Context context;

  @Override
  public void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    articlesBtn = (Button)findViewById(R.id.articlesButton);
    articlesBtn.setOnClickListener(articlesListener);

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

    adView = new AdView(this);
    Ad.addAd(adView, (RelativeLayout)findViewById(R.id.activity_main));

    Log.d(TAG, "onCreate called");
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
        StorageFeed.getInstance().loadStorage(getApplicationContext());
        break;
      case R.id.action_saveFeed:
        StorageFeed.getInstance().saveFeeds(getApplicationContext());
        break;
      case R.id.action_help:
    	context = this;
    	showHelp();
      default:
        break;
    }
    return true;
  }

  OnClickListener articlesListener = new OnClickListener()
  {
    public void onClick(View v)
    {
      Intent intent = new Intent(getApplicationContext(), ArticleAdapter.class);
      startActivity(intent);
    }
  };

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
    Log.d(TAG, "onResume called");
  }

  @Override
  public void onPause() 
  {
    if (adView != null)
      adView.pause();
    super.onPause();
    Log.d(TAG, "onPause called");
  }

  @Override
  public void onDestroy() 
  {
    if (adView != null)
      adView.destroy();
    super.onDestroy();
    Log.d(TAG, "onDestroy called");
  }
  
  //display help overlay, disappears on click
  public void showHelp()
  {
	final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
    dialog.getWindow().setBackgroundDrawableResource(R.drawable.help_transparency);
    dialog.setContentView(R.layout.help_overlay);
    dialog.getWindow().setWindowAnimations(R.style.help_fade);
    
    LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.helpLayout);

    layout.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        dialog.dismiss();
      }
      
	});

    dialog.show();
  }

}
