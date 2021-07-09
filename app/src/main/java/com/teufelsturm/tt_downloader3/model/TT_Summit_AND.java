package com.teufelsturm.tt_downloader3.model;


import android.database.Cursor;
import android.os.Looper;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.teufelsturm.tt_downloader3.TT_DownLoadedApp;
import com.teufelsturm.tt_downloader3.dbHelper.StaticSQLQueries;
import com.teufelsturm.tt_downloader3.firestoreHelper.UserSummitComment;
import com.teufelsturm.tt_downloader3.repositories.RepositoryFactory;

import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.lifecycle.MutableLiveData;

public class TT_Summit_AND extends BaseModel {
    private static String TAG = TT_Summit_AND.class.getSimpleName();
    private int int_SummitNumberOfficial;
    private String str_Area;
    private int int_NumberOfRoutes;
    private int int_NumberofStarRoutes;
    private String str_EasiestGrade;
    private double dbl_GpsLong;
    private double dbl_GpsLat;
    /* Properties for the comments */
    private MutableLiveData<Boolean> bln_Asscended = new MutableLiveData<>();
    private MutableLiveData<Long> long_DateAsscended = new MutableLiveData<>();
    private MutableLiveData<String> str_MyComment = new MutableLiveData<>();
    private DocumentReference docRef;
    public  DocumentReference getDocRef() {
        return docRef;
    }


    public TT_Summit_AND(int intTTGipfelNr,
                         int int_SummitNumberOfficial, String str_SummitName,
                         String str_Area, Integer int_NumberOfRoutes,
                         int int_NumberofStarRoutes, String str_EasiestGrade,
                         double dbl_GpsLong, double dbl_GpsLat,
                         @Nullable MutableLiveData<Boolean> bln_Asscended,
                         @Nullable MutableLiveData<Long> long_DateAsscended,
                         @Nullable MutableLiveData<String> str_MyComment) {
        super.intTT_IDOrdinal = intTTGipfelNr;
        super.str_TTSummitName = str_SummitName;

        Log.i(TAG,"--> intTTGipfelNr eingetragen...: " + intTTGipfelNr);
        this.int_SummitNumberOfficial = int_SummitNumberOfficial;
        this.str_Area = str_Area;
        this.int_NumberOfRoutes = int_NumberOfRoutes;
        this.int_NumberofStarRoutes = int_NumberofStarRoutes;
        this.str_EasiestGrade = str_EasiestGrade;
        this.dbl_GpsLong = dbl_GpsLong;
        this.dbl_GpsLat = dbl_GpsLat;
        this.bln_Asscended = bln_Asscended;
        this.long_DateAsscended = long_DateAsscended;
        this.str_MyComment = str_MyComment;
        updateFireBaseData();
        RepositoryFactory.getSummitRepository(null).saveItem(this);
    }

    public TT_Summit_AND(int intTTGipfelNr) {
        super.intTT_IDOrdinal = intTTGipfelNr;

        Log.i(TAG, "--> intTTGipfelNr eingetragen...: " + intTTGipfelNr);
        Log.i(TAG, "intTTGipfelNr... : " + intTTGipfelNr);
        String queryStringSQL4SummitObject = StaticSQLQueries.getSQL4SummitObject(intTTGipfelNr);
        Log.i(TAG, "Neue Wege zum Gipfel suchen:\r\n" + queryStringSQL4SummitObject);

        Log.i(TAG, "noch bin ich da....");
        Cursor cursor = TT_DownLoadedApp.getDataBaseHelper().getMyDataBase()
                .rawQuery(queryStringSQL4SummitObject, null);
        Log.i(TAG, "Neue Wege zum Gipfel suchen:\t c != null'" + (cursor != null));

        if (cursor != null && cursor.moveToFirst())  {
            int iCounter = 0;
                do {
                    super.intTT_IDOrdinal = intTTGipfelNr;
                    String WegName = cursor.getString(cursor
                            .getColumnIndex("strName"));
                    this.int_SummitNumberOfficial = cursor.getInt(cursor
                            .getColumnIndex("intKleFuGipfelNr"));
                    super.str_TTSummitName = cursor.getString(cursor
                            .getColumnIndex("strName"));
                    this.str_Area = cursor.getString(cursor
                            .getColumnIndex("strGebiet"));
                    this.int_NumberOfRoutes = cursor.getInt(cursor
                            .getColumnIndex("intAnzahlWege"));
                    this.int_NumberofStarRoutes = cursor.getInt(cursor
                            .getColumnIndex("intAnzahlSternchenWege"));
                    this.str_EasiestGrade = cursor.getString(cursor
                            .getColumnIndex("strLeichtesterWeg"));
                    this.dbl_GpsLong = cursor.getDouble(cursor
                            .getColumnIndex("dblGPS_Longitude"));
                    this.dbl_GpsLat = cursor.getDouble(cursor
                            .getColumnIndex("dblGPS_Latitude"));
                    this.bln_Asscended.setValue(cursor.getInt(cursor
                            .getColumnIndex("isAscendedSummit")) > 0);
                    this.long_DateAsscended.setValue(cursor.getLong(cursor
                            .getColumnIndex("intDateOfAscend")));
                    this.str_MyComment.setValue(cursor.getString(cursor
                            .getColumnIndex("strMySummitComment")));
                    Log.i(TAG, ++iCounter + " -> Neuer Weg... " + WegName);
                } while (cursor.moveToNext());
            cursor.close();
        }
        updateFireBaseData();
        RepositoryFactory.getSummitRepository(null).saveItem(this);
    }

