package com.teufelsturm.tt_downloader_kotlin.feature.inputs.vm

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTT_RoutePhotos_AND
import com.teufelsturm.tt_downloader_kotlin.data.entity.RouteComments
import com.teufelsturm.tt_downloader_kotlin.feature.inputs.adapter.CarouselViewAdapter
import com.teufelsturm.tt_downloader_kotlin.feature.inputs.util.CarouselAdapterData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CarouselViewAdapterViewModel @Inject constructor() : ViewModel() {

    val carouselAdapterData = CarouselAdapterData()

    private val _imagedClicked = MutableLiveData<Uri?>(null)
    val imagedClicked: LiveData<Uri?>
        get() = _imagedClicked

    private val _carouselImagesChange = MutableLiveData<Boolean>(false)
    val carouselImagesChange: LiveData<Boolean>
        get() = _carouselImagesChange

    private val _carouselImageDelete = MutableLiveData<CustomCarouselViewModel?>(null)
    val carouselImageDelete: LiveData<CustomCarouselViewModel?>
        get() = _carouselImageDelete

    private val _carouselImageShow = MutableLiveData<CustomCarouselViewModel?>(null)
    val carouselImageShow: LiveData<CustomCarouselViewModel?>
        get() = _carouselImageShow

    fun onMoveCurrentImageLeft() {
        if (carouselAdapterData.onMoveCurrentImage(_isToRight = false)) {
            _carouselImagesChange.value = true
        }
    }

    fun onMoveCurrentImageRight() {
        if (carouselAdapterData.onMoveCurrentImage(_isToRight = true)) {
            _carouselImagesChange.value = true
        }
    }

    fun onCarouselImagesChanged() {
        if (_carouselImagesChange.value == false) return
        _carouselImagesChange.value = false
    }

    fun onDeleteItem() {
        _carouselImageDelete.value = carouselAdapterData.getImageCaptionVM()
    }

    fun onItemDeleted() {
        if (_carouselImageDelete.value != null) {
            _carouselImageDelete.value = null
        }
    }

    fun onItemShow() {
        if (_carouselImageShow.value == carouselAdapterData.getImageCaptionVM()) return
        _carouselImageShow.value = carouselAdapterData.getImageCaptionVM()
    }

    fun onItemShown() {
        if (_carouselImageShow.value == null) return
        _carouselImageShow.value = null
    }

    fun onItemAdd() {
        if (_imagedClicked == Uri.parse(CarouselViewAdapter.ADD_IMAGE)) return
        _imagedClicked.value = Uri.parse(CarouselViewAdapter.ADD_IMAGE)
    }

    fun onImageClick(position: Int) {
        if (_imagedClicked.value == carouselAdapterData.getCarouselItemViewModels()[position].getImage()) return
        _imagedClicked.value = carouselAdapterData.getCarouselItemViewModels()[position].getImage()
    }

    fun onImageClickHandled() {
        if (_imagedClicked.value == null) return
        _imagedClicked.value = null
    }

    fun setMyTTRouteANDWithPhotos(myTTRouteANDWithPhotos: RouteComments.MyTTRouteANDWithPhotos) {
        carouselAdapterData.getCarouselItemViewModels().clear()
        myTTRouteANDWithPhotos.myTT_Route_PhotosANDList.forEach {
            carouselAdapterData.addCustomCarouselViewModel(CustomCarouselViewModel(it))
        }
        carouselAdapterData.addCustomCarouselViewModel(
            CustomCarouselViewModel(
                MyTT_RoutePhotos_AND(0, 0, CarouselViewAdapter.ADD_IMAGE, "Bild hinzufügen...")
            )
        )
    }
}