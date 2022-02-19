package com.teufelsturm.tt_downloader_kotlin.feature.results.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teufelsturm.tt_downloader_kotlin.data.entity.Comments
import com.teufelsturm.tt_downloader_kotlin.data.entity.CommentsSummit
import com.teufelsturm.tt_downloader_kotlin.databinding.ListitemCommentBinding
import com.teufelsturm.tt_downloader_kotlin.databinding.ListitemMyCommentAddBinding
import com.teufelsturm.tt_downloader_kotlin.databinding.ListitemMyCommentBinding
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.CommentImageClickListener
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.RouteCommentsClickListener
import java.lang.IllegalArgumentException

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
            RecyclerView.ViewHolder>(TTCommentANDDiffCallback()) {

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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_COMMENT -> RouteDetailAdapterViewHolder.from(parent)
            ITEM_VIEW_TYPE_MY_COMMENT -> MyCommentsListAdapterViewHolder.from(parent)
            ITEM_VIEW_TYPE_ADD_COMMENT -> MyCommentsAddViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RouteDetailAdapterViewHolder -> {
                val item = getItem(position) as Comments.TTCommentAND
                holder.bind(item)
            }
            is MyCommentsListAdapterViewHolder -> {
                val item = getItem(position) as Comments.MyTTCommentANDWithPhotos
                holder.bind(item, clickListenerRouteComments, clickListenerRouteCommentImage)
            }
            is MyCommentsAddViewHolder -> {
                val item = getItem(position) as Comments.AddComment
                holder.bind(item, clickListenerRouteComments)
            }
        }
    }

    class RouteDetailAdapterViewHolder private constructor(private val mainBinding: ListitemCommentBinding) :
        RecyclerView.ViewHolder(mainBinding.root) {
        fun bind(item: Comments.TTCommentAND) {
            mainBinding.comment = CommentsSummit.CommentsWithRouteWithSummit(item)
            mainBinding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RouteDetailAdapterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ListitemCommentBinding.inflate(layoutInflater, parent, false)
                return RouteDetailAdapterViewHolder(binding)
            }
        }
    }
}

class MyCommentsListAdapterViewHolder private constructor(
    private val itemBinding: ListitemMyCommentBinding
) :
    RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(
        item: Comments.MyTTCommentANDWithPhotos,
        clickListenerRouteComments: RouteCommentsClickListener?,
        clickListenerRouteCommentImage: CommentImageClickListener?
    ) {
        itemBinding.myRouteWithPhotos = item
        itemBinding.clickListenerComment = clickListenerRouteComments
        itemBinding.clickListenerImage = clickListenerRouteCommentImage
        Log.e(TAG, "carousel.currentIndex: ${itemBinding.carousel.currentIndex}")
        val commentImageCarouselAdapter = CommentImageCarouselAdapter()
        item.myTT_comment_PhotosANDList.forEach {
            commentImageCarouselAdapter.data.add(it)
        }
        itemBinding.tv2.doAfterTextChanged {
            itemBinding.tilImageCaption.animation?.cancel()
            itemBinding.tilImageCaption.animate()
                .scaleY(0f)
                .alpha(0f)
                .setDuration(150L)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .withEndAction {
                    itemBinding.tilImageCaption.text = it
                    itemBinding.tilImageCaption.animate()
                        .scaleY(1f)
                        .alpha(1f)
                        .setDuration(150L).interpolator = AccelerateDecelerateInterpolator()
                }
                .start()
        }
        itemBinding.carousel.setAdapter(commentImageCarouselAdapter)

        if (commentImageCarouselAdapter.count() == 0) {
            val scale: Float = itemBinding.mlCommentPhotos.context.resources.displayMetrics.density
            val pixels = (65.0 /*dps*/ * scale + 0.5f).toInt()
            itemBinding.mlCommentPhotos.updateLayoutParams { height = pixels }
        } else {
            itemBinding.mlCommentPhotos.updateLayoutParams {
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
        }
        itemBinding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): MyCommentsListAdapterViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                ListitemMyCommentBinding.inflate(layoutInflater, parent, false)
            return MyCommentsListAdapterViewHolder(binding)
        }
    }
}

class MyCommentsAddViewHolder private constructor(val binding: ListitemMyCommentAddBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item:  Comments.AddComment,
        clickListenerRouteComments: RouteCommentsClickListener?
    ) {
        binding.routeComments = item // as static RouteComments.AddComment
        binding.clickListenerComment = clickListenerRouteComments
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): MyCommentsAddViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                ListitemMyCommentAddBinding.inflate(layoutInflater, parent, false)
            return MyCommentsAddViewHolder(binding)
        }
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
