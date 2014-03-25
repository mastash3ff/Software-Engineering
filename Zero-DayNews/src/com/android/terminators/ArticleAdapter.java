package com.android.terminators;

import com.android.terminators.ZeroDayNews.R;
import com.android.terminators.reddit.ArticleHolder;
import com.android.terminators.rss.RssReader;
import android.os.AsyncTask;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
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

/**
 * ArticleAdapter class
 * @author Derrick
 * 
 * Adapter Pattern for
 * RSS and Reddit reader
 * components by:
 * @author ITCuties
 * @author Hathy
 * 
 */

public class ArticleAdapter extends Activity
{
  private ListView articleListView = null;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.article_list);
    articleListView = (ListView)findViewById(R.id.article_list);
    ArticleBuilder articleBuilder = new ArticleBuilder();
    articleBuilder.execute();
  }

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

  private class ArticleBuilder extends AsyncTask<String, Void, List<Article> > 
  {
    private List<Article> articleList = new ArrayList<Article>();
    private ArticleHolder articleHolder = new ArticleHolder();

    @Override
    protected List<Article> doInBackground(String... urls) 
    {
      Log.d("ITCRssReader", Thread.currentThread().getName());
      try
      {
        RssReader rssReader = new RssReader();
        articleList.addAll(rssReader.getItems());
      }
      catch (Exception e)
      {
        Log.e("ITCRssReader", e.getMessage());
      }
      FeedManager feedManager = FeedManager.getInstance();
      for (int i = 0; i < feedManager.getFeedList(Feed.REDDIT_FEED).size(); ++i)
      {
        if (feedManager.getFeed(i, Feed.REDDIT_FEED).isEnabled())
        {
          articleHolder.setSubReddit(feedManager.getFeed(i, Feed.REDDIT_FEED).toString());
          articleList.addAll(articleHolder.fetchMoreArticles());
        }
      }
      return articleList;
    }

    @Override
    protected void onPostExecute(final List<Article> parsedArticleList)
    {
      ArrayAdapter<Article> adapter = new ArrayAdapter<Article>(getBaseContext(), R.layout.article, parsedArticleList)
      {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) 
        {
          if (convertView == null)
            convertView = getLayoutInflater().inflate(R.layout.article, null);

          TextView postTitle;
          postTitle = (TextView)convertView.findViewById(R.id.article_title);

          TextView postDetails;
          postDetails = (TextView)convertView.findViewById(R.id.article_details);

          postTitle.setText(parsedArticleList.get(position).getTitle());
          postDetails.setText(parsedArticleList.get(position).getDetails());

          return convertView;
        }
      };
      articleListView.setAdapter(adapter);
      articleListView.setOnItemClickListener(new ListListener(parsedArticleList, ArticleAdapter.this));
      articleListView.setOnItemLongClickListener(new ListListener(parsedArticleList, ArticleAdapter.this));
    }
  }

}
