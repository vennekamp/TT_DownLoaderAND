package com.teufelsturm.tt_downloader_kotlin.feature.searches.generics

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.runBlocking

private const val TAG = "ViewModelSpinner"

class ViewModelSpinner(val entries: LiveData<List<String>>) {

    private val _selected = MutableLiveData(0)
    val selected: LiveData<Int>
        get() = _selected

    private val _selectedItem = MutableLiveData<String>(entries.value?.get(selected.value ?: 0))
    val selectedItem: LiveData<String?>
        get() {
            return _selectedItem
        }

    fun onItemSelected(id: Long, item: Int) {
        Log.i(TAG, "onItemSelected() for $id with '${entries.value?.get(item)}'")
        _selected.value = item
        _selectedItem.value = entries.value?.get(item)
    }

}