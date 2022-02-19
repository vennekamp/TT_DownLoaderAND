package com.teufelsturm.tt_downloader_kotlin.feature.results.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.teufelsturm.tt_downloader_kotlin.R
import com.teufelsturm.tt_downloader_kotlin.databinding.ResultSummitDetailBinding
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.TTRouteClickListener
import com.teufelsturm.tt_downloader_kotlin.feature.results.vm.SummitDetailResultViewModel
import com.teufelsturm.tt_downloader_kotlin.feature.searches.generics.EventNavigatingToRoute
import com.teufelsturm.tt_downloader_kotlin.feature.searches.generics.EventNavigatingToSummit
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.SummitDetailAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

private const val TAG = "SummitDetailResultFrag"

@AndroidEntryPoint
class SummitDetailResultFragment : Fragment() {

    var detailAdapter: SummitDetailAdapter = SummitDetailAdapter()

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

        binding.listRouteFound.adapter = detailAdapter
        detailAdapter.setOnClickListener(TTRouteClickListener { routeId, _ ->
            // Toast.makeText(context, "Route-ID: $routeId", Toast.LENGTH_LONG).show()
            this.viewModel.onClickRoute(routeId)
        })
        viewModel.ttRoutes.observe(viewLifecycleOwner, {
            detailAdapter.submitList(it)
        })

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
        viewModel.mTTSummit.observe(viewLifecycleOwner, Observer {
            binding.summit = it
        })
        viewModel.mMYTTComment.observe(viewLifecycleOwner, {
            binding.listMyComment = it
        })

        // this Fragment has a ActionBar Options Menu
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title = "Gipfel"


        viewModel.viewModelRouteOrderWidget.sortRoutesBy.observe(viewLifecycleOwner, {
            viewModel.onChangeSortOrder(it)
            detailAdapter.notifyDataSetChanged()
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // at 'onResume' the view is created, how can it be forced to be measured?
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
                            runBlocking { viewModel.viewModelRouteOrderWidget.setVisibility(it) }
                        }
                        .start()
                }
            })
    }

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
}