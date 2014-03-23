package com.android.terminators.rss;

import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import com.android.terminators.Feed;
import com.android.terminators.FeedManager;

/**
 * Class reads RSS data.
 * 
 * @author ITCuties
 *
 */
public class RssReader 
{
  /**
   * Get RSS items.
   * 
   * @return
   */
  public List<RssItem> getItems() throws Exception
  {    
    // SAX parse RSS data
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser saxParser = factory.newSAXParser();

    RssParseHandler handler = new RssParseHandler();
    
    FeedManager feedManager = FeedManager.getInstance();
    for (int i = 0; i < feedManager.getFeedList(Feed.RSS_FEED).size(); ++i)
      if (feedManager.getFeed(i, Feed.RSS_FEED).isEnabled())
        saxParser.parse(feedManager.getFeed(i, Feed.RSS_FEED).toString(), handler);

    return handler.getItems();
  }

}
