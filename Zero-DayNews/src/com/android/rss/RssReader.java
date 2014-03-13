package com.android.rss;

import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Class reads RSS data.
 * 
 * @author ITCuties
 *
 */
public class RssReader 
{
  private String rssUrl;

  /**
   * Constructor
   * 
   * @param rssUrl
   */
  public RssReader(String rssUrl) 
  {
    this.rssUrl = rssUrl;
  }

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
    
    saxParser.parse(rssUrl, handler);

    return handler.getItems();
  }

}
