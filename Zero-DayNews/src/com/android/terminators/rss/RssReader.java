package com.android.terminators.rss;

import java.util.Iterator;
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
    Iterator<Feed> itr = FeedManager.getFeed().getRssFeed().listIterator();
    
    // SAX parse RSS data
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser saxParser = factory.newSAXParser();

    RssParseHandler handler = new RssParseHandler();
    
    while (itr.hasNext())
      saxParser.parse(itr.next().toString(), handler);

    return handler.getItems();
  }

}
