package com.teufelsturm.tt_downloader3.foundSummitSingle;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Location;
import android.util.Log;

import com.teufelsturm.tt_downloader3.dbHelper.DataBaseHelper;
import com.teufelsturm.tt_downloader3.model.TT_Route_AND;
import com.teufelsturm.tt_downloader3.model.TT_Summit_AND;
import com.teufelsturm.tt_downloader3.repos.Queries;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class ViewModel4TT_SummitFoundFragment extends AndroidViewModel {
    private static final String TAG = ViewModel4TT_SummitFoundFragment.class.getSimpleName();

    public ViewModel4TT_SummitFoundFragment(@NonNull Application application) {
        super(application);
    }

    private MutableLiveData<TT_Summit_AND> aTT_Summit_AND = new MutableLiveData<>();
    public MutableLiveData<TT_Summit_AND> getaTT_Summit_AND() {
        return aTT_Summit_AND;
    }
    public void setaTT_Summit_AND(TT_Summit_AND aTT_Summit_AND) {
        this.aTT_Summit_AND.setValue(aTT_Summit_AND);
    }

    private MediatorLiveData<ArrayList<TT_Route_AND>> lstTT_Route_AND;
    LiveData<ArrayList<TT_Route_AND>> getLstTT_Route_AND() {
        if (lstTT_Route_AND == null) {
            lstTT_Route_AND = new MediatorLiveData<>();
            lstTT_Route_AND.setValue(new ArrayList<TT_Route_AND>());
            loadLstTT_Route_AND();
        }
        return lstTT_Route_AND;
    }

    private void loadLstTT_Route_AND() {
        // query all routes to this summit
        this.openAndQueryDatabase(getaTT_Summit_AND().getValue());
    }


    public Map<Integer, String> hashmapNeighbourSummit = new HashMap<>();
    public boolean hasUnSavedData;




    private void openAndQueryDatabase(TT_Summit_AND aTT_Summit_AND) {
        Log.i(TAG, "Neuer openAndQueryDatabase... " + aTT_Summit_AND.getStr_TTSummitName());
        // IMPORTANT: old List needs to be cleared!
        lstTT_Route_AND.getValue().clear();
        SQLiteDatabase newDB = null;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(getApplication().getApplicationContext());
            newDB = dbHelper.getWritableDatabase();

//            String queryString1;
            Cursor cursor = null;
            cursor = newDB.rawQuery(Queries.getSQL4SummitsAscension(aTT_Summit_AND.getIntTT_IDOrdinal()), null);
            if (cursor != null && cursor.moveToFirst()) {
                aTT_Summit_AND.setBln_Asscended(cursor.getInt(cursor
                        .getColumnIndex("isAscendedSummit")) > 0);
                aTT_Summit_AND.setDatumBestiegen(cursor.getLong(cursor
                        .getColumnIndex("intDateOfAscend")));
                aTT_Summit_AND.setStr_MyComment(cursor.getString(cursor
                        .getColumnIndex("strMySummitComment")));
            }
            String queryString4SummitNeighbours = Queries.getSQL4SummitNeighbours(aTT_Summit_AND.getIntTT_IDOrdinal());
            Log.i(TAG, "Neuer Nachbargipfel suchen:\r\n" + queryString4SummitNeighbours);
            cursor = newDB.rawQuery(queryString4SummitNeighbours, null);
            Log.i(TAG, " --> cursor.getCount() " + cursor.getCount() + "'");
            if ( cursor != null ) {
                int iCounter = 0;
                Location mainCoordinates = new Location("reverseGeocoded");
                Location neighborCoordinate = new Location("reverseGeocoded");
                mainCoordinates.setLatitude(aTT_Summit_AND.getDbl_GpsLat());
                mainCoordinates.setLongitude(aTT_Summit_AND.getDbl_GpsLong());
                mainCoordinates.setTime(new Date().getTime());
                Log.i(TAG, " --> mainCoordinates ");

                DecimalFormat aDecimalFormat = new DecimalFormat("##");
                if (cursor.moveToFirst()) {
                    do {
                        neighborCoordinate.setLatitude(cursor.getDouble(cursor
                                .getColumnIndex("dblGPS_Latitude")));
                        neighborCoordinate.setLongitude(cursor.getDouble(cursor
                                .getColumnIndex("dblGPS_Longitude")));
                        neighborCoordinate.setTime(new Date().getTime());

                        Integer intTTGipfelNachbarNr = cursor.getInt(cursor
                                .getColumnIndex("intTTGipfelNr"));
                        Log.i(TAG,
                                " --> neighborCoordinate \t("
                                        + intTTGipfelNachbarNr + ")");
                        switch (++iCounter) {
                            case 1:
                                hashmapNeighbourSummit
                                        .put(intTTGipfelNachbarNr,
                                                getStrDistance(cursor,
                                                        mainCoordinates,
                                                        aDecimalFormat,
                                                        neighborCoordinate));
                                Log.i(TAG,
                                        " --> neighborCoordinate im 'put'\t"
                                                + intTTGipfelNachbarNr + ")");

                                break;
                            case 2:
                                hashmapNeighbourSummit
                                        .put(intTTGipfelNachbarNr,
                                                getStrDistance(cursor,
                                                        mainCoordinates,
                                                        aDecimalFormat,
                                                        neighborCoordinate));
                                Log.i(TAG,
                                        " --> neighborCoordinate im 'put'\t"
                                                + intTTGipfelNachbarNr + ")");
                                break;
                            case 3:
                                hashmapNeighbourSummit
                                        .put(intTTGipfelNachbarNr,
                                                getStrDistance(cursor,
                                                        mainCoordinates,
                                                        aDecimalFormat,
                                                        neighborCoordinate));
                                Log.i(TAG,
                                        " --> neighborCoordinate im 'put'\t"
                                                + intTTGipfelNachbarNr + ")");
                                break;
                            case 4:
                                hashmapNeighbourSummit
                                        .put(intTTGipfelNachbarNr,
                                                getStrDistance(cursor,
                                                        mainCoordinates,
                                                        aDecimalFormat,
                                                        neighborCoordinate));
                                Log.i(TAG,
                                        " --> neighborCoordinate im 'put'\t"
                                                + intTTGipfelNachbarNr + ")");
                                break;
                        }
                        Log.i(TAG," --> wird:"
                                + cursor.getString(cursor
                                .getColumnIndex("strName"))
                                + " in "
                                + (mainCoordinates
                                .distanceTo(neighborCoordinate) < 4000000 ? aDecimalFormat.format(mainCoordinates
                                .distanceTo(neighborCoordinate))
                                : "?") + "m; ");

                    } while (cursor.moveToNext());
                }
            }

            String queryString4RoutesBySummit = Queries.getSQL4RoutesBySummit(aTT_Summit_AND.getIntTT_IDOrdinal());
            Log.i(TAG,"Neue Wege zum Gipfel suchen:\r\n" + queryString4RoutesBySummit);
            cursor = newDB.rawQuery(queryString4RoutesBySummit, null);
            Log.i(TAG,"Neue Wege zum Gipfel suchen:\t c != null'" + (cursor != null));

            if (cursor != null) {
                int iCounter = 0;
                if (cursor.moveToFirst()) {
                    do {
                        String WegName = cursor.getString(cursor
                                .getColumnIndex("WegName"));
                        int intTTWegNr = cursor.getInt(cursor
                                .getColumnIndex("intTTWegNr"));
                        String strSchwierigkeitsGrad = cursor.getString(cursor
                                .getColumnIndex("strSchwierigkeitsGrad"));
                        int intSterne = cursor.getInt(cursor
                                .getColumnIndex("intSterne"));
                        Boolean blnAusrufeZeichen = cursor.getInt(cursor
                                .getColumnIndex("blnAusrufeZeichen")) > 0;
                        Integer sachsenSchwierigkeitsGrad = cursor
                                .getInt(cursor
                                        .getColumnIndex("sachsenSchwierigkeitsGrad"));
                        Integer int_OhneUnterstuetzungSchwierigkeitsGrad = cursor
                                .getInt(cursor
                                        .getColumnIndex("ohneUnterstützungSchwierigkeitsGrad"));
                        Integer int_RotPunktSchwierigkeitsGrad = cursor
                                .getInt(cursor
                                        .getColumnIndex("rotPunktSchwierigkeitsGrad"));
                        int intSprungSchwierigkeitsGrad = cursor.getInt(cursor
                                .getColumnIndex("intSprungSchwierigkeitsGrad"));
                        int intAnzahlDerKommentare = cursor.getInt(cursor
                                .getColumnIndex("intAnzahlDerKommentare"));
                        float fltMittlereWegBewertung = cursor.getFloat(cursor
                                .getColumnIndex("fltMittlereWegBewertung"));
                        // Read from Firebase TODO TODO TODO TODO???
                        MutableLiveData<Integer> int_typeAsscended = new MutableLiveData<>();
                        MutableLiveData<Long> long_DateAsscended = new MutableLiveData<>();
                        MutableLiveData<String> str_Comment = new MutableLiveData<>();

                        int_typeAsscended.setValue(cursor.getInt(cursor.getColumnIndex("isAscendedRoute")));
                        long_DateAsscended.setValue(cursor.getLong(cursor.getColumnIndex("intDateOfAscend")));
                        str_Comment.setValue(cursor.getString(cursor.getColumnIndex("strMyRouteComment")));

                        MediatorLiveData<TT_Route_AND> tt_route_andMutableLiveData = new MediatorLiveData<>();
                        tt_route_andMutableLiveData.setValue(
                                new TT_Route_AND(intTTWegNr,
                                        aTT_Summit_AND.getIntTT_IDOrdinal(), WegName,
                                        aTT_Summit_AND.getStr_TTSummitName(),
                                        blnAusrufeZeichen, intSterne,
                                        strSchwierigkeitsGrad,
                                        sachsenSchwierigkeitsGrad,
                                        int_OhneUnterstuetzungSchwierigkeitsGrad,
                                        int_RotPunktSchwierigkeitsGrad,
                                        intSprungSchwierigkeitsGrad,
                                        intAnzahlDerKommentare,
                                        fltMittlereWegBewertung, int_typeAsscended,
                                        long_DateAsscended, str_Comment));

                        tt_route_andMutableLiveData.addSource(int_typeAsscended, new Observer<Integer>() {
                            @Override
                            public void onChanged(Integer integer) {
                                Log.e(TAG, "public void onChanged(Integer integer) {: <" + integer + ">");
                                lstTT_Route_AND.postValue(lstTT_Route_AND.getValue());
                            }
                        });
                        tt_route_andMutableLiveData.addSource(long_DateAsscended, new Observer<Long>() {
                            @Override
                            public void onChanged(Long aLong) {
                                Log.e(TAG, "public void onChanged(Long aLong) {: <" + aLong + ">");
                                lstTT_Route_AND.postValue((lstTT_Route_AND.getValue()));
                            }
                        });
                        tt_route_andMutableLiveData.addSource(str_Comment, new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                Log.e(TAG, "public void onChanged(String s) {: <" + s + ">");
                                lstTT_Route_AND.postValue((lstTT_Route_AND.getValue()));

                            }
                        });

                        lstTT_Route_AND.getValue().add(tt_route_andMutableLiveData.getValue());
                        lstTT_Route_AND.addSource(tt_route_andMutableLiveData, new Observer<TT_Route_AND>() {
                            @Override
                            public void onChanged(TT_Route_AND tt_route_and) {
                                Log.e(TAG, "public void onChanged(TT_Route_AND tt_route_and) <" + tt_route_and.getStrWegName() + ">");
                                Log.e(TAG, "public void onChanged(TT_Route_AND tt_route_and) Kommentar: <" + tt_route_and.getStrKommentar() + ">");
                            }
                        });

                        Log.i(TAG, ++iCounter + " -> Neuer Weg... " + WegName);
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException se) {
            Log.e(TAG,
                    "Could not create or Open the database");
        } finally {
            newDB.close();
        }
    }


    @NonNull
    private String getStrDistance(@NotNull Cursor cursor, @NotNull Location mainLocation,
                                  DecimalFormat aDecimalFormat, Location neighborCoordinate) {
        return cursor.getString(cursor.getColumnIndex("strName"))
                + System.getProperty("line.separator")
                + " in "
                + ((mainLocation.distanceTo(neighborCoordinate) < 4000000 && mainLocation
                .distanceTo(neighborCoordinate) > 0) ? aDecimalFormat
                .format(mainLocation.distanceTo(neighborCoordinate))
                : "?") + "m";
    }
}
