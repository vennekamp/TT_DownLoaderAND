package com.teufelsturm.tt_downloaderand_kotlin.searches;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.teufelsturm.tt_downloaderand_kotlin.MainActivity;
import com.teufelsturm.tt_downloaderand_kotlin.OnFragmentInteractionListener;
import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.dbHelper.DB_BackUp2SDCard;
import com.teufelsturm.tt_downloaderand_kotlin.foundSummitList.TT_SummitsFoundFragment;

public class FragmentSearchSummit extends FragmentSearchAbstract
		implements OnClickListener {
	private static final String TAG = FragmentSearchSummit.class.getSimpleName();

	private static int intMinAnzahlDerWege = 0;
	private static int intMaxAnzahlDerWege = 100;
	private static int intMinAnzahlDerSternchenWege = 0;
	private static int intMaxAnzahlDerSternchenWege = 50;
	private Spinner mySpinner;

	public int getIntMinAnzahlDerWege() {
		return intMinAnzahlDerWege;
	}

	public int getIntMaxAnzahlDerWege() {
		return intMaxAnzahlDerWege;
	}

	public int getIntMinAnzahlDerSternchenWege() {
		return intMinAnzahlDerSternchenWege;
	}

	public int getIntMaxAnzahlDerSternchenWege() {
		return intMaxAnzahlDerSternchenWege;
	}

	@Override
	protected Cursor getAutoCompleteCursor(CharSequence constraint) {
		return myAutoCompleteDbAdapter
				.getAllSummits(this, (constraint != null ? constraint.toString()
						: null));
	}


    @Override
	public View onCreateView(@NonNull LayoutInflater inflater,
							  ViewGroup container,
                              Bundle savedInstanceState) {
		myViewID = R.layout._main_activity__summit;
		myEditTextSuchtextID = R.id.editTextSuchtextGipfel;
		from = new String[] { "strName" };
		view = super.createView(inflater, container);
		// ***************************************************************************************
		// Define Action Listener
		// Spinner 
		mySpinner = view.findViewById(R.id.spinnerAreaSummit);
		loadSpinnerData(this.getActivity(), mySpinner);
		mySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				strGebiet = mySpinner.getSelectedItem().toString();
				Log.e(TAG,
						"mySpinner.getSelectedItem()" + strGebiet);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}

		});
		// SEARCH Button
		Button buttonSearchSummit = view.findViewById(R.id.buttonSearchSummit);
		buttonSearchSummit.setOnClickListener(this);
		Button buttonInfo = view.findViewById(R.id.button_INFO);
		buttonInfo.setOnClickListener(this);
		// ***************************************************************************************
		// create RangeSeekBar as Integer range between 0 and 250
		RangeSeekBar<Integer> seekBarAnzahlDerWege = new RangeSeekBar<Integer>(intMinAnzahlDerWege,
				intMaxAnzahlDerWege, getActivity());
		seekBarAnzahlDerWege
				.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
					@Override
					public void onRangeSeekBarValuesChanged(
							RangeSeekBar<?> bar, Integer minValue,
							Integer maxValue) {
						// handle changed range values
						String strUpdate = getString(R.string.strAnzahlDerWege)
								+ " (" + minValue + " bis " + maxValue + ")";
						intMinAnzahlDerWege = minValue;
						intMaxAnzahlDerWege = maxValue;
						View viewAnzahlDerWege = view
								.findViewById(R.id.includeAnzahlDerWege);
						((TextView) viewAnzahlDerWege
								.findViewById(R.id.textViewAnzahlDerWege))
								.setText(strUpdate);
					}
				});
		// ***************************************************************************************
		// add RangeSeekBar to predefined layout
		ViewGroup layout_activity_seekbar_anzahld_der_wege = (ViewGroup) view
				.findViewById(R.id.includeAnzahlDerWege);
		LayoutParams layoutParamsSeekBarAnzahlDerWege = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParamsSeekBarAnzahlDerWege
				.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		layoutParamsSeekBarAnzahlDerWege.addRule(RelativeLayout.BELOW,
				R.id.textViewAnzahlDerWege);
		seekBarAnzahlDerWege.setLayoutParams(layoutParamsSeekBarAnzahlDerWege);
		layout_activity_seekbar_anzahld_der_wege.addView(seekBarAnzahlDerWege);
		// ***************************************************************************************
		// create RangeSeekBar as Integer range between 0 and 200
		RangeSeekBar<Integer> seekBarAnzahlDerSternchenWege = new RangeSeekBar<Integer>(
				intMinAnzahlDerSternchenWege, intMaxAnzahlDerSternchenWege,
				getActivity());
		seekBarAnzahlDerSternchenWege
				.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
					@Override
					public void onRangeSeekBarValuesChanged(
							RangeSeekBar<?> bar, Integer minValue,
							Integer maxValue) {
						// handle changed range values
						String strUpdate = getString(R.string.strAnzahlDerSternchenWege)
								+ " (" + minValue + " bis " + maxValue + ")";
						intMinAnzahlDerSternchenWege = minValue;
						intMaxAnzahlDerSternchenWege = maxValue;
						View viewAnzahlDerSternchenWege = view
								.findViewById(R.id.includeAnzahlDerSternchenWege);
						((TextView) viewAnzahlDerSternchenWege
								.findViewById(R.id.textViewAnzahlDerSternchenWege))
								.setText(strUpdate);
					}
				});
		// ***************************************************************************************
		// add RangeSeekBar to predefined layout
		ViewGroup layout_activity_seekbar_anzahld_der_sternchen_wege = (ViewGroup) view
				.findViewById(R.id.includeAnzahlDerSternchenWege);
		LayoutParams layoutParamsSeekBarAnzahlDerSternchenWege = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParamsSeekBarAnzahlDerSternchenWege
				.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		layoutParamsSeekBarAnzahlDerSternchenWege.addRule(RelativeLayout.BELOW,
				R.id.textViewAnzahlDerSternchenWege);
		seekBarAnzahlDerSternchenWege
				.setLayoutParams(layoutParamsSeekBarAnzahlDerSternchenWege);
		layout_activity_seekbar_anzahld_der_sternchen_wege
				.addView(seekBarAnzahlDerSternchenWege);
		return view;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonSearchSummit) {
			Log.i(getClass().getSimpleName(), "onClick 'buttonSearchSummit' gedrückt...");
			myEditTextSuchtextID = R.id.editTextSuchtextGipfel;
			myAutoCompleteTextViewText = ((AutoCompleteTextView) view
					.findViewById(myEditTextSuchtextID)).getText().toString();

            String strTextSuchtext = getStrTextSuchtext();
            String strGebiet = getStrtextViewGebiet();
            int intMinAnzahlDerWege = getIntMinAnzahlDerWege();
            int intMaxAnzahlDerWege = getIntMaxAnzahlDerWege();
            int intMinAnzahlDerSternchenWege = getIntMinAnzahlDerSternchenWege();
            int intMaxAnzahlDerSternchenWege = getIntMaxAnzahlDerSternchenWege();

			Fragment fragment = TT_SummitsFoundFragment.newInstance(
                    strTextSuchtext, strGebiet, intMinAnzahlDerWege, intMaxAnzahlDerWege,
                    intMinAnzahlDerSternchenWege, intMaxAnzahlDerSternchenWege
            );

            ((MainActivity) getActivity())
                    .replaceFragment(fragment, TT_SummitsFoundFragment.ID);


		} else if (v.getId() == R.id.button_INFO) {
			// Put up the Yes/No message box
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setIcon(R.drawable.ic_launcher)
					.setTitle(
							getString(R.string.app_name) + " Vers.: "
									+ MainActivity.APP_VERSION)
					.setMessage(R.string.APP_INFO_TEXT)
					.setIcon(android.R.drawable.ic_dialog_alert)
					// POSITIVE BUTTON: BackUP Data
					.setPositiveButton(R.string.BACKUP_DATA,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// Yes button clicked, do something
									String strMsg = DB_BackUp2SDCard
											.exportDB(getActivity());
									Toast.makeText(getActivity(), strMsg,
											Toast.LENGTH_LONG).show();

								}
							})
					// NEGATIVE BUTTON: Do nothing
					.setNegativeButton(android.R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// CONTINUE button clicked, do nothing
									dialog.cancel();
								}
							})
					// NEUTRAL BUTTON: READ Data from Backup-Folder
					.setNeutralButton(R.string.IMPORT_DATA,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// NEUTRAL BUTTON: READ Data from
									// Backup-Folder
									// Close first Dialog
									dialog.cancel();
									// *****************************************
									// Just another Dialog: "ARE YOU SURE?"
									new AlertDialog.Builder(getActivity())
											.setIcon(R.drawable.ic_launcher)
											.setTitle(R.string.app_name)
											.setMessage(
													R.string.INFO_TEXT_DB_IMPORT)
											.setIcon(
													android.R.drawable.ic_dialog_alert)
											// POSITIVE BUTTON: BackUP Data
											.setPositiveButton(
													R.string.YES,
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int which) {
															// Yes button
															// clicked, do
															// something
															String strMsg = DB_BackUp2SDCard
																	.importDB(getActivity());
															Toast.makeText(
																	getActivity(),
																	strMsg,
																	Toast.LENGTH_LONG)
																	.show();

														}
													})
											// NEGATIVE BUTTON: Do nothing
											.setNegativeButton(
													android.R.string.cancel,
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int which) {
															// CONTINUE button
															// clicked, do
															// nothing
															dialog.cancel();
														}
													}).show();
									// ****************************************
								}
							})
					// Do nothing on no
					.show();
		}
	}

	@Override
	public void onResume() {
		myViewID = R.layout._main_activity__summit;
//		loadSpinnerData(this.getActivity(), mySpinner);
		myEditTextSuchtextID = R.id.editTextSuchtextGipfel;
		super.onResume();
	}
}
