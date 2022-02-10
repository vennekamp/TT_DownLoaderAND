package com.teufelsturm.tt_downloader_kotlin.results

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teufelsturm.tt_downloader_kotlin.data.entity.Comments
import com.teufelsturm.tt_downloader_kotlin.databinding.ListitemRouteBinding
import com.teufelsturm.tt_downloader_kotlin.data.entity.RouteWithMyCommentWithSummit
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.DiffCallBacks
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.TTRouteClickListener
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.TTSummitClickListener

private const val TAG = "RoutesListAdapter"

/**
 * Display the routes (aka 'Details') to a summit used for the recyclerview in
 * [com.teufelsturm.tt_downloader_kotlin.databinding.ResultsRoutesListBinding] fills [ListitemRouteBinding]
 */
class RoutesListAdapter :
    ListAdapter<RouteWithMyCommentWithSummit, RoutesListAdapter.ViewHolder>(DiffCallBacks.RouteWithMyCommentWithSummitDiffCallback()) {

    private var clickListenerRoute: TTRouteClickListener? = null
    private var clickListenerSummit: TTSummitClickListener? = null

    fun setOnClickListener(
        onClickListenerRoute: TTRouteClickListener,
        onClickListenerSummit: TTSummitClickListener
    ) {
        this.clickListenerRoute = onClickListenerRoute
        this.clickListenerSummit = onClickListenerSummit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListenerRoute, clickListenerSummit)
    }

    class ViewHolder private constructor(val binding: ListitemRouteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: RouteWithMyCommentWithSummit,
            onClickListenerRoute: TTRouteClickListener?,
            onClickListenerSummit: TTSummitClickListener?
        ) {
            binding.route = Comments.RouteWithMyComment(item.ttRouteAND, item.myTTCommentANDList)
            binding.summit = item.ttSummitAND
            onClickListenerRoute?.let { binding.clickListenerRoute = it }
            onClickListenerSummit?.let { binding.clickListenerSummit = it }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ListitemRouteBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}
