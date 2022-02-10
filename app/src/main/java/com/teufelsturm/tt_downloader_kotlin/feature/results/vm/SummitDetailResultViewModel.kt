package com.teufelsturm.tt_downloader_kotlin.feature.results.vm

import android.app.Application
import androidx.lifecycle.*
import com.teufelsturm.tt_downloader_kotlin.data.db.*
import com.teufelsturm.tt_downloader_kotlin.data.entity.*
import com.teufelsturm.tt_downloader_kotlin.data.order.SortRouteWithMyRouteCommentBy
import com.teufelsturm.tt_downloader_kotlin.data.order.sortRoutesBy
import com.teufelsturm.tt_downloader_kotlin.feature.results.vm.generics.ViewModelRouteOrderWidget
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
    private val _mTTSummit: MutableLiveData<TTSummitAND> = MutableLiveData()
    val mTTSummit: LiveData<TTSummitAND>
        get() = _mTTSummit

    private val _mMYTTSummit: MutableLiveData<List<MyTTSummitAND>> = MutableLiveData()
    val mMYTTSummit: LiveData<List<MyTTSummitAND>>
        get() = _mMYTTSummit

    private val _ttRoutes: MutableLiveData<List<Comments.RouteWithMyComment>> = MutableLiveData()
    val ttRoutes: LiveData<List<Comments.RouteWithMyComment>>
        get() = _ttRoutes
    private val _ttNeigbours: MutableLiveData<List<TTNeigbourANDTTName>> = MutableLiveData()
    val ttNeigbours: LiveData<List<TTNeigbourANDTTName>>
        get() = _ttNeigbours

    fun queryData(intTTGipfelNr: Int) {
        _queriesRunning.value = 3
        viewModelScope.launch {queryTTSummitAsync(intTTGipfelNr) }
        viewModelScope.launch {queryNeighboursAsync(intTTGipfelNr) }
        viewModelScope.launch { queryTTRoutesAsync(intTTGipfelNr) }
    }

    private suspend fun queryTTSummitAsync(intTTGipfelNr: Int) {
        // mTTSummit =
        ttSummitDAO.getSummit(intTTGipfelNr).collect {
            _mTTSummit.value = it
            _queriesRunning.apply { value = value?.minus(1) }
        }
    }

    private suspend fun queryMyTTSummitAsync(intTTGipfelNr: Int) {
        // mMyTTSummit =
        ttSummitDAO.getSummitWithMySummitComment(intTTGipfelNr).collect {
            _mMYTTSummit.value = it.myTTSummitANDList
            _queriesRunning.apply { value = value?.minus(1) }
        }
    }

    private suspend fun queryNeighboursAsync(intTTGipfelNr: Int) {
        // ttNeigbours =
        ttNeigbourSummitANDDAO.getTSPSummits(intTTGipfelNr).collect {
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

    private val _navigateToSummitGeo = MutableLiveData<Boolean>(false)
    val navigateToSummitGeo : LiveData<Boolean>
        get() = _navigateToSummitGeo
    fun onSummitGeoClick(){
        _navigateToSummitGeo.value = true
    }
    fun onSummitGeoClicked(){
        _navigateToSummitGeo.value = false
    }
    /**
     * Variable that tells the Fragment to navigate to a specific [SummitDetailResultFragment]
     *
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    private val _navigateToRouteDetail = MutableLiveData<Int?>(null)
    val navigateToRouteDetail : LiveData<Int?>
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
