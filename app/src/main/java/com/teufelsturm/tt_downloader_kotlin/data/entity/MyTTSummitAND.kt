package com.teufelsturm.tt_downloader_kotlin.data.entity

import androidx.room.*
import com.teufelsturm.tt_downloader_kotlin.data.db.NO_ID

@Entity(tableName = "MyTT_Summit_AND")
class MyTTSummitAND(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var Id: Long = NO_ID,

    @ColumnInfo(name = "mySummitCommentTimStamp")
    var mySummitCommentTimStamp: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "myIntTTGipfelNr")
    var myIntTTGipfelNr: Int = 0,

    @ColumnInfo(name = "isAscendedSummit")
    var isAscendedSummit: Boolean? = null,

    @ColumnInfo(name = "myIntDateOfAscend")
    var myIntDateOfAscend: Long? = null,

    @ColumnInfo(name = "strMySummitComment")
    var strMySummitComment: String? = null
)

data class SummitWithMySummitComment(
    @Embedded val ttSummitAND: TTSummitAND,
    @Relation(
        parentColumn = "intTTGipfelNr",
        entityColumn = "myIntTTGipfelNr"
    )
    val myTTSummitANDList: List<MyTTSummitAND>
)