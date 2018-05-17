package com.teufelsturm.tt_downloaderand_kotlin.TT_DB_Helper;

import java.sql.SQLException;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 
 * @author Martin
 */
public class StoreMySummitComment {

	private SQLiteDatabase newDB;

	public StoreMySummitComment(Context aContext, Integer intTTGipfelNr,
			Boolean isAscendedSummit, Long myLongDateOfAscend,
			String strMySummitComment, Boolean append) throws ClassNotFoundException,
			SQLException {

		Log.i(StoreMySummitComment.class.getSimpleName(), "start doing the update ...: ");
		DataBaseHelper dbHelper = new DataBaseHelper(aContext);
		newDB = dbHelper.getWritableDatabase();
		if (append) {
            String queryString1st = "SELECT a.[isAscendedSummit], a.[intDateOfAscend], a.[strMySummitComment]" +
            		" FROM [myTT_Summit_AND] a\n"
                    + "WHERE a.[intTTGipfelNr] =  " + intTTGipfelNr;
            Log.i(StoreMySummitComment.class.getSimpleName(), "start doing newDB.rawQuery(queryString1st, null);...: ");
            Cursor cursor = newDB.rawQuery(queryString1st, null);
            Log.i(StoreMySummitComment.class.getSimpleName(), "done newDB.rawQuery(queryString1st, null);...: ");
		    if (cursor != null && cursor.moveToFirst() ) {
	            if (  cursor.getInt(cursor
						.getColumnIndex("isAscendedSummit")) == 1){
	                isAscendedSummit = true;
	            }
	            if ( myLongDateOfAscend == null || myLongDateOfAscend < 
	            		cursor.getLong(cursor
	    						.getColumnIndex("intDateOfAscend"))){
	                myLongDateOfAscend = cursor.getLong(cursor
    						.getColumnIndex("intDateOfAscend"));
	            }
	            // IF Current Summit Comment already contains the new comment...
	            if ( cursor.getString(cursor
						.getColumnIndex("strMySummitComment"))
							.contains(strMySummitComment) ){
	                strMySummitComment = cursor.getString(cursor
							.getColumnIndex("strMySummitComment"));
	            }
	            // IF Current Summit Comment is not empty: append new comment...
	            else if (!cursor.getString(cursor
							.getColumnIndex("strMySummitComment")).equals("") ){
	                strMySummitComment = cursor.getString(cursor
							.getColumnIndex("strMySummitComment"))+ "\n" 
	                		+ strMySummitComment;
	            }
            }
        }
		String updateQuery = "INSERT OR REPLACE INTO myTT_Summit_AND "
				+ "(\"_id\", " + "\"_idTimStamp\", " + "\"intTTGipfelNr\", "
				+ "\"isAscendedSummit\", " + "\"intDateOfAscend\", "
				+ "\"strMySummitComment\" )" + " VALUES "
				+ " ( ( SELECT _id FROM myTT_Summit_AND "
				+ "  WHERE intTTGipfelNr = '" + intTTGipfelNr + "'), "
				+ new Date().getTime() + ", " /* _idTimStamp */
				+ intTTGipfelNr + ", " /* intTTGipfelNr */
				+ (isAscendedSummit ? 1 : 0) + ", " /* isAscendedRoute */
				+ myLongDateOfAscend + ", " /* intDateOfAscend */
				+ DatabaseUtils.sqlEscapeString(stripTrailingLineFeed(strMySummitComment)) + ")"; /* strMyRouteComment */

		Log.i(StoreMySummitComment.class.getSimpleName(), "do the update ...: "
				+ updateQuery);
		// do the update
		newDB.execSQL(updateQuery);
		// close all the connections.
		newDB.close();
		dbHelper.close(); 
	}
	
	public static String stripTrailingLineFeed(String inStr) {
		while (inStr.startsWith("\n") ){
			inStr = inStr.substring(1);
		}
		return inStr;
	}
	
}
