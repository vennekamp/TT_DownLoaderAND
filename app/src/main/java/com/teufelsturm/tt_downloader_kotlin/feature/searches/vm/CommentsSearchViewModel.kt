package com.teufelsturm.tt_downloader_kotlin.feature.searches.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.teufelsturm.tt_downloader_kotlin.R
import com.teufelsturm.tt_downloader_kotlin.data.db.TTCommentDAO
import com.teufelsturm.tt_downloader_kotlin.data.db.TTSummitDAO
import com.teufelsturm.tt_downloader_kotlin.feature.searches.generics.*
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.RouteGrade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "CommentsSearchVM"

@HiltViewModel
class CommentsSearchViewModel @Inject constructor(
    application: Application,
    val ttSummitDAO: TTSummitDAO,
    val ttCommentDAO: TTCommentDAO
) :
    AndroidViewModel(application) {
    val searchTextVM = ViewModelEditText("", viewModelScope)
    val commentCount = MutableLiveData<Int?>(null)

    val actionBarString = Transformations.map(commentCount) { routeCount ->
        formatItemCount4Button(
            routeCount,
            application.applicationContext.resources.getString(R.string.strSearchComment)
        )
    }

    val spinnerAreaComment = ViewModelSpinner(
        ttSummitDAO.getDistictAreaNames()
    )

    val rangeSliderMinMaxGradeInCommentSearch = ViewModelRangeSlider(
        application.applicationContext.resources.getString(R.string.strLimitForScale),
        RouteGrade.getMaxOrdinal().toFloat(), RouteGrade.float2Grade
    )

    val rangeSliderMinMaxRatingInComment = ViewModelRangeSlider(
        application.applicationContext.resources.getString(R.string.strMinGradingInComment),
        6f, float2Rating
    )

    private fun upDateCommentCount(): Flow<Int> {
        val partialComment = "%${searchTextVM.searchText.value}%"
        val searchAreas = spinnerAreaComment.selectedItem.value ?: ""
        val _lowerLimit = rangeSliderMinMaxGradeInCommentSearch.values.value?.get(0)?.toInt()
        val _upperLimit = rangeSliderMinMaxGradeInCommentSearch.values.value?.get(1)?.toInt()
        val intMinSchwierigkeit =
            RouteGrade.getRouteGradeByOrdinal(_lowerLimit) ?: RouteGrade.getMinOrdinal()
        val intMaxSchwierigkeit =
            RouteGrade.getRouteGradeByOrdinal(_upperLimit) ?: RouteGrade.getMaxOrdinal()
        val minEntryBewertung =
            rangeSliderMinMaxRatingInComment.values.value?.get(0)?.toInt() ?: 0
        val maxEntryBewertung =
            rangeSliderMinMaxRatingInComment.values.value?.get(1)?.toInt() ?: 6
        return when {
            (partialComment.isBlank() && searchAreas.isBlank()) -> ttCommentDAO.getCommentsConstrainedCount(
                minEntryBewertung,
                maxEntryBewertung,
                intMinSchwierigkeit,
                intMaxSchwierigkeit
            )
            (partialComment.length < 3) -> ttCommentDAO.getCommentsConstrainedCount(
                minEntryBewertung,
                maxEntryBewertung,
                searchAreas,
                intMinSchwierigkeit,
                intMaxSchwierigkeit
            )
            else -> ttCommentDAO.getCommentsConstrainedCount(
                minEntryBewertung,
                maxEntryBewertung,
                partialComment,
                searchAreas,
                intMinSchwierigkeit,
                intMaxSchwierigkeit
            )
        }
    }

    private val _eventSearchComment = MutableLiveData<Boolean>()
    val eventSearchComment: LiveData<Boolean>
        get() = _eventSearchComment

    /** Method for the search summit event **/

    fun onSearchCommentComplete() {
        _eventSearchComment.value = false
    }

    fun onSearchComment() {
        Log.i(TAG, "onSearchComment()")
        _eventSearchComment.value = true
    }

    private var lastRequestTime = 0L
    @FlowPreview
    fun refreshCommentCount() {
        Log.i(TAG, "refreshCommentCount()")
        lastRequestTime = System.currentTimeMillis()
        viewModelScope.launch {
            commentCount.value = null
            delay(300)      // debounce time
            if (System.currentTimeMillis() - lastRequestTime < 300) return@launch
            else
            upDateCommentCount().debounce(300L).collect{
                    commentCount.value = it
                }
        }
    }

    init {
        refreshCommentCount()
    }
}
