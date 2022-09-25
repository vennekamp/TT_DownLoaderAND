package com.teufelsturm.tt_downloader_kotlin.feature.results.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.navigation.fragment.findNavController
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.TTRouteClickListener
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.TTSummitClickListener
import com.teufelsturm.tt_downloader_kotlin.feature.results.vm.RoutesListResultViewModel
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.RoutesListAdapter
import com.teufelsturm.tt_downloader_kotlin.feature.searches.generics.EventNavigatingToSummit
import dagger.hilt.android.AndroidEntryPoint
import de.teufelsturm.tt_downloader_ktx.R
import de.teufelsturm.tt_downloader_ktx.databinding.ResultsRoutesListBinding
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


private const val TAG = "RoutesListResultFr"

@AndroidEntryPoint
class RoutesListResultFragment @Inject constructor() : Fragment() {

    var routesListAdapter: RoutesListAdapter = RoutesListAdapter()

    private lateinit var binding: ResultsRoutesListBinding

    private val viewModel by viewModels<RoutesListResultViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.results_routes_list,
            container,
            false
        )
        Log.i(TAG, "Called onCreateView()")

        // this Fragment has a ActionBar Options Menu
        setHasOptionsMenu(true)

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // Retrieve the search arguments from the Bundle and initiate the query
        val args = RoutesListResultFragmentArgs.fromBundle(requireArguments())
        viewModel.queryRoutes(args)

        // Set the viewmodel for databinding - this allows the bound layout access
        // to all the data in the ViewModel
        binding.viewModel4RoutesResult = viewModel

        binding.listRoutesFound.adapter = routesListAdapter
        routesListAdapter.setOnClickListener(
            TTRouteClickListener { routeID, summitId ->
                this.viewModel.onClickRoute(routeID, summitId!!)
            },
            TTSummitClickListener { summitId ->
                this.viewModel.onClickSummit(summitId)
            })
        viewModel.ttRouteANDList.observe(viewLifecycleOwner) {
            routesListAdapter.submitList(it)
        }

        // Add an Observer on the state variable for Navigating when and item is clicked.
        viewModel.navigateToSummitDetail.observe(viewLifecycleOwner) { intTTGipfelNr ->
            intTTGipfelNr?.let {
                this.findNavController().navigate(
                    RoutesListResultFragmentDirections.actionRoutesListResultFragmentToSummitDetailResultFragment(
                        EventNavigatingToSummit(intTTGipfelNr)
                    )
                )
                viewModel.doneNavigatingToSumit()
            }
        }
        viewModel.navigateToRouteDetailEvent.observe(viewLifecycleOwner
        ) { _eventNavigatingToRoute ->
            _eventNavigatingToRoute?.let {
                _eventNavigatingToRoute.intTTSummitNr.let {
                    this.findNavController().navigate(
                        RoutesListResultFragmentDirections.actionRoutesListResultFragmentToRouteDetailResultFragment(
                            _eventNavigatingToRoute
                        )
                    )
                    viewModel.doneNavigatingToRoute()
                }
            }
        }

        viewModel.viewModelRouteOrderWidget.sortRoutesBy.observe(viewLifecycleOwner) {
            viewModel.onChangeSortOrder(it)
            routesListAdapter.notifyDataSetChanged()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // at 'onResume' the view is created, how can it be forced to be measured?
        viewModel.viewModelRouteOrderWidget.futureVisibility.observe(viewLifecycleOwner
        ) {
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
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_result, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return when (id) {
            R.id.summits_menu_sort -> {
                viewModel.viewModelRouteOrderWidget.startAnimation()
                true
            }
            R.id.summits_menu_search -> {
                findNavController().navigate(
                    RoutesListResultFragmentDirections.actionRoutesListResultFragmentToMainSearchCollectionFragment(
                        1
                    )
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}