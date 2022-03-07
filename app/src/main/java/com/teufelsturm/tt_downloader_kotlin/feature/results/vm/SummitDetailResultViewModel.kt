package com.teufelsturm.tt_downloader_kotlin.feature.results.vm

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.teufelsturm.tt_downloader_kotlin.data.db.*
import com.teufelsturm.tt_downloader_kotlin.data.entity.*
import com.teufelsturm.tt_downloader_kotlin.data.order.SortRouteWithMyRouteCommentBy
import com.teufelsturm.tt_downloader_kotlin.data.order.sortRoutesBy
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.CompareComments
import com.teufelsturm.tt_downloader_kotlin.feature.results.vm.generics.ViewModelRouteOrderWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SummitDetailResultVM"

@HiltViewModel
class SummitDetailResultViewModel @Inject constructor(
    application: Application,
    private val ttSummitDAO: TTSummitDAO,
    private val ttRouteDAO: TTRouteDAO,
    private val myTTCommentDAO: MyTTCommentDAO,
    private val ttNeighbourSummitANDDAO: TTNeighbourSummitANDDAO
) :
    AndroidViewModel(application) {

    val viewModelRouteOrderWidget = ViewModelRouteOrderWidget()

    private val _queriesRunning: MutableLiveData<Int> = MutableLiveData<Int>(3)
    val queriesRunning: LiveData<Int>
        get() = _queriesRunning
    private val _mTTSummit: MutableLiveData<TTSummitAND> = MutableLiveData()
    val mTTSummit: LiveData<TTSummitAND>
        get() = _mTTSummit

    private val _showMyComments: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>(null)
    val showMyComments: LiveData<Boolean?>
        get() = _showMyComments

    private val _ttRoutes: MutableStateFlow<List<Comments.RouteWithMyComment>> =
        MutableStateFlow(listOf())
    val ttRoutes: StateFlow<List<Comments.RouteWithMyComment>>
        get() = _ttRoutes
    private val _ttNeigbours: MutableLiveData<List<TTNeigbourANDTTName>> = MutableLiveData()
    val ttNeigbours: LiveData<List<TTNeigbourANDTTName>>
        get() = _ttNeigbours

    private val _mMyTTCommentANDWithPhotos: MutableStateFlow<MutableList<Comments>> =
        MutableStateFlow(mutableListOf())
    val mMyTTCommentANDWithPhotos: StateFlow<MutableList<Comments>>
        get() = _mMyTTCommentANDWithPhotos

    private val _navigateToCommentInputFragment: MutableLiveData<Comments.MyTTCommentANDWithPhotos?> =
        MutableLiveData()
    val navigateToCommentInputFragment: LiveData<Comments.MyTTCommentANDWithPhotos?>
        get() = _navigateToCommentInputFragment

    private val _navigateToImageFragment: MutableLiveData<View?> =
        MutableLiveData()
    val navigateToImageFragment: LiveData<View?>
        get() = _navigateToImageFragment

    fun queryData(intTTGipfelNr: Int) {
        _queriesRunning.value = 3
        Log.e(TAG, "queryData")
        viewModelScope.launch { queryTTSummitAsync(intTTGipfelNr) }
        viewModelScope.launch { queryNeighboursAsync(intTTGipfelNr) }
        viewModelScope.launch {
            queryTTRoutesAsync(intTTGipfelNr)
            // queryMyTTCommentANDWithPhotos(intTTGipfelNr)
        }
    }

    private suspend fun queryTTSummitAsync(intTTGipfelNr: Int) {
        // mTTSummit =
        ttSummitDAO.getSummit(intTTGipfelNr).collect {
            _mTTSummit.value = it
            _queriesRunning.apply { value = value?.minus(1) }
        }
    }

    private suspend fun queryNeighboursAsync(intTTGipfelNr: Int) {
        // ttNeigbours =
        ttNeighbourSummitANDDAO.getTSPSummits(intTTGipfelNr).collect {
            _ttNeigbours.value = it
            _queriesRunning.apply { value = value?.minus(1) }
        }
    }

    private suspend fun queryTTRoutesAsync(intTTGipfelNr: Int) {
        // _ttRoutes
        ttRouteDAO.getRouteWithMySummitCommentBySummit(intTTGipfelNr).collect {
            _ttRoutes.value = it.sortRoutesBy(viewModelRouteOrderWidget.sortRoutesBy.value)
            _queriesRunning.apply { value = value?.minus(1) }
            // }
//    }
//
//    private suspend fun queryMyTTCommentANDWithPhotos(intTTGipfelNr: Int) {
            Log.e(TAG, "queryMyTTCommentANDWithPhotos")
            // _mMyTTCommentANDWithPhotos
            myTTCommentDAO.getCommentWithPhotoBySummit(intTTGipfelNr).collect { mList ->
                Log.e(TAG, "queryMyTTCommentANDWithPhotos 1")
                Log.e(TAG, "queryMyTTCommentANDWithPhotos 2")
                val summitList = mutableListOf<Comments>()
                Log.e(TAG, "queryMyTTCommentANDWithPhotos 3 it size is: ${it.size}")
                mList.forEach { commentWithPhoto ->
                    val routeWithMyTTCommentANDWithPhotos =
                        Comments.RouteWithMyTTCommentANDWithPhotos(
                            ttRouteAND = null,
                            myTTCommentAND = commentWithPhoto
                        )
                    commentWithPhoto.myTTCommentAND.myIntTTWegNr?.let { wegNr ->
                        ttRoutes.value.forEach findRoute@{ routeWithComment ->
                            if (routeWithComment.ttRouteAND.intTTWegNr == wegNr) {
                                routeWithMyTTCommentANDWithPhotos.ttRouteAND =
                                    routeWithComment.ttRouteAND
                                return@findRoute
                            }
                        }
                    }
                    summitList.add(routeWithMyTTCommentANDWithPhotos)
                }
                summitList.sortWith(CompareComments)
                val addComment = Comments.AddComment
                summitList.add(addComment)
                Log.e(TAG, "queryMyTTCommentANDWithPhotos 4 - summitList.size = ${summitList.size}")
                _mMyTTCommentANDWithPhotos.value = summitList
                Log.e(
                    TAG,
                    "queryMyTTCommentANDWithPhotos 4 - _mMyTTCommentANDWithPhotos.value.size = ${_mMyTTCommentANDWithPhotos.value.size}"
                )
                _queriesRunning.apply { value = value?.minus(1) }
            }
        }
    }


    fun onClickShowMyComments() {
        _showMyComments.value = _showMyComments.value?.not() ?: false
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
        if ((ttNeigbours.value?.size?.compareTo(pos) ?: 0) > 0) {
            _navigateToSummitDetail.value = ttNeigbours.value?.get(pos)?.intTTNachbarGipfelNr
        }
    }

    private val _navigateToSummitGeo = MutableLiveData<Boolean>(false)
    val navigateToSummitGeo: LiveData<Boolean>
        get() = _navigateToSummitGeo

    fun onSummitGeoClick() {
        _navigateToSummitGeo.value = true
    }

    fun onSummitGeoClicked() {
        _navigateToSummitGeo.value = false
    }

    /**
     * Variable that tells the Fragment to navigate to a specific [SummitDetailResultFragment]
     *
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    private val _navigateToRouteDetail = MutableLiveData<Int?>(null)
    val navigateToRouteDetail: LiveData<Int?>
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

    // region Click on (my) comment or 'add comment'
    fun onClickComment(comments: Comments) {
        when (comments) {
            is Comments.AddComment ->
                _navigateToCommentInputFragment.value =
                    Comments.MyTTCommentANDWithPhotos(
                        MyTTCommentAND(
                            myIntTTGipfelNr = mTTSummit.value!!.intTTGipfelNr,
                            myIntTTWegNr = null
                        )
                    )
            is Comments.MyTTCommentANDWithPhotos -> {
                _navigateToCommentInputFragment.value =
                    comments
            }
            is Comments.RouteWithMyComment -> throw IllegalArgumentException("onClickComment(routeComments: RouteComments) -> ${comments.javaClass}")
            is Comments.TTCommentAND -> throw IllegalArgumentException("onClickComment(routeComments: RouteComments) -> ${comments.javaClass}")
            is Comments.RouteWithMyTTCommentANDWithPhotos -> throw java.lang.IllegalArgumentException("Parameter class 'Comments.RouteWithMyTTCommentANDWithPhotos' not expected")
        }
    }

    fun doneNavigationToCommentInputFragment() {
        _navigateToCommentInputFragment.value = null
    }
    //endregion

    // region Click on Image, initiate Image Fragment
    fun onClickImage(view: View) {
        _navigateToImageFragment.value = view
    }

    fun doneNavigationToCommentImageFragment() {
        _navigateToImageFragment.value = null
    }
    // endregion

    fun onChangeSortOrder(sortRouteWithMyRouteCommentBy: SortRouteWithMyRouteCommentBy) {
        _ttRoutes.value =
            _ttRoutes.value.let { it.sortRoutesBy(sortRouteWithMyRouteCommentBy) }
    }
}
