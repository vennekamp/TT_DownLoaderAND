package com.teufelsturm.tt_downloader3.foundRouteSingle;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.teufelsturm.tt_downloader3.dbHelper.DataBaseHelper;
import com.teufelsturm.tt_downloader3.model.TT_Comment_AND;
import com.teufelsturm.tt_downloader3.model.TT_Route_AND;
import com.teufelsturm.tt_downloader3.repos.Queries;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ViewModel4TT_RouteFoundFragment extends AndroidViewModel {
    public static final String TAG = ViewModel4TT_RouteFoundFragment.class.getSimpleName();

    public ViewModel4TT_RouteFoundFragment(@NonNull Application application) {
        super(application);
    }

    private MutableLiveData<TT_Route_AND> aTT_Route_AND = new MutableLiveData<>();
    public String strSummitName_inComment;
    public String strAreaName_inComment;


    public MutableLiveData<TT_Route_AND> getaTT_Route_AND() {
        return aTT_Route_AND;
    }
    public void setaTT_Route_AND(TT_Route_AND tt_Route_AND) {
        this.aTT_Route_AND.setValue(tt_Route_AND);
    }

    private MutableLiveData<ArrayList<TT_Comment_AND>> lstTT_Comment_AND;

    LiveData<ArrayList<TT_Comment_AND>> getLstTT_Comment_AND() {
        if (lstTT_Comment_AND == null) {
            lstTT_Comment_AND = new MutableLiveData<>();
            lstTT_Comment_AND.setValue(new ArrayList<>());
            loadLstTT_Comment_AND();
        }
        return lstTT_Comment_AND;
    }

    private void loadLstTT_Comment_AND() {
        // query all routes to this summit
        this.openAndQueryDatabase( getaTT_Route_AND().getValue() );
    }

    public boolean hasUnSavedData;


    // **************************
    private void openAndQueryDatabase(@NotNull TT_Route_AND aTT_Route_AND) {
        Log.i(TAG, "Neuer openAndQueryDatabase... " + aTT_Route_AND.getStrWegName());
        SQLiteDatabase newDB = null;
        try {
            DataBaseHelper dbHelper = new DataBaseHelper( getApplication().getApplicationContext() );
            newDB = dbHelper.getWritableDatabase();
            Cursor cursor = null;
            String queryString1;
            Log.i(TAG,"Namen und Gebiet zum Gipfel # : " + aTT_Route_AND.getIntGipfelNr());
            cursor = newDB.rawQuery(Queries.getSQL4CommentsBySummit(aTT_Route_AND.getIntGipfelNr()),
                    null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    strSummitName_inComment = cursor.getString(cursor
                            .getColumnIndex("strName"));
                    strAreaName_inComment = cursor.getString(cursor
                            .getColumnIndex("strGebiet"));
                    Log.i(TAG,"Neuen Kommentar zum Weg im Gebiet: " + strAreaName_inComment);
                }
            }

            String queryString4CommentsByRoute = Queries.getSQL4CommentsByRoute(aTT_Route_AND.getIntTT_IDOrdinal());
            Log.i(TAG,"Neuen Kommentar zum Weg suchen:\r\n" + queryString4CommentsByRoute);

            cursor = newDB.rawQuery(queryString4CommentsByRoute, null);
            Log.i(TAG,"Neuen Kommentar zum Weg suchen:\t c != null'"
                    + (cursor != null));

            if (cursor != null) {
                int iCounter = 0;
                if (cursor.moveToFirst()) {
                    do {
                        int intTTWegNr = cursor.getInt(cursor
                                .getColumnIndex("intTTWegNr"));
                        Log.i(TAG," -> intTTWegNr..... " + intTTWegNr);
                        String strEntryKommentar = cursor.getString(cursor
                                .getColumnIndex("strEntryKommentar"));
                        Log.i(TAG," -> strEntryKommentar..... "
                                + strEntryKommentar);
                        Integer intEntryBewertung = cursor.getInt(cursor
                                .getColumnIndex("entryBewertung"));
                        Log.i(TAG," -> intEntryBewertung..... "
                                + intEntryBewertung);
                        String strEntryUser = cursor.getString(cursor
                                .getColumnIndex("strEntryUser"));
                        Log.i(TAG," -> strEntryUser..... " + strEntryUser);
                        Long longEntryDatum = cursor.getLong(cursor
                                .getColumnIndex("entryDatum"));
                        Log.i(TAG," -> longEntryDatum..... " + longEntryDatum);

                        lstTT_Comment_AND.getValue().add(new TT_Comment_AND(intTTWegNr,
                                strEntryKommentar, aTT_Route_AND
                                .getStr_TTSummitName(), aTT_Route_AND
                                .getStr_TTSummitName(), aTT_Route_AND
                                .getIntGipfelNr(), intEntryBewertung,
                                strEntryUser, longEntryDatum));
                        Log.i(TAG,++iCounter + " -> Neuer Kommentar... " + strEntryUser);
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


}