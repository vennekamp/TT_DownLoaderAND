package com.teufelsturm.tt_downloaderand_kotlin.searches;

import android.content.Context;
import android.database.SQLException;

import com.teufelsturm.tt_downloaderand_kotlin.dbHelper.DataBaseHelper;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumSachsenSchwierigkeitsGrad;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumTT_WegBewertung;

import java.io.IOException;
import java.util.ArrayList;

public class SearchManager4FragmentSearches

{
    public SearchManager4FragmentSearches(){

    }
    private static SearchManager4FragmentSearches thisInstance = null;


    public static SearchManager4FragmentSearches getInstance() {
        if (thisInstance == null ) {
            thisInstance = new SearchManager4FragmentSearches();
            thisInstance.myAreaPositionFromSpinner = 0;
            thisInstance.intMinAnzahlDerWege = 0;
            thisInstance.intMaxAnzahlDerWege = 100;
            thisInstance.intMinAnzahlDerSternchenWege = 0;
            thisInstance.intMaxAnzahlDerSternchenWege = 50;

            thisInstance.intMinLimitsForDifficultyGrade = EnumSachsenSchwierigkeitsGrad.getMinInteger();
            thisInstance.intMaxLimitsForDifficultyGrade = EnumSachsenSchwierigkeitsGrad.getMaxInteger();
            thisInstance.intMinNumberOfComments = 0;
            thisInstance.intMinOfMeanRating = EnumTT_WegBewertung.getMinInteger();
        }
        return thisInstance;
    }

    /**
     * the position of the item currently selected in the {@link MyPagerFragment}
     */
    private int pagerPageSelected;
    public int getPagerPageSelected() { return pagerPageSelected; }
    public void setPagerPageSelected(int position) { pagerPageSelected = position; }

    /**
     * the position of the {@link FragmentSearchAbstract#mySpinner} (used in all three fragments
     * for the Area.
     */
    private int myAreaPositionFromSpinner;
    public int getMyAreaPositionFromSpinner (){return myAreaPositionFromSpinner;}
    public void setMyAreaFromSpinner (int areaPositionFromSpinner){ myAreaPositionFromSpinner = areaPositionFromSpinner;}

    /**
     * All Areas currently available in the database. Used to supply the name of the currently selected
     * area in the {@link FragmentSearchAbstract#mySpinner}
     */
    private ArrayList<String> allAreaLabels;
    public String getStrtextViewGebiet() { return allAreaLabels.get(myAreaPositionFromSpinner); }
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

    /**
     * the searchtext in the {@link FragmentSearchSummit} for the summit
     */
    private String strTextSuchtext4Summit;
    public String getStrTextSuchtext4Summit() { return strTextSuchtext4Summit; }
    public void setStrTextSuchtext4Summit(String textSuchtext4Summit) {
        strTextSuchtext4Summit = textSuchtext4Summit;
    }
    /**
     * the searchtext in the {@link FragmentSearchRoute} for the route
     */
    private String strTextSuchtext4Route;
    public String getStrTextSuchtext4Route() { return strTextSuchtext4Route; }
    public void setStrTextSuchtext4Route(String textSuchtext4Route) {
        strTextSuchtext4Route = textSuchtext4Route;
    }
    /**
     * the searchtext in the {@link FragmentSearchComment} for the comments
     */
    private String strTextSuchtext4Comment;
    public String getStrTextSuchtext4Comment() { return strTextSuchtext4Comment; }
    public void setStrTextSuchtext4Comment(String textSuchtext4Comment) {
        strTextSuchtext4Comment = textSuchtext4Comment;
    }

    /**
     * the position of the {@link com.crystal.crystalrangeseekbar.widgets.BubbleThumbRangeSeekbar} used
     * in the {@link FragmentSearchRoute} and {@link FragmentSearchComment} for the
     * grade {@link EnumSachsenSchwierigkeitsGrad}.  The position needs to range between
     * {@link EnumSachsenSchwierigkeitsGrad#getMinInteger()} and {@link EnumSachsenSchwierigkeitsGrad#getMaxInteger()} }.
     */
    private int intMinLimitsForDifficultyGrade;

