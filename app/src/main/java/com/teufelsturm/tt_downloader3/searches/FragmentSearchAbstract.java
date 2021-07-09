package com.teufelsturm.tt_downloader3.searches;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.BubbleThumbRangeSeekbar;
import com.teufelsturm.tt_downloader3.R;
import com.teufelsturm.tt_downloader3.dbHelper.AutoCompleteDbAdapter;
import com.teufelsturm.tt_downloader3.tt_enums.EnumSachsenSchwierigkeitsGrad;

public abstract class FragmentSearchAbstract extends Fragment implements
		OnClickListener {
    private static final String TAG = FragmentSearchAbstract.class.getSimpleName();

	Integer myViewID;
	Integer myEditTextSuchtextID;
	AutoCompleteDbAdapter myAutoCompleteDbAdapter;
	AutoCompleteTextView myAutoCompleteTextView;
    Spinner mySpinner;
	protected final int[] to = new int[] { android.R.id.text1 };
	protected String[] from;
	private SimpleCursorAdapter adapter;

//	protected ViewModel4FragmentSearches searchManager4FragmentSearches = ViewModel4FragmentSearches.getInstance();
	ViewModel4FragmentSearches mViewModel;

	protected View view;

	public enum SearchType {
		SUMMIT, ROUTE, COMMENT
	}

	public static FragmentSearchAbstract newInstance(SearchType which) {
		switch (which) {
		case SUMMIT:
			return new FragmentSearchSummit();
		case ROUTE:
			return new FragmentSearchRoute();
		case COMMENT:
			return new FragmentSearchComment();
		}
		throw new IllegalArgumentException("Incorrect 'SearchType'");
	}

    View createView(@NonNull LayoutInflater inflater,
                              @Nullable ViewGroup container) {
		 if (container == null) {
             // We have different layouts, and in one of them this
             // fragment's containing frame doesn't exist. The fragment
             // may still be created from its saved state, but there is
             // no reason to try to create its view hierarchy because it
             // won't be displayed. Note this is not needed -- we could
             // just run the code below, where we would create and return
             // the view hierarchy; it would just never be used.
             return null;
		 }
		mViewModel = ViewModelProviders.of(getActivity()).get(ViewModel4FragmentSearches.class);
		view = inflater.inflate(myViewID, container, false);
		/* has to be of type "layout-ID" */
		Log.i(TAG,"getExternalStorageDirectory().getAbsolutePath()"
						+ Environment.getExternalStorageDirectory()
								.getAbsolutePath() + "context.getCacheDir()"
						+ getActivity().getCacheDir());
		// ***************************************************************************************
		// Create a SimpleCursorAdapter for the Search-Text-Field.
		// http://www.outofwhatbox.com/blog/2010/11/android-simpler-autocompletetextview-with-simplecursoradapter/
		adapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_dropdown_item_1line, null, from, to);
		myAutoCompleteDbAdapter = new AutoCompleteDbAdapter(getActivity());
		myAutoCompleteTextView = view.findViewById(myEditTextSuchtextID);
        myAutoCompleteTextView.setAdapter(adapter);
		// ***************************************************************************************
		// Set the CursorToStringConverter, to provide the labels for the
		// choices to be displayed in the AutoCompleteTextView.
		adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
			public String convertToString(Cursor cursor) {
				// Get the label for this row out of the "state" column
				// final int columnIndex =
				// cursor.getColumnIndexOrThrow("state");
				return cursor.getString(1);
			}
		});

		// Set the FilterQueryProvider, to run queries for choices
		// that match the specified input.
		adapter.setFilterQueryProvider(new FilterQueryProvider() {
			public Cursor runQuery(CharSequence constraint) {
				// Search for states whose names begin with the specified
				// letters.
				return getAutoCompleteCursor(constraint);
			}
		});
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	}
	public void onDestroy() {
		adapter.changeCursor(null);
//		myAutoCompleteDbAdapter.close();
		super.onDestroy();
	}

	protected abstract Cursor getAutoCompleteCursor(CharSequence constraint);


	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.main, menu);
	}

	/**
	 * Function to load the spinner data from SQLite database
	 * */
	void loadAreaSpinnerData(Context context, Spinner mySpinner) {
		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context,
				android.R.layout.simple_spinner_item,
                mViewModel.getAllAreaLabels());
        /* simple_spinner_dropdown_item */
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

		// attaching data adapter to spinner
		mySpinner.setAdapter(dataAdapter);
		mySpinner.setSelection(mViewModel.getMyAreaPositionFromSpinner());
	}

    void mySpinnerSetOnItemSelectedListener(final Spinner mySpinner) {
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG,"mySpinner.getSelectedItem()" + mySpinner.getSelectedItem().toString());
				mViewModel.setMyAreaFromSpinner(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }


	void setListenerRangeSeekbarLimitsForScale4RouteSearch(
            BubbleThumbRangeSeekbar rangeSeekbarLimitsForScale4RouteSearch,
            final TextView textViewLimitsForScale) {
		rangeSeekbarLimitsForScale4RouteSearch.setMinValue(EnumSachsenSchwierigkeitsGrad.getMinInteger());
		rangeSeekbarLimitsForScale4RouteSearch.setMaxValue(EnumSachsenSchwierigkeitsGrad.getMaxInteger());
		rangeSeekbarLimitsForScale4RouteSearch
				.setMinStartValue(mViewModel.getMinLimitsForDifficultyGrade())
				.setMaxStartValue(mViewModel.getMaxLimitsForDifficultyGrade()).apply();

		rangeSeekbarLimitsForScale4RouteSearch.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
			@Override
			public void valueChanged(Number minValue, Number maxValue) {
				// handle changed range values
				String strUpdate = getString(R.string.strLimitForScale)
						+ "\n(" + EnumSachsenSchwierigkeitsGrad.toStringFromSkaleOrdinal(minValue.intValue())
						+ " bis " + EnumSachsenSchwierigkeitsGrad.toStringFromSkaleOrdinal(maxValue.intValue()) + ")";
				textViewLimitsForScale.setText(strUpdate);
			}
		});
		// set final value listener
		rangeSeekbarLimitsForScale4RouteSearch.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
			@Override
			public void finalValue(Number minValue, Number maxValue) {
				// handle changed range values
				mViewModel.setMinLimitsForDifficultyGrade(minValue.intValue());
				mViewModel.setMaxLimitsForDifficultyGrade(maxValue.intValue());
			}
		});
	}

	@Override
	public abstract void onClick(View v);
}
