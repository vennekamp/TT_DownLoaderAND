package com.teufelsturm.tt_downloader3.searches;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import com.crystal.crystalrangeseekbar.widgets.BubbleThumbRangeSeekbar;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.teufelsturm.tt_downloader3.BuildConfig;
import com.teufelsturm.tt_downloader3.DownloadFileFromURL;
import com.teufelsturm.tt_downloader3.MainActivity;
import com.teufelsturm.tt_downloader3.R;
import com.teufelsturm.tt_downloader3.dbHelper.DB_BackUp2SDCard;
import com.teufelsturm.tt_downloader3.foundSummitList.TT_SummitsFoundFragment;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FragmentSearchSummit extends FragmentSearchAbstract
		implements OnClickListener {
    private static final String TAG = FragmentSearchSummit.class.getSimpleName();
    private static final String STR_NAME = "strName";

	@Override
	protected Cursor getAutoCompleteCursor(CharSequence constraint) {
		return myAutoCompleteDbAdapter
				.getAllSummits(mViewModel, (constraint != null ? constraint.toString() : null));
	}

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//	    super.onCreate(savedInstanceState);
//    }

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
        myAutoCompleteTextView.setText(mViewModel.getStrTextSuchtext4Summit());
        myAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setStrTextSuchtext4Summit(s.toString());
            }
        });

		// Spinner 
        super.mySpinner = view.findViewById(R.id.spinnerAreaSummit);
		super.loadAreaSpinnerData(this.getActivity(), mySpinner);
        super.mySpinnerSetOnItemSelectedListener(mySpinner);
		// ***************************************************************************************
		// create RangeSeekBar for number of summits as Integer range between 0 and 250
        BubbleThumbRangeSeekbar seekBarAnzahlDerWege
                = view.findViewById(R.id.rangeSeekbarAnzahlDerWege);
		// ***************************************************************************************
        // create RangeSeekBar for number of summits as Integer range between 0 and 200
        BubbleThumbRangeSeekbar seekBarAnzahlDerSternchenWege
                = view.findViewById(R.id.rangeSeekbarAnzahlDerSternchenWege);
        seekBarAnzahlDerWegeSetOnRangeSeekBarChangeListener(seekBarAnzahlDerWege);
        seekBarAnzahlDerSternchenWegeSetOnRangeSeekBarChangeListener(seekBarAnzahlDerSternchenWege);
		return view;
	}

    private void seekBarAnzahlDerSternchenWegeSetOnRangeSeekBarChangeListener(@NotNull BubbleThumbRangeSeekbar
                                                                                      seekBarAnzahlDerSternchenWege) {
        seekBarAnzahlDerSternchenWege
                .setMinStartValue(mViewModel.getIntMinAnzahlDerSternchenWege())
                .setMaxStartValue(mViewModel.getIntMaxAnzahlDerSternchenWege()).apply();
        // set listener
        seekBarAnzahlDerSternchenWege.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            // handle changed range values
            String strUpdate = getString(R.string.strAnzahlDerSternchenWege)
                    + " (" + minValue + " bis " + maxValue + ")";
            ((TextView) view.findViewById(R.id.textViewAnzahlDerSternchenWege))
                    .setText(strUpdate);
        });

        // set final value listener
        seekBarAnzahlDerSternchenWege.setOnRangeSeekbarFinalValueListener((minValue, maxValue) -> {
            // handle changed range values
            mViewModel.setIntMinAnzahlDerSternchenWege(minValue.intValue());
            mViewModel.setIntMaxAnzahlDerSternchenWege(maxValue.intValue());
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // SEARCH Button
        Button buttonSearchSummit = view.findViewById(R.id.buttonSearchSummit);
        buttonSearchSummit.setOnClickListener(this);
        ImageButton buttonInfo = view.findViewById(R.id.imageButtonInfo);
        buttonInfo.setBackgroundResource(R.mipmap.ic_info);
        buttonInfo.setOnClickListener(this);
        ImageButton buttonLogIn = view.findViewById(R.id.imageButtonLogIn);

        buttonLogIn.setOnClickListener(this);
        File file = new File(getActivity().getApplicationContext().getFilesDir()
                + "/user_profile_photo." + getExtensionOfUser_Profile_Photo() );
        if ( file.exists() ) {
            updateUserPhoto(file);
        }
        else {
            new DownloadFileFromURL(getActivity()).execute(file.list());
        }
    }

    private void seekBarAnzahlDerWegeSetOnRangeSeekBarChangeListener(@NotNull BubbleThumbRangeSeekbar seekBarAnzahlDerWege) {
        seekBarAnzahlDerWege
                .setMinStartValue(mViewModel.getIntMinAnzahlDerWege())
                .setMaxStartValue(mViewModel.getIntMaxAnzahlDerWege()).apply();
        // set listener
        seekBarAnzahlDerWege.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            // handle changed range values
            String strUpdate = getString(R.string.strAnzahlDerWege)
                    + " (" + minValue + " bis " + maxValue + ")";
            ((TextView) view.findViewById(R.id.textViewAnzahlDerWege))
                    .setText(strUpdate);
        });

        // set final value listener
        seekBarAnzahlDerWege.setOnRangeSeekbarFinalValueListener((minValue, maxValue) -> {
            // handle changed range values
            mViewModel.setIntMinAnzahlDerWege(minValue.intValue());
            mViewModel.setIntMaxAnzahlDerWege(maxValue.intValue());
        });
    }

	@Override
	public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSearchSummit:
                Log.i(getClass().getSimpleName(), "onClick 'buttonSearchSummit' gedrückt...");


                String strTextSuchtext = mViewModel.getStrTextSuchtext4Summit();
                String strGebiet = mViewModel.getStrtextViewGebiet();
                int intMinAnzahlDerWege = mViewModel.getIntMinAnzahlDerWege();
                int intMaxAnzahlDerWege = mViewModel.getIntMaxAnzahlDerWege();
                int intMinAnzahlDerSternchenWege = mViewModel.getIntMinAnzahlDerSternchenWege();
                int intMaxAnzahlDerSternchenWege = mViewModel.getIntMaxAnzahlDerSternchenWege();

                Fragment fragment = TT_SummitsFoundFragment.newInstance(
                        strTextSuchtext, strGebiet, intMinAnzahlDerWege, intMaxAnzahlDerWege,
                        intMinAnzahlDerSternchenWege, intMaxAnzahlDerSternchenWege
                );
                ((MainActivity) getActivity()).replaceFragment(fragment, TT_SummitsFoundFragment.ID);
                break;
            case R.id.imageButtonInfo:
                showInfo();
                break;
            case R.id.imageButtonLogIn:
                login();
                break;
        }
	}

    private void login() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
	    if ( user != null) {
            Log.e(TAG, "current user: " + user);
            showLogOutDialog(user);
        }
        else {


            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.PhoneBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());
