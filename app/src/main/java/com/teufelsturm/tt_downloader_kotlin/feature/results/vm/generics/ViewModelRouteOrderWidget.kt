package com.teufelsturm.tt_downloader_kotlin.feature.results.vm.generics

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.teufelsturm.tt_downloader_kotlin.data.order.Order
import com.teufelsturm.tt_downloader_kotlin.data.order.SortRouteWithMyRouteCommentBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val TAG = "ViewModelOrderWidget"

class ViewModelRouteOrderWidget : ViewModel() {

    val sortRoutesBy: MutableLiveData<SortRouteWithMyRouteCommentBy> =
        MutableLiveData(SortRouteWithMyRouteCommentBy.Name(Order.Ascending))

    private val _sortOrder = MutableLiveData<Boolean>(true)
    val sortOrder: LiveData<Boolean>
        get() = _sortOrder

    fun setCheckState(value: Boolean) {
        // Avoids infinite loops.
        if (_sortOrder.value == value) return
        _sortOrder.value = value
        sortRoutesBy.value = sortRoutesBy.value?.copy(convertBooleanToOrder(value))
        _futureVisibility.value = View.GONE
    }

    var order: LiveData<Order> =
        Transformations.map(sortOrder) { convertBooleanToOrder(it) }
    private val _futureVisibility = MutableLiveData<Int>(View.INVISIBLE)
    val futureVisibility: LiveData<Int>
        get() = _futureVisibility
    private val _visibility = MutableStateFlow<Int>(View.INVISIBLE)
    val visibility: StateFlow<Int>
        get() = _visibility

    fun setFutureVisibility(vis: Int) {
        if (_futureVisibility.value == vis) return
        _futureVisibility.value = vis
    }
    fun setVisibility(vis: Int) {
        if (_visibility.value == vis) return
        _visibility.value = vis
    }

    val radioButtons = listOf(
        ViewModelRadioButtonRwMRC(SortRouteWithMyRouteCommentBy.Name(order.value), true),
        ViewModelRadioButtonRwMRC(SortRouteWithMyRouteCommentBy.Stars(order.value), false),
        ViewModelRadioButtonRwMRC(SortRouteWithMyRouteCommentBy.MyAscend(order.value), false),
        ViewModelRadioButtonRwMRC(SortRouteWithMyRouteCommentBy.MeanRating(order.value), false),
        ViewModelRadioButtonRwMRC(SortRouteWithMyRouteCommentBy.CommentCount(order.value), false),
        ViewModelRadioButtonRwMRC(SortRouteWithMyRouteCommentBy.Grade(order.value), false),
        ViewModelRadioButtonRwMRC(SortRouteWithMyRouteCommentBy.Summit(order.value), false),
        ViewModelRadioButtonRwMRC(SortRouteWithMyRouteCommentBy.Area(order.value), false)
    )

    fun onClickName() = onClick(SortRouteWithMyRouteCommentBy.Name())
    fun onClickStars() = onClick(SortRouteWithMyRouteCommentBy.Stars())
    fun onClickMyAscend() = onClick(SortRouteWithMyRouteCommentBy.MyAscend())
    fun onClickMeanRating() = onClick(SortRouteWithMyRouteCommentBy.MeanRating())
    fun onClickCommentCount() = onClick(SortRouteWithMyRouteCommentBy.CommentCount())
    fun onClickGrade() = onClick(SortRouteWithMyRouteCommentBy.Grade())
    fun onClickSummit() = onClick(SortRouteWithMyRouteCommentBy.Summit())
    fun onClickArea() = onClick(SortRouteWithMyRouteCommentBy.Area())

    val isCheckedByName = getViewModelRadioButton(SortRouteWithMyRouteCommentBy.Name()).isChecked
    val isCheckedByStars = getViewModelRadioButton(SortRouteWithMyRouteCommentBy.Stars()).isChecked
    val isCheckedByMyAscend =
        getViewModelRadioButton(SortRouteWithMyRouteCommentBy.MyAscend()).isChecked
    val isCheckedByMeanRating =
        getViewModelRadioButton(SortRouteWithMyRouteCommentBy.MeanRating()).isChecked
    val isCheckedByCommentCount =
        getViewModelRadioButton(SortRouteWithMyRouteCommentBy.CommentCount()).isChecked
    val isCheckedByGrade = getViewModelRadioButton(SortRouteWithMyRouteCommentBy.Grade()).isChecked
    val isCheckedByArea = getViewModelRadioButton(SortRouteWithMyRouteCommentBy.Area()).isChecked
    val isCheckedBySummit = getViewModelRadioButton(SortRouteWithMyRouteCommentBy.Summit()).isChecked


    fun getViewModelRadioButton(_category: SortRouteWithMyRouteCommentBy): ViewModelRadioButtonRwMRC {
        return radioButtons.find {
            it.category::class == _category::class
        } ?: throw NullPointerException("Looking for $_category, but found nothing.")
    }

    fun startAnimation() {
        _futureVisibility.value =
            (if (_visibility.value != View.VISIBLE) View.VISIBLE else View.GONE)
    }

    fun onClick(_category: SortRouteWithMyRouteCommentBy) {
        // Is the button now checked?
        if (radioButtons.find {
                it.category::class == _category::class
            }?.isChecked?.value == true) return
        radioButtons.forEach { it.onClick(it.category::class == _category::class) }
        sortRoutesBy.value = radioButtons.find { it.isChecked.value ?: false }?.category
            ?: sortRoutesBy.value
        _futureVisibility.value = View.GONE
    }
}