    public int getMinLimitsForDifficultyGrade() { return intMinLimitsForDifficultyGrade; }
    public void setMinLimitsForDifficultyGrade(int minLimitsForDifficultyGrade) {
        if ( minLimitsForDifficultyGrade < EnumSachsenSchwierigkeitsGrad.getMinInteger()
                || minLimitsForDifficultyGrade > EnumSachsenSchwierigkeitsGrad.getMaxInteger()) {
            throw new IllegalArgumentException("Not a valid value: " + minLimitsForDifficultyGrade);
        }
        intMinLimitsForDifficultyGrade = minLimitsForDifficultyGrade;
    }
    /**
     * the position of the {@link com.crystal.crystalrangeseekbar.widgets.BubbleThumbRangeSeekbar} used
     * in the {@link FragmentSearchRoute} and {@link FragmentSearchComment} for the
     * grade {@link EnumSachsenSchwierigkeitsGrad}.  The position needs to range between
     * {@link EnumSachsenSchwierigkeitsGrad#getMinInteger()} and {@link EnumSachsenSchwierigkeitsGrad#getMaxInteger()} }.
     */
    private int intMaxLimitsForDifficultyGrade;
    public int getMaxLimitsForDifficultyGrade() { return intMaxLimitsForDifficultyGrade; }
    public void setMaxLimitsForDifficultyGrade(int maxLimitsForDifficultyGrade) {
        if ( maxLimitsForDifficultyGrade < EnumSachsenSchwierigkeitsGrad.getMinInteger()
                || maxLimitsForDifficultyGrade > EnumSachsenSchwierigkeitsGrad.getMaxInteger()) {
            throw new IllegalArgumentException("Not a valid value: " + maxLimitsForDifficultyGrade);
        }
        intMaxLimitsForDifficultyGrade = maxLimitsForDifficultyGrade;
    }

    /**
     * the minimum number of routes as filter for teh database query.
     */
    private int intMinAnzahlDerWege;
    public int getIntMinAnzahlDerWege() { return intMinAnzahlDerWege; }
    public void setIntMinAnzahlDerWege(Integer minValue) { intMinAnzahlDerWege = minValue;}
    /**
     * the maximum number of routes as filter for teh database query.
     */
    private int intMaxAnzahlDerWege;
    public int getIntMaxAnzahlDerWege() { return intMaxAnzahlDerWege; }
    public void setIntMaxAnzahlDerWege(Integer maxValue) { intMaxAnzahlDerWege = maxValue; }
    /**
     * the minimum number of very good routes as filter for teh database query.
     */
    private int intMinAnzahlDerSternchenWege;
    public int getIntMinAnzahlDerSternchenWege() {
        return intMinAnzahlDerSternchenWege;
    }
    public void setIntMinAnzahlDerSternchenWege(Integer minValue) {
        intMinAnzahlDerSternchenWege = minValue;
    }
    /**
     * the maximum number of very good routes as filter for teh database query.
     */
    private int intMaxAnzahlDerSternchenWege;
    public int getIntMaxAnzahlDerSternchenWege() {
        return intMaxAnzahlDerSternchenWege;
    }

    public void setIntMaxAnzahlDerSternchenWege(Integer maxValue) {
        intMaxAnzahlDerSternchenWege = maxValue;
    }
    /**
     *  The minimum number of comments for a route, used as filter in {@link FragmentSearchRoute}
     */
    private int intMinNumberOfComments;
    public int getIntMinNumberOfComments() { return intMinNumberOfComments; }
    public void setIntMinNumberOfComments(int minNumberOfComments ) {
        intMinNumberOfComments = minNumberOfComments;
    }

    /**
     *  The minimum mean rating of comments for a route, used as filter in {@link FragmentSearchRoute}
     */
    private int intMinOfMeanRating;
    public int getIntMinOfMeanRating() { return intMinOfMeanRating; }
    public void setIntMinOfMeanRating(int minOfMeanRating) {
        intMinOfMeanRating = minOfMeanRating;
    }

    /**
     *  The minimum mean rating for a comment, used as filter in {@link FragmentSearchComment}
     */
    private int intMinGradingOfComment;
    public int getMinGradingOfCommet() {return intMinGradingOfComment; }
    public void setMinGradingOfCommet(int minGradingInComment) {intMinGradingOfComment = minGradingInComment;
    }

}
