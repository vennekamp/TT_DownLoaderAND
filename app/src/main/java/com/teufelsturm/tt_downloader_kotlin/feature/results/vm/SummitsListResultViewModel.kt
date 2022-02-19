package com.teufelsturm.tt_downloader_kotlin.feature.results.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.teufelsturm.tt_downloader_kotlin.data.db.TTSummitDAO
import com.teufelsturm.tt_downloader_kotlin.data.entity.CommentsSummit
import com.teufelsturm.tt_downloader_kotlin.data.order.SortSummitWithMySummitCommentBy
import com.teufelsturm.tt_downloader_kotlin.data.order.sortRoutesBy
import com.teufelsturm.tt_downloader_kotlin.feature.results.ui.SummitsListResultFragmentArgs
import com.teufelsturm.tt_downloader_kotlin.feature.results.vm.generics.ViewModelSummitOrderWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SummitsListResultVM"

@HiltViewModel
class SummitsListResultViewModel @Inject constructor(
    application: Application,
    private val ttSummitDAO: TTSummitDAO
) :
    AndroidViewModel(application) {

    private var _ttSummits: MutableLiveData<List<CommentsSummit.SummitWithMySummitComment>> =
        MutableLiveData()
    val ttSummits: LiveData<List<CommentsSummit.SummitWithMySummitComment>>
        get() = _ttSummits
    private val _queryRunning: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val queryRunning: LiveData<Boolean>
        get() = _queryRunning

    val viewModelSummitOrderWidget = ViewModelSummitOrderWidget()

    fun querySummits(args: SummitsListResultFragmentArgs) {
        _queryRunning.value = true
        viewModelScope.launch { querySummitsAsync(args) }
    }

    private suspend fun querySummitsAsync(
        args: SummitsListResultFragmentArgs
    ) {
        with(args.argSearchSummitParameter) {
            // ttSummits = // ttSummitDAO.getSummitWithMySummitComment() /*
            if (just_my_summit) {
                ttSummitDAO.loadConstrainedSummitsAndMyCommentsJustMine(
                    minAnzahlWege = minAnzahlWege,
                    maxAnzahlWege = maxAnzahlWege,
                    minAnzahlSternchenWege = minAnzahlSternchenWege,
                    maxAnzahlSternchenWege = maxAnzahlSternchenWege,
                    searchAreas = searchAreas,
                    searchText = searchText
                ).collect {
                    _ttSummits.value =
                        it.sortRoutesBy(viewModelSummitOrderWidget.sortSummitBy.value)
                    _queryRunning.value = false
                }
            } else {
                ttSummitDAO.loadConstrainedSummitsAndMyComments(
                    minAnzahlWege = minAnzahlWege,
                    maxAnzahlWege = maxAnzahlWege,
                    minAnzahlSternchenWege = minAnzahlSternchenWege,
                    maxAnzahlSternchenWege = maxAnzahlSternchenWege,
                    searchAreas = searchAreas,
                    searchText = searchText
                ).collect {
                    _ttSummits.value =
                        it.sortRoutesBy(viewModelSummitOrderWidget.sortSummitBy.value)
                    _queryRunning.value = false
                }
            }
        }
    }

    /**
     * Variable that tells the Fragment to navigate to a specific [SummitDetailResultFragment]
     *
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    private val _navigateToSummitDetail = MutableLiveData<Int?>()
    val navigateToSummitDetail: LiveData<Int?>
        get() = _navigateToSummitDetail

    /**
     * Call this immediately after navigating to [SummitDetailResultFragment]
     *
     * It will clear the navigation request, so if the user rotates their phone it won't navigate
     * twice.
     */
    fun doneNavigating() {
        _navigateToSummitDetail.value = null
    }

    fun onClickSummit(summitId: Int) {
        _navigateToSummitDetail.value = summitId
    }

    fun onChangeSortOrder(sortSummitWithMySummitCommentBy: SortSummitWithMySummitCommentBy) {
        _ttSummits.value =
            _ttSummits.value?.let { _ttSummits.value!!.sortRoutesBy(sortSummitWithMySummitCommentBy) }
    }
}
