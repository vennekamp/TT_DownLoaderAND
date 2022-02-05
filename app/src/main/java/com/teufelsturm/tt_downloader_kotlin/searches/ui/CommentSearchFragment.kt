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
import androidx.navigation.findNavController
import com.google.android.material.slider.LabelFormatter
import com.teufelsturm.tt_downloader_kotlin.R
import com.teufelsturm.tt_downloader_kotlin.app.MainActivity
import com.teufelsturm.tt_downloader_kotlin.databinding.SearchCommentsBinding
import com.teufelsturm.tt_downloader_kotlin.results.adapter.util.RouteGrade
import com.teufelsturm.tt_downloader_kotlin.searches.generics.EventSearchCommentParameter
import com.teufelsturm.tt_downloader_kotlin.searches.vm.CommentsSearchViewModel
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "CommentSearchFragment"

@AndroidEntryPoint
class CommentSearchFragment : Fragment() {

    private lateinit var binding: SearchCommentsBinding

    private val viewModel by activityViewModels<CommentsSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.search__comments,
            container,
            false
        )
        Log.i(TAG, "Called onCreateView()")

        // Set the viewmodel for databinding - this allows the bound layout access
        // to all the data in the ViewModel
        binding.viewModel4CommentSearch = viewModel

        //region OBSERVER
        viewModel.searchTextVM.searchText.observe(
            viewLifecycleOwner,
            {
                viewModel.refreshCommentCount()
            })
        viewModel.spinnerAreaComment.selectedItem.observe(
            viewLifecycleOwner,
            {
                viewModel.refreshCommentCount()
            })
        viewModel.rangeSliderMinMaxGradeInCommentSearch.values.observe(
            viewLifecycleOwner,
            {
                viewModel.refreshCommentCount()
            })
        viewModel.rangeSliderMinMaxRatingInComment.values.observe(
            viewLifecycleOwner,
            {
                viewModel.refreshCommentCount()
            })


        binding.rangeSliderLimitsForScale4CommentSearch.setLabelFormatter(LabelFormatter {
            viewModel.rangeSliderMinMaxGradeInCommentSearch.converter.invoke(it)
        })
        binding.rangeSliderMinMaxRatingInComment.setLabelFormatter(LabelFormatter {
            viewModel.rangeSliderMinMaxRatingInComment.converter.invoke(it)
        })
        //endregion

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // Observer for the SEARCH event
        viewModel.eventSearchComment.observe(viewLifecycleOwner, { doSearch ->
            if (doSearch) onSearchComment()
        })
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG,"In ViewPager2: onResume()")
        viewModel.actionBarString.observe(viewLifecycleOwner,
            {
                (activity as MainActivity).supportActionBar!!.title = it
            }
        )
        (activity as MainActivity).supportActionBar!!.title = viewModel.actionBarString.value
    }

    override fun onPause() {
        super.onPause()
        Log.v(TAG,"In ViewPager2: onPause()")
        viewModel.actionBarString.removeObservers(viewLifecycleOwner)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id != R.id.search_menu_show_results_text && id != R.id.search_menu_show_results_icon) super.onOptionsItemSelected(
            item
        )
        viewModel.onSearchComment()
        return true
    }


    private fun onSearchComment() {
        Log.i(TAG, "onSearchComment()")
        // Arguments:
        //      - partialSummitName
        //      - area
        //      - limits4RouteSearch
        //      - minNumberOfComments
        //      - minOfMeanRating
        val partialComment = "%${viewModel.searchTextVM.searchText.value}%"
        val area = viewModel.spinnerAreaComment.selectedItem.value ?: ""
        val mLowerLimit =
            viewModel.rangeSliderMinMaxGradeInCommentSearch.values.value?.get(0)?.toInt()
        val mUpperLimit =
            viewModel.rangeSliderMinMaxGradeInCommentSearch.values.value?.get(1)?.toInt()
        val intMinGrade =
            RouteGrade.getRouteGradeByOrdinal(mLowerLimit) ?: RouteGrade.getMinOrdinal()
        val intMaxGrade =
            RouteGrade.getRouteGradeByOrdinal(mUpperLimit) ?: RouteGrade.getMaxOrdinal()
        val minRatingInComment =
            viewModel.rangeSliderMinMaxRatingInComment.values.value?.get(0)?.toInt() ?: 0
        val maxRatingInComment =
            viewModel.rangeSliderMinMaxRatingInComment.values.value?.get(1)?.toInt() ?: 6
        val eventSearchCommentParameter = EventSearchCommentParameter(
            partialComment = partialComment,
            searchAreas = area,
            intMinGrade = intMinGrade,
            intMaxGrade = intMaxGrade,
            minRatingInComment = minRatingInComment,
            maxRatingInComment = maxRatingInComment
        )
        val action =
            TabOfSearchesFragmentDirections.actionMainSearchCollectionFragmentToCommentsListResultFragment(
                eventSearchCommentParameter
            )

        binding.root.findNavController().navigate(action)
        viewModel.onSearchCommentComplete()
    }

}

