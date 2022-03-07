package com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util

import androidx.recyclerview.widget.DiffUtil
import com.teufelsturm.tt_downloader_kotlin.data.entity.Comments
import com.teufelsturm.tt_downloader_kotlin.data.entity.CommentsSummit
import com.teufelsturm.tt_downloader_kotlin.data.entity.RouteWithMyCommentWithSummit

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

    class TTSummitANDDiffCallback : DiffUtil.ItemCallback<CommentsSummit.SummitWithMySummitComment>() {
        override fun areItemsTheSame(
            oldItem: CommentsSummit.SummitWithMySummitComment,
            newItem: CommentsSummit.SummitWithMySummitComment
        ): Boolean {
            return oldItem.ttSummitAND.intTTGipfelNr == newItem.ttSummitAND.intTTGipfelNr
        }

        override fun areContentsTheSame(
            oldItem: CommentsSummit.SummitWithMySummitComment,
            newItem: CommentsSummit.SummitWithMySummitComment
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


    class TTCommentANDDiffCallback : DiffUtil.ItemCallback<Comments>() {
        override fun areItemsTheSame(
            oldItem: Comments,
            newItem: Comments
        ): Boolean {
            return false // oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: Comments,
            newItem: Comments
        ): Boolean {
            return oldItem == newItem
        }
    }

    class RouteWithMyTTCommentANDWithPhotosDiffCallback : DiffUtil.ItemCallback<Comments.RouteWithMyTTCommentANDWithPhotos>() {
        override fun areItemsTheSame(
            oldItem: Comments.RouteWithMyTTCommentANDWithPhotos,
            newItem: Comments.RouteWithMyTTCommentANDWithPhotos
        ): Boolean {
            return false // oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: Comments.RouteWithMyTTCommentANDWithPhotos,
            newItem: Comments.RouteWithMyTTCommentANDWithPhotos
        ): Boolean {
            return oldItem == newItem
        }
    }

}