//                    new AuthUI.IdpConfig.FacebookBuilder().build(),
//                    new AuthUI.IdpConfig.TwitterBuilder().build());

            Log.e(TAG, "current user: is 'null'");


            // Create and launch sign-in intent
            getActivity().startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    MainActivity.RC_SIGN_IN);
        }
    }

    private void showLogOutDialog(@NotNull FirebaseUser user) {
        // Put up the Yes/No message box
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String stringBuilder = "Aktueller Nutzer:" +
                // Name, email address, and phone number
                "\r\n\t\tName: '" + user.getDisplayName() +
                "'\r\n\t\tEmail: '" + user.getEmail() +
                // Check if user's email is verified
                (user.isEmailVerified() ? " (verifiziert)" : " (ungeprüft)") +
                "'\r\n\t\tTel: '" +
                (user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty()
                        ? "unbekannt" : user.getPhoneNumber()) +
                // The user's ID, unique to the Firebase project. Do NOT use this value to
                // authenticate with your backend server, if you have one. Use
                // FirebaseUser.getIdToken() instead.
                "'\r\n\t\tUserID: '" + user.getUid().substring(0, 17) +
                "..." +
                "'\r\n\r\n\r\nSoll der Nutzer ausgeloggt werden?\r\n";
        builder.setIcon(R.mipmap.ic_account_user)
                .setTitle(
                        getString(R.string.app_name) + " Vers.: "
                                + BuildConfig.VERSION_NAME)
                .setMessage(stringBuilder)
                .setIcon(android.R.drawable.ic_dialog_alert)
                // POSITIVE BUTTON: LOG-OUT
                .setPositiveButton(R.string.YES_ONLY,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // Yes button clicked, do something
                                // Declaration and definition
                                FirebaseAuth firebaseAuth;
                                FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
                                    @Override
                                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                        if (firebaseAuth.getCurrentUser() == null){
                                            //Do anything here which needs to be done after signout is complete
                                            Toast.makeText(getActivity(), "Du bist erfolgreich ausgeloggt!",
                                                    Toast.LENGTH_LONG).show();
                                            updateUserPhoto(null);
                                        }
                                        else {
                                            Toast.makeText(getActivity(), "Ohh, irgendetwas ging schief beim Ausloggen...!",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                        firebaseAuth.removeAuthStateListener(this);
                                    }
                                };
                                //Init and attach
                                firebaseAuth = FirebaseAuth.getInstance();
                                firebaseAuth.addAuthStateListener(authStateListener);
                                //Call signOut()
                                firebaseAuth.signOut();
                            }
                        })
                // NEGATIVE BUTTON: Do nothing
                .setNegativeButton(android.R.string.cancel,
                        (dialog, which) -> {
                            // CONTINUE button clicked, do nothing
                            dialog.cancel();
                        })
                .show();
    }

    private void showInfo() {
        // Put up the Yes/No message box
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_launcher)
                .setTitle(
                        getString(R.string.app_name) + " Vers.: "
                                + BuildConfig.VERSION_NAME)
                .setMessage(R.string.APP_INFO_TEXT)
                .setIcon(android.R.drawable.ic_dialog_info)
                // POSITIVE BUTTON: BackUP Data
                .setPositiveButton(R.string.BACKUP_DATA,
                        (dialog, which) -> {
                            // Yes button clicked, do something
                            String strMsg = DB_BackUp2SDCard
                                    .exportDB(getActivity());
                            Toast.makeText(getActivity(), strMsg,
                                    Toast.LENGTH_LONG).show();

                        })
                // NEGATIVE BUTTON: Do nothing
                .setNegativeButton(android.R.string.cancel,
                        (dialog, which) -> {
                            // CONTINUE button clicked, do nothing
                            dialog.cancel();
                        })
                // NEUTRAL BUTTON: READ Data from Backup-Folder
                .setNeutralButton(R.string.IMPORT_DATA,
                        (dialog, which) -> {
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
                                            (dialog12, which12) -> {
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

                                            })
                                    // NEGATIVE BUTTON: Do nothing
                                    .setNegativeButton(
                                            android.R.string.cancel,
                                            (dialog1, which1) -> {
                                                // CONTINUE button
                                                // clicked, do
                                                // nothing
                                                dialog1.cancel();
                                            }).show();
                            // ****************************************
                        })
                .show();
    }

    @Override
	public void onResume() {
		myViewID = R.layout._main_activity__summit;
//		loadAreaSpinnerData(this.getActivity(), mySpinner);
		myEditTextSuchtextID = R.id.editTextSuchtextGipfel;
		super.onResume();
	}

    void updateUserPhoto(@Nullable File file) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if ( getView() != null ) {
            FloatingActionButton imageViewUserProfilePhoto = getView().findViewById(R.id.imageButtonLogIn);
            if (file != null && file.exists() && imageViewUserProfilePhoto != null && user != null) {
//                imageViewUserProfilePhoto.setVisibility(View.VISIBLE);
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                roundedBitmapDrawable.setCircular(true);
//                imageViewUserProfilePhoto.setImageBitmap(roundedBitmapDrawable.getBitmap());
                imageViewUserProfilePhoto.setImageDrawable(roundedBitmapDrawable);
            } else if (imageViewUserProfilePhoto != null) {
//                imageViewUserProfilePhoto.setVisibility(View.INVISIBLE);
                Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_account_user);
                imageViewUserProfilePhoto.setImageBitmap(bitmap);
            }
        }
    }


    private String getExtensionOfUser_Profile_Photo (){
        File folder = getActivity().getApplicationContext().getFilesDir();
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles)
        {
            if (file.isFile())
            {
                String[] filename = file.getName().split("\\.(?=[^.]+$)"); //split filename from it's extension
                if (filename[0].equalsIgnoreCase("user_profile_photo")) //matching defined filename
                    return filename[1]; // match occures.Apply any condition what you need
            }
        }
        return "";
    }
}
