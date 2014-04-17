package com.android.terminators.testing;

import org.junit.Before;
import com.android.terminators.MainActivity;
import com.android.terminators.ZeroDayNews.R;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.TextView;


/**
 * To use this test, you must create an Android Test Project from our Zero-Days News Project and
 * by placing this file into the src folder.
 * 
 * JUnit test for MainActivity that tests all the initialized text of Buttons/TextViews.
 *
 * @author Brandon Sheffield 
 *
 */
public class MainActivityStartupTest extends ActivityInstrumentationTestCase2<MainActivity>
{
	private MainActivity mainActTest;

	private TextView titleTextView;
	private Button articlesButtons, addFeedButton, 
	configureRssFeedsButtons,configureRedditFeedsButton;

	public MainActivityStartupTest(Class<MainActivity> activityClass)
	{
		super(activityClass);
	}

	/**
	 * Sets all the variables from the MainActivity to be used later for testing.
	 */
	@Before
	public void setUp() throws Exception
	{
		super.setUp();

		mainActTest = getActivity();
		titleTextView = (TextView) mainActTest.findViewById(R.id.appTitle);
		articlesButtons = (Button) mainActTest.findViewById(R.id.articlesButton);
		addFeedButton = (Button) mainActTest.findViewById(R.id.addFeedButton);
		configureRssFeedsButtons = (Button) mainActTest.findViewById(R.id.configureRssFeedsButton);
		configureRedditFeedsButton = (Button) mainActTest.findViewById(R.id.configureRedditFeedsButton);

	}

	public void testPreConditions()
	{

		//Try to add a message to add context to your assertions. These messages will be shown if
		//a tests fails and make it easy to understand why a test failed
		assertNotNull("mainActTest is null", mainActTest);
		assertNotNull("titleTextView is null", titleTextView);
		assertNotNull("articleButtons is null", articlesButtons);
		assertNotNull("configureRssFeedsButton is null", configureRssFeedsButtons);
		assertNotNull("configureRedditFeedsButton", configureRedditFeedsButton);

	}

	/**
	 * Tests the correctness of the button's initial text.
	 */
	public void testButtonText()
	{
		final String expectedArticleButtonString = mainActTest.getString(R.string.displayArticles);
		final String actualArticleButtonString = (String) articlesButtons.getText().toString();
		assertEquals("expectedArticleButtonString contains wrong text", expectedArticleButtonString, actualArticleButtonString);

		final String expectedAddFeedButtonString = mainActTest.getString(R.string.addNewFeed);
		final String actualAddFeedButtonString = (String) addFeedButton.getText().toString();
		assertEquals("actualAddFeedButtonString contains wrong text", expectedAddFeedButtonString, actualAddFeedButtonString);

		final String expectedConfigureRssString = mainActTest.getString(R.string.configureRssFeeds);
		final String actualConfigureRssString = (String) configureRssFeedsButtons.getText().toString();
		assertEquals("actualConfigureRssString contains wrong text", expectedConfigureRssString, actualConfigureRssString);

		final String expectedConfigureRedditString = mainActTest.getString(R.string.configureRedditFeeds);
		final String actualConfigureRedditString = (String) configureRedditFeedsButton.getText().toString();
		assertEquals("actualConfigureRedditString contains wrong text", expectedConfigureRedditString, actualConfigureRedditString);

	}

	/**
	 * Tests the correctness of the initial text.
	 */
	public void testTextView()
	{
		//It is good practice to read the string from your resources in order to not break
		//multiple tests when a string changes.
		final String expected = mainActTest.getString(R.string.app_name);
		final String actual = titleTextView.getText().toString();
		assertEquals("mFirstTestText contains wrong text", expected, actual);

	}

}
