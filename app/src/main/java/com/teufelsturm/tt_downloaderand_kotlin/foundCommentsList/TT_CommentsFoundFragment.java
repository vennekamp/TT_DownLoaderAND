package com.teufelsturm.tt_downloaderand_kotlin.foundCommentsList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.teufelsturm.tt_downloader3.MainActivity;
import com.teufelsturm.tt_downloader3.R;
import com.teufelsturm.tt_downloader3.TT_DownLoadedApp;
import com.teufelsturm.tt_downloader3.dbHelper.StaticSQLQueries;
import com.teufelsturm.tt_downloader3.model.TT_Comment_AND;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TT_CommentsFoundFragment extends Fragment {
	private static final String TAG = TT_CommentsFoundFragment.class.getSimpleName();
	public static final String ID = "TT_CommentsFoundFragment";

    private static final String SUCH_TEXT = "SUCH_TEXT";
    private static final String SUCH_GEBIET = "SUCH_GEBIET";
    private static final String SUCH_MIN_SCHWIERIGKEIT= "SUCH_MIN_SCHWIERIGKEIT";
    private static final String SUCH_MAX_SCHWIERIGKEIT= "SUCH_MAX_SCHWIERIGKEIT";
//	public static final String SUCH_MIN_MIN_COMMENT_IN_COMMENT = "SUCH_MIN_MIN_COMMENT_IN_COMMENT";
	public static final String SUCH_MIN_GRADING_OF_COMMET = "SUCH_MIN_GRADING_OF_COMMET";

	private static ArrayList<TT_Comment_AND> lstTT_Comment_AND;
//	private SQLiteDatabase newDB;
//	private TextToSpeech tts;

	public static Fragment newInstance(
            String strTextSuchtext, String strGebiet , int intMinSchwierigkeit,
            int intMaxSchwierigkeit, int intMinGradingOfComment) {
        TT_CommentsFoundFragment f = new TT_CommentsFoundFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SUCH_TEXT, strTextSuchtext);
        bundle.putString(SUCH_GEBIET, strGebiet);
        bundle.putInt(SUCH_MIN_SCHWIERIGKEIT, intMinSchwierigkeit);
        bundle.putInt(SUCH_MAX_SCHWIERIGKEIT, intMaxSchwierigkeit);
		bundle.putInt(SUCH_MIN_GRADING_OF_COMMET, intMinGradingOfComment);
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
		TT_Comment_ANDAdapter listenAdapter = new TT_Comment_ANDAdapter((MainActivity)getActivity(),
				lstTT_Comment_AND, true);
		Log.i(TAG,"(ListView) findViewById(R.id.list_routes);");
		RecyclerView recyclerview_comment = view.findViewById(R.id.recyclerview_comment);


        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerview_comment.setLayoutManager(linearLayoutManager);


        recyclerview_comment.setAdapter(listenAdapter);

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
    public void onViewCreated(@NotNull View view, Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);
        ((MainActivity)getActivity()).showFAB(ID);
    }


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
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
        int intMinGradingOfComment = bundle.getInt(SUCH_MIN_GRADING_OF_COMMET);
        assert strGebiet != null;
        openAndQueryDatabase(strTextSuchtext, strGebiet, intMinSchwierigkeit, intMaxSchwierigkeit,
                intMinGradingOfComment);
    }


    private void openAndQueryDatabase(String strTextSuchtext, @NotNull String strGebiet, int intMinSchwierigkeit,
                                      int intMaxSchwierigkeit, int intMinGradingOfComment ) {
		Log.i(TAG, "Neuer openAndQueryDatabase... ");

		try {
//			DataBaseHelper dbHelper = new DataBaseHelper( getActivity().getApplicationContext() );

			Cursor cursor;

			String queryString1 = StaticSQLQueries.getSQL4CommentsSearch(getContext(),
                    strTextSuchtext, strGebiet, intMinSchwierigkeit, intMaxSchwierigkeit, intMinGradingOfComment);
            cursor = TT_DownLoadedApp.getDataBaseHelper().getMyDataBase().rawQuery(queryString1, null);
			Log.i(TAG,"Neuen Kommentar zum Weg suchen:\t c != null'"
							+ (cursor != null));

			if (cursor != null && cursor.moveToFirst()) {
				int iCounter = 0;
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

				cursor.close();
			}
		} catch (SQLiteException se) {
			Log.e(TAG,"Could not create or Open the database");
		} finally {
			Toast.makeText(getActivity(), lstTT_Comment_AND.size() + " Kommentare gefunden"
					+ ( lstTT_Comment_AND.size()  ==  getResources().getInteger(R.integer.MaxNoItemQuerxy)
					? " (Maximalanzahl an Ergebnissen erreicht)" : ""), Toast.LENGTH_LONG)
				.show();
		}
	}

}
