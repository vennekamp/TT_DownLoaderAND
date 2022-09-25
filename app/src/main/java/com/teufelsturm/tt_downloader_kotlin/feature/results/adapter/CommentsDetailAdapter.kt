package com.teufelsturm.tt_downloader_kotlin.feature.results.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teufelsturm.tt_downloader_kotlin.data.entity.Comments
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.TTCommentClickListener
import de.teufelsturm.tt_downloader_ktx.databinding.ListitemCommentBinding

private const val ITEM_VIEW_TYPE_COMMENT = 0
private const val ITEM_VIEW_TYPE_MY_COMMENT = 1
private const val ITEM_VIEW_TYPE_MY_ROUTE_COMMENT = 2
private const val ITEM_VIEW_TYPE_ADD_COMMENT = 3

/**
 * Display the routes (aka 'Details') to a summit used for the recyclerview in
 * [com.teufelsturm.tt_downloader_kotlin.databinding.ResultsCommentsListBinding] fills [ListitemCommentBinding]
 */
class CommentsListdapter :
    ListAdapter<Comments.CommentsWithRouteWithSummit, CommentsListdapter.ViewHolder>(
        CommentsWithRouteWithSummitDiffCallback()
    ) {

    private var clickListenerRoute: TTCommentClickListener? = null
    private var clickListenerSummit: TTCommentClickListener? = null

    fun setOnClickListener(
        onClickListenerRoute: TTCommentClickListener,
        onClickListenerSummit: TTCommentClickListener
    ) {
        this.clickListenerRoute = onClickListenerRoute
        this.clickListenerSummit = onClickListenerSummit
    }
/*
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CommentsSummit.TTSummitAND -> TODO()
            is CommentsSummit.SummitWithMySummitComment -> TODO()
            is CommentsSummit.MyTTSummitANDWithPhotos -> TODO()
            CommentsSummit.AddComment -> TODO()
        }
    }
*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListenerRoute, clickListenerSummit)
    }

    class ViewHolder private constructor(val binding: ListitemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Comments.CommentsWithRouteWithSummit,
            onClickListenerRoute: TTCommentClickListener?,
            onClickListenerSummit: TTCommentClickListener?
        ) {
            binding.comment = item
            onClickListenerRoute?.let { binding.clickListenerRoute = it }
            onClickListenerSummit?.let { binding.clickListenerSummit = it }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ListitemCommentBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class CommentsWithRouteWithSummitDiffCallback :
    DiffUtil.ItemCallback<Comments.CommentsWithRouteWithSummit>() {
    override fun areItemsTheSame(
        oldItem: Comments.CommentsWithRouteWithSummit,
        newItem: Comments.CommentsWithRouteWithSummit
    ): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(
        oldItem: Comments.CommentsWithRouteWithSummit,
        newItem: Comments.CommentsWithRouteWithSummit
    ): Boolean {
        return oldItem == newItem
    }
}
