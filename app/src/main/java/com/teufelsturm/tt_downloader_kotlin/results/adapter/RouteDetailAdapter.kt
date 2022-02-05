package com.teufelsturm.tt_downloader_kotlin.results.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teufelsturm.tt_downloader_kotlin.R
import com.teufelsturm.tt_downloader_kotlin.data.entity.CommentsWithRouteWithSummit
import com.teufelsturm.tt_downloader_kotlin.data.entity.RouteComments
import com.teufelsturm.tt_downloader_kotlin.databinding.ListitemCommentBinding
import com.teufelsturm.tt_downloader_kotlin.databinding.ListitemMyCommentAddBinding
import com.teufelsturm.tt_downloader_kotlin.databinding.ListitemMyCommentBinding
import com.teufelsturm.tt_downloader_kotlin.results.adapter.util.RouteCommentsClickListener

private const val ITEM_VIEW_TYPE_COMMENT = 0
private const val ITEM_VIEW_TYPE_MY_COMMENT = 1
private const val ITEM_VIEW_TYPE_ADD_COMMENT = 2

/**
 * Display the routes (aka 'Details') to a summit used for the recyclerview in
 * [com.teufelsturm.tt_downloader_kotlin.databinding.ResultRouteDetailBinding] fills [ListitemCommentBinding]
 */
class RouteDetailAdapter :
    ListAdapter<RouteComments,
            RecyclerView.ViewHolder>(TTCommentANDDiffCallback()) {

    private var clickListenerRouteComments: RouteCommentsClickListener? = null

    fun setOnClickListener(
        onClickListenerRoute: RouteCommentsClickListener
    ) {
        this.clickListenerRouteComments = onClickListenerRoute
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RouteComments.TTRouteCommentAND -> ITEM_VIEW_TYPE_COMMENT
            is RouteComments.MyTTRouteANDWithPhotos -> ITEM_VIEW_TYPE_MY_COMMENT
            is RouteComments.AddComment -> ITEM_VIEW_TYPE_ADD_COMMENT
            is RouteComments.RouteWithMyRouteComment -> TODO()
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
                val item = getItem(position) as RouteComments.TTRouteCommentAND
                holder.bind(item)
            }
            is MyCommentsListAdapterViewHolder -> {
                val item = getItem(position) as RouteComments.MyTTRouteANDWithPhotos
                holder.bind(item, clickListenerRouteComments)
            }
            is MyCommentsAddViewHolder -> {
                val item = getItem(position) as RouteComments.AddComment
                holder.bind(item, clickListenerRouteComments)
            }
        }
    }

    class RouteDetailAdapterViewHolder private constructor(val binding: ListitemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RouteComments.TTRouteCommentAND) {
            binding.comment = CommentsWithRouteWithSummit(item)
            binding.executePendingBindings()
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
    val binding: ListitemMyCommentBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: RouteComments.MyTTRouteANDWithPhotos,
        clickListenerRouteComments: RouteCommentsClickListener?
    ) {
        binding.myRouteWithPhotos = item
        binding.clickListenerComment = clickListenerRouteComments
        Log.e(TAG, "carousel.currentIndex: ${binding.carousel.currentIndex}")
        val commentImageCarouselAdapter = CommentImageCarouselAdapter()
        item.myTT_Route_PhotosANDList.forEach {
            commentImageCarouselAdapter.data.add(it)
        }
        binding.tv2.doAfterTextChanged {
            binding.tilImageCaption.animation?.cancel()
            binding.tilImageCaption.animate()
                .scaleY(0f)
                .alpha(0f)
                .setDuration(150L)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .withEndAction {
                    binding.tilImageCaption.text = it
                    binding.tilImageCaption.animate()
                        .scaleY(1f)
                        .alpha(1f)
                        .setDuration(150L)
                        .setInterpolator(AccelerateDecelerateInterpolator())
                }
                .start()
        }
        binding.carousel.setAdapter(commentImageCarouselAdapter)

        if (commentImageCarouselAdapter.count() == 0) {
            val scale: Float = binding.mlCommentPhotos.context.resources.displayMetrics.density;
            val pixels = (65.0 /*dps*/ * scale + 0.5f).toInt()
            binding.mlCommentPhotos.updateLayoutParams { height = pixels }
        } else {
            binding.mlCommentPhotos.updateLayoutParams { height = ViewGroup.LayoutParams.WRAP_CONTENT }
        }
        binding.executePendingBindings()
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
        item: RouteComments.AddComment,
        clickListenerRouteComments: RouteCommentsClickListener?
    ) {
        binding.routeComments = RouteComments.AddComment
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

class TTCommentANDDiffCallback : DiffUtil.ItemCallback<RouteComments>() {
    override fun areItemsTheSame(
        oldItem: RouteComments,
        newItem: RouteComments
    ): Boolean {
        return false // oldItem._id == newItem._id
    }

    override fun areContentsTheSame(
        oldItem: RouteComments,
        newItem: RouteComments
    ): Boolean {
        return oldItem == newItem
    }
}
