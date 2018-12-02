package com.teufelsturm.tt_downloaderand_kotlin.dbHelper;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.teufelsturm.tt_downloaderand_kotlin.searches.FragmentSearchRoute;
import com.teufelsturm.tt_downloaderand_kotlin.searches.FragmentSearchSummit;
import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.searches.SearchManager;

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
	public Cursor getAllSummits(Context context, SearchManager searchManager, String constraint) throws SQLException {
		Log.v(getClass().getSimpleName(), "getAllSummits");
		// Select Query
		String strGebiet =  searchManager.getAllAreaLabels(context)
				.get(searchManager.getMyAreaPositionFromSpinner());
		int intMinAnzahlWege = searchManager.getIntMinAnzahlDerWege();
		int intMaxAnzahlWege = searchManager.getIntMaxAnzahlDerWege();
		int intMinAnzahlSternchenWege = searchManager.getIntMinAnzahlDerSternchenWege();
		int intMaxAnzahlSternchenWege = searchManager.getIntMaxAnzahlDerSternchenWege();
		String queryString = new StringBuilder()
                .append("SELECT a.[_id], a.[strName] FROM [TT_Summit_AND] a")
                .append(mActivity.getString(R.string.strAll).equals(strGebiet)
                        ? "       WHERE a.[strGebiet] != \"\" "
                        : "       WHERE a.[strGebiet] = '" + strGebiet + "'")
                .append("\r\n		AND a.[intAnzahlWege] >= ")
                .append(intMinAnzahlWege)
                .append("\r\n		AND a.[intAnzahlWege] <= ")
                .append(intMaxAnzahlWege)
                .append("\r\n		AND a.[intAnzahlSternchenWege] >= ")
                .append(intMinAnzahlSternchenWege)
                .append("\r\n		AND a.[intAnzahlSternchenWege] <= ")
                .append(intMaxAnzahlSternchenWege).toString();
		return getAllXYZ(constraint, queryString, "\r\n AND ", "strName");
	}

	/**
	 * Getting all Areas returns list of Climbing Route Names
	 * */
	public Cursor getAllRoutes(SearchManager searchManager,
							   FragmentSearchRoute f, String constraint) throws SQLException {
		Log.v(getClass().getSimpleName(), "getAllRoutes");
		String strGebiet = searchManager.getAllAreaLabels(f.getContext())
                .get( searchManager.getMyAreaPositionFromSpinner() );
		int intMinSchwierigkeit = searchManager.getEnumMinLimitsForScale();
		int intMaxSchwierigkeit = searchManager.getEnumMaxLimitsForScale();
		int intAnzahlDerKommentare = searchManager.getIntMinNumberOfComments();
		int intMittlereWegBewertung = searchManager.getIntMinOfMeanRating();
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