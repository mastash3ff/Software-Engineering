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
  static final private String fileName = "storagelinks.txt";
  static final private String filePath = cacheDirectory + fileName;

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
   * Utility method to determine if storagelinks.txt exists on Android Storage
   * @return
   */
  public static Boolean checkIfStorageFileExists()
  {
    File f = new File ( getFilepath() );

    if ( f.exists() ) 
      return true;
    else
      return false;
  }

  /**
   * Creates a blank links.txt file if it is non-existent.
   */
  public static void createFile()
  {
    File f = new File( getFilepath() );

    try
    {
      f.createNewFile();
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
  public static ArrayList<String> readStoredFeeds( )
  {
    //filename parameter will only expect reddit and rss text file
    File f = new File( getFilepath() );
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
    return list;
  }

  /** Checks to see if file exists with Feed links if not creates a file called 
   * StoredLinks.txt on storage
   */
  public static void initializeStorage( Context context )
  {
    //reads text file to see what links are saved and returns as a list
    ArrayList<String> listOfStrings = readStoredFeeds();

    //file checks
    if ( !StorageLinks.checkIfStorageFileExists() ) 
    {
      StorageLinks.createFile();
    }
    else
    {
      String[] feedData;

      for ( int i = 0; i < listOfStrings.size(); ++i )
      {
        //on startup displays what links are already stored and adds them to feed
        //feedData = listOfStrings.get(i).split("\\s+");
        feedData = listOfStrings.get(i).split(" ");
        Toast.makeText(context, feedData[0], Toast.LENGTH_SHORT).show();
        Toast.makeText(context, feedData[2], Toast.LENGTH_SHORT).show();
        Toast.makeText(context, feedData[1], Toast.LENGTH_SHORT).show();

        FeedManager.getInstance().addFeed( new Feed( feedData[0], Integer.parseInt(feedData[1]), 
            Boolean.parseBoolean( feedData[2])));
      }

      Toast.makeText( context, "Saved Feeds Loaded" , Toast.LENGTH_SHORT).show();
    }
  }

  /**Writes to storagelinks.txt
   * @param link
   * */
  public static void writeStoredFeeds(String link)
  {
    try
    {
      File f = new File( getFilepath() );
      PrintWriter pw=new PrintWriter( new BufferedWriter(new FileWriter(f, true)));
      pw.println( link );
      pw.close();
    }
    catch(Exception e) 
    {
      e.printStackTrace();
    }
  }


  /**
   * Brandon's lazy way of deleting the contents of a file.
   */
  @Override
  public void clear()
  {
    File f = new File ( getFilepath() );

    if ( f.isDirectory() ) 
      f.delete();

    createFile();
  }

  /**
   * Fetches the Cache Directory which is the system's file path to the applications folder.
   * @return
   */
  public static String getCacheDirectory()
  {
    return cacheDirectory;
  }

  public static String getFilename()
  {
    return fileName;
  }

  public static String getFilepath()
  {
    return filePath;
  }
}  
