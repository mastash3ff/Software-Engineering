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
 * Custom Storage class that has methods pertaining to Creation, Retrieval, and Storage of Feed
 * Links on the user's Android Device.
 * @author Brandon
 */

/**
 * @author Brandon Sheffield 
 * Creation Date: @date
 *
 */
public class StorageLinks implements Storage
{
	static private String cacheDirectory = 
			"/Android/data/com.android.terminators/cache/"; 
	static final private String FILENAME = "storagelinks.txt";
	static final private String TAG = StorageLinks.class.getSimpleName();

	/*
	 * Make sure the SD Card is available and we can write to it
	 * Once confirmed, create the cache directory if it does not
	 * exist yet
	 */
	static 
	{
		if(Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED))
		{
			cacheDirectory = Environment.getExternalStorageDirectory()
					+ cacheDirectory;
			File f = new File( cacheDirectory );
			f.mkdirs();
		}
	}


	/**
	 * Utility method to determine if storagelinks.txt exists on Android Storage
	 * @return
	 */
	public static Boolean checkIfStorageFileExists()
	{
		File f = new File ( cacheDirectory, FILENAME );

		if ( f.exists() ) 
			return true;
		else
			return false;
	}


	/**
	 * Reads the contents of storagelinks.txt file and returns a list of what it read.
	 * @param FILENAME
	 * 
	 * */
	public static ArrayList<String> readStoredFeeds( )
	{
		//filename parameter will only expect reddit and rss text file
		File f = new File( cacheDirectory, FILENAME );
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
			Log.d(TAG, e.toString());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			Log.d(TAG, e.toString());
		} 
		finally 
		{
			try
			{
				if (br != null)br.close();
			} catch (IOException ex) 
			{
				ex.printStackTrace();
				Log.d(TAG, ex.toString());
			}
		}
		
		//lazy haxxor technique for removing duplicates in file
		for (String string : list)
			Log.d(TAG, "list contents before:  " + 	string);
		
		Set<String> lStrings = new HashSet<String>(list);
		list.clear();
		list.addAll(lStrings);
		
		for (String string : list)
			Log.d(TAG, "list contents after:  " + 	string);
		
		return list;
	}

	/** Checks to see if file exists with Feed links if not creates a file called 
	 * StoredLinks.txt on storage
	 * @param context
	 */
	public static void loadStorage( Context context )
	{

		Log.d(TAG, "beginning loadStorage...");
		//reads text file to see what links are saved and returns as a list
		ArrayList<String> listOfStrings = readStoredFeeds();

		if ( !StorageLinks.checkIfStorageFileExists() ) 
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

	/**Writes strings from saveFeeds() to storagelinks.txt
	 * @param link
	 * */
	public static void writeStoredFeeds(String link)
	{

		Log.d(TAG, "beginning writeStoredFeeds...");
		
		try
		{
			File f = new File( cacheDirectory, FILENAME );
			PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter( f, true )));
			Log.d(TAG, "writing to file: " + link);
			pw.println( link );
			pw.close();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			Log.d(TAG, e.toString());
		}
	}

	public static void saveFeeds()
	{
		Log.d(TAG, "saveFeeds beginning...");

		ArrayList<Feed> feedList = FeedManager.getInstance().getFeedList(Feed.RSS_FEED);

		for (int i = 4; i < feedList.size(); ++i)
			StorageLinks.writeStoredFeeds(feedList.get(i).getFeedSite() + " "
					+ Integer.toString(feedList.get(i).getFeedType()) + " " + feedList.get(i).isEnabled().toString());

		feedList = FeedManager.getInstance().getFeedList(Feed.REDDIT_FEED);
		
		for (int i = 0; i < feedList.size(); ++i)
			Log.d(TAG, "all the reddit feeds: " + feedList.get(i).getFeedSite() );

		for (int i = 3; i < feedList.size(); ++i)
			StorageLinks.writeStoredFeeds(feedList.get(i).getFeedSite() + " "
					+ Integer.toString(feedList.get(i).getFeedType()) + " " + feedList.get(i).isEnabled().toString());

		Log.d(TAG, "saveFeeds ran");

	}

	/**
	 * Clear contents storagelinks.txt 
	 */
	@Override
	public void clear()
	{
		File f = new File ( cacheDirectory, FILENAME );

		if ( f.isDirectory() ) 
			f.delete();
	}

	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	/**
	 * Fetches the Cache Directory which is the system's file path to the applications folder.
	 * @return
	 */
	public static String getCacheDirectory()
	{
		return cacheDirectory;
	}

	/**
	 * Returns the name of file aka storagelinks.txt
	 * @return
	 */
	public static String getFilename()
	{
		return FILENAME;
	}
	
	/**
	 * Returns the tag associated for the log tool
	 * @return
	 */
	public static String getTag()
	{
		return TAG;
	}
	
	/**
	 * Sets the file path to the cache directory.
	 * @param cacheDirectory
	 */
	public static void setCacheDirectory(String cacheDirectory)
	{
		StorageLinks.cacheDirectory = cacheDirectory;
	}

}  
