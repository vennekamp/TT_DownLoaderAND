package com.teufelsturm.tt_downloaderand_kotlin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.teufelsturm.tt_downloaderand_kotlin.searches.MyPagerFragment;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends FragmentActivity
        implements OnFragmentReplaceListener {

    public static final int ANZAHL_GIPFEL = 1120;
    public final static String DB_NAME = "TT_DownLoader_AND.mp3";
    public final static String MAP_NAME = "saechsische_schweiz.map";
    public final static String APP_VERSION = "0.99";

//    private FloatingActionButton fab4search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.fab4search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment searchPagerWidget = MyPagerFragment.newInstance();
                replaceFragment(searchPagerWidget, MyPagerFragment.ID);
            }
        });
        Fragment searchPagerWidget = MyPagerFragment.newInstance();
        replaceFragment(searchPagerWidget, MyPagerFragment.ID);

    }

    @Override
    public void replaceFragment(@NotNull Fragment fragment, String mTAG ) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, mTAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showFAB(String mTAG) {
//        if (findViewById(R.id.fab4search) == null ) {
//            return;
//        }
        if ( MyPagerFragment.ID.equals(mTAG)) {
            findViewById(R.id.fab4search).setVisibility(View.INVISIBLE);
        }
        else {
            findViewById(R.id.fab4search).setVisibility(View.VISIBLE);
        }
    }

}
