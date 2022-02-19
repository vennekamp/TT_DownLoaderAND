package com.teufelsturm.tt_downloader_kotlin.feature.inputs.vm

import android.net.Uri
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.teufelsturm.tt_downloader_kotlin.BR
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTTCommentPhotosAND

private const val TAG = "CustomCarouselViewModel"

class CustomCarouselViewModel(private val myTT_commentPhotos_AND: MyTTCommentPhotosAND) :
    BaseObservable() {

    fun getMyTT_RoutePhotos_AND(): MyTTCommentPhotosAND{
        return myTT_commentPhotos_AND
    }

    fun getImage(): Uri? {
        return myTT_commentPhotos_AND.uri?.let { Uri.parse(it) }
    }

    @Bindable
    fun getImageCaption(): String? {
        return this.myTT_commentPhotos_AND.caption
    }

    fun setImageCaption(_imageCaption: String?) {
        if (myTT_commentPhotos_AND.caption == _imageCaption) return
        myTT_commentPhotos_AND.caption = _imageCaption
        Log.e(TAG, "image caption is now: ${myTT_commentPhotos_AND.caption}")
        notifyPropertyChanged(BR.imageCaption)
    }
}