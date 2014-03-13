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
    private AdRequest adRequest;
    private ShareActionProvider mShareActionProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adView = (AdView)findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        
        //TODO uncomment this for social share.  needs specific information.  need to work with later.
        //Intent sendIntent = new Intent();
        //sendIntent.setAction(Intent.ACTION_SEND);
        //sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        //sendIntent.setType("text/plain");
        //startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.shareto)));

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
        //MenuInflater inflater = getMenuInflater();

        //inflate menu resource file
        getMenuInflater().inflate(R.menu.activity_action_bar, menu);

        //Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        //Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();

        //return true to display menu
        return true;

        //inflater.inflate(R.menu.activity_action_bar, menu);
        //return super.onCreateOptionsMenu(menu);
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
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        titleTxt.setVisibility(View.VISIBLE);
        rssBtn.setVisibility(View.VISIBLE);
        redditBtn.setVisibility(View.VISIBLE);
    }

}
