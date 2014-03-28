package com.android.terminators.rss;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.android.terminators.Article;

/**
 * SAX tag handler
 * 
 * Modified by Derrick:
 * Made slight modifications for use with class Article
 * 
 * @author Derrick
 * @author ITCuties
 * 
 */

public class RssParseHandler extends DefaultHandler 
{
  private List<Article> rssItems;

  // Used to reference item while parsing
  private Article currentItem;

  // Parsing title indicator
  private boolean parsingTitle;
  // Parsing link indicator
  private boolean parsingLink;

  public RssParseHandler()
  {
    rssItems = new ArrayList<Article>();
  }

  public List<Article> getItems()
  {
    return rssItems;
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
  {
    if ("item".equals(qName))
    {
      currentItem = new RssArticle();
    }
    else if ("title".equals(qName))
    {
      parsingTitle = true;
    }
    else if ("link".equals(qName)) 
    {
      parsingLink = true;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    if ("item".equals(qName))
    {
      rssItems.add(currentItem);
      currentItem = null;
    }
    else if ("title".equals(qName))
    {
      parsingTitle = false;
    }
    else if ("link".equals(qName))
    {
      parsingLink = false;
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException
  {
    if (parsingTitle)
    {
      if (currentItem != null)
        currentItem.setTitle(new String(ch, start, length));
    }
    else if (parsingLink)
    {
      if (currentItem != null)
      {
        currentItem.setLink(new String(ch, start, length));
        parsingLink = false;
      }
    }
  }

}
