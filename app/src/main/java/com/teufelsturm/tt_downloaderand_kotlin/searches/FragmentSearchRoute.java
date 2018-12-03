package com.teufelsturm.tt_downloaderand_kotlin.searches;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.BubbleThumbRangeSeekbar;
import com.teufelsturm.tt_downloaderand_kotlin.MainActivity;
import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.foundRoutesList.TT_RoutesFoundFragment;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumSachsenSchwierigkeitsGrad;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumTT_WegBewertung;

public class FragmentSearchRoute extends FragmentSearchAbstract
		implements OnClickListener, OnSeekBarChangeListener {

	public static final String WEG_NAME = "WegName";

	@Override
	protected Cursor getAutoCompleteCursor(CharSequence constraint) {
		return myAutoCompleteDbAdapter
				.getAllRoutes(searchManager4FragmentSearches,this,
                        (constraint != null ? constraint.toString(): null));
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		myViewID = R.layout._main_activity__route;
		myEditTextSuchtextID = R.id.editTextSuchtextWege;
		from = new String[] {WEG_NAME};
		view = super.createView(inflater, container);
		// ***************************************************************************************
		// Define Action Listener
        myAutoCompleteTextView.setText(searchManager4FragmentSearches.getStrTextSuchtext4Route());
        myAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchManager4FragmentSearches.setStrTextSuchtext4Route(s.toString());
            }
        });
		// Spinner 
        super.mySpinner = view.findViewById(R.id.spinnerAreaRoute);
        super.loadAreaSpinnerData(this.getActivity(), mySpinner);
        super.mySpinnerSetOnItemSelectedListener(mySpinner);
		// SEARCH Button
		Button buttonSearchRoute = view.findViewById(R.id.buttonSearchRoute);
		buttonSearchRoute.setOnClickListener(this);
		// ***************************************************************************************
		// create RangeSeekBar as Integer range between 0 and Maxvalue
		BubbleThumbRangeSeekbar rangeSeekbarLimitsForScale4RouteSearch
				= view.findViewById(R.id.rangeSeekbarLimitsForScale4RouteSearch);
        // set listener
        super.setListenerRangeSeekbarLimitsForScale4RouteSearch(rangeSeekbarLimitsForScale4RouteSearch,
                ((TextView) view.findViewById(R.id.textViewLimitsForScale4RouteSearch)));
		// ***************************************************************************************
		// alter the SeekBars in the layout
		SeekBar seekBarNumberOfComments = view.findViewById(R.id.seekBarNumberOfComments);
		int progressSeekBarNumberOfComments = searchManager4FragmentSearches.getIntMinNumberOfComments();
        seekBarNumberOfComments.setOnSeekBarChangeListener(this);
        seekBarNumberOfComments.setProgress(progressSeekBarNumberOfComments);
        // seekBarNumberOfComments.setProgress(R.integer.Zero);
		SeekBar seekBarMinOfMeanRating = view.findViewById(R.id.seekBarMinOfMeanRating);
        int progressSeekBarMinOfMeanRating = searchManager4FragmentSearches.getIntMinOfMeanRating();
		seekBarMinOfMeanRating.setOnSeekBarChangeListener(this);
        seekBarMinOfMeanRating.setProgress(progressSeekBarMinOfMeanRating);
		// seekBarMinOfMeanRating.setProgress(R.integer.Zero);
		// ***************************************************************************************
		// Refresh the text above the seekbars
		writeLimitsForSeekBar((TextView) view.findViewById(R.id.TextViewNumberOfComments),
				searchManager4FragmentSearches.getIntMinNumberOfComments(), R.string.strNumberOfComments);
        writeLimitsForSeekBar((TextView) view.findViewById(R.id.TextViewMinOfMeanRating),
				searchManager4FragmentSearches.getIntMinOfMeanRating(), R.string.strMinOfMeanRating);
		return view;
	}


	@Override
	public void onClick(View v) {
		//		startActivity(new Intent(getActivity(), _TT_RoutesFoundActivity.class));
		String strTextSuchtext =  searchManager4FragmentSearches.getStrTextSuchtext4Route();
		String strGebiet =  searchManager4FragmentSearches.getStrtextViewGebiet();
		int intMinSchwierigkeit = EnumSachsenSchwierigkeitsGrad.valuesFromSkaleOrdinal(
                searchManager4FragmentSearches.getMinLimitsForDifficultyGrade()).getValue();
		int intMaxSchwierigkeit = EnumSachsenSchwierigkeitsGrad.valuesFromSkaleOrdinal(
                searchManager4FragmentSearches.getMaxLimitsForDifficultyGrade()).getValue();
		int intMinAnzahlDerKommentare = searchManager4FragmentSearches.getIntMinNumberOfComments();
		int intMittlereWegBewertung = searchManager4FragmentSearches.getIntMinOfMeanRating();

        Fragment fragment = TT_RoutesFoundFragment.newInstance(strTextSuchtext, strGebiet,
				intMinSchwierigkeit, intMaxSchwierigkeit, intMinAnzahlDerKommentare, intMittlereWegBewertung );

		((MainActivity) getActivity())
				.replaceFragment(fragment, TT_RoutesFoundFragment.ID);

	}

	// ***************************************************************************************
	// SeekBar action handler
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
//	    // return, if caused by e.g. rotation of device
//	    if (!fromUser ) return;
		if (seekBar.getId() == R.id.seekBarNumberOfComments) {
			searchManager4FragmentSearches.setIntMinNumberOfComments(progress);
			writeLimitsForSeekBar(
					(TextView) view.findViewById(R.id.TextViewNumberOfComments),
					progress, R.string.strNumberOfComments);

		} else if (seekBar.getId() == R.id.seekBarMinOfMeanRating) {
		    searchManager4FragmentSearches.setIntMinOfMeanRating(progress);
            writeLimitsForSeekBar(
					(TextView) view.findViewById(R.id.TextViewMinOfMeanRating),
					progress + EnumTT_WegBewertung.getMinInteger(),
					R.string.strMinOfMeanRating);
		}
	}

    protected void writeLimitsForSeekBar(TextView textView, Integer progress,
                                         Integer intRstringID) {
        // handle changed range values
        String strUpdate = getString(intRstringID) + progress;
        textView.setText(strUpdate);
    }
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onResume() {
		myViewID = R.layout._main_activity__route;
//		loadAreaSpinnerData(this.getActivity(), mySpinner);
		myEditTextSuchtextID = R.id.editTextSuchtextWege;
		super.onResume();
	}
}
