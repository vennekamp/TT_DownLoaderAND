package com.teufelsturm.tt_downloaderand_kotlin.foundRoutesList;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.teufelsturm.tt_downloaderand_kotlin.MainActivity;
import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.dbHelper.DataBaseHelper;
import com.teufelsturm.tt_downloaderand_kotlin.foundRouteSingle.TT_RouteFoundFragment;
import com.teufelsturm.tt_downloaderand_kotlin.foundSummitSingle.TT_SummitFoundFragment;
import com.teufelsturm.tt_downloaderand_kotlin.foundRouteSingle.TT_Route_AND;
import com.teufelsturm.tt_downloaderand_kotlin.foundSummitSingle.TT_Summit_AND;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TT_RoutesFoundFragment extends Fragment
		implements View.OnClickListener {

	public static final String ID = "TT_RoutesFoundFragment";
	private final static String TAG = TT_RoutesFoundFragment.class.getSimpleName();
    private static final String SUCH_TEXT = "SUCH_TEXT";
    private static final String SUCH_GEBIET = "SUCH_GEBIET";
    private static final String SUCH_MIN_SCHWIERIGKEIT= "SUCH_MIN_SCHWIERIGKEIT";
    private static final String SUCH_MAX_SCHWIERIGKEIT= "SUCH_MAX_SCHWIERIGKEIT";
    private static final String SUCH_MIN_ANZAHL_DER_KOMMENTARE = "SUCH_MIN_ANZAHL_DER_KOMMENTARE";
    private static final String SUCH_MITTLERE_WEG_BEWERTUNG = "SUCH_MITTLERE_WEG_BEWERTUNG";



	private ArrayList<TT_Route_AND> lstTT_Routes_AND;
	private SQLiteDatabase newDB;
	private RecyclerView mRecyclerView_RoutesFound;
	private TT_Route_ANDAdapter listenAdapter;
	private TT_RoutesFoundFragment thisTT_RoutesFoundActivity;
	private Boolean dataHasChanged = Boolean.FALSE;

    public static Fragment newInstance(String strTextSuchtext,
                                       String strGebiet,
                                       int intMinSchwierigkeit,
                                       int intMaxSchwierigkeit,
                                       int intMinAnzahlDerKommentare,
                                       int intMittlereWegBewertung ) {
        TT_RoutesFoundFragment f = new TT_RoutesFoundFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SUCH_TEXT, strTextSuchtext);
        bundle.putString(SUCH_GEBIET, strGebiet);
        bundle.putInt(SUCH_MIN_SCHWIERIGKEIT, intMinSchwierigkeit);
        bundle.putInt(SUCH_MAX_SCHWIERIGKEIT, intMaxSchwierigkeit);
        bundle.putInt(SUCH_MIN_ANZAHL_DER_KOMMENTARE, intMinAnzahlDerKommentare);
        bundle.putInt(SUCH_MITTLERE_WEG_BEWERTUNG, intMittlereWegBewertung);
        f.setArguments(bundle);
        return f;
    }

    @Override
	public void onResume() {
		super.onResume();
		thisTT_RoutesFoundActivity = this;
		if (dataHasChanged) {
			this.openAndQueryDatabase();
			listenAdapter.notifyDataSetChanged();
			dataHasChanged = Boolean.FALSE;
		}
	}

	@Override
	public void onPause() {
		thisTT_RoutesFoundActivity = null;
		super.onPause();
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

        View view = inflater.inflate(R.layout.routes_activity_found_lv_list,
                container, false);
		lstTT_Routes_AND = new ArrayList<>();
		openAndQueryDatabase();
		Log.i(TAG,"Neuer openAndQueryDatabase... BEENDET!");
		listenAdapter = new TT_Route_ANDAdapter(this,
                lstTT_Routes_AND,true);
		Log.i(TAG,"Suche mRecyclerView_RoutesFound... ");
		mRecyclerView_RoutesFound
				= view.findViewById(R.id.recyclerview_in_routes_activity_found_lv_list);

        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView_RoutesFound.setLayoutManager(linearLayoutManager);

		Log.i(TAG,"mRecyclerView_RoutesFound.setAdapter...");
		// http://www.androiddesignpatterns.com/2012/07/understanding-loadermanager.html
		mRecyclerView_RoutesFound.setAdapter(listenAdapter);
		// http://android-er.blogspot.de/2011/11/detect-swipe-using-simpleongestureliste.html
		// http://android-er.blogspot.de/2012/05/simple-example-use-osmdroid-and-slf4j.html
        Log.i(TAG,"Neuer onCreate... BEENDET");
		return view;
	}

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).showFAB(ID);
    }

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
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
        Log.e(TAG, "strGebiet gesucht... " + strGebiet);
        int intMinSchwierigkeit = bundle.getInt(SUCH_MIN_SCHWIERIGKEIT);
        int intMaxSchwierigkeit = bundle.getInt(SUCH_MAX_SCHWIERIGKEIT);
        int intMinAnzahlDerKommentare = bundle.getInt(SUCH_MIN_ANZAHL_DER_KOMMENTARE);
        int intMittlereWegBewertung = bundle.getInt(SUCH_MITTLERE_WEG_BEWERTUNG);
        openAndQueryDatabase(strTextSuchtext, strGebiet, intMinSchwierigkeit, intMaxSchwierigkeit,
                intMinAnzahlDerKommentare, intMittlereWegBewertung);
    }


    private void openAndQueryDatabase(String strTextSuchtext,
                                      String strGebiet,
                                      int intMinSchwierigkeit,
                                      int intMaxSchwierigkeit,
                                      int intMinAnzahlDerKommentare,
                                      int intMittlereWegBewertung ) {

		lstTT_Routes_AND.clear();
		try {
			DataBaseHelper dbHelper = new DataBaseHelper(getActivity());
			newDB = dbHelper.getWritableDatabase();
			Log.i(TAG, "queryString erzeugen... ");
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
			String queryString = new StringBuilder()
                    .append("SELECT a.[intTTWegNr], a.[intTTGipfelNr]\r\n")
                    .append("       , a.[WegName], c.[strName], a.[blnAusrufeZeichen]\r\n")
                    .append("       , a.[intSterne], a.[strSchwierigkeitsGrad], a.[intSprungSchwierigkeitsGrad]\r\n")
                    .append("       , a.[sachsenSchwierigkeitsGrad]\r\n")
                    .append("       , a.[ohneUnterstützungSchwierigkeitsGrad]\r\n")
                    .append("       , a.[rotPunktSchwierigkeitsGrad]\r\n")
                    .append("       , a.[intSprungSchwierigkeitsGrad]\r\n")
                    .append("       , a. [intAnzahlDerKommentare], a. [fltMittlereWegBewertung]\r\n")
                    .append("       , d.[isAscendedRoute], d.[intDateOfAscend], d.[strMyRouteComment]\r\n")
                    .append("       FROM [TT_Route_AND] a, [TT_Summit_AND] c\r\n")
                    .append("       LEFT OUTER JOIN [myTT_Route_AND] d\r\n")
                    .append("            ON a.[intTTWegNr] = d.[intTTWegNr]\r\n")
                    .append("       WHERE a.[intAnzahlDerKommentare] >= ")
                    .append(intMinAnzahlDerKommentare)
                    .append("\r\n       AND a.[fltMittlereWegBewertung] >= ")
                    .append(intMittlereWegBewertung)
                    .append("\r\n       AND a.[WegName] LIKE ")
                    .append(DatabaseUtils.sqlEscapeString("%" + strTextSuchtext + "%"))
                    .append(strGebiet.equals(this.getString(R.string.strAll)) ? "       AND c.[strGebiet] != \"\" "
                            : "       AND c.[strGebiet] = '" + strGebiet + "'")
                    .append("\r\n		  AND a.[intTTGipfelNr] in (\r\n")
                    .append("       SELECT DISTINCT b.[intTTKletterWeg4Gipfel] from [TT_Route4SummitAND] b\r\n")
                    .append("       WHERE b.[intTTHauptGipfelNr] in (\r\n")
                    .append("       	   		SELECT DISTINCT c.[intTTGipfelNr] from [TT_Summit_AND] c)\r\n")
                    .append("           )\r\n")
                    .append("\r\n 	  AND a.[intTTGipfelNr] = c.[intTTGipfelNr]")
                    .append("\r\n       AND  coalesce(a.[sachsenSchwierigkeitsGrad], a.[ohneUnterstützungSchwierigkeitsGrad], " +
                            "a.[rotPunktSchwierigkeitsGrad]")
                    .append("              , a.[intSprungSchwierigkeitsGrad] ) BETWEEN ")
                    .append(intMinSchwierigkeit).append(" AND ")
                    .append(intMaxSchwierigkeit)
                    .append(" ORDER BY WegName")
                    .append(" Limit ")
                    .append(getResources().getInteger(R.integer.MaxNoItemQuerxy)).toString();
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
						Integer ohneUnterstuetzungSchwierigkeitsGrad = cursor
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
								ohneUnterstuetzungSchwierigkeitsGrad,
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
			Toast.makeText(getActivity(), lstTT_Routes_AND.size() + " Wege gefunden"
					+ ( lstTT_Routes_AND.size()  ==  getResources().getInteger(R.integer.MaxNoItemQuerxy) 
					? " (Maximalanzahl an Ergebnissen erreicht)" : ""), Toast.LENGTH_LONG)
			.show();
		}
	}

	@Override
	public void onClick(View v) {

        // handle click on the row (TextView) with the summit
        if ( v. getTag() != null ) {
            int intGipfelNr = (int)v.getTag();
            TT_Summit_AND tt_summit_and = new TT_Summit_AND(intGipfelNr , getContext() );
            Log.i(TAG, "onClick(View v) {... heads to summit: " + tt_summit_and.getStr_SummitName());
            Fragment tt_summitFoundFragment = TT_SummitFoundFragment.newInstance(tt_summit_and);
            ((MainActivity)getActivity()).replaceFragment(tt_summitFoundFragment, TT_SummitFoundFragment.ID);
        }
        // handle click on an item of the RecyclerView (R.layout.routes_lv_item_found)
        else {
            int itemPosition = mRecyclerView_RoutesFound.getChildLayoutPosition(v);
            TT_Route_AND tt_route_and = lstTT_Routes_AND.get(itemPosition);
            Log.i(TAG, "onClick(View v) {... for route: " + tt_route_and.getStrWegName());

            Fragment tt_summitFoundFragment = TT_RouteFoundFragment.newInstance(tt_route_and);
            ((MainActivity) getActivity()).replaceFragment(tt_summitFoundFragment, TT_RouteFoundFragment.ID);
        }
	}

//    // Event Listener
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Log.e(TAG, "onItemClick in TT_RoutesFoundFragment"
//                + view.getTag().toString() );
//        Log.i(TAG,"onItemClick(AdapterView<?> parent, View view, int position, long id) {...");
//
//        Fragment fragment = TT_RouteFoundFragment.newInstance(lstTT_Routes_AND.get(position) );
//        ((MainActivity)getActivity()).replaceFragment(fragment, TT_RouteFoundFragment.ID);
//    }
}
