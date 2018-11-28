package com.teufelsturm.tt_downloaderand_kotlin.dbHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.teufelsturm.tt_downloaderand_kotlin.MainActivity;
import com.teufelsturm.tt_downloaderand_kotlin.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

	private static List<String> labels;
	// The Android's default system path of your application database.
	private static String DB_PATH;

	// = "/data/data/com.teufelsturm.tt_downloader/";
	/**
	 * returns the file-Path of the database on the system.
	 * */
	public String getDB_PATH_NAME() {
		return DB_PATH + MainActivity.DB_NAME;
	}

	private SQLiteDatabase myDataBase;

	private Context myContext;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DataBaseHelper(Context context) {
		super(context, MainActivity.DB_NAME, null, 1);
		this.myContext = context;
		DB_PATH = context.getFilesDir().getAbsolutePath()
				.replace("files", "databases")
				+ File.separator;
		Log.v(getClass().getSimpleName(), "Constructor(); wurde aufgerufen!");
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			Log.v(getClass().getSimpleName(), "createDataBase(); database already exist:" + "'"
					+ DB_PATH + MainActivity.DB_NAME + "'");
			// do nothing - database already exist
		} else {
			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are going to be able to overwrite that
			// database with our database.
			this.getReadableDatabase();
			try {
				Log.v(getClass().getSimpleName(), "Rufe copyDataBase(); auf");
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");

			}
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + MainActivity.DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
			Log.v(getClass().getSimpleName(), "checkDB.rawQuery(") ;
			Cursor cursor = checkDB.rawQuery("SELECT name FROM sqlite_master " +
					"WHERE type='table' AND name='TT_Summit_AND';"
					,null);
			Log.v(getClass().getSimpleName(), "cursor.moveToFirst();") ;
			cursor.moveToFirst();
			Log.v(getClass().getSimpleName(), "cursor.getString(0)") ;
			Log.v(getClass().getSimpleName(), cursor.getString(0)) ;
			Log.v(getClass().getSimpleName(), "try beendet") ;

		} catch (SQLiteException e) {

			Log.v(getClass().getSimpleName(), "checkDataBase(); database does't exist yet." + "\r\n'"
					+ DB_PATH + MainActivity.DB_NAME + "'");
			Toast.makeText(myContext, "database does't exist yet: Will be copied from default DB",
					Toast.LENGTH_LONG).show();
			// database does't exist yet.
			return false;
		} catch (android.database.CursorIndexOutOfBoundsException e){

			Log.v(getClass().getSimpleName(), "checkDataBase(); Table does exist,  but seems to be corrupted." + "\r\n'"
					+ DB_PATH + MainActivity.DB_NAME + "'");
			Toast.makeText(myContext, "database does exist,  but seems to be corrupted: Will be overwritten with default DB",
					Toast.LENGTH_LONG).show();
			// database does't exist yet.
			return false;
		}

		if (checkDB != null) {
			checkDB.close();
			Log.v(getClass().getSimpleName(), "checkDataBase(); database does exist already."
					+ "\r\n'" + DB_PATH + MainActivity.DB_NAME
					+ "'");
		}
		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(
				MainActivity.DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + MainActivity.DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
		Log.v(getClass().getSimpleName(), "copyDataBase() aufgerufen.");

	}

	public void openDataBase() throws SQLException {
		if (myDataBase != null) {
			// Open the database
			String myPath = DB_PATH + MainActivity.DB_NAME;
			myDataBase = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		}
	}

	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	// Add your public helper methods to access and get content from the
	// database.
	// You could return cursors by doing "return myDataBase.query(....)" so it'd
	// be easy
	// to you to create adapters for your views.

	/**
	 * Getting all Areas returns list of Climbing Areas
	 * */
	public List<String> getAllAreas() {
		if (labels == null) {
			labels = new ArrayList<String>();
			// Select All Query
			String queryString;
			queryString = "SELECT DISTINCT a.[strGebiet] from [TT_Summit_AND] a "
					+ " where a.[strGebiet] != \"\" "
					+ " ORDER BY a.[strGebiet]";

			SQLiteDatabase myDb = this.getReadableDatabase();
			Cursor cursor = myDb.rawQuery(queryString, null);

			// looping through all rows and adding to list
			labels.add(myContext.getString(R.string.strAll));
			if (cursor.moveToFirst()) {
				do {
					labels.add(cursor.getString(0));
				} while (cursor.moveToNext());
			}

			// closing connection
			cursor.close();
			cursor = null;
			myDb.close();
		}
		// Spinner Drop down elements: labels
		return labels;
	}
}
