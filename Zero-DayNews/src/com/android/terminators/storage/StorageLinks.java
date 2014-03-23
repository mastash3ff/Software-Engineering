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
import android.os.Environment;

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
  public static Boolean checkIfFilesExists()
  {
    File myRSSFile = new File ( getRssFilePath() );
    File myRedditFile = new File ( getRedditFilePath() );

    if (myRSSFile.exists() && myRedditFile.exists() ) 
      return true;
    else
      return false;
  }

  /**
   * Creates a blank links.txt file if it is non-existent.
   */
  public static void createFile()
  {
    File myRedditFile = new File( getRedditFilePath() );
    File myRSSFile = new File( getRssFilePath() );

    try
    {
      myRedditFile.createNewFile();
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
    ArrayList<String> ArrayList = new ArrayList<String>();

    //if file does not exist, create a file and exit function
    if ( !f.exists()  )
      createFile();
    else
    {
      //read contents of file if it does exist and return a list of links
      try
      {
        br = new BufferedReader(new FileReader(f));

        while ( (contents = br.readLine() ) != null) 
        {
          ArrayList.add(contents);
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
    }
    return ArrayList;
  }

  /**Writes to links.txt
   * @param link
   * */
  public static void writeStoredFeeds( String fileName )
  {
    try
    {
      String file= getCacheDirectory() + fileName;
      File f = new File(file);
      PrintWriter pw=new PrintWriter( new BufferedWriter(new FileWriter(f, true)));
      pw.println( fileName );
      pw.close();
    }
    catch(Exception e) 
    {
      e.printStackTrace();
    }
  }

  /**
   * Deletes file and recreates a blank one with filename links.txt
   * 
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
