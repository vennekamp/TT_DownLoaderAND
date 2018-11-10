package com.teufelsturm.tt_downloaderand_kotlin.tt_objects;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;

import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.routes.TT_RouteFoundFragment;
import com.teufelsturm.tt_downloaderand_kotlin.summit.TT_SummitFoundActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

// see: http://stackoverflow.com/questions/11886514/android-datapickerdialog-ondatechanged-is-not-firing 
public class DatePickerFragment extends DialogFragment implements OnDateSetListener, OnClickListener{

	private Calendar calendar;
	public class MyDatePickerDialog extends DatePickerDialog {

        
        // Regular constructor
        public MyDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
            super(context, callBack, year, monthOfYear, dayOfMonth);
            calendar = Calendar.getInstance();
        }

        // Short constructor
        public MyDatePickerDialog(Context context, OnDateSetListener callBack, Calendar aCalendar) {
            super(context, callBack, aCalendar.get(Calendar.YEAR), aCalendar.get(Calendar.MONTH), aCalendar.get(Calendar.DAY_OF_MONTH));
            calendar = aCalendar;
        }
		@Override
        public void onDateChanged(DatePicker view, int year, int month, int day) {
            super.onDateChanged(view, year, month, day);
            calendar.set(year, month, day, 12, 0);
//            setTitle(DateFormat.format(format, calendar, Locale.GERMANY));
            
        }
    }

    private MyDatePickerDialog mDatePickerDialog;
    private int intDatePickerDialogButton;
//	private Button myButtonAscended;
    public int getIntDatePickerDialogButton()  {
    	return intDatePickerDialogButton;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	calendar = Calendar.getInstance();
		if (getActivity() instanceof TT_RouteFoundFragment
				&& ((TT_RouteFoundFragment)getActivity()).getTT_Route_AND().getLong_DateAsscended() != 0) {
			calendar.setTimeInMillis(((TT_RouteFoundFragment)getActivity()).getTT_Route_AND().getLong_DateAsscended());
		}
		else if ( getActivity() instanceof TT_SummitFoundActivity
				&& ((TT_SummitFoundActivity)getActivity()).getTT_Summit_AND().getLong_DateAsscended() != 0 ){
			calendar.setTimeInMillis(((TT_SummitFoundActivity)getActivity()).getTT_Summit_AND().getLong_DateAsscended());
		}

        mDatePickerDialog = new MyDatePickerDialog(this.getActivity(), this, calendar);
        Log.v("DatePickerFragment", "onCreateDialog im DatePickerFragment -- this.getActivity().getClass().getSimpleName(): " + this.getActivity().getClass().getSimpleName());
		
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
		if (getActivity() instanceof TT_RouteFoundFragment) {
			onClick4TT_Route(which);
		}
		else if ( getActivity() instanceof TT_SummitFoundActivity){
			onClick4TT_Summit(which);
		}
		}
	
	private void onClick4TT_Route(int which) {
		TT_Route_AND aTT_Route_AND = ((TT_RouteFoundFragment)getActivity()).getTT_Route_AND();
		aTT_Route_AND.setDatumBestiegen(calendar.getTimeInMillis());
		Button buttonAscendedDay = ((TT_RouteFoundFragment)getActivity()).getButtonMyAscendDate();
		// When the DatePicker is closed the parent view is re-created,
		// thus the background data also needs to be changed instead of
		// the text...
		if (onClick4TT_XYZ(which, aTT_Route_AND.getLong_DateAsscended(), buttonAscendedDay) ) {
			((TT_RouteFoundFragment)getActivity()).setHasUnSavedData(true);
			aTT_Route_AND.setDatumBestiegen(calendar.getTimeInMillis());
		}
	}
	private void onClick4TT_Summit(int which) {
		TT_Summit_AND aTT_Summit_AND = ((TT_SummitFoundActivity)getActivity()).getTT_Summit_AND();
		aTT_Summit_AND.setDatumBestiegen(calendar.getTimeInMillis());
		Button buttonAscendedDay = ((TT_SummitFoundActivity)getActivity()).getButtonMyAscendDate();
		Log.v(getClass().getSimpleName(), "buttonAscendedDay: " + buttonAscendedDay);
		if (onClick4TT_XYZ(which, aTT_Summit_AND.getLong_DateAsscended(), buttonAscendedDay) ) {
			((TT_SummitFoundActivity)getActivity()).setHasUnSavedData(true);
			aTT_Summit_AND.setDatumBestiegen(calendar.getTimeInMillis());
		}
	}
	private boolean onClick4TT_XYZ(int which, Long aLong_DateAsscended, 
			Button buttonAscendedDay) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MMM.yyyy",
				Locale.GERMANY);
		Log.v(getClass().getSimpleName(), "onDateSet gestartet... intDatePickerDialogButton: " + intDatePickerDialogButton);
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE: // Set DATE
			buttonAscendedDay.setText(sdf.format(calendar.getTime()));
			Log.v(getClass().getSimpleName(),
					"buttonRouteAscendDay.setText(sdf.format(calendar.getTime()));");
			break;
		case DialogInterface.BUTTON_NEUTRAL: // TODAY
			calendar = Calendar.getInstance();
			buttonAscendedDay.setText(sdf.format(calendar.getTime()));
			Log.v(getClass().getSimpleName(),
					"buttonRouteAscendDay.setText(sdf.format(calendar.getTime()));");
			break;
		case DialogInterface.BUTTON_NEGATIVE: // No DATE
			calendar.setTimeInMillis(0L);
			buttonAscendedDay.setText(getResources().getString(R.string.strChooseDate));
			Log.v(getClass().getSimpleName(),
					"buttonRouteAscendDay.setText(getApplicationContext().getResources().getString(R.string.strChooseDate));");
			break;
		}
		return (aLong_DateAsscended  != (calendar.getTimeInMillis()));
	}
	
}
