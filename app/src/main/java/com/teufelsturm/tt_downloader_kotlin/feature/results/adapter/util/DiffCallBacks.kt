package com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util

import androidx.recyclerview.widget.DiffUtil
import com.teufelsturm.tt_downloader_kotlin.data.entity.Comments
import com.teufelsturm.tt_downloader_kotlin.data.entity.RouteWithMyCommentWithSummit
import com.teufelsturm.tt_downloader_kotlin.data.entity.SummitWithMySummitComment

sealed class DiffCallBacks {

    class RouteWithMyRouteCommentDiffCallback :
        DiffUtil.ItemCallback<Comments.RouteWithMyComment>() {
        override fun areItemsTheSame(
            oldItem: Comments.RouteWithMyComment,
            newItem: Comments.RouteWithMyComment
        ): Boolean {
            return oldItem.ttRouteAND.intTTGipfelNr == newItem.ttRouteAND.intTTGipfelNr
        }

        override fun areContentsTheSame(
            oldItem: Comments.RouteWithMyComment,
            newItem: Comments.RouteWithMyComment
        ): Boolean {
            return oldItem == newItem
        }
    }

    class TTSummitANDDiffCallback : DiffUtil.ItemCallback<SummitWithMySummitComment>() {
        override fun areItemsTheSame(
            oldItem: SummitWithMySummitComment,
            newItem: SummitWithMySummitComment
        ): Boolean {
            return oldItem.ttSummitAND.intTTGipfelNr == newItem.ttSummitAND.intTTGipfelNr
        }

        override fun areContentsTheSame(
            oldItem: SummitWithMySummitComment,
            newItem: SummitWithMySummitComment
        ): Boolean {
            return oldItem == newItem
        }
    }

    class RouteWithMyCommentWithSummitDiffCallback :
        DiffUtil.ItemCallback<RouteWithMyCommentWithSummit>() {
        override fun areItemsTheSame(
            oldItem: RouteWithMyCommentWithSummit,
            newItem: RouteWithMyCommentWithSummit
        ): Boolean {
            return oldItem.ttRouteAND.intTTWegNr == newItem.ttRouteAND.intTTWegNr
        }

        override fun areContentsTheSame(
            oldItem: RouteWithMyCommentWithSummit,
            newItem: RouteWithMyCommentWithSummit
        ): Boolean {
            return oldItem == newItem
        }
    }
}
