package com.teufelsturm.tt_downloaderand_kotlin.searches;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.teufelsturm.tt_downloaderand_kotlin.R;

import java.util.ArrayList;
import java.util.List;

public class MyPagerFragment extends Fragment
        implements OnTabChangeListener, OnPageChangeListener {
    private static final String TAG = MyPagerFragment.class.getSimpleName();
    public static String ID = "MyPagerFragment";
    private MyPageAdapter pageAdapter;
    private ViewPager mViewPager;
    private TabHost mTabHost;

    @NonNull
    public static MyPagerFragment newInstance() {
        return new MyPagerFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout._main_activity_pager,
                container, false);
        mViewPager = view.findViewById(R.id.viewpager);
        // Tab Initialization
        initialiseTabHost(view);

        pageAdapter = new MyPageAdapter( getChildFragmentManager() );
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setSaveFromParentEnabled(false);
        mViewPager.setOffscreenPageLimit(pageAdapter.getCount()  - 1);
        return view;
	}

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (getView() != null) {
//            ViewGroup parent = (ViewGroup) getView().getParent();
//            if (parent != null) {
//                parent.removeAllViews();
//            }
//        }
//    }

    // Method to add a TabHost
    private void addTab(Context context,
                        TabHost tabHost,
                        TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new MyTabFactory(context));
        tabHost.addTab(tabSpec);
    }

    // Manages the Tab changes, synchronizing it with Pages
    public void onTabChanged(String tag) {
        Log.e(TAG, "onTabChanged(String tag)   --->   " + tag);
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
	public void onPageSelected(int position) {
        Log.e(TAG, "onPageSelected(int position)   --->   " + position);
        this.mTabHost.setCurrentTab(position);
	}

    // Tabs Creation
    private void initialiseTabHost(View v) {
        mTabHost = v.findViewById(android.R.id.tabhost);
        mTabHost.setup();

		Resources res = getResources(); // Resource object to get Drawables
        addTab(getContext(), this.mTabHost, this.mTabHost.newTabSpec("Gipfel")
        		.setIndicator("Gipfel", res.getDrawable(R.drawable.ic_summit)));
        addTab(getContext(), this.mTabHost, this.mTabHost.newTabSpec("Wege")
        		.setIndicator("Wege",res.getDrawable(R.drawable.ic_route)));
        addTab(getContext(), this.mTabHost, this.mTabHost.newTabSpec("Kommentare")
				.setIndicator("Kommentare", res.getDrawable(R.drawable.ic_comments)));
        mTabHost.setOnTabChangedListener(this);
    }


    private class MyPageAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mFragments;

        MyPageAdapter(FragmentManager fm) {
            super(fm);
            // Fragments and ViewPager Initialization
            mFragments = getFragments();
        }

        @Override
        public Fragment getItem(int position) {
            Log.e(TAG, "getItem(int position)   --->   " + position);
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        public void add(Fragment fragment) {
            mFragments.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getItem(position).getTag();
        }

        private ArrayList<Fragment> getFragments() {
            ArrayList<Fragment> fList = new ArrayList<>();
            FragmentSearchAbstract fragSummit = FragmentSearchAbstract
                    .newInstance(FragmentSearchAbstract.SearchType.SUMMIT);
            FragmentSearchAbstract fragRoute = FragmentSearchAbstract
                    .newInstance(FragmentSearchAbstract.SearchType.ROUTE);
            FragmentSearchAbstract fragComment = FragmentSearchAbstract
                    .newInstance(FragmentSearchAbstract.SearchType.COMMENT);
            fList.add(fragSummit);
            fList.add(fragRoute);
            fList.add(fragComment);

            return fList;
        }
    }
}
