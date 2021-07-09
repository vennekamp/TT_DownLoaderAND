package com.teufelsturm.tt_downloader3.foundSummitList;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.teufelsturm.tt_downloader3.TT_DownLoadedApp;
import com.teufelsturm.tt_downloader3.dbHelper.StaticSQLQueries;
import com.teufelsturm.tt_downloader3.model.TT_Summit_AND;

import java.util.ArrayList;

public class ViewModel4TT_SummitsFoundFragment extends AndroidViewModel {
    private static final String TAG = ViewModel4TT_SummitsFoundFragment.class.getSimpleName();

    public ViewModel4TT_SummitsFoundFragment(@NonNull Application application) {
        super(application);
    }

    String strTextSuchtext = "";
    public String strGebiet = "";
    int intMinAnzahlDerWege = 0;
    int intMaxAnzahlDerWege = 250;
    int intMinAnzahlDerSternchenWege = 0;
    int intMaxAnzahlDerSternchenWege = 100;


    private MediatorLiveData<ArrayList<TT_Summit_AND>> lstTT_Summit_AND;
    MutableLiveData<ArrayList<TT_Summit_AND>> getLstTT_Route_AND() {
        if (lstTT_Summit_AND == null) {
            lstTT_Summit_AND = new MediatorLiveData<>();
            lstTT_Summit_AND.setValue(new ArrayList<>());
            loadLstTT_Gipfel_AND();
        }
        return lstTT_Summit_AND;
    }

    private void loadLstTT_Gipfel_AND() {
        // query all routes to this summit
        this.openAndQueryDatabase();
    }

    private void openAndQueryDatabase() {
        Log.i(TAG,"Neuer openAndQueryDatabase... ");
        lstTT_Summit_AND.getValue().clear();
        try {
            String queryString1;
            queryString1 = StaticSQLQueries.getSQL4SummitsSearch(strTextSuchtext, strGebiet,
                    intMinAnzahlDerWege, intMaxAnzahlDerWege,
                    intMinAnzahlDerSternchenWege, intMaxAnzahlDerSternchenWege);
            Log.i(TAG, "Neuen SuchText: '" + strTextSuchtext + "'");
            Cursor cursor = TT_DownLoadedApp.getDataBaseHelper().getMyDataBase()
                    .rawQuery(queryString1, null);
            Log.i(TAG,"Neuen Gipfel gesucht... 'c != null'" + (cursor != null));

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String strGipfelName = cursor.getString(cursor
                            .getColumnIndex("strName"));
                    int intTTGipfelNr = cursor.getInt(cursor
                            .getColumnIndex("intTTGipfelNr"));
                    int intKleFuGipfelNr = cursor.getInt(cursor
                            .getColumnIndex("intKleFuGipfelNr"));
                    String str_Area = cursor.getString(cursor
                            .getColumnIndex("strGebiet"));
                    int int_NumberOfRoutes = cursor.getInt(cursor
                            .getColumnIndex("intAnzahlWege"));
                    int int_NumberofStarRoutes = cursor.getInt(cursor
                            .getColumnIndex("intAnzahlSternchenWege"));
                    String str_EasiestGrade = cursor.getString(cursor
                            .getColumnIndex("strLeichtesterWeg"));
                    double dbl_GpsLat = cursor.getDouble(cursor
                            .getColumnIndex("dblGPS_Latitude"));
                    double dbl_GpsLong = cursor.getDouble(cursor
                            .getColumnIndex("dblGPS_Longitude"));
                    // Read from Firebase TODO TODO TODO TODO???
                    MutableLiveData<Boolean> bln_Asscended = new MutableLiveData<>();
                    MutableLiveData<Long> long_DateAsscended = new MutableLiveData<>();
                    MutableLiveData<String> str_MyComment = new MutableLiveData<>();

                    bln_Asscended.setValue( cursor.getInt(cursor.getColumnIndex("isAscendedSummit")) > 0 );
                    long_DateAsscended.setValue(cursor.getLong(cursor.getColumnIndex("intDateOfAscend")) );
                    str_MyComment.setValue( cursor.getString(cursor.getColumnIndex("strMySummitComment")) );

                    MediatorLiveData<TT_Summit_AND> tt_summit_andMediatorLiveData = new MediatorLiveData<>();
                    tt_summit_andMediatorLiveData.setValue( new TT_Summit_AND(intTTGipfelNr,
                            intKleFuGipfelNr, strGipfelName,
                            str_Area, int_NumberOfRoutes,
                            int_NumberofStarRoutes,
                            str_EasiestGrade, dbl_GpsLong,
                            dbl_GpsLat,
                                bln_Asscended,
                                long_DateAsscended,
                                str_MyComment) );
                    tt_summit_andMediatorLiveData.addSource(bln_Asscended, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            Log.e(TAG, "public void onChanged(Boolean aBoolean) {: <" + aBoolean + ">");
                            lstTT_Summit_AND.postValue(lstTT_Summit_AND.getValue());
                        }
                    });
                    tt_summit_andMediatorLiveData.addSource(long_DateAsscended, new Observer<Long>() {
                        @Override
                        public void onChanged(Long aLong) {
                            Log.e(TAG, "public void onChanged(Long aLong) {: <" + aLong + ">");
                            lstTT_Summit_AND.postValue(lstTT_Summit_AND.getValue());
                        }
                    });
                    tt_summit_andMediatorLiveData.addSource(str_MyComment, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            Log.e(TAG, "public void onChanged(String s) {: <" + s + ">");
                            lstTT_Summit_AND.postValue(lstTT_Summit_AND.getValue());
                        }
                    });

                    lstTT_Summit_AND.getValue().add(tt_summit_andMediatorLiveData.getValue());
                    lstTT_Summit_AND.addSource(tt_summit_andMediatorLiveData, new Observer<TT_Summit_AND>() {
                        @Override
                        public void onChanged(TT_Summit_AND tt_summit_and) {
                            Log.e(TAG, "public void onChanged(TT_Summit_AND tt_summit_and) für: <" + tt_summit_and.getStr_TTSummitName() + ">");
                            Log.e(TAG, "public void onChanged(TT_Summit_AND tt_summit_and) Kommentar: <" + tt_summit_and.getStr_MyComment() + ">");
                        }
                    });

                    Log.i(TAG," -> Neuer Gipfel... " + strGipfelName + "\r\nTT-Gipfelnummer: " + intTTGipfelNr);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException se) {
            Log.e(TAG,"Could not create or Open the database");
        } finally {
            Toast.makeText(getApplication().getApplicationContext(), lstTT_Summit_AND.getValue().size() + " Gipfel gefunden",
                    Toast.LENGTH_LONG).show();
        }
    }

}
