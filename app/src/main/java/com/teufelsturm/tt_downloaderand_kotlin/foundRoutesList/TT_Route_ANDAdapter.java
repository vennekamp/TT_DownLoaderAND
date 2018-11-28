package com.teufelsturm.tt_downloaderand_kotlin.foundRoutesList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumBegehungsStil;
import com.teufelsturm.tt_downloaderand_kotlin.foundRouteSingle.TT_Route_AND;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class TT_Route_ANDAdapter
        extends RecyclerView.Adapter<TT_Route_ANDAdapter.MyViewHolder> {
    private static final String TAG = TT_Route_ANDAdapter.class.getSimpleName();

    private ArrayList<TT_Route_AND> lstTT_Routes_AND;
    private View.OnClickListener mOnClickListener;
    private Context mContext;
    private boolean mShowSummit;


    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public int intTTGipfelNr;
        public TextView textView_tableCol_RouteName;
        public TextView textView_tableCol_SummitName2Route;
        public TextView textView_tableCol_NumberOfComments;
        public TextView textView_tableCol_MeanGrade;
        public TextView textViewStyleAsscended;
        public TextView textView_tableCol_DateAsscended;
        public TextView textView_tableCol_MyComment;

        public MyViewHolder(View v) {
            super(v);
            textView_tableCol_RouteName = v.findViewById(R.id.textView_tableCol_RouteName);
            textView_tableCol_SummitName2Route = v.findViewById(R.id.textView_tableCol_SummitName2Route);
            textView_tableCol_NumberOfComments = v.findViewById(R.id.textView_tableCol_NumberOfComments);
            textView_tableCol_MeanGrade = v.findViewById(R.id.textView_tableCol_MeanGrade);
            textViewStyleAsscended = v.findViewById(R.id.textViewStyleAsscended);
            textView_tableCol_DateAsscended = v.findViewById(R.id.textView_tableCol_DateAsscended);
            textView_tableCol_MyComment = v.findViewById(R.id.textView_tableCol_MyComment);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TT_Route_ANDAdapter(View.OnClickListener onClickListener,
                               ArrayList<TT_Route_AND> myDataset, boolean showSummit) {
        lstTT_Routes_AND = myDataset;
        mOnClickListener = onClickListener;
        mContext = ((Fragment) onClickListener).getContext();
        mShowSummit = showSummit;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public TT_Route_ANDAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                               int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.routes_lv_item_found, parent, false);
        v.setOnClickListener(mOnClickListener);

        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        fillViewHolder(holder, lstTT_Routes_AND.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return lstTT_Routes_AND.size();
    }

    private void fillViewHolder(@NotNull MyViewHolder aTT_Route_ANDView,
                                @NotNull TT_Route_AND currentTT_Route_AND) {
        // Transfer the stock data from the data object
        // to the view objects
//		TT_Route_AND currentTT_Route_AND = lstTT_Routes_AND.get(position);
        Log.i(TAG, "Suche Weg...: "
                + currentTT_Route_AND.getStrWegName());
        aTT_Route_ANDView.intTTGipfelNr = currentTT_Route_AND.getIntGipfelNr();
        aTT_Route_ANDView.textView_tableCol_RouteName
                .setText(MessageFormat.format("{0}  ({1})",
                        currentTT_Route_AND.getStrWegName(),
                        currentTT_Route_AND.getStrSchwierigkeitsGrad()));
        aTT_Route_ANDView.textView_tableCol_SummitName2Route.setText(MessageFormat.format("{0}{1}",
                mContext.getResources().getString(R.string.tableCol_SummitName),
                currentTT_Route_AND.getStrGipfelName()));
        aTT_Route_ANDView.textView_tableCol_NumberOfComments.setText(MessageFormat.format("{0}  {1}",
                mContext.getResources().getString(R.string.tableCol_NumberOfComments),
                currentTT_Route_AND.getIntAnzahlDerKommentare().toString()));
        // getting a formater for default locale
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        aTT_Route_ANDView.textView_tableCol_MeanGrade.setText(MessageFormat.format("{0}{1}",
                mContext.getResources().getString(R.string.tableCol_MeanGrade),
                nf.format(currentTT_Route_AND.getFltMittlereWegBewertung())));
//		aTT_Route_ANDView.CheckBoxAsscended.setChecked(currentTT_Route_AND
//				.getBegehungsStil() > 1);

        SpannableString ss = new SpannableString("  " + EnumBegehungsStil.values()[currentTT_Route_AND
                .getBegehungsStil()].toString());
        Drawable d = mContext.getResources().getDrawable(R.drawable.my_checkbox);
        switch (currentTT_Route_AND.getBegehungsStil()) {
            case 1: // EnumBegehungsStil.TO_DO.ordinal():
                d = mContext.getResources().getDrawable(R.drawable.ic_todo);
                break;
            case 2: // EnumBegehungsStil.SACK.ordinal():
                d = mContext.getResources().getDrawable(R.drawable.sack);
                break;
            case 3: // EnumBegehungsStil.NACHSTIEG.ordinal():
                d = mContext.getResources().getDrawable(R.drawable.ic_vog);
                break;
            case 4: // EnumBegehungsStil.SITZSCHLINGE.ordinal():
                d = mContext.getResources().getDrawable(R.drawable.ic_ruheschlinge);
                break;
            case 5: // EnumBegehungsStil.ALLESFREI.ordinal():
                d = mContext.getResources().getDrawable(R.drawable.ic_allesfrei);
                break;
            case 6: // EnumBegehungsStil.GETEILTEFUEHRUNG.ordinal():
                d = mContext.getResources().getDrawable(R.drawable.ic_seilschaft);
                break;
            case 7: // EnumBegehungsStil.ROTPUNKT.ordinal():
                d = mContext.getResources().getDrawable(R.drawable.ic_rp);
                break;
            case 8: // EnumBegehungsStil.ONSIGHT.ordinal():
                d = mContext.getResources().getDrawable(R.drawable.ic_onsight);
                break;
            case 9: // EnumBegehungsStil.SOLO.ordinal():
                d = mContext.getResources().getDrawable(R.drawable.ic_solo);
                break;
        }

        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        aTT_Route_ANDView.textViewStyleAsscended.setText(ss);
        aTT_Route_ANDView.textView_tableCol_DateAsscended
                .setText(currentTT_Route_AND.getDatumBestiegen());
        aTT_Route_ANDView.textView_tableCol_MyComment.setText(MessageFormat.format("{0}{1}",
                mContext.getResources()
                        .getString(R.string.tableCol_MyComment),
                currentTT_Route_AND.getStrKommentar()));
        // Set the onClick Listener
        if (!mShowSummit) {
            aTT_Route_ANDView.textView_tableCol_SummitName2Route
                    .setVisibility(View.GONE);
        } else {
            aTT_Route_ANDView.textView_tableCol_SummitName2Route
                    .setTag(currentTT_Route_AND.getIntGipfelNr());
            aTT_Route_ANDView.textView_tableCol_SummitName2Route
                    .setOnClickListener( mOnClickListener);
        } // end if

    }
}