    private void updateFireBaseData(){
        this.docRef = new UserSummitComment().receiveUserSummitComment(this);
        if ( docRef != null ) {
            docRef.addSnapshotListener((snapshot, e) -> {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    setBln_Asscended( snapshot.getBoolean("IsAscendedSummit"));
                    setDatumBestiegen(snapshot.getLong("DateAsscended"));
                    setStr_MyComment(snapshot.getString("UserSummitComment"));
//                } else {
//                        Log.d(TAG, "Current data: null");
                }
            });
        }
    }

    public int getInt_SummitNumberOfficial() {
        return int_SummitNumberOfficial;
    }

    public String getStr_Area() {
        return str_Area;
    }

    public int getInt_NumberOfRoutes() {
        return int_NumberOfRoutes;
    }

    public int getInt_NumberofStarRoutes() {
        return int_NumberofStarRoutes;
    }

    public String getStr_EasiestGrade() {
        return str_EasiestGrade;
    }

    public double getDbl_GpsLong() {
        return dbl_GpsLong;
    }

    public double getDbl_GpsLat() {
        return dbl_GpsLat;
    }

    /*
     * Getter for the Comments
     */
    public Boolean getBln_Asscended() {
        return (bln_Asscended != null) && bln_Asscended.getValue() != null && bln_Asscended.getValue();
    }

    public Long getLong_DateAsscended() {
        return long_DateAsscended.getValue();
    }
    public String getStr_DateAsscended() {
        if (long_DateAsscended.getValue() == null || long_DateAsscended.getValue() == 0)
            return "   ";
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MMM.yyyy",
                Locale.GERMANY);
        Date resultdate = new Date(long_DateAsscended.getValue());
        return sdf.format(resultdate);
    }

    public String getStr_MyComment() {
        if (str_MyComment.getValue() == null)
            return "";
        return str_MyComment.getValue();
    }
    /*
     * Setter for the Comments
     */
    public void setBln_Asscended(Boolean aBln_Asscended) {
        Log.e(TAG, "public void setBln_Asscended(Boolean bln_Asscended) {: <" + aBln_Asscended + ">");
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            // On UI thread.
            this.bln_Asscended.setValue(aBln_Asscended);
        } else {
            // Not on UI thread.
            this.bln_Asscended.postValue(aBln_Asscended);
        }
    }


    public void setStr_MyComment(String aStr_MyComment) {
        Log.e(TAG, "public void setStr_MyComment(String aStr_MyComment) {: <" + aStr_MyComment + ">");
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            // On UI thread.
            this.str_MyComment.setValue(aStr_MyComment);
        } else {
            // Not on UI thread.
            this.str_MyComment.postValue(aStr_MyComment);
        }
    }


    public void setDatumBestiegen(Long aLong_DateAsscended) {
        Log.e(TAG, "public void setDatumBestiegen(Long aLong_DateAsscended) {: <" + aLong_DateAsscended + ">");
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            // On UI thread.
            this.long_DateAsscended.setValue(aLong_DateAsscended);
        } else {
            // Not on UI thread.
            this.long_DateAsscended.postValue(aLong_DateAsscended);
        }
    }
}
