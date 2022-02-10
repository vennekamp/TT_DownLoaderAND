package com.teufelsturm.tt_downloader_kotlin.feature.inputs.vm

import android.net.Uri
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.teufelsturm.tt_downloader_kotlin.BR
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTT_RoutePhotos_AND

private const val TAG = "CustomCarouselViewModel"

class CustomCarouselViewModel(private val myTT_routePhotos_AND: MyTT_RoutePhotos_AND) :
    BaseObservable() {

    fun getMyTT_RoutePhotos_AND(): MyTT_RoutePhotos_AND{
        return myTT_routePhotos_AND
    }

    fun getImage(): Uri? {
        return myTT_routePhotos_AND.uri?.let { Uri.parse(it) }
    }

    @Bindable
    fun getImageCaption(): String? {
        return this.myTT_routePhotos_AND.caption
    }

    fun setImageCaption(_imageCaption: String?) {
        if (myTT_routePhotos_AND.caption == _imageCaption) return
        myTT_routePhotos_AND.caption = _imageCaption
        Log.e(TAG, "image caption is now: ${myTT_routePhotos_AND.caption}")
        notifyPropertyChanged(BR.imageCaption)
    }
}