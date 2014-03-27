package com.android.terminators.rss;

import com.android.terminators.Article;
import com.android.terminators.FeedManager;
import com.android.terminators.ListListener;
import com.android.terminators.ZeroDayNews.R;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

/**
 * Main application activity.
 * 
 * Update: Downloading RSS data in an async task 
 * 
 * @author ITCuties
 * 
 * Modified by:
 * @author Derrick
 * Made many changes
 * See git history
 * 
 */

public class RssBuilder extends Activity
{
  // A reference to the local object
  private RssBuilder local;
  private final String TAG = RssBuilder.class.getSimpleName();

  /** 
   * This method creates main application view
   */
  @Override
  public void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    // Set view
    setContentView(R.layout.article_list);

    // Set reference to this activity
    local = this;

    GetRSSDataTask task = new GetRSSDataTask();

    // Start download RSS task
    task.execute();

    // Debug the thread name
    Log.d("ITCRssReader", Thread.currentThread().getName());
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
        FeedManager.getInstance().configureFeeds(this);
        break;
      case R.id.action_addFeed:
        FeedManager.getInstance().addFeed(this);
        break;
      case R.id.action_refresh:
        finish();
        startActivity(getIntent());
        Toast.makeText(getApplicationContext(), "Feed refreshed", Toast.LENGTH_SHORT).show();
        break;
      default:
        break;
    }
    return true;
  }

  private class GetRSSDataTask extends AsyncTask<String, Void, List<Article> > 
  {
    @Override
    protected List<Article> doInBackground(String... urls) 
    {
      // Debug the task thread name
      Log.d("ITCRssReader", Thread.currentThread().getName());

      try
      {
        // Create RSS reader
        RssReader rssReader = new RssReader();

        // Parse RSS, get items
        return rssReader.getItems();
      }
      catch (Exception e)
      {
        Log.e("ITCRssReader", e.getMessage());
      }

      return null;
    }

    @Override
    protected void onPostExecute(final List<Article> result)
    {
      // Get a ListView from main view
      ListView itcItems = (ListView)findViewById(R.id.article_list);

      // Create a list adapter
      ArrayAdapter<Article> adapter = new ArrayAdapter<Article>(getBaseContext(), R.layout.article, result)
      {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) 
        {
          if (convertView == null)
            convertView = getLayoutInflater().inflate(R.layout.article, null);

          TextView postTitle;
          //ID can be found in post_item.xml
          postTitle = (TextView)convertView.findViewById(R.id.article_title);

          TextView postDetails;
          postDetails = (TextView)convertView.findViewById(R.id.article_details);
          
          postTitle.setText(result.get(position).getTitle());
          postDetails.setText(result.get(position).getDetails());

          return convertView;
        }
      };

      /*
      try
      {
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(adapter);
        animationAdapter.setAbsListView(itcItems);
        itcItems.setAdapter(animationAdapter);
      }
      catch (Exception e)
      {
        Toast.makeText(getBaseContext(), "An error has occurred" , Toast.LENGTH_SHORT).show();
        e.printStackTrace();
      }
      */

      // Set list adapter for the ListView
      itcItems.setAdapter(adapter);

      // Set list view item click listener
      itcItems.setOnItemClickListener(new ListListener(result, local));
      itcItems.setOnItemLongClickListener(new ListListener(result, local));
    }
  }
  
}