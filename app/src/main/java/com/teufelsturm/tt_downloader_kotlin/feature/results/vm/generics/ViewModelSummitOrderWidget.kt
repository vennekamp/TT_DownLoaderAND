package com.teufelsturm.tt_downloader_kotlin.feature.results.vm.generics

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.teufelsturm.tt_downloader_kotlin.R
import com.teufelsturm.tt_downloader_kotlin.data.order.Order
import com.teufelsturm.tt_downloader_kotlin.data.order.SortSummitWithMySummitCommentBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val TAG = "ViewModelOrderWidget"

class ViewModelSummitOrderWidget : ViewModel() {

    val sortSummitBy: MutableLiveData<SortSummitWithMySummitCommentBy> =
        MutableLiveData(SortSummitWithMySummitCommentBy.GipfelNr(Order.Ascending))

    private val _sortOrder = MutableLiveData<Boolean>(true)
    val sortOrder: LiveData<Boolean>
        get() = _sortOrder

    fun setCheckState(value: Boolean) {
        // Avoids infinite loops.
        if (_sortOrder.value == value) return
        _sortOrder.value = value
        sortSummitBy.value = sortSummitBy.value?.copy(convertBooleanToOrder(value))
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

    val radioButtons = listOf(
        ViewModelRadioButtonSwMSC(SortSummitWithMySummitCommentBy.AnzahlSternchenWege(order.value), false),
        ViewModelRadioButtonSwMSC(SortSummitWithMySummitCommentBy.AnzahlWege(order.value), false),
        ViewModelRadioButtonSwMSC(SortSummitWithMySummitCommentBy.Gebiet(order.value), false),
        ViewModelRadioButtonSwMSC(SortSummitWithMySummitCommentBy.GipfelNr(order.value), true),
        ViewModelRadioButtonSwMSC(SortSummitWithMySummitCommentBy.LeichtesterWeg(order.value), false),
        ViewModelRadioButtonSwMSC(SortSummitWithMySummitCommentBy.Name(order.value), false)
    )

    fun onCheckRadioButton(radioButtonId: Int) {
        Log.v(TAG, "onCheckRadioButton $radioButtonId")
        when (radioButtonId) {
            R.id.rb_Area -> onClick(SortSummitWithMySummitCommentBy.Gebiet())
            R.id.rb_EasiestGrade -> onClick(SortSummitWithMySummitCommentBy.LeichtesterWeg())
            R.id.rb_NumberOfRoutes -> onClick(SortSummitWithMySummitCommentBy.AnzahlWege())
            R.id.rb_NumberOfStarredRoutes -> onClick(SortSummitWithMySummitCommentBy.AnzahlSternchenWege())
            R.id.rb_SummitName -> onClick(SortSummitWithMySummitCommentBy.Name())
            R.id.rb_SummitNumber -> onClick(SortSummitWithMySummitCommentBy.GipfelNr())
        }
    }

    fun getViewModelRadioButton(_category: SortSummitWithMySummitCommentBy): ViewModelRadioButtonSwMSC {
        // Log.v(TAG, "getViewModelRadioButton = Looking for: ${_category::class.simpleName}")
        return radioButtons.find {
            it.category::class == _category::class
        } ?: throw NullPointerException("Looking for $_category, but found nothing.")
    }

    val isCheckedByName = getViewModelRadioButton(SortSummitWithMySummitCommentBy.Name()).isChecked
    val isCheckedByGebiet = getViewModelRadioButton(SortSummitWithMySummitCommentBy.Gebiet()).isChecked
    val isCheckedByAnzahlWege = getViewModelRadioButton(SortSummitWithMySummitCommentBy.AnzahlWege()).isChecked
    val isCheckedByAnzahlSternchenWege =
        getViewModelRadioButton(SortSummitWithMySummitCommentBy.AnzahlSternchenWege()).isChecked
    val isCheckedByGipfelNr = getViewModelRadioButton(SortSummitWithMySummitCommentBy.GipfelNr()).isChecked
    val isCheckedByLeichtesterWeg =
        getViewModelRadioButton(SortSummitWithMySummitCommentBy.LeichtesterWeg()).isChecked

    fun startAnimation() {
        _futureVisibility.value =
            (if (_visibility.value != View.VISIBLE) View.VISIBLE else View.GONE)
    }

    fun onClick(_category: SortSummitWithMySummitCommentBy) {
        Log.v(
            TAG,
            "onClick = Looking for: ${_category::class.simpleName}"
        )
        // Is the button now checked?
        if (radioButtons.find {
                it.category::class == _category::class
            }?.isChecked?.value == true) return
        radioButtons.forEach { it.onClick(it.category::class == _category::class) }
        sortSummitBy.value = radioButtons.find { it.isChecked.value ?: false }?.category
            ?: sortSummitBy.value
        _futureVisibility.value = View.GONE
    }
}
