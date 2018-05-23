package com.teufelsturm.tt_downloaderand_kotlin.comments;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.routes.TT_RouteFoundActivity;
import com.teufelsturm.tt_downloaderand_kotlin.summit.TT_SummitFoundActivity;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumTT_WegBewertung;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.TT_Comment_AND;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.TT_Route_AND;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.TT_Summit_AND;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TT_Comment_ANDAdapter extends ArrayAdapter<TT_Comment_AND> {
	private final Activity activity;
	private final Boolean showRoute;
	private final List<TT_Comment_AND> lstTT_Comment_AND;

	public TT_Comment_ANDAdapter(Activity activity,
			List<TT_Comment_AND> objects, Boolean showRoute) {
		super(activity, R.layout.comment_lv_item_found, objects);
		Log.i(this.getClass().getSimpleName(),
				"TT_Comment_ANDAdapter starten..." + objects.getClass());
		this.activity = activity;
		this.showRoute = showRoute;
		this.lstTT_Comment_AND = objects;
	}

	@Override
	@NonNull
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		View rowView = convertView;
		Log.i(this.getClass().getSimpleName(), "Suche view...: " + position);

		TT_Comment_ANDView aTT_Comment_ANDView;
		if (rowView == null) {
			aTT_Comment_ANDView = new TT_Comment_ANDView();
			// Get a new instance of the row layout view
			LayoutInflater inflater = activity.getLayoutInflater();
			rowView = inflater.inflate(R.layout.comment_lv_item_found, null);

			// Hold the view objects in an object,
			// so they don't need to be re-fetched
			Log.i(this.getClass().getSimpleName(),
					"textView_Comment_in_Comment suchen...  ");
			aTT_Comment_ANDView.textView_Comment_in_Comment = rowView
					.findViewById(R.id.textView_Comment_in_Comment);
			Log.i(this.getClass().getSimpleName(),
					"textView_Comment_in_Comment...: "
							+ (aTT_Comment_ANDView.textView_Comment_in_Comment)
									.getText());
			// the TextView with the Route Name (for this Comment View)
			// Handle it's visibility and Click Listener
			aTT_Comment_ANDView.textView_tableCol_RouteName2Comment = rowView
					.findViewById(R.id.textView_tableCol_RouteName2Comment);
			aTT_Comment_ANDView.textView_tableCol_SummitName2Comment = rowView
					.findViewById(R.id.textView_tableCol_SummitName2Comment);

			aTT_Comment_ANDView.textView_Comment_UserGrading = rowView
					.findViewById(R.id.textView_Comment_UserGrading);
			aTT_Comment_ANDView.textView_Comment_tableColStrUser = rowView
					.findViewById(R.id.textView_Comment_tableColStrUser);
			aTT_Comment_ANDView.textView_Comment_tableCol_DateOfComment = rowView
					.findViewById(R.id.textView_Comment_tableCol_DateOfComment);
			// Cache the view objects in the tag,
			// so they can be re-accessed later
			rowView.setTag(aTT_Comment_ANDView);
		} else {
			aTT_Comment_ANDView = (TT_Comment_ANDView) rowView.getTag();
			// Log.i(this.getClass().getSimpleName(),
			// "aTT_Gipfel_ANDView = (TT_Gipfel_ANDView) rowView.getTag();");
		}

		// Transfer the data from the data object
		// to the view objects
		TT_Comment_AND currentTT_Comment_AND = lstTT_Comment_AND.get(position);
		Log.i(this.getClass().getSimpleName(), "Suche Kommentar...: "
				+ currentTT_Comment_AND.getStrEntryKommentar());
		aTT_Comment_ANDView.textView_Comment_in_Comment
				.setText(currentTT_Comment_AND.getStrEntryKommentar());
		Log.i(this.getClass().getSimpleName(), "Suche WegName...: ");
		aTT_Comment_ANDView.textView_tableCol_RouteName2Comment
				.setText(String.format("%s  %s", activity.getApplicationContext().getResources()
                        .getString(R.string.tableCol_RouteName), currentTT_Comment_AND.getStrWegName()));
		aTT_Comment_ANDView.textView_tableCol_SummitName2Comment
				.setText(String.format("%s%s", activity.getApplicationContext().getResources()
                        .getString(R.string.tableCol_SummitName), currentTT_Comment_AND.getStrGipfelName()));
		Log.i(this.getClass().getSimpleName(), "Suche UserGrading...: "
				+ currentTT_Comment_AND.getIntEntryBewertung());
		String strUserGrading = EnumTT_WegBewertung.values()[currentTT_Comment_AND
				.getIntEntryBewertung() + EnumTT_WegBewertung.getMinInteger()]
				.toString();

		aTT_Comment_ANDView.textView_Comment_UserGrading.setText(String.format("%s  %s", activity
                .getApplicationContext().getResources()
                .getString(R.string.tableCol_UserGrade), strUserGrading));

		Log.i(this.getClass().getSimpleName(), "Suche StrUser...: "
				+ currentTT_Comment_AND.getStrEntryUser());
		aTT_Comment_ANDView.textView_Comment_tableColStrUser.setText(String.format("%s   %s", activity
                .getApplicationContext().getResources()
                .getString(R.string.tableColStrUser), currentTT_Comment_AND.getStrEntryUser()));
		Log.i(this.getClass().getSimpleName(), "Suche StrUser...: "
				+ currentTT_Comment_AND.getLongEntryDatum().toString());

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MMM.yyyy HH:mm",
				Locale.GERMANY);
		Date resultdate = new Date(currentTT_Comment_AND.getLongEntryDatum());

		Log.i(this.getClass().getSimpleName(),
				"resultdate ...: " + resultdate
						+ " currentTt_Comment_AND.getIntEntryDatum(): "
						+ sdf.format(resultdate));
		aTT_Comment_ANDView.textView_Comment_tableCol_DateOfComment
				.setText(String.format("%s   %s", activity.getApplicationContext().getResources()
                        .getString(R.string.tableCol_DateOfComment), sdf.format(resultdate)));
		if (!this.showRoute) {
			aTT_Comment_ANDView.textView_tableCol_RouteName2Comment
					.setVisibility(View.GONE);
			aTT_Comment_ANDView.textView_tableCol_SummitName2Comment
					.setVisibility(View.GONE);
		} else {
			aTT_Comment_ANDView.textView_tableCol_RouteName2Comment
					.setTag(currentTT_Comment_AND.getIntWegNr());
			aTT_Comment_ANDView.textView_tableCol_RouteName2Comment
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Toast.makeText(
									activity.getApplicationContext(),
									"Click textView_tableCol_RouteName2Comment for Route...",
									Toast.LENGTH_LONG).show();
							Integer bInt = (Integer)v.getTag();
							Log.i(getClass().getSimpleName(),
									"Intent TT_RouteFoundActivity = new Intent(...");
							Intent addonPageRouteFoundActivity = new Intent(
									activity, TT_RouteFoundActivity.class);
							Log.i(getClass().getSimpleName(),
									"addonPageSummitFoundActivity.putExtra(...");
							addonPageRouteFoundActivity.putExtra(
									"TT_Route_AND",
									new TT_Route_AND(bInt, activity
											.getApplicationContext()));
							Log.i(getClass().getSimpleName(),
									"startActivity... ");
							activity.startActivity(addonPageRouteFoundActivity); 
						}
					});
			aTT_Comment_ANDView.textView_tableCol_SummitName2Comment
					.setTag(currentTT_Comment_AND.getIntTTGipfelNr());
			aTT_Comment_ANDView.textView_tableCol_SummitName2Comment
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
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

	protected static class TT_Comment_ANDView {
		TextView textView_Comment_in_Comment;
		TextView textView_tableCol_RouteName2Comment;
		TextView textView_tableCol_SummitName2Comment;
		TextView textView_Comment_UserGrading;
		TextView textView_Comment_tableColStrUser;
		TextView textView_Comment_tableCol_DateOfComment;
	}
}
