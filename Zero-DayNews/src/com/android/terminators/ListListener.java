package com.android.terminators;

import java.util.List;

import com.android.terminators.reddit.Post;
import com.android.terminators.rss.RssItem;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * Class implements a list listener
 * 
 * @author ITCuties
 *
 */

public class ListListener<T> implements OnItemClickListener, OnItemLongClickListener
{
  // List item's reference
  private List<T> listItems;
  // Calling activity reference
  private Activity activity;

  public ListListener(List<T> aListItems, Activity anActivity)
  {
    listItems = aListItems;
    activity  = anActivity;
  }
  
  public List<T> getList()
  {
    return listItems;
  }
  
  public Activity getActivity()
  {
    return activity;
  }

  /**
   * Start a browser with url from the rss item.
   */
  public void onItemClick(AdapterView<?> parent, View view, int pos, long id)
  {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    if (getList().get(0) instanceof RssItem)
      intent.setData(Uri.parse(((RssItem)getList().get(pos)).getLink()));
    else if (getList().get(0) instanceof Post)
      intent.setData(Uri.parse(((Post)getList().get(pos)).getLink()));
    getActivity().startActivity(intent);
  }
  
  public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id)
  {
    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setType("text/plain");
    if (getList().get(0) instanceof RssItem)
      shareIntent.putExtra(Intent.EXTRA_TEXT, ((RssItem)getList().get(pos)).getLink());
    else if (getList().get(0) instanceof Post)
      shareIntent.putExtra(Intent.EXTRA_TEXT, ((Post)getList().get(pos)).getBodyText());
    getActivity().startActivity(shareIntent);
    return true;
  }

}
