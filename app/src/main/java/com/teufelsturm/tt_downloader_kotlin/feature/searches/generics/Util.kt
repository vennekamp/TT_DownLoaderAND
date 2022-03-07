package com.teufelsturm.tt_downloader_kotlin.feature.searches.generics

import android.annotation.SuppressLint
import android.text.Spanned
import android.util.Log
import com.teufelsturm.tt_downloader_kotlin.data.entity.Comments
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTTCommentAND
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.toHTMLSpan
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Take the Long milliseconds returned by the system and stored in Room,
 * and convert it to a nicely formatted string for display.
 *
 * EEE - Display the short letter version of the weekday
 * MMM - Display the letter abbreviation of the nmotny
 * dd-yyyy - day in month and full year numerically
 * HH:mm - Hours and minutes in 24hr format
 */
@SuppressLint("SimpleDateFormat")
fun convertLongToDateTimeString(systemTime: Long): String {
    // Create a DateFormatter object for displaying date in specified format.
    val formatter = SimpleDateFormat("E, d.MMM yyyy' - 'HH:mm", Locale.GERMANY)
    return formatter.format(systemTime).toString()
}

fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("E, d.MMM yyyy", Locale.GERMANY)
        .format(systemTime).toString()
}

fun convertLongToSimpleDateString(systemTime: Long): String {
    return SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
        .format(systemTime).toString()
}

fun appendOrReplaceTime(_ascentDate: String?, hour: Int, minute: Int): String {

    if (_ascentDate == null) return "ohne Datum ${timeToString(hour, minute)}"

    val regEx = "(\\d{1,2}:\\d{1,2})".toRegex()
    if (_ascentDate.contains(regEx)) {
        return _ascentDate.replace(regEx, timeToString(hour, minute))
    }
    return "$_ascentDate ${timeToString(hour, minute)}"
}

fun timeToString(hour: Int, minute: Int): String {
    return "${hour.toString().padStart(2, '0')}:${
        minute.toString().padStart(2, '0')
    }"
}

fun convertDateTimeStringToLong(_ascentDate: String?): Long? {
    if (_ascentDate == null) return null
    // DATE
    var ascentDateObject: Date?
    var dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
    dateFormat.isLenient = false
    try {
        ascentDateObject = dateFormat.parse(_ascentDate.trim { it <= ' ' })
    } catch (pe: ParseException) {
        try {
            dateFormat = SimpleDateFormat("E, d.MMM yyyy", Locale.GERMAN)
            val dateMillis =
                (dateFormat.parse(_ascentDate.trim { it <= ' ' }) ?: throw  ParseException(
                    null,
                    0
                )).time
            ascentDateObject = Date(dateMillis)
        } catch (pe: ParseException) {
            ascentDateObject = null
        }
    }
    // TIME
    val matcher = "(\\d{1,2}:\\d{1,2})".toRegex()
    val ascentTime = matcher.find(_ascentDate)?.groupValues?.first()
    ascentTime?.let { mTime ->
        val timeFormat = SimpleDateFormat("HH:mm", Locale.GERMAN)
        try {
            val timeMillis = timeFormat.parse(mTime.trim { it <= ' ' })?.time ?: 0L
            ascentDateObject?.let { it.time = it.time.plus(timeMillis) }
        } catch (pe: ParseException) {
            Log.e("convertDateStringToLong", "No time extracted from: $_ascentDate")
        }
    }
    val mCalendar: Calendar = GregorianCalendar()
    val mTimeZone = mCalendar.timeZone
    val mGMTOffset = mTimeZone.rawOffset

    return ascentDateObject?.time?.plus(mGMTOffset)
}

