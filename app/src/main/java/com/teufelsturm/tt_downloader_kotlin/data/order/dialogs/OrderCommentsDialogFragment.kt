package com.teufelsturm.tt_downloader_kotlin.data.order.dialogs

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.teufelsturm.tt_downloader_kotlin.R
import com.teufelsturm.tt_downloader_kotlin.databinding.DialogCommentOrderBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderCommentsDialogFragment(val viewModel: ViewModel4CommentOrder) : DialogFragment() {

    private lateinit var binding: DialogCommentOrderBinding
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.MyDialogAnimation
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mActivity: Activity = requireActivity()
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            mActivity.layoutInflater,
            R.layout.dialog_comment_order,
            null,
            false
        )

        binding.viewModel4CommentOrder = viewModel
        val builder = AlertDialog.Builder(mActivity)
        // Get the layout inflated by the binding
        builder.setView(binding.root)
        val thisDialog = builder.create()

        val wmlp = thisDialog.window!!.attributes
        // set the gravity
        wmlp.gravity = Gravity.END.or(Gravity.TOP)
        // set the margin
        wmlp.horizontalMargin = 0f
        // margin top below actionbar
        wmlp.verticalMargin = 0f

        return thisDialog
    }
}