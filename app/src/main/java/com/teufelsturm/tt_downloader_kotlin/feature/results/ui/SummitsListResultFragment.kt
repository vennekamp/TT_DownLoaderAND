package com.teufelsturm.tt_downloader_kotlin.feature.results.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.marginTop
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.navigation.fragment.findNavController
import com.teufelsturm.tt_downloader_kotlin.R
import com.teufelsturm.tt_downloader_kotlin.databinding.ResultsSummitsListBinding
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.SummitClickListener
import com.teufelsturm.tt_downloader_kotlin.feature.results.vm.SummitsListResultViewModel
import com.teufelsturm.tt_downloader_kotlin.feature.searches.generics.EventNavigatingToSummit
import com.teufelsturm.tt_downloader_kotlin.results.SummitsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


private const val TAG = "SummitsListResultFr"

@AndroidEntryPoint
class SummitsListResultFragment @Inject constructor() : Fragment() {

    var summitsListAdapter: SummitsListAdapter = SummitsListAdapter()

    private lateinit var binding: ResultsSummitsListBinding

    private val viewModel by viewModels<SummitsListResultViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.results_summits_list,
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
        val args = SummitsListResultFragmentArgs.fromBundle(requireArguments())
        viewModel.querySummits(args)

        // Set the viewmodel for databinding - this allows the bound layout access
        // to all the data in the ViewModel
        binding.viewModel4SummitsResult = viewModel

        binding.listSummitsFound.adapter = summitsListAdapter
        summitsListAdapter.setOnClickListener(SummitClickListener { summitId ->
            // Toast.makeText(context, "Summit ID: ${summitId}", Toast.LENGTH_LONG).show()
            this.viewModel.onClickSummit(summitId)
        })
        viewModel.ttSummits.observe(viewLifecycleOwner, {
            summitsListAdapter.submitList(it)
        })

        // Add an Observer on the state variable for Navigating when and item is clicked.
        viewModel.navigateToSummitDetail.observe(viewLifecycleOwner, { intTTGipfelNr ->
            intTTGipfelNr?.let {
                this.findNavController().navigate(
                    SummitsListResultFragmentDirections.actionSummitsResultFragmentToSummitResultFragment(
                        EventNavigatingToSummit(intTTGipfelNr)
                    )
                )
                viewModel.doneNavigating()
            }
        })
        viewModel.viewModelSummitOrderWidget.sortSummitBy.observe(viewLifecycleOwner, {
            viewModel.onChangeSortOrder(it)
            summitsListAdapter.notifyDataSetChanged()
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // at 'onResume' the view is created, how can it be forced to be measured?
        viewModel.viewModelSummitOrderWidget.futureVisibility.observe(viewLifecycleOwner,
            {
                if (it == View.INVISIBLE) return@observe
                //toggle binding.gridLayout visibility with animation.
                val menuRB = binding.radioButtonGrid
                var coorTO = 0F
                var alphTo = 1F
                var dur = 250L
                if (it != View.VISIBLE) {
                    coorTO = -1F
                    alphTo = 0F
                    dur = 150L
                }
                runBlocking { viewModel.viewModelSummitOrderWidget.setVisibility(View.VISIBLE) }
                menuRB.animate()
                    .setDuration(dur)
                    .setInterpolator(FastOutLinearInInterpolator())
                    .translationX(menuRB.width.toFloat() * -coorTO)
                    .translationY(menuRB.height.toFloat() * coorTO)
                    .alpha(alphTo)
                    .withEndAction { //in case hiding the radiobutton grid hide it at the end.
                        viewModel.viewModelSummitOrderWidget.setVisibility(it)
                    }
                    .start()
                val marginTOP = binding.listSummitsFound.marginTop.toFloat()
                binding.listSummitsFound.animate()
                    .setDuration(dur)
                    .translationY((menuRB.height.toFloat() - marginTOP) * (1 + coorTO))
                    .setInterpolator(FastOutLinearInInterpolator())
                    .start()
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_result, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return when (id) {
            R.id.summits_menu_sort -> {
                viewModel.viewModelSummitOrderWidget.startAnimation()
                true
            }
            R.id.summits_menu_search -> {
                findNavController().navigate(
                    SummitsListResultFragmentDirections.actionSummitsResultFragmentToMainSearchCollectionFragment(
                        0
                    )
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}