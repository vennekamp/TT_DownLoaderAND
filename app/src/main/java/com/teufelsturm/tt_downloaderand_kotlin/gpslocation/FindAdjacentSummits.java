package com.teufelsturm.tt_downloaderand_kotlin.gpslocation;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.teufelsturm.tt_downloaderand_kotlin.TT_DB_Helper.DataBaseHelper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class FindAdjacentSummits {
//	private static Integer maxDistanceFromHere; 
//	private static Integer maxNeighbourFromHere;
	private static List<SummitLocation> allSummitLocations;
	private static DataBaseHelper myDbHelper;
	private static long lastUpDate = 0;
//	/** Gives the radius within which the adjacent summit are collected to the ArrayList in meter */
//	public static Integer getMaxDistanceFromHere() {
//		return maxDistanceFromHere;
//	}
//	/** Sets the radius (in meter) within which the adjacent summit are collected to the ArrayList */
//	public static void setMaxDistanceFromHere(Integer maxDistanceFromHere) {
//		FindAdjacentSummits.maxDistanceFromHere = maxDistanceFromHere;
//	}
//	/** Gives the number of the adjacent summit which are collected to the ArrayList (count) */
//	public static Integer getMaxNeighbourFromHere() {
//		return maxNeighbourFromHere;
//	}
//	/** Sets the number of the adjacent summit which are collected to the ArrayList (count) */
//	public static void setMaxNeighbourFromHere(Integer maxNeighbourFromHere) {
//		FindAdjacentSummits.maxNeighbourFromHere = maxNeighbourFromHere;
//	}
	/** Returns the ArrayList with the Objects containing the summit located closest to the current position */
	public static List<SummitLocation> getAllSummitLocations() {
		return allSummitLocations;
	}
	
	/**
	 * Function to load the summit location data from SQLite database
	 * */
	protected static void loadSummitLocationData(Context context) {
		Log.v(FindAdjacentSummits.class.getSimpleName(), 
				"loadSummitLocationData in FindAdjacentSummits errreicht...");
		// database handler
		myDbHelper = new DataBaseHelper(context);
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
		
		allSummitLocations = myDbHelper.getSummitLocationData();
		myDbHelper.close();
	}

	public static void updateData(){
//		Log.v(FindAdjacentSummits.class.getSimpleName(), "Noch da (1): updateData() in FindAdjacentSummits");
		for ( SummitLocation aSummitLocation : allSummitLocations) {
			aSummitLocation.updateData();
		}
		if ( ( System.currentTimeMillis() - lastUpDate  ) > 1000  ) {
			lastUpDate = System.currentTimeMillis();
			// Sort the arrayList
			Collections.sort(allSummitLocations);
			}
		}
}
