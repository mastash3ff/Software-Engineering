package com.android.terminators.reddit;

import com.android.terminators.Feed;
import com.android.terminators.FeedManager;
import com.android.terminators.ListListener;
import com.android.terminators.ZeroDayNews.R;
import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Loads posts into a listview
 * @author Hathy
 *
 */
public class PostFragment extends Fragment
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
  }

  public static Fragment newInstance() 
  {
    PostFragment pf = new PostFragment();
    pf.postsHolder = new PostHolder();
    return pf;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
  {
    View v = inflater.inflate(R.layout.posts, container, false);
    postsList = (ListView)v.findViewById(R.id.posts_list);
    return v;
  }

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    //setContentView(R.layout.posts);
    setRetainInstance(true);
  }

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

    if (posts.size() == 0)
    {
      // Must execute network tasks outside the UI
      // thread. So create a new thread.

      new Thread()
      {
        public void run()
        {
          // Iterator<Feed> itr = FeedManager.getFeed().getRedditFeedList().listIterator();
          FeedManager feedManager = FeedManager.getFeed();

          //TODO: current ugly.  might need to add a custom iterator function for type List<Post>
          for (int i = 0; i < feedManager.getRedditFeedList().size(); ++i)
          {
            if (feedManager.getRedditFeed(i).isEnabled())
            {
              postsHolder.setSubReddit(feedManager.getRedditFeed(i).toString());
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
    // Make sure this fragment is still a part of the activity.
    if (getActivity() == null) 
      return;

    adapter = new ArrayAdapter<Post>(getActivity(), R.layout.post_item, posts)
    {
      @Override
      public View getView(int position, View convertView, ViewGroup parent) 
      {
        if (convertView == null)
          convertView = getActivity().getLayoutInflater().inflate(R.layout.post_item, null);

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
    //postsList.setAdapter(adapter);
    ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(adapter);
    animationAdapter.setAbsListView(postsList);
    postsList.setAdapter(animationAdapter);
    
    postsList.setOnItemClickListener(new ListListener<Post>(posts, getActivity()));
    postsList.setOnItemLongClickListener(new ListListener<Post>(posts, getActivity()));
  }

}
