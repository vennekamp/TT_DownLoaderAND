package com.teufelsturm.tt_downloader_kotlin.feature.results.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teufelsturm.tt_downloader_kotlin.data.entity.Comments
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.*


private const val TAG = "SummitDetailAdp"

private const val ITEM_VIEW_TYPE_COMMENT = 0
private const val ITEM_VIEW_TYPE_MY_COMMENT = 1
private const val ITEM_VIEW_TYPE_ADD_COMMENT = 2
private const val ITEM_VIEW_TYPE_ROUTE_WITH_MY_COMMENT = 3
private const val ITEM_VIEW_TYPE_ROUTE_WITH_MY_COMMENT_AND_PHOTO = 4

/**
 * Display the routes (aka 'Details') to a summit used for the recyclerview in
 * [com.teufelsturm.tt_downloader_kotlin.databinding.ResultSummitDetailBinding] fills [ListitemRouteBinding]
 */
class SummitDetailAdapter : ListAdapter<Comments, RecyclerView.ViewHolder>(
    DiffCallBacks.TTCommentANDDiffCallback()
) {

    // taken from [SummitDetailAdapter]
    private var clickTTRouteListener: TTRouteClickListener? = null
    private var clickListenerRouteComments: RouteCommentsClickListener? = null
    private var clickListenerRouteCommentImage: CommentImageClickListener? = null

    fun setOnClickListener(
        onClickRouteListener: TTRouteClickListener,
        onClickListenerRouteComment: RouteCommentsClickListener,
        clickListenerRouteCommentImage: CommentImageClickListener
    ) {
        this.clickTTRouteListener = onClickRouteListener
        this.clickListenerRouteComments = onClickListenerRouteComment
        this.clickListenerRouteCommentImage = clickListenerRouteCommentImage
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Comments.TTCommentAND -> ITEM_VIEW_TYPE_COMMENT
            is Comments.MyTTCommentANDWithPhotos -> ITEM_VIEW_TYPE_MY_COMMENT
            is Comments.AddComment -> ITEM_VIEW_TYPE_ADD_COMMENT
            is Comments.RouteWithMyComment -> ITEM_VIEW_TYPE_ROUTE_WITH_MY_COMMENT
            is Comments.RouteWithMyTTCommentANDWithPhotos -> ITEM_VIEW_TYPE_ROUTE_WITH_MY_COMMENT_AND_PHOTO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.e(TAG,"onCreateViewHolder - viewType is: $viewType")
        return when (viewType) {
            ITEM_VIEW_TYPE_COMMENT -> throw IllegalArgumentException() //   RouteDetailAdapter.RouteDetailAdapterViewHolder.from(parent)
            ITEM_VIEW_TYPE_MY_COMMENT
                                -> ViewHolders.MyCommentsListAdapterViewHolder.from(parent)
            ITEM_VIEW_TYPE_ADD_COMMENT -> ViewHolders.MyCommentsAddViewHolder.from(parent)
            ITEM_VIEW_TYPE_ROUTE_WITH_MY_COMMENT
                                -> ViewHolders.RouteWithMyCommentViewHolder.from(parent)
            ITEM_VIEW_TYPE_ROUTE_WITH_MY_COMMENT_AND_PHOTO
                                -> ViewHolders.MyCommentsInSummitListAdapterViewHolder.from(parent)
            else -> throw java.lang.IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolders.RouteDetailAdapterViewHolder -> {
                throw java.lang.IllegalArgumentException()
            }
            is ViewHolders.MyCommentsListAdapterViewHolder -> {
                val item = getItem(position) as Comments.MyTTCommentANDWithPhotos
                holder.bind(item, clickListenerRouteComments, clickListenerRouteCommentImage)
            }
            is ViewHolders.MyCommentsInSummitListAdapterViewHolder -> {
                val item = getItem(position) as Comments.RouteWithMyTTCommentANDWithPhotos
                holder.bind(item.ttRouteAND, item.myTTCommentAND, clickListenerRouteComments, clickListenerRouteCommentImage)
            }
            is ViewHolders.MyCommentsAddViewHolder -> {
                val item = getItem(position) as Comments.AddComment
                holder.bind(item, clickListenerRouteComments)
            }
            is ViewHolders.RouteWithMyCommentViewHolder -> {
                val item = getItem(position) as Comments.RouteWithMyComment
                holder.bind(item, clickTTRouteListener)
            }
        }
    }
}