package com.teufelsturm.tt_downloaderand_kotlin;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.teufelsturm.tt_downloaderand_kotlin.routes.TT_RoutesFoundFragment;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumSachsenSchwierigkeitsGrad;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumTT_WegBewertung;

public class MainActivitySearchRoute extends MainActivitySearchAbstract
		implements OnClickListener, OnSeekBarChangeListener {

	private static int intMinLimitsForScale = EnumSachsenSchwierigkeitsGrad
			.getMinInteger();
	private static int intMaxLimitsForScale = EnumSachsenSchwierigkeitsGrad
			.getMaxInteger();
	public static int getEnumMinLimitsForScale() {
		return EnumSachsenSchwierigkeitsGrad.values()[intMinLimitsForScale]
				.getValue();
	}
	public static int getEnumMaxLimitsForScale() {
		return EnumSachsenSchwierigkeitsGrad.values()[intMaxLimitsForScale]
				.getValue();
	}
	private static int intMinNumberOfComments = 0;
	private static int intMinOfMeanRating = EnumTT_WegBewertung.getMinInteger();
	private RangeSeekBar<Integer> seekBarLimitsForScale;
	private Spinner mySpinner;

	public static int getIntMinNumberOfComments() {
		return intMinNumberOfComments;
	}

	public static int getIntMinOfMeanRating() {
		return intMinOfMeanRating;
	}

	@Override
	protected Cursor getAutoCompleteCursor(CharSequence constraint) {
		return myAutoCompleteDbAdapter
				.getAllRoutes((constraint != null ? constraint.toString()
						: null));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myViewID = R.layout._main_activity__route;
		myEditTextSuchtextID = R.id.editTextSuchtextWege;
		from = new String[] { "WegName" };
		view = super.onCreateView(inflater, container, savedInstanceState);
		// ***************************************************************************************
		// Define Action Listener
		// Spinner 
		mySpinner = (Spinner)view.findViewById(R.id.spinnerAreaRoute);
		loadSpinnerData(this.getActivity(), mySpinner);
		mySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {		
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				strGebiet = mySpinner.getSelectedItem().toString();
				Log.e(MainActivitySearchAbstract.class.getSimpleName(),
						"mySpinner.getSelectedItem()" + strGebiet);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}

		});
		// SEARCH Button
		Button buttonSearchRoute = (Button) view
				.findViewById(R.id.buttonSearchRoute);
		buttonSearchRoute.setOnClickListener(this);
		// ***************************************************************************************
		// create RangeSeekBar as Integer range between 0 and Macvalue
		seekBarLimitsForScale = new RangeSeekBar<Integer>(intMinLimitsForScale,
				intMaxLimitsForScale, getActivity());
		seekBarLimitsForScale
				.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
					@Override
					public void onRangeSeekBarValuesChanged(
							RangeSeekBar<?> bar, Integer minValue,
							Integer maxValue) {
						writeLimitsForScale(minValue, maxValue);
					}
				});
		// ***************************************************************************************
		// add RangeSeekBar to predefined layout
		ViewGroup layout_activity_seekBarLimitsForScale = (ViewGroup) view
				.findViewById(R.id.includeLimitsForScale4RouteSearch);
		LayoutParams layoutParamsSeekBarAnzahlDerWege = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParamsSeekBarAnzahlDerWege
				.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		layoutParamsSeekBarAnzahlDerWege.addRule(RelativeLayout.BELOW,
				R.id.textViewLimitsForScale);
		seekBarLimitsForScale.setLayoutParams(layoutParamsSeekBarAnzahlDerWege);
		layout_activity_seekBarLimitsForScale.addView(seekBarLimitsForScale);
		// ***************************************************************************************
		// alter the SeekBars in the layout
		SeekBar seekBarNumberOfComments = (SeekBar) view
				.findViewById(R.id.seekBarNumberOfComments);
		seekBarNumberOfComments.setOnSeekBarChangeListener(this);
		// seekBarNumberOfComments.setProgress(R.integer.Zero);
		SeekBar seekBarMinOfMeanRating = (SeekBar) view
				.findViewById(R.id.seekBarMinOfMeanRating);
		seekBarMinOfMeanRating.setOnSeekBarChangeListener(this);
		// seekBarMinOfMeanRating.setProgress(R.integer.Zero);
		// ***************************************************************************************
		// Refresh the text above the seekbars
		writeLimitsForScale(intMinLimitsForScale, intMaxLimitsForScale);
		writeLimitsForSeekBar(
				(TextView) view.findViewById(R.id.TextViewNumberOfComments),
				intMinNumberOfComments, R.string.strNumberOfComments);
		writeLimitsForSeekBar(
				(TextView) view.findViewById(R.id.TextViewMinOfMeanRating),
				intMinOfMeanRating, R.string.strMinOfMeanRating);
		return view;
	}

	private void writeLimitsForScale(Integer minValue, Integer maxValue) {
		Log.i(getClass().getSimpleName(),
				"writeLimitsForScale(Integer minValue..." + minValue
						+ " , Integer maxValue..." + maxValue);

		// handle changed range values
		String strUpdate = getString(R.string.strLimitForScale)
				+ "\n("
				+ EnumSachsenSchwierigkeitsGrad
						.toStringFromSkaleOrdinal(minValue)
				+ " bis "
				+ EnumSachsenSchwierigkeitsGrad
						.toStringFromSkaleOrdinal(maxValue) + ")";
		// Log.v(MainActivitySearchRoute.class.getSimpleName(),
		// "2");
		intMinLimitsForScale = minValue;
		intMaxLimitsForScale = maxValue;
		// Log.v(MainActivitySearchRoute.class.getSimpleName(),
		// "3");
		View viewLimitsForScale = view
				.findViewById(R.id.includeLimitsForScale4RouteSearch);
		((TextView) viewLimitsForScale
				.findViewById(R.id.textViewLimitsForScale)).setText(strUpdate);
	}

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

        Fragment fragment = new TT_RoutesFoundFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
	}

	// ***************************************************************************************
	// SeekBar action handler
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (seekBar.getId() == R.id.seekBarNumberOfComments) {
			intMinNumberOfComments = progress;
			writeLimitsForSeekBar(
					(TextView) view.findViewById(R.id.TextViewNumberOfComments),
					intMinNumberOfComments, R.string.strNumberOfComments);

		} else if (seekBar.getId() == R.id.seekBarMinOfMeanRating) {
			intMinOfMeanRating = progress;
			writeLimitsForSeekBar(
					(TextView) view.findViewById(R.id.TextViewMinOfMeanRating),
					intMinOfMeanRating + EnumTT_WegBewertung.getMinInteger(),
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
