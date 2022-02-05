package com.teufelsturm.tt_downloader_kotlin.feature.inputs.ui

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.teufelsturm.tt_downloader_kotlin.R
import com.teufelsturm.tt_downloader_kotlin.databinding.DialogDeleteOrEditBinding
import com.teufelsturm.tt_downloader_kotlin.feature.inputs.vm.CarouselViewAdapterViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DialogDeleteOrOpen(val carouselViewAdapterViewModel: CarouselViewAdapterViewModel) :
    DialogFragment() {

    private lateinit var binding: DialogDeleteOrEditBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mActivity: Activity = requireActivity()
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            mActivity.layoutInflater,
            R.layout.dialog_delete_or_edit,
            null,
            false
        )
        val builder = AlertDialog.Builder(mActivity)
        // Get the layout inflated by the binding
        builder.setView(binding.root)
        val thisDialog = builder.create()

        val wmlp = thisDialog.window!!.attributes
        // set the gravity
        wmlp.gravity = Gravity.END.or(Gravity.START).or(Gravity.BOTTOM)
        // set the margin
        wmlp.horizontalMargin = 0f
        // margin top below actionbar
        wmlp.verticalMargin = 0f


        binding.viewModel4Carousel = carouselViewAdapterViewModel
        return thisDialog
    }
}