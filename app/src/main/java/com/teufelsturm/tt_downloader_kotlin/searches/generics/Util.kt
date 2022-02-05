package com.teufelsturm.tt_downloader_kotlin.searches.generics

import android.annotation.SuppressLint
import android.text.Spanned
import android.util.Log
import com.teufelsturm.tt_downloader_kotlin.data.entity.RouteComments
import com.teufelsturm.tt_downloader_kotlin.data.entity.SummitWithMySummitComment
import com.teufelsturm.tt_downloader_kotlin.results.adapter.util.toHTMLSpan
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
    return SimpleDateFormat("E, d.MMM yyyy' - 'HH:mm", Locale.GERMANY)
        .format(systemTime).toString()
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

fun formatSummitComments(summit: SummitWithMySummitComment): Spanned {
    val sb = StringBuilder()
    sb.apply {
        summit.myTTSummitANDList.forEach { comment ->
            append(if (comment.isAscendedSummit == true) "&#9745;" else " &#10060;")
            append(
                if (comment.myIntDateOfAscend != null) "[${
                    convertLongToDateString(
                        comment.myIntDateOfAscend!!
                    )
                }]" else "[ --- ]"
            )
            append(" ")
            append("${comment.strMySummitComment}")
            if (comment != summit.myTTSummitANDList.last()) append("<br>")
        }
    }
    return sb.toString().toHTMLSpan()
}

fun formatRouteComments(route: RouteComments.RouteWithMyRouteComment): Spanned {
    val sb = StringBuilder()
    sb.apply {
        route.myTTRouteANDList.forEach { comment ->
            append(if (comment.isAscendedRouteType?.let { it > 2 } == true) "&#9745;" else " &#10060;")
            append(comment.myIntDateOfAscendRoute?.let { "[$it]" } ?: "[ --- ]")
            append(" ")
            append("${comment.strMyRouteComment}")
            if (comment != route.myTTRouteANDList.last()) append("<br>")
        }
    }
    return sb.toString().toHTMLSpan()
}

fun isAscendedRoute(route: RouteComments.RouteWithMyRouteComment): Boolean {
    route.myTTRouteANDList.forEach { route ->
        if ((route.isAscendedRouteType?.let { it > 2 } == true)) {
            Log.i("isAscendedFormatted", " ==> TRUE")
            return true
        }
    }
    return false
}

fun maxAscentRoute(routes: List<RouteComments.MyTTRouteANDWithPhotos>): Int {
    var maxAscent = 0 // set the value
    routes.forEach { myRoute ->
        myRoute.myTTRouteAND?.isAscendedRouteType?.let {
            if (it > maxAscent) maxAscent = it
        }
    }
    return maxAscent
}

fun maxAscentRoute(route: RouteComments.RouteWithMyRouteComment): Int {
    var maxAscent = 0 // set the value
    route.myTTRouteANDList.forEach { myRoute ->
        myRoute.isAscendedRouteType?.let {
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
