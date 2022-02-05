package com.teufelsturm.tt_downloader_kotlin.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "TT_Summit_AND",
    indices = [Index(value = ["intTTGipfelNr"], unique = true)]
)
data class TTSummitAND(

    //      RecNo	FieldName	                SQLType
    //      1	    _id	                        INTEGER
    //      2	    _idTimStamp                 BIGINT
    //      3	    intTTGipfelNr               INT
    //      4	    strName	                    VARCHAR
    //      5	    strGebiet	                VARCHAR
    //      6	    intKleFuGipfelNr	        INT
    //      7	    intAnzahlWege	            INT
    //      8	    intAnzahlSternchenWege	    INT
    //      9	    strLeichtesterWeg	        VARCHAR
    //      10	    dblGPS_Latitude	            DOUBLE
    //      11	    dblGPS_Longitude	        DOUBLE
    //      12	    fltGPS_Altitude	            FLOAT
    //      13	    osm_type	                VARCHAR
    //      14	    osm_ID	                    INT
    //      15	    osm_display_name	        TEXT

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