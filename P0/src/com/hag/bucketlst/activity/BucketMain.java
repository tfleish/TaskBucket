package com.hag.bucketlst.activity;

// test push from eclipse in testBranch?
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.hag.bucketlst.R;
import com.hag.bucketlst.customWindows.CustomTab;

public class BucketMain extends CustomTab {
    /** Called when the activity is first created. */
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
        this.title.setText("bucketLST");

        //Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, MyTasks.class);
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("My Tasks").setIndicator("My Tasks").setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, MyDoneTasks.class);
        spec = tabHost.newTabSpec("Checked Tasks").setIndicator("Done Tasks").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, NCategoryView.class);
        spec = tabHost.newTabSpec("Bucket Sort").setIndicator("My Buckets").setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }

}