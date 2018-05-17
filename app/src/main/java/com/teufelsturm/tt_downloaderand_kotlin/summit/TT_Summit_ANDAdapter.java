package com.teufelsturm.tt_downloaderand_kotlin.summit;

import android.app.Activity;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.TT_Summit_AND;

import java.util.List;


public class TT_Summit_ANDAdapter extends ArrayAdapter<TT_Summit_AND> {
	private final Activity activity;
	private List<TT_Summit_AND> lstTT_Gipfel_AND;
	private TT_Gipfel_ANDView aTT_Gipfel_ANDView;

	public TT_Summit_ANDAdapter(Activity activity, List<TT_Summit_AND> objects) {
		super(activity, R.layout.summit_lv_item_found, objects);
		this.activity = activity;
		this.lstTT_Gipfel_AND = objects;
		this.setNotifyOnChange(true);
	}
		
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		Log.i(getClass().getSimpleName(), "Suche view...: " + position );
		// TODO: Improve View with ImageView
		if (rowView == null) 
		{
			aTT_Gipfel_ANDView = new TT_Gipfel_ANDView();
			// Get a new instance of the row layout view
			LayoutInflater inflater = activity.getLayoutInflater();
			Log.i(getClass().getSimpleName(), "Suche rowView...: " + position );
			rowView = inflater.inflate(R.layout.summit_lv_item_found, null);

			Log.i(getClass().getSimpleName(), "Suche rowView...BEENDET: " + position );
			// Hold the view objects in an object,
			// so they don't need to be re-fetched

			Log.i(getClass().getSimpleName(), "Suche textView_tableCol_SummitName...: " + position );
			aTT_Gipfel_ANDView.textView_tableCol_SummitName = (TextView) rowView
					.findViewById(R.id.textView_tableCol_SummitName);
			Log.i(getClass().getSimpleName(), "Suche textView_tableColStrUser...: " + position );
			aTT_Gipfel_ANDView.textView_tableColSummitNumberOfficial = (TextView) rowView
					.findViewById(R.id.textView_tableColStrUser);
			Log.i(getClass().getSimpleName(), "Suche textView_tableCol_Area...: " + position );
			aTT_Gipfel_ANDView.textView_tableCol_Area = (TextView) rowView
					.findViewById(R.id.textView_tableCol_Area);
			Log.i(getClass().getSimpleName(), "Suche textView_tableCol_DateOfComment...: " + position );
			aTT_Gipfel_ANDView.textView_tableCol_NumberOfRoutes = (TextView) rowView
					.findViewById(R.id.textView_tableCol_DateOfComment);
			Log.i(getClass().getSimpleName(), "Suche textView_tableCol_NumberofStarRoutes...: " + position );
			aTT_Gipfel_ANDView.textView_tableCol_NumberofStarRoutes = (TextView) rowView
					.findViewById(R.id.textView_tableCol_NumberofStarRoutes);
			Log.i(getClass().getSimpleName(), "Suche textView_tableCol_NumberOfComments...: " + position );
			aTT_Gipfel_ANDView.textView_tableCol_EasiestGrade = (TextView) rowView
					.findViewById(R.id.textView_tableCol_NumberOfComments);
			Log.i(getClass().getSimpleName(), "Suche textView_tableCol_MeanGrade...: " + position );
			aTT_Gipfel_ANDView.textView_tableCol_Gps = (TextView) rowView
					.findViewById(R.id.textView_tableCol_MeanGrade);
			Log.i(getClass().getSimpleName(), "Suche CheckBoxAsscended_in_lv...: " + position );
			aTT_Gipfel_ANDView.CheckBoxAsscended = (CheckBox) rowView
					.findViewById(R.id.CheckBoxAsscended_in_lv);
			Log.i(getClass().getSimpleName(), "Suche textView_tableCol_DateAsscended...: " + position );
			aTT_Gipfel_ANDView.textView_tableCol_DateAsscended = (TextView) rowView
					.findViewById(R.id.textView_tableCol_DateAsscended);
			Log.i(getClass().getSimpleName(), "Suche textView_tableCol_MyComment...: " + position );
			aTT_Gipfel_ANDView.textView_tableCol_MyComment = (TextView) rowView
					.findViewById(R.id.textView_tableCol_MyComment);
			// Cache the view objects in the tag,
			// so they can be re-accessed later
			 rowView.setTag(aTT_Gipfel_ANDView);
			Log.i(getClass().getSimpleName(), "Suche rowView... BEENDET: " + position );
		} else {
			aTT_Gipfel_ANDView = (TT_Gipfel_ANDView) rowView.getTag();
//			Log.i(TAG, "aTT_Gipfel_ANDView = (TT_Gipfel_ANDView) rowView.getTag();");
		}
		
