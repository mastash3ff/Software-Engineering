package com.terminators.main;

import com.example.zero_daynews.R;
import com.terminators.admob.BannerSample;
import com.terminators.reddit.PostFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import com.google.android.gms.ads.*;

/**
 * Class that holds data for JSON objects returned by Reddit API.
 * @author Brandon
 *
 */

public class MainActivity extends FragmentActivity
{


	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		AdView adView = (AdView) this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		 
		//Intent intent = new Intent(this,BannerSample.class);
		//startActivity(intent);
		//finish();

		addFragment();
	}

	//makes use of custom actionbar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_action_bar, menu);
		return super.onCreateOptionsMenu(menu);
	}

	void addFragment(){
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_holder,
				PostFragment.newInstance("technology"))
				.commit();
	}


}
