package com.teufelsturm.tt_downloaderand_kotlin.summit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.teufelsturm.tt_downloaderand_kotlin.MainActivitySearchSummit;
import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.TT_DB_Helper.DataBaseHelper;
import com.teufelsturm.tt_downloaderand_kotlin.TT_DB_Helper.StoreMySummitComment;
import com.teufelsturm.tt_downloaderand_kotlin.gpslocation.TT_SummitCoord_TabWidget;
import com.teufelsturm.tt_downloaderand_kotlin.routes.TT_RouteFoundActivity;
import com.teufelsturm.tt_downloaderand_kotlin.routes.TT_Route_ANDAdapter;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.DatePickerFragment;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.TT_Route_AND;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.TT_Summit_AND;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TT_SummitFoundActivity extends FragmentActivity{

    private static String TAG = TT_SummitFoundActivity.class.getSimpleName();

	private static List<TT_Route_AND> lstTT_Route_AND;
	private SQLiteDatabase newDB;
	private TT_Summit_AND aTT_Summit_AND;
	private TT_Route_ANDAdapter listenAdapter;
	@SuppressLint("UseSparseArrays")
	private Map<Integer, String> hashmapNeighbourSummit = new HashMap<Integer, String>();
	private static Boolean dataHasChanged = Boolean.FALSE;
	private static TT_SummitFoundActivity thisTT_SummitFoundActivity;
	private DialogFragment dateFragment; 
	private static boolean hasUnSavedData;
	private static EditText editTextMySummitComment;
	private static ListView meinListView;
	private static Button buttonSummitAscendDay_inComment;
	private static CheckBox checkBoxSummitAsscended;
	public static final String SAVED_TEXT_KEY = "SavedText";

	@Override
	protected void onResume() {
		super.onResume();
		thisTT_SummitFoundActivity  = this;
		Log.i(getClass().getSimpleName(), "onResume ");
		if (dataHasChanged) {
			Log.i(getClass().getSimpleName(), "onResume; dataHasChanged --> "
					+ dataHasChanged);
			this.openAndQueryDatabase(this.aTT_Summit_AND);
			listenAdapter.notifyDataSetChanged();
			fillSummitDetails();
			dataHasChanged = Boolean.FALSE;
		}
	}
	@Override
	protected void onPause() {
		thisTT_SummitFoundActivity  = null;
		super.onPause();
	}

	public static void hasChangedData() {
		Log.i(TAG,"thisTT_SummitFoundActivity != null " + (thisTT_SummitFoundActivity != null));
		if (thisTT_SummitFoundActivity != null){
			thisTT_SummitFoundActivity.openAndQueryDatabase(thisTT_SummitFoundActivity.aTT_Summit_AND);
			thisTT_SummitFoundActivity.listenAdapter.notifyDataSetChanged();
			thisTT_SummitFoundActivity.fillSummitDetails();
			dataHasChanged = Boolean.FALSE;
		}
		else{
			dataHasChanged = Boolean.TRUE;
		}
	}

	public void onSaveInstanceState(Bundle savedInstanceState) {
	    super.onSaveInstanceState(savedInstanceState);
		// now, save the text if something overlaps this Activity
		savedInstanceState.putString(SAVED_TEXT_KEY, editTextMySummitComment
				.getText().toString());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(getClass().getSimpleName(), "Neuer onCreate... ");
		setContentView(R.layout.summit_found_lv_w_header);
		// has to be of type "layout"
		// Find the packet with the 'parent' object
		Intent intent = getIntent();
		Log.i(getClass().getSimpleName(), "Neuer intent...");
		aTT_Summit_AND = intent.getParcelableExtra("TT_Gipfel_AND");
		Log.i(getClass().getSimpleName(), "aTT_Gipfel_AND == null...? "
				+ (aTT_Summit_AND == null));
		lstTT_Route_AND = new ArrayList<TT_Route_AND>();
		// query all routes to this summit
		this.openAndQueryDatabase(aTT_Summit_AND);
		listenAdapter = new TT_Route_ANDAdapter(this, lstTT_Route_AND, false);
		Log.i(getClass().getSimpleName(),
				"(ListView) findViewById(R.id.list_routes);");
		meinListView = (ListView) findViewById(R.id.list_routes);
		// working Code for the Header Creation:
		LayoutInflater layoutInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.i(getClass().getSimpleName(), "RelativeLayout myHeaderView  ... ");
		RelativeLayout myHeaderView = (RelativeLayout) layoutInflater.inflate(
				R.layout.summit_activity_found_header, null, false);
		// has to be of type "layout"

		Log.i(getClass().getSimpleName(),
				"RelativeLayout meinListView.addHeaderView... ");
		meinListView.addHeaderView(myHeaderView, null, false);
		// the editTextMySummitComment is part of the header - can not be found
		// before...
		editTextMySummitComment = (EditText) findViewById(R.id.editTextMySummitComment);
		editTextMySummitComment.setText(aTT_Summit_AND.getStr_MyComment());
		// http://stackoverflow.com/questions/9770252/scrolling-editbox-inside-scrollview
		editTextMySummitComment.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(final View v, final MotionEvent motionEvent) {
				if (v.getId() == R.id.editTextMySummitComment) {
					v.getParent().requestDisallowInterceptTouchEvent(true);
					switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_UP:
						v.getParent().requestDisallowInterceptTouchEvent(false);
						break;
					}
				}
				return false;
			}
		});
		editTextMySummitComment.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				Log.v(getClass().getSimpleName(),
						"editTextMyRouteComment.addTextChanged: " + s);
				if (!aTT_Summit_AND.getStr_MyComment().equals(s)) {
					aTT_Summit_AND.setStr_MyComment(s.toString());
					hasUnSavedData = true;
					// not working: editTextMySummitComment.removeTextChangedListener(this);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		checkBoxSummitAsscended = (CheckBox) findViewById(R.id.CheckBoxSummitAsscended);
		
		// fill the Data in the Header
		fillSummitDetails();
		meinListView.setAdapter(listenAdapter);
		// recreate content in editTextMySummitComment (if saved)
		// ok we back, load the saved text
		if (savedInstanceState != null) {
			String savedText = savedInstanceState.getString(SAVED_TEXT_KEY);
			editTextMySummitComment.setText(savedText);
		}
		
		// Event Listener
		createOnItemClickListener();
		checkBoxSummitAsscended.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hasUnSavedData = true;
			}
		});
			
		
		buttonSummitAscendDay_inComment = (Button)findViewById(R.id.buttonSummitAscendDay_inComment);
		Log.v(getClass().getSimpleName(), "buttonSummitAscendDay_inComment: " + buttonSummitAscendDay_inComment);
		updateDateAscended();
		// Define Action Listener
		buttonSummitAscendDay_inComment.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// ********************************************************
				dateFragment = new DatePickerFragment();
			    dateFragment.show(getSupportFragmentManager(), "datePicker");
			   
				// ********************************************************
				Log.i(getClass().getSimpleName(),
						"datePickerDialog.show()...: ");
				// ********************************************************
			}
		});
		hasUnSavedData = false;
		actionListenertextView_GpsCoords();

		Log.i(getClass().getSimpleName(),
				"Neuer onCreate komplett abgearbeitet... ");
	}

	private void createOnItemClickListener() {
		meinListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i(this.getClass().getSimpleName(),
						"Neuer addonPageSummitsFoundActivity... ");
				Intent addonPageCommentsFoundActivity = new Intent(
						TT_SummitFoundActivity.this,
						TT_RouteFoundActivity.class);

				Log.i(this.getClass().getSimpleName(),
						"addonPageCommentsFoundActivity... ");
				addonPageCommentsFoundActivity.putExtra("TT_Route_AND",
						lstTT_Route_AND.get(position - 1));
				Log.i(this.getClass().getSimpleName(), "startActivity... ");
				startActivity(addonPageCommentsFoundActivity);
				// Log.i(TAG, "Click ListItem Number " + position
				// + "\r\nGipfelname: "
				// + lstTT_Gipfel_AND.get(position).getStr_SummitName());
			}
		});
	}

	@Override
	protected void onStop() {
		if (hasUnSavedData) {
			saveMySummitComment();
			hasUnSavedData = false;
		}
		super.onStop();
	}
	
	public void saveMySummitComment() {
		Toast.makeText(getApplicationContext(),
				"Saved Comment for this Summit...\n"
						+ aTT_Summit_AND.getStr_DateAsscended() + ":\n"
						+ aTT_Summit_AND.getStr_SummitName(), Toast.LENGTH_SHORT)
						.show();
		try {
			aTT_Summit_AND
					.setBln_Asscended(checkBoxSummitAsscended.isChecked());
			aTT_Summit_AND.setStr_MyComment(editTextMySummitComment
					.getText().toString());
			new StoreMySummitComment(getApplicationContext(),
					aTT_Summit_AND.getInt_TTGipfelNr(), aTT_Summit_AND
							.getBln_Asscended(), aTT_Summit_AND
							.getLong_DateAsscended(), aTT_Summit_AND
							.getStr_MyComment(), false);
			TT_SummitsFoundActivity.hasChangedData();
		} catch (Exception ex) {
			Log.i(getClass().getSimpleName(), ex.toString());
		}
	}

	private void actionListenerButton_NeighbourSummit(
			int button_NeighbourSummitId) {
		// Define Action Listener
		Button button_NeighbourSummit = (Button) findViewById(button_NeighbourSummitId);
		Log.i(getClass().getSimpleName(), "buttonSearchSummit ...: "
				+ button_NeighbourSummit.getText());
		button_NeighbourSummit.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Button b = (Button) v;
				Integer bInt = (Integer) b.getTag();
				String buttonText = b.getText().toString();
				Log.i(getClass().getSimpleName(),
						"Click button_NeighbourSummit for Summit..."
								+ buttonText + "\r\n" + bInt);
				Log.i(getClass().getSimpleName(),
						"Intent addonPageRoutesFoundActivity = new Intent(...");
				Intent addonPageRoutesFoundActivity = new Intent(
						TT_SummitFoundActivity.this,
						TT_SummitFoundActivity.class);
				Log.i(getClass().getSimpleName(),
						"addonPageRoutesFoundActivity.putExtra(...");
				addonPageRoutesFoundActivity.putExtra("TT_Gipfel_AND",
						new TT_Summit_AND(bInt, getApplicationContext()));
				Log.i(getClass().getSimpleName(), "startActivity... ");
				startActivity(addonPageRoutesFoundActivity);
			}
		});
	}

	private void actionListenertextView_GpsCoords() {
		// Define Action Listener
		Button textView_GpsCoords = (Button) findViewById(R.id.textView_GpsCoords);
		// Log.i(getClass().getSimpleName(), "buttonSearchSummit ...: " +
		// buttonSaveSummit.toString());
		textView_GpsCoords.setOnClickListener(new TextView.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(),
						"Click on textView_GpsCoords...", Toast.LENGTH_LONG)
						.show();
				Intent addonPageSummitsFoundActivity = new Intent(
				/* TT_SummitFoundActivity.this, MapsOsmDroidActivity.class */
				/* TT_SummitFoundActivity.this, MapsForgeActivity.class */
				TT_SummitFoundActivity.this, TT_SummitCoord_TabWidget.class);

				// Log.i(TAG, "startActivity... ");
				addonPageSummitsFoundActivity.putExtra("TT_Gipfel_AND",
						aTT_Summit_AND);
				startActivity(addonPageSummitsFoundActivity);
			}
		});
	}

	private void fillSummitDetails() {
		Log.i(getClass().getSimpleName(),
				"aTT_Gipfel_AND - fillSummitDetails: 1"
						+ aTT_Summit_AND.getInt_TTGipfelNr().toString()
						+ " --> " + aTT_Summit_AND.getStr_SummitName());
		// Summit Name
		((TextView) findViewById(R.id.textView_SummitName))
				.setText(aTT_Summit_AND.getStr_SummitName() + "   (KleFü #"
						+ aTT_Summit_AND.getInt_SummitNumberOfficial() + ")");

		// Area Name
		((TextView) findViewById(R.id.textView_Area)).setText(getResources()
				.getString(R.string.lblGebiet) + aTT_Summit_AND.getStr_Area());
		Log.i(getClass().getSimpleName(),
				"aTT_Gipfel_AND - fillSummitDetails: 2");
		Log.i(getClass().getSimpleName(),
				"aTT_Gipfel_AND - fillSummitDetails: 3");
		// *******************************************************************************
		// Create the text for the Neighbor Summit and their actionListener
		int[] arrNeighbourSummits = new int[] { R.id.button_NeighbourSummit01,
				R.id.button_NeighbourSummit02, R.id.button_NeighbourSummit03,
				R.id.button_NeighbourSummit04 };
		// Neighbor Summit #01..04
		int iCount = 0;
		for (Map.Entry<Integer, String> strH : hashmapNeighbourSummit
				.entrySet()) {
			((Button) findViewById(arrNeighbourSummits[iCount])).setTag(strH
					.getKey());
			((Button) findViewById(arrNeighbourSummits[iCount])).setText((strH
					.getValue()).toString());
			Log.i(getClass().getSimpleName(),
					"aTT_Gipfel_AND - fillSummitDetails: Neighbor Summit #0"
							+ (iCount + 1));
			actionListenerButton_NeighbourSummit(arrNeighbourSummits[iCount]);
			iCount++;
		}
		// is Ascended?
		checkBoxSummitAsscended.setChecked(aTT_Summit_AND.getBln_Asscended());
		Log.i(getClass().getSimpleName(),
				"aTT_Gipfel_AND - fillSummitDetails: is Ascended? "
						+ aTT_Summit_AND.getBln_Asscended());
		// My Comment
		editTextMySummitComment.setText(aTT_Summit_AND.getStr_MyComment());

		Log.i(getClass().getSimpleName(),
				"aTT_Gipfel_AND - fillSummitDetails erledigt: "
						+ aTT_Summit_AND.getInt_TTGipfelNr().toString()
						+ " --> " + aTT_Summit_AND.getStr_SummitName());
	}

	private void openAndQueryDatabase(TT_Summit_AND aTT_Gipfel_AND) {
		Log.i(getClass().getSimpleName(), "Neuer openAndQueryDatabase... "
				+ aTT_Gipfel_AND.getStr_SummitName());
		// IMPORTANT: old List needs to be cleared!
		lstTT_Route_AND.clear();
		try {
			DataBaseHelper dbHelper = new DataBaseHelper(
					this.getApplicationContext());
			newDB = dbHelper.getWritableDatabase();

			String QueryString1;
			Cursor cursor = null;
			QueryString1 = "SELECT a.[isAscendedSummit], a.[intDateOfAscend], a.[strMySummitComment] "
					+ "FROM [myTT_Summit_AND] a"
					+ " WHERE a.[intTTGipfelNr] =  "
					+ aTT_Gipfel_AND.getInt_TTGipfelNr();
			cursor = newDB.rawQuery(QueryString1, null);
			if (cursor != null && cursor.moveToFirst()) {
				aTT_Gipfel_AND.setBln_Asscended(cursor.getInt(cursor
						.getColumnIndex("isAscendedSummit")) > 0);
				aTT_Gipfel_AND.setDatumBestiegen(cursor.getLong(cursor
						.getColumnIndex("intDateOfAscend")));
				aTT_Gipfel_AND.setStr_MyComment(cursor.getString(cursor
						.getColumnIndex("strMySummitComment")));
			}
			QueryString1 = "SELECT a.[intTTGipfelNr], a.[strName], a.[dblGPS_Latitude], a.[dblGPS_Longitude]"
					+ " from [TT_Summit_AND] a"
					+ " where a.[intTTGipfelNr] in"
					+ "		 ( SELECT b.[intTTNachbarGipfelNr]"
					+ " 		from [TT_NeigbourSummit_AND] b"
					+ " 		where b.[intTTHauptGipfelNr] = "
					+ this.aTT_Summit_AND.getInt_TTGipfelNr() + " )";
			Log.i(getClass().getSimpleName(), "Neuer Nachbargipfel suchen:\r\n"
					+ QueryString1);
			cursor = newDB.rawQuery(QueryString1, null);
			Log.i(getClass().getSimpleName(), " --> cursor.getCount() "
					+ cursor.getCount() + "'");
			if (cursor != null) {
				int iCounter = 0;
				Location mainCoordinates = new Location("reverseGeocoded");
				Location neighborCoordinate = new Location("reverseGeocoded");
				mainCoordinates.setLatitude(aTT_Gipfel_AND.getDbl_GpsLat());
				mainCoordinates.setLongitude(aTT_Gipfel_AND.getDbl_GpsLong());
				mainCoordinates.setTime(new Date().getTime());
				Log.i(getClass().getSimpleName(), " --> mainCoordinates ");

				DecimalFormat aDecimalFormat = new DecimalFormat("##");
				if (cursor.moveToFirst()) {
					do {
						neighborCoordinate.setLatitude(cursor.getDouble(cursor
								.getColumnIndex("dblGPS_Latitude")));
						neighborCoordinate.setLongitude(cursor.getDouble(cursor
								.getColumnIndex("dblGPS_Longitude")));
						neighborCoordinate.setTime(new Date().getTime());

						Integer intTTGipfelNachbarNr = cursor.getInt(cursor
								.getColumnIndex("intTTGipfelNr"));
						Log.i(getClass().getSimpleName(),
								" --> neighborCoordinate \t("
										+ intTTGipfelNachbarNr + ")");
						switch (++iCounter) {
						case 1:
							hashmapNeighbourSummit
									.put(intTTGipfelNachbarNr,
											getStrDistance(cursor,
													mainCoordinates,
													aDecimalFormat,
													neighborCoordinate));
							Log.i(getClass().getSimpleName(),
									" --> neighborCoordinate im 'put'\t"
											+ intTTGipfelNachbarNr + ")");

							break;
						case 2:
							hashmapNeighbourSummit
									.put(intTTGipfelNachbarNr,
											getStrDistance(cursor,
													mainCoordinates,
													aDecimalFormat,
													neighborCoordinate));
							Log.i(getClass().getSimpleName(),
									" --> neighborCoordinate im 'put'\t"
											+ intTTGipfelNachbarNr + ")");
							break;
						case 3:
							hashmapNeighbourSummit
									.put(intTTGipfelNachbarNr,
											getStrDistance(cursor,
													mainCoordinates,
													aDecimalFormat,
													neighborCoordinate));
							Log.i(getClass().getSimpleName(),
									" --> neighborCoordinate im 'put'\t"
											+ intTTGipfelNachbarNr + ")");
							break;
						case 4:
							hashmapNeighbourSummit
									.put(intTTGipfelNachbarNr,
											getStrDistance(cursor,
													mainCoordinates,
													aDecimalFormat,
													neighborCoordinate));
							Log.i(getClass().getSimpleName(),
									" --> neighborCoordinate im 'put'\t"
											+ intTTGipfelNachbarNr + ")");
							break;
						}
						Log.i(getClass().getSimpleName(),
								" --> wird:"
										+ cursor.getString(cursor
												.getColumnIndex("strName"))
										+ " in "
										+ (mainCoordinates
												.distanceTo(neighborCoordinate) < 4000000 ? aDecimalFormat.format(mainCoordinates
												.distanceTo(neighborCoordinate))
												: "?") + "m; ");

					} while (cursor.moveToNext());
				}
			}

			QueryString1 = "SELECT a.intTTWegNr, a.WegName, a.strSchwierigkeitsGrad"
					+ ", a.intSterne, a.blnAusrufeZeichen, a.sachsenSchwierigkeitsGrad"
					+ ", a.ohneUnterstützungSchwierigkeitsGrad"
					+ ", a.rotPunktSchwierigkeitsGrad, a.intSprungSchwierigkeitsGrad"
					+ ", a.intAnzahlDerKommentare, a.fltMittlereWegBewertung"
					+ ", b.[isAscendedRoute], b.[intDateOfAscend], b.[strMyRouteComment]"
					+ " FROM TT_Route_AND a"
					+ " LEFT OUTER JOIN myTT_Route_AND b"
					+ " ON (a.[intTTWegNr] = b.[intTTWegNr])"
					+ " WHERE a.[intTTGipfelNr] = "
					+ aTT_Gipfel_AND.getInt_TTGipfelNr()
					+ " ORDER BY a.WegName";
			Log.i(getClass().getSimpleName(),
					"Neue Wege zum Gipfel suchen:\r\n" + QueryString1);
			Log.i(this.getClass().getSimpleName(), "Neuer SuchText: '"
					+ MainActivitySearchSummit.getStrTextSuchtext() + "'");

			cursor = newDB.rawQuery(QueryString1, null);
			Log.i(getClass().getSimpleName(),
					"Neue Wege zum Gipfel suchen:\t c != null'"
							+ (cursor != null));

			if (cursor != null) {
				int iCounter = 0;
				if (cursor.moveToFirst()) {
					do {
						String WegName = cursor.getString(cursor
								.getColumnIndex("WegName"));
						int intTTWegNr = cursor.getInt(cursor
								.getColumnIndex("intTTWegNr"));
						String strSchwierigkeitsGrad = cursor.getString(cursor
								.getColumnIndex("strSchwierigkeitsGrad"));
						int intSterne = cursor.getInt(cursor
								.getColumnIndex("intSterne"));
						Boolean blnAusrufeZeichen = cursor.getInt(cursor
								.getColumnIndex("blnAusrufeZeichen")) > 0;
						Integer sachsenSchwierigkeitsGrad = cursor
								.getInt(cursor
										.getColumnIndex("sachsenSchwierigkeitsGrad"));
						Integer int_OhneUnterstuetzungSchwierigkeitsGrad = cursor
								.getInt(cursor
										.getColumnIndex("ohneUnterstützungSchwierigkeitsGrad"));
						Integer int_RotPunktSchwierigkeitsGrad = cursor
								.getInt(cursor
										.getColumnIndex("rotPunktSchwierigkeitsGrad"));
						int intSprungSchwierigkeitsGrad = cursor.getInt(cursor
								.getColumnIndex("intSprungSchwierigkeitsGrad"));
						int intAnzahlDerKommentare = cursor.getInt(cursor
								.getColumnIndex("intAnzahlDerKommentare"));
						float fltMittlereWegBewertung = cursor.getFloat(cursor
								.getColumnIndex("fltMittlereWegBewertung"));
						Integer int_typeAsscended = cursor.getInt(cursor
								.getColumnIndex("isAscendedRoute"));
						Long long_DateAsscended = cursor.getLong(cursor
								.getColumnIndex("intDateOfAscend"));

						String str_Comment = cursor.getString(cursor
								.getColumnIndex("strMyRouteComment"));

						lstTT_Route_AND.add(new TT_Route_AND(intTTWegNr,
								aTT_Gipfel_AND.getInt_TTGipfelNr(), WegName,
								aTT_Gipfel_AND.getStr_SummitName(),
								blnAusrufeZeichen, intSterne,
								strSchwierigkeitsGrad,
								sachsenSchwierigkeitsGrad,
								int_OhneUnterstuetzungSchwierigkeitsGrad,
								int_RotPunktSchwierigkeitsGrad,
								intSprungSchwierigkeitsGrad,
								intAnzahlDerKommentare,
								fltMittlereWegBewertung, int_typeAsscended,
								long_DateAsscended, str_Comment));
						Log.i(getClass().getSimpleName(), ++iCounter
								+ " -> Neuer Weg... " + WegName);
					} while (cursor.moveToNext());
				}
			}
		} catch (SQLiteException se) {
			Log.e(getClass().getSimpleName(),
					"Could not create or Open the database");
		} finally {
			newDB.close();
		}
	}

	private String getStrDistance(Cursor cursor, Location mainLocation,
			DecimalFormat aDecimalFormat, Location neighborCoordinate) {
		return cursor.getString(cursor.getColumnIndex("strName"))
				+ System.getProperty("line.separator")
				+ " in "
				+ ((mainLocation.distanceTo(neighborCoordinate) < 4000000 && mainLocation
						.distanceTo(neighborCoordinate) > 0) ? aDecimalFormat
						.format(mainLocation.distanceTo(neighborCoordinate))
						: "?") + "m";
	}
	// ***************************
	private void updateDateAscended() {
		String strDatumBestiegenString = aTT_Summit_AND.getStr_DateAsscended();
		if (strDatumBestiegenString.equals("")) {
			strDatumBestiegenString = getApplicationContext().getResources()
					.getString(R.string.strChooseDate);
		}
		buttonSummitAscendDay_inComment.setText(strDatumBestiegenString);
	}
	public TT_Summit_AND getTT_Summit_AND() {
		return aTT_Summit_AND;
	}
	public Button getButtonMyAscendDate() {
		return buttonSummitAscendDay_inComment;
	}

	public void setHasUnSavedData(boolean hasUnSavedData) {
		TT_SummitFoundActivity.hasUnSavedData = hasUnSavedData;
	}

}
