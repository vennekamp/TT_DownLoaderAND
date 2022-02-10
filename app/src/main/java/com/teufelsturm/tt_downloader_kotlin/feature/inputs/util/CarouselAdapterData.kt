package com.teufelsturm.tt_downloader_kotlin.feature.inputs.util

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.core.text.toSpannable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.teufelsturm.tt_downloader_kotlin.BR
import com.teufelsturm.tt_downloader_kotlin.feature.inputs.vm.CustomCarouselViewModel

private const val TAG = "CarouselAdapterData"

class CarouselAdapterData : BaseObservable() {

    val deletedCarouselItemViewModels = mutableListOf<CustomCarouselViewModel>()
    private val carouselItemViewModels = mutableListOf<CustomCarouselViewModel>()
    private var selectedCarouselVM: CustomCarouselViewModel? = null
    private var canEdit = true
    private var dialogMoveHelperText: Spannable? = SpannableString("??")

    fun getPosition() : Int {
        return  carouselItemViewModels.indexOf(selectedCarouselVM)
    }

    @Bindable
    fun getDialogMoveHelperText(): Spannable? {
        return dialogMoveHelperText
    }

    fun setDialogMoveHelperText(_helperText: Spannable) {
        dialogMoveHelperText = _helperText
        notifyPropertyChanged(BR.dialogMoveHelperText)
    }


    @Bindable
    fun getCanEdit(): Boolean {
        return canEdit
    }

    fun setCanEdit(_canEdit: Boolean) {
        canEdit = _canEdit
        notifyPropertyChanged(BR.canEdit)
    }

    @Bindable
    fun getImageCaptionVM(): CustomCarouselViewModel? {
        return this.selectedCarouselVM
    }

    private fun setImageCaptionVM(_imageCaption: CustomCarouselViewModel) {
        if (selectedCarouselVM == _imageCaption) return
        selectedCarouselVM = _imageCaption
        Log.v(TAG, "imageCaption is now: ${selectedCarouselVM!!.getImageCaption()}")
        setCanEdit(selectedCarouselVM != carouselItemViewModels.last())
        Log.v(TAG, "canEdit is now: $canEdit")
        notifyPropertyChanged(BR.imageCaptionVM)
    }

    fun onCarouselPageSelected(_position: Int) {
        setImageCaptionVM(carouselItemViewModels[_position])
        setHelperTextBuilder(_position)
    }

    fun getCarouselItemViewModels(): MutableList<CustomCarouselViewModel> {
        return carouselItemViewModels
    }

    fun onMoveCurrentImage(_isToRight: Boolean): Boolean {
        if (selectedCarouselVM == null) return false
        swapCarouselItemViewModels(_isToRight, getCarouselItemViewModels(), selectedCarouselVM)?.let {
            onCarouselPageSelected(it)
            return true
        }
        return false
    }

    fun <T> swapCarouselItemViewModels(
        _forward: Boolean,
        mutableList: MutableList<T>,
        selected: T?
    ): Int? {
        if (selected == null) return null
        val position = mutableList.indexOf(selected)
        val toPosition: Int = if (_forward) position.plus(1) else position.minus(1)
        if (position < 0 || toPosition > (mutableList.size - 2)) return null
        if (toPosition < 0 || position > (mutableList.size - 2)) return null
        val toValue = mutableList[position]
        val fromValue = mutableList[toPosition]
        Log.e(TAG, "moved element from $position to $toPosition")
        mutableList[position] = fromValue.also { mutableList[toPosition] = toValue }
        return toPosition
    }

    fun addCustomCarouselViewModel(
        customCarouselViewModel: CustomCarouselViewModel,
        insert: Boolean = false
    ) {
        if (!carouselItemViewModels.contains(customCarouselViewModel)) {
            if (insert && carouselItemViewModels.size > 0) {
                carouselItemViewModels.add(carouselItemViewModels.size - 1, customCarouselViewModel)
            } else {
                carouselItemViewModels.add(customCarouselViewModel)
            }
            Log.e(TAG, "CustomCarouselViewModel added ${customCarouselViewModel.getImage()} - ${customCarouselViewModel.getImageCaption()}")
        }
    }

    private fun setHelperTextBuilder(position: Int) {
        val str = SpannableStringBuilder()
        (1..getCarouselItemViewModels().size.minus(1)).forEach {
            val sIt = "$it "
            str.append(SpannableString(sIt))
            if (it.minus(1) == position) {
                str.setSpan(
                    ForegroundColorSpan(Color.RED),
                    str.length.minus(sIt.length),
                    str.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        setDialogMoveHelperText(str.toSpannable())
    }

}