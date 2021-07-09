package com.teufelsturm.tt_downloader3.dbHelper;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.teufelsturm.tt_downloader3.R;
import com.teufelsturm.tt_downloader3.TT_DownLoadedApp;
import com.teufelsturm.tt_downloader3.searches.FragmentSearchRoute;
import com.teufelsturm.tt_downloader3.searches.ViewModel4FragmentSearches;

import org.jetbrains.annotations.NotNull;

public class AutoCompleteDbAdapter {

//	private SQLiteDatabase mDb;
	private final Activity mActivity;

	private static final String TAG = AutoCompleteDbAdapter.class.getSimpleName();
	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param activity
	 *            the Activity that is using the database
	 */
	public AutoCompleteDbAdapter(@NotNull Activity activity) {
		this.mActivity = activity;
	}

//	/**
//	 * Closes the database.
//	 */
//	public void close() {
////        SteinFibelApplication.getDataBaseHelper().getWritableDatabase().close();
//	}

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
	public Cursor getAllSummits(ViewModel4FragmentSearches viewModel4FragmentSearches, String constraint) throws SQLException {
		Log.v(TAG, "getAllSummits");
		// Select Query
		String strGebiet =  viewModel4FragmentSearches.getAllAreaLabels()
				.get(viewModel4FragmentSearches.getMyAreaPositionFromSpinner());
		int intMinAnzahlWege = viewModel4FragmentSearches.getIntMinAnzahlDerWege();
		int intMaxAnzahlWege = viewModel4FragmentSearches.getIntMaxAnzahlDerWege();
		int intMinAnzahlSternchenWege = viewModel4FragmentSearches.getIntMinAnzahlDerSternchenWege();
		int intMaxAnzahlSternchenWege = viewModel4FragmentSearches.getIntMaxAnzahlDerSternchenWege();
		// TODO: MOVE to ROOM
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
	public Cursor getAllRoutes(ViewModel4FragmentSearches viewModel4FragmentSearches,
							   FragmentSearchRoute f, String constraint) throws SQLException {
		Log.v(TAG, "getAllRoutes");
		String strGebiet = viewModel4FragmentSearches.getAllAreaLabels()
                .get( viewModel4FragmentSearches.getMyAreaPositionFromSpinner() );
		int intMinSchwierigkeit = viewModel4FragmentSearches.getMinLimitsForDifficultyGrade();
		int intMaxSchwierigkeit = viewModel4FragmentSearches.getMaxLimitsForDifficultyGrade();
		int intAnzahlDerKommentare = viewModel4FragmentSearches.getIntMinNumberOfComments();
		float floatMittlereWegBewertung = viewModel4FragmentSearches.getFloatMinOfMeanRating();
		// Select All Query
		// TODO: MOVE to ROOM
		String queryString = "SELECT a.[_id], a.[WegName] from [TT_Route_AND] a "
				+ "       WHERE a.[intTTGipfelNr] in ("
				+ "       SELECT DISTINCT b.[intTTKletterWeg4Gipfel] from [TT_Route4SummitAND] b"
				+ "       WHERE b.[intTTHauptGipfelNr] in ("
				+ "       	   		SELECT DISTINCT c.[intTTGipfelNr] from [TT_Summit_AND] c"
				+ (strGebiet.equals(mActivity.getString(R.string.strAll)) ? "       where c.[strGebiet] != \"\" "
						: "       where c.[strGebiet] = '" + strGebiet + "'")
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
				+ floatMittlereWegBewertung;
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
		Log.v(TAG, "constraint: " + constraint);
		String params[] = { constraint };
		if (constraint == null) {
			// If no parameters are used in the query,
			// the params arg must be null.
			params = null;
		}
		queryString += " ORDER BY a.[" + queryColumnName + "]";
		Log.v(TAG, "String queryString: " + queryString);

		try {
			Cursor cursor = TT_DownLoadedApp.getDataBaseHelper().getMyDataBase()
                    .rawQuery(queryString, params);
			if ( cursor != null ) {
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