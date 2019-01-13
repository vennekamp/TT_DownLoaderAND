package com.teufelsturm.tt_downloader3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.teufelsturm.tt_downloader3.searches.MyPagerFragment;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity implements OnFragmentReplaceListener {
    public static final String TAG = MainActivity.class.getSimpleName();

    public final static String DB_NAME = "TT_DownLoader_AND.mp3";
//    public final static String MAP_NAME = "saechsische_schweiz.map";
    public static final int RC_SIGN_IN = 3894;

    // see http://stackoverflow.com/a/21520641
    private boolean exit = false;
    private Toast exitToast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.fab4search).setOnClickListener(v -> {
            Fragment searchPagerWidget = MyPagerFragment.newInstance();
            replaceFragment(searchPagerWidget, MyPagerFragment.ID);
        });
        if (savedInstanceState == null ) {
            // set you initial fragment object
            Fragment searchPagerWidget = MyPagerFragment.newInstance();
            replaceFragment(searchPagerWidget, MyPagerFragment.ID);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.e(TAG, "response: " + response + "; user: " + user);

                // (optional) download and show to image
                String userPhotoUrl = String.valueOf (user.getPhotoUrl());
                new DownloadFileFromURL(this).execute(userPhotoUrl);
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Log.e(TAG, "response: " + response + "; resultCode: " + resultCode + "("
                        + CommonStatusCodes.getStatusCodeString(resultCode) + ")");
            }
        }

    }
    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            // see http://stackoverflow.com/a/21520641
            if (exit) {
                exitToast.cancel();
                finish();
            } else {
                // For customized TOAST see: http://stackoverflow.com/a/17801115
                exitToast = Toast.makeText(this,
                        "\nZum Verlassen 'Zurück' nochmals drücken.\n",
                        Toast.LENGTH_LONG);
                exitToast.show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                },3 * 1000);
            }
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 1) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void replaceFragment(@NotNull Fragment fragment, String mTAG) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, mTAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showFAB(String mTAG) {
        if ( findViewById(R.id.fab4search) == null ) {
            Crashlytics.logException(new NullPointerException("findViewById(R.id.fab4search) == null"));
            return;
        }
        if (MyPagerFragment.ID.equals(mTAG)) {
            findViewById(R.id.fab4search).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.fab4search).setVisibility(View.VISIBLE);
        }
    }
}
