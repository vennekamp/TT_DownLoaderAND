package com.teufelsturm.tt_downloader_kotlin.feature.results.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.teufelsturm.tt_downloader_kotlin.data.db.TTCommentDAO
import com.teufelsturm.tt_downloader_kotlin.data.entity.CommentsWithRouteWithSummit
import com.teufelsturm.tt_downloader_kotlin.data.order.SortCommentsWithRouteWithSummitBy
import com.teufelsturm.tt_downloader_kotlin.data.order.sortCommentsWithRouteSummitBy
import com.teufelsturm.tt_downloader_kotlin.feature.results.ui.CommentsListResultFragmentArgs
import com.teufelsturm.tt_downloader_kotlin.feature.results.vm.generics.ViewModelCommentOrderWidget
import com.teufelsturm.tt_downloader_kotlin.feature.searches.generics.EventNavigatingToRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "CommentsListResultVM"

@HiltViewModel
class CommentsListResultViewModel @Inject constructor(
    application: Application,
    private val ttCommentDAO: TTCommentDAO
) :
    AndroidViewModel(application) {

    val viewModelCommentOrderWidget = ViewModelCommentOrderWidget()

    private var _ttCommentANDList: MutableLiveData<List<CommentsWithRouteWithSummit>> =
        MutableLiveData()
    val ttCommentANDList: LiveData<List<CommentsWithRouteWithSummit>>
        get() = _ttCommentANDList
    private val _queryRunning: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val queryRunning: LiveData<Boolean>
        get() = _queryRunning

    fun queryRoutes(args: CommentsListResultFragmentArgs) {
        _queryRunning.value = true
        viewModelScope.launch { queryRoutesAsync(args) }
    }

    private suspend fun queryRoutesAsync(
        args: CommentsListResultFragmentArgs
    ) {
        with(args.argSearchCommentParameter) {
            // ttRouteDAO.getRouteListWithMyCommentWithSummit(4)
            ttCommentDAO.getAllCommentsConstrained(
                minRatingInComment = minRatingInComment,
                maxRatingInComment = maxRatingInComment,
                partialComment = partialComment,
                area = searchAreas,
                intMinSchwierigkeit = intMinGrade,
                intMaxSchwierigkeit = intMaxGrade
            ).collect {
                _ttCommentANDList.value = it
                _queryRunning.value = false
            }
        }
    }

    private val _navigateToDetailEvent = MutableLiveData<EventNavigatingToRoute?>()
    val navigateToDetailEvent: LiveData<EventNavigatingToRoute?>
        get() = _navigateToDetailEvent

    fun doneNavigatingToDetail() {
        if (_navigateToDetailEvent.value != null) _navigateToDetailEvent.value = null
    }

    fun onClickItem(routeId: Int?, summitId: Int) {
        if (_navigateToDetailEvent.value != EventNavigatingToRoute(routeId, summitId))
            _navigateToDetailEvent.value = EventNavigatingToRoute(routeId, summitId)
    }

    fun onChangeSortOrder(sortCommentsWithRouteWithSummitBy: SortCommentsWithRouteWithSummitBy) {
        _ttCommentANDList.value =
            _ttCommentANDList.value?.let { _ttCommentANDList.value!!.sortCommentsWithRouteSummitBy(sortCommentsWithRouteWithSummitBy) }
    }
}
