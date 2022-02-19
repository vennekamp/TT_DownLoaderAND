package com.teufelsturm.tt_downloader_kotlin.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.teufelsturm.tt_downloader_kotlin.data.db.NO_ID
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.RouteGrade

@Entity(tableName = "TT_Route_AND")
data class TTRouteAND(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Long = NO_ID,

    @ColumnInfo(name = "_idTimeStamp")
    var idTimeStamp: Long = 0,

    @ColumnInfo(name = "intTTWegNr")
    var intTTWegNr: Int = 0,

    @ColumnInfo(name = "intTTGipfelNr")
    var intTTGipfelNr: Int = 0,

    @ColumnInfo(name = "WegName")
    var WegName: String? = null,

    @ColumnInfo(name = "blnAusrufeZeichen")
    var blnAusrufeZeichen: Boolean? = null,

    @ColumnInfo(name = "intSterne")
    var intSterne: Int? = null,

    @ColumnInfo(name = "strSchwierigkeitsGrad")
    var strSchwierigkeitsGrad: String? = null,

    @ColumnInfo(name = "sachsenSchwierigkeitsGrad")
    var sachsenSchwierigkeitsGrad: Int? = null,

    @ColumnInfo(name = "ohneUnterstuetzungSchwierigkeitsGrad")
    var ohneUnterstuetzungSchwierigkeitsGrad: Int? = null,

    @ColumnInfo(name = "rotPunktSchwierigkeitsGrad")
    var rotPunktSchwierigkeitsGrad: Int? = null,

    @ColumnInfo(name = "intSprungSchwierigkeitsGrad")
    var intSprungSchwierigkeitsGrad: Int? = null,

    @ColumnInfo(name = "intAnzahlDerKommentare")
    var intAnzahlDerKommentare: Int? = null,

    @ColumnInfo(name = "fltMittlereWegBewertung")
    var fltMittlereWegBewertung: Float? = null
)

data class GradeMinMax(val minGrade: Int?, val maxGrade: Int?) {
    fun asListOfOrdinal(): List<Float> {
        val _min =
            RouteGrade.getOrdinalByRouteGrade(minGrade)?.toFloat() ?: RouteGrade.getMinOrdinal()
        val _max =
            RouteGrade.getOrdinalByRouteGrade(maxGrade)?.toFloat() ?: RouteGrade.getMaxOrdinal()
        return listOf(_min.toFloat(), _max.toFloat())
    }
}