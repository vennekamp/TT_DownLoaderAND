package com.teufelsturm.tt_downloader_kotlin.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "TT_Summit_AND",
    indices = [Index(value = ["intTTGipfelNr"], unique = true)]
)
data class TTSummitAND(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var Id: Long = 0L,

    @ColumnInfo(name = "_idTimeStamp")
    var idTimeStamp: Long = 0L,

    @ColumnInfo(name = "intTTGipfelNr")
    var intTTGipfelNr: Int = 0,

    @ColumnInfo(name = "strName")
    override var strName: String? = null,

    @ColumnInfo(name = "dblGPS_Latitude")
    var dblGPS_Latitude: Double? = null,

    @ColumnInfo(name = "dblGPS_Longitude")
    var dblGPS_Longitude: Double? = null,

    @ColumnInfo(name = "strGebiet")
    override var strGebiet: String? = null,

    @ColumnInfo(name = "intKleFuGipfelNr")
    override var intKleFuGipfelNr: Int? = null,

    @ColumnInfo(name = "intAnzahlWege")
    override var intAnzahlWege: Int? = null,

    @ColumnInfo(name = "intAnzahlSternchenWege")
    override var intAnzahlSternchenWege: Int? = null,

    @ColumnInfo(name = "strLeichtesterWeg")
    override var strLeichtesterWeg: String? = null,

    @ColumnInfo(name = "fltGPS_Altitude")
    var fltGPS_Altitude: Float? = null,

    @ColumnInfo(name = "osm_type")
    var osm_type: String? = null,

    @ColumnInfo(name = "osm_ID")
    var osm_ID: Int? = null,

    @ColumnInfo(name = "osm_display_name")
    var osm_display_name: String? = null,
    /*
    -- Main query that calculates the Bayesian summit average for a specific summit.
    See wikipedia: https://en.wikipedia.org/wiki/Bayesian_average.
    SELECT
           [stats].[intTTGipfelNr],                 -- Summit identifier (primary key or unique identifier for a summit)
           [stats].[num_routes],                    -- Number of routes associated with the summit
           [stats].[sum_routes_BayesianAverage],    -- Sum of Bayesian average ratings for all routes for the summit
           [stats].[avg_routes_BayesianAverage],    -- Average of Bayesian average ratings for all routes for the summit
           -- Calculation of Bayesian summit average using a weighted formula.
           -- The formula adjusts the summit's rating towards the global average, with 'm' controlling the weight of the global average.
           (([stats].[num_routes] * [stats].[avg_routes_BayesianAverage]) + ([global].[m] * [global].[global_avg]))
                     / ([stats].[num_routes] + [global].[m]) AS [bayesian_summit_average]
    FROM
          -- Subquery that calculates stats for the specific summit
          (SELECT
                   [tt_r].[intTTGipfelNr] AS [intTTGipfelNr],                                       -- Summit identifier
                   COUNT (*) AS [num_routes],                                                       -- Count of all routes for this summit
                   AVG ([tt_r].[fltBayesianAverageWegBewertung]) AS [avg_routes_BayesianAverage],   -- Average Bayesian rating for the routes of this summit
                   SUM ([tt_r].[fltBayesianAverageWegBewertung]) AS [sum_routes_BayesianAverage]    -- Sum of Bayesian ratings for the routes of this summit
            FROM   [TT_ROUTE_AND] [tt_r]  -- Source table containing route data
            WHERE  [tt_r].[intTTGipfelNr] = :intTTGipfelNr -- Filter to calculate stats for a single summit identified by the input parameter
          ) stats,
          -- Subquery that calculates global stats for the Bayesian average adjustment
          (SELECT
                   20 AS [m],  -- Constant 'm', which determines the weight of the global average in the Bayesian calculation
                   AVG ([tt_r].[fltBayesianAverageWegBewertung]) AS [global_avg]  -- Global average of Bayesian route ratings across all summits
            FROM   [TT_ROUTE_AND] [tt_r]  -- Source table containing route data
          ) global;
     */
    // Sum of Bayesian average ratings for all routes for the summit.
    @ColumnInfo(name = "sum_routes_BayesianAverage")
    var sum_routes_BayesianAverage: Float? = null,
    // Average of Bayesian average ratings for all routes for the summit
    @ColumnInfo(name = "avg_routes_BayesianAverage")
    var avg_routes_BayesianAverage: Float? = null,
    // Average of Bayesian average ratings for all routes for the summit
    @ColumnInfo(name = "bayesian_summit_average")
    var bayesian_summit_average: Float? = null,


    // Sum of Bayesian average ratings for all routes for the summit
    @ColumnInfo(name = "normalisierteWegBewertungSumme")
    var normalisierteWegBewertungSumme: Double? = null,
    // Average of Bayesian average ratings for all routes for the summit
    @ColumnInfo(name = "normalisierteWegBewertung")
    var normalisierteWegBewertung: Float? = null
) : SummitInterface

