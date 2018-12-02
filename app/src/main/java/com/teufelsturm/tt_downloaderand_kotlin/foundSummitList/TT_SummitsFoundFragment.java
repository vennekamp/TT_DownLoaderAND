package com.teufelsturm.tt_downloaderand_kotlin.foundSummitList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.teufelsturm.tt_downloaderand_kotlin.MainActivity;
import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.dbHelper.DataBaseHelper;
import com.teufelsturm.tt_downloaderand_kotlin.foundRouteSingle.TT_Route_AND;
import com.teufelsturm.tt_downloaderand_kotlin.foundSummitSingle.TT_SummitFoundFragment;
import com.teufelsturm.tt_downloaderand_kotlin.foundSummitSingle.TT_Summit_AND;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TT_SummitsFoundFragment extends Fragment
		implements View.OnClickListener  {

	public static final String ID = "TT_SummitsFoundFragment";
    private final static String TAG = TT_SummitsFoundFragment.class.getSimpleName();
    private static final String SUCH_TEXT = "SUCH_TEXT";
    private static final String SUCH_GEBIET = "SUCH_GEBIET";
    private static final String SUCH_MIN_ANZAHL_DER_WEGE = "SUCH_MIN_ANZAHL_DER_WEGE";
    private static final String SUCH_MAX_ANZAHL_DER_WEGE = "SUCH_MAX_ANZAHL_DER_WEGE";
    private static final String SUCH_MIN_ANZAHL_DER_STERNCHEN_WEGE= "SUCH_MIN_ANZAHL_DER_STERNCHEN_WEGE";
    private static final String SUCH_MAX_ANZAHL_DER_STERNCHEN_WEGE= "SUCH_MAX_ANZAHL_DER_STERNCHEN_WEGE";

	private ArrayList<TT_Summit_AND> lstTT_Gipfel_AND;
	private SQLiteDatabase newDB;
    private TT_Summit_ANDAdapter listenAdapter;
    private RecyclerView mRecyclerView;
//	private TT_SummitsFoundFragment thisTT_SummitsFoundFragment;
	private static Boolean dataHasChanged = Boolean.FALSE;

	public static Fragment newInstance(String strTextSuchtext, String strGebiet,
                                       int intMinAnzahlDerWege, int intMaxAnzahlDerWege,
                                       int intMinAnzahlDerSternchenWege, int intMaxAnzahlDerSternchenWege) {
        TT_SummitsFoundFragment f = new TT_SummitsFoundFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SUCH_TEXT, strTextSuchtext);
        bundle.putString(SUCH_GEBIET, strGebiet);
        bundle.putInt(SUCH_MIN_ANZAHL_DER_WEGE, intMinAnzahlDerWege);
        bundle.putInt(SUCH_MAX_ANZAHL_DER_WEGE, intMaxAnzahlDerWege);
        bundle.putInt(SUCH_MIN_ANZAHL_DER_STERNCHEN_WEGE, intMinAnzahlDerSternchenWege);
        bundle.putInt(SUCH_MAX_ANZAHL_DER_STERNCHEN_WEGE, intMaxAnzahlDerSternchenWege);
        f.setArguments(bundle);
        return f;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (dataHasChanged) {
			this.openAndQueryDatabase();
			listenAdapter.notifyDataSetChanged();
			dataHasChanged = Boolean.FALSE;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public void hasChangedData() {
		Log.i(TAG,"thisTT_SummitsFoundFragment != null ");
        openAndQueryDatabase();
        listenAdapter.notifyDataSetChanged();
        dataHasChanged = Boolean.FALSE;
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

        View view = inflater.inflate(R.layout.summits_activity_found_lv_list,
                container, false);
		lstTT_Gipfel_AND = new ArrayList<>();
		openAndQueryDatabase();
		Log.i(TAG,"Neuer openAndQueryDatabase... BEENDET!");

		listenAdapter = new TT_Summit_ANDAdapter(this, lstTT_Gipfel_AND);


		Log.i(TAG, "Suche meinListView... ");
        mRecyclerView = view.findViewById(R.id.list_summits_activity_found_lv_list);
		Log.i(TAG, "meinListView.setAdapter...");

        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

		mRecyclerView.setAdapter(listenAdapter);


		// http://android-er.blogspot.de/2011/11/detect-swipe-using-simpleongestureliste.html
		// TODO: OSM-Map einblenden
		// http://android-er.blogspot.de/2012/05/simple-example-use-osmdroid-and-slf4j.html

		Log.i(TAG, "Neuer onCreate... BEENDET");
        return view;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}
	@Override
	public void onViewCreated(@NotNull View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		((MainActivity)getActivity()).showFAB(ID);
	}


	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.summits_found, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }


    private void openAndQueryDatabase() {
        Log.i(TAG, "Neuer openAndQueryDatabase... ");

        Bundle bundle = getArguments();
        String strTextSuchtext = bundle.getString(SUCH_TEXT);
        String strGebiet = bundle.getString(SUCH_GEBIET);
        int intMinAnzahlDerWege = bundle.getInt(SUCH_MIN_ANZAHL_DER_WEGE);
        int intMaxAnzahlDerWege = bundle.getInt(SUCH_MAX_ANZAHL_DER_WEGE);
        int intMinAnzahlDerSternchenWege = bundle.getInt(SUCH_MIN_ANZAHL_DER_STERNCHEN_WEGE);
        int intMaxAnzahlDerSternchenWege = bundle.getInt(SUCH_MAX_ANZAHL_DER_STERNCHEN_WEGE);
        openAndQueryDatabase(strTextSuchtext, strGebiet, intMinAnzahlDerWege, intMaxAnzahlDerWege,
                intMinAnzahlDerSternchenWege, intMaxAnzahlDerSternchenWege);
    }



    private void openAndQueryDatabase(String strTextSuchtext, @NotNull String strGebiet,
                                      int intMinAnzahlDerWege, int intMaxAnzahlDerWege,
                                      int intMinAnzahlDerSternchenWege, int intMaxAnzahlDerSternchenWege) {
		Log.i(TAG, "Neuer openAndQueryDatabase... ");
		lstTT_Gipfel_AND.clear();
		try {
			DataBaseHelper dbHelper = new DataBaseHelper(getActivity());
			newDB = dbHelper.getWritableDatabase();
			String QueryString1;
			Log.i(TAG, "QueryString1 erzeugen... ");
			QueryString1 = new StringBuilder().append("SELECT   a.intTTGipfelNr, a.strName, a.strGebiet")
                    .append(", a.intKleFuGipfelNr , a.intAnzahlWege , a.intAnzahlSternchenWege")
                    .append(", a.strLeichtesterWeg, a.dblGPS_Latitude, a.dblGPS_Longitude")
                    .append(", b.[isAscendedSummit], b.[intDateOfAscend], b.[strMySummitComment] ")
                    .append(" FROM ")
                    .append(" TT_Summit_AND a ")
                    .append(" LEFT OUTER JOIN myTT_Summit_AND b")
                    .append(" ON (a.[intTTGipfelNr] = b.[intTTGipfelNr]) ")
                    .append(" WHERE ").toString();
			Log.i(TAG, "QueryString1 erweitern (1)... "
					+ strTextSuchtext);
			if (!strGebiet.equals("Alle"))
				QueryString1 += " strGebiet = '"
						+ strGebiet + "' AND ";
			Log.i(TAG, "QueryString1 erweitern (2)... ");
			QueryString1 += " intAnzahlWege >= "
					+ intMinAnzahlDerWege + " AND intAnzahlWege <= '"
					+ intMaxAnzahlDerWege
                    + "' AND intAnzahlSternchenWege >= '"
					+ intMinAnzahlDerSternchenWege
					+ "' AND intAnzahlSternchenWege <= '"
					+ intMaxAnzahlDerSternchenWege + "'";
			Log.i(TAG, "QueryString1 erweitern (3)... ");
			if (!strTextSuchtext.equals(""))
				QueryString1 += " AND strName like '%"
						+ strTextSuchtext + "%'";
			Log.i(TAG, "Neuen Gipfel suchen:\r\n"
					+ QueryString1);
			Log.i(TAG, "Neuen SuchText: '" + strTextSuchtext + "'");
			Cursor cursor = null;
			cursor = newDB.rawQuery(QueryString1, null);
			Log.i(TAG,
					"Neuen Gipfel gesucht... 'c != null'" + (cursor != null));

			if (cursor != null) {
				if (cursor.moveToFirst()) {
					do {
						String strGipfelName = cursor.getString(cursor
								.getColumnIndex("strName"));
						int intTTGipfelNr = cursor.getInt(cursor
								.getColumnIndex("intTTGipfelNr"));
						int intKleFuGipfelNr = cursor.getInt(cursor
								.getColumnIndex("intKleFuGipfelNr"));
						String str_Area = cursor.getString(cursor
								.getColumnIndex("strGebiet"));
						int int_NumberOfRoutes = cursor.getInt(cursor
								.getColumnIndex("intAnzahlWege"));
						int int_NumberofStarRoutes = cursor.getInt(cursor
								.getColumnIndex("intAnzahlSternchenWege"));
						String str_EasiestGrade = cursor.getString(cursor
								.getColumnIndex("strLeichtesterWeg"));
						double dbl_GpsLat = cursor.getDouble(cursor
								.getColumnIndex("dblGPS_Latitude"));
						double dbl_GpsLong = cursor.getDouble(cursor
								.getColumnIndex("dblGPS_Longitude"));
						boolean bln_Asscended = cursor.getInt(cursor
								.getColumnIndex("isAscendedSummit")) > 0;
						Long int_DateAsscended = cursor.getLong(cursor
								.getColumnIndex("intDateOfAscend"));
						String str_MyComment = cursor.getString(cursor
								.getColumnIndex("strMySummitComment"));

						lstTT_Gipfel_AND
								.add(new TT_Summit_AND(intTTGipfelNr,
										intKleFuGipfelNr, strGipfelName,
										str_Area, int_NumberOfRoutes,
										int_NumberofStarRoutes,
										str_EasiestGrade, dbl_GpsLong,
										dbl_GpsLat, bln_Asscended,
										int_DateAsscended, str_MyComment));

						Log.i(TAG," -> Neuer Gipfel... " + strGipfelName
										+ "\r\nTT-Gipfelnummer: " + intTTGipfelNr);
					} while (cursor.moveToNext());
				}
			}
		} catch (SQLiteException se) {
			Log.e(TAG,"Could not create or Open the database");
		} finally {
			newDB.close();
			Toast.makeText(getActivity(), lstTT_Gipfel_AND.size() + " Gipfel gefunden",
					Toast.LENGTH_LONG).show();
		}
	}

    @Override
    public void onClick(View v) {
        Log.i(TAG,"Start TT_RouteFoundFragment... ");

        int itemPosition = mRecyclerView.getChildLayoutPosition(v);
        TT_Summit_AND tt_summit_andNext  = lstTT_Gipfel_AND.get(itemPosition);
        TT_SummitFoundFragment tt_summitFoundFragment =
                TT_SummitFoundFragment.newInstance(tt_summit_andNext);
        ((MainActivity)getActivity()).replaceFragment(tt_summitFoundFragment, TT_SummitFoundFragment.ID);
    }
}
