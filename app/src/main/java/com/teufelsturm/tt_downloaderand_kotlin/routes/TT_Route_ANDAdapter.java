package com.teufelsturm.tt_downloaderand_kotlin.routes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.summit.TT_SummitFoundActivity;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumBegehungsStil;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.TT_Route_AND;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.TT_Summit_AND;

import java.text.NumberFormat;
import java.util.List;

public class TT_Route_ANDAdapter extends ArrayAdapter<TT_Route_AND> {
	private final Activity activity;
	private final boolean showSummit;
	private final List<TT_Route_AND> lstTT_Routes_AND;
	private TT_Route_ANDView aTT_Route_ANDView;

	public TT_Route_ANDAdapter(Activity activity, List<TT_Route_AND> objects,
			Boolean showSummit) {
		super(activity, R.layout.routes_lv_item_found, objects);
		// Log.i(this.getClass().getSimpleName(),
		// "TT_Route_ANDAdapter starten..." + objects.getClass());
		this.activity = activity;
		this.lstTT_Routes_AND = objects;
		this.showSummit = showSummit;
		this.setNotifyOnChange(true);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		Log.i(this.getClass().getSimpleName(), "Suche view...: " + position);

		if (rowView == null) {
			aTT_Route_ANDView = new TT_Route_ANDView();
			// Get a new instance of the row layout view
			LayoutInflater inflater = activity.getLayoutInflater();
			rowView = inflater.inflate(R.layout.routes_lv_item_found, null);
			// Hold the view objects in an object,
			// so they don't need to be re-fetched
			aTT_Route_ANDView.textView_tableCol_RouteName = (TextView) rowView
					.findViewById(R.id.textView_tableCol_RouteName);
			// the TextView with the Summit Name (for this Route View)
			// Handle it's visibility and Click Listener
			aTT_Route_ANDView.textView_tableCol_SummitName2Route = (TextView) rowView
					.findViewById(R.id.textView_tableCol_SummitName2Route);
			aTT_Route_ANDView.textView_tableCol_NumberOfComments = (TextView) rowView
					.findViewById(R.id.textView_tableCol_NumberOfComments);
			aTT_Route_ANDView.textView_tableCol_MeanGrade = (TextView) rowView
					.findViewById(R.id.textView_tableCol_MeanGrade);
			aTT_Route_ANDView.TextViewStyleAsscended = (TextView) rowView
					.findViewById(R.id.CheckBoxRouteAsscended_in_lv);
			aTT_Route_ANDView.textView_tableCol_DateAsscended = (TextView) rowView
					.findViewById(R.id.textView_tableCol_DateAsscended);
			aTT_Route_ANDView.textView_tableCol_MyComment = (TextView) rowView
					.findViewById(R.id.textView_tableCol_MyComment);
			// Cache the view objects in the tag,
			// so they can be re-accessed later
			rowView.setTag(aTT_Route_ANDView);
		} else {
			aTT_Route_ANDView = (TT_Route_ANDView) rowView.getTag();
			// Log.i(this.getClass().getSimpleName(),
			// "aTT_Gipfel_ANDView = (TT_Gipfel_ANDView) rowView.getTag();");
		}

		// Transfer the stock data from the data object
		// to the view objects
		TT_Route_AND currentTT_Route_AND = lstTT_Routes_AND.get(position);
		Log.i(this.getClass().getSimpleName(), "Suche Weg...: "
				+ currentTT_Route_AND.getStrWegName());
		aTT_Route_ANDView.intTTGipfelNr = currentTT_Route_AND.getIntGipfelNr();
		aTT_Route_ANDView.textView_tableCol_RouteName
				.setText(currentTT_Route_AND.getStrWegName() + "  ("
						+ currentTT_Route_AND.getStrSchwierigkeitsGrad() + ")");
		aTT_Route_ANDView.textView_tableCol_SummitName2Route.setText(activity
				.getApplicationContext().getResources()
				.getString(R.string.tableCol_SummitName)
				+ currentTT_Route_AND.getStrGipfelName());
		aTT_Route_ANDView.textView_tableCol_NumberOfComments.setText(activity
				.getApplicationContext().getResources()
				.getString(R.string.tableCol_NumberOfComments)
				+ "  "
				+ currentTT_Route_AND.getIntAnzahlDerKommentare().toString());
		// getting a formater for default locale
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
		aTT_Route_ANDView.textView_tableCol_MeanGrade.setText(activity
				.getApplicationContext().getResources()
				.getString(R.string.tableCol_MeanGrade)
				+ nf.format(currentTT_Route_AND.getFltMittlereWegBewertung()));
//		aTT_Route_ANDView.CheckBoxAsscended.setChecked(currentTT_Route_AND
//				.getBegehungsStil() > 1);
		 
        SpannableString ss = new SpannableString("  " + EnumBegehungsStil.values()[currentTT_Route_AND
                                                            				.getBegehungsStil()].toString()); 
        Drawable d = activity.getResources().getDrawable(R.drawable.my_checkbox);
        switch (currentTT_Route_AND.getBegehungsStil()) {
              case 1: // EnumBegehungsStil.TO_DO.ordinal():
                   d = activity.getResources().getDrawable(R.drawable.ic_todo);
                   break;
              case 2: // EnumBegehungsStil.SACK.ordinal():
                  d = activity.getResources().getDrawable(R.drawable.sack);
                  break;
              case 3: // EnumBegehungsStil.NACHSTIEG.ordinal():
                  d = activity.getResources().getDrawable(R.drawable.ic_vog);
                  break;
              case 4: // EnumBegehungsStil.SITZSCHLINGE.ordinal():
                  d = activity.getResources().getDrawable(R.drawable.ic_ruheschlinge);
                  break;
              case 5: // EnumBegehungsStil.ALLESFREI.ordinal():
                  d = activity.getResources().getDrawable(R.drawable.ic_allesfrei);
                  break;
              case 6: // EnumBegehungsStil.GETEILTEFUEHRUNG.ordinal():
                  d = activity.getResources().getDrawable(R.drawable.ic_seilschaft);
                  break;
              case 7: // EnumBegehungsStil.ROTPUNKT.ordinal():
                  d = activity.getResources().getDrawable(R.drawable.ic_rp);
                  break;
              case 8: // EnumBegehungsStil.ONSIGHT.ordinal():
                  d = activity.getResources().getDrawable(R.drawable.ic_onsight);
                  break;
              case 9: // EnumBegehungsStil.SOLO.ordinal():
                  d = activity.getResources().getDrawable(R.drawable.ic_solo);
                  break;
        }
        
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight()); 
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE); 
        ss.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE); 
		aTT_Route_ANDView.TextViewStyleAsscended.setText(ss);
		aTT_Route_ANDView.textView_tableCol_DateAsscended
				.setText(currentTT_Route_AND.getDatumBestiegen());
		aTT_Route_ANDView.textView_tableCol_MyComment.setText(activity
				.getApplicationContext().getResources()
				.getString(R.string.tableCol_MyComment)
				+ currentTT_Route_AND.getStrKommentar());
		// Set the onClick Listener
		if (!this.showSummit) {
			aTT_Route_ANDView.textView_tableCol_SummitName2Route
					.setVisibility(View.GONE);
		} else {
			aTT_Route_ANDView.textView_tableCol_SummitName2Route
				.setTag(currentTT_Route_AND.getIntGipfelNr());
			aTT_Route_ANDView.textView_tableCol_SummitName2Route
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Log.e(getClass().getSimpleName(),
									"onClick(View v); v.getTag(): "
											+ v.getTag() );
							Integer bInt = (Integer)v.getTag();
							Log.i(getClass().getSimpleName(),
									"Intent addonPageSummitFoundActivity = new Intent(...");
							Intent addonPageSummitFoundActivity = new Intent(
									activity, TT_SummitFoundActivity.class);
							Log.i(getClass().getSimpleName(),
									"addonPageSummitFoundActivity.putExtra(...");
							addonPageSummitFoundActivity.putExtra(
									"TT_Gipfel_AND",
									new TT_Summit_AND(bInt, activity
											.getApplicationContext()));
							Log.i(getClass().getSimpleName(),
									"startActivity... ");
							activity.startActivity(addonPageSummitFoundActivity); 
						}
					});
		} // end if
		return rowView;
	}

	protected static class TT_Route_ANDView {
		protected Integer intTTGipfelNr;
		protected TextView textView_tableCol_RouteName;
		protected TextView textView_tableCol_SummitName2Route;
		protected TextView textView_tableCol_NumberOfComments;
		protected TextView textView_tableCol_MeanGrade;
		protected TextView TextViewStyleAsscended;
		protected TextView textView_tableCol_DateAsscended;
		protected TextView textView_tableCol_MyComment;
	}
}
