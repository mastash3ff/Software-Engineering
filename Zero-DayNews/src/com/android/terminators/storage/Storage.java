package com.android.terminators.storage;

import java.util.ArrayList;

/**
 * Common interface for Storage classes:  StorageLinks and StorageCache
 * @author Brandon
 * @date 
 *
 */

public interface Storage
{
	public Boolean checkIfStorageFileExists();
	
	public void clear();
	
	public String convertToStorageName(String url);
	
  public String getStorageDirectory();
  
  public String getTag();
  
  public  boolean isExternalStorageReadable();
  
  public  boolean isExternalStorageWritable();
  
  public  byte[] read(String url);
  
  public ArrayList<String> read() ;
  
  public void write(String url, String data);
  
  public  void write(String feed);
  
}
