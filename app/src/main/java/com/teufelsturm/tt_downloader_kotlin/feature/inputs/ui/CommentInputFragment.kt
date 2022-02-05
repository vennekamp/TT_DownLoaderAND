package com.teufelsturm.tt_downloader_kotlin.feature.inputs.ui

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import com.teufelsturm.tt_downloader_kotlin.R
import com.teufelsturm.tt_downloader_kotlin.app.MainActivity
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTT_RoutePhotos_AND
import com.teufelsturm.tt_downloader_kotlin.data.entity.RouteComments
import com.teufelsturm.tt_downloader_kotlin.databinding.InputMyCommentBinding
import com.teufelsturm.tt_downloader_kotlin.feature.inputs.adapter.CarouselViewAdapter
import com.teufelsturm.tt_downloader_kotlin.feature.inputs.util.FieldValidators
import com.teufelsturm.tt_downloader_kotlin.feature.inputs.vm.CarouselViewAdapterViewModel
import com.teufelsturm.tt_downloader_kotlin.feature.inputs.vm.CommentInputViewModel
import com.teufelsturm.tt_downloader_kotlin.feature.inputs.vm.CustomCarouselViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.*

private const val TAG = "CommentInputFrag"

@AndroidEntryPoint
class CommentInputFragment : Fragment() {

    private lateinit var binding: InputMyCommentBinding

    private lateinit var carouselViewAdapter: CarouselViewAdapter

    private var dialogDeleteOrOpen: DialogDeleteOrOpen? = null

