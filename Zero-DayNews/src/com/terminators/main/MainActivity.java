package com.terminators.main;

import com.example.zero_daynews.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity
{

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addFragment();
	}

	void addFragment(){
		getSupportFragmentManager().beginTransaction().add(R.id.fragments_holder,
				PostsFragment.newInstance("askreddit"))
				.commit();
	}

}
