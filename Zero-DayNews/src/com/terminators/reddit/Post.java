package com.terminators.reddit;


/**
 * Class that holds data for JSON objects returned by Reddit API.
 * @author Brandon
 * @author Hathy
 *
 */

//represents data structure of JSON
//https://github.com/reddit/reddit/wiki/JSON
public class Post
{
	String subreddit;
	String title;
	String author;
	String permalink;
	String url;
	String domain;
	String id;
	String body;

	String getDetails() 
	{
		String details = author + "posted this";
		return details;
	}

	String getTitle()
	{
		return title;
	}
	
	String getBodyText()
	{
		return body;
	}

}