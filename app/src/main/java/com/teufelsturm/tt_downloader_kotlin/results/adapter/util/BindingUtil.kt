package com.teufelsturm.tt_downloader_kotlin.results.adapter.util

import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.teufelsturm.tt_downloader_kotlin.R
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTRouteAND

fun String.toHTMLSpan(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}


fun TextView.AddConstructionAndJump(
    item: TTRouteAND,
    span: SpannableStringBuilder
) {
    if (item.ohneUnterstuetzungSchwierigkeitsGrad != null) {
        val undercon: Drawable = ContextCompat.getDrawable(context, R.drawable.undercon_icon)!!
        undercon.setBounds(0, 0, lineHeight, lineHeight)
        val img = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ImageSpan(undercon, ImageSpan.ALIGN_CENTER)
        } else {
            ImageSpan(undercon)
        }
        span.append(" ")
        span.setSpan(img, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }
    if (item.intSprungSchwierigkeitsGrad != null) {
        val undercon: Drawable = ContextCompat.getDrawable(context, R.drawable.hop_logo)!!
        // val lineHeight: Int = lineHeight
        undercon.setBounds(0, 0, lineHeight, lineHeight)
        val img = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ImageSpan(undercon, ImageSpan.ALIGN_CENTER)
        } else {
            ImageSpan(undercon)
        }
        span.append(" ")
        span.setSpan(img, span.length - 1, span.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }
}
