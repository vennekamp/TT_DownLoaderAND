package com.teufelsturm.tt_downloader_kotlin.searches.generics

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.math.max
import kotlin.math.min

private const val TAG = "ViewModelRangeSlider"

class ViewModelRangeSlider(
    private val baseLabel: String,
    var maxValueTo: Float,
    val converter: ((Float) -> String) = { it -> it.toInt().toString() }
) {

    init {
        if (maxValueTo <= 0f) {
            maxValueTo = 1f
        }
    }


    private val _label = MutableLiveData<String>(baseLabel)
    val label: LiveData<String>
        get() = _label

    private val _values = MutableLiveData<List<Float>>(listOf(0f, maxValueTo))
    val values: LiveData<List<Float>>
        get() = _values

    private val _valueFrom = MutableLiveData<Int>(0)
    val valueFrom: LiveData<Int>
        get() = _valueFrom

    fun setValueFrom(_newValueFrom: Float) {
        if (_newValueFrom.toInt() == _valueFrom.value) return
        val newValueFrom = if (_newValueFrom < 0f) 0f else _newValueFrom
        // values (lower & upper thumb) must be within [valueFrom..valueTo]
        var newLowerPos = min(values.value?.get(0) ?: 0f, newValueFrom)
        var newUpperPos = max(values.value?.get(1) ?: 0f, newLowerPos)
        // lower thumb on lowest position -> stay there!
        if (values.value?.get(0)?.toInt() == valueFrom.value) {
            newLowerPos = newValueFrom
        }
        // position of valueFrom..valueTo
        if (newUpperPos <= newLowerPos) {
            newUpperPos = newLowerPos + 1f
        }
        onChange(arrayListOf(newLowerPos, newUpperPos))
        _valueFrom.value = newValueFrom.toInt()
        Log.i(TAG, "setValueTo ($_newValueFrom) new Value: $label")
    }
    private val _valueTo = MutableLiveData<Int>(maxValueTo.toInt())
    val valueTo: LiveData<Int>
        get() = _valueTo

    fun setValueTo(_newValueTo: Float) {
        if (_newValueTo.toInt() == valueTo.value) return
        val newValueTo = if (_newValueTo < 1f) 1f else _newValueTo
        // values (lower & upper thumb) must be within [valueFrom..valueTo]
        var newUpperPos = min(values.value?.get(1) ?: 0f, newValueTo)
        val newLowerPos = min(values.value?.get(0) ?: 0f, newUpperPos)
        // lower thumb on highest position -> stay there!
        if (values.value?.get(1)?.toInt() == valueTo.value) {
            newUpperPos = newValueTo
        }
        // position of valueFrom..valueTo
        if (newUpperPos <= newLowerPos) {
            newUpperPos = newLowerPos + 1f
        }
        onChange(arrayListOf(newLowerPos, newUpperPos))
        _valueTo.value = newValueTo.toInt()
        Log.i(TAG, "setValueTo ($_newValueTo) new Value: $label")
    }

    fun onChange(newValues: List<Float>) {
        if ( _values.value == newValues ) return
        _values.value = newValues
        _label.value =
            "$baseLabel (${converter.invoke(_values.value?.get(0)!!)} bis " +
                    "${converter.invoke(_values.value?.get(1)!!)})"

        Log.i(TAG, "setValueTo... new Value: ${_label.value}")
    }
}