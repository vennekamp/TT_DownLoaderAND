package com.teufelsturm.tt_downloader_kotlin.feature.searches.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.teufelsturm.tt_downloader_kotlin.R
import com.teufelsturm.tt_downloader_kotlin.app.MainActivity
import com.teufelsturm.tt_downloader_kotlin.databinding.SearchRoutesBinding
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.RouteGrade
import com.teufelsturm.tt_downloader_kotlin.feature.searches.generics.EventSearchRouteParameter
import com.teufelsturm.tt_downloader_kotlin.feature.searches.vm.RouteSearchViewModel
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "RouteSearchFragment"

@AndroidEntryPoint
class RouteSearchFragment : Fragment() {

    private lateinit var binding: SearchRoutesBinding

    private val viewModel by activityViewModels<RouteSearchViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.search__routes,
            container,
            false
        )
        Log.i(TAG, "Called onCreateView()")

        // Set the viewmodel for databinding - this allows the bound layout access
        // to all the data in the ViewModel
        binding.viewModel4RouteSearch = viewModel

        //region OBSERVER
        viewModel.searchTextVM.searchText.observe(
            viewLifecycleOwner,
            {
                viewModel.refreshRouteCount()
                viewModel.refreshRangeSlider()
            })
        viewModel.spinnerAreaRoute.selectedItem.observe(
            viewLifecycleOwner,
            {
                viewModel.refreshRouteCount()
                viewModel.refreshRangeSlider()
            }
        )
        viewModel.rangeSliderMinMaxGradeInRouteSearch.values.observe(
            viewLifecycleOwner,
            {
                viewModel.refreshRouteCount()
            })
        viewModel.sliderNumberOfComments.value.observe(
            viewLifecycleOwner,
            {
                viewModel.refreshRouteCount()
            })
        viewModel.sliderMinOfMeanRating.value.observe(
            viewLifecycleOwner,
            {
                viewModel.refreshRouteCount()
            })
        //endregion
        binding.rangeSliderGradeLimits4RouteSearch.setLabelFormatter {
            viewModel.rangeSliderMinMaxGradeInRouteSearch.converter.invoke(it)
        }
        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // Observer for the SEARCH event
        viewModel.eventSearchRoute.observe(viewLifecycleOwner, { doSearch ->
            if (doSearch) onSearchRoute()
        })
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id != R.id.search_menu_show_results_text && id != R.id.search_menu_show_results_icon) super.onOptionsItemSelected(
            item
        )
        viewModel.onSearchRoute()
        return true
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG, "In ViewPager2: onResume()")
        viewModel.actionBarString.observe(viewLifecycleOwner,
            {
                (activity as MainActivity).supportActionBar!!.title = it
            }
        )
        (activity as MainActivity).supportActionBar!!.title = viewModel.actionBarString.value
    }

    override fun onPause() {
        super.onPause()
        Log.v(TAG, "In ViewPager2: onPause()")
        viewModel.actionBarString.removeObservers(viewLifecycleOwner)
    }

    private fun onSearchRoute() {
        Log.i(TAG, "onSearchSummit()")
        // Arguments:
        //      - partialSummitName
        //      - area
        //      - limits4RouteSearch
        //      - minNumberOfComments
        //      - minOfMeanRating
        val partialRouteName = "%${viewModel.searchTextVM.searchText.value}%"
        val area = viewModel.spinnerAreaRoute.selectedItem.value ?: ""
        val mLowerLimit =
            viewModel.rangeSliderMinMaxGradeInRouteSearch.values.value?.get(0)?.toInt()
        val mUpperLimit =
            viewModel.rangeSliderMinMaxGradeInRouteSearch.values.value?.get(1)?.toInt()
        val intMinGrade =
            RouteGrade.getRouteGradeByOrdinal(mLowerLimit) ?: RouteGrade.getMinOrdinal()
        val intMaxGrade =
            RouteGrade.getRouteGradeByOrdinal(mUpperLimit) ?: RouteGrade.getMaxOrdinal()
        val minNumberOfComments =
            viewModel.sliderNumberOfComments.value.value?.toInt() ?: 0
        val minOfMeanRating =
            viewModel.sliderMinOfMeanRating.value.value ?: 0f
        val just_mine = viewModel.justMyRoute.get()
        val eventSearchRouteParameter = EventSearchRouteParameter(
            partialRouteName = partialRouteName,
            area = area,
            intMinGrade = intMinGrade,
            intMaxGrade = intMaxGrade,
            minNumberOfComments = minNumberOfComments,
            minOfMeanRating = minOfMeanRating,
            just_mine
        )
        val action =
            TabOfSearchesFragmentDirections.actionMainSearchCollectionFragmentToRoutesListResultFragment(
                eventSearchRouteParameter
            )
        binding.root.findNavController().navigate(action)

        viewModel.onSearchRouteComplete()
    }

}

