package com.macieklato.ragetracks.test;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class MainGUITest extends UiAutomatorTestCase{
	public void testDemo() throws UiObjectNotFoundException {
		//click the home button
		getUiDevice().pressHome();

	    //find and click the "Apps" button to go to the applications menu
	    UiObject allAppsButton = new UiObject(new UiSelector().description("Apps"));

	    // Simulate a click to bring up the All Apps screen.
	    allAppsButton.clickAndWaitForNewWindow();

	    // Find and click the apps tab
	    UiObject appsTab = new UiObject(new UiSelector().text("Apps"));
	    appsTab.click();

	    // Make a UiScrollable object and set it to scroll horizontally
	    UiScrollable appViews = new UiScrollable(new UiSelector().scrollable(true));
	    appViews.setAsHorizontalList();

	    // create a UiSelector to find the RageTracks app and simulate
	    // a user clicking/launching the app.
	    UiObject settingsApp = appViews
	        .getChildByText(new UiSelector()
	            .className(android.widget.TextView.class.getName()),
	            "RageTracks");
	    settingsApp.clickAndWaitForNewWindow();
	    
	    //Tests
	    // Validate that the application has the proper packageName
	    UiObject settingsValidation = new UiObject(new UiSelector()
	        .packageName("com.macieklato.ragetracks"));
	    assertTrue("Unable to detect Settings", settingsValidation.exists());
	    
	    //Make sure the RageTracks Banner is present on startup
	    UiObject rtBanner = new UiObject(new UiSelector().text("RageTracks"));
	    assertTrue("rtBanner is not present on startup", rtBanner.exists());
	    
	  //Make sure the RageTracks Banner is present on startup
	    UiObject bottomBanner = new UiObject(new UiSelector().descriptionMatches("Play and Pause button"));
	    assertTrue("Bottom banner is not present on startup", bottomBanner.exists());
	    
	    

	  }
}
