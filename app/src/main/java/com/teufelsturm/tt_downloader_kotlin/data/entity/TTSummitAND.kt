package com.teufelsturm.tt_downloader_kotlin.data.entity

import android.os.Parcelable
import androidx.room.*
import com.teufelsturm.tt_downloader_kotlin.data.db.NO_ID
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "TT_Summit_AND",
    indices = [Index(value = ["intTTGipfelNr"], unique = true)]
)
data class TTSummitAND(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var Id: Long = 0L,

    @ColumnInfo(name = "_idTimeStamp")
    var idTimeStamp: Long = 0L,

    @ColumnInfo(name = "intTTGipfelNr")
    var intTTGipfelNr: Int = 0,

    @ColumnInfo(name = "strName")
    override var strName: String? = null,

    @ColumnInfo(name = "dblGPS_Latitude")
    var dblGPS_Latitude: Double? = null,

    @ColumnInfo(name = "dblGPS_Longitude")
    var dblGPS_Longitude: Double? = null,

    @ColumnInfo(name = "strGebiet")
    override var strGebiet: String? = null,

    @ColumnInfo(name = "intKleFuGipfelNr")
    override var intKleFuGipfelNr: Int? = null,

    @ColumnInfo(name = "intAnzahlWege")
    override var intAnzahlWege: Int? = null,

    @ColumnInfo(name = "intAnzahlSternchenWege")
    override var intAnzahlSternchenWege: Int? = null,

    @ColumnInfo(name = "strLeichtesterWeg")
    override var strLeichtesterWeg: String? = null,

    @ColumnInfo(name = "fltGPS_Altitude")
    var fltGPS_Altitude: Float? = null,

    @ColumnInfo(name = "osm_type")
    var osm_type: String? = null,

    @ColumnInfo(name = "osm_ID")
    var osm_ID: Int? = null,

    @ColumnInfo(name = "osm_display_name")
    var osm_display_name: String? = null
) : SummitInterface


@Parcelize
@Entity(tableName = "MyTT_SummitPhotos_AND")
class MyTT_SummitPhotos_AND(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var Id: Long = NO_ID,

    @ColumnInfo(name = "commentID")
    var commentID: Long = 0L,

    @ColumnInfo(name = "uri")
    var uri: String? = null,

    @ColumnInfo(name = "caption")
    var caption: String? = null
) : Parcelable

