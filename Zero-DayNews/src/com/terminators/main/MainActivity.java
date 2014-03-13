package com.terminators.main;

import com.example.zero_daynews.R;
import com.terminators.reddit.*;
import com.android.rss.*;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;


public class MainActivity extends FragmentActivity
{
  private Button rssBtn, redditBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
    rssBtn = (Button)findViewById(R.id.rssButton);
    rssBtn.setOnClickListener(rssListener);
    
    redditBtn = (Button)findViewById(R.id.redditButton);
    redditBtn.setOnClickListener(redditListener);
	}

	//makes use of custom action bar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_action_bar, menu);
		return super.onCreateOptionsMenu(menu);
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
    rssBtn.setVisibility(View.GONE);
    redditBtn.setVisibility(View.GONE);
  }

  @Override
  public void onBackPressed()
  {
    super.onBackPressed();
    rssBtn.setVisibility(View.VISIBLE);
    redditBtn.setVisibility(View.VISIBLE);
  }
  
}
