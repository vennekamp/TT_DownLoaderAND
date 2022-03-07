package com.teufelsturm.tt_downloader_kotlin.feature.results.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teufelsturm.tt_downloader_kotlin.data.entity.Comments
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.CommentImageClickListener
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.DiffCallBacks
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.RouteCommentsClickListener
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.ViewHolders

private const val ITEM_VIEW_TYPE_COMMENT = 0
private const val ITEM_VIEW_TYPE_MY_COMMENT = 1
private const val ITEM_VIEW_TYPE_ADD_COMMENT = 2

private const val TAG = "RouteDetailAdapter"

/**
 * Display the routes (aka 'Details') to a summit used for the recyclerview in
 * [com.teufelsturm.tt_downloader_kotlin.databinding.ResultRouteDetailBinding] fills [ListitemCommentBinding]
 */
class RouteDetailAdapter :
    ListAdapter<Comments,
            RecyclerView.ViewHolder>(DiffCallBacks.TTCommentANDDiffCallback()) {

    private var clickListenerRouteComments: RouteCommentsClickListener? = null
    private var clickListenerRouteCommentImage: CommentImageClickListener? = null

    fun setOnClickListener(
        onClickListenerRoute: RouteCommentsClickListener,
        clickListenerRouteCommentImage: CommentImageClickListener
    ) {
        this.clickListenerRouteComments = onClickListenerRoute
        this.clickListenerRouteCommentImage = clickListenerRouteCommentImage
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Comments.TTCommentAND -> ITEM_VIEW_TYPE_COMMENT
            is Comments.MyTTCommentANDWithPhotos -> ITEM_VIEW_TYPE_MY_COMMENT
            is Comments.AddComment -> ITEM_VIEW_TYPE_ADD_COMMENT
            is Comments.RouteWithMyComment -> throw IllegalArgumentException("getItem($position) for CommentsRoute.RouteWithMyRouteComment not supported.")
            is Comments.RouteWithMyTTCommentANDWithPhotos -> throw IllegalArgumentException("getItem($position) for CommentsRoute.RouteWithMyTTCommentANDWithPhotos not supported.")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_COMMENT -> ViewHolders.RouteDetailAdapterViewHolder.from(parent)
            ITEM_VIEW_TYPE_MY_COMMENT -> ViewHolders.MyCommentsListAdapterViewHolder.from(parent)
            ITEM_VIEW_TYPE_ADD_COMMENT -> ViewHolders.MyCommentsAddViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolders.RouteDetailAdapterViewHolder -> {
                val item = getItem(position) as Comments.TTCommentAND
                holder.bind(item)
            }
            is ViewHolders.MyCommentsListAdapterViewHolder -> {
                val item = getItem(position) as Comments.MyTTCommentANDWithPhotos
                holder.bind(item, clickListenerRouteComments, clickListenerRouteCommentImage)
            }
            is ViewHolders.MyCommentsAddViewHolder -> {
                val item = getItem(position) as Comments.AddComment
                holder.bind(item, clickListenerRouteComments)
            }
        }
    }

}


