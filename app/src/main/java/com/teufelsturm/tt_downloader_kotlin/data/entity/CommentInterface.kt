package com.teufelsturm.tt_downloader_kotlin.data.entity

interface CommentInterface {
    // TTCommentAND
    val _id: Long
    val intTTWegNr: Int
    val strEntryKommentar: String?
    val entryBewertung: Int?
    val strEntryUser: String?
    val entryDatum: Long?
}
