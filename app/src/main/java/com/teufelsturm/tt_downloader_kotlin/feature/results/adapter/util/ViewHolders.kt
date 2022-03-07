package com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.teufelsturm.tt_downloader_kotlin.data.entity.Comments
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTRouteAND
import com.teufelsturm.tt_downloader_kotlin.databinding.*
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.CommentImageCarouselAdapter

private const val TAG = "ViewHolders"

sealed class ViewHolders(rootView: View) : RecyclerView.ViewHolder(rootView) {

    class RouteDetailAdapterViewHolder private constructor(private val mainBinding: ListitemCommentBinding) :
        ViewHolders(mainBinding.root) {
        fun bind(item: Comments.TTCommentAND) {
            mainBinding.comment = Comments.CommentsWithRouteWithSummit(item)
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

    class MyCommentsListAdapterViewHolder private constructor(
        private val itemBinding: ListitemMyCommentBinding
    ) :
        ViewHolders(itemBinding.root) {
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
                val scale: Float =
                    itemBinding.mlCommentPhotos.context.resources.displayMetrics.density
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
            item: Comments.AddComment,
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


    class MyCommentsInSummitListAdapterViewHolder private constructor(
        private val itemBinding: ListitemMyCommentInSummitBinding
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(
            ttRouteItem: TTRouteAND?,
            commentItem:  Comments.MyTTCommentANDWithPhotos,
            clickListenerRouteComments: RouteCommentsClickListener?,
            clickListenerRouteCommentImage: CommentImageClickListener?
        ) {
            itemBinding.ttroute = ttRouteItem
            itemBinding.myCommentWithPhotos = commentItem
            itemBinding.clickListenerComment = clickListenerRouteComments
            itemBinding.clickListenerImage = clickListenerRouteCommentImage
            Log.e(TAG, "carousel.currentIndex: ${itemBinding.carousel.currentIndex}")
            val commentImageCarouselAdapter = CommentImageCarouselAdapter()
            commentItem.myTT_comment_PhotosANDList.forEach {
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
                val scale: Float =
                    itemBinding.mlCommentPhotos.context.resources.displayMetrics.density
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
            fun from(parent: ViewGroup): MyCommentsInSummitListAdapterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ListitemMyCommentInSummitBinding.inflate(layoutInflater, parent, false)
                return MyCommentsInSummitListAdapterViewHolder(binding)
            }
        }
    }


    class RouteWithMyCommentViewHolder private constructor(val binding: ListitemRouteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Comments.RouteWithMyComment,
            clickListener: TTRouteClickListener?
        ) {
            binding.route = item
            binding.summit = null
            clickListener?.let { binding.clickListenerRoute = it }
            binding.clickListenerSummit = TTSummitClickListener { }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RouteWithMyCommentViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ListitemRouteBinding.inflate(layoutInflater, parent, false)
                return RouteWithMyCommentViewHolder(binding)
            }
        }
    }

}