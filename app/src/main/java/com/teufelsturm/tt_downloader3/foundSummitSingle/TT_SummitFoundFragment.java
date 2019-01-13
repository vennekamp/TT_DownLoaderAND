package com.teufelsturm.tt_downloader3.foundSummitSingle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.teufelsturm.tt_downloader3.MainActivity;
import com.teufelsturm.tt_downloader3.R;
import com.teufelsturm.tt_downloader3.SteinFibelApplication;
import com.teufelsturm.tt_downloader3.firestoreHelper.UserSummitComment;
import com.teufelsturm.tt_downloader3.foundRouteSingle.TT_RouteFoundFragment;
import com.teufelsturm.tt_downloader3.model.TT_Route_AND;
import com.teufelsturm.tt_downloader3.foundRoutesList.TT_Route_ANDAdapter;
import com.teufelsturm.tt_downloader3.model.TT_Summit_AND;
import com.teufelsturm.tt_downloader3.repositories.RepositoryFactory;
import com.teufelsturm.tt_downloader3.tt_enums.DatePickerFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TT_SummitFoundFragment extends Fragment
        implements View.OnClickListener {

    private static final String TAG = TT_SummitFoundFragment.class.getSimpleName();
    public static final String ID = "TT_SummitFoundFragment";
    private static final String TT_GIPFEL_AND = "TT_GIPFEL_AND";

	private static final String SAVED_TEXT_KEY = "SavedText";
	private TT_Route_ANDAdapter listenAdapter;
	private DialogFragment dateFragment;
	private EditText editTextMySummitComment;
	private RecyclerView mRecyclerViewSummitFound;
	private Button buttonSummitAscendDay_inComment;
	private CheckBox checkBoxSummitAsscended;
	private ViewModel4TT_SummitFoundFragment mViewModel;
	private ListenerRegistration registration4TT_Summit;

    private MutableLiveData<TT_Summit_AND> tt_summit_andMutableLiveData = new MutableLiveData<>();

    public static TT_SummitFoundFragment newInstance(TT_Summit_AND tt_summit_and) {
		if ( tt_summit_and == null ){
		    throw new NullPointerException("tt_summit_and is null");
        }
		TT_SummitFoundFragment tt_summitFoundFragment = new TT_SummitFoundFragment();
		Bundle args = new Bundle();
		args.putInt(TT_GIPFEL_AND, tt_summit_and.getIntTT_IDOrdinal());
		tt_summitFoundFragment.setArguments(args);
		return tt_summitFoundFragment;
	}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.summit_found_lv_w_header, container, false);
        mViewModel = ViewModelProviders.of(this).get(ViewModel4TT_SummitFoundFragment.class);

        assert getArguments() != null;
        TT_Summit_AND tt_summit_and = RepositoryFactory.getSummitRepository(getActivity().getApplicationContext())
                .getItem( getArguments().getInt(TT_GIPFEL_AND)  );
        tt_summit_andMutableLiveData.setValue( tt_summit_and );
        if (tt_summit_andMutableLiveData.getValue() == null ){
            throw new NullPointerException("tt_summit_and is null");
        }
        tt_summit_andMutableLiveData.observe(this, new androidx.lifecycle.Observer<TT_Summit_AND>() {
            @Override
            public void onChanged(TT_Summit_AND tt_summit_and) {
                fillSummitDetails(tt_summit_and);
            }
        });
        mViewModel.setaTT_Summit_AND(tt_summit_andMutableLiveData.getValue() );
        if ( mViewModel.getaTT_Summit_AND().getValue() == null ) {
            Log.i(TAG, "aTT_Summit_AND == null...? ");
            throw new NullPointerException(" aTT_Summit_AND == null ");
        }
        Log.i(TAG,"(ListView) findViewById(R.id.list_routes);");
        mRecyclerViewSummitFound = view.findViewById(R.id.list_summit_found_lv_w_header);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerViewSummitFound.setLayoutManager(linearLayoutManager);

        // the editTextMySummitComment is part of the header - can not be found
        // before...
        editTextMySummitComment = view.findViewById(R.id.editTextMySummitComment);
        editTextMySummitComment.setText(mViewModel.getaTT_Summit_AND().getValue().getStr_MyComment());
        // http://stackoverflow.com/questions/9770252/scrolling-editbox-inside-scrollview
        editTextMySummitCommentSetOnTouchListener();
        editTextMySummitCommentAddTextChangedListener();
        checkBoxSummitAsscended = view.findViewById(R.id.CheckBoxSummitAsscended);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // fill the Data in the Header
        fillSummitDetails( mViewModel.getaTT_Summit_AND().getValue() );
        mViewModel.getaTT_Summit_AND().observe(this, new androidx.lifecycle.Observer<TT_Summit_AND>() {
            @Override
            public void onChanged(TT_Summit_AND tt_summit_and) {
                fillSummitDetails( mViewModel.getaTT_Summit_AND().getValue() );

                DocumentReference mDocRef = mViewModel.getaTT_Summit_AND().getValue().getDocRef();
                if ( mDocRef != null ) {
                    if ( TT_SummitFoundFragment.this.registration4TT_Summit != null ) {
                            TT_SummitFoundFragment.this.registration4TT_Summit.remove();
                    }
                    TT_SummitFoundFragment.this.registration4TT_Summit = mDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@org.jetbrains.annotations.Nullable DocumentSnapshot snapshot,
                                            @org.jetbrains.annotations.Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e);
                                return;
                            }
                            if (snapshot != null && snapshot.exists()) {
                                Log.d(TAG, "Current data: " + snapshot.getData());
                                if (mViewModel.getaTT_Summit_AND().getValue().getBln_Asscended()
                                        != snapshot.getBoolean("IsAscendedSummit")
                                        || !mViewModel.getaTT_Summit_AND().getValue().getLong_DateAsscended().equals(
                                                snapshot.getLong("DateAsscended"))
                                        || mViewModel.getaTT_Summit_AND().getValue().getStr_MyComment().equals(
                                                snapshot.getString("UserSummitComment"))
                                        ) {
                                    mViewModel.getaTT_Summit_AND().getValue().setBln_Asscended(snapshot.getBoolean("IsAscendedSummit"));
                                    mViewModel.getaTT_Summit_AND().getValue().setDatumBestiegen(snapshot.getLong("DateAsscended"));
                                    mViewModel.getaTT_Summit_AND().getValue().setStr_MyComment(snapshot.getString("UserSummitComment"));
                                }
                            } else {
//                        Log.d(TAG, "Current data: null");
                            }
                        }
                    });
                }
            }
        });



        // recreate content in editTextMySummitComment (if saved)
        // ok we back, load the saved text
        mViewModel.getLstTT_Route_AND().observe(this, new androidx.lifecycle.Observer<ArrayList<TT_Route_AND>>() {
            @Override
            public void onChanged(ArrayList<TT_Route_AND> tt_route_ands) {
                listenAdapter = new TT_Route_ANDAdapter( TT_SummitFoundFragment.this,
                        mViewModel.getLstTT_Route_AND().getValue(), false);
                mRecyclerViewSummitFound.setAdapter(listenAdapter);
            }
        });
        if (savedInstanceState != null) {
            String savedText = savedInstanceState.getString(SAVED_TEXT_KEY);
            editTextMySummitComment.setText(savedText);
        }

        checkBoxSummitAsscended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.hasUnSavedData = true;
            }
        });


        buttonSummitAscendDay_inComment = view.findViewById(R.id.buttonSummitAscendDay_inComment);
        Log.v(TAG, "buttonSummitAscendDay_inComment: " + buttonSummitAscendDay_inComment);
        updateDateAscended();
        // Define Action Listener
        buttonSummitAscendDay_inComment.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // ********************************************************
                dateFragment = DatePickerFragment.newInstance(ID,
                        mViewModel.getaTT_Summit_AND().getValue(),
                        R.id.buttonSummitAscendDay_inComment);
                dateFragment.show(getActivity().getSupportFragmentManager(),"datePickerDialog");
                // ********************************************************
                Log.i(TAG,"datePickerDialog.show()...: ");
                // ********************************************************
            }
        });
        mViewModel.hasUnSavedData = false;
        actionListenertextView_GpsCoords();

        ((MainActivity)getActivity()).showFAB(ID);
        Log.i(TAG,"Neuer onCreate komplett abgearbeitet... ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if ( TT_SummitFoundFragment.this.registration4TT_Summit != null ) {
            TT_SummitFoundFragment.this.registration4TT_Summit.remove();
        }
    }


	@Override
	public void onResume() {
		super.onResume();
		Log.i(TAG, "onResume ");
		if (mViewModel.hasUnSavedData) {
			Log.i(TAG, "onResume; dataHasChanged --> " + mViewModel.hasUnSavedData);
			assert getArguments() != null;

			mViewModel.getLstTT_Route_AND().observe(this, new androidx.lifecycle.Observer<ArrayList<TT_Route_AND>>() {
                @Override
                public void onChanged(ArrayList<TT_Route_AND> tt_route_ands) {
                    listenAdapter.notifyDataSetChanged();
                }
            });
		}
	}

	public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
	    super.onSaveInstanceState(savedInstanceState);
		// now, save the text if something overlaps this Activity
		savedInstanceState.putString(SAVED_TEXT_KEY, editTextMySummitComment
				.getText().toString());
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		((MainActivity)context).showFAB(ID);
	}


    @SuppressLint("ClickableViewAccessibility")
    private void editTextMySummitCommentSetOnTouchListener() {
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
    }

    private void editTextMySummitCommentAddTextChangedListener() {
        editTextMySummitComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                Log.v(TAG,
                        "editTextMyRouteComment.addTextChanged: " + s);
                if (!mViewModel.getaTT_Summit_AND().getValue().getStr_MyComment().contentEquals(s)) {
                    mViewModel.getaTT_Summit_AND().getValue().setStr_MyComment(s.toString());
                    mViewModel.hasUnSavedData = true;
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
    }

	@Override
	public void onStop() {
		if (mViewModel.hasUnSavedData) {
			saveMySummitComment();
            mViewModel.hasUnSavedData = false;
		}
		super.onStop();
	}
	
	private void saveMySummitComment() {
//        TT_Summit_AND aTT_Summit_AND = getArguments().getParcelable(TT_GIPFEL_AND);

		try {
            mViewModel.getaTT_Summit_AND().getValue()
					.setBln_Asscended(checkBoxSummitAsscended.isChecked());
            mViewModel.getaTT_Summit_AND().getValue().setStr_MyComment(editTextMySummitComment
					.getText().toString());

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if ( firebaseUser == null ) {
                SteinFibelApplication.showNotLoggedInToast(getActivity().getApplicationContext());
            }
            else {
                UserSummitComment.storeUserSummitComment(
                        firebaseUser,
                        mViewModel.getaTT_Summit_AND().getValue().getIntTT_IDOrdinal(),
                        mViewModel.getaTT_Summit_AND().getValue().getBln_Asscended(),
                        mViewModel.getaTT_Summit_AND().getValue().getLong_DateAsscended(),
                        mViewModel.getaTT_Summit_AND().getValue().getStr_MyComment(), false);

                Toast.makeText(getActivity().getApplicationContext(),
                        "Saved Comment for this Summit...\n"
                                + mViewModel.getaTT_Summit_AND().getValue().getStr_DateAsscended() + ":\n"
                                + mViewModel.getaTT_Summit_AND().getValue().getStr_TTSummitName(), Toast.LENGTH_SHORT)
                        .show();
            }
		} catch (Exception ex) {
            Crashlytics.logException(ex);
			Log.i(TAG, ex.toString());
		}
	}

	private void actionListenerButton_NeighbourSummit(Button button_NeighbourSummit) {
		// Define Action Listener
		Log.i(TAG, "buttonSearchSummit ...: "
				+ button_NeighbourSummit.getText());
		button_NeighbourSummit.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Button b = (Button) v;
				Integer bInt = (Integer) b.getTag();
				String buttonText = b.getText().toString();
				Log.i(TAG,"Click button_NeighbourSummit for Summit..." + buttonText + "\r\n" + bInt);
                Log.i(TAG,"Start TT_RouteFoundFragment... ");
                FragmentManager fm = getActivity().getSupportFragmentManager();
                TT_Summit_AND tt_summit_andNext = new TT_Summit_AND(v.getContext().getApplicationContext(), bInt);
                TT_SummitFoundFragment tt_summitFoundFragment =
                        TT_SummitFoundFragment.newInstance(tt_summit_andNext);
                FragmentTransaction ft = fm.beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack if needed
                ft.replace(R.id.fragment_container, tt_summitFoundFragment,
                        TT_SummitFoundFragment.ID);
                ft.addToBackStack(null);
                // Commit the transaction
                ft.commit();
			}
		});
	}

	private void actionListenertextView_GpsCoords() {
		// Define Action Listener
		Button textView_GpsCoords = (Button) getView().findViewById(R.id.textView_GpsCoords);
		// Log.i(TAG, "buttonSearchSummit ...: " +
		// buttonSaveSummit.toString());
		textView_GpsCoords.setOnClickListener(new TextView.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getActivity(),
						"Click on textView_GpsCoords...", Toast.LENGTH_LONG)
						.show();
			}
		});
	}

	private void fillSummitDetails(@NotNull TT_Summit_AND aTT_Summit_AND) {
		Log.i(TAG,"aTT_Gipfel_AND - fillSummitDetails: 1"
						+ String.valueOf(aTT_Summit_AND.getIntTT_IDOrdinal())
						+ " --> " + aTT_Summit_AND.getStr_TTSummitName());
		// Summit Name
		((TextView) getView().findViewById(R.id.textView_SummitName))
				.setText(String.format(Locale.GERMANY, "%s   (KleFü #%d)",
                        aTT_Summit_AND.getStr_TTSummitName(),
                        aTT_Summit_AND.getInt_SummitNumberOfficial()));
		// Area Name
		((TextView) getView().findViewById(R.id.textView_Area)).setText(String.format("%s%s", getResources()
				.getString(R.string.lblGebiet), aTT_Summit_AND.getStr_Area()));
		Log.i(TAG,"aTT_Gipfel_AND - fillSummitDetails: 2");
		// *******************************************************************************
		// Create the text for the Neighbor Summit and their actionListener
		int[] arrNeighbourSummits = new int[] { R.id.button_NeighbourSummit01,
				R.id.button_NeighbourSummit02, R.id.button_NeighbourSummit03,
				R.id.button_NeighbourSummit04 };
		// Neighbor Summit #01..04
		int iCount = 0;
		for (Map.Entry<Integer, String> strH : mViewModel.hashmapNeighbourSummit.entrySet()) {
		    // set the TAG
            ((Button) getView().findViewById(arrNeighbourSummits[iCount])).setTag(strH.getKey());
            // set the TEXT
			((Button) getView().findViewById(arrNeighbourSummits[iCount])).setText(strH.getValue());
			Log.i(TAG,"aTT_Gipfel_AND - fillSummitDetails: Neighbor Summit #0" + (iCount + 1));
			actionListenerButton_NeighbourSummit(
                    (Button) getView().findViewById(arrNeighbourSummits[iCount]));
			iCount++;
		}
		// is Ascended?
		checkBoxSummitAsscended.setChecked(aTT_Summit_AND.getBln_Asscended());
		Log.i(TAG,"aTT_Gipfel_AND - fillSummitDetails: is Ascended? " + aTT_Summit_AND.getBln_Asscended());
		// My Comment
		editTextMySummitComment.setText(aTT_Summit_AND.getStr_MyComment());
        Log.i(TAG,"aTT_Gipfel_AND - fillSummitDetails erledigt: " + aTT_Summit_AND.getIntTT_IDOrdinal()
						+ " --> " + aTT_Summit_AND.getStr_TTSummitName());
	}

	// ***************************
	private void updateDateAscended() {
		String strDatumBestiegenString = mViewModel.getaTT_Summit_AND().getValue().getStr_DateAsscended();
		if (strDatumBestiegenString == null || strDatumBestiegenString.trim().equals("")) {
			strDatumBestiegenString = getActivity().getApplicationContext().getResources()
					.getString(R.string.strChooseDate);
		}
		buttonSummitAscendDay_inComment.setText(strDatumBestiegenString);
	}

	@Override
	public void onClick( View v) {

        int itemPosition = mRecyclerViewSummitFound.getChildLayoutPosition(v);
        TT_Route_AND aTT_Route_AND = mViewModel.getLstTT_Route_AND().getValue().get(itemPosition);

		Log.e(TAG,"onClick(View v); aTT_Route_AND.getStrWegName(): " + aTT_Route_AND.getStrWegName());

		Fragment tt_routeFoundFragment = TT_RouteFoundFragment.newInstance(aTT_Route_AND);
		((MainActivity)getActivity()).replaceFragment(tt_routeFoundFragment,
				TT_RouteFoundFragment.ID);
	}
}
