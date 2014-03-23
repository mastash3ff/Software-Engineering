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

import com.android.terminators.Feed;
import com.android.terminators.FeedManager;
import com.android.terminators.MainActivity;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

/**
 * Custom Storage class that has methods pertaining to Creation, Retrieval, and Storage of Feed
 * Links on the user's Android Device.
 * @author Brandon
 */

public class StorageLinks implements Storage
{
  static private String cacheDirectory = 
      "/Android/data/com.android.terminators/cache/"; 
  static final private String redditFileName = "redditlinks.txt";
  static final private String rssFileName = "rsslinks.txt";
  static final private String redditFilePath = cacheDirectory + redditFileName;
  static final private String rssFilePath = cacheDirectory + rssFileName;

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
      File f=new File(cacheDirectory);
      f.mkdirs();
    }
  }

  /**
   * Utility method to determine if links.txt exists on Android Storage
   * @return
   */
  public static Boolean checkIfRSSFileExists()
  {
    File myRSSFile = new File ( getRssFilePath() );

    if ( myRSSFile.exists() ) 
      return true;
    else
      return false;
  }

  /**
   * Utility method to determine if links.txt exists on Android Storage
   * @return
   */
  public static Boolean checkIfRedditFileExists()
  {
    File myRedditFile = new File ( getRedditFilePath() );

    if ( myRedditFile.exists() ) 
      return true;
    else
      return false;
  }

  /**
   * Creates a blank links.txt file if it is non-existent.
   */
  public static void createRedditFile()
  {
    File myRedditFile = new File( getRedditFilePath() );

    try
    {
      myRedditFile.createNewFile();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Creates a blank links.txt file if it is non-existent.
   */
  public static void createRSSFile()
  {
    File myRSSFile = new File( getRssFilePath() );

    try
    {
      myRSSFile.createNewFile();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Reads the contents of links.txt file.  Takes only the filename.txt as a string.
   * @param fileName
   * 
   * */
  public static ArrayList<String> readStoredFeeds(String fileName)
  {
    //filename parameter will only expect reddit and rss text file
    String filePath = getCacheDirectory() + fileName;
    File f = new File( filePath );
    String contents = null;
    BufferedReader br = null;
    ArrayList<String> arrayList = new ArrayList<String>();

    //read contents of file if it does exist and return a list of links
    try
    {
      br = new BufferedReader(new FileReader(f));

      while ( (contents = br.readLine() ) != null) 
      {
        arrayList.add(contents);
      }
    } 
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    } 
    finally 
    {
      try
      {
        if (br != null)br.close();
      } catch (IOException ex) 
      {
        ex.printStackTrace();
      }
    }
    return arrayList;
  }
  
/**Writes to redditlinks.txt
 * @param link
 * */
public static void writeRedditFeed( String feedName )
{
  try
  {
    File f = new File( getRedditFilePath() );
    PrintWriter pw = new PrintWriter( new BufferedWriter(new FileWriter(f, true)));
    pw.println( feedName );
    pw.close();
  }
  catch(Exception e) 
  {
    e.printStackTrace();
  }
}

/**Writes to  rsslinks.txt
 * @param link
 * */
public static void writeRSSFeed( String feedName )
{
  try
  {
    File f = new File( getRssFilePath());
    PrintWriter pw = new PrintWriter( new BufferedWriter(new FileWriter(f, true)));
    pw.println( feedName );
    pw.close();
  }
  catch(Exception e) 
  {
    e.printStackTrace();
  }
}

/**
 *  Checks to see if there is any feeds on storage and notifies 
 * */
public static void storageNotification(Context context)
{
  ArrayList<String > redditListOfStrings = StorageLinks.readStoredFeeds( StorageLinks.getRedditFileName() );
  ArrayList<String> rssListOfStrings = StorageLinks.readStoredFeeds( StorageLinks.getRssFileName() );

  //notify user if no feeds are detected upon pressing an enter feed button
  if ( !(redditListOfStrings.isEmpty() && rssListOfStrings.isEmpty()) )
  {
    Toast.makeText(context, "Zero Feeds Detected on Device" , Toast.LENGTH_SHORT).show();
  }
}

/** Checks to see if file exists with Feed links if not creates a file called 
 * StoredLinks.txt on storage
 */
public static void initializeStorage( Context context )
{
  //reads text file to see what links are saved and returns as a list
  ArrayList<String > redditListOfStrings = StorageLinks.readStoredFeeds( StorageLinks.getRedditFileName() );
  ArrayList<String> rssListOfStrings = StorageLinks.readStoredFeeds( StorageLinks.getRssFileName() );

  ArrayList<Feed> arrayList = new ArrayList<Feed>();

  //file checks
  if ( StorageLinks.checkIfRSSFileExists() == false ) 
  {
    StorageLinks.createRSSFile();
  }

  if ( StorageLinks.checkIfRedditFileExists() == false ) 
  {
    StorageLinks.createRedditFile();
  }

  for ( int i = 0; i < redditListOfStrings.size(); ++i )
  {
    //on startup displays what links are already stored and adds them to feed
    arrayList.add( new Feed( redditListOfStrings.get(i) ) );
  }

  FeedManager.getFeed().setRedditFeedList( arrayList );
  arrayList.clear();

  for ( int i = 0; i < rssListOfStrings.size(); ++i )
  {
    //on startup displays what links are already stored and adds them to feed
    arrayList.add( new Feed( rssListOfStrings.get(i) ) );
  }

  FeedManager.getFeed().setRssFeedList( arrayList );

  Toast.makeText( context, "Saved Feeds Loaded" , Toast.LENGTH_SHORT).show();
}


/**
 * Brandon's lazy way of deleting the contents of a file.
 */
@Override
public void clear()
{
  File myRSSFile = new File ( getRssFilePath() );
  File myRedditFile = new File ( getRedditFilePath() );

  if ( myRSSFile.isDirectory() ) 
    myRSSFile.delete();

  if ( myRedditFile.isDirectory() )
    myRedditFile.delete();

  createRedditFile();
  createRSSFile();
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
 * Returns a static string of the filename + extension.
 * @return
 */
public static String getRedditFileName()
{
  return redditFileName;
}

/**
 * Returns the name of the RSS text file.
 * @return
 */
public static String getRssFileName()
{
  return rssFileName;
}

/**
 * Returns the entire system path to applications folder with the RSS file name appended.
 * @return
 */
public static String getRssFilePath()
{
  return rssFilePath;
}

/**
 * Returns the entire system path to applications folder with the Reddit file name appended.
 * @return
 */
public static String getRedditFilePath()
{
  return redditFilePath;
}
}  
