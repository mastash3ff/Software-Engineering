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
 * Loads articles into a listview
 * @author Hathy
 * 
 * Modified by:
 * @author Derrick
 * Made extensive changes
 * See git history
 */
public class RedditBuilder extends Activity
{
  private ListView articleList;
  private ArrayAdapter<RedditArticle> adapter;
  private Handler handler;
  private List<RedditArticle> articles;
  private ArticleHolder articleHolder;

  public RedditBuilder()
  {
    handler = new Handler();
    articles = new ArrayList<RedditArticle>();
    articleHolder = new ArticleHolder();
  }

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.article_list);
    articleList = (ListView)findViewById(R.id.article_list);
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

  private void initialize() 
  {
    // This should run only once for the fragment as the
    // setRetainInstance(true) method has been called on
    // this fragment

    if (articles.size() == 0)
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
              articleHolder.setSubReddit(feedManager.getFeed(i, Feed.REDDIT_FEED).toString());
              articles.addAll(articleHolder.fetchMoreArticles());
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
   * This method creates the adapter from the list of articles and assigns it to the list.
   */
  private void createAdapter()
  {
    adapter = new ArrayAdapter<RedditArticle>(getBaseContext(), R.layout.article, articles)
    {
      @Override
      public View getView(int position, View convertView, ViewGroup parent) 
      {
        if (convertView == null)
          convertView = getLayoutInflater().inflate(R.layout.article, null);

        TextView articleTitle;
        //ID can be found in post_item.xml
        articleTitle = (TextView)convertView.findViewById(R.id.article_title);

        TextView articleDetails;
        articleDetails = (TextView)convertView.findViewById(R.id.article_details);

        articleTitle.setText(articles.get(position).getTitle());
        articleDetails.setText(articles.get(position).getDetails());

        return convertView;
      }
    };
    
    /*
    ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(adapter);
    animationAdapter.setAbsListView(articleList);
    articleList.setAdapter(animationAdapter);
    */
    
    articleList.setAdapter(adapter);
    articleList.setOnItemClickListener(new ListListener<RedditArticle>(articles, this));
    articleList.setOnItemLongClickListener(new ListListener<RedditArticle>(articles, this));
  }

}
