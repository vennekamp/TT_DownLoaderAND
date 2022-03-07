package com.teufelsturm.tt_downloader_kotlin.feature.results.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.teufelsturm.tt_downloader_kotlin.R
import com.teufelsturm.tt_downloader_kotlin.data.entity.Comments
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTTCommentPhotosAND
import com.teufelsturm.tt_downloader_kotlin.databinding.ResultSummitDetailBinding
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.SummitDetailAdapter
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.CommentImageClickListener
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.RouteCommentsClickListener
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.TTRouteClickListener
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.toHTMLSpan
import com.teufelsturm.tt_downloader_kotlin.feature.results.vm.SummitDetailResultViewModel
import com.teufelsturm.tt_downloader_kotlin.feature.searches.generics.EventNavigatingToRoute
import com.teufelsturm.tt_downloader_kotlin.feature.searches.generics.EventNavigatingToSummit
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val TAG = "SummitDetailResultFrag"

@AndroidEntryPoint
class SummitDetailResultFragment : Fragment() {

    var summitDetailAdapter: SummitDetailAdapter = SummitDetailAdapter()
    // var summitCommentDetailAdapter: SummitCommentDetailAdapter = SummitCommentDetailAdapter()

    private lateinit var binding: ResultSummitDetailBinding

    private val viewModel by viewModels<SummitDetailResultViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.result_summit_detail,
            container,
            false
        )
        Log.i(TAG, "Called onCreateView()")

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner


        // Retrieve the search arguments from the Bundle and initiate the query
        val args = SummitDetailResultFragmentArgs.fromBundle(requireArguments())
        viewModel.queryData(args.argNavigatingToSummit.intTTSummitNr)

        // Set the viewmodel for databinding - this allows the bound layout access
        // to all the data in the ViewModel
        binding.viewModel4SummitResult = viewModel

        binding.listRouteFound.adapter = summitDetailAdapter
        summitDetailAdapter.setOnClickListener(TTRouteClickListener { routeId, _ ->
            // Toast.makeText(context, "Route-ID: $routeId", Toast.LENGTH_LONG).show()
            this.viewModel.onClickRoute(routeId)
        },
            RouteCommentsClickListener { RouteComments ->
                this.viewModel.onClickComment(RouteComments)
            },
            CommentImageClickListener { image ->
                this.viewModel.onClickImage(image)
            }
        )

        viewModel.navigateToCommentInputFragment.observe(viewLifecycleOwner, { myComment ->
            myComment?.let { mRoute ->
                Toast.makeText(
                    requireContext(),
                    "Clicked existing comment or 'Add Comment'.... ${mRoute.myTTCommentAND.strMyComment} ",
                    Toast.LENGTH_SHORT
                ).show()
                val action =
                    SummitDetailResultFragmentDirections.actionSummitDetailResultFragmentToCommentInputFragment(
                        mRoute.myTTCommentAND,
                        mRoute.myTT_comment_PhotosANDList.toTypedArray(),
                        viewModel.mTTSummit.value?.strName
                            ?: resources.getString(R.string.my_comment)
                    )
                findNavController().navigate(action)
                viewModel.doneNavigationToCommentInputFragment()
            }
        })

        viewModel.navigateToImageFragment.observe(viewLifecycleOwner, { view ->
            if (view == null) return@observe
            if (view.getTag(R.id.TAG_COMMENT_ID) == null
                || view.getTag(R.id.TAG_PHOTO_ID) == null
            ) return@observe

            ViewCompat.setTransitionName(view, "small_image")
            val commentID = view.getTag(R.id.TAG_COMMENT_ID) as Long
            val photoID = view.getTag(R.id.TAG_PHOTO_ID) as Long

            val mComment =
                viewModel.mMyTTCommentANDWithPhotos.value.find { comment ->
                    comment is Comments.RouteWithMyTTCommentANDWithPhotos &&
                            commentID.equals(comment.myTTCommentAND.myTTCommentAND.Id)
                } as? Comments.RouteWithMyTTCommentANDWithPhotos

            val mPhoto = mComment?.myTTCommentAND?.myTT_comment_PhotosANDList?.find { photo ->
                photoID.equals(photo.Id)
            }
            ViewCompat.setTransitionName(view, "small_image")
            val extras = FragmentNavigatorExtras(view to "image_big")

            val action =
                SummitDetailResultFragmentDirections.actionSummitDetailResultFragmentToZoomImageView(
                    mPhoto?.uri ?: "",
                    getDescription(mComment, mPhoto)
                )
            findNavController().navigate(action, extras)

            viewModel.doneNavigationToCommentImageFragment()
        })

        lifecycleScope.launch {
            viewModel.ttRoutes.collect {
                if (viewModel.showMyComments.value != true)
                    it.let {
                        summitDetailAdapter.submitList(it)
                    }
            }
        }

        // Add an Observer on the state variable for Navigating when and item is clicked.
        viewModel.navigateToRouteDetail.observe(viewLifecycleOwner, { intTTRouteNr ->
            intTTRouteNr?.let {
                this.findNavController().navigate(
                    SummitDetailResultFragmentDirections.actionSummitResultFragmentToRouteDetailResultFragment(
                        EventNavigatingToRoute(
                            intTTWegNr = intTTRouteNr,
                            intTTSummitNr = viewModel.mTTSummit.value?.intTTGipfelNr
                                ?: -1
                        )
                    )
                )
                viewModel.doneNavigatingRoute()
            }
        })
        viewModel.navigateToSummitGeo.observe(viewLifecycleOwner, { doNavigate ->
            if (doNavigate != true) return@observe
            val gmmIntentUri = Uri.parse(
                "geo:${viewModel.mTTSummit.value?.dblGPS_Latitude},${viewModel.mTTSummit.value?.dblGPS_Longitude}"
            )
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            startActivity(mapIntent)
            viewModel.onSummitGeoClicked()
        })
        // Add an Observer on the state variable for Navigating when and item is clicked.
        viewModel.navigateToNextSummitDetail.observe(viewLifecycleOwner, { intTTGipfelNr ->
            intTTGipfelNr?.let {
                this.findNavController().navigate(
                    SummitDetailResultFragmentDirections.actionSummitResultFragmentSelf(
                        EventNavigatingToSummit(intTTGipfelNr)
                    )
                )
                viewModel.doneNavigatingSummit()
            }
        })
        viewModel.mTTSummit.observe(viewLifecycleOwner, {
            binding.summit = it
        })
        // this Fragment has a ActionBar Options Menu
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title = "Gipfel"


        viewModel.viewModelRouteOrderWidget.sortRoutesBy.observe(viewLifecycleOwner, {
            viewModel.onChangeSortOrder(it)
        })
        return binding.root
    }

    private fun getDescription(
        mComment: Comments.RouteWithMyTTCommentANDWithPhotos?,
        mPhoto: MyTTCommentPhotosAND?
    ): String {
        val description = StringBuilder(mPhoto?.caption ?: " - ")
        description.append("\r\n")
        description.append(
            "${
                requireContext().getString(
                    R.string.formatted_summit_number,
                    viewModel.mTTSummit.value?.strName,
                    viewModel.mTTSummit.value?.intTTGipfelNr?.toString()
                )
            }\r\n"
        )
        mComment?.ttRouteAND?.let {
            if (it.blnAusrufeZeichen == true) {
                "&#10071; ".toHTMLSpan()
            }
            description.append((it.WegName ?: "").toHTMLSpan())
            it.intSterne?.let { sterne ->
                description.append(" ")
                repeat(sterne) { description.append("*") }
            }
        }
        description.append("\r\n")
        description.append("Mit: ")
        description.append(mComment?.myTTCommentAND?.myTTCommentAND?.myAscendedPartner ?: " -- ")
        description.append("\r\n")
        description.append(mComment?.myTTCommentAND?.myTTCommentAND?.strMyComment ?: "")
        description.append("\r\n")
        return description.toString()
    }

    override fun onResume() {
        restoreAnimatedState()
        viewModel.showMyComments.observe(viewLifecycleOwner, {
            if (it == null) return@observe
            val drawableID =
                if (it != true) R.drawable.ic_read_more_anim_in2 else R.drawable.ic_read_more_anim_out2
            val scaleYTo = if (it != true) 0f else 1f
            val translationYTo =
                if (it != true) -binding.edtMySummitComment.height.toFloat() else 0f

            binding.btnShowMySummitComments.icon =
                AnimatedVectorDrawableCompat.create(binding.root.context, drawableID)
            binding.edtMySummitComment.pivotY = 0f
            binding.listRouteFound.pivotY = 1f
            binding.listRouteFound.pivotX = binding.listRouteFound.measuredWidth.toFloat()
            animateRecyclerView()
            binding.edtMySummitComment.animate()
                .scaleY(scaleYTo)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setDuration(500L)
                .start()
            binding.listRouteFound.animate()
                .translationY(translationYTo)
                .setDuration(500L)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .withStartAction {
                    binding.edtMySummitComment.visibility = View.VISIBLE
                }
                .withEndAction {
                    if (scaleYTo == 0f) {
                        binding.edtMySummitComment.visibility = View.GONE
                        binding.listRouteFound.translationY = 0f
                    }
                }
                .start()

            if (binding.btnShowMySummitComments.icon is AnimatedVectorDrawableCompat) {
                (binding.btnShowMySummitComments.icon as AnimatedVectorDrawableCompat).start()
            }
        })
        viewModel.viewModelRouteOrderWidget.futureVisibility.observe(viewLifecycleOwner,
            {
                if (it == View.INVISIBLE) return@observe
                //toggle binding.gridLayout visibility with animation.
                with(binding.radioButtonGrid) {
                    var coorTO = 0F
                    var alphTo = 1F
                    var dur = 300L
                    if (it != View.VISIBLE) {
                        coorTO = -1F
                        alphTo = 0F
                        dur = 100L
                    }

                    runBlocking { viewModel.viewModelRouteOrderWidget.setVisibility(View.VISIBLE) }
                    animate()
                        .setDuration(dur)
                        .setInterpolator(FastOutLinearInInterpolator())
                        .translationX(width.toFloat() * -coorTO)
                        .translationY(height.toFloat() * coorTO)
                        .alpha(alphTo)
                        .withEndAction {//in case hiding the recyclerview hide it at the end.
                            runBlocking {
                                viewModel.viewModelRouteOrderWidget.setVisibility(
                                    it
                                )
                            }
                        }
                        .start()
                }
            })
        super.onResume()
    }

    private fun restoreAnimatedState() {
        viewModel.showMyComments.value?.let {
            val drawableID =
                if (it) R.drawable.ic_read_more_anim_in2 else R.drawable.ic_read_more_anim_out2
            binding.btnShowMySummitComments.icon =
                AnimatedVectorDrawableCompat.create(binding.root.context, drawableID)
            binding.edtMySummitComment.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun animateRecyclerView() {
        binding.listRouteFound.animate()
            .alpha(0.2f)
            .scaleX(0.2f)
            .scaleY(0.2f)
            .setInterpolator(AccelerateInterpolator())
            .setDuration(250L)
            .withEndAction {
                binding.listRouteFound.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setInterpolator(DecelerateInterpolator()).duration = 250
                if (viewModel.showMyComments.value != false) {
                    Log.e(
                        TAG,
                        "binding.listRouteFound.swapAdapter(summitCommentDetailAdapter - ROUTES"
                    )
                    summitDetailAdapter.submitList(viewModel.ttRoutes.value)
                } else {
                    Log.e(
                        TAG,
                        "binding.listRouteFound.swapAdapter(summitCommentDetailAdapter - COMMENTS"
                    )

                    lifecycleScope.launch { summitDetailAdapter.submitList(viewModel.mMyTTCommentANDWithPhotos.first()) }
                }
            }
            .start()
    }

    // region Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.summit_detail_result, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.summit_detail_menu_sort -> {
                viewModel.viewModelRouteOrderWidget.startAnimation()
                true
            }
            R.id.summit_detail_menu_search -> {
                findNavController().navigate(
                    SummitDetailResultFragmentDirections.actionSummitDetailResultFragmentToMainSearchCollectionFragment(
                        0
                    )
                )
                true
            }
            R.id.summit_detail_menu_add -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    // endregion
}