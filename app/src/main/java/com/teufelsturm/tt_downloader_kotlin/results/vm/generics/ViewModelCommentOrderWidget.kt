package com.teufelsturm.tt_downloader_kotlin.results.vm.generics

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.teufelsturm.tt_downloader_kotlin.data.order.Order
import com.teufelsturm.tt_downloader_kotlin.data.order.SortCommentsWithRouteWithSummitBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val TAG = "ViewModelOrderWidget"

class ViewModelCommentOrderWidget : ViewModel() {

    private val _sortOrder = MutableLiveData<Boolean>(true)
    val sortOrder: LiveData<Boolean>
        get() = _sortOrder

    fun setCheckState(value: Boolean) {
        // Avoids infinite loops.
        if (_sortOrder.value == value) return
        _sortOrder.value = value
        sortCommentsBy.value = sortCommentsBy.value?.copy(convertBooleanToOrder(value))
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

    fun setVisibility(vis: Int) {
        if (_visibility.value == vis) return
        _visibility.value = vis
    }


    val sortCommentsBy: MutableLiveData<SortCommentsWithRouteWithSummitBy> =
        MutableLiveData(SortCommentsWithRouteWithSummitBy.WegName(Order.Ascending))
    val radioButtons = listOf(
        ViewModelRadioButtonCwRwS(SortCommentsWithRouteWithSummitBy.WegName(order.value), true),
        ViewModelRadioButtonCwRwS(SortCommentsWithRouteWithSummitBy.GipfelName(order.value), false),
        ViewModelRadioButtonCwRwS(SortCommentsWithRouteWithSummitBy.Bewertung(order.value), false),
        ViewModelRadioButtonCwRwS(SortCommentsWithRouteWithSummitBy.Benutzer(order.value), false),
        ViewModelRadioButtonCwRwS(
            SortCommentsWithRouteWithSummitBy.KommentarDatum(order.value), false
        )
    )

    fun onClickRouteName() = onClick(SortCommentsWithRouteWithSummitBy.WegName())
    fun onClickSummitName() = onClick(SortCommentsWithRouteWithSummitBy.GipfelName())
    fun onClickBewertung() = onClick(SortCommentsWithRouteWithSummitBy.Bewertung())
    fun onClickKommentator() = onClick(SortCommentsWithRouteWithSummitBy.Benutzer())
    fun onClickKommentarDatum() = onClick(SortCommentsWithRouteWithSummitBy.KommentarDatum())

    val isCheckedByRouteName =
        getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.WegName()).isChecked
    val isCheckedBySummitName =
        getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.GipfelName()).isChecked
    val isCheckedByBewertung =
        getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.Bewertung()).isChecked
    val isCheckedByKommentator =
        getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.Benutzer()).isChecked
    val isCheckedByKommentarDaturm =
        getViewModelRadioButton(SortCommentsWithRouteWithSummitBy.KommentarDatum()).isChecked


    fun getViewModelRadioButton(_category: SortCommentsWithRouteWithSummitBy): ViewModelRadioButtonCwRwS {
        // Log.v(TAG, "getViewModelRadioButton = Looking for: ${_category::class.simpleName}")
        return radioButtons.find {
            it.category::class == _category::class
        } ?: throw NullPointerException("Looking for $_category, but found nothing.")
    }

    fun startAnimation() {
        _futureVisibility.value =
            (if (_visibility.value != View.VISIBLE) View.VISIBLE else View.GONE)
    }

    fun onClick(_category: SortCommentsWithRouteWithSummitBy) {
        // Is the button already checked? ==> RETURN
        if (radioButtons.find {
                it.category::class == _category::class
            }?.isChecked?.value == true) return

        radioButtons.forEach { it.onClick(it.category::class == _category::class) }
        sortCommentsBy.value = radioButtons.find { it.isChecked.value ?: false }?.category
            ?: sortCommentsBy.value
        _futureVisibility.value = View.GONE
    }
}
