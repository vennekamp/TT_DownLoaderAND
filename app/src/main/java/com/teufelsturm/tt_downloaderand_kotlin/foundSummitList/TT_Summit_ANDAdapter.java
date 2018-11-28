package com.teufelsturm.tt_downloaderand_kotlin.foundSummitList;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.teufelsturm.tt_downloaderand_kotlin.R;
import com.teufelsturm.tt_downloaderand_kotlin.foundRouteSingle.TT_Route_AND;
import com.teufelsturm.tt_downloaderand_kotlin.foundRoutesList.TT_Route_ANDAdapter;
import com.teufelsturm.tt_downloaderand_kotlin.foundSummitSingle.TT_Summit_AND;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class TT_Summit_ANDAdapter
		extends RecyclerView.Adapter<TT_Summit_ANDAdapter.MyViewHolder>  {

    private View.OnClickListener mOnClickListener;
    private Context mContext;
	private ArrayList<TT_Summit_AND> lstTT_Gipfel_AND;



    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView_tableCol_SummitName;
        public TextView textView_tableCol_Area;
        public TextView textView_tableColSummitNumberOfficial;
        public TextView textView_tableCol_NumberOfRoutes;
        public TextView textView_tableCol_NumberofStarRoutes;
        public TextView textView_tableCol_EasiestGrade;
        public CheckBox checkBoxAsscended_in_lv;
        public TextView textView_tableCol_MyComment;
        public TextView textView_tableCol_DateAsscended;

        public MyViewHolder(View v) {
            super(v);
            textView_tableCol_SummitName = v.findViewById(R.id.textView_tableCol_SummitName);
            textView_tableCol_Area = v.findViewById(R.id.textView_tableCol_Area);
            textView_tableColSummitNumberOfficial = v.findViewById(R.id.textView_tableColSummitNumberOfficial);
            textView_tableCol_NumberOfRoutes = v.findViewById(R.id.textView_tableCol_NumberOfRoutes);
            textView_tableCol_NumberofStarRoutes = v.findViewById(R.id.textView_tableCol_NumberofStarRoutes);
            textView_tableCol_EasiestGrade = v.findViewById(R.id.textView_tableCol_EasiestGrade);
            checkBoxAsscended_in_lv = v.findViewById(R.id.checkBoxAsscended_in_lv);
            textView_tableCol_MyComment = v.findViewById(R.id.textView_tableCol_MyComment);
            textView_tableCol_DateAsscended = v.findViewById(R.id.textView_tableCol_DateAsscended);

        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public TT_Summit_ANDAdapter(View.OnClickListener onClickListener,
                               ArrayList<TT_Summit_AND> myDataset) {
        lstTT_Gipfel_AND = myDataset;
        mOnClickListener = onClickListener;
        mContext = ((Fragment) onClickListener).getContext();
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public TT_Summit_ANDAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                               int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.summit_lv_item_found, parent, false);
        v.setOnClickListener(mOnClickListener);

        return new TT_Summit_ANDAdapter.MyViewHolder(v);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull TT_Summit_ANDAdapter.MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        fillViewHolder(holder, lstTT_Gipfel_AND.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return lstTT_Gipfel_AND.size();
    }

    private void fillViewHolder(@NotNull TT_Summit_ANDAdapter.MyViewHolder aTT_Gipfel_ANDView,
                                @NotNull TT_Summit_AND currentTT_Gipfel_AND ) {

        // Transfer the stock data from the data object
        // to the view objects
        aTT_Gipfel_ANDView.textView_tableCol_SummitName.setText(currentTT_Gipfel_AND.getStr_SummitName() );
        aTT_Gipfel_ANDView.textView_tableCol_Area.setText(
                new StringBuilder()
                        .append(mContext.getApplicationContext().getResources()
                                .getString(R.string.tableCol_Area))
                        .append(currentTT_Gipfel_AND.getStr_Area()).toString());
        aTT_Gipfel_ANDView.textView_tableColSummitNumberOfficial.setText(
                new StringBuilder()
                        .append(mContext.getApplicationContext().getResources()
                                .getString(R.string.tableColSummitNumberOfficial))
                        .append(currentTT_Gipfel_AND.getInt_SummitNumberOfficial()).toString());
        aTT_Gipfel_ANDView.textView_tableCol_NumberOfRoutes.setText(
                new StringBuilder()
                        .append(mContext.getApplicationContext().getResources()
                                .getString(R.string.tableCol_NumberOfRoutes))
                        .append(currentTT_Gipfel_AND.getInt_NumberOfRoutes().toString())
                        .toString());
        aTT_Gipfel_ANDView.textView_tableCol_NumberofStarRoutes.setText(
                new StringBuilder()
                        .append(mContext.getApplicationContext().getResources()
                                .getString(R.string.tableCol_NumberofStarRoutes))
                        .append(currentTT_Gipfel_AND.getInt_NumberofStarRoutes().toString())
                        .toString());
        aTT_Gipfel_ANDView.textView_tableCol_EasiestGrade.setText(
                new StringBuilder()
                        .append(mContext.getApplicationContext().getResources()
                                .getString(R.string.tableCol_EasiestGrade))
                        .append(currentTT_Gipfel_AND.getStr_EasiestGrade()).toString());
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
        aTT_Gipfel_ANDView.checkBoxAsscended_in_lv.setChecked(
                currentTT_Gipfel_AND.getBln_Asscended() );

        aTT_Gipfel_ANDView.textView_tableCol_DateAsscended.setText(
                currentTT_Gipfel_AND.getStr_DateAsscended() );
        aTT_Gipfel_ANDView.textView_tableCol_MyComment.setText(new StringBuilder()
                .append(mContext.getApplicationContext().getResources()
                        .getString(R.string.tableCol_MyComment))
                .append(currentTT_Gipfel_AND.getStr_MyComment()).toString());
    }
}
