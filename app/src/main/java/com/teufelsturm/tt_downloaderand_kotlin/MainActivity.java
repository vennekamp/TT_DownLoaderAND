package com.teufelsturm.tt_downloaderand_kotlin;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.teufelsturm.tt_downloaderand_kotlin.comments.TT_CommentsFoundFragment;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends FragmentActivity implements OnFragmentInteractionListener {

    public static final Uri URI_COMMENT =  Uri.parse("com.teufelsturm.comment");
    public static final Uri URI_ROUTES = Uri.parse("com.teufelsturm.route");
    public static final Uri URI_SUMMITS = Uri.parse("com.teufelsturm.summit");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment searchPagerWidget = new SearchPagerWidget();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, searchPagerWidget);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(@NotNull Uri uri) {

    }

    private void swapFragment()
    {
        Fragment newFragment = TT_CommentsFoundFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
