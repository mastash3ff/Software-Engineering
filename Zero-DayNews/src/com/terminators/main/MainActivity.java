package com.terminators.main;
import java.util.List;

import com.android.rss.ITCutiesReaderAppActivity;
import com.android.rss.RssItem;
import com.android.rss.RssReader;
import com.example.zero_daynews.R;
import com.terminators.reddit.PostFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Class that holds data for JSON objects returned by Reddit API.
 * @author Brandon
 *
 */

public class MainActivity extends FragmentActivity
{

	// A reference to the local object
	//private ITCutiesReaderAppActivity local;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//		Intent myIntent = new Intent(getApplicationContext(), ITCutiesReaderAppActivity.class);
		//		startActivity(myIntent);

		//TODO addFragment();
		
		// Set reference to this activity
		//TODO local = this;

		GetRSSDataTask task = new GetRSSDataTask();

		// Start download RSS task
		task.execute("http://feeds.feedburner.com/TechCrunch/");

		// Debug the thread name
		Log.d("RssReader", Thread.currentThread().getName());
		
		
	}
	private class GetRSSDataTask extends AsyncTask<String, Void, List<RssItem> > 
	{
		@Override
		protected List<RssItem> doInBackground(String... urls) 
		{

			// Debug the task thread name
			Log.d("RssReader", Thread.currentThread().getName());

			try
			{
				// Create RSS reader
				RssReader rssReader = new RssReader(urls[0]);

				// Parse RSS, get items
				return rssReader.getItems();

			}
			catch (Exception e)
			{
				Log.e("RssReader", e.getMessage());
			}

			return null;
		}
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
		/*getSupportFragmentManager().beginTransaction().add(R.id.fragment_holder,
				PostFragment.newInstance("technology"))
				.commit();
		 */

		//getSupportFragmentManager().beginTransaction().add(R.id.fragment_holder, arg1)
		//		.commit();
	}

}
