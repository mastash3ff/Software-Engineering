package com.android.terminators;

import java.util.concurrent.atomic.AtomicInteger;

public class Article
{
	static AtomicInteger nextId = new AtomicInteger();
	
	private int id;
	private String title;
	private String description;
	
	public Article ()
	{
		id = nextId.incrementAndGet();
		title = "";
		description = "";
	}
	
	public Article (String t, String d)
	{
		id = nextId.incrementAndGet();
		title = t;
		description = d;
	}
	
	public void setTitle(String t)
	{
		title = t;
	}
	
	public void setDescription(String d)
	{
		description = d;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public int getID()
	{
		return id;
	}
}

