package com.teufelsturm.tt_downloader_kotlin.results.vm.generics

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teufelsturm.tt_downloader_kotlin.data.order.SortCommentsWithRouteWithSummitBy
import com.teufelsturm.tt_downloader_kotlin.data.order.SortRouteWithMyRouteCommentBy
import com.teufelsturm.tt_downloader_kotlin.data.order.SortSummitWithMySummitCommentBy

private const val TAG = "ViewModelOrderWidget_RB"

class ViewModelRadioButtonSwMSC(
    val category: SortSummitWithMySummitCommentBy,
    state: Boolean
) : ViewModel() {
    private val _isChecked = MutableLiveData<Boolean>(state)
    val isChecked: LiveData<Boolean>
        get() {
            Log.v(
                TAG,
                "isChecked = returning ${_isChecked.value} for: ${category::class.simpleName}  for ${this::class.simpleName} "
            )
            return _isChecked
        }

    fun onClick(state: Boolean) {
        if (isChecked.value == state) return
        _isChecked.value = state
    }
}


class ViewModelRadioButtonRwMRC(
    val category: SortRouteWithMyRouteCommentBy,
    state: Boolean
) : ViewModel() {
    private val _isChecked = MutableLiveData<Boolean>(state)
    val isChecked: LiveData<Boolean>
        get() {
            Log.v(
                TAG,
                "isChecked = returning ${_isChecked.value} for: ${category::class.simpleName}  for ${this::class.simpleName} "
            )
            return _isChecked
        }

    fun onClick(state: Boolean) {
        if (isChecked.value == state) return
        _isChecked.value = state
    }
}

class ViewModelRadioButtonCwRwS(
    val category: SortCommentsWithRouteWithSummitBy,
    state: Boolean
) : ViewModel() {
    private val _isChecked = MutableLiveData<Boolean>(state)
    val isChecked: LiveData<Boolean>
        get() {
            Log.v(
                TAG,
                "isChecked = returning ${_isChecked.value} for: ${category::class.simpleName}  for ${this::class.simpleName} "
            )
            return _isChecked
        }

    fun onClick(state: Boolean) {
        if (isChecked.value == state) return
        _isChecked.value = state
    }
}