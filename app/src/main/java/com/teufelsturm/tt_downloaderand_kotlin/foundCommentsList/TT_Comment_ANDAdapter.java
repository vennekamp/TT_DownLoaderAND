package com.teufelsturm.tt_downloaderand_kotlin.foundCommentsList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.teufelsturm.tt_downloaderand_kotlin.MainActivity;
import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.foundRouteSingle.TT_RouteFoundFragment;
import com.teufelsturm.tt_downloaderand_kotlin.foundSummitSingle.TT_SummitFoundFragment;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumTT_WegBewertung;
import com.teufelsturm.tt_downloaderand_kotlin.foundRouteSingle.TT_Route_AND;
import com.teufelsturm.tt_downloaderand_kotlin.foundSummitSingle.TT_Summit_AND;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TT_Comment_ANDAdapter
        extends RecyclerView.Adapter<TT_Comment_ANDAdapter.MyViewHolder> {
	private static final String TAG = TT_Comment_ANDAdapter.class.getSimpleName();
	private final MainActivity activity;
	private Boolean showRoute;
	private ArrayList<TT_Comment_AND> lstTT_Comment_AND;

	// Provide a reference to the views for each data item
	// Complex data items may need more than one view per item, and
	// you provide access to all the views for a data item in a view holder
	public static class MyViewHolder extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
        public TextView textView_Comment_in_Comment;
        public TextView textView_tableCol_RouteName2Comment;
        public TextView textView_tableCol_SummitName2Comment;
        public TextView textView_Comment_UserGrading;
        public TextView textView_Comment_tableColStrUser;
        public TextView textView_Comment_tableCol_DateOfComment;
		public MyViewHolder(View rowView) {
			super(rowView);

            // Hold the view objects in an object,
            // so they don't need to be re-fetched
            Log.i(TAG,"textView_Comment_in_Comment suchen...  ");
            textView_Comment_in_Comment = rowView.findViewById(R.id.textView_Comment_in_Comment);
            Log.i(TAG,"textView_Comment_in_Comment...: "
                            + textView_Comment_in_Comment.getText());
            // the TextView with the Route Name (for this Comment View)
            // Handle it's visibility and Click Listener
            textView_tableCol_RouteName2Comment = rowView
                    .findViewById(R.id.textView_tableCol_RouteName2Comment);
            textView_tableCol_SummitName2Comment = rowView
                    .findViewById(R.id.textView_tableCol_SummitName2Comment);
            textView_Comment_UserGrading = rowView
                    .findViewById(R.id.textView_Comment_UserGrading);
            textView_Comment_tableColStrUser = rowView
                    .findViewById(R.id.textView_Comment_tableColStrUser);
            textView_Comment_tableCol_DateOfComment = rowView
                    .findViewById(R.id.textView_Comment_tableCol_DateOfComment);
		}
	}

    // Provide a suitable constructor (depends on the kind of dataset)
    public TT_Comment_ANDAdapter(MainActivity activity,
                                 @NotNull ArrayList<TT_Comment_AND> objects, Boolean showRoute) {
		Log.i(TAG,"TT_Comment_ANDAdapter starten..." + objects.getClass());
		this.activity = activity;
		this.showRoute = showRoute;
		this.lstTT_Comment_AND = objects;
	}

    // Create new views (invoked by the layout manager)
    @Override
    public TT_Comment_ANDAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View aTT_Comment_ANDView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_lv_item_found, parent, false);
        MyViewHolder vh = new MyViewHolder(aTT_Comment_ANDView);
        // Cache the view objects in the tag,
        // so they can be re-accessed later
        aTT_Comment_ANDView.setTag(aTT_Comment_ANDView);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TT_Comment_AND aTT_Comment_AND = lstTT_Comment_AND.get(position);
        fillViewHolder(holder, aTT_Comment_AND);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return lstTT_Comment_AND.size();
    }


    private void fillViewHolder(@NotNull TT_Comment_ANDAdapter.MyViewHolder aTT_Comment_ANDView,
                                @NotNull TT_Comment_AND currentTT_Comment_AND)  {
        Log.i(TAG, "Suche Kommentar...: "
                + currentTT_Comment_AND.getStrEntryKommentar());
        aTT_Comment_ANDView.textView_Comment_in_Comment
                .setText(currentTT_Comment_AND.getStrEntryKommentar());
        Log.i(TAG, "Suche WegName...: ");
        aTT_Comment_ANDView.textView_tableCol_RouteName2Comment
                .setText(MessageFormat.format("{0}  {1}",
                        activity.getApplicationContext().getResources()
                                .getString(R.string.tableCol_RouteName),
                        currentTT_Comment_AND.getStrWegName()));
        TT_Summit_AND tt_summit_and = new TT_Summit_AND(
                currentTT_Comment_AND.getIntTTGipfelNr(), activity);
        aTT_Comment_ANDView.textView_tableCol_SummitName2Comment
                .setText(String.format("%s%s (%s)", activity.getApplicationContext().getResources()
                        .getString(R.string.tableCol_SummitName),
                        currentTT_Comment_AND.getStrGipfelName(),
                        tt_summit_and.getStr_Area()));
        Log.i(TAG, "Suche UserGrading...: "
                + currentTT_Comment_AND.getIntEntryBewertung());
        String strUserGrading = EnumTT_WegBewertung.values()[currentTT_Comment_AND
                .getIntEntryBewertung() + EnumTT_WegBewertung.getMinInteger()]
                .toString();

        aTT_Comment_ANDView.textView_Comment_UserGrading.setText(
                MessageFormat.format("{0}  {1}",
                        activity.getApplicationContext().getResources()
                                .getString(R.string.tableCol_UserGrade),
                        strUserGrading));

        Log.i(TAG, "Suche StrUser...: "
                + currentTT_Comment_AND.getStrEntryUser());
        aTT_Comment_ANDView.textView_Comment_tableColStrUser.setText(
                MessageFormat.format("{0}   {1}",
                        activity.getApplicationContext().getResources()
                                .getString(R.string.tableColStrUser),
                        currentTT_Comment_AND.getStrEntryUser()));
        Log.i(TAG, "Suche StrUser...: "
                + currentTT_Comment_AND.getLongEntryDatum().toString());

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MMM.yyyy HH:mm",
                Locale.GERMANY);
        Date resultdate = new Date(currentTT_Comment_AND.getLongEntryDatum());

        Log.i(TAG,
                "resultdate ...: " + resultdate
                        + " currentTt_Comment_AND.getIntEntryDatum(): "
                        + sdf.format(resultdate));
        aTT_Comment_ANDView.textView_Comment_tableCol_DateOfComment
                .setText(MessageFormat.format("{0}   {1}",
                        activity.getApplicationContext().getResources()
                                .getString(R.string.tableCol_DateOfComment),
                        sdf.format(resultdate)));
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
                            Log.i(TAG,
                                    "Intent TT_RouteFoundFragment = new Intent(...");
                            Fragment fragment
                                    = TT_RouteFoundFragment.newInstance(
                                    new TT_Route_AND(bInt, activity));
                            activity.replaceFragment(fragment, TT_RouteFoundFragment.ID );
                        }
                    });
            aTT_Comment_ANDView.textView_tableCol_SummitName2Comment
                    .setTag(currentTT_Comment_AND.getIntTTGipfelNr());
            aTT_Comment_ANDView.textView_tableCol_SummitName2Comment
                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Integer bInt = (Integer)v.getTag();
                            TT_Summit_AND tt_summit_and = new TT_Summit_AND(bInt, activity
                                    .getApplicationContext());
                            Log.i(TAG,
                                    "Intent addonPageSummitFoundActivity = new Intent(...");
                            TT_SummitFoundFragment tt_summitFoundFragment
                                    = TT_SummitFoundFragment.newInstance(tt_summit_and);
                            activity.replaceFragment(tt_summitFoundFragment, TT_SummitFoundFragment.ID );
                        }
                    });
        } // end if
    }
}
