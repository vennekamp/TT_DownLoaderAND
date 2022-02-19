package com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.view.View
import android.widget.CheckBox
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.teufelsturm.tt_downloader_kotlin.data.entity.Comments
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTRouteAND
import com.teufelsturm.tt_downloader_kotlin.feature.searches.generics.*
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
            item.intSterne?.let { sterne ->
                append(" ")
                repeat(sterne) { append("*") }
            }
        }
        text = span
    }
}

@BindingAdapter("gradeTextFormatted")
fun TextView.gradeTextFormatted(item: Comments.RouteWithMyComment) {
    val sb =
        StringBuilder(item.ttRouteAND.strSchwierigkeitsGrad ?: "???")
    text = sb.toString().toHTMLSpan()
}

@BindingAdapter("commentCountTextFormatted")
fun TextView.commentCountTextFormatted(item: Comments.RouteWithMyComment) {
    text = item.ttRouteAND.intAnzahlDerKommentare.toString()
}

@BindingAdapter("meanGradeTextFormatted")
fun TextView.meanGradeTextFormatted(item: Comments.RouteWithMyComment) {
    val df = DecimalFormat("#.##")
    text = df.format(item.ttRouteAND.fltMittlereWegBewertung)
}

@BindingAdapter("fltRatingFormattedFormatted")
fun RatingBar.fltRatingFormattedFormatted(item: Comments.RouteWithMyComment) {
    rating = item.ttRouteAND.fltMittlereWegBewertung ?: 0f
}


@BindingAdapter("isAscendedRouteFormatted")
fun CheckBox.isAscendedRouteFormatted(item: Comments.RouteWithMyComment) {
    isChecked = isAscendedRoute(item)
}

@BindingAdapter("ascensionDateRouteFormatted")
fun TextView.ascensionDateRouteFormatted(item: Comments.RouteWithMyComment) {
    var myDate: Long? = null
    item.myTTCommentANDList.let { list ->
        list.forEach {
            it.myIntDateOfAscend?.let { itDate ->
                myDate = if (myDate == null) {
                    convertDateTimeStringToLong(itDate)
                } else {
                    (myDate!!).coerceAtMost(
                        convertDateTimeStringToLong(itDate) ?: Long.MIN_VALUE
                    )
                }
            }
        }
    }
    text = if (myDate == null) " --- " else convertLongToDateString(myDate!!)
}

@BindingAdapter("myAscentFormatted")
fun TextView.myAscentFormatted(myCommentList: List<Comments.MyTTCommentANDWithPhotos>?) {
    val spSB = SpannableStringBuilder()
    myCommentList?.let {
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
fun TextView.myExtendedCommentStringFormatted(route: Comments.RouteWithMyComment?) {
    val spSB = SpannableStringBuilder()
    spSB.apply {
        route?.myTTCommentANDList?.forEach { comment ->
            comment.isAscendedType.let { type ->
                append(" ")
                val mDrawable = RouteAscentType.values()[type].drawable(context)
                mDrawable?.setBounds(0, 0, lineHeight, lineHeight)
                val span = mDrawable?.let { d -> ImageSpan(d, ImageSpan.ALIGN_BASELINE) }
                setSpan(span, this.length - 1, this.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            }
            comment.myIntDateOfAscend?.let {
                append("[${it}]") ?: append("[ --- ]")
            }
            append(" ")
            append("${comment.strMyComment}")
            if (comment != route.myTTCommentANDList.last()) append("<br>".toHTMLSpan())
        }
    }
    text = spSB
}

@BindingAdapter("myCommentRouteTextFormatted")
fun TextView.myCommentRouteTextFormatted(item: Comments.RouteWithMyComment) {
    text = formatRouteComments(item)
}


@BindingAdapter("android:visibility")
fun View.setVisibility(item: Comments.RouteWithMyComment) {
    visibility = if (item.myTTCommentANDList.isEmpty()) View.GONE else View.VISIBLE
}