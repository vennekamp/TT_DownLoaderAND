package com.teufelsturm.tt_downloader_kotlin.results.adapter.util

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.view.View
import android.widget.CheckBox
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.teufelsturm.tt_downloader_kotlin.data.entity.RouteComments
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTRouteAND
import com.teufelsturm.tt_downloader_kotlin.searches.generics.*
import java.text.DecimalFormat


@BindingAdapter("routeTextFormatted")
fun TextView.routeTextFormatted(ttRouteAND: TTRouteAND?) {
    val span = SpannableStringBuilder()
    ttRouteAND?.let { item ->
        AddConstructionAndJump(item, span)
        span.apply {

            if (item.blnAusrufeZeichen == true) {
                "&#10071; ".toHTMLSpan()
            }
            append((item.WegName ?: "").toHTMLSpan())
            item.intSterne?.let { append(" ") }
            item.intSterne?.let { repeat(item.intSterne!!) { append("*") } }
        }
    }
    text = span
}

@BindingAdapter("gradeTextFormatted")
fun TextView.gradeTextFormatted(item: RouteComments.RouteWithMyRouteComment) {
    val sb =
        StringBuilder(item.ttRouteAND.strSchwierigkeitsGrad ?: "???")
    text = sb.toString().toHTMLSpan()
}

@BindingAdapter("commentCountTextFormatted")
fun TextView.commentCountTextFormatted(item: RouteComments.RouteWithMyRouteComment) {
    text = item.ttRouteAND.intAnzahlDerKommentare.toString()
}

@BindingAdapter("meanGradeTextFormatted")
fun TextView.meanGradeTextFormatted(item: RouteComments.RouteWithMyRouteComment) {
    val df = DecimalFormat("#.##")
    text = df.format(item.ttRouteAND.fltMittlereWegBewertung)
}

@BindingAdapter("fltRatingFormattedFormatted")
fun RatingBar.fltRatingFormattedFormatted(item: RouteComments.RouteWithMyRouteComment) {
    rating = item.ttRouteAND.fltMittlereWegBewertung ?: 0f
}


@BindingAdapter("isAscendedRouteFormatted")
fun CheckBox.isAscendedRouteFormatted(item: RouteComments.RouteWithMyRouteComment) {
    isChecked = isAscendedRoute(item)
}

@BindingAdapter("ascensionDateRouteFormatted")
fun TextView.ascensionDateRouteFormatted(item: RouteComments.RouteWithMyRouteComment) {
    var myDate: Long? = null
    item.myTTRouteANDList.let { list ->
        list.forEach {
            it.myIntDateOfAscendRoute?.let { itDate ->
                myDate = if (myDate == null) {
                    convertDateTimeStringToLong(itDate)
                } else {
                    (myDate!!).coerceAtMost(convertDateTimeStringToLong(itDate) ?: Long.MIN_VALUE)
                }
            }
        }
    }
    text = if (myDate == null) " --- " else convertLongToDateString(myDate!!)
}

@BindingAdapter("myAscentFormatted")
fun TextView.myAscentFormatted(myRouteList: List<RouteComments.MyTTRouteANDWithPhotos>?) {
    val spSB = SpannableStringBuilder()
    myRouteList?.let {
        val maxAscent = maxAscentRoute(it)
        spSB.apply {
                append(" ")
                val mDrawable = RouteAscentType.values()[maxAscent].drawable(context)
                mDrawable?.setBounds(0, 0, lineHeight, lineHeight)
                val span = mDrawable?.let { d -> ImageSpan(d, ImageSpan.ALIGN_BASELINE) }
                setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                append(" ${RouteAscentType.values()[maxAscent].text()}")
            }
        }

    text = spSB
}

@BindingAdapter("myExtendedCommentStringFormatted")
fun TextView.myExtendedCommentStringFormatted(route: RouteComments.RouteWithMyRouteComment?) {
    val spSB = SpannableStringBuilder()
    spSB.apply {
        route?.myTTRouteANDList?.forEach { comment ->
            comment.isAscendedRouteType?.let { type ->
                append(" ")
                val mDrawable = RouteAscentType.values()[type].drawable(context)
                mDrawable?.setBounds(0, 0, lineHeight, lineHeight)
                val span = mDrawable?.let { d -> ImageSpan(d, ImageSpan.ALIGN_BASELINE) }
                setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            }
            comment.myIntDateOfAscendRoute?.let {
                append("[${it}]") ?: append("[ --- ]")
            }
            append(" ")
            append("${comment.strMyRouteComment}")
            if (comment != route.myTTRouteANDList.last()) append("<br>".toHTMLSpan())
        }
    }
    text = spSB
}

@BindingAdapter("myCommentRouteTextFormatted")
fun TextView.myCommentRouteTextFormatted(item: RouteComments.RouteWithMyRouteComment) {
    text = formatRouteComments(item)
}


@BindingAdapter("android:visibility")
fun View.setVisibility(item: RouteComments.RouteWithMyRouteComment) {
    visibility = if (item.myTTRouteANDList.isEmpty()) View.GONE else View.VISIBLE
}