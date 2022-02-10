package com.teufelsturm.tt_downloader_kotlin.data.entity

import androidx.room.*
import com.teufelsturm.tt_downloader_kotlin.data.db.NO_ID

sealed class Comments {

    @Entity(tableName = "TT_Comment_AND")
    data class TTCommentAND(
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
    ) : CommentInterface, Comments()

    data class MyTTRouteANDWithPhotos(
        @Embedded val myTTCommentAND: MyTTCommentAND,
        @Relation(
            parentColumn = "_id",
            entityColumn = "commentID"
        )
        val myTT_route_PhotosANDList: MutableList<MyTT_RoutePhotos_AND> = mutableListOf()
    ) : Comments()

    data class RouteWithMyComment(
        @Embedded override val ttRouteAND: TTRouteAND,
        @Relation(
            parentColumn = "intTTWegNr",
            entityColumn = "myIntTTWegNr"
        )
        override val myTTCommentANDList: List<MyTTCommentAND>
    ) : RouteWithMyRouteCommentInterface, Comments()

    object AddComment : Comments()
}
