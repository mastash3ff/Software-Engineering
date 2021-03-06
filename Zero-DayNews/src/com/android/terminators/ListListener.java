package com.android.terminators;

import java.util.List;
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
 * Modified by Derrick:
 * Converted to template class
 * Added onLongClickListener for sharing to social media
 * @author Derrick
 * @author ITCuties
 */

public class ListListener implements OnItemClickListener, OnItemLongClickListener
{
  // List item's reference
  private List<Article> listItems;
  // Calling activity reference
  private Activity activity;

  public ListListener(List<Article> listItems, Activity activity)
  {
    this.listItems = listItems;
    this.activity  = activity;
  }
  
  public List<Article> getList()
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
    intent.setData(Uri.parse(getList().get(pos).getLink()));
    getActivity().startActivity(intent);
  }
  
  public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id)
  {
    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setType("text/plain");
    shareIntent.putExtra(Intent.EXTRA_TEXT, (getList().get(pos).getLink()));
    getActivity().startActivity(shareIntent);
    return true;
  }

}
