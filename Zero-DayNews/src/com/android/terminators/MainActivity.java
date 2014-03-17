package com.android.terminators;

import java.util.Iterator;
import java.util.LinkedList;
import com.android.terminators.ZeroDayNews.R;
import com.android.terminators.reddit.*;
import com.android.terminators.rss.*;
import com.android.terminators.storage.StorageLinks;
import com.google.android.gms.ads.*;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Zero Day News
 * @author Brandon
 * @author Brian
 * @author Derrick
 */
public class MainActivity extends FragmentActivity
{
  private TextView titleTxt;
  private Button rssBtn, redditBtn, addFeedBtn, configureRssFeedsBtn, configureRedditFeedsBtn;
  private AdView adView;
  private static final String AD_UNIT_ID = "ca-app-pub-5178282085023497/1033225563";
  private LinkedList<String> linkListOfStrings;

  @Override
  public void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    addAd();
    //initializeStorage();

    titleTxt = (TextView)findViewById(R.id.appTitle);

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
        configureRssFeeds();
        break;
      case R.id.action_addFeed:
        addFeed();
        break;
      default:
        break;
    }
    return true;
  }

  public void hideElements()
  {
    titleTxt.setVisibility(View.GONE);
    rssBtn.setVisibility(View.GONE);
    redditBtn.setVisibility(View.GONE);
    adView.setVisibility(View.GONE);
    addFeedBtn.setVisibility(View.GONE);
    configureRssFeedsBtn.setVisibility(View.GONE);
    configureRedditFeedsBtn.setVisibility(View.GONE);
  }

  public void showElements()
  {
    titleTxt.setVisibility(View.VISIBLE);
    rssBtn.setVisibility(View.VISIBLE);
    redditBtn.setVisibility(View.VISIBLE);
    adView.setVisibility(View.VISIBLE);
    addFeedBtn.setVisibility(View.VISIBLE);
    configureRssFeedsBtn.setVisibility(View.VISIBLE);
    configureRedditFeedsBtn.setVisibility(View.VISIBLE);
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
      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
          .add(R.id.fragment_holder, PostFragment.newInstance());
      transaction.addToBackStack("reddit");
      transaction.commit();
      hideElements();
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
  public void onBackPressed()
  {
    super.onBackPressed();
    showElements();
  }

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
    //storageNotification();
    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    builder.setTitle("Add New Feed");
    builder.setMessage("Enter new feed:");
    final EditText input = new EditText(MainActivity.this);
    builder.setView(input);
    builder.setPositiveButton("Add as Reddit Feed", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int id)
      {
        FeedManager.getFeed().addRedditFeed(new Feed(input.getText().toString()));
        //StorageLinks.writeStoredFeeds(input.getText().toString());
      }
    });
    builder.setNeutralButton("Add as RSS Feed", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int id)
      {
        FeedManager.getFeed().addRssFeed(new Feed(input.getText().toString()));
        //StorageLinks.writeStoredFeeds(input.getText().toString());
      }
    });
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int id)
      {
        dialog.dismiss();
      }
    });
    AlertDialog dialog = builder.create();
    dialog.show();
  }

  public void configureRssFeeds()
  {
    //TODO: this can likely be cleaned up some.  might need to implement a custom iterator function
    CharSequence[] items = new CharSequence[FeedManager.getFeed().getRssFeedList().size()];
    Iterator<Feed> itr = FeedManager.getFeed().getRssFeedList().listIterator();
    Feed feed = null;
    boolean[] checkedItems = new boolean[items.length];
    for (int i = 0; itr.hasNext(); ++i)
    {
      feed = itr.next();
      items[i] = feed.toString();
      checkedItems[i] = feed.isEnabled();
    }
    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    builder.setTitle("Configure RSS Feeds");
    builder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener()
    {
      public void onClick(DialogInterface dialog, int id, boolean isChecked)
      {
        if (isChecked)
          FeedManager.getFeed().getRssFeed(id).enableFeed();
        else if (!isChecked)
          FeedManager.getFeed().getRssFeed(id).disableFeed();
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
    //TODO: this can likely be cleaned up some.  might need to implement a custom iterator function
    CharSequence[] items = new CharSequence[FeedManager.getFeed().getRedditFeedList().size()];
    Iterator<Feed> itr = FeedManager.getFeed().getRedditFeedList().listIterator();
    Feed feed = null;
    boolean[] checkedItems = new boolean[items.length];
    for (int i = 0; itr.hasNext(); ++i)
    {
      feed = itr.next();
      items[i] = feed.toString();
      checkedItems[i] = feed.isEnabled();
    }
    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    builder.setTitle("Configure Reddit Feeds");
    builder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener()
    {
      public void onClick(DialogInterface dialog, int id, boolean isChecked)
      {
        if (isChecked)
          FeedManager.getFeed().getRedditFeed(id).enableFeed();
        else if (!isChecked)
          FeedManager.getFeed().getRedditFeed(id).disableFeed();
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

  /** Checks to see if file exists with Feed links if not creates a file called 
   * StoredLinks.txt on storage
   */
  public void initializeStorage()
  {
    //if file not found, create a file called StoredLinks.txt
    if ( StorageLinks.checkIfFileExists() == false ) 
    {
      StorageLinks.createFile();
      return;
    }
    else
    {
      linkListOfStrings = StorageLinks.readStoredFeeds();
      for ( int i = 0; i < linkListOfStrings.size(); ++i)
      {
        //on startup displays what links are already stored and adds them to feed
        Toast.makeText(MainActivity.this, "Saved Feed: " + linkListOfStrings.get(i), Toast.LENGTH_SHORT).show();
        FeedManager.getFeed().addRedditFeed(new Feed( onlyRedditFeeds(linkListOfStrings).get(i)));
        FeedManager.getFeed().addRssFeed(new Feed( onlyRSSFeeds(linkListOfStrings).get(i)));
      }
    }
  }

  /**
   *  Reminder message of what is already stored on file when user presses one of the Add Feed Buttons 
   * */
  public void storageNotification()
  {
    linkListOfStrings = StorageLinks.readStoredFeeds();

    //notify user if no feeds are detected upon pressing an enter feed button
    if ( linkListOfStrings.isEmpty() )
    {
      Toast.makeText(MainActivity.this, "Zero Feeds Detected on Device" , Toast.LENGTH_SHORT).show();
    }
    else
    {
      //notifies user of what is already stored
      for ( int i = 0; i < linkListOfStrings.size(); ++i )
      {
        Toast.makeText(MainActivity.this, "Already Stored: " + linkListOfStrings.get(i), Toast.LENGTH_SHORT).show();
      }   
    }
  }

  /**
   * Helper function to remove 'http' links from RSS and return only Reddit links
   * @param list
   * @return
   */
  public LinkedList<String> onlyRedditFeeds( LinkedList<String> list )
  {
    String tmp;
    for ( int i = 0; i < list.size(); ++i)
    {
      tmp = list.get(i);
      boolean hazTroof = tmp.startsWith("http");
      if (hazTroof == true)
        list.remove(i);
    }
    return list;
  }

  /**
   * Helper function to remove reddits and only contain RSS links
   * @param list
   * @return
   */
  public LinkedList<String> onlyRSSFeeds( LinkedList<String> list )
  {
    String tmp;
    for ( int i = 0; i < list.size(); ++i)
    {
      tmp = list.get(i);
      if (tmp.startsWith("http")) list.remove(i);
    }
    return list;
  }
}