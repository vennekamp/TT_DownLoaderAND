package com.teufelsturm.tt_downloader3.model;

import android.content.Context;
import android.database.Cursor;
import android.os.Looper;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.teufelsturm.tt_downloader3.SteinFibelApplication;
import com.teufelsturm.tt_downloader3.firestoreHelper.UserRouteComment;
import com.teufelsturm.tt_downloader3.dbHelper.StaticSQLQueries;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.lifecycle.MutableLiveData;

public class TT_Route_AND extends BaseModel{
    public static final String TAG = TT_Route_AND.class.getSimpleName();

    private int intTTGipfelNr;
    private String strWegName;
    private boolean blnAusrufeZeichen;
    private int intSterne;
    private String strSchwierigkeitsGrad;
    private int sachsenSchwierigkeitsGrad;
    private int ohneUnterstuetzungSchwierigkeitsGrad;
    private int rotPunktSchwierigkeitsGrad;
    private int intSprungSchwierigkeitsGrad;
    private int intAnzahlDerKommentare;
    private float fltMittlereWegBewertung;
    /* Properties for the comments */
    private MutableLiveData<Integer> intBegehungsStil = new MutableLiveData<>();
    private MutableLiveData<Long> long_DateAsscended = new MutableLiveData<>();
    private MutableLiveData<String> strKommentar = new MutableLiveData<>();

    public TT_Route_AND(int intTTWegNr, int intGipfelNr,
                        String WegName, String strGipfelName, boolean blnAusrufeZeichen,
                        int intSterne, String strSchwierigkeitsGrad,
                        int sachsenSchwierigkeitsGrad,
                        int ohneUnterstuetzungSchwierigkeitsGrad,
                        int rotPunktSchwierigkeitsGrad,
                        int intSprungSchwierigkeitsGrad,
                        int intAnzahlDerKommentare, float fltMittlereWegBewertung,
                        @Nullable MutableLiveData<Integer> isBestiegen,
                        @Nullable MutableLiveData<Long> datumBestiegen,
                        @Nullable MutableLiveData<String> strKommentar) {

        Log.i(TAG, "--> intTTWegNr eingetragen...: "
                + intTTWegNr + "******************************************************************");
        super.intTT_IDOrdinal = intTTWegNr;
        this.intTTGipfelNr = intGipfelNr;
        this.strWegName = WegName;
        super.str_TTSummitName = strGipfelName;
        this.blnAusrufeZeichen = blnAusrufeZeichen;
        this.intSterne = intSterne;
        this.strSchwierigkeitsGrad = strSchwierigkeitsGrad;
        this.sachsenSchwierigkeitsGrad = sachsenSchwierigkeitsGrad;
        this.ohneUnterstuetzungSchwierigkeitsGrad = ohneUnterstuetzungSchwierigkeitsGrad;
        this.rotPunktSchwierigkeitsGrad = rotPunktSchwierigkeitsGrad;
        this.intSprungSchwierigkeitsGrad = intSprungSchwierigkeitsGrad;
        this.intAnzahlDerKommentare = intAnzahlDerKommentare;
        this.fltMittlereWegBewertung = fltMittlereWegBewertung;
        this.intBegehungsStil = isBestiegen;
        this.long_DateAsscended = datumBestiegen;
        this.strKommentar= strKommentar;
        updateFireBaseData();
    }

    //
    public TT_Route_AND(Context context, int intTTWegNr) {
        super.intTT_IDOrdinal = intTTWegNr;

        Log.i(TAG, "--> intTTWegNr eingetragen...: " + intTTWegNr);
        String queryString1 = StaticSQLQueries.getSQL4RouteObject(intTTWegNr);
        Log.i(TAG,"Neue Wege zum Gipfel suchen:\r\n" + queryString1);

        Log.i(TAG, "noch bin ich da....");
//        SQLiteDatabase newDB;
//        DataBaseHelper dbHelper = new DataBaseHelper(context);
        Cursor cursor = SteinFibelApplication.getDataBaseHelper().getMyDataBase().rawQuery(queryString1, null);
        Log.i(TAG, "Neue Wege zum Gipfel suchen:\t c != null'" + (cursor != null));

        if (cursor != null) {
            int iCounter = 0;
            if (cursor.moveToFirst()) {
                do {
                    super.intTT_IDOrdinal = intTTWegNr;
                    String WegName = cursor.getString(cursor
                            .getColumnIndex("intTTWegNr"));
                    this.intTTGipfelNr = cursor.getInt(cursor
                            .getColumnIndex("intTTGipfelNr"));
                    this.strWegName = cursor.getString(cursor
                            .getColumnIndex("WegName"));
                    super.str_TTSummitName = cursor.getString(cursor
                            .getColumnIndex("strName"));
                    this.blnAusrufeZeichen = cursor.getInt(cursor
                            .getColumnIndex("blnAusrufeZeichen")) > 0;
                    this.intSterne = cursor.getInt(cursor
                            .getColumnIndex("intSterne"));
                    this.strSchwierigkeitsGrad = cursor.getString(cursor
                            .getColumnIndex("strSchwierigkeitsGrad"));
                    this.sachsenSchwierigkeitsGrad = cursor.getInt(cursor
                            .getColumnIndex("sachsenSchwierigkeitsGrad"));
                    this.ohneUnterstuetzungSchwierigkeitsGrad = cursor.getInt(cursor
                            .getColumnIndex("ohneUnterstützungSchwierigkeitsGrad"));
                    this.rotPunktSchwierigkeitsGrad = cursor.getInt(cursor
                            .getColumnIndex("rotPunktSchwierigkeitsGrad"));
                    this.intSprungSchwierigkeitsGrad = cursor.getInt(cursor
                            .getColumnIndex("intSprungSchwierigkeitsGrad"));
                    this.intAnzahlDerKommentare = cursor.getInt(cursor
                            .getColumnIndex("intAnzahlDerKommentare"));
                    this.fltMittlereWegBewertung = cursor.getFloat(cursor
                            .getColumnIndex("fltMittlereWegBewertung"));
                    // special treatment for the comment
                    this.long_DateAsscended.setValue(cursor.getLong(cursor
                            .getColumnIndex("intDateOfAscend")));
                    this.strKommentar.setValue(cursor.getString(cursor
                            .getColumnIndex("strMyRouteComment")));
                    Log.i(TAG, ++iCounter + " -> Neuer Weg... " + WegName);
                } while (cursor.moveToNext());
            }
        }
        updateFireBaseData();
    }

