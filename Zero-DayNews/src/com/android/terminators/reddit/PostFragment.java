package com.android.terminators.reddit;

import com.android.terminators.Feed;
import com.android.terminators.FeedManager;
import com.android.terminators.ListListener;
import com.android.terminators.ZeroDayNews.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads posts into a listview
 * @author Hathy
 *
 */
public class PostFragment extends Activity
{
  private ListView postsList;
  private ArrayAdapter<Post> adapter;
  private Handler handler;
  private List<Post> posts;
  private PostHolder postsHolder;

  public PostFragment()
  {
    handler = new Handler();
    posts = new ArrayList<Post>();
    postsHolder = new PostHolder();
  }

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.posts);
    postsList = (ListView)findViewById(R.id.posts_list);
    initialize();
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
        // configureRedditFeeds();
        break;
      case R.id.action_addFeed:
        // addFeed();
        break;
      case R.id.action_refresh:
        finish();
        startActivity(getIntent());
        Toast.makeText(getApplicationContext(), "Feed refreshed" , Toast.LENGTH_SHORT).show();
        break;
      default:
        break;
    }
    return true;
  }

  private void initialize() 
  {
    // This should run only once for the fragment as the
    // setRetainInstance(true) method has been called on
    // this fragment

    if (posts.size() == 0)
    {
      // Must execute network tasks outside the UI
      // thread. So create a new thread.

      new Thread()
      {
        public void run()
        {
          FeedManager feedManager = FeedManager.getInstance();
          for (int i = 0; i < feedManager.getFeedList(Feed.REDDIT_FEED).size(); ++i)
          {
            if (feedManager.getFeed(i, Feed.REDDIT_FEED).isEnabled())
            {
              postsHolder.setSubReddit(feedManager.getFeed(i, Feed.REDDIT_FEED).toString());
              posts.addAll(postsHolder.fetchMorePosts());
            }
          }

          // UI elements should be accessed only in
          // the primary thread, so we must use the
          // handler here.

          handler.post(new Runnable()
          {
            public void run()
            {
              createAdapter();
            }
          });
        }
      }.start();
    }
    else
      createAdapter();
  }

  /**
   * This method creates the adapter from the list of posts and assigns it to the list.
   */
  private void createAdapter()
  {
    adapter = new ArrayAdapter<Post>(getBaseContext(), R.layout.post_item, posts)
    {
      @Override
      public View getView(int position, View convertView, ViewGroup parent) 
      {
        if (convertView == null)
          convertView = getLayoutInflater().inflate(R.layout.post_item, null);

        TextView postTitle;
        //ID can be found in post_item.xml
        postTitle = (TextView)convertView.findViewById(R.id.post_title);

        TextView postDetails;
        postDetails = (TextView)convertView.findViewById(R.id.post_details);

        postTitle.setText(posts.get(position).getTitle());
        postDetails.setText(posts.get(position).getDetails());

        return convertView;
      }
    };
    
    /*
    ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(adapter);
    animationAdapter.setAbsListView(postsList);
    postsList.setAdapter(animationAdapter);
    */
    
    postsList.setAdapter(adapter);
    postsList.setOnItemClickListener(new ListListener<Post>(posts, this));
    postsList.setOnItemLongClickListener(new ListListener<Post>(posts, this));
  }

}
