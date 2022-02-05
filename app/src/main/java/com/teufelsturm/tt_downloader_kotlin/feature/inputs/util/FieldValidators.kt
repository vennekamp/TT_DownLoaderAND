package com.teufelsturm.tt_downloader_kotlin.feature.inputs.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * created by : Mustufa Ansari
 * Email : mustufaayub82@gmail.com
 */

object FieldValidators {
    /**
     * checking is string is a date
     * @param string value to check
     * @return true if contain else false
     */
    fun isValidDate(inDate: String): Boolean {
        if (inDate.isNullOrEmpty()) return true
        if (inDate.contains(Regex("\\d\\d\\d\\d\\d"))) return false
        if (inDate.contains("..")) return false
        if (inDate.contains("/")) return false
        if (inDate.contains("-")) return false
        var dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
        var date: Date
        dateFormat.isLenient = false
        try {
            date = dateFormat.parse(inDate.trim { it <= ' ' })
        } catch (pe: ParseException) {
            try {
                dateFormat = SimpleDateFormat("E, dd.MMM yyyy", Locale.GERMAN)
                date = dateFormat.parse(inDate.trim { it <= ' ' })
            } catch (pe: ParseException) {
                return false
            }
        }
        return date.time > -5367430408000L // in the year "1800"
    }
}