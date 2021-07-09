package com.teufelsturm.tt_downloaderand_kotlin.searches;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;


import com.teufelsturm.tt_downloader3.MainActivity;
import com.teufelsturm.tt_downloader3.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MyPagerFragment extends Fragment
        implements OnTabChangeListener, ViewPager.OnPageChangeListener {
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
        initialiseTabHost(inflater, view);

        pageAdapter = new MyPageAdapter( getChildFragmentManager() );
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setSaveFromParentEnabled(false);
        mViewPager.setOffscreenPageLimit(pageAdapter.getCount()  - 1);
        return view;
	}

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ViewModel4FragmentSearches searches = ViewModelProviders.of(getActivity()).get(ViewModel4FragmentSearches.class);
        mViewPager.setCurrentItem(searches.getPagerPageSelected());
        ((MainActivity)getActivity()).showFAB(ID);
    }

	@Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public View createTabIndicator(LayoutInflater inflater,
                                   TabHost tabHost, String text, int iconResource) {
        View tabIndicator = inflater.inflate(R.layout._main_tab_indicator,
                tabHost.getTabWidget(),false);
        ((TextView) tabIndicator.findViewById(android.R.id.title)).setText(text);
        ((ImageView) tabIndicator.findViewById(android.R.id.icon)).setImageResource(iconResource);
        return tabIndicator;
    }

    // Method to add a TabHost
    private void addTab(Context context,
                        @NotNull TabHost tabHost,
                        @NotNull TabHost.TabSpec tabSpec) {
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
        ViewModel4FragmentSearches searches = ViewModelProviders.of(getActivity()).get(ViewModel4FragmentSearches.class);
        searches.setPagerPageSelected(position);
	}

    // Tabs Creation
    private void initialiseTabHost(@NonNull LayoutInflater inflater, @NotNull View v) {
        mTabHost = v.findViewById(android.R.id.tabhost);
        mTabHost.setup();

        addTab(getContext(), mTabHost,mTabHost.newTabSpec("Gipfel")
        		.setIndicator(createTabIndicator(inflater, mTabHost,
        		        "Gipfel", R.drawable.ic_summit)));
        addTab(getContext(), mTabHost, mTabHost.newTabSpec("Wege")
        		.setIndicator(createTabIndicator(inflater, mTabHost,
                        "Wege", R.drawable.ic_route)));
        addTab(getContext(), mTabHost, mTabHost.newTabSpec("Kommentare")
				.setIndicator(createTabIndicator(inflater, mTabHost,
                        "Kommentare", R.drawable.ic_comments)));
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
