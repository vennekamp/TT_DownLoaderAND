package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.room.*
import com.teufelsturm.tt_downloader_kotlin.data.entity.Comments
import com.teufelsturm.tt_downloader_kotlin.data.entity.CommentsSummit
import kotlinx.coroutines.flow.Flow

@Dao
interface TTCommentDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(comment: Comments.TTCommentAND)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(comment: Comments.TTCommentAND)

    @Query("DELETE FROM TT_Comment_AND WHERE _id = :idNr")
    fun deleteById(idNr: Int)

    @Query("SELECT * FROM TT_Comment_AND WHERE _id = :idNr")
    fun get(idNr: Int): Comments.TTCommentAND?

    @Query("SELECT * from TT_Comment_AND")
    fun getAll(): List<Comments.TTCommentAND>

    @Query("SELECT * from TT_Comment_AND WHERE intTTWegNr = :intTTWegNr")
    fun getByRoute(intTTWegNr: Int): Flow<List<Comments.TTCommentAND>>

    @Query(
        """SELECT 
        a._id,
        a.intTTWegNr, 
        a.strEntryKommentar, 
        a.entryBewertung, 
        a.strEntryUser, 
        a.entryDatum, 
        b.WegName, 
        b.strSchwierigkeitsGrad,
        b.blnAusrufeZeichen,
        b.intSterne,  
        c.intTTGipfelNr,  
        c.strName,
        c.intKleFuGipfelNr,
        c.strGebiet 
        FROM   TT_Summit_AND c,
               TT_Route_AND b,
               TT_Comment_AND a
        WHERE  a.entryBewertung BETWEEN :minRatingInComment AND :maxRatingInComment
                 AND a.strEntryKommentar LIKE :partialComment
                 AND a.intTTWegNr = b.intTTWegNr
                 AND c.strGebiet = (CASE WHEN length(:area) THEN (:area) ELSE (strGebiet) END)
                 AND c.intTTGipfelNr = b.intTTGipfelNr
                 AND COALESCE (b.sachsenSchwierigkeitsGrad, b.ohneUnterstuetzungSchwierigkeitsGrad, b.rotPunktSchwierigkeitsGrad, b.intSprungSchwierigkeitsGrad)
                        BETWEEN :intMinSchwierigkeit AND :intMaxSchwierigkeit LIMIT 500"""
    )
    fun getAllCommentsConstrained(
        minRatingInComment: Int,
        maxRatingInComment: Int,
        partialComment: String,
        area: String,
        intMinSchwierigkeit: Int,
        intMaxSchwierigkeit: Int
    ): Flow<List<Comments.CommentsWithRouteWithSummit>>

    @Query(
        """SELECT 
        COUNT(a._id)
        FROM   TT_Summit_AND c,
               TT_Route_AND b,
               TT_Comment_AND a
        WHERE  a.entryBewertung BETWEEN :minRatingInComment AND :maxRatingInComment
                 AND a.intTTWegNr = b.intTTWegNr
                 AND c.intTTGipfelNr = b.intTTGipfelNr
                 AND c.strGebiet = (CASE WHEN length(:area) THEN (:area) ELSE (strGebiet) END)
                 AND COALESCE (b.sachsenSchwierigkeitsGrad, b.ohneUnterstuetzungSchwierigkeitsGrad, b.rotPunktSchwierigkeitsGrad, b.intSprungSchwierigkeitsGrad)
                        BETWEEN :intMinSchwierigkeit AND :intMaxSchwierigkeit
                 AND a.strEntryKommentar LIKE :partialComment"""
    )
    fun getCommentsConstrainedCount(
        minRatingInComment: Int,
        maxRatingInComment: Int,
        partialComment: String,
        area: String,
        intMinSchwierigkeit: Int,
        intMaxSchwierigkeit: Int
    ): Flow<Int>

    @Query(
        """SELECT 
        COUNT(a._id)
        FROM   TT_Summit_AND c,
               TT_Route_AND b,
               TT_Comment_AND a
        WHERE  a.entryBewertung BETWEEN :minRatingInComment AND :maxRatingInComment
                 AND a.intTTWegNr = b.intTTWegNr
                 AND c.intTTGipfelNr = b.intTTGipfelNr
                 AND c.strGebiet = (CASE WHEN length(:area) THEN (:area) ELSE (strGebiet) END)
                 AND COALESCE (b.sachsenSchwierigkeitsGrad, b.ohneUnterstuetzungSchwierigkeitsGrad, b.rotPunktSchwierigkeitsGrad, b.intSprungSchwierigkeitsGrad)
                        BETWEEN :intMinSchwierigkeit AND :intMaxSchwierigkeit"""
    )
    fun getCommentsConstrainedCount(
        minRatingInComment: Int,
        maxRatingInComment: Int,
        area: String,
        intMinSchwierigkeit: Int,
        intMaxSchwierigkeit: Int
    ): Flow<Int>

    @Query(
        """SELECT 
        COUNT(a._id)
        FROM   TT_Summit_AND c,
               TT_Route_AND b,
               TT_Comment_AND a
        WHERE  a.entryBewertung BETWEEN :minRatingInComment AND :maxRatingInComment
                 AND a.intTTWegNr = b.intTTWegNr
                 AND c.intTTGipfelNr = b.intTTGipfelNr
                 AND COALESCE (b.sachsenSchwierigkeitsGrad, b.ohneUnterstuetzungSchwierigkeitsGrad, b.rotPunktSchwierigkeitsGrad, b.intSprungSchwierigkeitsGrad)
                        BETWEEN :intMinSchwierigkeit AND :intMaxSchwierigkeit"""
    )
    fun getCommentsConstrainedCount(
        minRatingInComment: Int,
        maxRatingInComment: Int,
        intMinSchwierigkeit: Int,
        intMaxSchwierigkeit: Int
    ): Flow<Int>

}