package com.teufelsturm.tt_downloader_kotlin.feature.searches.vm

import android.app.Application
import android.util.Log
import android.widget.ArrayAdapter
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import com.teufelsturm.tt_downloader_kotlin.data.db.TTSummitDAO
import com.teufelsturm.tt_downloader_kotlin.feature.searches.generics.*
import dagger.hilt.android.lifecycle.HiltViewModel
import de.teufelsturm.tt_downloader_ktx.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

private const val TAG = "SummitSearchViewModel"

@HiltViewModel
class SummitSearchViewModel @Inject constructor(
    application: Application,
    private val ttSummitDAO: TTSummitDAO
) : AndroidViewModel(application) {

    private val _eventSearchSummit = MutableLiveData<Boolean>()
    val eventSearchSummit: LiveData<Boolean>
        get() = _eventSearchSummit
    val justMySummit = ObservableBoolean(false)
    fun onClickJustMySummit() {
        justMySummit.set(!justMySummit.get())
        refreshSummitCount()
    }

    val spinnerAreaSummit = ViewModelSpinner(
        ttSummitDAO.getDistictAreaNames()
    )

    val searchTextVM = ViewModelEditText("", viewModelScope)

    val rangeSliderAnzahlDerWege: ViewModelRangeSlider = ViewModelRangeSlider(
        application.applicationContext.resources.getString(R.string.strAnzahlDerWege),
        runBlocking { ttSummitDAO.getMaxAnzahlDerWege()?.toFloat() ?: 200f }
    )

    val rangeSliderAnzahlDerSternchenWege = ViewModelRangeSlider(
        application.applicationContext.resources.getString(R.string.strAnzahlDerSternchenWege),
        runBlocking { ttSummitDAO.getMaxAnzahlDerSternchenWege()?.toFloat() ?: 100f }
    )

    fun refreshRangeSlider() {
        viewModelScope.launch {
            val maxAnzahlDerWege: Float? = ttSummitDAO.getMaxAnzahlDerWege(
                spinnerAreaSummit.entries.value?.get(spinnerAreaSummit.selected.value ?: 0) ?: "",
                "%" + searchTextVM.searchText.value + "%"
            )?.toFloat()
            maxAnzahlDerWege?.let { rangeSliderAnzahlDerWege.setValueTo(maxAnzahlDerWege) }
            val maxAnzahlDerSternchenWege: Float? = ttSummitDAO.getMaxAnzahlDerSternchenWege(
                spinnerAreaSummit.entries.value?.get(spinnerAreaSummit.selected.value ?: 0) ?: "",
                "%" + searchTextVM.searchText.value + "%"
            )?.toFloat()
            maxAnzahlDerSternchenWege?.let {
                rangeSliderAnzahlDerSternchenWege.setValueTo(
                    maxAnzahlDerSternchenWege
                )
            }
        }
    }

    private val _summitAdapter = MutableLiveData<ArrayAdapter<String>>()
    val summitAdapter: LiveData<ArrayAdapter<String>>
        get() = _summitAdapter

    val summitCount = MutableLiveData<Int>()

    val actionBarString = Transformations.map(summitCount) { summitCount ->
        formatItemCount4Button(
            summitCount,
            application.resources.getString(R.string.strSearchSummit)
        )
    }

    fun refreshSummitCount() {
        viewModelScope.launch {
            summitCount.value = upDateSummitCount()
        }
    }

    private suspend fun upDateSummitCount(): Int {

        val rtnVal = if (justMySummit.get())
            ttSummitDAO.getConstrainedJustMineCount(
                minAnzahlWege = rangeSliderAnzahlDerWege.values.value?.get(0)?.toInt() ?: 0,
                maxAnzahlWege = rangeSliderAnzahlDerWege.values.value?.get(1)?.toInt() ?: 9999,
                minAnzahlSternchenWege = rangeSliderAnzahlDerSternchenWege.values.value?.get(0)
                    ?.toInt() ?: 0,
                maxAnzahlSternchenWege = rangeSliderAnzahlDerSternchenWege.values.value?.get(1)
                    ?.toInt() ?: 9999,
                searchAreas = spinnerAreaSummit.selectedItem.value ?: "",
                searchText = "%" + searchTextVM.searchText.value + "%"
            )
        else ttSummitDAO.getConstrainedCount(
            minAnzahlWege = rangeSliderAnzahlDerWege.values.value?.get(0)?.toInt() ?: 0,
            maxAnzahlWege = rangeSliderAnzahlDerWege.values.value?.get(1)?.toInt() ?: 9999,
            minAnzahlSternchenWege = rangeSliderAnzahlDerSternchenWege.values.value?.get(0)
                ?.toInt() ?: 0,
            maxAnzahlSternchenWege = rangeSliderAnzahlDerSternchenWege.values.value?.get(1)
                ?.toInt() ?: 9999,
            searchAreas = spinnerAreaSummit.selectedItem.value ?: "",
            searchText = "%" + searchTextVM.searchText.value + "%"
        )
        return rtnVal
    }

    /**
     * Returns a search result for the given summit start title.
     */
    private fun findSummits(summitPart: String): List<String> {
        val mArea =
            spinnerAreaSummit.selected.value?.let { spinnerAreaSummit.entries.value?.get(it) }
        val rtnList = if (mArea == "-" || mArea == null) {
            ttSummitDAO.getSummitNameForAutoText(summitPart)
        } else {
            ttSummitDAO.getSummitNameForAutoText(summitPart, mArea)
        }
        return rtnList
    }

    /** Method for the search summit event **/

    fun onSearchSummitComplete() {
        _eventSearchSummit.value = false
    }

    fun onSearchSummit() {
        _eventSearchSummit.value = true
    }


    init {
        Log.v(TAG, " SummitSearchViewModel initialized !!!")
        /**
         * Returns a search result for the given summit start title.
         */
        _summitAdapter.value =
            AutoCompleteAdapter(application, ::findSummits, R.id.editTextSuchtextGipfel)
        refreshSummitCount()
    }
}


