package com.teufelsturm.tt_downloader_kotlin.results

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teufelsturm.tt_downloader_kotlin.data.entity.RouteComments
import com.teufelsturm.tt_downloader_kotlin.databinding.ListitemRouteBinding
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.DiffCallBacks
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.TTRouteClickListener
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.TTSummitClickListener

private const val TAG = "SummitDetailAdapter"

/**
 * Display the routes (aka 'Details') to a summit used for the recyclerview in
 * [com.teufelsturm.tt_downloader_kotlin.databinding.ResultSummitDetailBinding] fills [ListitemRouteBinding]
 */
class SummitDetailAdapter :
    ListAdapter<RouteComments.RouteWithMyRouteComment, SummitDetailAdapter.ViewHolder>(
        DiffCallBacks.RouteWithMyRouteCommentDiffCallback()
    ) {

    // taken from [SummitDetailAdapter]
    private var clickListener: TTRouteClickListener? = null

    fun setOnClickListener(onClickListener: TTRouteClickListener) {
        this.clickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class ViewHolder private constructor(val binding: ListitemRouteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: RouteComments.RouteWithMyRouteComment,
            clickListener: TTRouteClickListener?
        ) {
            binding.route = item
            binding.summit = null
            clickListener?.let { binding.clickListenerRoute = it }
            binding.clickListenerSummit = TTSummitClickListener{ }
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