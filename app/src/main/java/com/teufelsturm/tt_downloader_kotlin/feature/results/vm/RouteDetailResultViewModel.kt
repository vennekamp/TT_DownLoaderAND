package com.teufelsturm.tt_downloader_kotlin.feature.results.vm

import android.app.Application
import android.text.Spannable
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teufelsturm.tt_downloader_kotlin.R
import com.teufelsturm.tt_downloader_kotlin.data.db.MyTTRouteDAO
import com.teufelsturm.tt_downloader_kotlin.data.db.TTCommentDAO
import com.teufelsturm.tt_downloader_kotlin.data.db.TTRouteDAO
import com.teufelsturm.tt_downloader_kotlin.data.db.TTSummitDAO
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTTRouteAND
import com.teufelsturm.tt_downloader_kotlin.data.entity.RouteComments
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTSummitAND
import com.teufelsturm.tt_downloader_kotlin.data.order.SortCommentsWithRouteWithSummitBy
import com.teufelsturm.tt_downloader_kotlin.data.order.sortCommentsBy
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.RouteAscentType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "RouteDetailResultVM"

@HiltViewModel
class RouteDetailResultViewModel @Inject constructor(
    application: Application,
    private val ttCommentDAO: TTCommentDAO,
    private val ttRouteDAO: TTRouteDAO,
    private val ttSummitDAO: TTSummitDAO,
    private val myTTRouteDAO: MyTTRouteDAO
) : ViewModel() {

    val adapter: ArrayAdapter<Spannable> = ArrayAdapter(
        application.applicationContext,
        R.layout.listitem_spinner_how_ascended,
        RouteAscentType.getArrayOfRouteAscentTypes(application.applicationContext)
    )

    init {
        adapter.setDropDownViewResource(R.layout.listitem_spinner_how_ascended) /* simple_spinner_dropdown_item */
    }

    private val _showMyComments: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>(null)
    val showMyComments: LiveData<Boolean?>
        get() = _showMyComments

    private val _queriesRunning: MutableLiveData<Int> = MutableLiveData<Int>(3)
    val queriesRunning: LiveData<Int>
        get() = _queriesRunning

//    // Creating adapter for spinner
//    val spinnerHowAscended = ViewModelSpinnerSpannable(adapter)

    private val _mTTRouteComments: MutableLiveData<List<RouteComments.TTRouteCommentAND>> =
        MutableLiveData<List<RouteComments.TTRouteCommentAND>>()
    val mTTRouteComments: LiveData<List<RouteComments.TTRouteCommentAND>>
        get() = _mTTRouteComments

    private val _mMyTTRouteANDWithPhotos: MutableLiveData<List<RouteComments.MyTTRouteANDWithPhotos>> =
        MutableLiveData<List<RouteComments.MyTTRouteANDWithPhotos>>()
    val mMyTTRouteANDWithPhotos: LiveData<List<RouteComments.MyTTRouteANDWithPhotos>>
        get() = _mMyTTRouteANDWithPhotos

    private val _mTTRouteAND: MutableLiveData<RouteComments.RouteWithMyRouteComment> =
        MutableLiveData()
    val mTTRouteAND: LiveData<RouteComments.RouteWithMyRouteComment>
        get() = _mTTRouteAND

    private val _mTTSummitAND: MutableLiveData<TTSummitAND> = MutableLiveData()
    val mTTSummitAND: LiveData<TTSummitAND>
        get() = _mTTSummitAND

    private val _navigateToCommentInputFragment: MutableLiveData<RouteComments.MyTTRouteANDWithPhotos?> =
        MutableLiveData()
    val navigateToCommentInputFragment: LiveData<RouteComments.MyTTRouteANDWithPhotos?>
        get() = _navigateToCommentInputFragment

    private val _navigateToImageFragment: MutableLiveData<View?> =
        MutableLiveData()
    val navigateToImageFragment: LiveData<View?>
        get() = _navigateToImageFragment

    fun queryData(intTTWegNr: Int, intTTGipfelNr: Int) {
        _queriesRunning.value = 3
        viewModelScope.launch {
            queryTTCommentsAsync(intTTWegNr)
        }
        viewModelScope.launch {
            queryTTRouteANDAsync(intTTWegNr)
        }
        viewModelScope.launch {
            queryMYTTRouteWithPhotoAsync(intTTWegNr)
        }

        viewModelScope.launch {
            queryTTSummitAndAsync(intTTGipfelNr)
        }
    }

    private suspend fun queryTTSummitAndAsync(intTTGipfelNr: Int) {
        // mTTSummitAND =
        ttSummitDAO.get(intTTGipfelNr).collect {
            Log.i(TAG, "ttSummitDAO.get(intTTGipfelNr).collect + ${it.strName}")
            _mTTSummitAND.value = it
            _queriesRunning.apply { value = value?.minus(1) }
            Log.i(TAG, "_queriesRunning.value: ${_queriesRunning.value}")
        }
    }

    private suspend fun queryTTRouteANDAsync(intTTWegNr: Int) {
        // mTTRouteAND =
        ttRouteDAO.getRouteWithMySummitCommentByRoute(intTTWegNr).collect {
            Log.i(TAG, "ttRouteDAO.getByRouteId(intTTWegNr).collect + ${it.ttRouteAND.WegName}")
            _mTTRouteAND.value = it
            _queriesRunning.apply { value = value?.minus(1) }
            Log.i(TAG, "_queriesRunning.value: ${_queriesRunning.value}")
        }
    }

    private suspend fun queryMYTTRouteWithPhotoAsync(intTTWegNr: Int) {
        // mMyTTRouteANDWithPhotos =
        myTTRouteDAO.getCommentWithPhoto(intTTWegNr).collect {
            Log.v(TAG, "ttRouteDAO.getCommentWithPhoto(intTTWegNr).collect ${it.size}")
            _mMyTTRouteANDWithPhotos.value = it
        }
    }

    private suspend fun queryTTCommentsAsync(intTTWegNr: Int) {
        // mTTComments = List of comments
        ttCommentDAO.getByRoute(intTTWegNr).collect {
            Log.i(TAG, "ttCommentDAO.getByRoute(intTTWegNr).collect {+ ${it.size}")
            _mTTRouteComments.value = it
            _queriesRunning.apply { value = value?.minus(1) }
            Log.i(TAG, "_queriesRunning.value: ${_queriesRunning.value}")
        }
    }

    fun onChangeSortOrder(sortCommentsWithRouteWithSummitBy: SortCommentsWithRouteWithSummitBy) {
        _mTTRouteComments.value?.let {
            _mTTRouteComments.value =
                it.sortCommentsBy(
                    sortCommentsWithRouteWithSummitBy
                )
        }
    }

    fun onClickShowMyComments() {
        _showMyComments.value = _showMyComments.value?.not() ?: true
    }


    fun onClickComment(routeComments: RouteComments) {
        when (routeComments) {
            is RouteComments.AddComment -> _navigateToCommentInputFragment.value =
                RouteComments.MyTTRouteANDWithPhotos(
                    MyTTRouteAND(myIntTTWegNr = mTTRouteAND.value!!.ttRouteAND.intTTWegNr)
                )
            is RouteComments.MyTTRouteANDWithPhotos -> _navigateToCommentInputFragment.value =
                routeComments
            is RouteComments.RouteWithMyRouteComment -> throw IllegalArgumentException("onClickComment(routeComments: RouteComments) -> ${routeComments.javaClass}")
            is RouteComments.TTRouteCommentAND -> throw IllegalArgumentException("onClickComment(routeComments: RouteComments) -> ${routeComments.javaClass}")
        }
    }

    fun doneNavigationToCommentInputFragment() {
        _navigateToCommentInputFragment.value = null
    }

    fun onClickImage(view: View) {
        _navigateToImageFragment.value = view
    }

    fun doneNavigationToCommentImageFragment() {
        _navigateToImageFragment.value = null
    }

}