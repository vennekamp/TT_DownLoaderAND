package com.teufelsturm.tt_downloader_kotlin.feature.inputs.util

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.teufelsturm.tt_downloader_kotlin.BR
import com.teufelsturm.tt_downloader_kotlin.data.entity.Comments
import com.teufelsturm.tt_downloader_kotlin.feature.searches.generics.appendOrReplaceTime
import com.teufelsturm.tt_downloader_kotlin.feature.searches.generics.convertDateTimeStringToLong
import com.teufelsturm.tt_downloader_kotlin.feature.searches.generics.convertLongToDateString
import com.teufelsturm.tt_downloader_kotlin.feature.searches.generics.convertLongToDateTimeString

private const val TAG = "AscentData"

class AscentCommentData : BaseObservable() {

    lateinit var myTTRouteANDWithPhotos: Comments.MyTTRouteANDWithPhotos
        private set

    fun setMyTTRouteANDWithPhotos (_myTTRouteANDWithPhotos: Comments.MyTTRouteANDWithPhotos) {
            if (!this::myTTRouteANDWithPhotos.isInitialized || myTTRouteANDWithPhotos != _myTTRouteANDWithPhotos) {
                myTTRouteANDWithPhotos = _myTTRouteANDWithPhotos
            }
        setAscentDate(myTTRouteANDWithPhotos.myTTCommentAND.myIntDateOfAscend)
        setAscentPartner(myTTRouteANDWithPhotos.myTTCommentAND.myAscendedPartner)
        setAscentComment(myTTRouteANDWithPhotos.myTTCommentAND.strMyComment)
        }


    private var ascentDate: String? = null
    private var ascentDateObject: Long? = null
    private var ascentPartner: String? = null
    private var ascentComment: String? = null

    fun getAscentDateObject(): Long? {
        return this.ascentDateObject
    }

    fun setAscentDateObject(_ascentDateInMillis: Long?) {
        if (_ascentDateInMillis == ascentDateObject) return
        if (_ascentDateInMillis == null) {
            ascentDate = null
            ascentDateObject = null
        } else {
            ascentDateObject = _ascentDateInMillis
            ascentDate = convertLongToDateString(_ascentDateInMillis)
        }
        Log.e(TAG, "ascent date is now: $ascentDate")
        myTTRouteANDWithPhotos.myTTCommentAND.myIntDateOfAscend = ascentDate
        notifyPropertyChanged(BR.ascentDate)
    }

    fun setAscentTime(hour: Int, minute: Int) {
        setAscentDate(appendOrReplaceTime(ascentDate, hour, minute))
    }

    @Bindable
    fun getAscentDate(): String? {
        return this.ascentDate
    }

    fun setAscentDate(_ascentDate: String?) {
        if (ascentDate == _ascentDate) return
        ascentDate = _ascentDate
        ascentDateObject = convertDateTimeStringToLong(_ascentDate)
        Log.e(
            TAG,
            "ascent date is now: $ascentDate, from ascentDateObject: ${
                ascentDateObject?.let {
                    convertLongToDateTimeString(it)
                }
            }")
        myTTRouteANDWithPhotos.myTTCommentAND.myIntDateOfAscend = _ascentDate
        notifyPropertyChanged(BR.ascentDate)
    }

    @Bindable
    fun getAscentPartner(): String? {
        return this.ascentPartner
    }

    fun setAscentPartner(_ascentPartner: String?) {
        if (ascentPartner == _ascentPartner) return
        ascentPartner = _ascentPartner
        Log.e(TAG, "ascent partner is now: $ascentPartner")
        myTTRouteANDWithPhotos.myTTCommentAND.myAscendedPartner = _ascentPartner
        notifyPropertyChanged(BR.ascentPartner)
    }

    @Bindable
    fun getAscentComment(): String? {
        return this.ascentComment
    }

    fun setAscentComment(_ascentComment: String?) {
        if (ascentComment == _ascentComment) return
        ascentComment = _ascentComment
        Log.e(TAG, "ascent comment is now: $ascentComment")
        myTTRouteANDWithPhotos.myTTCommentAND.strMyComment = _ascentComment
        notifyPropertyChanged(BR.ascentComment)
    }

}