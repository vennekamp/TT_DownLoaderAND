package com.teufelsturm.tt_downloaderand_kotlin.foundCommentsList;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.dbHelper.DataBaseHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TT_CommentsFoundFragment extends Fragment {
    private static final String TAG = TT_CommentsFoundFragment.class.getSimpleName();
	public static final String ID = "TT_CommentsFoundFragment";

    private static final String SUCH_TEXT = "SUCH_TEXT";
    private static final String SUCH_GEBIET = "SUCH_GEBIET";
    private static final String SUCH_MIN_SCHWIERIGKEIT= "SUCH_MIN_SCHWIERIGKEIT";
    private static final String SUCH_MAX_SCHWIERIGKEIT= "SUCH_MAX_SCHWIERIGKEIT";
    private static final String SUCH_MIN_ANZAHL_DER_KOMMENTARE = "SUCH_MIN_ANZAHL_DER_KOMMENTARE";
    private static final String SUCH_MIN_COMMENTS_IN_COMMENT = "SUCH_MIN_COMMENTS_IN_COMMENT";

	private static ArrayList<TT_Comment_AND> lstTT_Comment_AND;
	private SQLiteDatabase newDB;
//	private TextToSpeech tts;

	public static Fragment newInstance(
            String strTextSuchtext, String strGebiet , int intMinSchwierigkeit,
            int intMaxSchwierigkeit, int intMinMinCommentInComment) {
        TT_CommentsFoundFragment f = new TT_CommentsFoundFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SUCH_TEXT, strTextSuchtext);
        bundle.putString(SUCH_GEBIET, strGebiet);
        bundle.putInt(SUCH_MIN_SCHWIERIGKEIT, intMinSchwierigkeit);
        bundle.putInt(SUCH_MAX_SCHWIERIGKEIT, intMaxSchwierigkeit);
        bundle.putInt(SUCH_MIN_COMMENTS_IN_COMMENT, intMinMinCommentInComment);
        f.setArguments(bundle);
	    return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "Neuer onCreate... ");
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
								@Nullable ViewGroup container,
								@Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.comments_activity_found_lv_list,
                container,false);
		lstTT_Comment_AND = new ArrayList<>();
		// query all routes to this summit
		openAndQueryDatabase();
		TT_Comment_ANDAdapter listenAdapter = new TT_Comment_ANDAdapter(getActivity(),
				lstTT_Comment_AND, true);
		Log.i(TAG,"(ListView) findViewById(R.id.list_routes);");
		RecyclerView recyclerview_comment = view.findViewById(R.id.recyclerview_comment);


        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview_comment.setLayoutManager(linearLayoutManager);


        recyclerview_comment.setAdapter(listenAdapter);
