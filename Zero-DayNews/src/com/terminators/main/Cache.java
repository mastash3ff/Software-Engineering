package com.terminators.main;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

/**
 * Caches contents used from URL for fast I/O on the user's SD card.
 * File naming convention using the URL as a string.
 * @author Brandon
 *
 */

//Notes:  getExternalStorage (supports older SDK versions) can be changes to getExternalCacheDir
//May need to add deletion of cache logic
public class Cache
{

	static private String cacheDirectory = "/Android/data/com.terminators.main/cache/";

	/**
	 * Check to see if SD card is available and write to it.  Checks to see if existing directory is there.  If not, creates a new directory.
	 * 
	 */

	static {
		if ( Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)){
			cacheDirectory=Environment.getExternalStorageState() + cacheDirectory;
			File f = new File (cacheDirectory);
			f.mkdirs();		
		}
	}

	static public String convertToCacheName(String url) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(url.getBytes());
			byte[] b = digest.digest();
			BigInteger bi = new BigInteger(b);
			return "mycache_"+bi.toString(16)+".cac";
		} catch (Exception e ) {
			Log.d("ERROR", e.toString());
			return null;
		}//end catch
	}//end converToCacheName

	private static boolean tooOld(long time ) {
		long now = new Date().getTime();
		long diff = now - time;
		if ( diff > 1000*60*5)
			return true;
		return false;
	}//end tooOld

	public static byte[] read(String url){
		try{
			String file=cacheDirectory+"/"+convertToCacheName(url);
			File f=new File(file);
			if(!f.exists() || f.length() < 1) return null;
			if(f.exists() && tooOld(f.lastModified())){
				// Delete the cached file if it is too old
				f.delete();
			}
			byte data[]=new byte[(int)f.length()];
			DataInputStream fis=new DataInputStream(
					new FileInputStream(f));
			fis.readFully(data);
			fis.close();
			return data;
		}catch(Exception e) { return null; }
	}

	public static void write(String url, String data){
		try{
			String file=cacheDirectory+"/"+convertToCacheName(url);
			PrintWriter pw=new PrintWriter(new FileWriter(file));
			pw.print(data);
			pw.close();
		}catch(Exception e) { }
	}



}//ends public class Cache
