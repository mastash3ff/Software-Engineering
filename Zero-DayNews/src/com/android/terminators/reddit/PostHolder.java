package com.android.terminators.reddit;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;

/** Class that creates Post objects out of the Reddit API and stores a list of these posts for other classes
 * 
 * @author Hathy
 *
 */
public class PostHolder
{
  /**
   * We will be fetching JSON data from the API.
   */
  private final String URL_TEMPLATE = "http://www.reddit.com/r/SUBREDDIT_NAME/" +
      ".json" + "?after=AFTER";

  private String subreddit;
  private String url;
  private String after = "";

  public void setSubReddit(String sr)
  {
    subreddit = sr;
    after = "";
  }

  /**
   * Generates the actual URL from the template based on the
   * subreddit name and the 'after' property.
   */
  private void generateURL()
  {
    url = URL_TEMPLATE.replace("SUBREDDIT_NAME", subreddit);
    url = url.replace("AFTER", after);
  }

  /**
   * Returns a list of Post objects after fetching data from
   * Reddit using the JSON API.
   * 
   * @return
   */
  public List<Post> fetchPosts()
  {
    String raw = NetworkComm.readContents(url);
    List<Post> list = new ArrayList<Post>();

    try
    {
      JSONObject data = new JSONObject(raw).getJSONObject("data");
      JSONArray children = data.getJSONArray("children");

      //Using this property we can fetch the next set of
      //posts from the same subreddit
      after = data.getString("after");

      for (int i = 0; i < children.length(); i++)
      {
        JSONObject cur = children.getJSONObject(i).getJSONObject("data");

        Post p = new Post();
        p.setTitle(cur.optString("title"));
        p.setLink(cur.optString("url"));
        p.setAuthor(cur.optString("author"));
        p.setSubreddit(cur.optString("subreddit"));
        p.setPermalink(cur.optString("permalink"));
        p.setDomain(cur.optString("domain"));
        p.setId(cur.optString("id"));
        p.setBodyText(cur.optString("body"));

        if(p.getTitle() != null)
          list.add(p);
      }
    }
    catch(Exception e)
    {
      Log.e("fetchPosts()", e.toString());
    }
    return list;
  }

  /**
   * This is to fetch the next set of posts
   * using the 'after' property
   * @return
   */
  public List<Post> fetchMorePosts()
  {
    generateURL();
    return fetchPosts();
  }

}
