package com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.teufelsturm.tt_downloader_kotlin.data.entity.Comments
import com.teufelsturm.tt_downloader_kotlin.data.entity.CommentsSummit
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTRouteAND
import com.teufelsturm.tt_downloader_kotlin.feature.searches.generics.convertLongToDateTimeString


@BindingAdapter("tvCommentInCommentFormatted")
fun TextView.tvCommentInCommentFormatted(item: CommentsSummit.CommentsWithRouteWithSummit) {
    val comm = item.strEntryKommentar ?: " --- "
    text = comm.toHTMLSpan()
}

@BindingAdapter("tvUserGradeTextFormatted")
fun TextView.tvUserGradeTextFormatted(item: CommentsSummit.CommentsWithRouteWithSummit) {
    text = when (item.entryBewertung) {
        0 -> "kamikaze"
        1 -> "sehr schlecht"
        2 -> "schlecht"
        3 -> "(Normal)"
        4 -> "gut"
        5 -> "sehr gut"
        6 -> "herausragend"
        else -> " -- keine -- "
    }
}

@BindingAdapter("tvUserNameTextFormatted")
fun TextView.tvUserNameTextFormatted(item: CommentsSummit.CommentsWithRouteWithSummit) {
    text = item.strEntryUser
}

@BindingAdapter("tvCommentDateTextFormatted")
fun TextView.tvCommentDateTextFormatted(item: CommentsSummit.CommentsWithRouteWithSummit) {
    text = item.entryDatum?.let { dt -> convertLongToDateTimeString(dt) } ?: ""
}

@BindingAdapter("fltRatingFormattedFormatted")
fun RatingBar.fltRatingFormattedFormatted(item: CommentsSummit.CommentsWithRouteWithSummit) {
    rating = item.entryBewertung?.toFloat() ?: 0f
}

@BindingAdapter("android:visibility")
fun View.setVisibility(item: CommentsSummit.CommentsWithRouteWithSummit?) {
    visibility = if (item?.intTTGipfelNr != null) View.VISIBLE else View.GONE
}

@BindingAdapter("tvRouteNameInCommentFormatted")
fun TextView.tvRouteNameInCommentFormatted(item: CommentsSummit.CommentsWithRouteWithSummit?) {
    item?.let {
        val sb = StringBuilder()
        sb.apply {
            if (item.blnAusrufeZeichen == true) append("&#10071; ")
            append(item.WegName)
            item.intSterne?.let { count ->
                if (count > 0) {
                    append(" ")
                    repeat(count) { append("*") }
                }
            }
            append(" (${item.strSchwierigkeitsGrad})")
        }
        text = sb.toString().toHTMLSpan()
    }
}

@BindingAdapter("tvRouteNameInCommentFormatted")
fun TextView.tvRouteNameInCommentFormatted(ttRouteAND: TTRouteAND?) {
    val span = SpannableStringBuilder()
    ttRouteAND?.let { item ->
        AddConstructionAndJump(item, span)
        span.apply {
            if (item.blnAusrufeZeichen == true) append("&#10071; ")
            append(item.WegName)
            item.intSterne?.let { count ->
                if (count > 0) {
                    append(" ")
                    repeat(count) { append("*") }
                }
            }
            append(" (${item.strSchwierigkeitsGrad})")
        }
    }
    text = span
}

@BindingAdapter("myIntDateOfMyAscendRouteFormatted")
fun TextView.myIntDateOfMyAscendRouteFormatted(item: Comments.MyTTRouteANDWithPhotos?) {
    text = item?.myTTCommentAND?.myIntDateOfAscend ?: ""
}

@BindingAdapter("tvDateAndTypeOfAscend")
fun TextView.tvDateAndTypeOfAscend(item: Comments.MyTTRouteANDWithPhotos?) {
    val spSB = SpannableStringBuilder()
    spSB.apply {
        item?.myTTCommentAND?.isAscendedType?.let { type ->
            append(" ")
            val mDrawable = RouteAscentType.values()[type].drawable(context)
            mDrawable?.setBounds(0, 0, lineHeight, lineHeight)
            val span = mDrawable?.let { d -> ImageSpan(d, ImageSpan.ALIGN_BASELINE) }
            setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            append(RouteAscentType.values()[type].text())
            append(", ")
            item.myTTCommentAND.myIntDateOfAscend?.let { dt -> append(dt) }
        }
    }
    text = spSB
}

@BindingAdapter("app:referencedTextView")
fun TextView.getTextFromReferencedTextView(anotherTextView: TextView) {
    text = anotherTextView.text
}

@BindingAdapter("tvMyAscendedPartner")
fun TextView.tvMyAscendedPartner(item: Comments.MyTTRouteANDWithPhotos?) {
    "Partner: ${item?.myTTCommentAND?.myAscendedPartner}".also { text = it }
}