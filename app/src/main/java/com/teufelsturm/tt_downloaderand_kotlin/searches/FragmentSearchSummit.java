package com.teufelsturm.tt_downloaderand_kotlin.searches;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
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

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.BubbleThumbRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.teufelsturm.tt_downloaderand_kotlin.MainActivity;
import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.dbHelper.DB_BackUp2SDCard;
import com.teufelsturm.tt_downloaderand_kotlin.foundSummitList.TT_SummitsFoundFragment;

import org.jetbrains.annotations.NotNull;

public class FragmentSearchSummit extends FragmentSearchAbstract
		implements OnClickListener {
    private static final String TAG = FragmentSearchSummit.class.getSimpleName();
    private static final String STR_NAME = "strName";

    private Spinner mySpinner;


	@Override
	protected Cursor getAutoCompleteCursor(CharSequence constraint) {
		return myAutoCompleteDbAdapter
				.getAllSummits(getContext(), searchManager,
                        (constraint != null ? constraint.toString() : null));
	}


    @Override
	public View onCreateView(@NonNull LayoutInflater inflater,
							  ViewGroup container,
                              Bundle savedInstanceState) {
		myViewID = R.layout._main_activity__summit;
		myEditTextSuchtextID = R.id.editTextSuchtextGipfel;
		from = new String[] {STR_NAME};
		view = super.createView(inflater, container);
		// ***************************************************************************************
		// Define Action Listener
		// Spinner 
		mySpinner = view.findViewById(R.id.spinnerAreaSummit);
		loadSpinnerData(this.getActivity(), mySpinner);
		mySpinner.setSelection(searchManager.getMyAreaPositionFromSpinner());
		// SEARCH Button
		Button buttonSearchSummit = view.findViewById(R.id.buttonSearchSummit);
		buttonSearchSummit.setOnClickListener(this);
		Button buttonInfo = view.findViewById(R.id.button_INFO);
		buttonInfo.setOnClickListener(this);
		// ***************************************************************************************
		// create RangeSeekBar for number of summits as Integer range between 0 and 250
        BubbleThumbRangeSeekbar seekBarAnzahlDerWege
                = view.findViewById(R.id.rangeSeekbarAnzahlDerWege);
		// ***************************************************************************************
        // create RangeSeekBar for number of summits as Integer range between 0 and 200
        BubbleThumbRangeSeekbar seekBarAnzahlDerSternchenWege
                = view.findViewById(R.id.rangeSeekbarAnzahlDerSternchenWege);

        mySpinnerSetOnItemSelectedListener();
        seekBarAnzahlDerWegeSetOnRangeSeekBarChangeListener(seekBarAnzahlDerWege);
        seekBarAnzahlDerSternchenWegeSetOnRangeSeekBarChangeListener(seekBarAnzahlDerSternchenWege);
		return view;
	}

    private void seekBarAnzahlDerSternchenWegeSetOnRangeSeekBarChangeListener(@NotNull BubbleThumbRangeSeekbar
                                                                                      seekBarAnzahlDerSternchenWege) {
        // set listener
        seekBarAnzahlDerSternchenWege.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                // handle changed range values
                String strUpdate = getString(R.string.strAnzahlDerSternchenWege)
                        + " (" + minValue + " bis " + maxValue + ")";
                ((TextView) view.findViewById(R.id.textViewAnzahlDerSternchenWege))
                        .setText(strUpdate);
            }
        });

        // set final value listener
        seekBarAnzahlDerSternchenWege.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                // handle changed range values
                searchManager.setIntMinAnzahlDerWege(minValue.intValue());
                searchManager.setIntMaxAnzahlDerWege(maxValue.intValue());
            }
        });
    }

    private void seekBarAnzahlDerWegeSetOnRangeSeekBarChangeListener(@NotNull BubbleThumbRangeSeekbar seekBarAnzahlDerWege) {
        // set listener
        seekBarAnzahlDerWege.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                // handle changed range values
                String strUpdate = getString(R.string.strAnzahlDerWege)
                        + " (" + minValue + " bis " + maxValue + ")";
                ((TextView) view.findViewById(R.id.textViewAnzahlDerWege))
                        .setText(strUpdate);
            }
        });

        // set final value listener
        seekBarAnzahlDerWege.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                // handle changed range values
                searchManager.setIntMinAnzahlDerWege(minValue.intValue());
                searchManager.setIntMaxAnzahlDerWege(maxValue.intValue());
            }
        });
    }

	private void mySpinnerSetOnItemSelectedListener() {
		mySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				strGebiet = mySpinner.getSelectedItem().toString();
				Log.e(TAG,"mySpinner.getSelectedItem()" + strGebiet);
				searchManager.setMyAreaFromSpinner(position);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
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
            int intMinAnzahlDerWege = searchManager.getIntMinAnzahlDerWege();
            int intMaxAnzahlDerWege = searchManager.getIntMaxAnzahlDerWege();
            int intMinAnzahlDerSternchenWege = searchManager.getIntMinAnzahlDerSternchenWege();
            int intMaxAnzahlDerSternchenWege = searchManager.getIntMaxAnzahlDerSternchenWege();

			Fragment fragment = TT_SummitsFoundFragment.newInstance(
                    strTextSuchtext, strGebiet, intMinAnzahlDerWege, intMaxAnzahlDerWege,
                    intMinAnzahlDerSternchenWege, intMaxAnzahlDerSternchenWege
            );
            ((MainActivity) getActivity()).replaceFragment(fragment, TT_SummitsFoundFragment.ID);
		} else if (v.getId() == R.id.button_INFO) {
            showInfo();

        }
	}

    private void showInfo() {
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

    @Override
	public void onResume() {
		myViewID = R.layout._main_activity__summit;
//		loadSpinnerData(this.getActivity(), mySpinner);
		myEditTextSuchtextID = R.id.editTextSuchtextGipfel;
		super.onResume();
	}
}
