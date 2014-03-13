package com.android.terminators.rss;

import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import com.android.terminators.Feed;

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
    Feed feed = new Feed();
    Iterator<String> itr = feed.getRssFeed().listIterator();
    
    // SAX parse RSS data
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser saxParser = factory.newSAXParser();

    RssParseHandler handler = new RssParseHandler();
    
    while (itr.hasNext())
      saxParser.parse(itr.next(), handler);

    return handler.getItems();
  }

}