//		// Event Listener
//		tts = new TextToSpeech(getActivity(), this);
//		recyclerview_comment.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				Toast.makeText(
//						getActivity(),
//								lstTT_Comment_AND.get(position)
//										.getStrEntryKommentar(), Toast.LENGTH_LONG)
//						.show();
//				tts.speak(TT_RouteFoundFragment.ImprovedText4Tts(
//					lstTT_Comment_AND.get(position)
//						.getStrEntryKommentar() ), TextToSpeech.QUEUE_FLUSH, null);
//				// Log.i(TAG, "Click ListItem Number " + position
//				// + "\r\nGipfelname: "
//				// + lstTT_Gipfel_AND.get(position).getStr_SummitName());
//			}
//		});
		

		Log.i(TAG,"Neuer onCreate komplett abgearbeitet... ");
		Log.i(TAG,"Neuer onCreate... BEENDET");
        return view;
	}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.summits_found, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getView() != null) {
            ViewGroup parent = (ViewGroup) getView().getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
        }
    }

	//**************************

    private void openAndQueryDatabase() {
        Log.i(TAG, "Neuer openAndQueryDatabase... ");

        Bundle bundle = getArguments();
        assert bundle != null;
        String strTextSuchtext = bundle.getString(SUCH_TEXT);
        String strGebiet = bundle.getString(SUCH_GEBIET);
        Log.e(TAG, "strGebiet gesucht... " + strGebiet);
        int intMinSchwierigkeit = bundle.getInt(SUCH_MIN_SCHWIERIGKEIT);
        int intMaxSchwierigkeit = bundle.getInt(SUCH_MAX_SCHWIERIGKEIT);
        int intMinMinCommentInComment = bundle.getInt(SUCH_MIN_ANZAHL_DER_KOMMENTARE);
        assert strGebiet != null;
        openAndQueryDatabase(strTextSuchtext, strGebiet, intMinSchwierigkeit, intMaxSchwierigkeit,
                intMinMinCommentInComment);
    }


    private void openAndQueryDatabase(String strTextSuchtext, @NotNull String strGebiet, int intMinSchwierigkeit,
                                      int intMaxSchwierigkeit, int intMinMinCommentInComment ) {
		Log.i(TAG, "Neuer openAndQueryDatabase... ");

		try {
			DataBaseHelper dbHelper = new DataBaseHelper( getActivity().getApplicationContext() );
			newDB = dbHelper.getWritableDatabase();
			Cursor cursor;
			String QueryString1;
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
			QueryString1 = new StringBuilder()
					.append("SELECT a.[intTTWegNr], a.[strEntryKommentar], b.[WegName], b.[strSchwierigkeitsGrad]")
                    .append(" , c.[strName], ").append("b.[intTTGipfelNr], a.[entryBewertung], a.[strEntryUser], a.[entryDatum]")
                    .append("      FROM  [TT_Summit_AND] c, [TT_Route_AND] b, [TT_RouteComment_AND] a")
                    .append("      WHERE a.[entryBewertung] >= ").append(intMinMinCommentInComment)
                    .append("      AND a.[strEntryKommentar] like ")
                    .append(DatabaseUtils.sqlEscapeString("%" + strTextSuchtext + "%"))
                    .append("     AND a.[intTTWegNr] = b.[intTTWegNr]")
                    .append(strGebiet.equals(this.getString(R.string.strAll))
                            ? "       AND c.[strGebiet] != \"\" ": "       AND c.[strGebiet] = '" + strGebiet + "'")
                    .append("      AND c.[intTTGipfelNr] = b.[intTTGipfelNr]")
                    .append("      AND coalesce(b.[sachsenSchwierigkeitsGrad],")
                    .append("			b.[ohneUnterstützungSchwierigkeitsGrad],")
                    .append("			b.[rotPunktSchwierigkeitsGrad]")
                    .append("			, b.[intSprungSchwierigkeitsGrad] ) ")
                    .append(" BETWEEN ").append(intMinSchwierigkeit)
                    .append(" AND ").append(intMaxSchwierigkeit)
                    .append("     ORDER BY c.[strName] LIMIT ")
                    .append(getResources().getInteger(R.integer.MaxNoItemQuerxy)).toString();
					Log.i(TAG,"Neue Kommentarr zum Suche finden:\r\n" + QueryString1);

			cursor = newDB.rawQuery(QueryString1, null);
			Log.i(TAG,"Neuen Kommentar zum Weg suchen:\t c != null'"
							+ (cursor != null));

			if (cursor != null) {
				int iCounter = 0;
				if (cursor.moveToFirst()) {
					do {
						int intTTWegNr = cursor.getInt(cursor
								.getColumnIndex("intTTWegNr"));
						Log.i(TAG," -> intTTWegNr..... " + intTTWegNr);
						String strEntryKommentar = cursor.getString(cursor
								.getColumnIndex("strEntryKommentar"));
						Log.i(TAG," -> strEntryKommentar..... " + strEntryKommentar);
						String strWegName = cursor.getString(cursor
								.getColumnIndex("WegName")) + " ("
								+ cursor.getString(cursor
										.getColumnIndex("strSchwierigkeitsGrad")) + ")";
						String strName = cursor.getString(cursor
										 .getColumnIndex("strName"));
						int intTTGipfelNr = cursor.getInt(cursor
								 .getColumnIndex("intTTGipfelNr"));
						Log.i(TAG," -> strWegName..... " + strWegName);
						Integer intEntryBewertung = cursor.getInt(cursor
								.getColumnIndex("entryBewertung"));
						Log.i(TAG," -> intEntryBewertung..... " + intEntryBewertung);
						String strEntryUser = cursor.getString(cursor
								.getColumnIndex("strEntryUser"));
						Log.i(TAG," -> strEntryUser..... " + strEntryUser);
						Long longEntryDatum = cursor.getLong(cursor
								.getColumnIndex("entryDatum"));
						Log.i(TAG," -> longEntryDatum..... " + longEntryDatum);

						lstTT_Comment_AND.add(new TT_Comment_AND(intTTWegNr,
								strEntryKommentar, strWegName, strName, intTTGipfelNr, intEntryBewertung,
								strEntryUser, longEntryDatum));
						Log.i(TAG, ++iCounter + " -> Neuer Kommentar... " + strEntryUser);
					} while (cursor.moveToNext());
				}
				cursor.close();
			}
		} catch (SQLiteException se) {
			Log.e(TAG,"Could not create or Open the database");
		} finally {
			newDB.close();
			Toast.makeText(getActivity(), lstTT_Comment_AND.size() + " Kommentare gefunden"
					+ ( lstTT_Comment_AND.size()  ==  getResources().getInteger(R.integer.MaxNoItemQuerxy) 
					? " (Maximalanzahl an Ergebnissen erreicht)" : ""), Toast.LENGTH_LONG)
				.show();
		}
	}
//
//	@Override
//	public void onInit(int status) {
//		tts.setLanguage(Locale.GERMAN);
//	}
//
//	@Override
//	public void onPause() {
//	    //Close the Text to Speech Library
//	    if(tts != null) {
//	    	tts.stop();
//	        tts.shutdown();
//	        Log.d(TAG, "TTS Destroyed");
//	    }
//	    super.onPause();
//	}
}
