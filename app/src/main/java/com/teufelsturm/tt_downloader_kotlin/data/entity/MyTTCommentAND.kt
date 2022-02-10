package com.teufelsturm.tt_downloader_kotlin.data.entity

import android.os.Parcelable
import androidx.room.*
import com.teufelsturm.tt_downloader_kotlin.data.db.NO_ID
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "MyTT_Comment_AND")
class MyTTCommentAND(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var Id: Long = 0L,

    @ColumnInfo(name = "myCommentTimStamp")
    var myCommentTimStamp: Long = System.currentTimeMillis(),

    // summit SHOULD also be entered for summit-comment and route-comment
    @ColumnInfo(name = "intTTGipfelNr")
    val intTTGipfelNr: Int,

    // route SHOULD also be for summit-comment 'null' and for route-comment the route number
    @ColumnInfo(name = "myIntTTWegNr")
    var myIntTTWegNr: Int? = null,

    @ColumnInfo(name = "myAscendedPartner")
    var myAscendedPartner: String? = null,

    @ColumnInfo(name = "isAscendedType")
    var isAscendedType: Int = 0,

    @ColumnInfo(name = "myIntDateOfAscend")
    var myIntDateOfAscend: String? = null,

    @ColumnInfo(name = "strMyComment")
    var strMyComment: String? = null
): Parcelable

@Parcelize
@Entity(tableName = "MyTT_RoutePhotos_AND")
class MyTT_RoutePhotos_AND(
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

data class RouteWithMyCommentWithSummit(
    @Embedded override val ttRouteAND: TTRouteAND,
    @Relation(
        parentColumn = "intTTGipfelNr",
        entityColumn = "intTTGipfelNr"
    )
    val ttSummitAND: TTSummitAND,
    @Relation(
        parentColumn = "intTTWegNr",
        entityColumn = "myIntTTWegNr"
    ) override val myTTCommentANDList: List<MyTTCommentAND>
) : RouteWithMyRouteCommentInterface
