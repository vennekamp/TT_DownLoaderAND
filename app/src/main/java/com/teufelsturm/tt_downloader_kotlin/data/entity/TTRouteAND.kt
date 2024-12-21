package com.teufelsturm.tt_downloader_kotlin.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.teufelsturm.tt_downloader_kotlin.data.db.NO_ID
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.RouteGrade

@Entity(tableName = "TT_Route_AND")
data class TTRouteAND(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Long = NO_ID,

    @ColumnInfo(name = "_idTimeStamp")
    var idTimeStamp: Long = 0,

    @ColumnInfo(name = "intTTWegNr")
    var intTTWegNr: Int = 0,

    @ColumnInfo(name = "intTTGipfelNr")
    var intTTGipfelNr: Int = 0,

    @ColumnInfo(name = "WegName")
    var WegName: String? = null,

    @ColumnInfo(name = "blnAusrufeZeichen")
    var blnAusrufeZeichen: Boolean? = null,

    @ColumnInfo(name = "intSterne")
    var intSterne: Int? = null,

    @ColumnInfo(name = "strSchwierigkeitsGrad")
    var strSchwierigkeitsGrad: String? = null,

    @ColumnInfo(name = "sachsenSchwierigkeitsGrad")
    var sachsenSchwierigkeitsGrad: Int? = null,

    @ColumnInfo(name = "ohneUnterstuetzungSchwierigkeitsGrad")
    var ohneUnterstuetzungSchwierigkeitsGrad: Int? = null,

    @ColumnInfo(name = "rotPunktSchwierigkeitsGrad")
    var rotPunktSchwierigkeitsGrad: Int? = null,

    @ColumnInfo(name = "intSprungSchwierigkeitsGrad")
    var intSprungSchwierigkeitsGrad: Int? = null,

    @ColumnInfo(name = "intAnzahlDerKommentare")
    var intAnzahlDerKommentare: Int? = null,

    @ColumnInfo(name = "fltMittlereWegBewertung")
    var fltMittlereWegBewertung: Float? = null,
    /*
    *    -- Main query that calculates the Bayesian average rating for a specific route (intTTWegNr)
    *       See wikipedia: https://en.wikipedia.org/wiki/Bayesian_average.
    * Explanation of Key Concepts
    *
    * 1. Bayesian Average Calculation:
    *    - The Bayesian average is a "smoothed" version of the average route rating.
    *    - The purpose is to reduce the impact of routes with only a few ratings, which may be less reliable.
    *    - Formula:
    *      Bayesian Average = ((n * x̄) + (m * global_avg)) / (n + m)
    *
    *      Where:
    *      - n = number of comments (ratings) for the specific route
    *      - x̄ = local average rating for the specific route
    *      - m = a constant (set to 2.5 in this query) controlling the weight of the global average
    *      - global_avg = global average rating for all routes
    *
    * 2. 'stats' Subquery:
    *    - Filters the TT_Comment_AND table to only include comments for a specific route (intTTWegNr).
    *    - Counts the number of comments, computes the sum of all ratings, and calculates the average rating for the route.
    *
    * 3. 'global' Subquery:
    *    - Calculates the global average rating across all routes in the TT_Comment_AND table.
    *    - The constant m = 2.5 defines the weight of the global average in the Bayesian formula.
    *    - Smaller values of m give more weight to individual route ratings, while larger values of m favor the global average.
    *
    *    SELECT
    *        stats.intTTWegNr,  -- Route identifier (unique identifier for a specific route)
    *        stats.num_comments,  -- Total number of comments (ratings) associated with this route
    *        stats.avg_stars,  -- Average rating (number of stars) given in the comments for this route
    *        -- Bayesian average calculation:
    *        -- It adjusts the route's rating towards the global average, with 'm' acting as a regularization factor.
    *        -- The formula blends the local average rating with the global average rating.
    *        ((stats.num_comments * stats.avg_stars) + (global.m * global.global_avg))
    *        / (stats.num_comments + global.m) AS bayesian_average
    *    FROM
    *        -- Subquery to calculate the statistics for the specific route (intTTWegNr)
    *        (
    *            SELECT
    *                tt_c.intTTWegNr AS intTTWegNr,  -- Route identifier
    *                COUNT(*) AS num_comments,  -- Total number of comments (ratings) for this route
    *                AVG(tt_c.entryBewertung) AS avg_stars,  -- Average rating for this route
    *                SUM(tt_c.entryBewertung) AS total_stars  -- Sum of all star ratings for this route (not used in final query but could be useful for other calculations)
    *            FROM TT_Comment_AND tt_c  -- Table containing comments and ratings for all routes
    *            WHERE tt_c.intTTWegNr = :intTTWegNr  -- Filter to compute stats for a single route identified by the input parameter
    *        ) stats,
    *
    *        -- Subquery to calculate global statistics for all routes
    *        (
    *            SELECT
    *                2.5 AS m,  -- Constant 'm' that determines the weight of the global average in the Bayesian adjustment
    *                AVG(tt_c.entryBewertung) AS global_avg  -- Global average rating across all routes
    *           FROM TT_Comment_AND tt_c  -- Table containing comments and ratings for all routes
    *        ) global;
    */
    @ColumnInfo(name = "fltBayesianAverageWegBewertung")
    var fltBayesianAverageWegBewertung: Float? = null,
    @ColumnInfo(name = "normalisierteWegBewertung ")
    var normalisierteWegBewertung : Float? = null
)

data class GradeMinMax(val minGrade: Int?, val maxGrade: Int?) {
    fun asListOfOrdinal(): List<Float> {
        val _min =
            RouteGrade.getOrdinalByRouteGrade(minGrade)?.toFloat() ?: RouteGrade.getMinOrdinal()
        val _max =
            RouteGrade.getOrdinalByRouteGrade(maxGrade)?.toFloat() ?: RouteGrade.getMaxOrdinal()
        return listOf(_min.toFloat(), _max.toFloat())
    }
}