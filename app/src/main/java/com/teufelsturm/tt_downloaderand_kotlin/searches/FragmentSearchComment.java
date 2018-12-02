package com.teufelsturm.tt_downloaderand_kotlin.searches;

import android.database.Cursor;
import android.os.Bundle;
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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.BubbleThumbRangeSeekbar;
import com.teufelsturm.tt_downloaderand_kotlin.OnFragmentReplaceListener;
import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.foundCommentsList.TT_CommentsFoundFragment;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumSachsenSchwierigkeitsGrad;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumTT_WegBewertung;

public class FragmentSearchComment extends FragmentSearchAbstract
		implements OnClickListener, OnSeekBarChangeListener {
    private static final String TAG = FragmentSearchComment.class.getSimpleName();

    private static int intMinLimitsForScale
            = EnumSachsenSchwierigkeitsGrad.getMinInteger();
	private static int intMaxLimitsForScale
            = EnumSachsenSchwierigkeitsGrad.getMaxInteger();
	private static int intMinCommentInComment
            = EnumTT_WegBewertung.getMinInteger();
	public int getEnumMinLimitsForScale() {
		return EnumSachsenSchwierigkeitsGrad.values()[intMinLimitsForScale].getValue();
	}
	public int getEnumMaxLimitsForScale() {
		return EnumSachsenSchwierigkeitsGrad.values()[intMaxLimitsForScale].getValue();
	}
	public int getIntMinCommentInComment() {
		return intMinCommentInComment;
	}
	private Spinner mySpinner;
	
	private RangeSeekBar<Integer> seekBarLimitsForCommentGrade;

//    public static FragmentSearchAbstract newInstance() {
//    	FragmentSearchAbstract instance = new FragmentSearchComment();
//        return instance;
//    }
    
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
		// Spinner 
		mySpinner = (Spinner)view.findViewById(R.id.spinnerAreaComment);
		loadSpinnerData(this.getActivity(), mySpinner);
		mySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				strGebiet = mySpinner.getSelectedItem().toString();
				Log.e(TAG,"mySpinner.getSelectedItem()" + strGebiet);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}

		});
		// SEARCH Button
		Button buttonSearchComment = view.findViewById(R.id.buttonSearchComments);
		buttonSearchComment.setOnClickListener(this);
		// ***************************************************************************************
		// create RangeSeekBar as Integer range between 0 and 125 (Route Grading
		// in Comment)
		BubbleThumbRangeSeekbar seekBarLimitsForCommentGrade
				= view.findViewById(R.id.rangeSeekbarLimitsForScale4CommentSearch);
        seekBarLimitsForCommentGrade.setMinValue(intMinLimitsForScale);
        seekBarLimitsForCommentGrade.setMaxValue(intMaxLimitsForScale);
        // set listener
        seekBarLimitsForCommentGrade.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
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
                ((TextView) view.findViewById(R.id.textViewLimitsForScale4CommentSearch))
                        .setText(strUpdate);
            }
        });

        // set final value listener
        seekBarLimitsForCommentGrade.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                // handle changed range values
                searchManager.setIntMinAnzahlDerWege(minValue.intValue());
                searchManager.setIntMaxAnzahlDerWege(maxValue.intValue());
            }
        });


		// ***************************************************************************************
		// alter the SeekBars (standard) in the layout
		SeekBar seekBarMinGradingInCommet 
			= view.findViewById(R.id.seekBarMinGradingInComment);

		seekBarMinGradingInCommet.setMax(EnumTT_WegBewertung.getMaxInteger()
				- EnumTT_WegBewertung.getMinInteger());
		seekBarMinGradingInCommet.setOnSeekBarChangeListener(this);
		Log.v(TAG, "3");
		// ***************************************************************************************
		// Refresh the text above the seekbar (Range)
		Log.v(TAG, "4");
		writeLimitsForScale(intMinLimitsForScale, intMaxLimitsForScale);
		// Refresh the text above the seekbar (Standard)
		Log.v(TAG, "5");
		writeLimitsForSeekBar(
				(TextView) view.findViewById(R.id.TextViewCommmentGrading), 0,
				R.string.strMinGradingInComment);
		Log.v(TAG, "fin");
		return view;
	}

	private void writeLimitsForScale(Integer minValue, Integer maxValue) {
		// handle changed range values
		String strUpdate = getString(R.string.strLimitForScale) + "\n("
				+ EnumSachsenSchwierigkeitsGrad.toStringFromSkaleOrdinal(minValue)
				+ " bis "
				+ EnumSachsenSchwierigkeitsGrad.toStringFromSkaleOrdinal(maxValue) + ")";
		// Log.v(FragmentSearchRoute.class.getSimpleName(),
		// "2");
		intMinLimitsForScale = minValue;
		intMaxLimitsForScale = maxValue;
		// Log.v(FragmentSearchRoute.class.getSimpleName(),
		// "3");
		((TextView) view
				.findViewById(R.id.textViewLimitsForScale4CommentSearch)).setText(strUpdate);
	}
	
	private void writeLimitsForSeekBar(TextView textView, Integer progress,
			Integer intRstringID) {
		// handle changed range values
		intMinCommentInComment = progress + EnumTT_WegBewertung.getMinInteger();
		Log.v(TAG, "writeLimitsForSeekBar --> "
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

		Fragment fragment = TT_CommentsFoundFragment.newInstance(
		        getStrTextSuchtext(), getStrtextViewGebiet(), getEnumMinLimitsForScale(),
                getEnumMaxLimitsForScale(), getIntMinCommentInComment() );


        ((OnFragmentReplaceListener) getActivity())
                .replaceFragment(fragment, TT_CommentsFoundFragment.ID);

	}
	@Override
	public void onResume() {
		myViewID = R.layout._main_activity__comment;
		// loadSpinnerData(this.getActivity(), mySpinner);
		myEditTextSuchtextID = R.id.editTextSuchtextKommentare;
		super.onResume();
	}
}
