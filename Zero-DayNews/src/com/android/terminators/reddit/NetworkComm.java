package com.android.terminators.reddit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.android.terminators.storage.StorageCache;
import android.util.Log;

/**
 * Utility class that handles network connections
 * @author Hathy
 *
 */

public class NetworkComm
{
  /**
   * Returns a connection to a specified URL.  Has other properties like timeout and 
   * user-agent set to the requirements
   * @param url
   * @return
   */
  public static HttpURLConnection getConnection(String url)
  {
    HttpURLConnection hcon = null;

    try
    {
      hcon = (HttpURLConnection) new URL(url).openConnection();
      hcon.setReadTimeout(30000); //30 second time-out
      hcon.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 4.4.2");
    } 
    catch (MalformedURLException e) 
    {
      Log.e("getConnection()", "Invalid URL: "+ e.toString());
    }
    catch (IOException e)
    {
      Log.e("getConnection()", "Could not connect: " + e.toString());
    }

    return hcon;
  }

  /**
   * A very handy utility method that reads the contents of a URL
   * and returns them as a String.
   * 
   * @param url
   * @return
   */
  public static String readContents(String url){

    //Check if the cache contains data for this URL

    byte[] t=StorageCache.read(url);
    String cached=null;
    if(t!=null) {
      cached=new String(t);
      t=null;
    }
    if(cached!=null) {
      Log.d("MSG","Using cache for "+url);
      return cached;
    }

    //The following will be executed only if the
    //cache did not contain data for this URL

    HttpURLConnection hcon=getConnection(url);
    if(hcon==null) return null;
    try{
      StringBuffer sb=new StringBuffer(8192);
      String tmp="";
      BufferedReader br=new BufferedReader(
          new InputStreamReader(
              hcon.getInputStream()
              )
          );
      while((tmp=br.readLine())!=null)
        sb.append(tmp).append("\n");
      br.close();    

      // We now add this data to the cache
      StorageCache.write(url, sb.toString());
      return sb.toString();
    }catch(IOException e){
      Log.d("READ FAILED", e.toString());
      return null;
    }
  }  

}
