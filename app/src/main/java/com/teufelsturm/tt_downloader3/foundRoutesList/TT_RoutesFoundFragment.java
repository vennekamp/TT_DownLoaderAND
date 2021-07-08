package com.teufelsturm.tt_downloader3.foundRoutesList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teufelsturm.tt_downloader3.MainActivity;
import com.teufelsturm.tt_downloader3.R;
import com.teufelsturm.tt_downloader3.foundRouteSingle.TT_RouteFoundFragment;
import com.teufelsturm.tt_downloader3.foundSummitSingle.TT_SummitFoundFragment;
import com.teufelsturm.tt_downloader3.model.TT_Route_AND;
import com.teufelsturm.tt_downloader3.model.TT_Summit_AND;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TT_RoutesFoundFragment extends Fragment
		implements View.OnClickListener {
	private final static String TAG = TT_RoutesFoundFragment.class.getSimpleName();

	public static final String ID = "TT_RoutesFoundFragment";
    private static final String SUCH_TEXT = "SUCH_TEXT";
    private static final String SUCH_GEBIET = "SUCH_GEBIET";
    private static final String SUCH_MIN_SCHWIERIGKEIT= "SUCH_MIN_SCHWIERIGKEIT";
    private static final String SUCH_MAX_SCHWIERIGKEIT= "SUCH_MAX_SCHWIERIGKEIT";
    private static final String SUCH_MIN_ANZAHL_DER_KOMMENTARE = "SUCH_MIN_ANZAHL_DER_KOMMENTARE";
    private static final String SUCH_MITTLERE_WEG_BEWERTUNG = "SUCH_MITTLERE_WEG_BEWERTUNG";
    private TT_Route_ANDAdapter listenAdapter;


//	private ArrayList<TT_Route_AND> lstTT_Routes_AND;
//	private SQLiteDatabase newDB;
	private RecyclerView mRecyclerView_RoutesFound;
    private ViewModel4TT_RoutesFoundFragment mViewModel;

	public static Fragment newInstance(String strTextSuchtext,
                                       String strGebiet,
                                       int intMinSchwierigkeit,
                                       int intMaxSchwierigkeit,
                                       int intMinAnzahlDerKommentare,
                                       float floatMittlereWegBewertung ) {
        TT_RoutesFoundFragment f = new TT_RoutesFoundFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SUCH_TEXT, strTextSuchtext);
        bundle.putString(SUCH_GEBIET, strGebiet);
        bundle.putInt(SUCH_MIN_SCHWIERIGKEIT, intMinSchwierigkeit);
        bundle.putInt(SUCH_MAX_SCHWIERIGKEIT, intMaxSchwierigkeit);
        bundle.putInt(SUCH_MIN_ANZAHL_DER_KOMMENTARE, intMinAnzahlDerKommentare);
        bundle.putFloat(SUCH_MITTLERE_WEG_BEWERTUNG, floatMittlereWegBewertung);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.routes_activity_found_lv_list,
                container, false);
		Log.i(TAG,"Neuer openAndQueryDatabase... BEENDET!");
		Log.i(TAG,"Suche mRecyclerView_RoutesFound... ");
		mRecyclerView_RoutesFound
				= view.findViewById(R.id.recyclerview_in_routes_activity_found_lv_list);

        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView_RoutesFound.setLayoutManager(linearLayoutManager);

		Log.i(TAG,"mRecyclerView_RoutesFound.setAdapter...");
		// http://android-er.blogspot.de/2011/11/detect-swipe-using-simpleongestureliste.html
		// http://android-er.blogspot.de/2012/05/simple-example-use-osmdroid-and-slf4j.html
        Log.i(TAG,"Neuer onCreate... BEENDET");
		return view;
	}

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).showFAB(ID);
        mViewModel = ViewModelProviders.of(this).get(ViewModel4TT_RoutesFoundFragment.class);
		Bundle bundle = getArguments();
		mViewModel.strTextSuchtext = bundle.getString(SUCH_TEXT);
        mViewModel.strGebiet = bundle.getString(SUCH_GEBIET);
        mViewModel.intMinSchwierigkeit = bundle.getInt(SUCH_MIN_SCHWIERIGKEIT);
        mViewModel.intMaxSchwierigkeit = bundle.getInt(SUCH_MAX_SCHWIERIGKEIT);
        mViewModel.intMinAnzahlDerKommentare = bundle.getInt(SUCH_MIN_ANZAHL_DER_KOMMENTARE);
        mViewModel.floatMittlereWegBewertung = bundle.getFloat(SUCH_MITTLERE_WEG_BEWERTUNG);


        mViewModel.getLstTT_Route_AND().observe(this, new androidx.lifecycle.Observer<ArrayList<TT_Route_AND>>() {
            @Override
            public void onChanged(ArrayList<TT_Route_AND> tt_summit_ands) {
                listenAdapter = new TT_Route_ANDAdapter(TT_RoutesFoundFragment.this,
                        mViewModel.getLstTT_Route_AND().getValue(), false);
                mRecyclerView_RoutesFound.swapAdapter(listenAdapter, true);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.summits_found, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

	@Override
	public void onClick(View v) {

        // handle click on the row (TextView) with the summit
        if ( v. getTag() != null ) {
            int intGipfelNr = (int)v.getTag();
            TT_Summit_AND tt_summit_and = new TT_Summit_AND( intGipfelNr );
            Log.i(TAG, "onClick(View v) {... heads to summit: " + tt_summit_and.getStr_TTSummitName());
            Fragment tt_summitFoundFragment = TT_SummitFoundFragment.newInstance(tt_summit_and);
            ((MainActivity)getActivity()).replaceFragment(tt_summitFoundFragment, TT_SummitFoundFragment.ID);
        }
        // handle click on an item of the RecyclerView (R.layout.routes_lv_item_found)
        else {
            int itemPosition = mRecyclerView_RoutesFound.getChildLayoutPosition(v);
            TT_Route_AND tt_route_and = mViewModel.getLstTT_Route_AND().getValue().get(itemPosition);
            Log.i(TAG, "onClick(View v) {... for route: " + tt_route_and.getStrWegName());

            Fragment tt_summitFoundFragment = TT_RouteFoundFragment.newInstance(tt_route_and);
            ((MainActivity) getActivity()).replaceFragment(tt_summitFoundFragment, TT_RouteFoundFragment.ID);
        }
	}
}
