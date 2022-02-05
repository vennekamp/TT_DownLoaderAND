package com.teufelsturm.tt_downloader_kotlin.feature.inputs.vm

import android.app.Application
import android.text.SpannableString
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teufelsturm.tt_downloader_kotlin.R
import com.teufelsturm.tt_downloader_kotlin.data.db.MyTTRouteDAO
import com.teufelsturm.tt_downloader_kotlin.data.db.NO_ID
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTTRouteAND
import com.teufelsturm.tt_downloader_kotlin.data.entity.RouteComments
import com.teufelsturm.tt_downloader_kotlin.feature.inputs.adapter.CarouselViewAdapter
import com.teufelsturm.tt_downloader_kotlin.feature.inputs.util.AscentCommentData
import com.teufelsturm.tt_downloader_kotlin.results.adapter.util.RouteAscentType
import com.teufelsturm.tt_downloader_kotlin.results.adapter.util.RouteAscentTypeOnItemSelected
import com.teufelsturm.tt_downloader_kotlin.searches.generics.AutoCompleteAdapter
import com.teufelsturm.tt_downloader_kotlin.searches.generics.ViewModelSpinnerSpannable
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


private const val TAG = "CommentInputViewModel"

@HiltViewModel
class CommentInputViewModel @Inject constructor(
    val application: Application,
    private val myTTRouteDAO: MyTTRouteDAO,
) : ViewModel() {


    // region properties
    private val _partnerAdapter = MutableLiveData<ArrayAdapter<String>>()
    val partnerAdapter: LiveData<ArrayAdapter<String>>
        get() = _partnerAdapter
    private val _showDateDialog: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>(null)
    val showDateDialog: LiveData<Boolean?>
        get() = _showDateDialog
    private val _showTimeDialog: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>(null)
    val showTimeDialog: LiveData<Boolean?>
        get() = _showTimeDialog
    private val _deleteComment: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val deleteComment: LiveData<Boolean>
        get() = _deleteComment
    // endregion
    // region ViewModel and Adapter and DataHolder
    /** Returns a search result for the given partner start title. **/
    private fun findPartner(partNamePart: String): List<String> {
        return myTTRouteDAO.getDistinctPartner(partNamePart)
    }

    val adapter: ArrayAdapter<SpannableString> = ArrayAdapter(
        application.applicationContext,
        R.layout.listitem_spinner_how_ascended,
        RouteAscentType.getArrayOfRouteAscentTypes(application.applicationContext)
    )

    init {
        adapter.setDropDownViewResource(R.layout.listitem_spinner_how_ascended) /* simple_spinner_dropdown_item */
        _partnerAdapter.value =
            AutoCompleteAdapter(application, ::findPartner, R.id.editTextSuchtextWege)
    }

    // Creating adapter for the ascent type spinner
    val spinnerHowAscended = ViewModelSpinnerSpannable(adapter)

    /* Here all data are stored */
    val ascentData = AscentCommentData()
    // endregion

    private var clickListenerAscentType: RouteAscentTypeOnItemSelected? = null
    fun setOnClickListener(
        onClickListenerAscendType: RouteAscentTypeOnItemSelected
    ) {
        this.clickListenerAscentType = onClickListenerAscendType
    }

    fun getMyTTRouteANDId(): Long {
        return ascentData.myTTRouteANDWithPhotos.myTTRouteAND.Id
    }

    fun setMyTTRouteANDWithPhotos(mMyTTRouteANDWithPhotos: RouteComments.MyTTRouteANDWithPhotos) {

        ascentData.setMyTTRouteANDWithPhotos(mMyTTRouteANDWithPhotos)
        spinnerHowAscended.onItemSelected(
            null,
            ascentData.myTTRouteANDWithPhotos.myTTRouteAND.isAscendedRouteType
        )

    }

    fun onShowDateDialog() {
        _showDateDialog.value = true
    }

    fun onShowDateDialogFinished() {
        _showDateDialog.value = false
    }

    fun onShowTimeWidget() {
        _showTimeDialog.value = true
    }

    fun onShowTimeWidgetFinished() {
        _showTimeDialog.value = false
    }

    fun onDeleteComment() {
        _deleteComment.value = true
    }

    fun onDeletedComment() {
        _deleteComment.value = false
    }

    fun saveModifiedComment(
        myTTRouteAND: MyTTRouteAND,
        carouselItemViewModels: MutableList<CustomCarouselViewModel>,
        deletedCarouselItemViewModels: MutableList<CustomCarouselViewModel>
    ) {
        val rowIDRoute: Long = if (
            myTTRouteAND.isAscendedRouteType == 0
            && myTTRouteAND.myIntDateOfAscendRoute.isNullOrBlank()
            && myTTRouteAND.myAscendedPartner.isNullOrBlank()
            && myTTRouteAND.strMyRouteComment.isNullOrBlank()
            && carouselItemViewModels.size == 1
        ) {
            val deletedRows = myTTRouteDAO.deleteMyCommentById(myTTRouteAND.Id)
            val msg = if (deletedRows > 0) "Kommentar gelöscht" else "Löschen gescheitert!"
            Toast.makeText(
                application.applicationContext,
                msg,
                Toast.LENGTH_LONG
            ).show()
            myTTRouteAND.Id
        } else {
            saveMyRoute(myTTRouteAND)
        }
        Log.v(
            TAG,
            "saveModifiedComment(...myTTRouteAND for ${myTTRouteAND.myIntTTWegNr} in row -> $rowIDRoute"
        )
        carouselItemViewModels.forEach {
            saveMyPhoto(it, rowIDRoute, myTTRouteAND)
        }
        deletedCarouselItemViewModels.forEach {
            myTTRouteDAO.deletePhotoById(it.getMyTT_RoutePhotos_AND().Id)
        }
    }

    private fun saveMyPhoto(
        it: CustomCarouselViewModel,
        rowIDRoute: Long,
        myTTRouteAND: MyTTRouteAND
    ) {
        if (it.getMyTT_RoutePhotos_AND().uri != CarouselViewAdapter.ADD_IMAGE) {
            val commentPhoto = it.getMyTT_RoutePhotos_AND()
            commentPhoto.commentID = rowIDRoute

            val rowIDComment: Long = if (commentPhoto.Id == NO_ID) {
                Log.e(
                    TAG,
                    "saveModifiedComment(...commentPhoto for ${myTTRouteAND.myIntTTWegNr} insert"
                )
                myTTRouteDAO.insert(commentPhoto)
            } else {
                Log.e(
                    TAG,
                    "saveModifiedComment(...commentPhoto for ${myTTRouteAND.myIntTTWegNr} update"
                )
                myTTRouteDAO.update(commentPhoto)
                commentPhoto.Id
            }
            Log.e(
                TAG,
                "saveModifiedComment(...commentPhoto for ${commentPhoto.caption} in row -> $rowIDComment"
            )
        }
    }

    private fun saveMyRoute(myTTRouteAND: MyTTRouteAND) =
        if (myTTRouteAND.Id == NO_ID) {
            Log.e(
                TAG,
                "saveModifiedComment(...myTTRouteAND for ${myTTRouteAND.myIntTTWegNr} insert"
            )
            myTTRouteDAO.insert(myTTRouteAND)
        } else {
            Log.e(
                TAG,
                "saveModifiedComment(...myTTRouteAND for ${myTTRouteAND.myIntTTWegNr} update"
            )
            myTTRouteDAO.update(myTTRouteAND)
            myTTRouteAND.Id
        }
}