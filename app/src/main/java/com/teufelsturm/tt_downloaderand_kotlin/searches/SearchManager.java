package com.teufelsturm.tt_downloaderand_kotlin.searches;

import android.content.Context;
import android.database.SQLException;

import com.teufelsturm.tt_downloaderand_kotlin.dbHelper.DataBaseHelper;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumSachsenSchwierigkeitsGrad;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumTT_WegBewertung;

import java.io.IOException;
import java.util.ArrayList;

public class SearchManager

{
    public SearchManager(){

    }
    private static SearchManager thisInstance = new SearchManager();

//    private DataBaseHelper myDbHelper;
    private ArrayList<String> allAreaLabels;
    private int myAreaPositionFromSpinner;
    private int intMinAnzahlDerWege;
    private int intMaxAnzahlDerWege;
    private int intMinAnzahlDerSternchenWege;
    private int intMaxAnzahlDerSternchenWege;



    private int intMinLimitsForScale;
    private int intMaxLimitsForScale;
    private int intMinNumberOfComments = 0;
    private int intMinOfMeanRating = EnumTT_WegBewertung.getMinInteger();

    public static SearchManager getInstance() {
        if (thisInstance == null ) {
            thisInstance = new SearchManager();
            thisInstance.myAreaPositionFromSpinner = 0;
            thisInstance.intMinAnzahlDerWege = 0;
            thisInstance.intMaxAnzahlDerWege = 100;
            thisInstance.intMinAnzahlDerSternchenWege = 0;
            thisInstance.intMaxAnzahlDerSternchenWege = 50;

            thisInstance.intMinLimitsForScale = EnumSachsenSchwierigkeitsGrad.getMinInteger();
            thisInstance.intMaxLimitsForScale = EnumSachsenSchwierigkeitsGrad.getMaxInteger();
        }
        return thisInstance;
    }


    public int getMyAreaPositionFromSpinner (){return myAreaPositionFromSpinner;}
    public void setMyAreaFromSpinner (int areaPositionFromSpinner){ myAreaPositionFromSpinner = areaPositionFromSpinner;}
    public int getIntMinAnzahlDerWege() {
        return intMinAnzahlDerWege;
    }
    public int getIntMaxAnzahlDerWege() {
        return intMaxAnzahlDerWege;
    }
    public void setIntMinAnzahlDerWege(Integer minValue) {
        intMinAnzahlDerWege = minValue;
    }
    public void setIntMaxAnzahlDerWege(Integer maxValue) {
        intMaxAnzahlDerWege = maxValue;
    }
    public int getIntMinAnzahlDerSternchenWege() {
        return intMinAnzahlDerSternchenWege;
    }
    public int getIntMaxAnzahlDerSternchenWege() {
        return intMaxAnzahlDerSternchenWege;
    }
    public int getEnumMinLimitsForScale() {
        return EnumSachsenSchwierigkeitsGrad.values()[intMinLimitsForScale].getValue();
    }
    public int getEnumMaxLimitsForScale() {
        return EnumSachsenSchwierigkeitsGrad.values()[intMaxLimitsForScale].getValue();
    }
    public void setIntMinAnzahlDerSternchenWege(Integer minValue) {
        intMinAnzahlDerSternchenWege = minValue;
    }

    public void setIntMaxAnzahlDerSternchenWege(Integer maxValue) {
        intMaxAnzahlDerSternchenWege = maxValue;
    }
    public int getIntMinNumberOfComments() {
        return intMinNumberOfComments;
    }
    public int getIntMinOfMeanRating() {
        return intMinOfMeanRating;
    }
    public void setIntMinNumberOfComments(int minNumberOfComments ) {
        intMinNumberOfComments = minNumberOfComments;
    }
    public void setIntMinOfMeanRating(int minOfMeanRating) {
        intMinOfMeanRating = minOfMeanRating;
    }

    public ArrayList<String> getAllAreaLabels(Context context) {
        if ( allAreaLabels == null ) {
            // database handler
            DataBaseHelper myDbHelper = new DataBaseHelper(context);
            try {
                myDbHelper.createDataBase();
            } catch (IOException ioe) {
                throw new Error("Unable to create database");
            }
            try {
                myDbHelper.openDataBase();
            } catch (SQLException sqle) {
                throw sqle;
            }
            allAreaLabels = myDbHelper.getAllAreas();
            myDbHelper.close();
        }
        return allAreaLabels;
    }
}
