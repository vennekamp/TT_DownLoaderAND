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
import android.widget.Toast;

import com.teufelsturm.tt_downloaderand_kotlin.comments.TT_CommentsFoundActivity;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumSachsenSchwierigkeitsGrad;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumTT_WegBewertung;

public class MainActivitySearchComment extends MainActivitySearchAbstract
		implements OnClickListener, OnSeekBarChangeListener {
	private static int intMinLimitsForScale = EnumSachsenSchwierigkeitsGrad
			.getMinInteger();
	private static int intMaxLimitsForScale = EnumSachsenSchwierigkeitsGrad
			.getMaxInteger();
	private static int intMinCommentInComment = EnumTT_WegBewertung
			.getMinInteger();
	public static int getEnumMinLimitsForScale() {
		return EnumSachsenSchwierigkeitsGrad.values()[intMinLimitsForScale].getValue();
	}
	public static int getEnumMaxLimitsForScale() {
		return EnumSachsenSchwierigkeitsGrad.values()[intMaxLimitsForScale].getValue();
	}
	public static int getIntMinCommentInComment() {
		return intMinCommentInComment;
	}
	private Spinner mySpinner;
	
	private RangeSeekBar<Integer> seekBarLimitsForCommentGrade;

//    public static MainActivitySearchAbstract newInstance() {
//    	MainActivitySearchAbstract instance = new MainActivitySearchComment();
//        return instance;
//    }
    
	@Override
	protected Cursor getAutoCompleteCursor(CharSequence constraint) {
		return myAutoCompleteDbAdapter
				.getAllComments((constraint != null ? constraint.toString()
						: null));
	}

		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
		myViewID = R.layout._main_activity__comment;
		myEditTextSuchtextID = R.id.editTextSuchtextKommentare;
		from = new String[] { "strAutoCompleteText" };
		view = super.onCreateView(inflater, container, savedInstanceState);
		// ***************************************************************************************
		// Define Action Listener
		// Spinner 
		mySpinner = (Spinner)view.findViewById(R.id.spinnerAreaComment);
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
		Button buttonSearchComment = (Button) view.findViewById(R.id.buttonSearchComments);
		buttonSearchComment.setOnClickListener(this);
		// ***************************************************************************************
		// create RangeSeekBar as Integer range between 0 and 125 (Route Grading
		// in Comment)
		seekBarLimitsForCommentGrade = new RangeSeekBar<Integer>(
				intMinLimitsForScale, intMaxLimitsForScale, getActivity());
		seekBarLimitsForCommentGrade
				.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
					@Override
					public void onRangeSeekBarValuesChanged(
							RangeSeekBar<?> bar, Integer minValue,
							Integer maxValue) {

						Log.v(getClass().getSimpleName(),
								"writeLimitsForScale(minValue, maxValue); --> "
										+ minValue + "  " + maxValue);
						writeLimitsForScale(minValue, maxValue);
					}
				});
		Log.v(getClass().getSimpleName(), "1");
		// ***************************************************************************************
		// add RangeSeekBar to predefined layout
		ViewGroup layout_activity_seekBarLimitsForScale 
			= (ViewGroup) view.findViewById(R.id.includeLimitsForScale4CommentSearch);
		LayoutParams layoutParamsSeekBarAnzahlDerWege = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParamsSeekBarAnzahlDerWege
				.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		layoutParamsSeekBarAnzahlDerWege.addRule(RelativeLayout.BELOW,
				R.id.textViewLimitsForScale);
		seekBarLimitsForCommentGrade
				.setLayoutParams(layoutParamsSeekBarAnzahlDerWege);
		layout_activity_seekBarLimitsForScale
				.addView(seekBarLimitsForCommentGrade);
		Log.v(getClass().getSimpleName(), "2");
		// ***************************************************************************************
		// alter the SeekBars (standard) in the layout
		SeekBar seekBarMinGradingInCommet 
			= (SeekBar) view.findViewById(R.id.seekBarMinGradingInComment);

		seekBarMinGradingInCommet.setMax(EnumTT_WegBewertung.getMaxInteger()
				- EnumTT_WegBewertung.getMinInteger());
		seekBarMinGradingInCommet.setOnSeekBarChangeListener(this);
		Log.v(getClass().getSimpleName(), "3");
		// ***************************************************************************************
		// Refresh the text above the seekbar (Range)
		Log.v(getClass().getSimpleName(), "4");
		writeLimitsForScale(intMinLimitsForScale, intMaxLimitsForScale);
		// Refresh the text above the seekbar (Standard)
		Log.v(getClass().getSimpleName(), "5");
		writeLimitsForSeekBar(
				(TextView) view.findViewById(R.id.TextViewCommmentGrading), 0,
				R.string.strMinGradingInComment);
		Log.v(getClass().getSimpleName(), "fin");
		return view;
	}

	private void writeLimitsForScale(Integer minValue, Integer maxValue) {
		// handle changed range values
		String strUpdate = getString(R.string.strLimitForScale) + "\n("
				+ EnumSachsenSchwierigkeitsGrad.toStringFromSkaleOrdinal(minValue)
				+ " bis "
				+ EnumSachsenSchwierigkeitsGrad.toStringFromSkaleOrdinal(maxValue) + ")";
		// Log.v(MainActivitySearchRoute.class.getSimpleName(),
		// "2");
		intMinLimitsForScale = minValue;
		intMaxLimitsForScale = maxValue;
		// Log.v(MainActivitySearchRoute.class.getSimpleName(),
		// "3");
		Log.v(getClass().getSimpleName(), "4.1");
		View viewLimitsForScale = 
				view.findViewById(R.id.includeLimitsForScale4CommentSearch);
		Log.v(getClass().getSimpleName(), "4.2");
		((TextView) viewLimitsForScale
				.findViewById(R.id.textViewLimitsForScale)).setText(strUpdate);
	}
	
	private void writeLimitsForSeekBar(TextView textView, Integer progress,
			Integer intRstringID) {
		// handle changed range values
		intMinCommentInComment = progress + EnumTT_WegBewertung.getMinInteger();
		Log.v(getClass().getSimpleName(), "writeLimitsForSeekBar --> "
				+ intMinCommentInComment + "  progress --> " + progress
				+ "  EnumTT_WegBewertung.getMinInteger() --> "
				+ EnumTT_WegBewertung.getMinInteger());
		String strUpdate = getString(intRstringID) + " "
				+ intMinCommentInComment;
		textView.setText(strUpdate);
	}

	// ***************************************************************************************
	// SeekBar action handler
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
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
		myEditTextSuchtextID = R.id.editTextSuchtextKommentare;
		myAutoCompleteTextViewText
			= ((AutoCompleteTextView)view.findViewById(myEditTextSuchtextID)).getText().toString();

		Fragment fragment = new TT_CommentsFoundActivity();
		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_container, fragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
//		startActivity(new Intent(getActivity(), _TT_CommentsFoundActivity.class));
	}
	@Override
	public void onResume() {
		myViewID = R.layout._main_activity__comment;
		// loadSpinnerData(this.getActivity(), mySpinner);
		myEditTextSuchtextID = R.id.editTextSuchtextKommentare;
		super.onResume();
	}
}
