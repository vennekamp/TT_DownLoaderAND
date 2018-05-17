package com.teufelsturm.tt_downloaderand_kotlin.comments;

import android.app.Activity;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.teufelsturm.tt_downloaderand_kotlin.MainActivitySearchComment;
import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.TT_DB_Helper.DataBaseHelper;
import com.teufelsturm.tt_downloaderand_kotlin.routes.TT_RouteFoundActivity;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.TT_Comment_AND;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TT_CommentsFoundActivity extends Activity 
	implements OnInitListener {

	private static List<TT_Comment_AND> lstTT_Comment_AND;
	private SQLiteDatabase newDB;
	private ListView meinListView;
	private TT_Comment_ANDAdapter listenAdapter; 
	private TextToSpeech tts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(getClass().getSimpleName(), "Neuer onCreate... ");
		setContentView(R.layout.comments_activity_found_lv_list);

		lstTT_Comment_AND = new ArrayList<TT_Comment_AND>();
		// query all routes to this summit
		openAndQueryDatabase();
		listenAdapter = new TT_Comment_ANDAdapter(this,
				lstTT_Comment_AND, true);
		Log.i(getClass().getSimpleName(),
				"(ListView) findViewById(R.id.list_routes);");
		meinListView = (ListView) findViewById(R.id.list_comments);
		meinListView.setAdapter(listenAdapter);
		// Event Listener
		tts = new TextToSpeech(this, this);
		meinListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(
						getApplicationContext(),
								lstTT_Comment_AND.get(position)
										.getStrEntryKommentar(), Toast.LENGTH_LONG)
						.show();
				tts.speak(TT_RouteFoundActivity.ImprovedText4Tts(
					lstTT_Comment_AND.get(position)
						.getStrEntryKommentar() ), TextToSpeech.QUEUE_FLUSH, null);
				// Log.i(TAG, "Click ListItem Number " + position
				// + "\r\nGipfelname: "
				// + lstTT_Gipfel_AND.get(position).getStr_SummitName());
			}
		});
		

		Log.i(getClass().getSimpleName(),
				"Neuer onCreate komplett abgearbeitet... ");
	
		Log.i(getClass().getSimpleName(),"Neuer onCreate... BEENDET");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.summits_found, menu);
		return true;
	}

	//**************************
	private void openAndQueryDatabase() {
		Log.i(getClass().getSimpleName(), "Neuer openAndQueryDatabase... ");

		try {
			DataBaseHelper dbHelper = new DataBaseHelper(
					this.getApplicationContext());
			newDB = dbHelper.getWritableDatabase();
			Cursor cursor = null;
			String QueryString1 = null;
			String strTextSuchtext = MainActivitySearchComment
					.getStrTextSuchtext();
			String strGebiet = MainActivitySearchComment.getStrtextViewGebiet();
			int intMinSchwierigkeit = MainActivitySearchComment
					.getEnumMinLimitsForScale();
			int intMaxSchwierigkeit = MainActivitySearchComment
					.getEnumMaxLimitsForScale();
			int intMinMinCommentInComment = MainActivitySearchComment
					.getIntMinCommentInComment();
//			SELECT a.[intTTWegNr], a.[strEntryKommentar], b.[WegName], b.[strSchwierigkeitsGrad], c.[strName],  b.[intTTGipfelNr], a.[entryBewertung], a.[strEntryUser], a.[entryDatum] 
//				      FROM  [TT_Summit_AND] c, [TT_Route_AND] b, [TT_RouteComment_AND] a      
//				      WHERE a.[entryBewertung] >= 0    
//				      AND a.[strEntryKommentar] like '%%'
//				      AND a.[intTTWegNr] = b.[intTTWegNr]     
//				      AND c.[strGebiet] = 'Bielatal' 
//				      AND c.[intTTGipfelNr] = b.[intTTGipfelNr] 
//				      AND coalesce(b.[sachsenSchwierigkeitsGrad],
//							b.[ohneUnterstützungSchwierigkeitsGrad],
//							b.[rotPunktSchwierigkeitsGrad]
//							, b.[intSprungSchwierigkeitsGrad] ) BETWEEN 1 AND 15
//				     ORDER BY b.[intTTGipfelNr] LIMIT 250
			QueryString1 = "SELECT a.[intTTWegNr], a.[strEntryKommentar], b.[WegName], b.[strSchwierigkeitsGrad]" +
					" , c.[strName], " +
					"b.[intTTGipfelNr], a.[entryBewertung], a.[strEntryUser], a.[entryDatum]" +
					"      FROM  [TT_Summit_AND] c, [TT_Route_AND] b, [TT_RouteComment_AND] a" +
					"      WHERE a.[entryBewertung] >= " + intMinMinCommentInComment +  
					"      AND a.[strEntryKommentar] like " + DatabaseUtils.sqlEscapeString("%" + strTextSuchtext + "%") +
					"     AND a.[intTTWegNr] = b.[intTTWegNr]" +
					(strGebiet.equals(this.getString(R.string.strAll)) 
							? "       AND c.[strGebiet] != \"\" "
							: "       AND c.[strGebiet] = '" + strGebiet + "'") +
					"      AND c.[intTTGipfelNr] = b.[intTTGipfelNr]" +
					"      AND coalesce(b.[sachsenSchwierigkeitsGrad]," +
					"			b.[ohneUnterstützungSchwierigkeitsGrad]," +
					"			b.[rotPunktSchwierigkeitsGrad]" +
					"			, b.[intSprungSchwierigkeitsGrad] ) " +
					" BETWEEN " + intMinSchwierigkeit + " AND " + intMaxSchwierigkeit +
					"     ORDER BY c.[strName] LIMIT " + getResources().getInteger(R.integer.MaxNoItemQuerxy);  
					Log.i(getClass().getSimpleName(),
							"Neue Kommentarr zum Suche finden:\r\n" + QueryString1);

			cursor = newDB.rawQuery(QueryString1, null);
			Log.i(getClass().getSimpleName(),
					"Neuen Kommentar zum Weg suchen:\t c != null'"
							+ (cursor != null));

			if (cursor != null) {
				int iCounter = 0;
				if (cursor.moveToFirst()) {
					do {
						int intTTWegNr = cursor.getInt(cursor
								.getColumnIndex("intTTWegNr"));
						Log.i(getClass().getSimpleName(), 
								" -> intTTWegNr..... " + intTTWegNr);
						String strEntryKommentar = cursor.getString(cursor
								.getColumnIndex("strEntryKommentar"));
						Log.i(getClass().getSimpleName(), 
								" -> strEntryKommentar..... " + strEntryKommentar);
						String strWegName = cursor.getString(cursor
								.getColumnIndex("WegName")) + " ("
								+ cursor.getString(cursor
										.getColumnIndex("strSchwierigkeitsGrad")) + ")";
						String strName = cursor.getString(cursor
										 .getColumnIndex("strName"));
						int intTTGipfelNr = cursor.getInt(cursor
								 .getColumnIndex("intTTGipfelNr"));
						Log.i(getClass().getSimpleName(), 
								" -> strWegName..... " + strWegName);
						Integer intEntryBewertung = cursor.getInt(cursor
								.getColumnIndex("entryBewertung"));
						Log.i(getClass().getSimpleName(), 
								" -> intEntryBewertung..... " + intEntryBewertung);
						String strEntryUser = cursor.getString(cursor
								.getColumnIndex("strEntryUser"));
						Log.i(getClass().getSimpleName(), 
								" -> strEntryUser..... " + strEntryUser);
						Long longEntryDatum = cursor.getLong(cursor
								.getColumnIndex("entryDatum"));
						Log.i(getClass().getSimpleName(), 
								" -> longEntryDatum..... " + longEntryDatum);

						lstTT_Comment_AND.add(new TT_Comment_AND(intTTWegNr,
								strEntryKommentar, strWegName, strName, intTTGipfelNr, intEntryBewertung,
								strEntryUser, longEntryDatum));
						Log.i(getClass().getSimpleName(), ++iCounter
								+ " -> Neuer Kommentar... " + strEntryUser);
					} while (cursor.moveToNext());
				}
			}
		} catch (SQLiteException se) {
			Log.e(getClass().getSimpleName(),
					"Could not create or Open the database");
		} finally {
			newDB.close();
			Toast.makeText(this, lstTT_Comment_AND.size() + " Kommentare gefunden"
					+ ( lstTT_Comment_AND.size()  ==  getResources().getInteger(R.integer.MaxNoItemQuerxy) 
					? " (Maximalanzahl an Ergebnissen erreicht)" : ""), Toast.LENGTH_LONG)
				.show();
		}
	}

	@Override
	public void onInit(int status) {
		tts.setLanguage(Locale.GERMAN);
	}	
	@Override
	protected void onDestroy() {
	    //Close the Text to Speech Library
	    if(tts != null) {
	    	tts.stop();
	        tts.shutdown();
	        Log.d(getClass().getSimpleName(), "TTS Destroyed");
	    }
	    super.onDestroy();
	}
}
