package com.teufelsturm.tt_downloader3;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.teufelsturm.tt_downloader3.dbHelper.DataBaseHelper;

import org.jetbrains.annotations.Contract;

import androidx.multidex.MultiDex;

public class TT_DownLoadedApp extends Application {

    private  DataBaseHelper myDbHelper;
    private static TT_DownLoadedApp instance = null;

    public void onCreate() {
        super.onCreate();
        // database handler
        this.myDbHelper = DataBaseHelper.getInstance(getApplicationContext());

        TT_DownLoadedApp.instance = this;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if ( user == null ) {
            TT_DownLoadedApp.showNotLoggedInToast(getApplicationContext());
        }
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
    public static void showNotLoggedInToast(Context context) {
        Toast.makeText(context, context.getText(R.string.not_logged_in), Toast.LENGTH_LONG).show();
    }

    @Contract(pure = true)
    public static DataBaseHelper getDataBaseHelper(){
        return TT_DownLoadedApp.instance.myDbHelper;
    }

//    public static Context getTTApplicationContext() { return TT_DownLoadedApp.instance.getApplicationContext(); }
}

