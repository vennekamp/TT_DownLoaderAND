package com.teufelsturm.tt_downloaderand_kotlin.summit;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.teufelsturm.tt_downloaderand_kotlin.MainActivitySearchSummit;
import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.TT_DB_Helper.DataBaseHelper;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.TT_Summit_AND;

import java.util.ArrayList;
import java.util.List;

public class TT_SummitsFoundActivity extends Fragment {

    private final static String TAG = TT_SummitsFoundActivity.class.getSimpleName();

	private List<TT_Summit_AND> lstTT_Gipfel_AND;
	private SQLiteDatabase newDB;
	private ListView meinListView;
	private TT_Summit_ANDAdapter listenAdapter;
	private static TT_SummitsFoundActivity thisTT_SummitsFoundActivity;
	private static Boolean dataHasChanged = Boolean.FALSE;

	@Override
	public void onResume() {
		super.onResume();
		thisTT_SummitsFoundActivity = this;
		if (dataHasChanged) {
			this.openAndQueryDatabase();
			listenAdapter.notifyDataSetChanged();
			dataHasChanged = Boolean.FALSE;
		}
	}

	@Override
	public void onPause() {
		thisTT_SummitsFoundActivity = null;
		super.onPause();
	}

	public static void hasChangedData() {
		Log.i(TAG,"thisTT_SummitsFoundActivity != null " + (thisTT_SummitsFoundActivity != null));
		if (thisTT_SummitsFoundActivity != null) {
			thisTT_SummitsFoundActivity.openAndQueryDatabase();
			thisTT_SummitsFoundActivity.listenAdapter.notifyDataSetChanged();
			dataHasChanged = Boolean.FALSE;
		} else {
			dataHasChanged = Boolean.TRUE;
		}
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
		lstTT_Gipfel_AND = new ArrayList<TT_Summit_AND>();
		openAndQueryDatabase();
		Log.i(TAG,
				"Neuer openAndQueryDatabase... BEENDET!");

		listenAdapter = new TT_Summit_ANDAdapter(getActivity(), lstTT_Gipfel_AND);
		Log.i(TAG, "Suche meinListView... ");
		meinListView = (ListView) view.findViewById(R.id.list_summits);
		Log.i(TAG, "meinListView.setAdapter...");
		// // working Code for the Header Creation:
		// LayoutInflater layoutInflater = (LayoutInflater) this
		// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// Log.i(TAG,
		// "RelativeLayout myHeaderView  ... ");
		// RelativeLayout myHeaderView = (RelativeLayout)
		// layoutInflater.inflate(
		// R.layout.main_activity, null, false);
		// Log.i(TAG,
		// "RelativeLayout meinListView.addHeaderView... ");
		// meinListView.addHeaderView(myHeaderView, null, false);
		// http://www.androiddesignpatterns.com/2012/07/understanding-loadermanager.html
		meinListView.setAdapter(listenAdapter);
		// Event Listener
		meinListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Toast.makeText(
				// getApplicationContext(),
				// "Click ListItem Number "
				// + position
				// + "\r\nGipfelname: "
				// + lstTT_Gipfel_AND.get(position)
				// .getStr_SummitName(), Toast.LENGTH_LONG)
				// .show();
				Log.i(TAG,
						"Neuer addonPageSummitsFoundActivity... ");
				Intent addonPageSummitsFoundActivity = new Intent(
						TT_SummitsFoundActivity.this,
						TT_SummitFoundActivity.class);
				
				Log.i(TAG,
						"Click ListItem Number "
								+ position
								+ "\r\nGipfelname: "
								+ lstTT_Gipfel_AND.get(position)
										.getStr_SummitName());
				addonPageSummitsFoundActivity.putExtra("TT_Gipfel_AND",
						lstTT_Gipfel_AND.get(position));
				Log.i(TAG, "startActivity... ");
				startActivity(addonPageSummitsFoundActivity);


				Fragment fragment = TT_SummitFoundActivity.newInstanec(lstTT_Gipfel_AND.get(position));
				FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.fragment_container, fragment);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
		});
		// http://android-er.blogspot.de/2011/11/detect-swipe-using-simpleongestureliste.html
		// TODO: OSM-Map einblenden
		// http://android-er.blogspot.de/2012/05/simple-example-use-osmdroid-and-slf4j.html

		Log.i(TAG, "Neuer onCreate... BEENDET");
        return view;
	}

	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.summits_found, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }


    private void openAndQueryDatabase() {
		Log.i(TAG, "Neuer openAndQueryDatabase... ");
		lstTT_Gipfel_AND.clear();
		try {
			DataBaseHelper dbHelper = new DataBaseHelper(getActivity());
			newDB = dbHelper.getWritableDatabase();
			String QueryString1;
			Log.i(TAG, "QueryString1 erzeugen... ");
			QueryString1 = "SELECT   a.intTTGipfelNr, a.strName, a.strGebiet"
					+ ", a.intKleFuGipfelNr , a.intAnzahlWege , a.intAnzahlSternchenWege"
					+ ", a.strLeichtesterWeg, a.dblGPS_Latitude, a.dblGPS_Longitude"
					+ ", b.[isAscendedSummit], b.[intDateOfAscend], b.[strMySummitComment] "
					+ " FROM " + " TT_Summit_AND a "
					+ " LEFT OUTER JOIN myTT_Summit_AND b"
					+ " ON (a.[intTTGipfelNr] = b.[intTTGipfelNr]) "
					+ " WHERE ";
			Log.i(TAG, "QueryString1 erweitern (1)... "
					+ MainActivitySearchSummit.getStrtextViewGebiet());
			if (!MainActivitySearchSummit.getStrtextViewGebiet().equals("Alle"))
				QueryString1 += " strGebiet = '"
						+ MainActivitySearchSummit.getStrtextViewGebiet()
						+ "' AND ";
			Log.i(TAG, "QueryString1 erweitern (2)... ");
			QueryString1 += " intAnzahlWege >= "
					+ MainActivitySearchSummit.getIntMinAnzahlDerWege()
					+ " AND intAnzahlWege <= '"
					+ MainActivitySearchSummit.getIntMaxAnzahlDerWege()
					+ "' AND intAnzahlSternchenWege >= '"
					+ MainActivitySearchSummit
							.getIntMinAnzahlDerSternchenWege()
					+ "' AND intAnzahlSternchenWege <= '"
					+ MainActivitySearchSummit
							.getIntMaxAnzahlDerSternchenWege() + "'";
			Log.i(TAG, "QueryString1 erweitern (3)... ");
			if (!MainActivitySearchSummit.getStrTextSuchtext().equals(""))
				QueryString1 += " AND strName like '%"
						+ MainActivitySearchSummit.getStrTextSuchtext() + "%'";
			Log.i(TAG, "Neuen Gipfel suchen:\r\n"
					+ QueryString1);
			Log.i(TAG, "Neuen SuchText: '"
					+ MainActivitySearchSummit.getStrTextSuchtext() + "'");
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

						Log.i(TAG,
								" -> Neuer Gipfel... " + strGipfelName
										+ "\r\nTT-Gipfelnummer: "
										+ intTTGipfelNr);
					} while (cursor.moveToNext());
				}
			}
		} catch (SQLiteException se) {
			Log.e(TAG,
					"Could not create or Open the database");
		} finally {
			newDB.close();
			Toast.makeText(getActivity(), lstTT_Gipfel_AND.size() + " Gipfel gefunden",
					Toast.LENGTH_LONG).show();
		}
	}

}
