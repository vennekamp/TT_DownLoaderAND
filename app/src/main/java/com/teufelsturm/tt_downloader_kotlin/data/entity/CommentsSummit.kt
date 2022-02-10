package com.teufelsturm.tt_downloader_kotlin.data.entity

import androidx.room.Embedded
import androidx.room.Relation


sealed class CommentsSummit {

    data class CommentsWithRouteWithSummit(
        // TTCommentAND
        override val _id: Long,
        override val intTTWegNr: Int,
        override val strEntryKommentar: String?,
        override val entryBewertung: Int?,
        override val strEntryUser: String?,
        override val entryDatum: Long?,
        // route
        val WegName: String?,
        val strSchwierigkeitsGrad: String?,
        val blnAusrufeZeichen: Boolean?,
        var intSterne: Int?,
        // summit
        val intTTGipfelNr: Int?,
        override val strName: String?,
        override val intKleFuGipfelNr: Int?,
        override val strGebiet: String?
    ) : CommentInterface, SummitBaseDataInterface, CommentsSummit() {
        constructor(ttCommentANDRoute: Comments.TTCommentAND) : this(
            ttCommentANDRoute._id,
            ttCommentANDRoute.intTTWegNr,
            ttCommentANDRoute.strEntryKommentar,
            ttCommentANDRoute.entryBewertung,
            ttCommentANDRoute.strEntryUser,
            ttCommentANDRoute.entryDatum,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
    }

    data class MyTTRouteWithRouteWithSummit(
        // MyTTSummitAND --> Super
        @Embedded val myTTSummitAND: MyTTSummitAND,
        // route
        val WegName: String?,
        val strSchwierigkeitsGrad: String?,
        val blnAusrufeZeichen: Boolean?,
        var intSterne: Int?,
        // summit
        val intTTGipfelNr: Int?,
        val strName: String?,
        val intKleFuGipfelNr: Int?,
        val strGebiet: String?
    )

    data class MyTTSummitANDWithPhotos(
        @Embedded val myTTSummitAND: MyTTSummitAND,
        @Relation(
            parentColumn = "_id",
            entityColumn = "commentID"
        )
        val myTT_Summit_PhotosANDList: MutableList<MyTT_SummitPhotos_AND> = mutableListOf()
    ) : CommentsSummit()

    object AddComment : CommentsSummit()
}