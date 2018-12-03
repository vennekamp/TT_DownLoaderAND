package com.teufelsturm.tt_downloaderand_kotlin.searches;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.SimpleCursorAdapter.CursorToStringConverter;
import android.widget.Spinner;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.BubbleThumbRangeSeekbar;
import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.dbHelper.AutoCompleteDbAdapter;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumSachsenSchwierigkeitsGrad;

public abstract class FragmentSearchAbstract extends Fragment implements
		OnClickListener {
    private static final String TAG = FragmentSearchAbstract.class.getSimpleName();

	protected Integer myViewID;
	protected Integer myEditTextSuchtextID;
	protected AutoCompleteDbAdapter myAutoCompleteDbAdapter;
	protected AutoCompleteTextView myAutoCompleteTextView;
    protected Spinner mySpinner;;
	protected final int[] to = new int[] { android.R.id.text1 };
	protected String[] from;
	protected SimpleCursorAdapter adapter;

	protected SearchManager4FragmentSearches searchManager4FragmentSearches = SearchManager4FragmentSearches.getInstance();

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

    protected View createView(@NonNull LayoutInflater inflater,
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
		adapter.setCursorToStringConverter(new CursorToStringConverter() {
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

	public void onDestroy() {
		adapter.changeCursor(null);
		myAutoCompleteDbAdapter.close();
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeAllViews();
			}
		}
	}

	protected abstract Cursor getAutoCompleteCursor(CharSequence constraint);


	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.main, menu);
	}

	/**
	 * Function to load the spinner data from SQLite database
	 * */
	protected void loadAreaSpinnerData(Context context, Spinner mySpinner) {
		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context,
				android.R.layout.simple_spinner_item,
                SearchManager4FragmentSearches.getInstance().getAllAreaLabels(getContext()));
        /* simple_spinner_dropdown_item */
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

		// attaching data adapter to spinner
		mySpinner.setAdapter(dataAdapter);
		mySpinner.setSelection(searchManager4FragmentSearches.getMyAreaPositionFromSpinner());
	}

    protected void mySpinnerSetOnItemSelectedListener(final Spinner mySpinner) {
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG,"mySpinner.getSelectedItem()" + mySpinner.getSelectedItem().toString());
                searchManager4FragmentSearches.setMyAreaFromSpinner(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }


	protected void setListenerRangeSeekbarLimitsForScale4RouteSearch(
            BubbleThumbRangeSeekbar rangeSeekbarLimitsForScale4RouteSearch,
            final TextView textViewLimitsForScale) {
		rangeSeekbarLimitsForScale4RouteSearch.setMinValue(EnumSachsenSchwierigkeitsGrad.getMinInteger());
		rangeSeekbarLimitsForScale4RouteSearch.setMaxValue(EnumSachsenSchwierigkeitsGrad.getMaxInteger());
		rangeSeekbarLimitsForScale4RouteSearch
				.setMinStartValue(searchManager4FragmentSearches.getMinLimitsForDifficultyGrade())
				.setMaxStartValue(searchManager4FragmentSearches.getMaxLimitsForDifficultyGrade()).apply();

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
				searchManager4FragmentSearches.setMinLimitsForDifficultyGrade(minValue.intValue());
				searchManager4FragmentSearches.setMaxLimitsForDifficultyGrade(maxValue.intValue());
			}
		});
	}

	@Override
	public abstract void onClick(View v);
}
