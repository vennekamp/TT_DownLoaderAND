package com.teufelsturm.tt_downloader3.foundRoutesList;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.teufelsturm.tt_downloader3.R;
import com.teufelsturm.tt_downloader3.SteinFibelApplication;
import com.teufelsturm.tt_downloader3.model.TT_Route_AND;
import com.teufelsturm.tt_downloader3.dbHelper.StaticSQLQueries;
import com.teufelsturm.tt_downloader3.tt_enums.EnumSachsenSchwierigkeitsGrad;
import com.teufelsturm.tt_downloader3.tt_enums.EnumTT_WegBewertung;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class ViewModel4TT_RoutesFoundFragment extends AndroidViewModel {
    private static String TAG = ViewModel4TT_RoutesFoundFragment.class.getSimpleName();

    public ViewModel4TT_RoutesFoundFragment(@NonNull Application application) {
        super(application);
    }
    public String strTextSuchtext = "";
    public String strGebiet = "";
    public int intMinSchwierigkeit = EnumSachsenSchwierigkeitsGrad.getMinInteger();
    public int intMaxSchwierigkeit = EnumSachsenSchwierigkeitsGrad.getMaxInteger();
    public int intMinAnzahlDerKommentare = 0;
    public float floatMittlereWegBewertung = (float)EnumTT_WegBewertung.getMaxInteger();

	private MediatorLiveData<ArrayList<TT_Route_AND>> lstTT_Routes_AND;
    MediatorLiveData<ArrayList<TT_Route_AND>> getLstTT_Route_AND() {
        if (lstTT_Routes_AND == null) {
            lstTT_Routes_AND = new MediatorLiveData<>();
            lstTT_Routes_AND.setValue(new ArrayList<>());
            loadLstTT_Gipfel_AND();

        }
        return lstTT_Routes_AND;
    }

    private void loadLstTT_Gipfel_AND() {
        // query all routes to this summit
        this.openAndQueryDatabase();
    }



    private void openAndQueryDatabase() {
        lstTT_Routes_AND.getValue().clear();
        try {
            String queryString = StaticSQLQueries.getSQL4RoutesSearch(getApplication().getApplicationContext(),
                    intMinAnzahlDerKommentare, floatMittlereWegBewertung, strTextSuchtext, strGebiet,
                    intMinSchwierigkeit, intMaxSchwierigkeit);

            Cursor cursor = SteinFibelApplication.getDataBaseHelper().getMyDataBase().rawQuery(queryString, null);
            Log.i(TAG,"Neue Routen gesucht... 'c != null'" + (cursor != null));

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        Integer intTTWegNr = cursor.getInt(cursor.getColumnIndex("intTTWegNr"));
                        Integer intGipfelNr = cursor.getInt(cursor.getColumnIndex("intTTGipfelNr"));
                        String WegName = cursor.getString(cursor.getColumnIndex("WegName"));
                        String strGipfelName = cursor.getString(cursor.getColumnIndex("strName"));
                        Boolean blnAusrufeZeichen = cursor.getInt(cursor.getColumnIndex("blnAusrufeZeichen")) > 0;
                        Integer intSterne = cursor.getInt(cursor.getColumnIndex("intSterne"));
                        String strSchwierigkeitsGrad = cursor.getString(cursor.getColumnIndex("strSchwierigkeitsGrad"));
                        Integer intSprungSchwierigkeitsGrad = cursor.getInt(cursor
                                        .getColumnIndex("intSprungSchwierigkeitsGrad"));
                        Integer sachsenSchwierigkeitsGrad = cursor.getInt(cursor
                                        .getColumnIndex("sachsenSchwierigkeitsGrad"));
                        Integer ohneUnterstuetzungSchwierigkeitsGrad = cursor.getInt(cursor
                                        .getColumnIndex("ohneUnterstützungSchwierigkeitsGrad"));
                        Integer rotPunktSchwierigkeitsGrad = cursor.getInt(cursor
                                        .getColumnIndex("rotPunktSchwierigkeitsGrad"));
                        Integer intAnzahlDerKommentare = cursor.getInt(cursor
                                .getColumnIndex("intAnzahlDerKommentare"));
                        Float fltMittlereWegBewertung = cursor.getFloat(cursor
                                        .getColumnIndex("fltMittlereWegBewertung"));
                        // Read from Firebase TODO TODO TODO TODO???
                        MutableLiveData<Integer> intBegehungsStil = new MutableLiveData<>();
                        MutableLiveData<Long> long_DateAsscended = new MutableLiveData<>();
                        MutableLiveData<String> strKommentar = new MutableLiveData<>();

                        intBegehungsStil.setValue(cursor.getInt(cursor.getColumnIndex("isAscendedRoute")));
                        long_DateAsscended.setValue(cursor.getLong(cursor.getColumnIndex("intDateOfAscend")));
                        strKommentar.setValue(cursor.getString(cursor.getColumnIndex("strMyRouteComment")));

                        MediatorLiveData<TT_Route_AND> tt_route_andMutableLiveData = new MediatorLiveData<>();
                        tt_route_andMutableLiveData.setValue(
                                        new TT_Route_AND(intTTWegNr,
                                intGipfelNr, WegName, strGipfelName,
                                blnAusrufeZeichen, intSterne,
                                strSchwierigkeitsGrad,
                                sachsenSchwierigkeitsGrad,
                                ohneUnterstuetzungSchwierigkeitsGrad,
                                rotPunktSchwierigkeitsGrad,
                                intSprungSchwierigkeitsGrad,
                                intAnzahlDerKommentare,
                                fltMittlereWegBewertung,
                                                intBegehungsStil,
                                                long_DateAsscended,
                                                strKommentar));
                        tt_route_andMutableLiveData.addSource(intBegehungsStil, new Observer<Integer>() {
                            @Override
                            public void onChanged(Integer integer) {
                                Log.e(TAG, "public void onChanged(Integer integer) {: <" + integer + ">");
                                lstTT_Routes_AND.postValue( lstTT_Routes_AND.getValue() );
                            }
                        });
                        tt_route_andMutableLiveData.addSource(long_DateAsscended, new Observer<Long>() {
                            @Override
                            public void onChanged(Long aLong) {
                                Log.e(TAG, "public void onChanged(Long aLong) {: <" + aLong + ">");
                                lstTT_Routes_AND.postValue( lstTT_Routes_AND.getValue());
                            }
                        });
                        tt_route_andMutableLiveData.addSource(strKommentar, new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                Log.e(TAG, "public void onChanged(String s) {: <" + s + ">");
                                lstTT_Routes_AND.postValue(lstTT_Routes_AND.getValue());

                            }
                        });

                        lstTT_Routes_AND.getValue().add(tt_route_andMutableLiveData.getValue());
                        lstTT_Routes_AND.addSource(tt_route_andMutableLiveData, new Observer<TT_Route_AND>() {
                            @Override
                            public void onChanged(TT_Route_AND tt_route_and) {
                                Log.e(TAG, "public void onChanged(TT_Route_AND tt_route_and) <" + tt_route_and.getStrWegName() + ">");
                                Log.e(TAG, "public void onChanged(TT_Route_AND tt_route_and) Kommentar: <" + tt_route_and.getStrKommentar() + ">");
                            }
                        });


                        Log.i(TAG, " -> Neue Route... " + strGipfelName + "\r\nintTTWegNr: "
                                + intTTWegNr + "\t" + WegName);
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException se) {
            Log.e(TAG,"Could not create or Open the database");
        } finally {
            Toast.makeText(getApplication().getApplicationContext(),
                    lstTT_Routes_AND.getValue().size() + " Wege gefunden"
                    + ( lstTT_Routes_AND.getValue().size()  ==  getApplication().getApplicationContext()
                            .getResources().getInteger(R.integer.MaxNoItemQuerxy)
                    ? " (Maximalanzahl an Ergebnissen erreicht)" : ""), Toast.LENGTH_LONG)
                    .show();
        }
    }


}
