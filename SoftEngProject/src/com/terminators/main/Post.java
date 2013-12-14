package com.terminators.main;


/**
 * Class that holds data for JSON objects returned by Reddit API.
 * @author Brandon
 *
 */
public class Post
{

	String subreddit;
	String title;
	String author;
	String permalink;
	String url;
	String domain;
	String id;
	int points;
	int numComments;


	String getDetails () 
	{

		String details = author + "posted this and got " + numComments + " replies";
		return details;

	}

	String getTitle(){

		return title;

	}

	String getScore(){

		return Integer.toString(points);

	}
}
