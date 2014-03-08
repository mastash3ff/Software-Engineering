package com.terminators.reddit;

import java.util.ArrayList;
import java.util.List;
import com.example.zero_daynews.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


/**
 * Loads posts into a listview
 * @author Brandon
 * @author Hathy
 *
 */

//edited out some post score stuff

public class PostFragment extends Fragment
{

	ListView postsList;
	ArrayAdapter<Post> adapter;
	Handler handler;

	String subreddit;
	List<Post> posts;
	PostHolder postsHolder;
	
	private View redditView;

	public PostFragment()
	{
		handler = new Handler();
		posts = new ArrayList<Post>();
	}

	public static Fragment newInstance( String subreddit ) 
	{
		PostFragment pf = new PostFragment();
		pf.subreddit = subreddit;
		pf.postsHolder = new PostHolder( pf.subreddit);

		return pf;

	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) 
	{
		View v = inflater.inflate( R.layout.posts, container, false );
		postsList = (ListView)v.findViewById( R.id.posts_list );

		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.post_item);
		setRetainInstance(true);
		
		//TODO might not need this, set listeners for the clickable posts
		//redditView = (View) findViewById(R.id.post_details);
		//redditView.setOnClickListener(redditListener);
		
	}
	
    private View findViewById(int postDetails) {
		// TODO Auto-generated method stub
		return null;
	}

	//TODO need to implement listener for clickable posts that react to accordingly
	OnClickListener redditListener = new OnClickListener()
	{
		public void onClick(View v)
		{
			
			//distinguish between reddit URL and text post then setup intent
			
			// postview clicked
			//check if that post has a non-reddit url
			//open that url like a webpage with an intent
			//else open text body with intent
				
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
					Uri.parse("http://www.newsherald.com/entertainment"));
			
			startActivity(intent);
		}
	};

	//TODO need to implement listener for clickable posts that react to accordingly
	OnClickListener rssListener = new OnClickListener()
	{
		public void onClick(View v)
		{
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
					Uri.parse("http://www.newsherald.com/entertainment"));
			startActivity(intent);
		}
	};
	 
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{   
		super.onActivityCreated(savedInstanceState);
		initialize();
	}

	private void initialize() 
	{
		// This should run only once for the fragment as the
		// setRetainInstance(true) method has been called on
		// this fragment

		if( posts.size() == 0)
		{

			// Must execute network tasks outside the UI
			// thread. So create a new thread.

			new Thread(){
				public void run()
				{
					posts.addAll(postsHolder.fetchPosts());

					// UI elements should be accessed only in
					// the primary thread, so we must use the
					// handler here.

					handler.post(new Runnable()
					{
						public void run(){
							createAdapter();
						}
					});
				}
			}.start();
		}
		else
		{
			createAdapter();
		}
	}

	/**
	 * This method creates the adapter from the list of posts and assigns it to the list.
	 */
	private void createAdapter()
	{

		// Make sure this fragment is still a part of the activity.
		if( getActivity() == null ) 
			return;

		adapter = new ArrayAdapter<Post>( getActivity(), R.layout.post_item, posts)
				{
			@Override
			public View getView( int position, View convertView, ViewGroup parent) 
			{
				if( convertView == null )
				{
					convertView=getActivity().getLayoutInflater().inflate( R.layout.post_item, null );
				}

				TextView postTitle;
				//ID can be found in post_item.xml
				postTitle = (TextView)convertView.findViewById( R.id.post_title );

				TextView postDetails;
				postDetails = (TextView)convertView.findViewById( R.id.post_details );

				//		TextView postScore;
				//		postScore = (TextView)convertView.findViewById(R.id.post_score);

				postTitle.setText( posts.get( position ).title );
				postDetails.setText( posts.get( position ).getDetails() );
				
				//TODO think we set listeners here
				
				//	postScore.setText( posts.get( position ).getScore() );
				return convertView;
			}
				};
				postsList.setAdapter( adapter );
	}
}