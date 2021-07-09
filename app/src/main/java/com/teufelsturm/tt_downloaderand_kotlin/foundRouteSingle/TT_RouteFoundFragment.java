//package com.teufelsturm.tt_downloaderand_kotlin.foundRouteSingle;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteException;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.SpannableString;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemSelectedListener;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.DialogFragment;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.teufelsturm.tt_downloader3.MainActivity;
//import com.teufelsturm.tt_downloader3.R;
//import com.teufelsturm.tt_downloader3.model.TT_Comment_AND;
//import com.teufelsturm.tt_downloader3.model.TT_Route_AND;
//import com.teufelsturm.tt_downloaderand_kotlin.dbHelper.DataBaseHelper;
//import com.teufelsturm.tt_downloaderand_kotlin.dbHelper.StoreMyRouteComment;
//import com.teufelsturm.tt_downloaderand_kotlin.dbHelper.StoreMySummitComment;
//import com.teufelsturm.tt_downloaderand_kotlin.foundCommentsList.TT_Comment_ANDAdapter;
//import com.teufelsturm.tt_downloaderand_kotlin.foundRoutesList.TT_RoutesFoundFragment;
//import com.teufelsturm.tt_downloaderand_kotlin.foundSummitList.TT_SummitsFoundFragment;
//import com.teufelsturm.tt_downloaderand_kotlin.foundSummitSingle.TT_SummitFoundFragment;
//import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumBegehungsStil;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.sql.SQLException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Locale;
//import java.util.TimeZone;
//
//public class TT_RouteFoundFragment extends Fragment {
//
//    private static final String TAG = TT_RouteFoundFragment.class.getSimpleName();
//    public static final String ID = "TT_RouteFoundFragment";
//    public static final String SAVED_TEXT_KEY = "SavedText";
//
//	private ArrayList<TT_Comment_AND> lstTT_Comment_AND;
//	private SQLiteDatabase newDB;
//	private TT_Route_AND aTT_Route_AND;
//	private String strSummitName_inComment;
//	private String strAreaName_inComment;
////	private TextToSpeech tts;
//	private DialogFragment dateFragment;
//	private Calendar calendar = Calendar.getInstance(TimeZone
//			.getTimeZone("CET"));
//	private EditText editTextMyRouteComment;
//	private Button buttonRouteAscendDay;
//	private boolean hasUnSavedData;
//
//	public static TT_RouteFoundFragment newInstance(TT_Route_AND someTT_Route_AND) {
//		TT_RouteFoundFragment myFragment = new TT_RouteFoundFragment();
//
//		Bundle args = new Bundle();
//		args.putParcelable("TT_Route_AND", someTT_Route_AND);
//		myFragment.setArguments(args);
//		return myFragment;
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.i(TAG, "Neuer onCreate... ");
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
//							 Bundle savedInstanceState) {
//	    View view = inflater.inflate(R.layout.route_activity_found_header, container, false);
//		// Find the packet with the 'parent' object
////		Intent intent = getIntent();
////		Log.i(TAG, "Neuer intent... ");
////		aTT_Route_AND = intent.getParcelableExtra("TT_Route_AND");
//        if (  getArguments() == null || !getArguments().containsKey("TT_Route_AND") ) {
//            throw new IllegalArgumentException("getArguments() == null || !getArguments().containsKey(\"TT_Route_AND\")");
//        }
//        else {
//            aTT_Route_AND = getArguments().getParcelable("TT_Route_AND");
//        }
//		lstTT_Comment_AND = new ArrayList<>();
//		// query all routes to this summit
//		openAndQueryDatabase(aTT_Route_AND);
//		TT_Comment_ANDAdapter listenAdapter = new TT_Comment_ANDAdapter((MainActivity) getActivity(),
//				lstTT_Comment_AND,
//				false);
//		Log.i(TAG,
//				"(ListView) findViewById(R.id.list_routes);");
//        RecyclerView mRecyclerviewRoutesInRoutesFoundRoute = view.findViewById(R.id.recyclerview_routes_in_routes_found_route);
//		// fill the Data in the Header
//		fillRouteDetails(view);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mRecyclerviewRoutesInRoutesFoundRoute.setLayoutManager(linearLayoutManager);
//
//		// Define Action Listener for the ListView
//        mRecyclerviewRoutesInRoutesFoundRoute.setAdapter(listenAdapter);
//		return view;
//	}
//
//	@Override
//	public void onAttach(Context context) {
//		super.onAttach(context);
//	}
//
//	@Override
//	public void onViewCreated(@NotNull View view, Bundle savedInstanceState){
//        super.onViewCreated(view, savedInstanceState);
//        // ********************************************************
//        // Event Listener
//        // The e. is part of the header, will not be found before header is
//        // added!
//        Spinner spinnerRouteAsscended_inComment
//                = view.findViewById(R.id.spinnerRouteAsscended_inComment_route);
//        editTextMyRouteComment = view.findViewById(R.id.editTextMyRouteComment_route);
//        buttonRouteAscendDay = view.findViewById(R.id.buttonRouteAscendDay_route);
//        // see
//        editTextMyRouteCommentSetOnTouchListener();
//
//        editTextMyRouteCommentAddTextChangedListener();
//        spinnerRouteAsscended_inCommentSetOnItemSelectedListener(spinnerRouteAsscended_inComment);
////		tts = new TextToSpeech(getActivity(), this);
////		meinListView.setOnItemClickListener(new OnItemClickListener() {
////			@Override
////			public void onItemClick(AdapterView<?> parent, View view,
////					int position, long id) {
////				Toast.makeText(getActivity(),
////						lstTT_Comment_AND.get(position - 1)
////								.getStrEntryKommentar(), Toast.LENGTH_LONG)
////						.show();
////				tts.speak(ImprovedText4Tts(lstTT_Comment_AND.get(position - 1)
////						.getStrEntryKommentar()), TextToSpeech.QUEUE_FLUSH,
////						null);
////				// Log.i(TAG, "Click ListItem Number " + position
////				// + "\r\nGipfelname: "
////				// + lstTT_Gipfel_AND.get(position).getStr_SummitName());
////			}
////		});
//
//        buttonRouteAscendDaySetOnClickListener();
//        hasUnSavedData = false;
//        // recreate content in editTextMyRouteComment (if saved)
//        // ok we back, load the saved text
//        if (savedInstanceState != null) {
//            String savedText = savedInstanceState.getString(SAVED_TEXT_KEY);
//            editTextMyRouteComment.setText(savedText);
//        }
//
//        // set Date of Ascend in the linked button.
//        updateDateAscended();
//
//        Log.i(TAG,
//                "Neuer onCreate komplett abgearbeitet... ");
//
//		((MainActivity)getActivity()).showFAB(ID);
//
//    }
//
//    private void buttonRouteAscendDaySetOnClickListener() {
//        buttonRouteAscendDay.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v) {
//                Log.i(TAG,"Schreibe Routenkommentar... ");
//
//                // ********************************************************
//                // get the current date
//                if (aTT_Route_AND.getLong_DateAsscended() != 0)
//                    calendar.setTimeInMillis(aTT_Route_AND.getLong_DateAsscended());
//                dateFragment = DatePickerFragment.newInstance(
//                        TT_RouteFoundFragment.ID, aTT_Route_AND,
//                        R.id.buttonRouteAscendDay_route);
//                dateFragment.show(getActivity().getSupportFragmentManager(), "datePickerDialog");
//
//                // ********************************************************
//                Log.i(TAG,
//                        "datePickerDialog.show()...: ");
//                // ********************************************************
//            }
//        });
//    }
//
//    private void spinnerRouteAsscended_inCommentSetOnItemSelectedListener(Spinner spinnerRouteAsscended_inComment) {
//        spinnerRouteAsscended_inComment.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> adapter, View v,
//                                       int position, long id) {
//                // TODO Auto-generated method stub
//                if (aTT_Route_AND.getBegehungsStil() != position) {
//                    aTT_Route_AND.setBegehungsStil(position);
//                    calendar = Calendar.getInstance(TimeZone
//                            .getTimeZone("CET"));
//                    if (buttonRouteAscendDay.getText().equals(getResources().getString(R.string.strChooseDate))){
//                        aTT_Route_AND.setDatumBestiegen(calendar.getTimeInMillis());
//                        updateDateAscended();
//                    }
//                    hasUnSavedData = true;
//                }
//                Log.v(TAG, "position: "
//                        + position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//            }
//        });
//    }
//
//    private void editTextMyRouteCommentAddTextChangedListener() {
//        editTextMyRouteComment.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                Log.v(TAG,
//                        "editTextMyRouteComment.addTextChanged: '" + s + "'");
//                if (!aTT_Route_AND.getStrKommentar().equals(s)) {
//                    aTT_Route_AND.setStrKommentar(s.toString());
//                    hasUnSavedData = true;
//                    //not working: editTextMyRouteComment.removeTextChangedListener(this);
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // TODO Auto-generated method stub
//
//            }
//        });
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    private void editTextMyRouteCommentSetOnTouchListener() {
//        // http://stackoverflow.com/questions/9770252/scrolling-editbox-inside-scrollview
//        editTextMyRouteComment.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(final View v, final MotionEvent motionEvent) {
//                if (v.getId() == R.id.editTextMyRouteComment_route) {
//                    v.getParent().requestDisallowInterceptTouchEvent(true);
//                    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
//                        case MotionEvent.ACTION_UP:
//                            v.getParent().requestDisallowInterceptTouchEvent(false);
//                            break;
//                    }
//                }
//                return false;
//            }
//        });
//    }
//
//    // ***************************
//	private void updateDateAscended() {
//		String strDatumBestiegenString = aTT_Route_AND.getDatumBestiegen();
//		if (strDatumBestiegenString.equals("")) {
//			strDatumBestiegenString = getActivity().getApplicationContext().getResources()
//					.getString(R.string.strChooseDate);
//		}
//		buttonRouteAscendDay.setText(strDatumBestiegenString);
//	}
//
//	// **************************
//	private void fillRouteDetails(View v) {
//		Log.i(TAG, "aTT_Route_AND - fillRouteDetails: "
//				+ aTT_Route_AND.getStrWegName());
//		// Route Name
//		((TextView) v.findViewById(R.id.textView_tableCol_RouteName_inComment_route))
//				.setText(String.format("%s  (%s)",
//						aTT_Route_AND.getStrWegName(), aTT_Route_AND.getStrSchwierigkeitsGrad()));
//		// Summit Name
//		((TextView) v.findViewById(R.id.textView_tableCol_SummitName_inComment_route))
//				.setText(String.format("%s, %s",
//						strSummitName_inComment, strAreaName_inComment));
//		// style of Ascended?
//		// Creating adapter for spinner
//		ArrayAdapter<SpannableString> dataAdapter = new ArrayAdapter<SpannableString>(
//				getActivity().getApplicationContext(), android.R.layout.simple_spinner_item,
//				EnumBegehungsStil.getBegehungsStile(getContext()));
//		dataAdapter
//				.setDropDownViewResource(android.R.layout.simple_spinner_item); /* simple_spinner_dropdown_item */
//		// attaching data adapter to spinner
//		((Spinner) v.findViewById(R.id.spinnerRouteAsscended_inComment_route))
//				.setAdapter(dataAdapter);
//		((Spinner) v.findViewById(R.id.spinnerRouteAsscended_inComment_route))
//				.setSelection(aTT_Route_AND.getBegehungsStil());
//		// My Comment
//		((TextView) v.findViewById(R.id.editTextMyRouteComment_route))
//				.setText(aTT_Route_AND.getStrKommentar());
//
//	}
//
//	// **************************
//	private void openAndQueryDatabase(@NotNull TT_Route_AND aTT_Route_AND) {
//		Log.i(TAG, "Neuer openAndQueryDatabase... " + aTT_Route_AND.getStrWegName());
//		try {
//			DataBaseHelper dbHelper = new DataBaseHelper(getActivity());
//			newDB = dbHelper.getWritableDatabase();
//			Cursor cursor = null;
//			String QueryString1;
//			Log.i(TAG,
//					"Namen und Gebiet zum Gipfel # : "
//							+ aTT_Route_AND.getIntGipfelNr());
//			QueryString1 = new StringBuilder()
//                    .append("SELECT a.[strName], a.[strGebiet]  ")
//                    .append(" from [TT_Summit_AND] a where a.[intTTGipfelNr]  = ")
//                    .append(aTT_Route_AND.getIntGipfelNr()).toString();
//			cursor = newDB.rawQuery(QueryString1, null);
//			if (cursor != null) {
//				if (cursor.moveToFirst()) {
//					strSummitName_inComment = cursor.getString(cursor
//							.getColumnIndex("strName"));
//					strAreaName_inComment = cursor.getString(cursor
//							.getColumnIndex("strGebiet"));
//					Log.i(TAG,"Neuen Kommentar zum Weg im Gebiet: "
//									+ strAreaName_inComment);
//				}
//			}
//
//			QueryString1 = new StringBuilder()
//                    .append("SELECT a.[intTTWegNr], a.[strEntryKommentar], a.[entryBewertung] ")
//                    .append(", a.[strEntryUser], a.[entryDatum] ")
//                    .append("from [TT_RouteComment_AND] a where a.[intTTWegNr]  = ")
//                    .append(aTT_Route_AND.getIntWegNr())
//                    .append(" ORDER BY a.[entryDatum] ASC ").toString();
//			Log.i(TAG,"Neuen Kommentar zum Weg suchen:\r\n" + QueryString1);
//
//			cursor = newDB.rawQuery(QueryString1, null);
//			Log.i(TAG,"Neuen Kommentar zum Weg suchen:\t c != null'"
//							+ (cursor != null));
//
//			if (cursor != null) {
//				int iCounter = 0;
//				if (cursor.moveToFirst()) {
//					do {
//						int intTTWegNr = cursor.getInt(cursor
//								.getColumnIndex("intTTWegNr"));
//						Log.i(TAG," -> intTTWegNr..... " + intTTWegNr);
//						String strEntryKommentar = cursor.getString(cursor
//								.getColumnIndex("strEntryKommentar"));
//						Log.i(TAG," -> strEntryKommentar..... "
//										+ strEntryKommentar);
//						Integer intEntryBewertung = cursor.getInt(cursor
//								.getColumnIndex("entryBewertung"));
//						Log.i(TAG," -> intEntryBewertung..... "
//										+ intEntryBewertung);
//						String strEntryUser = cursor.getString(cursor
//								.getColumnIndex("strEntryUser"));
//						Log.i(TAG," -> strEntryUser..... " + strEntryUser);
//						Long longEntryDatum = cursor.getLong(cursor
//								.getColumnIndex("entryDatum"));
//						Log.i(TAG," -> longEntryDatum..... " + longEntryDatum);
//
//						lstTT_Comment_AND.add(new TT_Comment_AND(intTTWegNr,
//								strEntryKommentar, aTT_Route_AND
//										.getStrGipfelName(), aTT_Route_AND
//										.getStrGipfelName(), aTT_Route_AND
//										.getIntGipfelNr(), intEntryBewertung,
//								strEntryUser, longEntryDatum));
//						Log.i(TAG,++iCounter + " -> Neuer Kommentar... " + strEntryUser);
//					} while (cursor.moveToNext());
//				}
//			}
//		} catch (SQLiteException se) {
//			Log.e(TAG,
//					"Could not create or Open the database");
//		} finally {
//			newDB.close();
//		}
//	}
//
//	@Override
//	public void onStop() {
//		if (hasUnSavedData) {
//			saveMyRouteComment();
//			hasUnSavedData = false;
//		}
//		super.onStop();
//	}
//
//	private void saveMyRouteComment() {
//		// Set the date String.
//		StringBuilder strDatumBestiegen = new StringBuilder();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd",
//				Locale.GERMANY);
//		if (aTT_Route_AND.getLong_DateAsscended() != 0) {
//			strDatumBestiegen.append("["
//					+ sdf.format(aTT_Route_AND.getLong_DateAsscended())
//					+ "] ");
//		}
//		Toast.makeText(getActivity(),
//				"Saved Comment for this Route...\n"
//						+ aTT_Route_AND.getStrKommentar()
//						+ "\nand Comment for it's Summit...\n"
//						+ strDatumBestiegen
//						+ aTT_Route_AND.getStrWegName(), Toast.LENGTH_SHORT)
//				.show();
//		try {
//			// RouteComment
//			new StoreMyRouteComment(getActivity(),
//					aTT_Route_AND.getIntWegNr(),
//					aTT_Route_AND.getBegehungsStil(),
//					aTT_Route_AND.getLong_DateAsscended(),
//					aTT_Route_AND.getStrKommentar());
//			// SummitComment
//			new StoreMySummitComment(getActivity(),
//			// Integer intTTGipfelNr
//					aTT_Route_AND.getIntGipfelNr(),
//					// Boolean isAscendedSummit
//					(aTT_Route_AND.getBegehungsStil() > 2),
//					// Long myLongDateOfAscend
//					aTT_Route_AND.getLong_DateAsscended(),
//					// String strMySummitComment
//					strDatumBestiegen + aTT_Route_AND.getStrWegName(), true);
//		} catch (ClassNotFoundException e) {
//		    Log.e(TAG,e.getMessage(), e);
//		    throw new IllegalStateException(e.getCause());
//		} catch (SQLException e) {
//            Log.e(TAG,e.getMessage(), e);
//            throw new IllegalStateException(e.getCause());
//		}
//
//        FragmentManager fm = getActivity().getSupportFragmentManager();
//        TT_SummitFoundFragment tt_summitFoundFragment =
//                (TT_SummitFoundFragment) fm.findFragmentByTag(TT_SummitFoundFragment.ID);
////        TODO:         tt_summitFoundFragment.hasChangedData();
//        TT_SummitsFoundFragment tt_summitsFoundFragment =
//                (TT_SummitsFoundFragment) fm.findFragmentByTag(TT_SummitsFoundFragment.ID);
////        TODO:         tt_summitsFoundFragment.hasChangedData();
//        TT_RoutesFoundFragment tt_routesFoundFragment =
//                (TT_RoutesFoundFragment) fm.findFragmentByTag(TT_RoutesFoundFragment.ID);
////        TODO: tt_routesFoundFragment.hasChangedData(
////                strTextSuchtext, strGebiet, intMinSchwierigkeit, intMaxSchwierigkeit,
////                intMinAnzahlDerKommentare, intMittlereWegBewertung);
//	}
//
//
//	public void onSaveInstanceState(Bundle savedInstanceState) {
//		super.onSaveInstanceState(savedInstanceState);
//		// now, save the text if something overlaps this Activity
//		savedInstanceState.putString(SAVED_TEXT_KEY, editTextMyRouteComment
//				.getText().toString());
//	}
//}
