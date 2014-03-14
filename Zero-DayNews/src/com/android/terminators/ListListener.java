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

/**
 * Class implements a list listener
 * 
 * @author ITCuties
 *
 */

public class ListListener<T> implements OnItemClickListener
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

  /**
   * Start a browser with url from the rss item.
   */
  public void onItemClick(AdapterView<?> parent, View view, int pos, long id)
  {
    Intent i = new Intent(Intent.ACTION_VIEW);
    if (getList().get(0) instanceof RssItem)
      i.setData(Uri.parse(((RssItem)getList().get(pos)).getLink()));
    else if (getList().get(0) instanceof Post)
      i.setData(Uri.parse(((Post)getList().get(pos)).getLink()));
    activity.startActivity(i);
  }
  
  /*
  public void onItemLongClick(AdapterView<?> parent, View view, int pos, long id)
  {
    Intent i = new Intent(Intent.ACTION_SEND);
    i.setData(Uri.parse(listItems.get(pos).getLink()));
    activity.startActivity(i);
  }
  */

}
