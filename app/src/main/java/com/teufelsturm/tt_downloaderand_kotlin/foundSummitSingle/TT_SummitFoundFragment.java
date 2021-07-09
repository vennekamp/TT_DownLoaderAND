//package com.teufelsturm.tt_downloaderand_kotlin.foundSummitSingle;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteException;
//import android.location.Location;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.teufelsturm.tt_downloader3.MainActivity;
//import com.teufelsturm.tt_downloader3.R;
//import com.teufelsturm.tt_downloader3.model.TT_Route_AND;
//import com.teufelsturm.tt_downloader3.model.TT_Summit_AND;
//import com.teufelsturm.tt_downloaderand_kotlin.dbHelper.DataBaseHelper;
//import com.teufelsturm.tt_downloaderand_kotlin.dbHelper.StoreMySummitComment;
//import com.teufelsturm.tt_downloaderand_kotlin.foundRoutesList.TT_Route_ANDAdapter;
//import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.DatePickerFragment;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Locale;
//import java.util.Map;
//
//public class TT_SummitFoundFragment extends Fragment
//        implements View.OnClickListener  {
//
//    private static final String TAG = TT_SummitFoundFragment.class.getSimpleName();
//    public static final String ID = "TT_SummitFoundFragment";
//    private static final String TT_GIPFEL_AND = "TT_GIPFEL_AND";
//
//	private ArrayList<TT_Route_AND> lstTT_Route_AND;
//    private SQLiteDatabase newDB;
//	private TT_Summit_AND aTT_Summit_AND;
//	private TT_Route_ANDAdapter listenAdapter;
//	private Map<Integer, String> hashmapNeighbourSummit = new HashMap<>();
//	private static Boolean dataHasChanged = Boolean.FALSE;
//	private DialogFragment dateFragment;
//	private static boolean hasUnSavedData;
//	private EditText editTextMySummitComment;
//	private RecyclerView mRecyclerViewSummitFound;
//	private Button buttonSummitAscendDay_inComment;
//	private CheckBox checkBoxSummitAsscended;
//	public static final String SAVED_TEXT_KEY = "SavedText";
//
//	public static TT_SummitFoundFragment newInstance(TT_Summit_AND tt_summit_and) {
//		if ( tt_summit_and == null ){
//		    throw new NullPointerException("tt_summit_and is null");
//        }
//		TT_SummitFoundFragment tt_summitFoundFragment = new TT_SummitFoundFragment();
//
//		Bundle args = new Bundle();
//		args.putParcelable(TT_GIPFEL_AND, tt_summit_and);
//		tt_summitFoundFragment.setArguments(args);
//		Log.i(TAG, "Neuer newInstance(TT_Su...");
//
//		return tt_summitFoundFragment;
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		Log.i(TAG, "onResume ");
//		if (dataHasChanged) {
//			Log.i(TAG, "onResume; dataHasChanged --> " + dataHasChanged);
//			assert getArguments() != null;
//			aTT_Summit_AND = getArguments().getParcelable(TT_GIPFEL_AND);
//			assert aTT_Summit_AND != null;
//			this.openAndQueryDatabase(aTT_Summit_AND);
//			listenAdapter.notifyDataSetChanged();
//			dataHasChanged = Boolean.FALSE;
//		}
//	}
//
//	public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
//	    super.onSaveInstanceState(savedInstanceState);
//		// now, save the text if something overlaps this Activity
//		savedInstanceState.putString(SAVED_TEXT_KEY, editTextMySummitComment
//				.getText().toString());
//	}
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.summit_found_lv_w_header, container, false);
////		setContentView(R.layout.summit_found_lv_w_header);
//		// has to be of type "layout"
//		// Find the packet with the 'parent' object
////		Intent intent = getIntent();
//		Log.i(TAG, "Neuer intent...");
//		assert getArguments() != null;
//		aTT_Summit_AND = getArguments().getParcelable(TT_GIPFEL_AND);
//        Log.i(TAG, "aTT_Summit_AND == null...? " + (aTT_Summit_AND == null));
//		if ( aTT_Summit_AND == null ) {
//		    throw new NullPointerException(" aTT_Summit_AND == null ");
//        }
//		lstTT_Route_AND = new ArrayList<>();
//		// query all routes to this summit
//		this.openAndQueryDatabase(aTT_Summit_AND);
//		listenAdapter = new TT_Route_ANDAdapter(this,
//				lstTT_Route_AND, false);
//		Log.i(TAG,"(ListView) findViewById(R.id.list_routes);");
//		mRecyclerViewSummitFound = view.findViewById(R.id.list_summit_found_lv_w_header);
//
//		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//		mRecyclerViewSummitFound.setLayoutManager(linearLayoutManager);
//
//		// the editTextMySummitComment is part of the header - can not be found
//		// before...
//		editTextMySummitComment = view.findViewById(R.id.editTextMySummitComment);
//		editTextMySummitComment.setText(aTT_Summit_AND.getStr_MyComment());
//		// http://stackoverflow.com/questions/9770252/scrolling-editbox-inside-scrollview
//        editTextMySummitCommentSetOnTouchListener();
//        editTextMySummitCommentAddTextChangedListener();
//        checkBoxSummitAsscended = view.findViewById(R.id.CheckBoxSummitAsscended);
//		return view;
//	}
//
//	@Override
//	public void onAttach(Context context) {
//		super.onAttach(context);
//		((MainActivity)context).showFAB(ID);
//	}
//
//
//    @SuppressLint("ClickableViewAccessibility")
//    private void editTextMySummitCommentSetOnTouchListener() {
//        editTextMySummitComment.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(final View v, final MotionEvent motionEvent) {
//                if (v.getId() == R.id.editTextMySummitComment) {
//                    v.getParent().requestDisallowInterceptTouchEvent(true);
//                    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
//                    case MotionEvent.ACTION_UP:
//                        v.getParent().requestDisallowInterceptTouchEvent(false);
//                        break;
//                    }
//                }
//                return false;
//            }
//        });
//    }
//
//    private void editTextMySummitCommentAddTextChangedListener() {
//        editTextMySummitComment.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                    int count) {
//                Log.v(TAG,
//                        "editTextMyRouteComment.addTextChanged: " + s);
//                if (!aTT_Summit_AND.getStr_MyComment().contentEquals(s)) {
//                    aTT_Summit_AND.setStr_MyComment(s.toString());
//                    hasUnSavedData = true;
//                    // not working: editTextMySummitComment.removeTextChangedListener(this);
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                    int after) {
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
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
////        final TT_Summit_AND aTT_Summit_AND = getArguments().getParcelable("TT_Gipfel_AND");
//        // fill the Data in the Header
//        fillSummitDetails(view, aTT_Summit_AND);
//        mRecyclerViewSummitFound.setAdapter(listenAdapter);
//        // recreate content in editTextMySummitComment (if saved)
//        // ok we back, load the saved text
//        if (savedInstanceState != null) {
//            String savedText = savedInstanceState.getString(SAVED_TEXT_KEY);
//            editTextMySummitComment.setText(savedText);
//        }
//
//        checkBoxSummitAsscended.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                hasUnSavedData = true;
//            }
//        });
//
//
//        buttonSummitAscendDay_inComment = view.findViewById(R.id.buttonSummitAscendDay_inComment);
//        Log.v(TAG, "buttonSummitAscendDay_inComment: " + buttonSummitAscendDay_inComment);
//        updateDateAscended(aTT_Summit_AND);
//        // Define Action Listener
//        buttonSummitAscendDay_inComment.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v) {
//                // ********************************************************
//                dateFragment = DatePickerFragment.newInstance(ID,
//						aTT_Summit_AND,
//						R.id.buttonSummitAscendDay_inComment);
//                dateFragment.show(getActivity().getSupportFragmentManager(),"datePickerDialog");
//                // ********************************************************
//                Log.i(TAG,"datePickerDialog.show()...: ");
//                // ********************************************************
//            }
//        });
//        hasUnSavedData = false;
//        actionListenertextView_GpsCoords();
//
//        ((MainActivity)getActivity()).showFAB(ID);
//        Log.i(TAG,"Neuer onCreate komplett abgearbeitet... ");
//    }
//
//	@Override
//	public void onStop() {
//		if (hasUnSavedData) {
//			saveMySummitComment();
//			hasUnSavedData = false;
//		}
//		super.onStop();
//	}
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (getView() != null) {
//            ViewGroup parent = (ViewGroup) getView().getParent();
//            if (parent != null) {
//                parent.removeAllViews();
//            }
//        }
//    }
//
//	public void saveMySummitComment() {
////        TT_Summit_AND aTT_Summit_AND = getArguments().getParcelable(TT_GIPFEL_AND);
//
//        Toast.makeText(getActivity().getApplicationContext(),
//				"Saved Comment for this Summit...\n"
//						+ aTT_Summit_AND.getStr_DateAsscended() + ":\n"
//						+ aTT_Summit_AND.getStr_SummitName(), Toast.LENGTH_SHORT)
//						.show();
//		try {
//			aTT_Summit_AND
//					.setBln_Asscended(checkBoxSummitAsscended.isChecked());
//			aTT_Summit_AND.setStr_MyComment(editTextMySummitComment
//					.getText().toString());
//			new StoreMySummitComment(getActivity().getApplicationContext(),
//					aTT_Summit_AND.getInt_TTGipfelNr(), aTT_Summit_AND
//							.getBln_Asscended(), aTT_Summit_AND
//							.getLong_DateAsscended(), aTT_Summit_AND
//							.getStr_MyComment(), false);
//            FragmentManager fm = getActivity().getSupportFragmentManager();
//
//            TT_SummitsFoundFragment tt_summitsFoundFragment =
//                    (TT_SummitsFoundFragment) fm.findFragmentByTag(TT_SummitsFoundFragment.ID);
//            tt_summitsFoundFragment.hasChangedData();
//		} catch (Exception ex) {
//			Log.i(TAG, ex.toString());
//		}
//	}
//
//	private void actionListenerButton_NeighbourSummit(Button button_NeighbourSummit,
//			int button_NeighbourSummitId) {
//		// Define Action Listener
//		Log.i(TAG, "buttonSearchSummit ...: "
//				+ button_NeighbourSummit.getText());
//		button_NeighbourSummit.setOnClickListener(new Button.OnClickListener() {
//			public void onClick(View v) {
//				Button b = (Button) v;
//				Integer bInt = (Integer) b.getTag();
//				String buttonText = b.getText().toString();
//				Log.i(TAG,
//						"Click button_NeighbourSummit for Summit..."
//								+ buttonText + "\r\n" + bInt);
//                Log.i(TAG,"Start TT_RouteFoundFragment... ");
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                TT_Summit_AND tt_summit_andNext = new TT_Summit_AND(bInt, getActivity());
//                TT_SummitFoundFragment tt_summitFoundFragment =
//                        TT_SummitFoundFragment.newInstance(tt_summit_andNext);
//                FragmentTransaction ft = fm.beginTransaction();
//                // Replace whatever is in the fragment_container view with this fragment,
//                // and add the transaction to the back stack if needed
//                ft.replace(R.id.fragment_container, tt_summitFoundFragment,
//                        TT_SummitFoundFragment.ID);
//                ft.addToBackStack(null);
//                // Commit the transaction
//                ft.commit();
//			}
//		});
//	}
//
//	private void actionListenertextView_GpsCoords() {
//		// Define Action Listener
//		Button textView_GpsCoords = (Button) getView().findViewById(R.id.textView_GpsCoords);
//		// Log.i(TAG, "buttonSearchSummit ...: " +
//		// buttonSaveSummit.toString());
//		textView_GpsCoords.setOnClickListener(new TextView.OnClickListener() {
//			public void onClick(View v) {
//				Toast.makeText(getActivity(),
//						"Click on textView_GpsCoords...", Toast.LENGTH_LONG)
//						.show();
//
//				// TODO: WHAT to show here? Maybe the MAP???
////                Log.i(TAG,"Start TT_RouteFoundFragment... ");
////                FragmentManager fm = getActivity().getSupportFragmentManager();
////                TT_Summit_AND tt_summit_andNext = new TT_Summit_AND(bInt, getActivity();
////                TT_SummitCoord_TabWidget tt_summitCoord_tabWidget =
////                        TT_SummitCoord_TabWidget.newInstance(aTT_Summit_AND);
////                FragmentTransaction ft = fm.beginTransaction();
////                // Replace whatever is in the fragment_container view with this fragment,
////                // and add the transaction to the back stack if needed
////                ft.replace(R.id.fragment_container, tt_summitFoundFragment,
////                        TT_SummitFoundFragment.ID);
////                ft.addToBackStack(null);
////                // Commit the transaction
////                ft.commit();
////
////
////				Intent addonPageSummitsFoundActivity = new Intent(
////				/* TT_SummitFoundFragment.this, MapsOsmDroidActivity.class */
////				/* TT_SummitFoundFragment.this, MapsForgeActivity.class */
////				_TT_SummitFoundActivity.this, _TT_SummitCoord_TabWidget.class);
////
////				// Log.i(TAG, "startActivity... ");
////				addonPageSummitsFoundActivity.putExtra("TT_Gipfel_AND",
////						aTT_Summit_AND);
////				startActivity(addonPageSummitsFoundActivity);
//			}
//		});
//	}
//
//	private void fillSummitDetails(View view, TT_Summit_AND aTT_Summit_AND) {
//		Log.i(TAG,"aTT_Gipfel_AND - fillSummitDetails: 1"
//						+ String.valueOf(aTT_Summit_AND.getInt_TTGipfelNr())
//						+ " --> " + aTT_Summit_AND.getStr_SummitName());
//		// Summit Name
//		((TextView) view.findViewById(R.id.textView_SummitName))
//				.setText(String.format(Locale.GERMANY, "%s   (KleFü #%d)",
//                        aTT_Summit_AND.getStr_SummitName(),
//                        aTT_Summit_AND.getInt_SummitNumberOfficial()));
//
//		// Area Name
//		((TextView) getView().findViewById(R.id.textView_Area)).setText(String.format("%s%s", getResources()
//				.getString(R.string.lblGebiet), aTT_Summit_AND.getStr_Area()));
//		Log.i(TAG,
//				"aTT_Gipfel_AND - fillSummitDetails: 2");
//		Log.i(TAG,
//				"aTT_Gipfel_AND - fillSummitDetails: 3");
//		// *******************************************************************************
//		// Create the text for the Neighbor Summit and their actionListener
//		int[] arrNeighbourSummits = new int[] { R.id.button_NeighbourSummit01,
//				R.id.button_NeighbourSummit02, R.id.button_NeighbourSummit03,
//				R.id.button_NeighbourSummit04 };
//		// Neighbor Summit #01..04
//		int iCount = 0;
//		for (Map.Entry<Integer, String> strH : hashmapNeighbourSummit
//				.entrySet()) {
//			((Button) getView().findViewById(arrNeighbourSummits[iCount])).setTag(strH
//					.getKey());
//			((Button) getView().findViewById(arrNeighbourSummits[iCount])).setText((strH
//					.getValue()));
//			Log.i(TAG,
//					"aTT_Gipfel_AND - fillSummitDetails: Neighbor Summit #0"
//							+ (iCount + 1));
//			actionListenerButton_NeighbourSummit(
//                    (Button) getView().findViewById(arrNeighbourSummits[iCount]),
//			        arrNeighbourSummits[iCount]);
//			iCount++;
//		}
//		// is Ascended?
//		checkBoxSummitAsscended.setChecked(aTT_Summit_AND.getBln_Asscended());
//		Log.i(TAG,
//				"aTT_Gipfel_AND - fillSummitDetails: is Ascended? "
//						+ aTT_Summit_AND.getBln_Asscended());
//		// My Comment
//		editTextMySummitComment.setText(aTT_Summit_AND.getStr_MyComment());
//
//		Log.i(TAG,
//				"aTT_Gipfel_AND - fillSummitDetails erledigt: "
//						+ aTT_Summit_AND.getInt_TTGipfelNr().toString()
//						+ " --> " + aTT_Summit_AND.getStr_SummitName());
//	}
//
//	private void openAndQueryDatabase(TT_Summit_AND aTT_Summit_AND) {
//		Log.i(TAG, "Neuer openAndQueryDatabase... " + aTT_Summit_AND.getStr_SummitName());
//		// IMPORTANT: old List needs to be cleared!
//		lstTT_Route_AND.clear();
//		try {
//			DataBaseHelper dbHelper = new DataBaseHelper(
//					getActivity().getApplicationContext());
//			newDB = dbHelper.getWritableDatabase();
//
//			String QueryString1;
//			Cursor cursor = null;
//			QueryString1 = new StringBuilder("SELECT a.[isAscendedSummit], a.[intDateOfAscend], a.[strMySummitComment] ")
//					.append("FROM [myTT_Summit_AND] a").append(" WHERE a.[intTTGipfelNr] =  ")
//					.append(aTT_Summit_AND.getInt_TTGipfelNr()).toString();
//			cursor = newDB.rawQuery(QueryString1, null);
//			if (cursor != null && cursor.moveToFirst()) {
//                aTT_Summit_AND.setBln_Asscended(cursor.getInt(cursor
//						.getColumnIndex("isAscendedSummit")) > 0);
//                aTT_Summit_AND.setDatumBestiegen(cursor.getLong(cursor
//						.getColumnIndex("intDateOfAscend")));
//                aTT_Summit_AND.setStr_MyComment(cursor.getString(cursor
//						.getColumnIndex("strMySummitComment")));
//			}
//			QueryString1 = new StringBuilder()
//                    .append("SELECT a.[intTTGipfelNr], a.[strName], a.[dblGPS_Latitude], a.[dblGPS_Longitude]")
//                    .append(" from [TT_Summit_AND] a")
//                    .append(" where a.[intTTGipfelNr] in")
//                    .append("		 ( SELECT b.[intTTNachbarGipfelNr]")
//                    .append(" 		from [TT_NeigbourSummit_AND] b")
//                    .append(" 		where b.[intTTHauptGipfelNr] = ")
//                    .append(aTT_Summit_AND.getInt_TTGipfelNr())
//                    .append(" )").toString();
//			Log.i(TAG, "Neuer Nachbargipfel suchen:\r\n"
//					+ QueryString1);
//			cursor = newDB.rawQuery(QueryString1, null);
//			Log.i(TAG, " --> cursor.getCount() "
//					+ cursor.getCount() + "'");
//			if (cursor != null) {
//				int iCounter = 0;
//				Location mainCoordinates = new Location("reverseGeocoded");
//				Location neighborCoordinate = new Location("reverseGeocoded");
//                if ( aTT_Summit_AND == null ) {
//                    throw new NullPointerException(" aTT_Summit_AND == null ");
//                }
//                if ( aTT_Summit_AND == null ) {
//                    throw new NullPointerException(" aTT_Summit_AND == null ");
//                }
//				mainCoordinates.setLatitude(aTT_Summit_AND.getDbl_GpsLat());
//				mainCoordinates.setLongitude(aTT_Summit_AND.getDbl_GpsLong());
//				mainCoordinates.setTime(new Date().getTime());
//				Log.i(TAG, " --> mainCoordinates ");
//
//				DecimalFormat aDecimalFormat = new DecimalFormat("##");
//				if (cursor.moveToFirst()) {
//					do {
//						neighborCoordinate.setLatitude(cursor.getDouble(cursor
//								.getColumnIndex("dblGPS_Latitude")));
//						neighborCoordinate.setLongitude(cursor.getDouble(cursor
//								.getColumnIndex("dblGPS_Longitude")));
//						neighborCoordinate.setTime(new Date().getTime());
//
//						Integer intTTGipfelNachbarNr = cursor.getInt(cursor
//								.getColumnIndex("intTTGipfelNr"));
//						Log.i(TAG,
//								" --> neighborCoordinate \t("
//										+ intTTGipfelNachbarNr + ")");
//						switch (++iCounter) {
//						case 1:
//							hashmapNeighbourSummit
//									.put(intTTGipfelNachbarNr,
//											getStrDistance(cursor,
//													mainCoordinates,
//													aDecimalFormat,
//													neighborCoordinate));
//							Log.i(TAG,
//									" --> neighborCoordinate im 'put'\t"
//											+ intTTGipfelNachbarNr + ")");
//
//							break;
//						case 2:
//							hashmapNeighbourSummit
//									.put(intTTGipfelNachbarNr,
//											getStrDistance(cursor,
//													mainCoordinates,
//													aDecimalFormat,
//													neighborCoordinate));
//							Log.i(TAG,
//									" --> neighborCoordinate im 'put'\t"
//											+ intTTGipfelNachbarNr + ")");
//							break;
//						case 3:
//							hashmapNeighbourSummit
//									.put(intTTGipfelNachbarNr,
//											getStrDistance(cursor,
//													mainCoordinates,
//													aDecimalFormat,
//													neighborCoordinate));
//							Log.i(TAG,
//									" --> neighborCoordinate im 'put'\t"
//											+ intTTGipfelNachbarNr + ")");
//							break;
//						case 4:
//							hashmapNeighbourSummit
//									.put(intTTGipfelNachbarNr,
//											getStrDistance(cursor,
//													mainCoordinates,
//													aDecimalFormat,
//													neighborCoordinate));
//							Log.i(TAG,
//									" --> neighborCoordinate im 'put'\t"
//											+ intTTGipfelNachbarNr + ")");
//							break;
//						}
//						Log.i(TAG,
//								" --> wird:"
//										+ cursor.getString(cursor
//												.getColumnIndex("strName"))
//										+ " in "
//										+ (mainCoordinates
//												.distanceTo(neighborCoordinate) < 4000000 ? aDecimalFormat.format(mainCoordinates
//												.distanceTo(neighborCoordinate))
//												: "?") + "m; ");
//
//					} while (cursor.moveToNext());
//				}
//			}
//
//			QueryString1 = new StringBuilder()
//                    .append("SELECT a.intTTWegNr, a.WegName, a.strSchwierigkeitsGrad")
//                    .append(", a.intSterne, a.blnAusrufeZeichen, a.sachsenSchwierigkeitsGrad")
//                    .append(", a.ohneUnterstützungSchwierigkeitsGrad")
//                    .append(", a.rotPunktSchwierigkeitsGrad, a.intSprungSchwierigkeitsGrad")
//                    .append(", a.intAnzahlDerKommentare, a.fltMittlereWegBewertung")
//                    .append(", b.[isAscendedRoute], b.[intDateOfAscend], b.[strMyRouteComment]")
//                    .append(" FROM TT_Route_AND a")
//                    .append(" LEFT OUTER JOIN myTT_Route_AND b")
//                    .append(" ON (a.[intTTWegNr] = b.[intTTWegNr])")
//                    .append(" WHERE a.[intTTGipfelNr] = ")
//                    .append(aTT_Summit_AND.getInt_TTGipfelNr())
//                    .append(" ORDER BY a.WegName").toString();
//			Log.i(TAG,
//					"Neue Wege zum Gipfel suchen:\r\n" + QueryString1);
//
//			cursor = newDB.rawQuery(QueryString1, null);
//			Log.i(TAG,
//					"Neue Wege zum Gipfel suchen:\t c != null'"
//							+ (cursor != null));
//
//			if (cursor != null) {
//				int iCounter = 0;
//				if (cursor.moveToFirst()) {
//					do {
//						String WegName = cursor.getString(cursor
//								.getColumnIndex("WegName"));
//						int intTTWegNr = cursor.getInt(cursor
//								.getColumnIndex("intTTWegNr"));
//						String strSchwierigkeitsGrad = cursor.getString(cursor
//								.getColumnIndex("strSchwierigkeitsGrad"));
//						int intSterne = cursor.getInt(cursor
//								.getColumnIndex("intSterne"));
//						Boolean blnAusrufeZeichen = cursor.getInt(cursor
//								.getColumnIndex("blnAusrufeZeichen")) > 0;
//						Integer sachsenSchwierigkeitsGrad = cursor
//								.getInt(cursor
//										.getColumnIndex("sachsenSchwierigkeitsGrad"));
//						Integer int_OhneUnterstuetzungSchwierigkeitsGrad = cursor
//								.getInt(cursor
//										.getColumnIndex("ohneUnterstützungSchwierigkeitsGrad"));
//						Integer int_RotPunktSchwierigkeitsGrad = cursor
//								.getInt(cursor
//										.getColumnIndex("rotPunktSchwierigkeitsGrad"));
//						int intSprungSchwierigkeitsGrad = cursor.getInt(cursor
//								.getColumnIndex("intSprungSchwierigkeitsGrad"));
//						int intAnzahlDerKommentare = cursor.getInt(cursor
//								.getColumnIndex("intAnzahlDerKommentare"));
//						float fltMittlereWegBewertung = cursor.getFloat(cursor
//								.getColumnIndex("fltMittlereWegBewertung"));
//						Integer int_typeAsscended = cursor.getInt(cursor
//								.getColumnIndex("isAscendedRoute"));
//						Long long_DateAsscended = cursor.getLong(cursor
//								.getColumnIndex("intDateOfAscend"));
//
//						String str_Comment = cursor.getString(cursor
//								.getColumnIndex("strMyRouteComment"));
//
//						lstTT_Route_AND.add(new TT_Route_AND(intTTWegNr,
//                                aTT_Summit_AND.getInt_TTGipfelNr(), WegName,
//                                aTT_Summit_AND.getStr_SummitName(),
//								blnAusrufeZeichen, intSterne,
//								strSchwierigkeitsGrad,
//								sachsenSchwierigkeitsGrad,
//								int_OhneUnterstuetzungSchwierigkeitsGrad,
//								int_RotPunktSchwierigkeitsGrad,
//								intSprungSchwierigkeitsGrad,
//								intAnzahlDerKommentare,
//								fltMittlereWegBewertung, int_typeAsscended,
//								long_DateAsscended, str_Comment));
//						Log.i(TAG, ++iCounter
//								+ " -> Neuer Weg... " + WegName);
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
//	@NonNull
//	private String getStrDistance(@NotNull Cursor cursor, @NotNull Location mainLocation,
//                                  DecimalFormat aDecimalFormat, Location neighborCoordinate) {
//		return cursor.getString(cursor.getColumnIndex("strName"))
//				+ System.getProperty("line.separator")
//				+ " in "
//				+ ((mainLocation.distanceTo(neighborCoordinate) < 4000000 && mainLocation
//						.distanceTo(neighborCoordinate) > 0) ? aDecimalFormat
//						.format(mainLocation.distanceTo(neighborCoordinate))
//						: "?") + "m";
//	}
//	// ***************************
//	private void updateDateAscended(@NotNull TT_Summit_AND aTT_Summit_AND) {
//		String strDatumBestiegenString = aTT_Summit_AND.getStr_DateAsscended();
//		if (strDatumBestiegenString == null || strDatumBestiegenString.trim().equals("")) {
//			strDatumBestiegenString = getActivity().getApplicationContext().getResources()
//					.getString(R.string.strChooseDate);
//		}
//		buttonSummitAscendDay_inComment.setText(strDatumBestiegenString);
//	}
//
//	@Override
//	public void onClick( View v) {
//
//        int itemPosition = mRecyclerViewSummitFound.getChildLayoutPosition(v);
//        TT_Route_AND aTT_Route_AND = lstTT_Route_AND.get(itemPosition);
//
//		Log.e(TAG,"onClick(View v); aTT_Route_AND.getStrWegName(): "
//                + aTT_Route_AND.getStrWegName());
//
//		Fragment tt_routeFoundFragment = TT_RouteFoundFragment.newInstance(aTT_Route_AND);
//		((MainActivity)getActivity()).replaceFragment(tt_routeFoundFragment,
//				TT_RouteFoundFragment.ID);
//	}
//}
