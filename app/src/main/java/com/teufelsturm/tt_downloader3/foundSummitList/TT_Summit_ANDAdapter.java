package com.teufelsturm.tt_downloader3.foundSummitList;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.teufelsturm.tt_downloader3.R;
import com.teufelsturm.tt_downloader3.model.TT_Summit_AND;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class TT_Summit_ANDAdapter
		extends RecyclerView.Adapter<TT_Summit_ANDAdapter.MyViewHolder>  {

    private View.OnClickListener mOnClickListener;
    private Context mContext;
	private ArrayList<TT_Summit_AND> lstTT_Gipfel_AND;



    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView textView_tableCol_SummitName;
        TextView textView_tableCol_Area;
        TextView textView_tableColSummitNumberOfficial;
        TextView textView_tableCol_NumberOfRoutes;
        TextView textView_tableCol_NumberofStarRoutes;
        TextView textView_tableCol_EasiestGrade;
        CheckBox checkBoxAsscended_in_lv;
        TextView textView_tableCol_MyComment;
        TextView textView_tableCol_DateAsscended;

        MyViewHolder(View v) {
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
    TT_Summit_ANDAdapter(View.OnClickListener onClickListener,
                               ArrayList<TT_Summit_AND> myDataset) {
        lstTT_Gipfel_AND = myDataset;
        mOnClickListener = onClickListener;
        mContext = ((Fragment) onClickListener).getContext();
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                               int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.summit_lv_item_found, parent, false);
        v.setOnClickListener(mOnClickListener);

        return new MyViewHolder(v);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        fillViewHolder(holder, lstTT_Gipfel_AND.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return lstTT_Gipfel_AND.size();
    }

    private void fillViewHolder(@NotNull MyViewHolder aTT_Gipfel_ANDView,
                                @NotNull TT_Summit_AND currentTT_Gipfel_AND ) {

        // Transfer the stock data from the data object
        // to the view objects
        aTT_Gipfel_ANDView.textView_tableCol_SummitName.setText(currentTT_Gipfel_AND.getStr_TTSummitName() );
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
                        .append(currentTT_Gipfel_AND.getInt_NumberOfRoutes())
                        .toString());
        aTT_Gipfel_ANDView.textView_tableCol_NumberofStarRoutes.setText(
                new StringBuilder()
                        .append(mContext.getApplicationContext().getResources()
                                .getString(R.string.tableCol_NumberofStarRoutes))
                        .append(currentTT_Gipfel_AND.getInt_NumberofStarRoutes())
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
//				.getSQL4CommentsSearch(R.string.tableCol_Gps)
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
