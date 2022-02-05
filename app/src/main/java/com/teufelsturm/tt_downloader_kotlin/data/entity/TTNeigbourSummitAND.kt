package com.teufelsturm.tt_downloader_kotlin.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "TT_NeigbourSummit_AND")
class TTNeigbourSummitAND {
    //      RecNo	FieldName	            SQLType
    //      1	    _id	                    INTEGER
    //      2	    _idTimStamp             BIGINT
    //      3	    intTTHauptGipfelNr	    int
    //      4	    intTTNachbarGipfelNr	int
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var _id = 0

    @ColumnInfo(name = "_idTimStamp")
    var _idTimStamp: Long = 0

    @ColumnInfo(name = "intTTHauptGipfelNr")
    var intTTHauptGipfelNr = 0

    @ColumnInfo(name = "intTTNachbarGipfelNr")
    var intTTNachbarGipfelNr = 0
}

data class TTNeigbourANDTTName(
    val intTTNachbarGipfelNr: Int,
    val strName: String
)