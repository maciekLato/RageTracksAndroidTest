package com.macieklato.ragetracks.test;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class MainGUITest extends UiAutomatorTestCase {
	public void testDemo() throws UiObjectNotFoundException {
		// click the home button
		getUiDevice().pressHome();

		// find and click the "Apps" button to go to the applications menu
		UiObject allAppsButton = new UiObject(
				new UiSelector().description("Apps"));

		// Simulate a click to bring up the All Apps screen.
		allAppsButton.clickAndWaitForNewWindow();

		// Find and click the apps tab
		UiObject appsTab = new UiObject(new UiSelector().text("Apps"));
		appsTab.click();

		// Make a UiScrollable object and set it to scroll horizontally
		UiScrollable appViews = new UiScrollable(
				new UiSelector().scrollable(true));
		appViews.setAsHorizontalList();

		// create a UiSelector to find the RageTracks app and simulate
		// a user clicking/launching the app.
		UiObject settingsApp = appViews.getChildByText(new UiSelector()
				.className(android.widget.TextView.class.getName()),
				"RageTracks");
		settingsApp.clickAndWaitForNewWindow();

		// Tests
		// Validate that the application has the proper packageName
		UiObject settingsValidation = new UiObject(
				new UiSelector().packageName("com.macieklato.ragetracks"));
		assertTrue("Unable to detect Settings", settingsValidation.exists());

		// Make sure the RageTracks Banner is present on startup
		UiObject rtBanner = new UiObject(new UiSelector().text("RageTracks"));
		assertTrue("rtBanner is not present on startup", rtBanner.exists());

		// Make sure the RageTracks Banner is present on startup
		UiObject bottomBanner = new UiObject(
				new UiSelector().descriptionMatches("Play and Pause button"));
		assertTrue("Bottom banner is not present on startup",
				bottomBanner.exists());

		// Simulate a drag, which should hide the menus
		assertTrue("drag failed", getUiDevice().drag(200, 850, 200, 120, 100));

		// Make sure the RageTracks Banner is hides on drag
		rtBanner = new UiObject(new UiSelector().text("RageTracks"));
		assertFalse("rtBanner failed to hide", rtBanner.exists());

		// Make sure the RageTracks Banner hides on drag
		bottomBanner = new UiObject(
				new UiSelector().descriptionMatches("Play and Pause button"));
		assertFalse("Bottom banner failed to hide", bottomBanner.exists());

		// Bring banners back up
		assertTrue("drag failed", getUiDevice().drag(200, 120, 200, 850, 100));

		// open bookmarks menu with RageDude button
		UiObject rageDude = new UiObject(
				new UiSelector().descriptionMatches("Menu button"));
		assertTrue("RageDude not found", rageDude.exists());
		rageDude.click();
		UiObject bookmarks = new UiObject(new UiSelector().text("Bookmarks"));
		assertTrue("Bookmarks sidebar failed to appear", bookmarks.exists());

		// Close the side menu with drag
		assertTrue("drag failed", getUiDevice().drag(400, 200, 200, 200, 100));
		assertFalse("Bookmarks sidebar failed to appear", bookmarks.exists());
		
		//Check search button animation
		UiObject searchButton = new UiObject(
				new UiSelector().descriptionMatches("Open search button"));
		assertTrue("Search button not found", searchButton.exists());
		searchButton.click();
		UiObject commitSearchButton = new UiObject(
				new UiSelector().descriptionMatches("Commit search button"));
		assertTrue("Search animation failed", commitSearchButton.exists());

	}
}
