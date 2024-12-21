package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.room.*
import com.teufelsturm.tt_downloader_kotlin.data.entity.Comments
import kotlinx.coroutines.flow.Flow
// @Dao annotation identifies this interface as a Data Access Object (DAO) for Room Database
@Dao
interface TTCommentDAO {

    // Inserts a new comment into the TT_Comment_AND table.
    // If a conflict occurs (e.g., duplicate primary key), the REPLACE strategy will overwrite the existing entry.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(comment: Comments.TTCommentAND)

    // Updates an existing comment in the TT_Comment_AND table.
    // If the comment does not exist, no update occurs.
    // The REPLACE strategy ensures that if there is a conflict, the old row is replaced with the new one.
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(comment: Comments.TTCommentAND)

    // Deletes a specific comment from TT_Comment_AND by its primary key (_id).
    // The method takes the ID of the comment as input and returns the number of rows affected.
    @Query("DELETE FROM TT_Comment_AND WHERE _id = :idNr")
    fun deleteById(idNr: Int)

    // Retrieves a specific comment from TT_Comment_AND by its primary key (_id).
    // Returns a single TTCommentAND object or null if the comment with the given ID is not found.
    @Query("SELECT * FROM TT_Comment_AND WHERE _id = :idNr")
    fun get(idNr: Int): Comments.TTCommentAND?

    // Retrieves all comments from the TT_Comment_AND table.
    // Returns a list of all TTCommentAND objects.
    @Query("SELECT * from TT_Comment_AND")
    fun getAll(): List<Comments.TTCommentAND>

    // Retrieves all comments for a specific route (intTTWegNr) from the TT_Comment_AND table.
    // Returns a Flow of a list of TTCommentAND objects, enabling live observation of data changes.
    @Query("SELECT * from TT_Comment_AND WHERE intTTWegNr = :intTTWegNr")
    fun getByRoute(intTTWegNr: Int): Flow<List<Comments.TTCommentAND>>

    // Retrieves a list of comments along with additional route and summit details.
    // Filters comments based on a range of entry ratings, a partial comment match, an area name,
    // and a range of difficulty levels for the routes.
    // Joins TT_Comment_AND with TT_Route_AND and TT_Summit_AND to fetch related data.
    // Limits the number of results to 500 entries.
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
                        BETWEEN :intMinSchwierigkeit AND :intMaxSchwierigkeit 
        LIMIT 500"""
    )
    fun getAllCommentsConstrained(
        minRatingInComment: Int,
        maxRatingInComment: Int,
        partialComment: String,
        area: String,
        intMinSchwierigkeit: Int,
        intMaxSchwierigkeit: Int
    ): Flow<List<Comments.CommentsWithRouteWithSummit>>

    // Counts the number of comments that meet the specified criteria.
    // This query counts how many comments match the given constraints,
    // including the rating range, area, difficulty range, and a partial comment match.
    // Uses Flow to allow live observation of the count.
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

    // Counts the number of comments that meet the specified criteria but excludes the partial comment filter.
    // Counts how many comments match the given constraints, including the rating range, area, and difficulty range.
    // Uses Flow to allow live observation of the count.
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

    // Counts the number of comments that meet the specified criteria but excludes the area and partial comment filters.
    // Counts how many comments match the given constraints, including the rating range and difficulty range.
    // Uses Flow to allow live observation of the count.
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
