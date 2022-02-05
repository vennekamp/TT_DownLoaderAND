package com.teufelsturm.tt_downloader_kotlin.results.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.teufelsturm.tt_downloader_kotlin.data.db.TTRouteDAO
import com.teufelsturm.tt_downloader_kotlin.data.entity.RouteWithMyCommentWithSummit
import com.teufelsturm.tt_downloader_kotlin.data.order.SortRouteWithMyRouteCommentBy
import com.teufelsturm.tt_downloader_kotlin.data.order.sortRoutesWithSummitBy
import com.teufelsturm.tt_downloader_kotlin.results.ui.RoutesListResultFragmentArgs
import com.teufelsturm.tt_downloader_kotlin.results.vm.generics.ViewModelRouteOrderWidget
import com.teufelsturm.tt_downloader_kotlin.searches.generics.EventNavigatingToRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "RoutesListResultVM"

@HiltViewModel
class RoutesListResultViewModel @Inject constructor(
    application: Application,
    private val ttRouteDAO: TTRouteDAO
) :
    AndroidViewModel(application) {

    val viewModelRouteOrderWidget = ViewModelRouteOrderWidget()

    private var _ttRouteANDList: MutableLiveData<List<RouteWithMyCommentWithSummit>> =
        MutableLiveData()
    val ttRouteANDList: LiveData<List<RouteWithMyCommentWithSummit>>
        get() = _ttRouteANDList
    private val _queryRunning: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val queryRunning: LiveData<Boolean>
        get() = _queryRunning

    fun queryRoutes(args: RoutesListResultFragmentArgs) {
        _queryRunning.value = true
        viewModelScope.launch { queryRoutesAsync(args) }
    }

    private suspend fun queryRoutesAsync(
        args: RoutesListResultFragmentArgs
    ) {
        with(args.eventSearchRouteParameter) {
            // ttRouteANDList =
            if (justMyRoutes) {
                ttRouteDAO.loadRouteListWithMyCommentWithSummitConstrainedJustMine(
                    partialRouteName = partialRouteName,
                    area = area,
                    intMinSchwierigkeit = intMinGrade,
                    intMaxSchwierigkeit = intMaxGrade,
                    minNumberOfComments = minNumberOfComments,
                    minOfMeanRating = minOfMeanRating
                ).collect {
                    _ttRouteANDList.value =
                        it.sortRoutesWithSummitBy(viewModelRouteOrderWidget.sortRoutesBy.value)
                    _queryRunning.value = false
                }
            } else {
                ttRouteDAO.loadRouteListWithMyCommentWithSummitConstrained(
                    partialRouteName = partialRouteName,
                    area = area,
                    intMinSchwierigkeit = intMinGrade,
                    intMaxSchwierigkeit = intMaxGrade,
                    minNumberOfComments = minNumberOfComments,
                    minOfMeanRating = minOfMeanRating,
                ).collect {
                    _ttRouteANDList.value =
                        it.sortRoutesWithSummitBy(viewModelRouteOrderWidget.sortRoutesBy.value)
                    _queryRunning.value = false
                }
            }
        }
    }

    private val _navigateToSummitDetail = MutableLiveData<Int?>()
    val navigateToSummitDetail: LiveData<Int?>
        get() = _navigateToSummitDetail

    fun doneNavigatingToSumit() {
        _navigateToSummitDetail.value = null
    }

    fun onClickSummit(summitId: Int) {
        _navigateToSummitDetail.value = summitId
    }

    private val _navigateToRouteDetail = MutableLiveData<EventNavigatingToRoute?>()
    val navigateToRouteDetailEvent: LiveData<EventNavigatingToRoute?>
        get() = _navigateToRouteDetail

    fun doneNavigatingToRoute() {
        _navigateToRouteDetail.value = null
    }

    fun onClickRoute(routeId: Int?, summitId: Int) {
        _navigateToRouteDetail.value = EventNavigatingToRoute(
            intTTWegNr = routeId,
            intTTSummitNr = summitId
        )
    }

    fun onChangeSortOrder(sortRouteWithMyRouteCommentBy: SortRouteWithMyRouteCommentBy) {
        _ttRouteANDList.value =
            _ttRouteANDList.value?.let {
                _ttRouteANDList.value!!.sortRoutesWithSummitBy(
                    sortRouteWithMyRouteCommentBy
                )
            }
    }
}