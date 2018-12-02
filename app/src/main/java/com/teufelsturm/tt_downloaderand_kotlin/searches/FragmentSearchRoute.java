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

	private int intMinLimitsForScale = EnumSachsenSchwierigkeitsGrad
			.getMinInteger();
	private int intMaxLimitsForScale = EnumSachsenSchwierigkeitsGrad
			.getMaxInteger();
	public int getEnumMinLimitsForScale() {
		return EnumSachsenSchwierigkeitsGrad.values()[intMinLimitsForScale]
				.getValue();
	}
	public int getEnumMaxLimitsForScale() {
		return EnumSachsenSchwierigkeitsGrad.values()[intMaxLimitsForScale]
				.getValue();
	}
	private int intMinNumberOfComments = 0;
	private int intMinOfMeanRating = EnumTT_WegBewertung.getMinInteger();
	private Spinner mySpinner;

	public int getIntMinNumberOfComments() {
		return intMinNumberOfComments;
	}

	public int getIntMinOfMeanRating() {
		return intMinOfMeanRating;
	}

	@Override
	protected Cursor getAutoCompleteCursor(CharSequence constraint) {
		return myAutoCompleteDbAdapter
				.getAllRoutes(this, (constraint != null ? constraint.toString()
						: null));
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
        seekBarLimitsForScale.setMinValue(intMinLimitsForScale);
        seekBarLimitsForScale.setMaxValue(intMaxLimitsForScale);
        // set listener
        seekBarLimitsForScale.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                // handle changed range values
                String strUpdate = getString(R.string.strLimitForScale)
                        + "\n("
                        + EnumSachsenSchwierigkeitsGrad
                        .toStringFromSkaleOrdinal(minValue.intValue())
                        + " bis "
                        + EnumSachsenSchwierigkeitsGrad
                        .toStringFromSkaleOrdinal(maxValue.intValue()) + ")";
                ((TextView) view.findViewById(R.id.textViewLimitsForScale4RouteSearch))
                        .setText(strUpdate);
            }
        });

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
		// Log.v(FragmentSearchRoute.class.getSimpleName(),
		// "2");
		intMinLimitsForScale = minValue;
		intMaxLimitsForScale = maxValue;
		// Log.v(FragmentSearchRoute.class.getSimpleName(),
		// "3");
		((TextView) view
				.findViewById(R.id.textViewLimitsForScale4RouteSearch)).setText(strUpdate);
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
		String strTextSuchtext = getStrTextSuchtext();
		String strGebiet = getStrtextViewGebiet();
		int intMinSchwierigkeit = getEnumMinLimitsForScale();
		int intMaxSchwierigkeit = getEnumMaxLimitsForScale();
		int intMinAnzahlDerKommentare = getIntMinNumberOfComments();
		int intMittlereWegBewertung = getIntMinOfMeanRating();

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
