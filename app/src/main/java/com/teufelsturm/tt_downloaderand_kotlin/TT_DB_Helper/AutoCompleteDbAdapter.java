package com.teufelsturm.tt_downloaderand_kotlin.TT_DB_Helper;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.teufelsturm.tt_downloaderand_kotlin.MainActivitySearchRoute;
import com.teufelsturm.tt_downloaderand_kotlin.MainActivitySearchSummit;
import com.teufelsturm.tt_downloaderand_kotlin.R;

public class AutoCompleteDbAdapter {

	private DataBaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private final Activity mActivity;

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param activity
	 *            the Activity that is using the database
	 */
	public AutoCompleteDbAdapter(Activity activity) {
		this.mActivity = activity;
		this.mDbHelper = new DataBaseHelper(activity.getApplicationContext());
		this.mDb = mDbHelper.getWritableDatabase();
	}

	/**
	 * Closes the database.
	 */
	public void close() {
		mDbHelper.close();
	}

	/**
	 * Return a Cursor that returns all list of Climbing summit where the summit
	 * name begins with the given constraint string.
	 * 
	 * @param constraint
	 *            Specifies the first letters of the states to be listed. If
	 *            null, all rows are returned.
	 * @return Cursor managed and positioned to the first state, if found
	 * @throws SQLException
	 *             if query fails
	 */
	public Cursor getAllSummits(String constraint) throws SQLException {
		Log.v(getClass().getSimpleName(), "getAllSummits");
		// Select Query
		String strGebiet = MainActivitySearchSummit.getStrtextViewGebiet();
		int intMinAnzahlWege = MainActivitySearchSummit
				.getIntMinAnzahlDerWege();
		int intMaxAnzahlWege = MainActivitySearchSummit
				.getIntMaxAnzahlDerWege();
		int intMinAnzahlSternchenWege = MainActivitySearchSummit
				.getIntMinAnzahlDerSternchenWege();
		int intMaxAnzahlSternchenWege = MainActivitySearchSummit
				.getIntMaxAnzahlDerSternchenWege();
		String queryString = "SELECT a.[_id], a.[strName] FROM [TT_Summit_AND] a"
				+ (strGebiet.equals(mActivity.getString(R.string.strAll)) ? "       WHERE a.[strGebiet] != \"\" "
						: "       WHERE a.[strGebiet] = '" + strGebiet + "'")
				+ "\r\n		AND a.[intAnzahlWege] >= "
				+ intMinAnzahlWege
				+ "\r\n		AND a.[intAnzahlWege] <= "
				+ intMaxAnzahlWege
				+ "\r\n		AND a.[intAnzahlSternchenWege] >= "
				+ intMinAnzahlSternchenWege
				+ "\r\n		AND a.[intAnzahlSternchenWege] <= "
				+ intMaxAnzahlSternchenWege;
		return getAllXYZ(constraint, queryString, "\r\n AND ", "strName");
	}

	/**
	 * Getting all Areas returns list of Climbing Route Names
	 * */
	public Cursor getAllRoutes(String constraint) throws SQLException {
		Log.v(getClass().getSimpleName(), "getAllRoutes");
		String strGebiet = MainActivitySearchRoute.getStrtextViewGebiet();
		int intMinSchwierigkeit = MainActivitySearchRoute
				.getEnumMinLimitsForScale();
		int intMaxSchwierigkeit = MainActivitySearchRoute
				.getEnumMaxLimitsForScale();
		int intAnzahlDerKommentare = MainActivitySearchRoute
				.getIntMinNumberOfComments();
		int intMittlereWegBewertung = MainActivitySearchRoute
				.getIntMinOfMeanRating();
		// Select All Query
		String queryString = "SELECT a.[_id], a.[WegName] from [TT_Route_AND] a "
				+ "       WHERE a.[intTTGipfelNr] in ("
				+ "       SELECT DISTINCT b.[intTTKletterWeg4Gipfel] from [TT_Route4SummitAND] b"
				+ "       WHERE b.[intTTHauptGipfelNr] in ("
				+ "       	   		SELECT DISTINCT c.[intTTGipfelNr] from [TT_Summit_AND] c"
				+ (strGebiet.equals(mActivity.getString(R.string.strAll)) ? "       where c.[strGebiet] != \"\" "
						: "       where c.[strGebiet] = " + strGebiet)
				+ "                 )"
				+ "           )"
				+ "       AND  coalesce(a.[sachsenSchwierigkeitsGrad], a.[ohneUnterstützungSchwierigkeitsGrad], a.[rotPunktSchwierigkeitsGrad]"
				+ "              , a.[intSprungSchwierigkeitsGrad] ) BETWEEN "
				+ intMinSchwierigkeit
				+ " AND "
				+ intMaxSchwierigkeit
				+ "       AND a.[intAnzahlDerKommentare] >= "
				+ intAnzahlDerKommentare
				+ "       AND a.[fltMittlereWegBewertung] >= "
				+ intMittlereWegBewertung;
		return getAllXYZ(constraint, queryString, "\r\n AND ", "WegName");
	}

	public Cursor getAllComments(String constraint) throws SQLException {
		String queryString = "SELECT a.[_id], a.[strAutoCompleteText] from [AutoCompleteComment] a";
		return getAllXYZ(constraint, queryString, " WHERE ", "strAutoCompleteText");
	}

	private Cursor getAllXYZ(String constraint, String queryString,
			String queryConcater, String queryColumnName) {

		if (constraint != null) {
			// Query for any rows where the state name begins with the
			// string specified in constraint.
			//
			// NOTE:
			// If wildcards are to be used in a rawQuery, they must appear
			// in the query parameters, and not in the query string proper.
			// See http://code.google.com/p/android/issues/detail?id=3153
			constraint = "%" + constraint.trim() + "%";
			// if (queryString.contains(new char[]{ 'W','H','E','R','E'}) )
			queryString += queryConcater + queryColumnName + " LIKE ?";
		}
		Log.v(getClass().getSimpleName(), "constraint: " + constraint);
		String params[] = { constraint };
		if (constraint == null) {
			// If no parameters are used in the query,
			// the params arg must be null.
			params = null;
		}
		queryString += " ORDER BY a.[" + queryColumnName + "]";
		Log.v(getClass().getSimpleName(), "String queryString: " + queryString);

		try {
			Cursor cursor = mDb.rawQuery(queryString, params);
			if (cursor != null) {
				this.mActivity.startManagingCursor(cursor);
				cursor.moveToFirst();
				return cursor;
			}
		} catch (SQLException e) {
			Log.e("AutoCompleteDbAdapter", e.toString());
			throw e;
		}
		return null;
	}

}