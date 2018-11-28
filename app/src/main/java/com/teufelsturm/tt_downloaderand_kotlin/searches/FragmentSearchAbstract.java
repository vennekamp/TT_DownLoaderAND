package com.teufelsturm.tt_downloaderand_kotlin.searches;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;
import android.widget.Spinner;

import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.dbHelper.AutoCompleteDbAdapter;
import com.teufelsturm.tt_downloaderand_kotlin.dbHelper.DataBaseHelper;

import java.io.IOException;
import java.util.List;

public abstract class FragmentSearchAbstract extends Fragment implements
		OnClickListener {
    private static final String TAG = FragmentSearchAbstract.class.getSimpleName();

	protected Integer myViewID;
	protected Integer myEditTextSuchtextID;
	protected DataBaseHelper myDbHelper;
	protected AutoCompleteDbAdapter myAutoCompleteDbAdapter;
	protected AutoCompleteTextView myAutoCompleteTextView;
	protected String myAutoCompleteTextViewText;
	protected final int[] to = new int[] { android.R.id.text1 };
	protected String[] from;
	protected SimpleCursorAdapter adapter;

	protected static String strGebiet;
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
		return null;
	}

	public String getStrTextSuchtext() {
		// if (myAutoCompleteTextViewText != null)
		return myAutoCompleteTextViewText;
		// return "";
		// if (myAutoCompleteTextView == null)
		// myAutoCompleteTextView = (AutoCompleteTextView)
		// findViewById(FragmentSearchAbstract.myEditTextSuchtextXYZ);
		// return myAutoCompleteTextView.getText().toString();
	}

	public String getStrtextViewGebiet() {
		Log.e(FragmentSearchAbstract.class.getSimpleName(),
				"mySpinner.getSelectedItem()" + strGebiet);
		return strGebiet;
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
		myAutoCompleteTextViewText = myAutoCompleteTextView.getText().toString();
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
		myAutoCompleteTextView = view.findViewById(myEditTextSuchtextID);
		myAutoCompleteTextViewText = myAutoCompleteTextView.getText().toString();
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
	protected void loadSpinnerData(Context context, Spinner mySpinner) {
		// database handler
		myDbHelper = new DataBaseHelper(context);
		try {
			myDbHelper.createDataBase();

		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
		try {
			myDbHelper.openDataBase();
		} catch (SQLException sqle) {
			throw sqle;
		}
		List<String> getAllLabels = myDbHelper.getAllAreas();
		myDbHelper.close();
		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context,
				android.R.layout.simple_spinner_item, getAllLabels);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item); /* simple_spinner_dropdown_item */

		// attaching data adapter to spinner
		mySpinner.setAdapter(dataAdapter);
	}

	@Override
	public abstract void onClick(View v);
}
