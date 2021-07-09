package com.teufelsturm.tt_downloader3.searches;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.BubbleThumbRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.BubbleThumbSeekbar;
import com.teufelsturm.tt_downloader3.MainActivity;
import com.teufelsturm.tt_downloader3.R;
import com.teufelsturm.tt_downloader3.foundRoutesList.TT_RoutesFoundFragment;
import com.teufelsturm.tt_downloader3.tt_enums.EnumSachsenSchwierigkeitsGrad;
import com.teufelsturm.tt_downloader3.tt_enums.EnumTT_WegBewertung;

public class FragmentSearchRoute extends FragmentSearchAbstract
		implements OnClickListener {

	private static final String WEG_NAME = "WegName";

	@Override
	protected Cursor getAutoCompleteCursor(CharSequence constraint) {
		return myAutoCompleteDbAdapter
				.getAllRoutes(mViewModel,this,
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
        myAutoCompleteTextView.setText(mViewModel.getStrTextSuchtext4Route());
        myAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
				mViewModel.setStrTextSuchtext4Route(s.toString());
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
		BubbleThumbSeekbar seekBarNumberOfComments = view.findViewById(R.id.seekBarNumberOfComments);
		int progressSeekBarNumberOfComments = mViewModel.getIntMinNumberOfComments();
        seekBarNumberOfComments.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
			@Override
			public void valueChanged(Number value) {
				mViewModel.setIntMinNumberOfComments(value.intValue());
				writeLimitsForSeekBar(
						(TextView) view.findViewById(R.id.TextViewNumberOfComments),
						value, R.string.strNumberOfComments, false);

			}
		});
        seekBarNumberOfComments.setMinStartValue(progressSeekBarNumberOfComments).apply();
        // seekBarNumberOfComments.setProgress(R.integer.Zero);
		BubbleThumbSeekbar seekBarMinOfMeanRating = view.findViewById(R.id.seekBarMinOfMeanRating);
        float progressSeekBarMinOfMeanRating = mViewModel.getFloatMinOfMeanRating();
		seekBarMinOfMeanRating.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
			@Override
			public void valueChanged(Number value) {

				mViewModel.setFloatMinOfMeanRating(value.intValue());
				writeLimitsForSeekBar(
						(TextView) view.findViewById(R.id.TextViewMinOfMeanRating),
						value.floatValue() + EnumTT_WegBewertung.getMinInteger(),
						R.string.strMinOfMeanRating, true);
			}
		});
        seekBarMinOfMeanRating.setMinStartValue(progressSeekBarMinOfMeanRating).apply();
		// seekBarMinOfMeanRating.setProgress(R.integer.Zero);
		// ***************************************************************************************
		// Refresh the text above the seekbars
		writeLimitsForSeekBar((TextView) view.findViewById(R.id.TextViewNumberOfComments),
				mViewModel.getIntMinNumberOfComments(), R.string.strNumberOfComments, false);
        writeLimitsForSeekBar((TextView) view.findViewById(R.id.TextViewMinOfMeanRating),
				mViewModel.getFloatMinOfMeanRating(), R.string.strMinOfMeanRating, false);
		return view;
	}


	@Override
	public void onClick(View v) {
		//		startActivity(new Intent(getActivity(), _TT_RoutesFoundActivity.class));
		String strTextSuchtext =  mViewModel.getStrTextSuchtext4Route();
		String strGebiet =  mViewModel.getStrtextViewGebiet();
		int intMinSchwierigkeit = EnumSachsenSchwierigkeitsGrad.valuesFromSkaleOrdinal(
				mViewModel.getMinLimitsForDifficultyGrade()).getValue();
		int intMaxSchwierigkeit = EnumSachsenSchwierigkeitsGrad.valuesFromSkaleOrdinal(
				mViewModel.getMaxLimitsForDifficultyGrade()).getValue();
		int intMinAnzahlDerKommentare = mViewModel.getIntMinNumberOfComments();
		float intMittlereWegBewertung = mViewModel.getFloatMinOfMeanRating();

        Fragment fragment = TT_RoutesFoundFragment.newInstance(strTextSuchtext, strGebiet,
				intMinSchwierigkeit, intMaxSchwierigkeit, intMinAnzahlDerKommentare, intMittlereWegBewertung );

		((MainActivity) getActivity())
				.replaceFragment(fragment, TT_RoutesFoundFragment.ID);

	}

	// ***************************************************************************************
	// Helper for SeekBar action handler
    private void writeLimitsForSeekBar(TextView textView, Number progress,
                                         Integer intRstringID, boolean isEnumTT_WegBewertung) {
        // handle changed range values
        String strUpdate;
        if ( isEnumTT_WegBewertung ) {
            strUpdate = getString(intRstringID) + EnumTT_WegBewertung.getStrUpdate(progress);
        }
        else {
            strUpdate = getString(intRstringID) + progress.intValue();
        }
        textView.setText(strUpdate);
    }

	@Override
	public void onResume() {
		myViewID = R.layout._main_activity__route;
//		loadAreaSpinnerData(this.getActivity(), mySpinner);
		myEditTextSuchtextID = R.id.editTextSuchtextWege;
		super.onResume();
	}
}
