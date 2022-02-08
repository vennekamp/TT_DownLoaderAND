package com.teufelsturm.tt_downloader_kotlin.feature.searches.generics

import android.text.SpannableString
import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


private const val TAG = "ViewModelSpinner"

class ViewModelSpinnerSpannable(val adapter: ArrayAdapter<SpannableString>) {

    private val _selected = MutableLiveData(0)
    val selected: LiveData<Int>
        get() = _selected

    private val _selectedItem =
        MutableLiveData<SpannableString>(adapter.getItem(selected.value ?: 0))
    val selectedItem: LiveData<SpannableString>
        get() = _selectedItem

    fun onItemSelected(_viewID: Int?, _position: Int?) {
        val position = _position ?: 0
        Log.e(
            TAG,
            "onItemSelected() for $_viewID with '${_position.let { adapter.getItem(position) } ?: "null"}'")
        if (_selected.value == position) return
        _selected.value = position
        val sp: SpannableString = adapter.getItem(position) ?: throw IllegalStateException()
        _selectedItem.value = sp

    }
}
