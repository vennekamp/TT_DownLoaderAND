package com.teufelsturm.tt_downloader_kotlin.feature.inputs.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import coil.load
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageClickListener
import com.synnapps.carouselview.ImageListener
import com.synnapps.carouselview.ViewListener
import com.teufelsturm.tt_downloader_kotlin.BuildConfig
import com.teufelsturm.tt_downloader_kotlin.R
import com.teufelsturm.tt_downloader_kotlin.databinding.CustomCarouselBinding
import com.teufelsturm.tt_downloader_kotlin.feature.inputs.vm.CarouselViewAdapterViewModel
import com.teufelsturm.tt_downloader_kotlin.feature.inputs.vm.CustomCarouselViewModel


private const val TAG = "CarouselViewAdapter"

class CarouselViewAdapter constructor(
    val context: Context,
    private val carouselViewAdapterViewModel: CarouselViewAdapterViewModel
) {
    private lateinit var carouselView: CarouselView

    companion object {
        const val ADD_IMAGE =
            "android.resource:// ${BuildConfig.APPLICATION_ID} /drawable/add_image"
    }

    private val _dialogMoveHelperText = MutableLiveData<String>("???")
    val dialogMoveHelperText: LiveData<String>
        get() = _dialogMoveHelperText


    fun deleteItem(selectedViewModel: CustomCarouselViewModel) {
        if (carouselViewAdapterViewModel.carouselAdapterData.getCarouselItemViewModels()
                .remove(selectedViewModel)
        ) {
            carouselViewAdapterViewModel.carouselAdapterData.deletedCarouselItemViewModels.add(
                selectedViewModel
            )
            carouselView.pageCount--
        }
    }

    fun bind(_carouselView: CarouselView): CarouselViewAdapter {
        carouselView = _carouselView
        carouselView.pageCount =
            carouselViewAdapterViewModel.carouselAdapterData.getCarouselItemViewModels().size
        carouselView.setViewListener(viewListener)
        carouselView.setImageClickListener(imageClickListener)
        return this
    }


    // To set custom views
    private val viewListener =
        ViewListener { position ->
            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val binding =
                CustomCarouselBinding.inflate(layoutInflater, null, false)
            val myImageView = binding.carouselImageView
            Log.e(
                TAG,
                "image Uri is: ${carouselViewAdapterViewModel.carouselAdapterData.getCarouselItemViewModels()[position].getImage()}"
            )
            val uriOrNull =
                carouselViewAdapterViewModel.carouselAdapterData.getCarouselItemViewModels()[position].getImage()
            uriOrNull?.let { uri ->
                if (uri.toString() == ADD_IMAGE) {
                    myImageView.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.add_image,
                            context.theme
                        )
                    )
                } else {
                    myImageView.load(uri) {
                        crossfade(true)
                        placeholder(R.drawable.add_image_wait)
                        error(R.drawable.added_image_not_found)
                    }
                }
            }
            binding.customCarouselViewModel =
                carouselViewAdapterViewModel.carouselAdapterData.getCarouselItemViewModels()[position]
            binding.root
        }

    // setImageResource(sampleImages[position]) w/o custom view
    private val imageListener =
        ImageListener { position, imageView ->
            Log.e(TAG, "imageView.setImageResource... position: $position")
            imageView.load(carouselViewAdapterViewModel.carouselAdapterData.getCarouselItemViewModels()[position].getImage())
        }

    // to receive touch events for each image
    private val imageClickListener = ImageClickListener { position ->
        carouselViewAdapterViewModel.onImageClick(position)
    }

//    private fun addCarouselItems() {
//        carouselViewAdapterViewModel.carouselAdapterData.addCustomCarouselViewModel(
//            CustomCarouselViewModel(
//                MyTT_RoutePhotos_AND(0,0,
//                    "content://com.android.externalstorage.documents/document/primary%3ADCIM%2F100ANDRO%2FDSC_0006.JPG",
//                "bla_02 bla_02 bla_02 bla_02 bla_02 bla_02 bla_02 bla_02 bla_02 bla_02 ")
//            )
//        )
//
//        carouselViewAdapterViewModel.carouselAdapterData.addCustomCarouselViewModel(
//            CustomCarouselViewModel (
//                MyTT_RoutePhotos_AND(0,0,
//                "content://com.android.externalstorage.documents/document/primary%3ADCIM%2F100ANDRO%2FDSC_0004.JPG",
//                "image02")
//            )
//        )
//
//        carouselViewAdapterViewModel.carouselAdapterData.addCustomCarouselViewModel(
//            CustomCarouselViewModel(
//                MyTT_RoutePhotos_AND(0,0,
//                "https://teufelsturm.de/img/fotos/pic0847.jpg",
//                "image03")
//            )
//        )
//
//        carouselViewAdapterViewModel.carouselAdapterData.addCustomCarouselViewModel(
//            CustomCarouselViewModel(
//                MyTT_RoutePhotos_AND(0,0,
//                "content://com.android.externalstorage.documents/document/primary%3ADCIM%2F100ANDRO%2FDSC_0031.JPG",
//                "image04")
//            )
//        )
//    }
}