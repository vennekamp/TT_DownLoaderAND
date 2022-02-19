package com.teufelsturm.tt_downloader_kotlin.feature.results.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teufelsturm.tt_downloader_kotlin.data.entity.CommentsSummit
import com.teufelsturm.tt_downloader_kotlin.databinding.ListitemSummitBinding
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.DiffCallBacks
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.SummitClickListener

private const val TAG = "SummitsListAdapter"

/**
 * Display all summits used for the recyclerview in
 * [com.teufelsturm.tt_downloader_kotlin.databinding.ResultsSummitsListBinding] fills [ListitemSummitBinding]
 */
class SummitsListAdapter :
    ListAdapter<CommentsSummit.SummitWithMySummitComment, SummitsListAdapter.ViewHolder>(DiffCallBacks.TTSummitANDDiffCallback()) {

    private var clickListener: SummitClickListener? = null

    fun setOnClickListener(onClickListener: SummitClickListener) {
        this.clickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class ViewHolder private constructor(val binding: ListitemSummitBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CommentsSummit.SummitWithMySummitComment, clickListener: SummitClickListener?) {
            binding.summit = item.ttSummitAND
            binding.listMySummit = item.myTTSummitANDList
            clickListener?.let { binding.clickListener = it }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ListitemSummitBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}