//package com.teufelsturm.tt_downloader_kotlin.feature.inputs.vm
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.teufelsturm.tt_downloader_kotlin.databinding.CarouselImageItemBinding
//
//class CarouselImageAdapter(private val imageList: MutableList<Int>) :
//    RecyclerView.Adapter<CarouselImageAdapter.CarouselViewHolder>() {
//
//    inner class CarouselViewHolder(private val binding: CarouselImageItemBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(image: Int) {
//            binding.carouselImageView.setImageResource(image)
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
//        return CarouselViewHolder(
//            CarouselImageItemBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        )
//    }
//
//    override fun getItemCount(): Int {
//        return imageList.size
//    }
//
//    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
//        holder.bind(imageList[position])
//    }
//}