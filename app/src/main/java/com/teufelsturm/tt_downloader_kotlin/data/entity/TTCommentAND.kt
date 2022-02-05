package com.teufelsturm.tt_downloader_kotlin.data.entity


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
) : CommentInterface, SummitBaseDataInterface {
    constructor(ttRouteCommentAND: RouteComments.TTRouteCommentAND) : this(
        ttRouteCommentAND._id,
        ttRouteCommentAND.intTTWegNr,
        ttRouteCommentAND.strEntryKommentar,
        ttRouteCommentAND.entryBewertung,
        ttRouteCommentAND.strEntryUser,
        ttRouteCommentAND.entryDatum,
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