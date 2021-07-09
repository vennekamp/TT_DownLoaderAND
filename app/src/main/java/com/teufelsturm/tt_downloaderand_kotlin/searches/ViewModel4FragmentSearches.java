package com.teufelsturm.tt_downloaderand_kotlin.searches;

import com.teufelsturm.tt_downloader3.TT_DownLoadedApp;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumSachsenSchwierigkeitsGrad;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumTT_WegBewertung;

import java.util.ArrayList;

import androidx.lifecycle.ViewModel;

public class ViewModel4FragmentSearches extends ViewModel {
    /**
     * the position of the item currently selected in the {@link MyPagerFragment}
     */
    private int pagerPageSelected = 0;
    int getPagerPageSelected() { return pagerPageSelected; }
    void setPagerPageSelected(int position) { pagerPageSelected = position; }

    /**
     * the position of the {@link FragmentSearchAbstract mySpinner} (used in all three fragments
     * for the Area.
     */
    private int myAreaPositionFromSpinner = 0;
    public int getMyAreaPositionFromSpinner (){return myAreaPositionFromSpinner;}
    public void setMyAreaFromSpinner (int areaPositionFromSpinner){ myAreaPositionFromSpinner = areaPositionFromSpinner;}

    /**
     * All Areas currently available in the database. Used to supply the name of the currently selected
     * area in the {@link FragmentSearchAbstract mySpinner}
     */
    private ArrayList<String> allAreaLabels;
    String getStrtextViewGebiet() { return getAllAreaLabels().get(myAreaPositionFromSpinner); }
    public ArrayList<String> getAllAreaLabels() {
        if ( allAreaLabels == null ) {
            allAreaLabels = TT_DownLoadedApp.getDataBaseHelper().getAllAreas();
        }
        return allAreaLabels;
    }

    /**
     * the searchtext in the {@link FragmentSearchSummit} for the summit
     */
    private String strTextSuchtext4Summit = "";
    String getStrTextSuchtext4Summit() { return strTextSuchtext4Summit; }
    void setStrTextSuchtext4Summit(String textSuchtext4Summit) {
        strTextSuchtext4Summit = textSuchtext4Summit;
    }
    /**
     * the searchtext in the {@link FragmentSearchRoute} for the route
     */
    private String strTextSuchtext4Route = "";
    String getStrTextSuchtext4Route() { return strTextSuchtext4Route; }
    void setStrTextSuchtext4Route(String textSuchtext4Route) {
        strTextSuchtext4Route = textSuchtext4Route;
    }
    /**
     * the searchtext in the {@link FragmentSearchComment} for the comments
     */
    private String strTextSuchtext4Comment = "";
    String getStrTextSuchtext4Comment() { return strTextSuchtext4Comment; }
    void setStrTextSuchtext4Comment(String textSuchtext4Comment) {
        strTextSuchtext4Comment = textSuchtext4Comment;
    }

    /**
     * the position of the {@link com.crystal.crystalrangeseekbar.widgets.BubbleThumbRangeSeekbar} used
     * in the {@link FragmentSearchRoute} and {@link FragmentSearchComment} for the
     * grade {@link EnumSachsenSchwierigkeitsGrad}.  The position needs to range between
     * {@link EnumSachsenSchwierigkeitsGrad#getMinInteger()} and {@link EnumSachsenSchwierigkeitsGrad#getMaxInteger()} }.
     */
    private int intMinLimitsForDifficultyGrade = EnumSachsenSchwierigkeitsGrad.getMinInteger();
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
    private int intMaxLimitsForDifficultyGrade = EnumSachsenSchwierigkeitsGrad.getMaxInteger();
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
    private int intMinAnzahlDerWege = 0;
    public int getIntMinAnzahlDerWege() { return intMinAnzahlDerWege; }
    void setIntMinAnzahlDerWege(Integer minValue) { intMinAnzahlDerWege = minValue;}
    /**
     * the maximum number of routes as filter for teh database query.
     */
    private int intMaxAnzahlDerWege = 250;
    public int getIntMaxAnzahlDerWege() { return intMaxAnzahlDerWege; }
    void setIntMaxAnzahlDerWege(Integer maxValue) { intMaxAnzahlDerWege = maxValue; }
    /**
     * the minimum number of very good routes as filter for teh database query.
     */
    private int intMinAnzahlDerSternchenWege = 0;
    public int getIntMinAnzahlDerSternchenWege() {
        return intMinAnzahlDerSternchenWege;
    }
    void setIntMinAnzahlDerSternchenWege(Integer minValue) {
        intMinAnzahlDerSternchenWege = minValue;
    }
    /**
     * the maximum number of very good routes as filter for teh database query.
     */
    private int intMaxAnzahlDerSternchenWege = 100;
    public int getIntMaxAnzahlDerSternchenWege() {
        return intMaxAnzahlDerSternchenWege;
    }

    void setIntMaxAnzahlDerSternchenWege(Integer maxValue) {
        intMaxAnzahlDerSternchenWege = maxValue;
    }
    /**
     *  The minimum number of comments for a route, used as filter in {@link FragmentSearchRoute}
     */
    private int intMinNumberOfComments = 0;
    public int getIntMinNumberOfComments() { return intMinNumberOfComments; }
    void setIntMinNumberOfComments(int minNumberOfComments ) {
        intMinNumberOfComments = minNumberOfComments;
    }

    /**
     *  The minimum mean rating of comments for a route, used as filter in {@link FragmentSearchRoute}
     */
    private float intMinOfMeanRating = EnumTT_WegBewertung.getMinInteger();
    public float getFloatMinOfMeanRating() { return intMinOfMeanRating; }
    void setFloatMinOfMeanRating(int minOfMeanRating) {
        intMinOfMeanRating = minOfMeanRating;
    }

    /**
     *  The minimum mean rating for a comment, used as filter in {@link FragmentSearchComment}
     */
    private int intMinGradingOfComment = 0;
    int getMinGradingOfCommet() {return intMinGradingOfComment; }
    void setMinGradingOfCommet(int minGradingInComment) {intMinGradingOfComment = minGradingInComment;
    }

}
