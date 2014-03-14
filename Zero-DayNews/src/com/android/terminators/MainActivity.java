package com.android.terminators;

import com.android.terminators.ZeroDayNews.R;
import com.android.terminators.reddit.*;
import com.android.terminators.rss.*;
import com.google.android.gms.ads.*;
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
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;
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
    private Button rssBtn, redditBtn;
    private AdView adView;
    private ShareActionProvider mShareActionProvider;
    private static final String AD_UNIT_ID = "ca-app-pub-5178282085023497/1033225563";

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        addAd();

    /*
    TODO uncomment this for social share.  needs specific information.  need to work with later.
    Intent sendIntent = new Intent();
    sendIntent.setAction(Intent.ACTION_SEND);
    sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
    sendIntent.setType("text/plain");
    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.shareto)));
         */

        titleTxt = (TextView)findViewById(R.id.appTitle);

        rssBtn = (Button)findViewById(R.id.rssButton);
        rssBtn.setOnClickListener(rssListener);

        redditBtn = (Button)findViewById(R.id.redditButton);
        redditBtn.setOnClickListener(redditListener);
    }

    //makes use of custom action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_action_bar, menu);
        return super.onCreateOptionsMenu(menu);

        /* used for Share
    inflate menu resource file
    getMenuInflater().inflate(R.menu.activity_action_bar, menu);

    Locate MenuItem with ShareActionProvider
    MenuItem item = menu.findItem(R.id.menu_item_share);

    Fetch and store ShareActionProvider
    mShareActionProvider = (ShareActionProvider) item.getActionProvider();

    return true to display menu
    return true;
         */
    }

    //used to update shareintent
    private void setShareIntent (Intent shareIntent)
    {
        if (mShareActionProvider != null) 
        {
            mShareActionProvider.setShareIntent(shareIntent);
        }
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
            addFragment();
        }
    };

    void addFragment()
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_holder, PostFragment.newInstance("technology"));
        transaction.addToBackStack(null);
        transaction.commit();
        titleTxt.setVisibility(View.GONE);
        rssBtn.setVisibility(View.GONE);
        redditBtn.setVisibility(View.GONE);
        adView.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        titleTxt.setVisibility(View.VISIBLE);
        rssBtn.setVisibility(View.VISIBLE);
        redditBtn.setVisibility(View.VISIBLE);
        adView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() 
    {
        super.onResume();
        if (adView != null) 
        {
            adView.resume();
        }
    }

    @Override
    public void onPause() 
    {
        if (adView != null)
        {
            adView.pause();
        }
        super.onPause();
    }

    /** Called before the activity is destroyed. */
    @Override
    public void onDestroy() 
    {
        // Destroy the AdView.
        if (adView != null)
        {
            adView.destroy();
        }
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
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.fragment_holder);
        layout.addView(adView);

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest.Builder()
            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            //brandon's emulator test device might need to change to ** if you get problems
            .addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB")
            .build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
        
        
    }
}
