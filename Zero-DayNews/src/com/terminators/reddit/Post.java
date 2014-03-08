package com.terminators.reddit;


/**
 * Class that holds data for JSON objects returned by Reddit API.
 * @author Brandon
 * @author Hathy
 *
 */

//Commented out stuff that would not be useful for our purposes.  Can review this later.

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
	//int numComments;
	//int points;

	String getDetails() 
	{
		String details = author + "posted this";// and got " + numComments + " replies";
		return details;
	}

	String getTitle()
	{
		return title;
	}
/*
	String getScore()
	{
		return Integer.toString(points);
	}
	*/
}