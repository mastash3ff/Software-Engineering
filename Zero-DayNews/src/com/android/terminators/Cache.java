package com.android.terminators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import android.os.Environment;
/**
 * Used to Store and Retrieve Added Feeds through Android Storage
 * @author Brandon
 */

public class Cache
{

  static private String cacheDirectory = 
      "/Android/data/com.android.terminators/cache/"; 
  static private String filename = "storedlinks.txt";

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
      cacheDirectory=Environment.getExternalStorageDirectory()
          +cacheDirectory;
      File f=new File(cacheDirectory);
      f.mkdirs();
    }
  }    

  public static String readStoredFeeds()
  {
    String fileName=cacheDirectory+"/"+filename;
    File myFile = new File (fileName);
    String content = null;
    if (myFile.exists())
    {
      try 
      {
        FileReader reader = new FileReader(myFile);
        char[] chars = new char[(int) myFile.length()];
        reader.read(chars);
        content = new String(chars);
        reader.close();
      } 
      catch (IOException e) 
      {
        e.printStackTrace();
      }
    }
    return content;
  }

  public static void writeStoredFeeds(String link)
  {
    try{
      String file=cacheDirectory+"/"+filename;
      File f = new File(file);
      PrintWriter pw=new PrintWriter( new BufferedWriter(new FileWriter(f, true)));
      pw.println(link);
      pw.close();
    }catch(Exception e) { }
  }
}  
