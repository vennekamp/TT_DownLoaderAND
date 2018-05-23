package com.teufelsturm.tt_downloaderand_kotlin;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import java.util.ArrayList;
import java.util.List;

public class MainPagerWidget extends FragmentActivity
	implements OnTabChangeListener, OnPageChangeListener {

    MyPageAdapter pageAdapter;
    private ViewPager mViewPager;
    private TabHost mTabHost;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout._main_activity_pager);

        mViewPager = findViewById(R.id.viewpager);

        // Tab Initialization
        initialiseTabHost();

        // Fragments and ViewPager Initialization
        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.addOnPageChangeListener(MainPagerWidget.this);
	}

    // Method to add a TabHost
    private static void AddTab(MainPagerWidget activity, TabHost tabHost
    		, TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new MyTabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    // Manages the Tab changes, synchronizing it with Pages
    public void onTabChanged(String tag) {
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	// Manages the Page changes, synchronizing it with Tabs
	@Override
	public void onPageSelected(int arg0) {
        int pos = this.mViewPager.getCurrentItem();
        this.mTabHost.setCurrentTab(pos);
	}
	

    private List<Fragment> getFragments(){
        List<Fragment> fList = new ArrayList<>();
        MainActivitySearchAbstract fragSummit 
        	= MainActivitySearchAbstract.newInstance(MainActivitySearchAbstract.SearchType.SUMMIT );
        MainActivitySearchAbstract fragRoute 
    		= MainActivitySearchAbstract.newInstance(MainActivitySearchAbstract.SearchType.ROUTE );
        MainActivitySearchAbstract fragComment
    		= MainActivitySearchAbstract.newInstance(MainActivitySearchAbstract.SearchType.COMMENT );
        fList.add(fragSummit);
        fList.add(fragRoute);
        fList.add(fragComment);

        return fList;
    }

    // Tabs Creation
    private void initialiseTabHost() {
        mTabHost = findViewById(android.R.id.tabhost);
        mTabHost.setup();

		Resources res = getResources(); // Resource object to get Drawables
        AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Gipfel")
        		.setIndicator("Gipfel", res.getDrawable(R.drawable.ic_summit)));
        AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Wege")
        		.setIndicator("Wege",res.getDrawable(R.drawable.ic_route)));
        AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Kommentare")
				.setIndicator("Kommentare", res.getDrawable(R.drawable.ic_comments)));
        mTabHost.setOnTabChangedListener(this);
    }
	
}
