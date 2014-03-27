package com.android.terminators.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.android.terminators.Feed;
import com.android.terminators.FeedManager;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

/**
 * Custom Storage class that has methods pertaining to Creation, Retrieval, and Storage of Feeds
 * on the user's Android Device.
 * @author Brandon 
 * @date 3/24/14
 */

public class StorageFeed implements Storage
{
	private static StorageFeed storageFeed = null;
	private static String feedDirectory = 
			"/Android/data/com.android.terminators/feed/"; 
	private final static String FILENAME = "storagelinks.txt";
	private static final String TAG = StorageFeed.class.getSimpleName();

	/*
	 * Make sure the SD Card is available and we can write to it
	 * Once confirmed, create the storage directory if it does not
	 * exist yet
	 */
	static 
	{
		if(Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED))
		{
			feedDirectory = Environment.getExternalStorageDirectory()
					+ feedDirectory;
			File f = new File( feedDirectory );
			f.mkdirs();
		}
	}

	/**
	 * Uses Singleton pattern
	 * @return
	 */
	public static StorageFeed getInstance()
	{
		if ( storageFeed == null )
			storageFeed = new StorageFeed();
		return storageFeed;
	}

	/**
	 * Sets the file path to the cache directory.
	 * @param feedDirectory
	 */
	public static void setFeedDirectory(String feedDirectory)
	{
		StorageFeed.feedDirectory = feedDirectory;
	}

	/**
	 * Utility method to determine if storagelinks.txt exists on Android Storage
	 * @return
	 */
	@Override
	public Boolean checkIfStorageFileExists()
	{
		File f = new File ( feedDirectory, FILENAME );

		if ( f.exists() ) 
			return true;
		else
			return false;
	}


	/**
	 * Clear contents storagelinks.txt 
	 */
	@Override
	public void clear()
	{
		File f = new File ( feedDirectory, FILENAME );

		if ( f.isDirectory() ) 
			f.delete();
	}

	@Override
	public String convertToStorageName(String url)
	{
		return null;
	}

	/**
	 * Returns the name of file aka storagelinks.txt
	 * @return
	 */
	public String getFilename()
	{
		return FILENAME;
	}

	/**
	 * Fetches the Cache Directory which is the system's file path to the applications folder.
	 * @return
	 */
	@Override
	public String getStorageDirectory()
	{
		return feedDirectory;
	}

	/**
	 * Returns the tag associated for the log tool
	 * @return
	 */
	@Override
	public String getTag()
	{
		return TAG;
	}

	/**
	 * Checks if external storage is available to at least read
	 * @return
	 */
	@Override
	public boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if external storage is available for read and write
	 * @return
	 */
	@Override
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/** Checks to see if file exists with Feed links if not creates a file called 
	 * StoredLinks.txt on storage
	 * @param context
	 */
	public void loadStorage( Context context )
	{

		Log.d(TAG, "beginning loadStorage...");

		if (!isExternalStorageReadable())
		{
			Toast.makeText(context, "External Storage cannot be read." , Toast.LENGTH_SHORT);
			Log.d(TAG, "Storage is not readable");
			return;
		}

		//reads text file to see what links are saved and returns as a list
		ArrayList<String> listOfStrings = read();

		if ( !checkIfStorageFileExists() ) 
		{
			Toast.makeText(context, "No Saved Feeds Detected on Device" , Toast.LENGTH_SHORT);
			Log.d(TAG, "tried to load non-existent storagelinks.txt file.");
		}
		else
		{
			Log.d(TAG, "File exists!  Begin parsing...");
			String[] feedData;

			for ( int i = 0; i < listOfStrings.size(); ++i )
			{
				//on startup displays what links are already stored and adds them to feed
				feedData = listOfStrings.get(i).split(" ");

				Log.d(TAG, "feedData[" + i + "] contents: " + feedData[i]);

				FeedManager.getInstance().addFeed( new Feed( feedData[0], Integer.parseInt(feedData[1]), 
						Boolean.parseBoolean( feedData[2])));
			}

			Toast.makeText( context, "Saved Feeds Loaded" , Toast.LENGTH_SHORT).show();
			Log.d(TAG, "loadStorage ran");
		}
	}

	/**
	 * Reads the contents of storagelinks.txt file and returns a list of what it read.
	 * @return
	 */
	@Override
	public ArrayList<String> read()
	{
		//filename parameter will only expect reddit and rss text file
		File f = new File( feedDirectory, FILENAME );
		String contents = null;
		BufferedReader br = null;
		ArrayList<String> list = new ArrayList<String>();

		//read contents of file if it does exist and return a list of links
		try
		{
			br = new BufferedReader(new FileReader(f));

			while ( (contents = br.readLine() ) != null) 
			{
				list.add(contents);
			}
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			Log.e(TAG, e.toString());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			Log.e(TAG, e.toString());
		} 
		finally 
		{
			try
			{
				if (br != null)br.close();
			} catch (IOException ex) 
			{
				ex.printStackTrace();
				Log.e(TAG, ex.toString());
			}
		}

		//remove duplicates from file
		for (String string : list)
			Log.d(TAG, "list contents before:  " + 	string);

		Set<String> lStrings = new HashSet<String>(list);
		list.clear();
		list.addAll(lStrings);

		for (String string : list)
			Log.d(TAG, "list contents after:  " + 	string);

		return list;
	}

	@Override
	public byte[] read(String url)
	{
		return null;
	}

	/**
	 * Saves the Feed objects to disk which can be read later.  Utilizes write.
	 * @param context
	 */
	public void saveFeeds(Context context)
	{
		Log.d(TAG, "saveFeeds beginning...");

		if ( !isExternalStorageWritable())
		{
			Log.d(TAG, "Storage is not writable");
			return;
		}

		ArrayList<Feed> feedList = FeedManager.getInstance().getFeedList(Feed.RSS_FEED);

		for (int i = 4; i < feedList.size(); ++i)
			write(feedList.get(i).getFeedSite() + " "
					+ Integer.toString(feedList.get(i).getFeedType()) + " " + feedList.get(i).isEnabled().toString());

		feedList = FeedManager.getInstance().getFeedList(Feed.REDDIT_FEED);

		for (int i = 0; i < feedList.size(); ++i)
			Log.d(TAG, "all the reddit feeds: " + feedList.get(i).getFeedSite() );

		for (int i = 3; i < feedList.size(); ++i)
			write(feedList.get(i).getFeedSite() + " "
					+ Integer.toString(feedList.get(i).getFeedType()) + " " + feedList.get(i).isEnabled().toString());
		
		Toast.makeText( context, "Saved Feeds Successfully" , Toast.LENGTH_SHORT).show();

		Log.d(TAG, "saveFeeds ran");

	}

	/**Writes strings from saveFeeds() to storagelinks.txt
	 * @param link
	 * */
	@Override
	public void write(String feed)
	{

		Log.d(TAG, "beginning write...");

		try
		{
			File f = new File( feedDirectory, FILENAME );
			PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter( f, true )));
			Log.d(TAG, "writing to file: " + feed);
			pw.println( feed );
			pw.close();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			Log.e(TAG, e.toString());
		}
	}

	@Override
	public void write(String url, String data)
	{
		return;		
	}
}  
