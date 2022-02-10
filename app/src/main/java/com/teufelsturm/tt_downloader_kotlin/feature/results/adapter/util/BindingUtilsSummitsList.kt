package com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util

import android.text.Editable
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.teufelsturm.tt_downloader_kotlin.R
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTTSummitAND
import com.teufelsturm.tt_downloader_kotlin.data.entity.SummitBaseDataInterface
import com.teufelsturm.tt_downloader_kotlin.data.entity.SummitWithMySummitComment
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTSummitAND
import com.teufelsturm.tt_downloader_kotlin.feature.searches.generics.convertLongToDateString
import com.teufelsturm.tt_downloader_kotlin.feature.searches.generics.formatSummitComments

@BindingAdapter("summitTextFormatted")
fun TextView.summitTextFormatted(item: SummitWithMySummitComment?) {
    text = item?.let { item.ttSummitAND.strName }
    // context.getString(R.string.summit_number, item.ttSummitAND.strName,item.ttSummitAND.intKleFuGipfelNr)
}

@BindingAdapter("summitTextFormatted")
fun TextView.summitTextFormatted(item: TTSummitAND?) {
    text =
        item?.let {
            val nr = it.intKleFuGipfelNr?.toString() ?: "??"
            context.getString(R.string.formatted_summit_number, it.strName, nr)
        }
}

@BindingAdapter("myCommentStringFormatted")
fun EditText.myCommentStringFormatted(mySummits: List<MyTTSummitAND>?) {
    text = mySummits?.let { formatSummitComments(it) as Editable }
}

@BindingAdapter("isAscendedFormatted")
fun CheckBox.isAscendedFormatted(listMySummitComment: List<MyTTSummitAND>?) {
    var mIsChecked = false // set the value
    listMySummitComment?.let { list ->
        list.forEach {
            if (mIsChecked || (it.isAscendedSummit != null && it.isAscendedSummit!!)) {
                mIsChecked = true
                return@let
            }
        }
    }
    this.isChecked = mIsChecked
}

@BindingAdapter("summitExtendedTextFormatted")
fun TextView.summitExtendedTextFormatted(item: SummitBaseDataInterface?) {
    text = item?.let {
        val nr = item.intKleFuGipfelNr?.toString() ?: "??"
        context.getString(
            R.string.summit_number_area,
            it.strName ?: "??",
            nr,
            it.strGebiet ?: "?"
        )
    }
}


@BindingAdapter("areaTextFormatted")
fun TextView.areaTextFormatted(item: TTSummitAND) {
    text = item.strGebiet
}

@BindingAdapter("summitNumberTextFormatted")
fun TextView.summitNumberTextFormatted(item: TTSummitAND) {
    text = context.getString(R.string.sharpSummitNumber, item.intKleFuGipfelNr ?: "??")
}

@BindingAdapter("routeCountTextFormatted")
fun TextView.routeCountTextFormatted(item: TTSummitAND) {
    text = item.intAnzahlWege.toString()
}

@BindingAdapter("starredRouteCountTextFormatted")
fun TextView.starredRouteCountTextFormatted(item: TTSummitAND) {
    text = item.intAnzahlSternchenWege.toString()
}

@BindingAdapter("easiestRouteGradeTextFormatted")
fun TextView.easiestRouteGradeTextFormatted(item: TTSummitAND) {
    text = item.strLeichtesterWeg.toString()
}


@BindingAdapter("ascensionDateFormatted")
fun TextView.ascensionDateFormatted(items: List<MyTTSummitAND>) {
    var myDate: Long? = null
    items.let { list ->
        list.forEach {
            it.myIntDateOfAscend?.let { itDate ->
                myDate = if (myDate == null) {
                    itDate
                } else {
                    (myDate!!).coerceAtMost(itDate)
                }
            }
        }
    }
    text = if (myDate == null) " --- " else convertLongToDateString(myDate!!)
}

@BindingAdapter("myCommentTextFormatted")
fun TextView.myCommentTextFormatted(mySummits: List<MyTTSummitAND>) {
    text = formatSummitComments(mySummits)
}

@BindingAdapter("android:visibility")
fun View.setVisibility(items: List<MyTTSummitAND>) {
    visibility = if (items.isEmpty()) View.GONE else View.VISIBLE
}
