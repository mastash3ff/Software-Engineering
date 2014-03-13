package com.terminators.main;

import com.example.zero_daynews.R;
import com.terminators.admob.BannerSample;
import com.terminators.reddit.*;
import com.android.rss.*;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;

import com.google.android.gms.ads.*;


public class MainActivity extends FragmentActivity
{
  private Button rssBtn, redditBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		AdView adView = (AdView) this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		
    rssBtn = (Button)findViewById(R.id.rssButton);
    //rssBtn.setOnClickListener(rssListener);
    
    redditBtn = (Button)findViewById(R.id.redditButton);
    //redditBtn.setOnClickListener(redditListener);
	}

	//makes use of custom action bar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_action_bar, menu);
		return super.onCreateOptionsMenu(menu);
	}


}
