package com.teufelsturm.tt_downloaderand_kotlin.gpslocation;


import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.TT_Summit_AND;



public class TT_SummitCoord_TabWidget extends TabActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout._main_activity__tabhost);
		TT_Summit_AND aTT_Summit_AND;
		
		Resources res = this.getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Reusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab
		intent = getIntent();
		aTT_Summit_AND = intent.getParcelableExtra("TT_Gipfel_AND");
		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, ActivityGPSCompass.class);
		intent.putExtra("TT_Gipfel_AND", aTT_Summit_AND);
		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost
				.newTabSpec("direction")
				.setIndicator("Richtung",
						res.getDrawable(R.drawable.ic_compass_tt))
				.setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, GettingStarted.class);
		intent.putExtra("TT_Gipfel_AND", aTT_Summit_AND);
		spec = tabHost
				.newTabSpec("map")
				.setIndicator("Karte",
						res.getDrawable(R.drawable.ic_map_tt))
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
	}
}