    private void updateFireBaseData(){
        DocumentReference docRef = new UserRouteComment().receiveUserRouteComment(this);
        if ( docRef != null ) {
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, "Current data: " + snapshot.getData());
                        setBegehungsStil(snapshot.getLong("TypeRouteAscend").intValue());
                        setDatumBestiegen(snapshot.getLong("DateAsscended"));
                        setStrKommentar(snapshot.getString("UserRouteComment"));
                    } else {
                        // Log.v(TAG, "Current data: null");
                    }
                }
            });
        }
    }


    public Integer getIntGipfelNr() {
        return intTTGipfelNr;
    }

    public String getStrWegName() {
        return strWegName;
    }


    public Boolean getBlnAusrufeZeichen() {
        return blnAusrufeZeichen;
    }

    public Integer getIntSterne() {
        return intSterne;
    }

    public String getStrSchwierigkeitsGrad() {
        return strSchwierigkeitsGrad;
    }

    public Integer getSachsenSchwierigkeitsGrad() {
        return sachsenSchwierigkeitsGrad;
    }

    public Integer getOhneUnterstuetzungSchwierigkeitsGrad() {
        return ohneUnterstuetzungSchwierigkeitsGrad;
    }

    public Integer getRotPunktSchwierigkeitsGrad() {
        return rotPunktSchwierigkeitsGrad;
    }

    public Integer getIntSprungSchwierigkeitsGrad() {
        return intSprungSchwierigkeitsGrad;
    }

    public Integer getIntAnzahlDerKommentare() {
        return intAnzahlDerKommentare;
    }

    public Float getFltMittlereWegBewertung() {
        return fltMittlereWegBewertung;
    }

    /*
     * Getter for the Comments
     */
    @NotNull
    public Integer getBegehungsStil() {
        if ( intBegehungsStil.getValue() == null ) {
            return 0;
        }
        return intBegehungsStil.getValue();
    }

    @NotNull
    public String getDatumBestiegen() {
        if (long_DateAsscended.getValue() == null || long_DateAsscended.getValue() == 0)
            return "";
        Date resultdate = new Date(long_DateAsscended.getValue());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MMM.yyyy", Locale.GERMANY);
        return sdf.format(resultdate);
    }

    @NotNull
    public Long getLong_DateAsscended() {
        if ( long_DateAsscended.getValue() == null ) {
            return 0L;
        }
        return long_DateAsscended.getValue();
    }

    @NotNull
    public String getStrKommentar() {
        if (strKommentar.getValue() == null)
            return "";
        return strKommentar.getValue();
    }

    /*
     * Setter for the Comments
     */
    public void setBegehungsStil(Integer isBestiegen) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            // On UI thread.
            this.intBegehungsStil.setValue(isBestiegen);
        } else {
            // Not on UI thread.
            this.intBegehungsStil.postValue(isBestiegen);
        }
    }

    public void setDatumBestiegen(Long datumBestiegen) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            // On UI thread.
            this.long_DateAsscended.setValue(datumBestiegen);
        } else {
            // Not on UI thread.
            this.long_DateAsscended.postValue(datumBestiegen);
        }
    }

    public void setStrKommentar(String strKommentar) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            // On UI thread.
            this.strKommentar.setValue(strKommentar);
        } else {
            // Not on UI thread.
            this.strKommentar.postValue(strKommentar);
        }
    }
}
