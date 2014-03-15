package com.android.terminators;

import com.android.terminators.ZeroDayNews.R;
import com.android.terminators.reddit.*;
import com.android.terminators.rss.*;
import com.google.android.gms.ads.*;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Zero Day News
 * @author Brandon
 * @author Brian
 * @author Derrick
 */
public class MainActivity extends FragmentActivity
{
  private TextView titleTxt;
  private Button rssBtn, redditBtn, addFeedBtn;
  private AdView adView;
  private static final String AD_UNIT_ID = "ca-app-pub-5178282085023497/1033225563";

  @Override
  public void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    addAd();

    titleTxt = (TextView)findViewById(R.id.appTitle);

    rssBtn = (Button)findViewById(R.id.rssButton);
    rssBtn.setOnClickListener(rssListener);

    redditBtn = (Button)findViewById(R.id.redditButton);
    redditBtn.setOnClickListener(redditListener);
    
    addFeedBtn = (Button)findViewById(R.id.addFeedButton);
    addFeedBtn.setOnClickListener(addFeedListener);
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
  
  public void hideElements()
  {
    titleTxt.setVisibility(View.GONE);
    rssBtn.setVisibility(View.GONE);
    redditBtn.setVisibility(View.GONE);
    adView.setVisibility(View.GONE);
    addFeedBtn.setVisibility(View.GONE);
  }
  
  public void showElements()
  {
    titleTxt.setVisibility(View.VISIBLE);
    rssBtn.setVisibility(View.VISIBLE);
    redditBtn.setVisibility(View.VISIBLE);
    adView.setVisibility(View.VISIBLE);
    addFeedBtn.setVisibility(View.VISIBLE);
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
      transaction.addToBackStack(null);
      transaction.commit();
      hideElements();
    }
  };
  
  OnClickListener addFeedListener = new OnClickListener()
  {
    public void onClick(View v)
    {
      AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
      builder.setTitle("Add New Feed");
      builder.setMessage("Enter new feed and select type:");
      final EditText input = new EditText(MainActivity.this);
      builder.setView(input);
      builder.setPositiveButton("Reddit Feed", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface dialog, int id)
        {
          FeedManager.getFeed().addRedditFeed(new Feed(input.getText().toString()));
        }
      });
      builder.setNegativeButton("RSS Feed", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface dialog, int id)
        {
          FeedManager.getFeed().addRssFeed(new Feed(input.getText().toString()));
        }
      });
      AlertDialog dialog = builder.create();
      dialog.show();
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
