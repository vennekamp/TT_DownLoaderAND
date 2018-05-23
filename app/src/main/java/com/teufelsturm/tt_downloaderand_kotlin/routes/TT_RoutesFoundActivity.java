package com.teufelsturm.tt_downloaderand_kotlin.routes;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.teufelsturm.tt_downloaderand_kotlin.MainActivitySearchRoute;
import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.TT_DB_Helper.DataBaseHelper;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.TT_Route_AND;

import java.util.ArrayList;
import java.util.List;

public class TT_RoutesFoundActivity extends Activity {

	private final static String TAG = TT_RoutesFoundActivity.class.getSimpleName();

	private List<TT_Route_AND> lstTT_Routes_AND;
	private SQLiteDatabase newDB;
	private ListView meinListView;
	private TT_Route_ANDAdapter listenAdapter;
	private static TT_RoutesFoundActivity thisTT_RoutesFoundActivity;
	private static Boolean dataHasChanged = Boolean.FALSE;

	@Override
	protected void onResume() {
		super.onResume();
		thisTT_RoutesFoundActivity = this;
		if (dataHasChanged) {
			this.openAndQueryDatabase();
			listenAdapter.notifyDataSetChanged();
			dataHasChanged = Boolean.FALSE;
		}
	}

	@Override
	protected void onPause() {
		thisTT_RoutesFoundActivity = null;
		super.onPause();
	}
	public static void hasChangedData() {
		Log.i(TAG,"thisTT_RoutesFoundActivity != null " + (thisTT_RoutesFoundActivity != null));
		if (thisTT_RoutesFoundActivity != null) {
			thisTT_RoutesFoundActivity.openAndQueryDatabase();
			thisTT_RoutesFoundActivity.listenAdapter.notifyDataSetChanged();
			dataHasChanged = Boolean.FALSE;
		} else {
			dataHasChanged = Boolean.TRUE;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "Neuer onCreate... ");
		setContentView(R.layout.routes_activity_found_lv_list);
		lstTT_Routes_AND = new ArrayList<TT_Route_AND>();
		openAndQueryDatabase();
		Log.i(TAG,
				"Neuer openAndQueryDatabase... BEENDET!");

		listenAdapter = new TT_Route_ANDAdapter(this, lstTT_Routes_AND, true);
		Log.i(TAG, "Suche meinListView... ");
		meinListView = (ListView) findViewById(R.id.list_summits);
		Log.i(TAG, "meinListView.setAdapter...");
		// http://www.androiddesignpatterns.com/2012/07/understanding-loadermanager.html
		meinListView.setAdapter(listenAdapter);
		// Event Listener
		meinListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.e(TAG, "onItemClick in TT_RoutesFoundActivity"
							+ view.getTag().toString() );
				Log.i(TAG,
						"Intent addonPageSummitFoundActivity = new Intent(...");
				Intent addonPageRouteFoundActivity = new Intent(
						TT_RoutesFoundActivity.this,
						TT_RouteFoundActivity.class);
				Log.i(TAG,
						"addonPageSummitFoundActivity.putExtra(...");
				addonPageRouteFoundActivity.putExtra("TT_Route_AND",
						lstTT_Routes_AND.get(position) );
				Log.i(TAG, "startActivity... ");
				startActivity(addonPageRouteFoundActivity);
			}
		});

		// http://android-er.blogspot.de/2011/11/detect-swipe-using-simpleongestureliste.html
		// http://android-er.blogspot.de/2012/05/simple-example-use-osmdroid-and-slf4j.html

		Log.i(TAG, "Neuer onCreate... BEENDET");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.summits_found, menu);
		return true;
	}

	private void openAndQueryDatabase() {
		Log.i(TAG, "Neuer openAndQueryDatabase... ");
		lstTT_Routes_AND.clear();
		try {
			DataBaseHelper dbHelper = new DataBaseHelper(
					this.getApplicationContext());
			newDB = dbHelper.getWritableDatabase();
			Log.i(TAG, "queryString erzeugen... ");
			String strTextSuchtext = MainActivitySearchRoute
					.getStrTextSuchtext();
			String strGebiet = MainActivitySearchRoute.getStrtextViewGebiet();
			Log.e(TAG, "strGebiet gesucht... " + strGebiet );
			int intMinSchwierigkeit = MainActivitySearchRoute
					.getEnumMinLimitsForScale();
			int intMaxSchwierigkeit = MainActivitySearchRoute
					.getEnumMaxLimitsForScale();
			int intMinAnzahlDerKommentare = MainActivitySearchRoute
					.getIntMinNumberOfComments();
			int intMittlereWegBewertung = MainActivitySearchRoute
					.getIntMinOfMeanRating();
			//// Select All Query
//			 SELECT a.[intTTWegNr], a.[intTTGipfelNr]
//			 , a.[WegName], c.[strName], a.[blnAusrufeZeichen]
//			 , a.[intSterne], a.[strSchwierigkeitsGrad],
//			 a.[intSprungSchwierigkeitsGrad]
//			 , a.[sachsenSchwierigkeitsGrad]
//			 , a.[ohneUnterstützungSchwierigkeitsGrad]
//			 , a.[rotPunktSchwierigkeitsGrad]
//			 , a.[intSprungSchwierigkeitsGrad]
//			 , a. [intAnzahlDerKommentare], a. [fltMittlereWegBewertung]
//			 , d.[isAscendedRoute], d.[intDateOfAscend], d.[strMyRouteComment]
//			 FROM [TT_Route_AND] a, [TT_Summit_AND] c
//			 LEFT OUTER JOIN [myTT_Route_AND] d
//			 ON a.[intTTWegNr] = d.[intTTWegNr]
//			 WHERE a.[intAnzahlDerKommentare] >= 1
//			 AND a.[fltMittlereWegBewertung] >= -3
//			 AND a.[WegName] LIKE '%AW direkt%'
//			 AND a.[intTTGipfelNr] in (
//			 SELECT DISTINCT b.[intTTKletterWeg4Gipfel] from
//			 [TT_Route4SummitAND] b
//			 WHERE b.[intTTHauptGipfelNr] in (
//			 SELECT DISTINCT c.[intTTGipfelNr] from [TT_Summit_AND] c
//			 where c.[strGebiet] != ""
//			 )
//			 )
//			 AND a.[intTTGipfelNr] = c.[intTTGipfelNr]
//			 AND coalesce(a.[sachsenSchwierigkeitsGrad],
//			 a.[ohneUnterstützungSchwierigkeitsGrad],
//			 a.[rotPunktSchwierigkeitsGrad]
//			 , a.[intSprungSchwierigkeitsGrad] ) BETWEEN 1 AND 15
			String queryString = "SELECT a.[intTTWegNr], a.[intTTGipfelNr]\r\n"
					+ "       , a.[WegName], c.[strName], a.[blnAusrufeZeichen]\r\n"
					+ "       , a.[intSterne], a.[strSchwierigkeitsGrad], a.[intSprungSchwierigkeitsGrad]\r\n"
					+ "       , a.[sachsenSchwierigkeitsGrad]\r\n"
					+ "       , a.[ohneUnterstützungSchwierigkeitsGrad]\r\n"
					+ "       , a.[rotPunktSchwierigkeitsGrad]\r\n"
					+ "       , a.[intSprungSchwierigkeitsGrad]\r\n"
					+ "       , a. [intAnzahlDerKommentare], a. [fltMittlereWegBewertung]\r\n"
					+ "       , d.[isAscendedRoute], d.[intDateOfAscend], d.[strMyRouteComment]\r\n"
					+ "       FROM [TT_Route_AND] a, [TT_Summit_AND] c\r\n"
					+ "       LEFT OUTER JOIN [myTT_Route_AND] d\r\n"
					+ "            ON a.[intTTWegNr] = d.[intTTWegNr]\r\n"
					+ "       WHERE a.[intAnzahlDerKommentare] >= "
					+ intMinAnzahlDerKommentare
					+ "\r\n       AND a.[fltMittlereWegBewertung] >= "
					+ intMittlereWegBewertung
					+ "\r\n       AND a.[WegName] LIKE "
					+ DatabaseUtils.sqlEscapeString("%" + strTextSuchtext + "%") 
					+ (strGebiet.equals(this.getString(R.string.strAll)) ? "       AND c.[strGebiet] != \"\" "
							: "       AND c.[strGebiet] = '" + strGebiet + "'")
					+ "\r\n		  AND a.[intTTGipfelNr] in (\r\n"
					+ "       SELECT DISTINCT b.[intTTKletterWeg4Gipfel] from [TT_Route4SummitAND] b\r\n"
					+ "       WHERE b.[intTTHauptGipfelNr] in (\r\n"
					+ "       	   		SELECT DISTINCT c.[intTTGipfelNr] from [TT_Summit_AND] c)\r\n"
					+ "           )\r\n"
					+ "\r\n 	  AND a.[intTTGipfelNr] = c.[intTTGipfelNr]"
					+ "\r\n       AND  coalesce(a.[sachsenSchwierigkeitsGrad], a.[ohneUnterstützungSchwierigkeitsGrad], a.[rotPunktSchwierigkeitsGrad]"
					+ "              , a.[intSprungSchwierigkeitsGrad] ) BETWEEN "
					+ intMinSchwierigkeit + " AND "
					+ intMaxSchwierigkeit
					+ " ORDER BY WegName"
					+ " Limit " + getResources().getInteger(R.integer.MaxNoItemQuerxy);
			Log.i(TAG, "Neue Query erzeugt..."
					+ queryString);

			Cursor cursor = null;
			cursor = newDB.rawQuery(queryString, null);
			Log.i(TAG,
					"Neue Routen gesucht... 'c != null'" + (cursor != null));

			if (cursor != null) {
				if (cursor.moveToFirst()) {
					do {
						Integer intTTWegNr = cursor.getInt(cursor
								.getColumnIndex("intTTWegNr"));
						Integer intGipfelNr = cursor.getInt(cursor
								.getColumnIndex("intTTGipfelNr"));
						String WegName = cursor.getString(cursor
								.getColumnIndex("WegName"));
						String strGipfelName = cursor.getString(cursor
								.getColumnIndex("strName"));
						Boolean blnAusrufeZeichen = cursor.getInt(cursor
								.getColumnIndex("blnAusrufeZeichen")) > 0;
						Integer intSterne = cursor.getInt(cursor
								.getColumnIndex("intSterne"));
						String strSchwierigkeitsGrad = cursor.getString(cursor
								.getColumnIndex("strSchwierigkeitsGrad"));
						Integer intSprungSchwierigkeitsGrad = cursor
								.getInt(cursor
										.getColumnIndex("intSprungSchwierigkeitsGrad"));
						Integer sachsenSchwierigkeitsGrad = cursor
								.getInt(cursor
										.getColumnIndex("sachsenSchwierigkeitsGrad"));
						Integer ohneUnterstützungSchwierigkeitsGrad = cursor
								.getInt(cursor
										.getColumnIndex("ohneUnterstützungSchwierigkeitsGrad"));
						Integer rotPunktSchwierigkeitsGrad = cursor
								.getInt(cursor
										.getColumnIndex("rotPunktSchwierigkeitsGrad"));
						Integer intAnzahlDerKommentare = cursor.getInt(cursor
								.getColumnIndex("intAnzahlDerKommentare"));
						Float fltMittlereWegBewertung = cursor
								.getFloat(cursor
										.getColumnIndex("fltMittlereWegBewertung"));
						Integer isBestiegen = cursor.getInt(cursor
								.getColumnIndex("isAscendedRoute"));
						Long datumBestiegen = cursor.getLong(cursor
								.getColumnIndex("intDateOfAscend"));
						String strKommentar = cursor.getString(cursor
								.getColumnIndex("strMyRouteComment"));

						lstTT_Routes_AND.add(new TT_Route_AND(intTTWegNr,
								intGipfelNr, WegName, strGipfelName,
								blnAusrufeZeichen, intSterne,
								strSchwierigkeitsGrad,
								sachsenSchwierigkeitsGrad,
								ohneUnterstützungSchwierigkeitsGrad,
								rotPunktSchwierigkeitsGrad,
								intSprungSchwierigkeitsGrad,
								intAnzahlDerKommentare,
								fltMittlereWegBewertung, isBestiegen,
								datumBestiegen, strKommentar

						));

						Log.i(TAG, " -> Neue Route... "
								+ strGipfelName + "\r\nintTTWegNr: "
								+ intTTWegNr + "\t" + WegName);
					} while (cursor.moveToNext());
				}
			}
		} catch (SQLiteException se) {
			Log.e(TAG,
					"Could not create or Open the database");
		} finally {
			newDB.close();
			Toast.makeText(this, lstTT_Routes_AND.size() + " Wege gefunden"
					+ ( lstTT_Routes_AND.size()  ==  getResources().getInteger(R.integer.MaxNoItemQuerxy) 
					? " (Maximalanzahl an Ergebnissen erreicht)" : ""), Toast.LENGTH_LONG)
			.show();
		}
	}
}