fun formatSummitComments(mySummits: List<Comments.MyTTCommentANDWithPhotos>): Spanned {
    val sb = StringBuilder()
    sb.apply {
        mySummits.forEach { comment ->
            append(if (comment.myTTCommentAND.isAscendedType > 2) "&#9745;" else " &#10060;")
            append(comment.myTTCommentAND.myIntDateOfAscend ?: "[ --- ]")
            append(" ")
            append("${comment.myTTCommentAND.strMyComment}")
            if (comment != mySummits.last()) append("<br>")
        }
    }
    return sb.toString().toHTMLSpan()
}
// MyTTCommentAND
fun formatSummitFromMyCommentComments(mySummits: List<MyTTCommentAND>): Spanned {
    val sb = StringBuilder()
    sb.apply {
        mySummits.forEach { comment ->
            append(if (comment.isAscendedType > 0) "&#9745;" else " &#10060;")
            append(comment.myIntDateOfAscend ?: "[ --- ]")
            append(" ")
            append("${comment.strMyComment}")
            if (comment != mySummits.last()) append("<br>")
        }
    }
    return sb.toString().toHTMLSpan()
}
fun formatRouteComments(route: Comments.RouteWithMyComment): Spanned {
    val sb = StringBuilder()
    sb.apply {
        route.myTTCommentANDList.forEach { comment ->
            append(if (comment.isAscendedType.let { it > 2 }) "&#9745;" else " &#10060;")
            append(comment.myIntDateOfAscend?.let { "[$it]" } ?: "[ --- ]")
            append(" ")
            append("${comment.strMyComment}")
            if (comment != route.myTTCommentANDList.last()) append("<br>")
        }
    }
    return sb.toString().toHTMLSpan()
}

fun isAscendedRoute(route: Comments.RouteWithMyComment): Boolean {
    route.myTTCommentANDList.forEach { mRoute ->
        if (mRoute.isAscendedType.let { it > 2 }) {
            Log.i("isAscendedFormatted", " ==> TRUE")
            return true
        }
    }
    return false
}

fun maxAscentRoute(comments: List<Comments.MyTTCommentANDWithPhotos>): Int {
    var maxAscent = 0 // set the value
    comments.forEach { myRoute ->
        myRoute.myTTCommentAND.isAscendedType.let {
            if (it > maxAscent) maxAscent = it
        }
    }
    return maxAscent
}

fun maxAscentRoute(route: Comments.RouteWithMyComment): Int {
    var maxAscent = 0 // set the value
    route.myTTCommentANDList.forEach { myRoute ->
        myRoute.isAscendedType.let {
            if (it > maxAscent) maxAscent = it
        }
    }
    return maxAscent
}

val float2Rating: ((Float) -> String) = {
    when (it) {
        0f -> "'---' (Kamikaze)"
        1f -> "'--' (sehr schlecht)"
        2f -> "'-' (schlecht)"
        3f -> "(Normal)"
        4f -> "'+' (gut)"
        5f -> "'++' (sehr gut)"
        6f -> "'+++' (Herausragend)"
        else -> it.toString()
    }
}

val oldFloat2Grade: ((Float) -> String) = {
    when (it) {
        0f -> "min"
        1f -> "I"
        2f -> "II"
        3f -> "III"
        4f -> "IV"
        5f -> "V"
        6f -> "VI"
        7f -> "VIIa"
        8f -> "VIIb"
        9f -> "VIIc"
        10f -> "VIIIa"
        11f -> "VIIIb"
        12f -> "VIIIC"
        13f -> "IXa"
        14f -> "IXb"
        15f -> "IXc"
        16f -> "Xa"
        17f -> "Xb"
        18f -> "Xc"
        19f -> "XIa"
        20f -> "XIb"
        21f -> "XIc"
        22f -> "XIIa"
        23f -> "XIIb"
        24f -> "XIIc"
        25f -> "XIIIa"
        26f -> "XIIIb"
        27f -> "XIIIc"
        28f -> "XIVa"
        29f -> "Sp1"
        30f -> "Sp2"
        31f -> "Sp3"
        32f -> "Sp4"
        33f -> "Sp5"
        34f -> "Sp6"
        35f -> "Sp7"
        else -> "max"
    }
}


fun formatItemCount4Button(itemCount: Int?, baseString: String): Spanned {
    val sb =
        if (itemCount != null) StringBuilder(itemCount.toString()).append(" ")
            .append(baseString)
        else StringBuilder("??? ").append(baseString)
    return sb.toString().toHTMLSpan()
}
