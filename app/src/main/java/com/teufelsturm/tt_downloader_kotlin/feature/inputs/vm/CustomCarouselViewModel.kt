package com.teufelsturm.tt_downloader_kotlin.feature.inputs.vm

import android.net.Uri
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.teufelsturm.tt_downloader_kotlin.BR
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTT_RoutePhotos_AND
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "CustomCarouselViewModel"

class CustomCarouselViewModel(private val myTT_RoutePhotos_AND: MyTT_RoutePhotos_AND) :
    BaseObservable() {

    fun getMyTT_RoutePhotos_AND(): MyTT_RoutePhotos_AND{
        return myTT_RoutePhotos_AND
    }

    fun getImage(): Uri? {
        return myTT_RoutePhotos_AND.uri?.let { Uri.parse(it) }
    }

    fun setImage(_uri: Uri?) {
        myTT_RoutePhotos_AND.uri = _uri.toString()
    }

    @Bindable
    fun getImageCaption(): String? {
        return this.myTT_RoutePhotos_AND.caption
    }

    fun setImageCaption(_imageCaption: String?) {
        if (myTT_RoutePhotos_AND.caption == _imageCaption) return
        myTT_RoutePhotos_AND.caption = _imageCaption
        Log.e(TAG, "image caption is now: ${myTT_RoutePhotos_AND.caption}")
        notifyPropertyChanged(BR.imageCaption)
    }
}