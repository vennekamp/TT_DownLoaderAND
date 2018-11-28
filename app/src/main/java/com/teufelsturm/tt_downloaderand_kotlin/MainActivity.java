package com.teufelsturm.tt_downloaderand_kotlin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.teufelsturm.tt_downloaderand_kotlin.searches.MyPagerFragment;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends FragmentActivity
        implements OnFragmentInteractionListener {

    public static final int ANZAHL_GIPFEL = 1120;
    public final static String DB_NAME = "TT_DownLoader_AND.mp3";
    public final static String MAP_NAME = "saechsische_schweiz.map";
    public final static String APP_VERSION = "0.99";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment searchPagerWidget = MyPagerFragment.newInstance();
        replaceFragment(searchPagerWidget, MyPagerFragment.ID);

//		Fragment tt_routeFoundFragment
//				= TT_RouteFoundFragment.newInstance();
//		replaceFragment(tt_routeFoundFragment,TT_RouteFoundFragment_FAKE.ID);
    }

    @Override
    public void replaceFragment(@NotNull Fragment fragment, String mTAG ) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, mTAG);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
