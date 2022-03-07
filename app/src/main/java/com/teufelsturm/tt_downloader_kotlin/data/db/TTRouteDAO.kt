package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.teufelsturm.tt_downloader_kotlin.data.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TTRouteDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(summit: TTRouteAND)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(summit: TTRouteAND)

    @Query("DELETE FROM TT_Route_AND WHERE intTTWegNr = :intTTWegNr")
    fun deleteByTTWegNr(intTTWegNr: Int)

    @Query("SELECT * from TT_Route_AND WHERE intTTGipfelNr = :intTTGipfelNr")
    fun getBySummit(intTTGipfelNr: Int): LiveData<List<TTRouteAND>>

    @Query("SELECT * from TT_Route_AND WHERE intTTWegNr = :intTTWegNr")
    fun getByRouteId(intTTWegNr: Int): Flow<TTRouteAND>

    @Query("SELECT * from TT_Route_AND")
    fun getAll(): LiveData<List<TTRouteAND>>

    @Query(
        """SELECT DISTINCT weg FROM 
                            (SELECT a.WegName AS weg, b.strGebiet AS gebiet from TT_Summit_AND b, TT_Route_AND a where a.intTTGipfelNr = b.intTTGipfelNr)
                                where weg like :searchSummit and gebiet in 
                                (CASE WHEN length(:searchAreas) THEN (:searchAreas) ELSE (gebiet) END) ORDER BY weg LIMIT 30"""
    )
    fun getRouteNameForAutoText(searchSummit: String, searchAreas: String = ""): List<String>

    @Query(
        """SELECT MAX(a.intAnzahlDerKommentare) 
                    FROM   TT_Route_AND a
                    WHERE  a.fltMittlereWegBewertung >= :minOfMeanRating
							AND COALESCE (a.sachsenSchwierigkeitsGrad, a.ohneUnterstuetzungSchwierigkeitsGrad, a.rotPunktSchwierigkeitsGrad, a.intSprungSchwierigkeitsGrad) BETWEEN :intMinSchwierigkeit AND :intMaxSchwierigkeit
							AND a.intTTGipfelNr IN (SELECT b.intTTGipfelNr
						                FROM   TT_Summit_AND b
						                WHERE  b.strGebiet = (CASE WHEN LENGTH (:area) THEN (:area) ELSE (strGebiet) END))
                            AND a.wegName like :partialRouteName"""
    )
    fun getMaxAnzahlDerKommentare(
        partialRouteName: String,
        area: String,
        intMinSchwierigkeit: Int,
        intMaxSchwierigkeit: Int,
        minOfMeanRating: Float
    ): Int?

    @Query(
        """
        SELECT MAX (fltMittlereWegBewertung)
        FROM   TT_Route_AND a
        WHERE  a.intAnzahlDerKommentare >= :minNumberOfComments
              	AND COALESCE (a.sachsenSchwierigkeitsGrad, a.ohneUnterstuetzungSchwierigkeitsGrad, a.rotPunktSchwierigkeitsGrad, a.intSprungSchwierigkeitsGrad) BETWEEN :intMinSchwierigkeit AND :intMaxSchwierigkeit
				AND a.intTTGipfelNr IN (SELECT b.intTTGipfelNr
               FROM   TT_Summit_AND b
               WHERE  b.strGebiet = (CASE WHEN LENGTH (:area) THEN (:area) ELSE (strGebiet) END))
         AND a.wegName LIKE :partialRouteName"""
    )
    fun getMaxMeanRating(
        partialRouteName: String,
        area: String,
        intMinSchwierigkeit: Int,
        intMaxSchwierigkeit: Int,
        minNumberOfComments: Int
    ): Float?

    @Query(
        """
        SELECT 
       MIN (COALESCE ([a].[sachsenSchwierigkeitsGrad], [a].[ohneUnterstuetzungSchwierigkeitsGrad], [a].[rotPunktSchwierigkeitsGrad], [a].[intSprungSchwierigkeitsGrad])) as minGrade, 
       MAX (COALESCE ([a].[sachsenSchwierigkeitsGrad], [a].[ohneUnterstuetzungSchwierigkeitsGrad], [a].[rotPunktSchwierigkeitsGrad], [a].[intSprungSchwierigkeitsGrad])) as maxGrade
        FROM   [TT_Route_AND] [a]
        WHERE  [a].[fltMittlereWegBewertung] >= :minOfMeanRating
                 AND [a].[intAnzahlDerKommentare] >= :minNumberOfComments
                 AND [a].[intTTGipfelNr] IN (SELECT [b].[intTTGipfelNr]
               FROM   [TT_Summit_AND] [b]
               WHERE  [b].[strGebiet] = (CASE WHEN LENGTH (:area) THEN (:area) ELSE ([strGebiet]) END))
         AND [a].[wegName] LIKE :partialRouteName"""
    )
    suspend fun getConstrainedMinMaxGrade(
        partialRouteName: String,
        area: String,
        minNumberOfComments: Int,
        minOfMeanRating: Float
    ): GradeMinMax

    @Transaction
    @Query("SELECT * FROM TT_Route_AND")
    fun getRouteWithMySummitComment(): LiveData<List<Comments.RouteWithMyComment>>

    @Transaction
    @Query("SELECT * FROM TT_Route_AND WHERE intTTWegNr = :intTTWegNr")
    fun getRouteWithMySummitCommentByRoute(intTTWegNr: Int): Flow<Comments.RouteWithMyComment>

    @Transaction
    @Query("SELECT * FROM TT_Route_AND WHERE intTTGipfelNr = :intTTGipfelNr")
    fun getRouteWithMySummitCommentBySummit(intTTGipfelNr: Int): Flow<List<Comments.RouteWithMyComment>>

    @Transaction
    @Query("SELECT * FROM TT_Route_AND WHERE intTTWegNr = :intTTWegNr")
    fun getRouteWithMyCommentWithSummit(intTTWegNr: Int): LiveData<RouteWithMyCommentWithSummit>

    @Transaction
    @Query("SELECT * FROM TT_Route_AND WHERE intTTGipfelNr = :intTTGipfelNr")
    fun getRouteListWithMyCommentWithSummit(intTTGipfelNr: Int): LiveData<List<RouteWithMyCommentWithSummit>>

    @Transaction
    @Query(
        """SELECT COUNT(DISTINCT(a._id))
                    FROM   TT_Route_AND a
                    WHERE  a.fltMittlereWegBewertung >= :minOfMeanRating
							AND a.intAnzahlDerKommentare >= :minNumberOfComments
							AND COALESCE (a.sachsenSchwierigkeitsGrad, a.ohneUnterstuetzungSchwierigkeitsGrad, a.rotPunktSchwierigkeitsGrad, a.intSprungSchwierigkeitsGrad) BETWEEN :intMinSchwierigkeit AND :intMaxSchwierigkeit
							AND a.intTTGipfelNr IN (SELECT c.intTTGipfelNr
						                                 FROM TT_Summit_AND c
                                                    WHERE  c.strGebiet = (CASE WHEN LENGTH (:area) THEN (:area) ELSE (c.strGebiet) END))
                            AND a.wegName like :partialRouteName"""
    )
    suspend fun getConstrainedCount(
        partialRouteName: String,
        area: String,
        intMinSchwierigkeit: Int,
        intMaxSchwierigkeit: Int,
        minNumberOfComments: Int,
        minOfMeanRating: Float
    ): Int

    @Transaction
    @Query(
        """SELECT COUNT (DISTINCT (a._id))
                        FROM   TT_Route_AND a
                        WHERE  a.fltMittlereWegBewertung >= :minOfMeanRating
                                 AND a.intAnzahlDerKommentare >= :minNumberOfComments
                                 AND COALESCE (a.sachsenSchwierigkeitsGrad, a.ohneUnterstuetzungSchwierigkeitsGrad, a.rotPunktSchwierigkeitsGrad, a.intSprungSchwierigkeitsGrad) BETWEEN :intMinSchwierigkeit AND :intMaxSchwierigkeit
                                 AND a.intTTGipfelNr IN (SELECT c.intTTGipfelNr FROM   TT_Summit_AND c
                                                                WHERE  c.strGebiet = (CASE WHEN LENGTH (:area) THEN (:area) ELSE (c.strGebiet) END))
                                 AND a.intTTWegNr IN (SELECT DISTINCT (d.myIntTTWegNr) FROM   MyTT_Comment_AND d)
                                 AND a.wegName LIKE :partialRouteName;"""
    )
    suspend fun getConstrainedJustMineCount(
        partialRouteName: String,
        area: String,
        intMinSchwierigkeit: Int,
        intMaxSchwierigkeit: Int,
        minNumberOfComments: Int,
        minOfMeanRating: Float // ,
        // just_mine: Boolean
    ): Int

    @Transaction
    @Query(
        """SELECT 
                    DISTINCT(a._id), 
                    a._idTimeStamp,
                    a.intTTWegNr, 
                    a.intTTGipfelNr, 
                    a.WegName, 
                    a.blnAusrufeZeichen, 
                    a.intSterne, 
                    a.strSchwierigkeitsGrad, 
                    a.sachsenSchwierigkeitsGrad, 
                    a.ohneUnterstuetzungSchwierigkeitsGrad, 
                    a.rotPunktSchwierigkeitsGrad, 
                    a.intSprungSchwierigkeitsGrad, 
                    a.intAnzahlDerKommentare, 
                    a.fltMittlereWegBewertung
                    FROM   TT_Route_AND a
                    WHERE  a.fltMittlereWegBewertung >= :minOfMeanRating
							AND a.intAnzahlDerKommentare >= :minNumberOfComments
							AND COALESCE (a.sachsenSchwierigkeitsGrad, a.ohneUnterstuetzungSchwierigkeitsGrad, a.rotPunktSchwierigkeitsGrad, a.intSprungSchwierigkeitsGrad) BETWEEN :intMinSchwierigkeit AND :intMaxSchwierigkeit
							AND a.intTTGipfelNr IN (SELECT c.intTTGipfelNr
						                                 FROM   TT_Summit_AND c
						                                 WHERE  c.strGebiet = (CASE WHEN LENGTH (:area) THEN (:area) ELSE (strGebiet) END))  
                            AND a.wegName like :partialRouteName"""
    )
    fun loadRouteListWithMyCommentWithSummitConstrained(
        partialRouteName: String,
        area: String,
        intMinSchwierigkeit: Int,
        intMaxSchwierigkeit: Int,
        minNumberOfComments: Int,
        minOfMeanRating: Float
    ): Flow<List<RouteWithMyCommentWithSummit>>


    @Transaction
    @Query(
        """SELECT 
                    DISTINCT(a._id), 
                    a._idTimeStamp,
                    a.intTTWegNr, 
                    a.intTTGipfelNr, 
                    a.WegName, 
                    a.blnAusrufeZeichen, 
                    a.intSterne, 
                    a.strSchwierigkeitsGrad, 
                    a.sachsenSchwierigkeitsGrad, 
                    a.ohneUnterstuetzungSchwierigkeitsGrad, 
                    a.rotPunktSchwierigkeitsGrad, 
                    a.intSprungSchwierigkeitsGrad, 
                    a.intAnzahlDerKommentare, 
                    a.fltMittlereWegBewertung
                    FROM   TT_Route_AND a
                    WHERE  a.fltMittlereWegBewertung >= :minOfMeanRating
							AND a.intAnzahlDerKommentare >= :minNumberOfComments
							AND COALESCE (a.sachsenSchwierigkeitsGrad, a.ohneUnterstuetzungSchwierigkeitsGrad, a.rotPunktSchwierigkeitsGrad, a.intSprungSchwierigkeitsGrad) BETWEEN :intMinSchwierigkeit AND :intMaxSchwierigkeit
                    AND a.intTTGipfelNr IN (SELECT c.intTTGipfelNr FROM   TT_Summit_AND c WHERE  c.strGebiet = (CASE WHEN LENGTH (:area) THEN (:area) ELSE (strGebiet) END))
                    AND a.intTTWegNr IN (SELECT DISTINCT (d.myIntTTWegNr) FROM MyTT_Comment_AND d)
                            AND a.wegName like :partialRouteName"""
    )
    fun loadRouteListWithMyCommentWithSummitConstrainedJustMine(
        partialRouteName: String,
        area: String,
        intMinSchwierigkeit: Int,
        intMaxSchwierigkeit: Int,
        minNumberOfComments: Int,
        minOfMeanRating: Float
    ): Flow<List<RouteWithMyCommentWithSummit>>
}