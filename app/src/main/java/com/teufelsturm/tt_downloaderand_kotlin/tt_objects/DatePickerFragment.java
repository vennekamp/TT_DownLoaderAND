package com.teufelsturm.tt_downloaderand_kotlin.tt_objects;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;

import com.teufelsturm.tt_downloader3.R;
import com.teufelsturm.tt_downloader3.foundRouteSingle.TT_RouteFoundFragment;
import com.teufelsturm.tt_downloader3.foundSummitSingle.TT_SummitFoundFragment;
import com.teufelsturm.tt_downloader3.model.TT_Route_AND;
import com.teufelsturm.tt_downloader3.model.TT_Summit_AND;
import com.teufelsturm.tt_downloader3.repositories.RepositoryFactory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

// see: http://stackoverflow.com/questions/11886514/android-datapickerdialog-ondatechanged-is-not-firing 
public class DatePickerFragment extends DialogFragment
        implements OnDateSetListener, OnClickListener {
    private static final String TAG = DatePickerFragment.class.getSimpleName();

    private static final String PARENTS_ID = "PARENTS_ID";
    private static final String TT_ROUTE_AND = "TT_ROUTE_AND";
    private static final String TT_SUMMIT_AND = "TT_SUMMIT_AND";
    private static final String BUTTON_ASCENDED_DAY_ID = "BUTTON_ASCENDED_DAY_ID";

    private Calendar calendar;
    @Nullable
    private TT_Route_AND aTT_Route_AND;
    @Nullable
    private TT_Summit_AND aTT_Summit_AND;
    private Button buttonAscendedDay;

    public static DialogFragment newInstance(String parents_ID,
                                             TT_Route_AND aTT_Route_AND,
                                             int buttonAscendedDayID ) {
        DatePickerFragment myFragment = new DatePickerFragment();

        Bundle args = new Bundle();
        args.putString(PARENTS_ID, parents_ID);
        args.putInt(TT_ROUTE_AND, aTT_Route_AND.getIntTT_IDOrdinal());
        args.putInt(BUTTON_ASCENDED_DAY_ID, buttonAscendedDayID);
        myFragment.setArguments(args);

        return myFragment;
    }
    public static DialogFragment newInstance(String parents_ID,
                                             TT_Summit_AND aTT_Summit_AND,
                                             int buttonAscendedDayID ) {
        DatePickerFragment myFragment = new DatePickerFragment();

        Bundle args = new Bundle();
        args.putString(PARENTS_ID, parents_ID);
        args.putInt(TT_SUMMIT_AND, aTT_Summit_AND.getIntTT_IDOrdinal());
        args.putInt(BUTTON_ASCENDED_DAY_ID, buttonAscendedDayID);
        myFragment.setArguments(args);

        return myFragment;
    }

    public class MyDatePickerDialog extends DatePickerDialog {


        // Regular constructor
        public MyDatePickerDialog(Context context,
                                  OnDateSetListener callBack,
                                  int year,
                                  int monthOfYear,
                                  int dayOfMonth) {
            super(context, callBack, year, monthOfYear, dayOfMonth);
            calendar = Calendar.getInstance();
        }

        // Short constructor
        public MyDatePickerDialog(Context context,
                                  OnDateSetListener callBack,
                                  @NotNull Calendar aCalendar) {
            super(context, callBack, aCalendar.get(Calendar.YEAR), aCalendar.get(Calendar.MONTH), aCalendar.get(Calendar.DAY_OF_MONTH));
            calendar = aCalendar;
        }

        @Override
        public void onDateChanged(@NonNull DatePicker view, int year, int month, int day) {
            super.onDateChanged(view, year, month, day);
            calendar.set(year, month, day, 12, 0);
//            setTitle(DateFormat.format(format, calendar, Locale.GERMANY));

        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        calendar = Calendar.getInstance();

        FragmentManager fm = getActivity().getSupportFragmentManager();

        if (  getArguments() != null && getArguments().containsKey(TT_ROUTE_AND) ) {
            aTT_Route_AND = RepositoryFactory.getRouteRepository(getActivity().getApplicationContext())
                    .getItem( getArguments().getInt(TT_ROUTE_AND) );
            if ( aTT_Route_AND.getLong_DateAsscended() != 0 ) {
                calendar.setTimeInMillis(aTT_Route_AND.getLong_DateAsscended());
            }
        } else if (getArguments() != null && getArguments().containsKey(TT_SUMMIT_AND) ) {
            aTT_Summit_AND = RepositoryFactory.getSummitRepository(getActivity().getApplicationContext())
            .getItem( getArguments().getInt(TT_SUMMIT_AND) );
            if ( aTT_Summit_AND.getLong_DateAsscended() != 0 ) {
                calendar.setTimeInMillis(aTT_Summit_AND.getLong_DateAsscended());
            }
        }

        MyDatePickerDialog mDatePickerDialog = new MyDatePickerDialog(this.getActivity(), this, calendar);
        Log.v(TAG, "onCreateDialog im DatePickerFragment -- this.getActivity().TAG: "
                + this.getActivity().getClass().getSimpleName() );

        mDatePickerDialog.setButton(Dialog.BUTTON_POSITIVE
                , getResources().getText(android.R.string.ok)
                , this);
        mDatePickerDialog.setButton(Dialog.BUTTON_NEUTRAL
                , getResources().getText(R.string.strToDay)
                , this);
        mDatePickerDialog.setButton(Dialog.BUTTON_NEGATIVE
                , getResources().getText(R.string.strNoDay)
                , this);
        return mDatePickerDialog;
    }

    //    public void setButtonAscended( Button aButtonAscended ){
//    	myButtonAscended = aButtonAscended;
//    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Log.v("DatePickerFragment", "onDateSet im DatePickerFragment");
        // This is not fired in combination with onClickListener in ANDROID 2.x...!!!!!!!
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub
        Log.v("DatePickerFragment", "onClick im DatePickerFragment");

        if (  getArguments() != null && getArguments().containsKey(TT_ROUTE_AND)) {
            onClick4TT_Route(which);
        } else if (  getArguments() != null && getArguments().containsKey(TT_SUMMIT_AND) ) {
                onClick4TT_Summit(which);
            }
        }

    private void onClick4TT_Route(int which) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        TT_RouteFoundFragment tt_routeFoundFragment = (TT_RouteFoundFragment) fm.findFragmentByTag(TT_RouteFoundFragment.ID);
        aTT_Route_AND.setDatumBestiegen(calendar.getTimeInMillis());

        int buttonAscendedDayID = getArguments().getInt(BUTTON_ASCENDED_DAY_ID, -1 );

        if ( buttonAscendedDayID > 0) {
            buttonAscendedDay = tt_routeFoundFragment.getView().findViewById(buttonAscendedDayID);
        }
        // When the DatePicker is closed the parent view is re-created,
        // thus the background data also needs to be changed instead of
        // the text...
        if (onClick4TT_XYZ(which, aTT_Route_AND.getLong_DateAsscended(), buttonAscendedDay)) {
//            tt_routeFoundFragment.setHasUnSavedData(true);
            aTT_Route_AND.setDatumBestiegen(calendar.getTimeInMillis());
        }
    }

    private void onClick4TT_Summit(int which) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        TT_SummitFoundFragment tt_summitFoundFragment =
                (TT_SummitFoundFragment) fm.findFragmentByTag(TT_SummitFoundFragment.ID);
        aTT_Summit_AND.setDatumBestiegen(calendar.getTimeInMillis());

        int buttonAscendedDayID = getArguments().getInt(BUTTON_ASCENDED_DAY_ID, -1 );


        if ( buttonAscendedDayID > 0) {
            buttonAscendedDay = tt_summitFoundFragment.getView().findViewById(buttonAscendedDayID);
        }
        // When the DatePicker is closed the parent view is re-created,
        // thus the background data also needs to be changed instead of
        // the text...
        if (onClick4TT_XYZ(which, aTT_Summit_AND.getLong_DateAsscended(), buttonAscendedDay)) {
//            tt_routeFoundFragment.setHasUnSavedData(true);
            aTT_Summit_AND.setDatumBestiegen(calendar.getTimeInMillis());
        }

    }

    private boolean onClick4TT_XYZ(int which, Long aLong_DateAsscended,
                                   Button buttonAscendedDay) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MMM.yyyy",
                Locale.GERMANY);
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE: // Set DATE
                buttonAscendedDay.setText(sdf.format(calendar.getTime()));
                Log.v(TAG,"buttonRouteAscendDay.setText(sdf.format(calendar.getTime()));");
                break;
            case DialogInterface.BUTTON_NEUTRAL: // TODAY
                calendar = Calendar.getInstance();
                buttonAscendedDay.setText(sdf.format(calendar.getTime()));
                Log.v(TAG,"buttonRouteAscendDay.setText(sdf.format(calendar.getTime()));");
                break;
            case DialogInterface.BUTTON_NEGATIVE: // No DATE
                calendar.setTimeInMillis(0L);
                buttonAscendedDay.setText(getResources().getString(R.string.strChooseDate));
                Log.v(TAG,"buttonRouteAscendDay.setText(getApplicationContext().getResources().getSQL4CommentsSearch(R.string.strChooseDate));");
                break;
        }
        return (aLong_DateAsscended != (calendar.getTimeInMillis()));
    }

}
