package com.teufelsturm.tt_downloader_kotlin.feature.searches.generics

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.math.max
import kotlin.math.min

private const val TAG = "ViewModelSlider"

class ViewModelSlider(
    private val baseLabel: String,
    var maxValueTo: Float,
    val converter: ((Float) -> String) = { it -> it.toInt().toString() },
    setTo: Float = 0f
) {
    init {
        // Log.v(TAG, "new 'ViewModelRangeSlider' created for '$baseLabel' !!!")
        // val _minmax = arrayOf(0f, listOf(maxValueTo, 1f).maxOrNull() ?: 1f)
        // _values.value = _minmax
        if (maxValueTo <= 0f) {
            maxValueTo = 1f
        }
    }

    private val _value = MutableLiveData<Float>(setTo)
    val value: LiveData<Float>
        get() = _value

    private val _label = MutableLiveData<String>("$baseLabel ${converter.invoke(setTo)}")
    val label: LiveData<String>
        get() = _label

    private val _valueTo = MutableLiveData<Int>(maxValueTo.toInt())
    fun setValueTo(_newValueTo: Int) {
        if (_newValueTo == valueTo.value) return
        Log.i(TAG, "setValueTo... new Value: $_newValueTo")
        // reset the value to at least _newValueTo
        val newValue = min(value.value?.toInt() ?: 1, _newValueTo)
        onChange(newValue.toFloat())
        // Set the valueTo (max range)
        _valueTo.value = max(_newValueTo,1)
    }

    val valueTo: LiveData<Int>
        get() = _valueTo

    fun onChange(newValue: Float) {
        _value.value = newValue
        _label.value = "$baseLabel ${converter.invoke(newValue)}"
    }
}