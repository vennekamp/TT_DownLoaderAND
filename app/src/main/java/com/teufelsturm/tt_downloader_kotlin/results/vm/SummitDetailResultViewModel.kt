package com.teufelsturm.tt_downloader_kotlin.results.vm

import android.app.Application
import androidx.lifecycle.*
import com.teufelsturm.tt_downloader_kotlin.data.db.*
import com.teufelsturm.tt_downloader_kotlin.data.entity.RouteComments
import com.teufelsturm.tt_downloader_kotlin.data.entity.SummitWithMySummitComment
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTNeigbourANDTTName
import com.teufelsturm.tt_downloader_kotlin.data.order.SortRouteWithMyRouteCommentBy
import com.teufelsturm.tt_downloader_kotlin.data.order.sortRoutesBy
import com.teufelsturm.tt_downloader_kotlin.results.vm.generics.ViewModelRouteOrderWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SummitDetailResultVM"

@HiltViewModel
class SummitDetailResultViewModel @Inject constructor(
    application: Application,
    private val ttSummitDAO: TTSummitDAO,
    private val ttRouteDAO: TTRouteDAO,
    private val ttNeigbourSummitANDDAO: TTNeigbourSummitANDDAO
) :
    AndroidViewModel(application) {

    val viewModelRouteOrderWidget = ViewModelRouteOrderWidget()

    private val _queriesRunning: MutableLiveData<Int> = MutableLiveData<Int>(3)
    val queriesRunning: LiveData<Int>
        get() = _queriesRunning
    val _mTTSummit: MutableLiveData<SummitWithMySummitComment> = MutableLiveData()
    val mTTSummit: LiveData<SummitWithMySummitComment>
        get() = _mTTSummit
    val _ttRoutes: MutableLiveData<List<RouteComments.RouteWithMyRouteComment>> = MutableLiveData()
    val ttRoutes: LiveData<List<RouteComments.RouteWithMyRouteComment>>
        get() = _ttRoutes
    val _ttNeigbours: MutableLiveData<List<TTNeigbourANDTTName>> = MutableLiveData()
    val ttNeigbours: LiveData<List<TTNeigbourANDTTName>>
        get() = _ttNeigbours

    fun queryData(intTTGipfelNr: Int) {
        _queriesRunning.value = 3
        viewModelScope.launch { queryTTSummitAsync(intTTGipfelNr) }
        viewModelScope.launch { queryNeighboursAsync(intTTGipfelNr) }
        viewModelScope.launch { queryTTRoutesAsync(intTTGipfelNr) }
    }

    private suspend fun queryTTSummitAsync(intTTGipfelNr: Int) {
        // mTTSummit =
        ttSummitDAO.getSummitWithMySummitComment(intTTGipfelNr).collect {
            _mTTSummit.value = it
            _queriesRunning.apply { value = value?.minus(1) }
        }
    }

    private suspend fun queryNeighboursAsync(intTTGipfelNr: Int) {
        // ttNeigbours =
        ttNeigbourSummitANDDAO.getNeighbours(intTTGipfelNr).collect {
            _ttNeigbours.value = it
            _queriesRunning.apply { value = value?.minus(1) }
        }
    }

    private suspend fun queryTTRoutesAsync(intTTGipfelNr: Int) {
        ttRouteDAO.getRouteWithMySummitCommentBySummit(intTTGipfelNr).collect {
            _ttRoutes.value = it.sortRoutesBy(viewModelRouteOrderWidget.sortRoutesBy.value)
            _queriesRunning.apply { value = value?.minus(1) }
        }
    }

    fun upDateMyData() {

    }

    /**
     * Variable that tells the Fragment to navigate to a specific [SummitDetailResultFragment]
     *
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    private val _navigateToSummitDetail = MutableLiveData<Int?>(null)
    val navigateToNextSummitDetail
        get() = _navigateToSummitDetail

    /**
     * Call this immediately after navigating to [SummitDetailResultFragment]
     *
     * It will clear the navigation request, so if the user rotates their phone it won't navigate
     * twice.
     */
    fun doneNavigatingSummit() {
        _navigateToSummitDetail.value = null
    }

    fun onClickNeighbour(pos: Int) {
        _navigateToSummitDetail.value = ttNeigbours.value?.get(pos)?.intTTNachbarGipfelNr
    }

    /**
     * Variable that tells the Fragment to navigate to a specific [SummitDetailResultFragment]
     *
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    private val _navigateToRouteDetail = MutableLiveData<Int?>(null)
    val navigateToRouteDetail
        get() = _navigateToRouteDetail

    /**
     * Call this immediately after navigating to [SummitDetailResultFragment]
     *
     * It will clear the navigation request, so if the user rotates their phone it won't navigate
     * twice.
     */
    fun doneNavigatingRoute() {
        _navigateToRouteDetail.value = null
    }

    fun onClickRoute(routeID: Int) {
        _navigateToRouteDetail.value = routeID
    }

    fun onChangeSortOrder(sortRouteWithMyRouteCommentBy: SortRouteWithMyRouteCommentBy) {
        _ttRoutes.value =
            _ttRoutes.value?.let { _ttRoutes.value!!.sortRoutesBy(sortRouteWithMyRouteCommentBy) }
    }
}
