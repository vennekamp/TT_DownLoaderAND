package com.teufelsturm.tt_downloaderand_kotlin.routes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.TT_DB_Helper.DataBaseHelper;
import com.teufelsturm.tt_downloaderand_kotlin.TT_DB_Helper.StoreMyRouteComment;
import com.teufelsturm.tt_downloaderand_kotlin.TT_DB_Helper.StoreMySummitComment;
import com.teufelsturm.tt_downloaderand_kotlin.comments.TT_Comment_ANDAdapter;
import com.teufelsturm.tt_downloaderand_kotlin.summit.TT_SummitFoundActivity;
import com.teufelsturm.tt_downloaderand_kotlin.summit.TT_SummitsFoundActivity;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.DatePickerFragment;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumBegehungsStil;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.TT_Comment_AND;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.TT_Route_AND;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class TT_RouteFoundFragment extends Fragment implements OnInitListener {

	private static List<TT_Comment_AND> lstTT_Comment_AND;
	private SQLiteDatabase newDB;
	private ListView meinListView;
	private TT_Route_AND aTT_Route_AND;
	private String strSummitName_inComment;
	private String strAreaName_inComment;
	private TT_Comment_ANDAdapter listenAdapter;
	private TextToSpeech tts;
	private DialogFragment dateFragment; 
	private Calendar calendar = Calendar.getInstance(TimeZone
			.getTimeZone("CET"));
	private static EditText editTextMyRouteComment;
	private static Button buttonRouteAscendDay;
	private static Spinner spinnerRouteAsscended_inComment;
	private static boolean hasUnSavedData;
	public static final String SAVED_TEXT_KEY = "SavedText";
	static final int DATE_DIALOG_ID = 0;

	public static Fragment newInstance(TT_Route_AND someTT_Route_AND) {
		TT_RouteFoundFragment myFragment = new TT_RouteFoundFragment();

		Bundle args = new Bundle();
		args.putParcelable("TT_Route_AND", someTT_Route_AND);
		myFragment.setArguments(args);

		return myFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(getClass().getSimpleName(), "Neuer onCreate... ");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.comments_found_lv_w_header ,container);
		// Find the packet with the 'parent' object
//		Intent intent = getIntent();
//		Log.i(getClass().getSimpleName(), "Neuer intent... ");
//		aTT_Route_AND = intent.getParcelableExtra("TT_Route_AND");
        if (  getArguments() == null || !getArguments().containsKey("TT_Route_AND") ) {
            throw new IllegalArgumentException("getArguments() == null || !getArguments().containsKey(\"TT_Route_AND\")");
        }
        else {
            aTT_Route_AND = getArguments().getParcelable("TT_Route_AND");
        }
		lstTT_Comment_AND = new ArrayList<TT_Comment_AND>();
		// query all routes to this summit
		openAndQueryDatabase(aTT_Route_AND);
		listenAdapter = new TT_Comment_ANDAdapter(getActivity(), lstTT_Comment_AND,
				false);
		Log.i(getClass().getSimpleName(),
				"(ListView) findViewById(R.id.list_routes);");
		meinListView = (ListView) view.findViewById(R.id.list_comment);
		// working Code for the Header Creation:
		LayoutInflater layoutInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.i(getClass().getSimpleName(), "RelativeLayout myHeaderView  ... ");
		RelativeLayout myHeaderView = (RelativeLayout) layoutInflater.inflate(
				R.layout.route_activity_found_header, null, false);
		Log.i(getClass().getSimpleName(),
				"meinListView.addHeaderView(myHeaderView, null, false);... ");

		meinListView.addHeaderView(myHeaderView, null, false);
		// The e. is part of the header, will not be found before header is
		// added!
		spinnerRouteAsscended_inComment = (Spinner) view.findViewById(R.id.spinnerRouteAsscended_inComment);
		editTextMyRouteComment = (EditText) view.findViewById(R.id.editTextMyRouteComment);
		buttonRouteAscendDay = (Button) view.findViewById(R.id.buttonRouteAscendDay);
		// see
		// http://stackoverflow.com/questions/9770252/scrolling-editbox-inside-scrollview
		editTextMyRouteComment.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(final View v, final MotionEvent motionEvent) {
				if (v.getId() == R.id.editTextMyRouteComment) {
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
		// fill the Data in the Header
		fillRouteDetails(view);
		// set Date of Ascend in the linked button.
		updateDateAscended();

		// Define Action Listener for the ListView
		meinListView.setAdapter(listenAdapter);
		// ********************************************************
		// Event Listener
		editTextMyRouteComment.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				Log.v(getClass().getSimpleName(),
						"editTextMyRouteComment.addTextChanged: '" + s + "'");
				if (!aTT_Route_AND.getStrKommentar().equals(s)) {
					aTT_Route_AND.setStrKommentar(s.toString());
					hasUnSavedData = true;
					//not working: editTextMyRouteComment.removeTextChangedListener(this);
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
		spinnerRouteAsscended_inComment
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> adapter, View v,
							int position, long id) {
						// TODO Auto-generated method stub
						if (aTT_Route_AND.getBegehungsStil() != position) {
							aTT_Route_AND.setBegehungsStil(position);
							calendar = Calendar.getInstance(TimeZone
									.getTimeZone("CET"));
							if (buttonRouteAscendDay.getText().equals(getResources().getString(R.string.strChooseDate))){
								aTT_Route_AND.setDatumBestiegen(calendar.getTimeInMillis());
								updateDateAscended(); 
							}
							hasUnSavedData = true;
						}
						Log.v(getClass().getSimpleName(), "position: "
								+ position);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});
		tts = new TextToSpeech(getActivity(), this);
		meinListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getActivity(),
						lstTT_Comment_AND.get(position - 1)
								.getStrEntryKommentar(), Toast.LENGTH_LONG)
						.show();
				tts.speak(ImprovedText4Tts(lstTT_Comment_AND.get(position - 1)
						.getStrEntryKommentar()), TextToSpeech.QUEUE_FLUSH,
						null);
				// Log.i(TAG, "Click ListItem Number " + position
				// + "\r\nGipfelname: "
				// + lstTT_Gipfel_AND.get(position).getStr_SummitName());
			}
		});

		buttonRouteAscendDay.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Log.i(getClass().getSimpleName(),
						"Schreibe Routenkommentar... ");

				// ********************************************************
				// get the current date
				if (aTT_Route_AND.getLong_DateAsscended() != 0)
					calendar.setTimeInMillis(aTT_Route_AND.getLong_DateAsscended());
			    dateFragment = new DatePickerFragment();
			    dateFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
			   
				// ********************************************************
				Log.i(getClass().getSimpleName(),
						"datePickerDialog.show()...: ");
				// ********************************************************
			}
		});
		hasUnSavedData = false;
		// recreate content in editTextMyRouteComment (if saved)
		// ok we back, load the saved text
		if (savedInstanceState != null) {
			String savedText = savedInstanceState.getString(SAVED_TEXT_KEY);
			editTextMyRouteComment.setText(savedText);
		}
		Log.i(getClass().getSimpleName(),
				"Neuer onCreate komplett abgearbeitet... ");
		return view;
	}

	// ***************************
	private void updateDateAscended() {
		String strDatumBestiegenString = aTT_Route_AND.getDatumBestiegen();
		if (strDatumBestiegenString.equals("")) {
			strDatumBestiegenString = getActivity().getApplicationContext().getResources()
					.getString(R.string.strChooseDate);
		}
		buttonRouteAscendDay.setText(strDatumBestiegenString);
	}

	// **************************
	private void fillRouteDetails(View v) {
		Log.i(getClass().getSimpleName(), "aTT_Route_AND - fillRouteDetails: "
				+ aTT_Route_AND.getStrWegName());
		// Route Name
		((TextView) v.findViewById(R.id.textView_tableCol_RouteName_inComment))
				.setText(aTT_Route_AND.getStrWegName() + "  ("
						+ aTT_Route_AND.getStrSchwierigkeitsGrad() + ")");
		// Summit Name
		((TextView) v.findViewById(R.id.textView_tableCol_SummitName_inComment))
				.setText(strSummitName_inComment + ", " + strAreaName_inComment);
		// style of Ascended?
		// Creating adapter for spinner
		ArrayAdapter<EnumBegehungsStil> dataAdapter = new ArrayAdapter<EnumBegehungsStil>(
				getActivity(), android.R.layout.simple_spinner_item,
				EnumBegehungsStil.values());
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item); /* simple_spinner_dropdown_item */
		// attaching data adapter to spinner
		((Spinner) v.findViewById(R.id.spinnerRouteAsscended_inComment))
				.setAdapter(dataAdapter);
		((Spinner) v.findViewById(R.id.spinnerRouteAsscended_inComment))
				.setSelection(aTT_Route_AND.getBegehungsStil());
		// My Comment
		((TextView) v.findViewById(R.id.editTextMyRouteComment))
				.setText(aTT_Route_AND.getStrKommentar());

	}

	// **************************
	private void openAndQueryDatabase(TT_Route_AND aTT_Route_AND) {
		Log.i(getClass().getSimpleName(), "Neuer openAndQueryDatabase... "
				+ aTT_Route_AND.getStrWegName());

		try {
			DataBaseHelper dbHelper = new DataBaseHelper(getActivity());
			newDB = dbHelper.getWritableDatabase();
			Cursor cursor = null;
			String QueryString1;
			Log.i(getClass().getSimpleName(),
					"Namen und Gebiet zum Gipfel # : "
							+ aTT_Route_AND.getIntGipfelNr());
			QueryString1 = "SELECT a.[strName], a.[strGebiet]  "
					+ " from [TT_Summit_AND] a where a.[intTTGipfelNr]  = "
					+ aTT_Route_AND.getIntGipfelNr();
			cursor = newDB.rawQuery(QueryString1, null);
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					strSummitName_inComment = cursor.getString(cursor
							.getColumnIndex("strName"));
					strAreaName_inComment = cursor.getString(cursor
							.getColumnIndex("strGebiet"));
					Log.i(getClass().getSimpleName(),
							"Neuen Kommentar zum Weg im Gebiet: "
									+ strAreaName_inComment);
				}
			}

			QueryString1 = "SELECT a.[intTTWegNr], a.[strEntryKommentar], a.[entryBewertung] "
					+ ", a.[strEntryUser], a.[entryDatum] "
					+ "from [TT_RouteComment_AND] a where a.[intTTWegNr]  = "
					+ aTT_Route_AND.getIntWegNr()
					+ " ORDER BY a.[entryDatum] ASC ";
			Log.i(getClass().getSimpleName(),
					"Neuen Kommentar zum Weg suchen:\r\n" + QueryString1);

			cursor = newDB.rawQuery(QueryString1, null);
			Log.i(getClass().getSimpleName(),
					"Neuen Kommentar zum Weg suchen:\t c != null'"
							+ (cursor != null));

			if (cursor != null) {
				int iCounter = 0;
				if (cursor.moveToFirst()) {
					do {
						int intTTWegNr = cursor.getInt(cursor
								.getColumnIndex("intTTWegNr"));
						Log.i(getClass().getSimpleName(),
								" -> intTTWegNr..... " + intTTWegNr);
						String strEntryKommentar = cursor.getString(cursor
								.getColumnIndex("strEntryKommentar"));
						Log.i(getClass().getSimpleName(),
								" -> strEntryKommentar..... "
										+ strEntryKommentar);
						Integer intEntryBewertung = cursor.getInt(cursor
								.getColumnIndex("entryBewertung"));
						Log.i(getClass().getSimpleName(),
								" -> intEntryBewertung..... "
										+ intEntryBewertung);
						String strEntryUser = cursor.getString(cursor
								.getColumnIndex("strEntryUser"));
						Log.i(getClass().getSimpleName(),
								" -> strEntryUser..... " + strEntryUser);
						Long longEntryDatum = cursor.getLong(cursor
								.getColumnIndex("entryDatum"));
						Log.i(getClass().getSimpleName(),
								" -> longEntryDatum..... " + longEntryDatum);

						lstTT_Comment_AND.add(new TT_Comment_AND(intTTWegNr,
								strEntryKommentar, aTT_Route_AND
										.getStrGipfelName(), aTT_Route_AND
										.getStrGipfelName(), aTT_Route_AND
										.getIntGipfelNr(), intEntryBewertung,
								strEntryUser, longEntryDatum));
						Log.i(getClass().getSimpleName(), ++iCounter
								+ " -> Neuer Kommentar... " + strEntryUser);
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

	public static String ImprovedText4Tts(String RawText) {
		RawText = RawText.replace("SU", "Sanduhr");
		RawText = RawText.replace("AW", "alter Weg");
		RawText = RawText.replace("RP", "Rotpunkt");
		RawText = RawText.replace("z.B.", "zum Beispiel");
		RawText = RawText.replace(" NR.", " nachträglicher Ring");
		RawText = RawText
				.replace("XIII",
						"nu, dass ist schon ganz schön schwere, nicht wahr?")
				.replace("XII", "Zwölf").replace("XI", "Elf")
				.replace("X", "Zehn").replace("IX", "Neun")
				.replace("VIII", "Acht").replace("VII", "Sieben")
				.replace("VI", "Sechs").replace("IV", "Vier")
				.replace("V", "Fünf").replace("II", "Zwei")
				.replace("III", "Drei").replace("I", "Eins")
				.replace(" 1. R ", " erster Ring ")
				.replace(" 1.R ", " erster Ring ")
				.replace(" 2. R ", " zweiter Ring ")
				.replace(" 2.R ", " zweiter Ring ")
				.replace(" 3. R ", " dritter Ring ")
				.replace(" 3.R ", " dritter Ring ")
				.replace(" 4. R ", " vierter Ring ")
				.replace(" 4.R ", " vierter Ring ");
		return RawText;
	}

	@Override
	public void onInit(int status) {
		tts.setLanguage(Locale.GERMAN);
	}

	@Override
	public void onStop() {
		// Close the Text to Speech Library
		if (tts != null) {
			tts.stop();
			tts.shutdown();
			Log.d(getClass().getSimpleName(), "TTS Destroyed");
		}
		if (hasUnSavedData) {
			saveMyRouteComment();
			hasUnSavedData = false;
		}
		super.onStop();
	}

	private void saveMyRouteComment() {
		// Set the date String.
		StringBuilder strDatumBestiegen = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd",
				Locale.GERMANY);
		if (aTT_Route_AND.getLong_DateAsscended() != 0) {
			strDatumBestiegen.append("["
					+ sdf.format(aTT_Route_AND.getLong_DateAsscended())
					+ "] ");
		}
		Toast.makeText(getActivity(),
				"Saved Comment for this Route...\n"
						+ aTT_Route_AND.getStrKommentar()
						+ "\nand Comment for it's Summit...\n"
						+ strDatumBestiegen
						+ aTT_Route_AND.getStrWegName(), Toast.LENGTH_SHORT)
				.show();
		try {
			// RouteComment
			new StoreMyRouteComment(getActivity(),
					aTT_Route_AND.getIntWegNr(),
					aTT_Route_AND.getBegehungsStil(),
					aTT_Route_AND.getLong_DateAsscended(),
					aTT_Route_AND.getStrKommentar());
			// SummitComment
			new StoreMySummitComment(getActivity(),
			// Integer intTTGipfelNr
					aTT_Route_AND.getIntGipfelNr(),
					// Boolean isAscendedSummit
					(aTT_Route_AND.getBegehungsStil() > 2),
					// Long myLongDateOfAscend
					aTT_Route_AND.getLong_DateAsscended(),
					// String strMySummitComment
					strDatumBestiegen + aTT_Route_AND.getStrWegName(), true);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TT_SummitFoundActivity.hasChangedData();
		TT_SummitsFoundActivity.hasChangedData();
		TT_RoutesFoundFragment.hasChangedData();
	}
	

	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		// now, save the text if something overlaps this Activity
		savedInstanceState.putString(SAVED_TEXT_KEY, editTextMyRouteComment
				.getText().toString());
	}
	public TT_Route_AND getTT_Route_AND() {
		return aTT_Route_AND;
	}
	public Button getButtonMyAscendDate() {
		return buttonRouteAscendDay;
	}

	public void setHasUnSavedData(boolean hasUnSavedData) {
		TT_RouteFoundFragment.hasUnSavedData = hasUnSavedData;
	}
}
