package com.teufelsturm.tt_downloader_kotlin.feature.searches.generics

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventNavigatingToSummit(val intTTSummitNr: Int) : Parcelable

@Parcelize
data class EventNavigatingToRoute(val intTTWegNr: Int?, val intTTSummitNr: Int) : Parcelable

@Parcelize
data class EventSearchSummitParameter(
    val minAnzahlWege: Int,
    val maxAnzahlWege: Int,
    val minAnzahlSternchenWege: Int,
    val maxAnzahlSternchenWege: Int,
    val searchAreas: String,
    val just_my_summit: Boolean,
    val searchText: String
) : Parcelable

@Parcelize
data class EventSearchRouteParameter(
    val partialRouteName: String,
    val area: String,
    val intMinGrade: Int,
    val intMaxGrade: Int,
    val minNumberOfComments: Int,
    val minOfMeanRating: Float,
    val justMyRoutes: Boolean
) : Parcelable

@Parcelize
data class EventSearchCommentParameter(
    val partialComment: String,
    val searchAreas: String,
    val intMinGrade: Int,
    val intMaxGrade: Int,
    val minRatingInComment: Int,
    val maxRatingInComment: Int

) : Parcelable
