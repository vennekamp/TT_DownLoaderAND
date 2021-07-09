package com.teufelsturm.tt_downloader3.foundRouteSingle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.teufelsturm.tt_downloader3.MainActivity;
import com.teufelsturm.tt_downloader3.R;
import com.teufelsturm.tt_downloader3.TT_DownLoadedApp;
import com.teufelsturm.tt_downloader3.firestoreHelper.UserRouteComment;
import com.teufelsturm.tt_downloader3.firestoreHelper.UserSummitComment;
import com.teufelsturm.tt_downloader3.model.TT_Comment_AND;
import com.teufelsturm.tt_downloader3.model.TT_Route_AND;
import com.teufelsturm.tt_downloader3.repositories.RepositoryFactory;
import com.teufelsturm.tt_downloaderand_kotlin.foundCommentsList.TT_Comment_ANDAdapter;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.DatePickerFragment;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumBegehungsStil;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TT_RouteFoundFragment extends Fragment {
    private static final String TAG = TT_RouteFoundFragment.class.getSimpleName();
    public static final String ID = "TT_RouteFoundFragment";
    private static final String SAVED_TEXT_KEY = "SavedText";
    private static final String TT_ROUTE_AND = "TT_Route_AND";

    private TT_Comment_ANDAdapter listenAdapter;
    private DialogFragment dateFragment;
	private Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CET"));
	private EditText editTextMyRouteComment;
	private Button buttonRouteAscendDay;
    private RecyclerView mRecyclerviewRoutesInRoutesFoundRoute;
    private ViewModel4TT_RouteFoundFragment mViewModel;

	public static TT_RouteFoundFragment newInstance(TT_Route_AND someTT_Route_AND) {
		TT_RouteFoundFragment myFragment = new TT_RouteFoundFragment();
		Bundle args = new Bundle();
		args.putInt(TT_ROUTE_AND, someTT_Route_AND.getIntTT_IDOrdinal());
		myFragment.setArguments(args);
		return myFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Neuer onCreate... ");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.route_activity_found_header, container, false);
		mViewModel = ViewModelProviders.of(this).get(ViewModel4TT_RouteFoundFragment.class);
		assert getArguments() != null;
		// Find the packet with the 'parent' object
		TT_Route_AND aTT_Route_AND = RepositoryFactory.getRouteRepository(getActivity().getApplicationContext())
               .getItem(getArguments().getInt(TT_ROUTE_AND));
		// query all routes to this summit
        if ( aTT_Route_AND == null ){
            throw new NullPointerException("tt_summit_and is null");
        }
        mViewModel.setaTT_Route_AND( aTT_Route_AND );

		Log.i(TAG,"(ListView) findViewById(R.id.list_routes);");
        mRecyclerviewRoutesInRoutesFoundRoute = view.findViewById(R.id.recyclerview_routes_in_routes_found_route);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerviewRoutesInRoutesFoundRoute.setLayoutManager(linearLayoutManager);

		return view;
	}

	@Override
	public void onViewCreated(@NotNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        // fill the Data in the Header
        fillRouteDetails(view);
        mViewModel.getaTT_Route_AND().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<TT_Route_AND>() {
            @Override
            public void onChanged(TT_Route_AND tt_route_and) {
                fillRouteDetails(getView());
            }
        });

        listenAdapter = new TT_Comment_ANDAdapter((MainActivity) getActivity(),
                mViewModel.getLstTT_Comment_AND().getValue(), false);
        mRecyclerviewRoutesInRoutesFoundRoute.setAdapter(listenAdapter);
        mViewModel.getLstTT_Comment_AND().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<ArrayList<TT_Comment_AND>>() {
            @Override
            public void onChanged(ArrayList<TT_Comment_AND> tt_route_ands) {
                listenAdapter = new TT_Comment_ANDAdapter((MainActivity) getActivity(),
                    mViewModel.getLstTT_Comment_AND().getValue(), false);
                mRecyclerviewRoutesInRoutesFoundRoute.setAdapter(listenAdapter);
            }
        });
        // recreate content in editTextMyRouteComment (if saved)
        // ok we back, load the saved text
        if (savedInstanceState != null && editTextMyRouteComment != null) {
            String savedText = savedInstanceState.getString(SAVED_TEXT_KEY);
            editTextMyRouteComment.setText(savedText);
        }
        // ********************************************************
        // Event Listener
        // The e. is part of the header, will not be found before header is
        // added!
        Spinner spinnerRouteAsscended_inComment
                = view.findViewById(R.id.spinnerRouteAsscended_inComment_route);
        editTextMyRouteComment = view.findViewById(R.id.editTextMyRouteComment_route);
        buttonRouteAscendDay = view.findViewById(R.id.buttonRouteAscendDay_route);
        // see
        editTextMyRouteCommentSetOnTouchListener();

        editTextMyRouteCommentAddTextChangedListener();
        spinnerRouteAsscended_inCommentSetOnItemSelectedListener(spinnerRouteAsscended_inComment);

        buttonRouteAscendDaySetOnClickListener();
        mViewModel.hasUnSavedData = false;
        // set Date of Ascend in the linked button.
        updateDateAscended();

        Log.i(TAG,"Neuer onCreate komplett abgearbeitet... ");
        actionListenertextView_ShareBtn();

        ((MainActivity)getActivity()).showFAB(ID);
	}

    private void buttonRouteAscendDaySetOnClickListener() {
        buttonRouteAscendDay.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG,"Schreibe Routenkommentar... ");

                // ********************************************************
                // get the current date
                if ( mViewModel.getaTT_Route_AND().getValue().getLong_DateAsscended() != 0)
                    calendar.setTimeInMillis(mViewModel.getaTT_Route_AND().getValue().getLong_DateAsscended());
                dateFragment = DatePickerFragment.newInstance(
                        TT_RouteFoundFragment.ID, mViewModel.getaTT_Route_AND().getValue(),
                        R.id.buttonRouteAscendDay_route);
                dateFragment.show(getActivity().getSupportFragmentManager(), "datePickerDialog");

                // ********************************************************
                Log.i(TAG,"datePickerDialog.show()...: ");
                // ********************************************************
            }
        });
    }

    private void spinnerRouteAsscended_inCommentSetOnItemSelectedListener(Spinner spinnerRouteAsscended_inComment) {
        spinnerRouteAsscended_inComment.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (mViewModel.getaTT_Route_AND().getValue().getBegehungsStil() != position) {
                    mViewModel.getaTT_Route_AND().getValue().setBegehungsStil(position);
                    calendar = Calendar.getInstance(TimeZone
                            .getTimeZone("CET"));
                    if (buttonRouteAscendDay.getText().equals(getResources().getString(R.string.strChooseDate))){
                        mViewModel.getaTT_Route_AND().getValue().setDatumBestiegen(calendar.getTimeInMillis());
                        updateDateAscended();
                    }
                    mViewModel.hasUnSavedData = true;
                }
                Log.v(TAG, "position: "+ position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void editTextMyRouteCommentAddTextChangedListener() {
        editTextMyRouteComment.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Log.v(TAG,"editTextMyRouteComment.addTextChanged: '" + s + "'");
                if (!String.valueOf(s).equals( mViewModel.getaTT_Route_AND().getValue().getStrKommentar() )) {
                    mViewModel.getaTT_Route_AND().getValue().setStrKommentar(s.toString());
                    mViewModel.hasUnSavedData = true;
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
    }

    @SuppressLint("ClickableViewAccessibility")
    private void editTextMyRouteCommentSetOnTouchListener() {
        // http://stackoverflow.com/questions/9770252/scrolling-editbox-inside-scrollview
        editTextMyRouteComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent motionEvent) {
                if (v.getId() == R.id.editTextMyRouteComment_route) {
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
    }

    private void actionListenertextView_ShareBtn() {
        // Define Action Listener
        Button button_ShareBtn = getView().findViewById(R.id.share_btn);
        // Log.v(TAG, "button_ShareBtn ...: " +
        // button_ShareBtn.toString());

        // Listener for the Share-Button
        button_ShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder shareText = new StringBuilder();


                shareText.append( mViewModel.getaTT_Route_AND().getValue().getStrWegName() );
                shareText.append("\n[");
                shareText.append(mViewModel.getaTT_Route_AND().getValue().getStr_TTSummitName() );
                shareText.append("]");
                shareText.append("\nMein Kommentar:\n");
                shareText.append( editTextMyRouteComment.getText().toString() );
                shareText.append("\n\n<https://play.google.com/store/apps/details?id=de.steinfibel.free>");

                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Steinfibel-Text"
                        ,shareText);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity().getApplicationContext(), "Kommentar wurde ins Clipboard kopiert.",
                        Toast.LENGTH_LONG).show();

                ShareCompat.IntentBuilder.from( getActivity() )
                        .setType("text/plain")
                        .setChooserTitle("Kommentar zum Weg teilen")
                        .setText( shareText.toString() )
                        .startChooser();
            }
        });
    }

    // ***************************
	private void updateDateAscended() {
		String strDatumBestiegenString = mViewModel.getaTT_Route_AND().getValue().getDatumBestiegen();
		if (strDatumBestiegenString.equals("")) {
			strDatumBestiegenString = getActivity().getApplicationContext().getResources()
					.getString(R.string.strChooseDate);
		}
		buttonRouteAscendDay.setText(strDatumBestiegenString);
	}

	// **************************
	private void fillRouteDetails(@NotNull View v) {
		Log.i(TAG, "aTT_Route_AND - fillRouteDetails: "
				+ mViewModel.getaTT_Route_AND().getValue().getStrWegName());
		// Route Name
		((TextView) v.findViewById(R.id.textView_tableCol_RouteName_inComment_route))
				.setText(String.format("%s  (%s)",
                        mViewModel.getaTT_Route_AND().getValue().getStrWegName(),
                        mViewModel.getaTT_Route_AND().getValue().getStrSchwierigkeitsGrad()));
		// Summit Name
		((TextView) v.findViewById(R.id.textView_tableCol_SummitName_inComment_route))
				.setText(String.format("%s, %s",
						mViewModel.strSummitName_inComment,
                        mViewModel.strAreaName_inComment));
		// style of Ascended?
		// Creating adapter for spinner
		ArrayAdapter<SpannableString> dataAdapter = new ArrayAdapter<SpannableString>(
				getActivity().getApplicationContext(), android.R.layout.simple_spinner_item,
				EnumBegehungsStil.getBegehungsStile(getContext()));
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item); /* simple_spinner_dropdown_item */
		// attaching data adapter to spinner
		((Spinner) v.findViewById(R.id.spinnerRouteAsscended_inComment_route))
				.setAdapter(dataAdapter);
		((Spinner) v.findViewById(R.id.spinnerRouteAsscended_inComment_route))
				.setSelection(mViewModel.getaTT_Route_AND().getValue().getBegehungsStil());
		// My Comment
		((TextView) v.findViewById(R.id.editTextMyRouteComment_route))
				.setText(mViewModel.getaTT_Route_AND().getValue().getStrKommentar());

	}

	@Override
	public void onStop() {
		if (mViewModel.hasUnSavedData) {
			saveMyRouteComment();
            mViewModel.hasUnSavedData = false;
		}
		super.onStop();
	}

	private void saveMyRouteComment() {
		// Set the date String.
		StringBuilder strDatumBestiegen = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd",
				Locale.GERMANY);
		if ( mViewModel.getaTT_Route_AND().getValue().getLong_DateAsscended() != 0) {
			strDatumBestiegen.append("["
					+ sdf.format(mViewModel.getaTT_Route_AND().getValue().getLong_DateAsscended())
					+ "] ");
		}
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
		if ( firebaseUser == null ) {
            TT_DownLoadedApp.showNotLoggedInToast(getActivity().getApplicationContext());
        }
        else {
            // RouteComment
            UserRouteComment.storeUserRouteComment(
                    firebaseUser,
                    mViewModel.getaTT_Route_AND().getValue().getIntTT_IDOrdinal(),
                    mViewModel.getaTT_Route_AND().getValue().getBegehungsStil(),
                    mViewModel.getaTT_Route_AND().getValue().getLong_DateAsscended(),
                    mViewModel.getaTT_Route_AND().getValue().getStrKommentar());
            // SummitComment
            UserSummitComment.storeUserSummitComment(
                    firebaseUser,
                    // Integer intTTGipfelNr
                    mViewModel.getaTT_Route_AND().getValue().getIntGipfelNr(),
                    // Boolean isAscendedSummit
                    (mViewModel.getaTT_Route_AND().getValue().getBegehungsStil() > 2),
                    // Long myLongDateOfAscend
                    mViewModel.getaTT_Route_AND().getValue().getLong_DateAsscended(),
                    // String strMySummitComment
                    strDatumBestiegen + mViewModel.getaTT_Route_AND().getValue().getStrWegName(), true);
            Toast.makeText(getActivity(),
                    "Saved Comment for this Route...\n"
                            + mViewModel.getaTT_Route_AND().getValue().getStrKommentar()
                            + "\nand Comment for it's Summit...\n"
                            + strDatumBestiegen
                            + mViewModel.getaTT_Route_AND().getValue().getStrWegName(), Toast.LENGTH_SHORT)
                    .show();
        }
	}


	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		// now, save the text if something overlaps this Activity
		savedInstanceState.putString(SAVED_TEXT_KEY, editTextMyRouteComment
				.getText().toString());
	}
}
