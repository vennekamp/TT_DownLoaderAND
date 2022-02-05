package com.teufelsturm.tt_downloader_kotlin.searches.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.teufelsturm.tt_downloader_kotlin.R
import com.teufelsturm.tt_downloader_kotlin.app.MainActivity
import com.teufelsturm.tt_downloader_kotlin.databinding.SearchSummitsBinding
import com.teufelsturm.tt_downloader_kotlin.searches.generics.EventSearchSummitParameter
import com.teufelsturm.tt_downloader_kotlin.searches.vm.SummitSearchViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "SearchSummitFragment"

@AndroidEntryPoint
class SummitSearchFragment : Fragment() {

    private lateinit var binding: SearchSummitsBinding

    private val viewModel by activityViewModels<SummitSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.search__summits,
            container,
            false
        )
        Log.i(TAG, "Called onCreateView()")

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // Set the viewmodel for databinding - this allows the bound layout access
        // to all the data in the ViewModel
        binding.viewModel4SummitSearch = viewModel
        //region OBSERVER
        viewModel.spinnerAreaSummit.selectedItem.observe(
            viewLifecycleOwner,
            {
                viewModel.refreshSummitCount()
                viewModel.refreshRangeSlider()
            }
        )
        viewModel.rangeSliderAnzahlDerWege.values.observe(
            viewLifecycleOwner,
            {
                viewModel.refreshSummitCount()
            })

        viewModel.rangeSliderAnzahlDerSternchenWege.values.observe(
            viewLifecycleOwner,
            {
                viewModel.refreshSummitCount()
            })
        viewModel.searchTextVM.searchText.observe(
            viewLifecycleOwner,
            {
                viewModel.refreshSummitCount()
                viewModel.refreshRangeSlider()
            })
        // Observer for the SEARCH event
        viewModel.eventSearchSummit.observe(viewLifecycleOwner, Observer<Boolean> { doSearch ->
            if (doSearch) onSearchSummit()
        })
        //endregion
        setHasOptionsMenu(true)
        return binding.root
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id != R.id.search_menu_show_results_text && id != R.id.search_menu_show_results_icon)
            return super.onOptionsItemSelected(item)
        viewModel.onSearchSummit()
        return true
    }

    private fun onSearchSummit() {
        Log.i(TAG, "onSearchSummit()")
        // Arguments:
        //      - area                      (String)
        //      - numberOfRoutes            (Array<Int>)
        //      - numberOfStarredRoutes     (Array<Int>)
        //      - partialSummitName         (String)
        val area = viewModel.spinnerAreaSummit.selectedItem.value
        val numberOfRoutes =
            viewModel.rangeSliderAnzahlDerWege.values.value!!.map { it.toInt() }.toIntArray()
        val numberOfStarredRoutes =
            viewModel.rangeSliderAnzahlDerSternchenWege.values.value!!.map { it.toInt() }
                .toIntArray()
        val just_my_summits = viewModel.justMySummit.get()
        val partialSummitName = "%${binding.editTextSuchtextGipfel.text}%"
        val eventSearchSummitParameter = EventSearchSummitParameter(
            minAnzahlWege = numberOfRoutes[0],
            maxAnzahlWege = numberOfRoutes[1],
            minAnzahlSternchenWege = numberOfStarredRoutes[0],
            maxAnzahlSternchenWege = numberOfStarredRoutes[1],
            searchAreas = area.toString(),
            just_my_summit = just_my_summits,
            searchText = partialSummitName
        )
        val action =
            TabOfSearchesFragmentDirections.actionMainSearchCollectionFragmentToSummitsResultFragment(
                eventSearchSummitParameter
            )

        binding.root.findNavController().navigate(action)
        viewModel.onSearchSummitComplete()
    }
}

