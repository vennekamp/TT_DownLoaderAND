package com.teufelsturm.tt_downloader_kotlin.data.entity

import androidx.room.*
import com.teufelsturm.tt_downloader_kotlin.data.db.NO_ID

sealed class RouteComments {

    @Entity(tableName = "TT_Comment_AND")
    data class TTRouteCommentAND(
        //      RecNo	FieldName	            SQLType
        //      1	    _id	                    INTEGER
        //      2	    _idTimStamp             BIGINT
        //      3	    intTTWegNr	            INT
        //      4	    strEntryKommentar	    TEXT
        //      5	    entryBewertung	        INT
        //      6	    strEntryUser	        TEXT
        //      7	    entryDatum	            BIGINT
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "_id")
        override var _id: Long = NO_ID,

        @ColumnInfo(name = "_idTimStamp")
        var _idTimStamp: Long = 0,

        @ColumnInfo(name = "intTTWegNr")
        override var intTTWegNr: Int = 0,

        @ColumnInfo(name = "strEntryKommentar")
        override var strEntryKommentar: String? = null,

        @ColumnInfo(name = "entryBewertung")
        override var entryBewertung: Int? = null,

        @ColumnInfo(name = "strEntryUser")
        override var strEntryUser: String? = null,

        @ColumnInfo(name = "entryDatum")
        override var entryDatum: Long? = null
    ) : CommentInterface, RouteComments()

    data class MyTTRouteANDWithPhotos(
        @Embedded val myTTRouteAND: MyTTRouteAND,
        @Relation(
            parentColumn = "_id",
            entityColumn = "commentID"
        )
        val myTT_Route_PhotosANDList: MutableList<MyTT_RoutePhotos_AND> = mutableListOf()
    ) : RouteComments()

    data class RouteWithMyRouteComment(
        @Embedded override val ttRouteAND: TTRouteAND,
        @Relation(
            parentColumn = "intTTWegNr",
            entityColumn = "myIntTTWegNr"
        )
        override val myTTRouteANDList: List<MyTTRouteAND>
    ) : RouteWithMyRouteCommentInterface, RouteComments()

    object AddComment : RouteComments()
}
