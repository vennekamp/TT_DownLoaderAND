package com.teufelsturm.tt_downloader_kotlin.feature.searches.generics

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope

private const val TAG = "ViewModelEditText"

class ViewModelEditText(startValue: String, private val scope: CoroutineScope) {

    private var debounceSearchFor = ""
    private var _searchText: MutableLiveData<String?> = MutableLiveData(startValue)
    val searchText: MutableLiveData<String?>
        get() = _searchText

    fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        val searchText = s.toString().trim()
//        if ( searchText == debounceSearchFor ) return
//        debounceSearchFor = searchText
//        scope.launch{
//            delay(300 ) // debounce timeout
//            if ( searchText != debounceSearchFor) return@launch
        _searchText.value = s.toString()
        Log.i(TAG, " ---> $s")
    }
//    }
}