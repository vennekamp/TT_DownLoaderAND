package com.teufelsturm.tt_downloader_kotlin.feature.searches.vm

import android.app.Application
import android.util.Log
import android.widget.ArrayAdapter
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import com.teufelsturm.tt_downloader_kotlin.data.db.TTRouteDAO
import com.teufelsturm.tt_downloader_kotlin.data.db.TTSummitDAO
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.RouteGrade
import com.teufelsturm.tt_downloader_kotlin.feature.searches.generics.*
import dagger.hilt.android.lifecycle.HiltViewModel
import de.teufelsturm.tt_downloader_ktx.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.math.ceil


private const val TAG = "RouteSearchViewModel"


@HiltViewModel
class RouteSearchViewModel @Inject constructor(
    application: Application,
    private val ttRouteDAO: TTRouteDAO,
    private val ttSummitDAO: TTSummitDAO
) : AndroidViewModel(application) {

    val searchTextVM = ViewModelEditText("", viewModelScope)

    private val _eventSearchRoute = MutableLiveData<Boolean>()
    val eventSearchRoute: LiveData<Boolean>
        get() = _eventSearchRoute
    val justMyRoute = ObservableBoolean(false)
    fun onClickJustMySummit() {
        justMyRoute.set(!justMyRoute.get())
        refreshRouteCount()
    }

    /** Method for the search summit event **/

    fun onSearchRouteComplete() {
        _eventSearchRoute.value = false
    }

    fun onSearchRoute() {
        Log.i(TAG, "onSearchRoute()")
        _eventSearchRoute.value = true
    }

    val spinnerAreaRoute = ViewModelSpinner(
        ttSummitDAO.getDistictAreaNames()
    )

    val rangeSliderMinMaxGradeInRouteSearch = ViewModelRangeSlider(
        application.applicationContext.resources.getString(R.string.strLimitForScale),
        RouteGrade.getMaxOrdinal().toFloat(),
        RouteGrade.float2Grade
    )

    val sliderNumberOfComments = ViewModelSlider(
        application.applicationContext.resources.getString(R.string.strNumberOfComments),
        runBlocking {
            ttRouteDAO.getMaxAnzahlDerKommentare(
                "%", "", -999, 999, 0f
            )?.toFloat() ?: 99f
        }
    )
    private val converter: ((Float) -> String) = { it -> String.format("%.2f", it) }

    val sliderMinOfMeanRating = ViewModelSlider(
        application.applicationContext.resources.getString(R.string.strMinOfMeanRating),
        runBlocking {
            ttRouteDAO.getMaxMeanRating("%", "", -999, 999, 0)
                ?: 6f
        },
        converter
    )
    private val _routeAdapter = MutableLiveData<ArrayAdapter<String>>()
    val routeAdapter: LiveData<ArrayAdapter<String>>
        get() = _routeAdapter

    val routeCount = MutableLiveData<Int>()

    val actionBarString = Transformations.map(routeCount) { routeCount ->
        formatItemCount4Button(
            routeCount,
            application.applicationContext.resources.getString(R.string.strSearchRoute)
        )
    }

    fun refreshRouteCount() {
        viewModelScope.launch {
            routeCount.value = upDateRouteCount()
        }
    }

    private suspend fun upDateRouteCount(): Int {
        val partialRouteName = "%${searchTextVM.searchText.value}%"
        val area = spinnerAreaRoute.selectedItem.value ?: ""
        val lowerLimit = rangeSliderMinMaxGradeInRouteSearch.values.value?.get(0)?.toInt()
        val upperLimit = rangeSliderMinMaxGradeInRouteSearch.values.value?.get(1)?.toInt()
        val intMinGrade =
            RouteGrade.getRouteGradeByOrdinal(lowerLimit) ?: RouteGrade.getMinOrdinal()
        val intMaxGrade =
            RouteGrade.getRouteGradeByOrdinal(upperLimit) ?: RouteGrade.getMaxOrdinal()
        val minNumberOfComments = sliderNumberOfComments.value.value?.toInt() ?: 0
        val minOfMeanRating = sliderMinOfMeanRating.value.value ?: 0f
        val justMyRoute = justMyRoute.get()
        if (justMyRoute) {
            return ttRouteDAO.getConstrainedJustMineCount(
                partialRouteName = partialRouteName,
                area = area,
                intMinSchwierigkeit = intMinGrade,
                intMaxSchwierigkeit = intMaxGrade,
                minNumberOfComments = minNumberOfComments,
                minOfMeanRating = minOfMeanRating
            )
        }
        return ttRouteDAO.getConstrainedCount(
            partialRouteName = partialRouteName,
            area = area,
            intMinSchwierigkeit = intMinGrade,
            intMaxSchwierigkeit = intMaxGrade,
            minNumberOfComments = minNumberOfComments,
            minOfMeanRating = minOfMeanRating
        )
    }

    fun refreshRangeSlider() {
        viewModelScope.launch {
            val partialRouteName = "%${searchTextVM.searchText.value}%"
            val area = spinnerAreaRoute.selectedItem.value ?: ""
            val lowerLimit = rangeSliderMinMaxGradeInRouteSearch.values.value?.get(0)?.toInt()
            val upperLimit = rangeSliderMinMaxGradeInRouteSearch.values.value?.get(1)?.toInt()
            val intMinGrade =
                RouteGrade.getRouteGradeByOrdinal(lowerLimit) ?: RouteGrade.getMinOrdinal()
            val intMaxGrade =
                RouteGrade.getRouteGradeByOrdinal(upperLimit) ?: RouteGrade.getMaxOrdinal()
            val minNumberOfComments =
                sliderNumberOfComments.value.value?.toInt() ?: 0
            val minOfMeanRating =
                sliderMinOfMeanRating.value.value ?: 0f
            // This causes circular dependencies
//            val gradeMinMaxValues = runBlocking {
//                ttRouteDAO.getConstrainedMinMaxGrade(
//                    partialRouteName,
//                    area,
//                    minNumberOfComments,
//                    minOfMeanRating
//                )
//            }.asListOfOrdinal()
//            rangeSliderMinMaxGradeInRouteSearch.setValueFrom(gradeMinMaxValues[0])
//            rangeSliderMinMaxGradeInRouteSearch.setValueTo(1 + gradeMinMaxValues[1])
            val maxAnzahlDerKommentare = runBlocking {
                ttRouteDAO.getMaxAnzahlDerKommentare(
                partialRouteName,
                area,
                intMinGrade,
                intMaxGrade,
                minOfMeanRating
            )
        } ?: 1
        sliderNumberOfComments.setValueTo(maxAnzahlDerKommentare)
        val maxMeanRating = ceil((runBlocking {
            ttRouteDAO.getMaxMeanRating(
                partialRouteName,
                area,
                intMinGrade,
                intMaxGrade,
                minNumberOfComments
            )
        } ?: 1f) - 0.001f).toInt()
        sliderMinOfMeanRating.setValueTo(maxMeanRating)
    }
}

/**
 * Returns a search result for the given summit start title.
 */
private fun findRoutes(routePart: String): List<String> {
    val _area =
        spinnerAreaRoute.selected.value?.let { spinnerAreaRoute.entries.value?.get(it) }
    val rtnList = if (_area == "-" || _area == null) {
        ttRouteDAO.getRouteNameForAutoText(routePart)
    } else {
        ttRouteDAO.getRouteNameForAutoText(routePart, _area)
    }
    return rtnList
}

init {
    _routeAdapter.value =
        AutoCompleteAdapter(application, ::findRoutes, R.id.editTextSuchtextWege)
    refreshRouteCount()
}
}