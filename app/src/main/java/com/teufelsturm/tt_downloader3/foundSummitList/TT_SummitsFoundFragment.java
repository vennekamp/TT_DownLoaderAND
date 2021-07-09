package com.teufelsturm.tt_downloader3.foundSummitList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teufelsturm.tt_downloader3.MainActivity;
import com.teufelsturm.tt_downloader3.R;
import com.teufelsturm.tt_downloader3.foundSummitSingle.TT_SummitFoundFragment;
import com.teufelsturm.tt_downloader3.model.TT_Summit_AND;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TT_SummitsFoundFragment extends Fragment
		implements View.OnClickListener {

	public static final String ID = "TT_SummitsFoundFragment";
    private final static String TAG = TT_SummitsFoundFragment.class.getSimpleName();
    private static final String SUCH_TEXT = "SUCH_TEXT";
    private static final String SUCH_GEBIET = "SUCH_GEBIET";
    private static final String SUCH_MIN_ANZAHL_DER_WEGE = "SUCH_MIN_ANZAHL_DER_WEGE";
    private static final String SUCH_MAX_ANZAHL_DER_WEGE = "SUCH_MAX_ANZAHL_DER_WEGE";
    private static final String SUCH_MIN_ANZAHL_DER_STERNCHEN_WEGE= "SUCH_MIN_ANZAHL_DER_STERNCHEN_WEGE";
    private static final String SUCH_MAX_ANZAHL_DER_STERNCHEN_WEGE= "SUCH_MAX_ANZAHL_DER_STERNCHEN_WEGE";

    private TT_Summit_ANDAdapter listenAdapter;
    private RecyclerView mRecyclerView;
	private ViewModel4TT_SummitsFoundFragment mViewModel;

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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.summits_activity_found_lv_list,
                container, false);
        assert getArguments() != null;
		mViewModel = ViewModelProviders.of(this).get(ViewModel4TT_SummitsFoundFragment.class);
		mViewModel.strTextSuchtext = getArguments().getString(SUCH_TEXT);
		mViewModel.strGebiet = getArguments().getString(SUCH_GEBIET);
		mViewModel.intMinAnzahlDerWege = getArguments().getInt(SUCH_MIN_ANZAHL_DER_WEGE);
		mViewModel.intMaxAnzahlDerWege = getArguments().getInt(SUCH_MAX_ANZAHL_DER_WEGE);
		mViewModel.intMinAnzahlDerSternchenWege = getArguments().getInt(SUCH_MIN_ANZAHL_DER_STERNCHEN_WEGE);
		mViewModel.intMaxAnzahlDerSternchenWege = getArguments().getInt(SUCH_MAX_ANZAHL_DER_STERNCHEN_WEGE);

        mViewModel.getLstTT_Route_AND().observe(this, new androidx.lifecycle.Observer<ArrayList<TT_Summit_AND>>() {
            @Override
            public void onChanged(ArrayList<TT_Summit_AND> tt_summit_ands) {
                listenAdapter = new TT_Summit_ANDAdapter(TT_SummitsFoundFragment.this,
                        mViewModel.getLstTT_Route_AND().getValue());
                mRecyclerView.setAdapter(listenAdapter);
                listenAdapter.notifyDataSetChanged();
            }
        });

		Log.i(TAG,"Neuer openAndQueryDatabase... BEENDET!");

		Log.i(TAG, "Suche meinListView... ");
        mRecyclerView = view.findViewById(R.id.list_summits_activity_found_lv_list);
		Log.i(TAG, "meinListView.setAdapter...");

        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);


		// http://android-er.blogspot.de/2011/11/detect-swipe-using-simpleongestureliste.html
		// TODO: OSM-Map einblenden
		// http://android-er.blogspot.de/2012/05/simple-example-use-osmdroid-and-slf4j.html

		Log.i(TAG, "Neuer onCreate... BEENDET");
        return view;
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

    @Override
    public void onClick(View v) {
        Log.i(TAG,"Start TT_RouteFoundFragment... ");

        int itemPosition = mRecyclerView.getChildLayoutPosition(v);
        TT_Summit_AND tt_summit_andNext  = mViewModel.getLstTT_Route_AND().getValue().get(itemPosition);
        TT_SummitFoundFragment tt_summitFoundFragment =
                TT_SummitFoundFragment.newInstance(tt_summit_andNext);
        ((MainActivity)getActivity()).replaceFragment(tt_summitFoundFragment, TT_SummitFoundFragment.ID);
    }
}
