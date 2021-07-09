package com.teufelsturm.tt_downloaderand_kotlin.searches;

import android.database.Cursor;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.crystal.crystalrangeseekbar.widgets.BubbleThumbRangeSeekbar;
import com.teufelsturm.tt_downloader3.R;
import com.teufelsturm.tt_downloaderand_kotlin.OnFragmentReplaceListener;
import com.teufelsturm.tt_downloaderand_kotlin.foundCommentsList.TT_CommentsFoundFragment;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumSachsenSchwierigkeitsGrad;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumTT_WegBewertung;

public class FragmentSearchComment extends FragmentSearchAbstract
		implements OnClickListener, OnSeekBarChangeListener {
    private static final String TAG = FragmentSearchComment.class.getSimpleName();

	@Override
	protected Cursor getAutoCompleteCursor(CharSequence constraint) {
		return myAutoCompleteDbAdapter
				.getAllComments((constraint != null ? constraint.toString() : null));
	}

		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
		myViewID = R.layout._main_activity__comment;
		myEditTextSuchtextID = R.id.editTextSuchtextKommentare;
		from = new String[] { "strAutoCompleteText" };
		view = super.createView(inflater, container);
		// ***************************************************************************************
		// Define Action Listener
			ViewModel4FragmentSearches searches = ViewModelProviders.of(getActivity()).get(ViewModel4FragmentSearches.class);

			myAutoCompleteTextView.setText(searches.getStrTextSuchtext4Comment());
        myAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
				ViewModel4FragmentSearches searches = ViewModelProviders.of(getActivity()).get(ViewModel4FragmentSearches.class);

				searches.setStrTextSuchtext4Comment(s.toString());
            }
        });
		// Spinner 
		mySpinner = view.findViewById(R.id.spinnerAreaComment);
		super.loadAreaSpinnerData(this.getActivity(), mySpinner);
		mySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				ViewModel4FragmentSearches searchManager4FragmentSearches = ViewModelProviders.of(getActivity()).get(ViewModel4FragmentSearches.class);
				searchManager4FragmentSearches.setMyAreaFromSpinner(position);
				Log.e(TAG,"mySpinner.getSelectedItem()" + mySpinner.getSelectedItem().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}

		});
		// SEARCH Button
		Button buttonSearchComment = view.findViewById(R.id.buttonSearchComments);
		buttonSearchComment.setOnClickListener(this);
		// ***************************************************************************************
		// create RangeSeekBar as Integer range between 0 and 125 (Route Grading
		// in Comment)
		BubbleThumbRangeSeekbar rangeSeekbarLimitsForScale4CommentSearch
				= view.findViewById(R.id.rangeSeekbarLimitsForScale4CommentSearch);
        // set listener
        super.setListenerRangeSeekbarLimitsForScale4RouteSearch(rangeSeekbarLimitsForScale4CommentSearch,
                ((TextView) view.findViewById(R.id.textViewLimitsForScale44CommentSearch)));

		// ***************************************************************************************
		// alter the SeekBars (standard) in the layout
		SeekBar seekBarMinGradingInCommet = view.findViewById(R.id.seekBarMinGradingInComment);
			ViewModel4FragmentSearches searchManager4FragmentSearches = ViewModelProviders.of(getActivity()).get(ViewModel4FragmentSearches.class);
			int progressSeekBarMinGradingInCommet = searchManager4FragmentSearches.getMinGradingOfCommet();
		seekBarMinGradingInCommet.setMax(EnumTT_WegBewertung.getMaxInteger() - EnumTT_WegBewertung.getMinInteger());
		seekBarMinGradingInCommet.setOnSeekBarChangeListener(this);
		seekBarMinGradingInCommet.setProgress(progressSeekBarMinGradingInCommet);

		// ***************************************************************************************
		// Refresh the text above the seekbar (Range)
		Log.v(TAG, "4");
		writeLimitsForSeekBar( (TextView) view.findViewById(R.id.TextViewCommmentGrading),
                progressSeekBarMinGradingInCommet,
				R.string.strMinGradingInComment);
		Log.v(TAG, "fin");
		return view;
	}

	// ***************************************************************************************
	// SeekBar action handler
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		ViewModel4FragmentSearches searchManager4FragmentSearches = ViewModelProviders.of(getActivity()).get(ViewModel4FragmentSearches.class);
		searchManager4FragmentSearches.setMinGradingOfCommet(progress);
		writeLimitsForSeekBar(
				(TextView) view.findViewById(R.id.TextViewCommmentGrading),
				progress, R.string.strMinGradingInComment);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "Diese Suche kann etwas dauern...", Toast.LENGTH_SHORT)
				.show();
		ViewModel4FragmentSearches searchManager4FragmentSearches = ViewModelProviders.of(getActivity()).get(ViewModel4FragmentSearches.class);

		Fragment fragment = TT_CommentsFoundFragment.newInstance(
                searchManager4FragmentSearches.getStrTextSuchtext4Comment(),
                searchManager4FragmentSearches.getStrtextViewGebiet(),
                EnumSachsenSchwierigkeitsGrad.valuesFromSkaleOrdinal(searchManager4FragmentSearches.getMinLimitsForDifficultyGrade()).getValue(),
                EnumSachsenSchwierigkeitsGrad.valuesFromSkaleOrdinal(searchManager4FragmentSearches.getMaxLimitsForDifficultyGrade()).getValue(),
                searchManager4FragmentSearches.getMinGradingOfCommet() );


        ((OnFragmentReplaceListener) getActivity())
                .replaceFragment(fragment, TT_CommentsFoundFragment.ID);

	}


    protected void writeLimitsForSeekBar(TextView textView, Integer progress,
                                         Integer intRstringID) {
        // handle changed range values
        String strUpdate = getString(intRstringID) +
                 EnumTT_WegBewertung.values()[progress].toString();
        textView.setText(strUpdate);
    }

	@Override
	public void onResume() {
		myViewID = R.layout._main_activity__comment;
		// loadAreaSpinnerData(this.getActivity(), mySpinner);
		myEditTextSuchtextID = R.id.editTextSuchtextKommentare;
		super.onResume();
	}
}
