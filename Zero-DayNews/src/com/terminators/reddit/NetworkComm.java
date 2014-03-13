package com.terminators.reddit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
    //System.out.println("URL:" + url);
  	HttpURLConnection hcon = null;

	  try
		{
			hcon = (HttpURLConnection) new URL(url).openConnection();
			hcon.setReadTimeout(30000); //30 second time-out
			hcon.setRequestProperty("User-Agent", "FSU SE Project 1.0");
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
	 * Reads contents of a URL and returns as a string.  Used for interacting with JSON API.
	 * Also, handles cache using Cache.java
	 * @param url
	 * @return
	 */
	public static String readContents (String url)
	{
		HttpURLConnection hcon = getConnection(url);
		if (hcon == null) return null;
		try
		{
			StringBuffer sb = new StringBuffer(8192);
			String tmp = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(hcon.getInputStream()));

			while ((tmp = br.readLine()) != null)
				sb.append(tmp).append("\n");

			br.close();                       
			return sb.toString();
		}
		catch (IOException e)
		{
			Log.d("READ FAILED", e.toString());
			return null;
		}
	} //end readContents
	
}
