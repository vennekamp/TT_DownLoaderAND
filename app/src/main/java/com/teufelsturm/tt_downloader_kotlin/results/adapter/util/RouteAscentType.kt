package com.teufelsturm.tt_downloader_kotlin.results.adapter.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import androidx.core.content.res.ResourcesCompat
import com.teufelsturm.tt_downloader_kotlin.R

/**
 *
 * Andere Varianten 1: (https://www.thecrag.com/dashboard)
 * Vorstieg Begehungen"
 * Vorstieg
 * Onsight
 * Flash
 * Rotpunkt
 * Pinkpunkt
 * Ground up red point
 * Grünpunkt Onsight
 * Grünpunkt
 * All free with rest
 * mit Hänger
 * Solo frei
 * Allgemeine Begehungen
 * durchgestiegen
 * Nachstieg Begehungen"
 * Nachstieg
 * Nachstieg frei
 * Nachstieg mit Hänger
 * Top-Rope Begehungen"
 * Toprope
 * Toprope on-sight
 * Toprope flash
 * Toprope frei
 * Toprope mit Hänger
 * Solo seilgesichert
 * Technische Begehungen"
 * technisch
 * technisch Solo
 * Solo Begehungen"
 * Solo frei
 * Onsight Solo
 * Versuchte Begehungen"
 * Nemesis
 * Versuch
 * projektiert
 * Rückzug
 * Historische Begehungen"
 * Erstbegehung
 * Erste freie Begehung
 *
 * Andere Varianten 2: YacGuide
 * (https://github.com/vennekamp/yacguide/blob/master/app/src/main/java/com/example/paetz/yacguide/database/Ascend.java)
 * "Solo"
 * "Onsight";
 * "Rotpunkt";
 * "Alles frei";
 * "Irgendwie hochgeschleudert";
 * "Wechselführung";
 * "Nachstieg";
 * "Hinterhergehampelt";
 * "Gesackt";
 *
 */

private val arrayOfRouteAscentTypes = arrayOf(
    RouteAscentType.GARNICHT,
    RouteAscentType.TODO,
    RouteAscentType.SACK,
    RouteAscentType.NACHSTIEG,
    RouteAscentType.SITZSCHLINGE,
    RouteAscentType.ALLESFREI,
    RouteAscentType.GETEILTEFUEHRUNG,
    RouteAscentType.ROTPUNKT,
    RouteAscentType.ONSIGHT,
    RouteAscentType.SOLO
)

sealed class RouteAscentType() {
    object GARNICHT : RouteAscentType()
    object TODO : RouteAscentType()
    object SACK : RouteAscentType()
    object NACHSTIEG : RouteAscentType()
    object SITZSCHLINGE : RouteAscentType()
    object ALLESFREI : RouteAscentType()
    object GETEILTEFUEHRUNG : RouteAscentType()
    object ROTPUNKT : RouteAscentType()
    object ONSIGHT : RouteAscentType()
    object SOLO : RouteAscentType()

    fun text(): String {
        return when (this) {
            is GARNICHT -> "NIX: Nicht Bestiegen"
            is TODO -> "ToDo: Will mal"
            is SACK -> "SACK: Gesackt..."
            is NACHSTIEG -> "VOG: Nachstieg"
            is SITZSCHLINGE -> "RS: mit Ruheschlinge"
            is ALLESFREI -> "AF: alles Frei"
            is GETEILTEFUEHRUNG -> "MIX: geteilte Führung"
            is ROTPUNKT -> "RP: Rotpunkt"
            is ONSIGHT -> "OS: on-sight: OS"
            is SOLO -> "SOLO: free solo"
        }
    }

    fun drawable(ctx: Context): Drawable? {
        return getMyDrawable(ctx, drawableID())
    }

    fun drawableID(): Int {
        return when (this) {
            is GARNICHT -> R.drawable.ic_checkbox
            is TODO -> R.drawable.ic_todo
            is SACK -> R.drawable.ic_sack
            is NACHSTIEG -> R.drawable.ic_vog
            is SITZSCHLINGE -> R.drawable.ic_ruheschlinge
            is ALLESFREI -> R.drawable.ic_allesfrei
            is GETEILTEFUEHRUNG -> R.drawable.ic_seilschaft
            is ROTPUNKT -> R.drawable.ic_rp
            is ONSIGHT -> R.drawable.ic_onsight
            is SOLO -> R.drawable.ic_solo
        }
    }

    fun ordinal(): Int? {
        return when (this) {
            is GARNICHT -> 0
            is TODO -> 1
            is SACK -> 2
            is NACHSTIEG -> 3
            is SITZSCHLINGE -> 4
            is ALLESFREI -> 5
            is GETEILTEFUEHRUNG -> 6
            is ROTPUNKT -> 7
            is ONSIGHT -> 8
            is SOLO -> 9
        }
    }

    private fun getMyDrawable(mContext: Context, id: Int): Drawable? {
        return ResourcesCompat.getDrawable(mContext.resources, id, null)
    }

    companion object {
        fun values() = arrayOfRouteAscentTypes

        fun getArrayOfRouteAscentTypesPairs(context: Context): List<Pair<Drawable?, String>> {
            val rtnArray = mutableListOf<Pair<Drawable?, String>>()
            values().forEach {
                rtnArray.add(
                    Pair(
                        it.drawable(context),
                        it.text()
                    )
                )
            }
            return rtnArray
        }

        fun getArrayOfRouteAscentTypes(context: Context): List<SpannableString> {
            val rtnArray = mutableListOf<SpannableString>()
            values().forEach {
                rtnArray.add(getRouteAscentStyle(context, it))
            }
            return rtnArray
        }

        private fun getRouteAscentStyle(
            mContext: Context,
            mRouteAscentType: RouteAscentType
        ): SpannableString {
            val ss = SpannableString("  ${mRouteAscentType.text()}")
            val d: Drawable? = mRouteAscentType.drawable(mContext)
            d?.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
            val span = d?.let { ImageSpan(it, ImageSpan.ALIGN_BASELINE) }
            ss.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            return ss
        }
    }

}
