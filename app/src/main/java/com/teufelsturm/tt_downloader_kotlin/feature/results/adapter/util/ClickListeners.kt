package com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util

import android.view.View
import com.teufelsturm.tt_downloader_kotlin.data.db.*
import com.teufelsturm.tt_downloader_kotlin.data.entity.*
import com.teufelsturm.tt_downloader_kotlin.databinding.*
import com.teufelsturm.tt_downloader_kotlin.feature.results.ui.RoutesListResultFragment

/**
 *  for [ResultsSummitsListBinding], [ListitemSummitBinding]
 *  - IMPORTANT: contains [SummitWithMySummitComment]
 */
class SummitClickListener(val clickListener: (summitID: Int) -> Unit) {
    fun onClick(item: TTSummitAND) = clickListener(item.intTTGipfelNr)
}

/**
 *  for [ResultSummitDetailBinding], [ListitemRouteBinding]
 *  - IMPORTANT: contains [RouteWithMyRouteComment] and [TTSummitAND] from
 *  ViewModel [RoutesListResultFragment] Class
 */
class TTSummitClickListener(val clickListener: (itemID: Int) -> Unit) {
    fun onClick(item: TTSummitAND) = clickListener(item.intTTGipfelNr)
}

/**
 *  for [ResultsRoutesListBinding], [ListitemRouteBinding]
 * - IMPORTANT: contains [RouteWithMyRouteComment] and [TTSummitAND] in XML File
 */
class TTRouteClickListener(val clickListener: (routeNumber: Int, summitNumber: Int?) -> Unit) {
    fun onClick(route: Comments.RouteWithMyComment, summit: TTSummitAND?) =
        clickListener(route.ttRouteAND.intTTWegNr, summit?.intTTGipfelNr)
}

/**
 *  for [ResultsRoutesListBinding], [ListitemCommentBinding]
 * - contains [RouteWithMyRouteComment] and [TTSummitAND] in XML File
 */
class TTCommentClickListener(val clickListener: (CommentsSummit.CommentsWithRouteWithSummit) -> Unit) {
    fun onClick(ttCommentAND: CommentsSummit.CommentsWithRouteWithSummit) =
        clickListener(ttCommentAND)
}

/**
 *  for [ResultsRoutesListBinding], [ListitemCommentBinding]
 * - contains [RouteWithMyRouteComment] and [TTSummitAND] in XML File
 */
class RouteCommentsClickListener(val clickListener: (Comments) -> Unit) {
    fun onClick(comment: Comments) = clickListener(comment)
}

class CommentImageClickListener(val clickListener: (View) -> Unit) {
    fun onClick(view: View) = clickListener(view)
}

class RouteAscentTypeOnItemSelected(val clickListener: (itemID: Int) -> Unit) {
    fun onClick(itemID: Int) = clickListener(itemID)
}