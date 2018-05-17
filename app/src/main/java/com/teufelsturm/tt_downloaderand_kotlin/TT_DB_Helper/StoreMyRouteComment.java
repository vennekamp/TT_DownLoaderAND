package com.teufelsturm.tt_downloaderand_kotlin.TT_DB_Helper;

import java.sql.SQLException;
import java.util.Date;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 
 * @author Martin
 */
public class StoreMyRouteComment {

	private SQLiteDatabase newDB;

	public StoreMyRouteComment(Context aContext, Integer intTTGipfelNr,
			Integer typeRouteAscend, Long long_DateAsscended,
			String strMyRouteComment) throws ClassNotFoundException,
			SQLException {

		Log.i(StoreMySummitComment.class.getSimpleName(), "start doing the update ...: ");
		DataBaseHelper dbHelper = new DataBaseHelper(aContext);
		newDB = dbHelper.getWritableDatabase();
		String updateQuery = "INSERT OR REPLACE INTO myTT_Route_AND "
				+ "(\"_id\", " + "\"_idTimStamp\", " + "\"intTTWegNr\", "
				+ "\"isAscendedRoute\", " + "\"intDateOfAscend\", "
				+ "\"strMyRouteComment\" )" + " VALUES "
				+ " ( ( SELECT _id FROM myTT_Route_AND "
				+ "  WHERE intTTWegNr = '" + intTTGipfelNr + "'), "
				+ new Date().getTime() + ", " /* _idTimStamp */
				+ intTTGipfelNr + ", " /* intTTGipfelNr */
				+ typeRouteAscend + ", " /* isAscendedRoute */
				+ long_DateAsscended + ", " /* intDateOfAscend */
				+ DatabaseUtils.sqlEscapeString(StoreMySummitComment.stripTrailingLineFeed(strMyRouteComment)) + ")"; /* strMyRouteComment */
		
		Log.i(StoreMySummitComment.class.getSimpleName(), "do the update ...: "
				+ updateQuery);
		// do the update
		newDB.execSQL(updateQuery);
		// close all the connections.
		newDB.close();
		dbHelper.close();
	}
}