    private val viewModelComment by viewModels<CommentInputViewModel>()
    private val viewModelCarousel by viewModels<CarouselViewAdapterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // see https://developer.android.com/guide/navigation/navigation-custom-back
        // This callback will only be called when MyFragment is at least Started.
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
            viewModelComment.saveModifiedComment(
                viewModelComment.ascentData.myTTRouteANDWithPhotos.myTTRouteAND,
                viewModelCarousel.carouselAdapterData.getCarouselItemViewModels(),
                viewModelCarousel.carouselAdapterData.deletedCarouselItemViewModels

            )
            this.remove()
            requireActivity().supportFragmentManager.popBackStack()
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.input_my_comment,
            container,
            false
        )

        binding.viewModel4CommentInut = viewModelComment
        binding.viewModel4Carousel = viewModelCarousel
        carouselViewAdapter =
            CarouselViewAdapter(
                requireContext(),
                viewModelCarousel
            ).bind(binding.carouselView)

        val toolBar = (requireActivity() as MainActivity).supportActionBar
        arguments?.let { itBundle ->
            val args = CommentInputFragmentArgs.fromBundle(itBundle)
            val commentData = RouteComments.MyTTRouteANDWithPhotos(
                args.mMyTTRouteAnd,
                args.lstMyTTRoutePhotosAND.toMutableList()
            )
            viewModelComment.setMyTTRouteANDWithPhotos(commentData)
            viewModelCarousel.setMyTTRouteANDWithPhotos(commentData)

            toolBar?.let {
                it.setHomeAsUpIndicator(
                    android.R.drawable.ic_delete
                )
                it.title = args.myRouteName
            }
        }
        binding.carouselView.pageCount =
            viewModelCarousel.carouselAdapterData.getCarouselItemViewModels().size

        addCarouselPageListener()
        setHasOptionsMenu(true)
        createObservers()
        return binding.root
    }

    private fun addCarouselPageListener() {
        binding.carouselView.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                viewModelCarousel.carouselAdapterData.onCarouselPageSelected(position)
            }

            override fun onPageSelected(position: Int) {
                // Check if this is the page you want.
            }
        })
    }

    private fun createObservers() {
        viewModelComment.showDateDialog.observe(viewLifecycleOwner, {
            if (it != true) return@observe
            val startDate = viewModelComment.ascentData.getAscentDateObject()
                ?: MaterialDatePicker.todayInUtcMilliseconds()
            val dialog = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Datum")
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setSelection(startDate)
                .build()
            dialog.addOnPositiveButtonClickListener { selection: Long ->
                viewModelComment.ascentData.setAscentDateObject(selection)
            }
            dialog.show(childFragmentManager, "myTimeDialog")
            viewModelComment.onShowDateDialogFinished()
        })
        viewModelComment.showTimeDialog.observe(viewLifecycleOwner, {
            if (it != true) return@observe
            val cal = Calendar.getInstance()
            val dialog = MaterialTimePicker.Builder()
                .setTitleText("Uhrzeit")
                .setTimeFormat(CLOCK_24H)
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .setHour(cal.get(Calendar.HOUR_OF_DAY))
                .setMinute(cal.get(Calendar.MINUTE))
                .build()
            dialog.addOnPositiveButtonClickListener {
                viewModelComment.ascentData.setAscentTime(dialog.hour, dialog.minute)
            }
            dialog.show(childFragmentManager, "myTimeDialog")
            viewModelComment.onShowTimeWidgetFinished()
        })

        viewModelComment.deleteComment.observe(viewLifecycleOwner, {
            if (it != true) return@observe
            viewModelComment.ascentData.setAscentDate(null)
            viewModelComment.ascentData.setAscentPartner(null)
            viewModelComment.ascentData.setAscentComment(null)
            binding.spinnerRouteAsscendedInCommentRoute.post {
                binding.spinnerRouteAsscendedInCommentRoute.setSelection(0)
            }

            viewModelCarousel.carouselAdapterData.deletedCarouselItemViewModels.addAll(
                viewModelCarousel.carouselAdapterData.getCarouselItemViewModels().filter {
                    it.getImage().toString() != CarouselViewAdapter.ADD_IMAGE
                })
            viewModelCarousel.carouselAdapterData.getCarouselItemViewModels().removeAll(
                viewModelCarousel.carouselAdapterData.deletedCarouselItemViewModels
            )
            binding.carouselView.pageCount = 1
            viewModelComment.onDeletedComment()
        })

        binding.edtDateOfAscend.addTextChangedListener(
            TextFieldValidation(
                binding.edtDateOfAscend
            )
        )

        viewModelCarousel.carouselImagesChange.observe(viewLifecycleOwner, {
            if (it) {
                binding.carouselView.pageCount =
                    binding.carouselView.pageCount // reset the carouselView
                val currentItem = viewModelCarousel.carouselAdapterData.getPosition()
                binding.carouselView.setCurrentItem(currentItem, true)
                viewModelCarousel.onCarouselImagesChanged()
            }
        })

        viewModelComment.spinnerHowAscended.selected.observe(viewLifecycleOwner, {
            binding.spinnerRouteAsscendedEditTextStub.requestFocus()
            viewModelComment.ascentData.myTTRouteANDWithPhotos.myTTRouteAND.isAscendedRouteType = it
        })

        viewModelCarousel.carouselImageDelete.observe(viewLifecycleOwner, { deleteItem ->
            deleteItem?.let {
                carouselViewAdapter.deleteItem(it)
                dialogDeleteOrOpen?.dismiss()
            }
            viewModelCarousel.onItemDeleted()
        })

        viewModelCarousel.carouselImageShow.observe(viewLifecycleOwner, { carouselViewModel ->

            carouselViewModel?.getImage()?.let { uri ->
                openImage(uri)

                viewModelCarousel.onItemShown()
            }
        })


        viewModelCarousel.imagedClicked.observe(viewLifecycleOwner, { nullOrUri ->
            nullOrUri?.let { uri ->
                if (uri.toString() == CarouselViewAdapter.ADD_IMAGE) {
                    Toast.makeText(
                        context,
                        "Bilder auswählen...",
                        Toast.LENGTH_LONG
                    ).show()
                    filesChooserContract.launch(arrayOf("image/*"))
                } else {
                    dialogDeleteOrOpen = DialogDeleteOrOpen(viewModelCarousel)
                    dialogDeleteOrOpen?.show(
                        requireActivity().supportFragmentManager,
                        "dialog_delete_or_open"
                    )
                }
                viewModelCarousel.onImageClickHandled()
            }
        })
    }

    private fun openImage(uri: Uri) {
        val viewIntent = Intent(Intent.ACTION_VIEW)
        viewIntent.setDataAndType(uri, "image/*")
        viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val editIntent = Intent(Intent.ACTION_EDIT)
        editIntent.setDataAndType(uri, "image/*")
        editIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val chooserIntent =
            Intent.createChooser(viewIntent, requireContext().getString(R.string.open_with))
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(editIntent))
        startActivity(chooserIntent)
    }

    // https://commonsware.com/blog/2020/08/08/uri-access-lifetime-still-shorter-than-you-might-think.html
    private val filesChooserContract =
        registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uriList ->
            for (uri in uriList) {
                requireContext().contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                viewModelCarousel.carouselAdapterData.addCustomCarouselViewModel(
                    CustomCarouselViewModel(
                        MyTT_RoutePhotos_AND(
                            0L, viewModelComment.getMyTTRouteANDId(),
                            uri.toString(),
                            requireContext().getFileName(uri)
                        )
                    ), true
                )
                binding.carouselView.pageCount++
            }
        }

    fun Context.getFileName(uri: Uri): String? = when (uri.scheme) {
        ContentResolver.SCHEME_CONTENT -> getContentFileName(uri)
        else -> uri.path?.let(::File)?.name
    }

    private fun Context.getContentFileName(uri: Uri): String? = runCatching {
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            cursor.moveToFirst()
            return@use cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                .let(cursor::getString)
        }
    }.getOrNull()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.comment_input, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.comment_input_menu -> {
                viewModelComment.saveModifiedComment(
                    viewModelComment.ascentData.myTTRouteANDWithPhotos.myTTRouteAND,
                    viewModelCarousel.carouselAdapterData.getCarouselItemViewModels(),
                    viewModelCarousel.carouselAdapterData.deletedCarouselItemViewModels
                )
                requireActivity().supportFragmentManager.popBackStack()
                true
            }
            R.id.comment_edit_image -> {
                val pos = viewModelCarousel.carouselAdapterData.getPosition()
                if (pos != -1) {
                    viewModelCarousel.onImageClick(pos)
                } else {
                    val msg = "clicked image not found!! "
                    Log.e(TAG, msg)
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT)
                    val fallbackPos =
                        viewModelCarousel.carouselAdapterData.getCarouselItemViewModels().size - 1
                    viewModelCarousel.onImageClick(fallbackPos)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    /**
     * applying text watcher on each text field
     */
    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // checking ids of each text field and applying functions accordingly.
            when (view) {
                binding.edtDateOfAscend -> {
                    binding.edtDateOfAscend.apply {
                        val mColor = if (FieldValidators.isValidDate(this.text.toString())) {
                            R.color.Black
                        } else {
                            R.color.Red
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            binding.tilDateOfAscend.setHelperTextColor(
                                resources.getColorStateList(
                                    mColor,
                                    null
                                )
                            )
                        } else {
                            binding.tilDateOfAscend.setHelperTextColor(
                                AppCompatResources.getColorStateList(
                                    context,
                                    mColor
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}