		// Transfer the stock data from the data object
		// to the view objects
		TT_Summit_AND currentTT_Gipfel_AND = lstTT_Gipfel_AND.get(position);
		
		aTT_Gipfel_ANDView.textView_tableCol_SummitName.setText(
				currentTT_Gipfel_AND.getStr_SummitName() );
		aTT_Gipfel_ANDView.textView_tableColSummitNumberOfficial.setText(
				activity.getApplicationContext().getResources()
						.getString(R.string.tableColSummitNumberOfficial) +
						currentTT_Gipfel_AND.getInt_SummitNumberOfficial()
								.toString());
		aTT_Gipfel_ANDView.textView_tableCol_Area.setText(
				activity.getApplicationContext().getResources()
				.getString(R.string.tableCol_Area)
				+ currentTT_Gipfel_AND.getStr_Area());
		aTT_Gipfel_ANDView.textView_tableCol_NumberOfRoutes.setText(
				activity.getApplicationContext().getResources()
				.getString(R.string.tableCol_NumberOfRoutes)
				+ currentTT_Gipfel_AND.getInt_NumberOfRoutes().toString() );
		aTT_Gipfel_ANDView.textView_tableCol_NumberofStarRoutes.setText(
				activity.getApplicationContext().getResources()
				.getString(R.string.tableCol_NumberofStarRoutes)
				+ currentTT_Gipfel_AND.getInt_NumberofStarRoutes().toString() ); 
		aTT_Gipfel_ANDView.textView_tableCol_EasiestGrade.setText(
				activity.getApplicationContext().getResources()
				.getString(R.string.tableCol_EasiestGrade )
				+ currentTT_Gipfel_AND.getStr_EasiestGrade ());
		Location aTT_Gipfel_Location = new Location("reverseGeocoded");
		aTT_Gipfel_Location.setLongitude(currentTT_Gipfel_AND.getDbl_GpsLong() );
		aTT_Gipfel_Location.setLatitude(currentTT_Gipfel_AND.getDbl_GpsLat() );
//		aTT_Gipfel_ANDView.textView_tableCol_Gps.setText(
//				activity.getApplicationContext().getResources()
//				.getString(R.string.tableCol_Gps)
//				+ Location.convert(aTT_Gipfel_Location.getLongitude()
//						, Location.FORMAT_SECONDS ) + " N; "
//				+ Location.convert(aTT_Gipfel_Location.getLatitude()
//						, Location.FORMAT_SECONDS ) + " O");
		aTT_Gipfel_ANDView.CheckBoxAsscended.setChecked( 
				currentTT_Gipfel_AND.getBln_Asscended() );
		aTT_Gipfel_ANDView.textView_tableCol_DateAsscended.setText(
				currentTT_Gipfel_AND.getStr_DateAsscended() ); 
		aTT_Gipfel_ANDView.textView_tableCol_MyComment.setText(activity
				.getApplicationContext().getResources()
				.getString(R.string.tableCol_MyComment)
				+ currentTT_Gipfel_AND.getStr_MyComment());
		return rowView;
	}

	protected static class TT_Gipfel_ANDView {
		protected TextView textView_tableCol_SummitName;
		protected TextView textView_tableColSummitNumberOfficial;
		protected TextView textView_tableCol_Area;
		protected TextView textView_tableCol_NumberOfRoutes;
		protected TextView textView_tableCol_NumberofStarRoutes;
		protected TextView textView_tableCol_EasiestGrade;
		protected TextView textView_tableCol_Gps;
		protected TextView textView_tableCol_GpsLat;
		protected CheckBox CheckBoxAsscended;
		protected TextView textView_tableCol_DateAsscended;
		protected TextView textView_tableCol_MyComment;
	}
}
