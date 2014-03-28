package com.android.terminators;

import android.util.Log;
import android.widget.RelativeLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

/** Ads an ad to activity_main layout.
 * @author Brandon Sheffield
 * @author Derrick
 * @date 3/27/14
 *
 */

public class Ad
{
	private static final String AD_UNIT_ID = "ca-app-pub-5178282085023497/1033225563";
	private static final String TAG = Ad.class.getSimpleName();

	public static void addAd(AdView adView, RelativeLayout layout)
	{
		Log.d(TAG, "addAd started...");

		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(AD_UNIT_ID);
		Log.d(TAG, "Create an Ad finished..");
		
		// Add the AdView to the view hierarchy. The view will have no size
		// until the ad is loaded.
		Log.d(TAG,"Begin Layout Structuring...");
		//RelativeLayout layout = (RelativeLayout)findViewById(R.id.activity_main);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		Log.d(TAG, "about to add a view...");
		
		layout.addView(adView, params);

		Log.d(TAG, "added the view to the layout");

		// Create an ad request. Check logcat output for the hashed device ID to
		// get test ads on a physical device.
		Log.d(TAG, "starting ad request...");
		AdRequest adRequest = new AdRequest.Builder()
		  .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
	  	.addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB")
		  .build();

		// Start loading the ad in the background.
		adView.loadAd(adRequest);
		Log.d(TAG, "Ad request finished");

		Log.d(TAG, "addAd ran");
	}

}
