package com.teufelsturm.tt_downloader_kotlin.data.entity

import android.os.Parcelable
import androidx.room.*
import com.teufelsturm.tt_downloader_kotlin.data.db.NO_ID
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "MyTT_Route_AND")
class MyTTRouteAND(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var Id: Long = 0L,

    @ColumnInfo(name = "myRouteCommentTimStamp")
    var myRouteCommentTimStamp: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "myIntTTWegNr")
    var myIntTTWegNr: Int,

    @ColumnInfo(name = "isAscendedRouteType")
    var isAscendedRouteType: Int? = 0,

    @ColumnInfo(name = "myAscendedPartner")
    var myAscendedPartner: String? = null,

    @ColumnInfo(name = "myIntDateOfAscendRoute")
    var myIntDateOfAscendRoute: String? = null,

    @ColumnInfo(name = "strMyRouteComment")
    var strMyRouteComment: String? = null
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
    )
    override val myTTRouteANDList: List<MyTTRouteAND>
) : RouteWithMyRouteCommentInterface
