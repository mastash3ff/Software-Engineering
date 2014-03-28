package com.android.terminators.storage;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

/**
 * Implements the caching mechanism used by our app.
 * Modifications by Brandon: Fit to Storage interface and applied singleton
 *
 * @version 2.0
 * @since 3/24/14
 * @author Brandon
 * @author Hathy
 */
public class StorageCache implements Storage {

	static private String cacheDirectory = 
			"/Android/data/com.android.terminators/cache/"; 
	private static StorageCache storageCache = null;
	private static final String TAG = StorageCache.class.getSimpleName();

	/**
	 * Uses Singleton pattern
	 * @return
	 */
	public static StorageCache getInstance()
	{
		if ( storageCache == null )
			storageCache = new StorageCache();
		return storageCache;
	}

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

	private static boolean tooOld(long time)
	{
		long now=new Date().getTime();
		long diff=now-time;
		if(diff>1000*60*5) //5 minutes
			return true;
		return false;
	}

	@Override
	public Boolean checkIfStorageFileExists()
	{
		File f = new File ( cacheDirectory );

		if ( f.exists() ) 
			return true;
		else
			return false;
	}

	@Override
	public void clear()
	{
		File directory = new File(cacheDirectory);
		if ( directory.isDirectory() )
			for ( File files: directory.listFiles() ) files.delete();

	}

	@Override
	public String convertToStorageName(String url)
	{
		try {            
			MessageDigest digest=MessageDigest.getInstance("MD5");
			digest.update(url.getBytes());
			byte[] b=digest.digest();
			BigInteger bi=new BigInteger(b);
			return "mycache_"+bi.toString(16)+".cac";            
		} 
		catch (Exception e) 
		{
			Log.d("ERROR", e.toString());
			return null;
		}
	}

	@Override
	public String getStorageDirectory()
	{
		return cacheDirectory;
	}

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
	public boolean isExternalStorageReadable()
	{
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
	public boolean isExternalStorageWritable()
	{
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	@Override
	public ArrayList<String> read()
	{
		return null;
	}

	@Override
	public byte[] read(String url)
	{
		try
		{
			String file=cacheDirectory+"/"+convertToStorageName(url);
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

	@Override
	public void write(String feed)
	{
		return;
	}

	@Override
	public void write(String url, String data)
	{
		try{
			String file=cacheDirectory+"/"+convertToStorageName(url);
			PrintWriter pw=new PrintWriter(new FileWriter(file));
			pw.print(data);
			pw.close();
		}catch(Exception e) { }

	}
}