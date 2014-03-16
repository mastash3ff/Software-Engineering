package com.android.terminators.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import android.os.Environment;

/**
 * Static Class that has methods pertaining to Creation, Retrieval, and Storage of Feed
 * Links on the user's Android Device.
 * @author Brandon
 */

public class StorageLinks implements Storage
{
  static private String cacheDirectory = 
      "/Android/data/com.android.terminators/cache/"; 
  static private String textFileName = "links.txt";

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

  /**Helper method to determine if links.txt exists on Android Storage
   * 
   * */
  public static Boolean checkIfFileExists()
  {
    String filePath = cacheDirectory + textFileName;
    File myFile = new File (filePath);
    if (myFile.exists()) 
      return true;
    else
      return false;
  }

  /**
   * Creates links.txt file if it is non-existent
   * 
   */
  public static void createFile()
  {
    String filePath = cacheDirectory + textFileName;
    File myFile = new File (filePath);

    try
    {
      myFile.createNewFile();
    } catch (IOException e)
    {
      e.printStackTrace();
    }

  }

  /**
   * Reads the contents of links.txt file
   * 
   * */
  public static LinkedList<String> readStoredFeeds()
  {
    String filePath = cacheDirectory + textFileName;
    File myFile = new File (filePath);
    String contents = null;
    BufferedReader br = null;
    LinkedList<String> linkedList = new LinkedList<String>();

    //if file does not exist, create a file and exit function
    if (!myFile.exists())
    {
      createFile();
      return null;
    }
    else
    {
      //read contents of file if it does exist and return a list of links
      try
      {
        br = new BufferedReader(new FileReader(filePath));

        while ((contents = br.readLine()) != null) 
        {
          linkedList.add(contents);
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
    return linkedList;
  }

  /**Writes to links.txt
   * @param link
   * */
  public static void writeStoredFeeds(String link)
  {
    try
    {
      String file= cacheDirectory + textFileName;
      File f = new File(file);
      PrintWriter pw=new PrintWriter( new BufferedWriter(new FileWriter(f, true)));
      pw.println(link);
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
    File f = new File(cacheDirectory+textFileName);
    if ( f.isDirectory() )
      f.delete();

    createFile();
  }
}  
