package com.teufelsturm.tt_downloaderand_kotlin.searches;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
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

	private Spinner mySpinner;


	@Override
	protected Cursor getAutoCompleteCursor(CharSequence constraint) {
		return myAutoCompleteDbAdapter
				.getAllRoutes(searchManager,this,
                        (constraint != null ? constraint.toString(): null));
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		myViewID = R.layout._main_activity__route;
		myEditTextSuchtextID = R.id.editTextSuchtextWege;
		from = new String[] { "WegName" };
		view = super.createView(inflater, container);
		// ***************************************************************************************
		// Define Action Listener
		// Spinner 
		mySpinner = view.findViewById(R.id.spinnerAreaRoute);
		loadSpinnerData(this.getActivity(), mySpinner);
		mySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {		
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				strGebiet = mySpinner.getSelectedItem().toString();
				Log.e(FragmentSearchAbstract.class.getSimpleName(),
						"mySpinner.getSelectedItem()" + strGebiet);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}

		});
		// SEARCH Button
		Button buttonSearchRoute = view.findViewById(R.id.buttonSearchRoute);
		buttonSearchRoute.setOnClickListener(this);
		// ***************************************************************************************
		// create RangeSeekBar as Integer range between 0 and Maxvalue

		BubbleThumbRangeSeekbar seekBarLimitsForScale
				= view.findViewById(R.id.rangeSeekbarLimitsForScale4RouteSearch);
        seekBarLimitsForScale.setMinValue(searchManager.getEnumMinLimitsForScale());
        seekBarLimitsForScale.setMaxValue(searchManager.getEnumMaxLimitsForScale());
        // ***************************************************************************************
        // Refresh the text above the seekbars
        // set listener
//        seekBarLimitsForScale.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
//            @Override
//            public void valueChanged(Number minValue, Number maxValue) {
//                // handle changed range values
//                String strUpdate = getString(R.string.strLimitForScale)
//                        + "\n("
//                        + EnumSachsenSchwierigkeitsGrad
//                        .toStringFromSkaleOrdinal(minValue.intValue())
//                        + " bis "
//                        + EnumSachsenSchwierigkeitsGrad
//                        .toStringFromSkaleOrdinal(maxValue.intValue()) + ")";
//                ((TextView) view.findViewById(R.id.textViewLimitsForScale4RouteSearch))
//                        .setText(strUpdate);
//            }
//        });

        // set final value listener
        seekBarLimitsForScale.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                // handle changed range values
                searchManager.setIntMinAnzahlDerWege(minValue.intValue());
                searchManager.setIntMaxAnzahlDerWege(maxValue.intValue());
            }
        });
		// ***************************************************************************************
		// alter the SeekBars in the layout
		SeekBar seekBarNumberOfComments = view
				.findViewById(R.id.seekBarNumberOfComments);
		seekBarNumberOfComments.setOnSeekBarChangeListener(this);
		// seekBarNumberOfComments.setProgress(R.integer.Zero);
		SeekBar seekBarMinOfMeanRating = view
				.findViewById(R.id.seekBarMinOfMeanRating);
		seekBarMinOfMeanRating.setOnSeekBarChangeListener(this);
		// seekBarMinOfMeanRating.setProgress(R.integer.Zero);
		// ***************************************************************************************
		// Refresh the text above the seekbars
		writeLimitsForSeekBar(
				(TextView) view.findViewById(R.id.TextViewNumberOfComments),
				searchManager.getIntMinNumberOfComments(), R.string.strNumberOfComments);
		writeLimitsForSeekBar(
				(TextView) view.findViewById(R.id.TextViewMinOfMeanRating),
				searchManager.getIntMinOfMeanRating(), R.string.strMinOfMeanRating);
		return view;
	}

//	private void writeLimitsForScale(int minValue, int maxValue) {
//		Log.i(getClass().getSimpleName(),
//				"writeLimitsForScale(Integer minValue..." + minValue
//						+ " , Integer maxValue..." + maxValue);
//
//		// handle changed range values
//		String strUpdate = getString(R.string.strLimitForScale)
//				+ "\n("
//				+ EnumSachsenSchwierigkeitsGrad
//						.toStringFromSkaleOrdinal(minValue)
//				+ " bis "
//				+ EnumSachsenSchwierigkeitsGrad
//						.toStringFromSkaleOrdinal(maxValue) + ")";
//		// Log.v(FragmentSearchRoute.class.getSimpleName(),
//		// "2");
//		searchManager.setIntMinNumberOfComments(minValue.intValue());
//		intMaxLimitsForScale = maxValue;
//		// Log.v(FragmentSearchRoute.class.getSimpleName(),
//		// "3");
//		((TextView) view
//				.findViewById(R.id.textViewLimitsForScale4RouteSearch)).setText(strUpdate);
//	}

	private void writeLimitsForSeekBar(TextView textView, Integer progress,
			Integer intRstringID) {
		// handle changed range values
		String strUpdate = getString(intRstringID) + progress;
		textView.setText(strUpdate);
	}

	@Override
	public void onClick(View v) {
		Log.i(getClass().getSimpleName(), "onClick gedrückt...");
		myEditTextSuchtextID = R.id.editTextSuchtextWege;
		myAutoCompleteTextViewText = ((AutoCompleteTextView) view
				.findViewById(myEditTextSuchtextID)).getText().toString();
//		startActivity(new Intent(getActivity(), _TT_RoutesFoundActivity.class));
		String strTextSuchtext = getStrTextSuchtext();
		String strGebiet = getStrtextViewGebiet();
		int intMinSchwierigkeit = searchManager.getEnumMinLimitsForScale();
		int intMaxSchwierigkeit = searchManager.getEnumMaxLimitsForScale();
		int intMinAnzahlDerKommentare = searchManager.getIntMinNumberOfComments();
		int intMittlereWegBewertung = searchManager.getIntMinOfMeanRating();

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
		if (seekBar.getId() == R.id.seekBarNumberOfComments) {
			searchManager.setIntMinNumberOfComments(progress);
			writeLimitsForSeekBar(
					(TextView) view.findViewById(R.id.TextViewNumberOfComments),
					progress, R.string.strNumberOfComments);

		} else if (seekBar.getId() == R.id.seekBarMinOfMeanRating) {
		    searchManager.setIntMinOfMeanRating(progress);
			writeLimitsForSeekBar(
					(TextView) view.findViewById(R.id.TextViewMinOfMeanRating),
					progress + EnumTT_WegBewertung.getMinInteger(),
					R.string.strMinOfMeanRating);
		}
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
//		loadSpinnerData(this.getActivity(), mySpinner);
		myEditTextSuchtextID = R.id.editTextSuchtextWege;
		super.onResume();
	}
}
