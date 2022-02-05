package com.teufelsturm.tt_downloader_kotlin.results.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.marginTop
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.teufelsturm.tt_downloader_kotlin.R
import com.teufelsturm.tt_downloader_kotlin.data.db.MyTTRouteDAO
import com.teufelsturm.tt_downloader_kotlin.data.db.TTCommentDAO
import com.teufelsturm.tt_downloader_kotlin.data.db.TTRouteDAO
import com.teufelsturm.tt_downloader_kotlin.data.db.TTSummitDAO
import com.teufelsturm.tt_downloader_kotlin.data.entity.CommentsWithRouteWithSummit
import com.teufelsturm.tt_downloader_kotlin.data.order.sortCommentsWithRouteSummitBy
import com.teufelsturm.tt_downloader_kotlin.databinding.ResultsCommentsListBinding
import com.teufelsturm.tt_downloader_kotlin.results.adapter.CommentsListdapter
import com.teufelsturm.tt_downloader_kotlin.results.adapter.util.TTCommentClickListener
import com.teufelsturm.tt_downloader_kotlin.results.vm.CommentsListResultViewModel
import com.teufelsturm.tt_downloader_kotlin.searches.generics.EventNavigatingToRoute
import com.teufelsturm.tt_downloader_kotlin.searches.generics.EventNavigatingToSummit
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


private const val TAG = "CommentsListResultFr"

@AndroidEntryPoint
class CommentsListResultFragment @Inject constructor() : Fragment() {

    var commentsListdapter: CommentsListdapter = CommentsListdapter()


    private lateinit var binding: ResultsCommentsListBinding

    private val viewModel by viewModels<CommentsListResultViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.results_comments_list,
            container,
            false
        )
        Log.i(TAG, "Called onCreateView()")

        // this Fragment has a ActionBar Options Menu
        setHasOptionsMenu(true)

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // Set the viewmodel for databinding - this allows the bound layout access
        // to all the data in the ViewModel
        binding.viewModel4CommentsResult = viewModel

        binding.listCommentsFound.adapter = commentsListdapter

        commentsListdapter.setOnClickListener(
            onClickListenerRoute = TTCommentClickListener { comment: CommentsWithRouteWithSummit ->
                this.viewModel.onClickItem(comment.intTTWegNr, comment.intTTGipfelNr!!)
            },
            onClickListenerSummit = TTCommentClickListener { comment: CommentsWithRouteWithSummit ->
                this.viewModel.onClickItem(null, comment.intTTGipfelNr!!)
            },
        )
        viewLifecycleOwner.lifecycleScope.launch {
            // Retrieve the search arguments from the Bundle and initiate the query
            val args = CommentsListResultFragmentArgs.fromBundle(requireArguments())
            viewModel.queryRoutes(args)
            viewModel.ttCommentANDList.observe(viewLifecycleOwner, {
                it?.let {
                    commentsListdapter.submitList(it.sortCommentsWithRouteSummitBy(viewModel.viewModelCommentOrderWidget.sortCommentsBy.value))
                }
            })
        }

        // Add an Observer on the state variable for Navigating when and item is clicked.
        viewModel.navigateToDetailEvent.observe(viewLifecycleOwner, {
            it?.let {
                if (it.intTTWegNr == null) {
                    Toast.makeText(
                        this.context,
                        "Navigiere zum Gipfel: #${it.intTTSummitNr}",
                        Toast.LENGTH_SHORT
                    ).show()
                    this.findNavController().navigate(
                        CommentsListResultFragmentDirections.actionCommentsListResultFragmentToSummitDetailResultFragment(
                            EventNavigatingToSummit(it.intTTSummitNr)
                        )
                    )
                } else {
                    Toast.makeText(
                        this.context,
                        "Navigiere zur Route: ${it.intTTWegNr} am Gipfel #${it.intTTSummitNr}",
                        Toast.LENGTH_SHORT
                    ).show()
                    this.findNavController().navigate(
                        CommentsListResultFragmentDirections.actionCommentsListResultFragmentToRouteDetailResultFragment(
                            EventNavigatingToRoute(
                                it.intTTWegNr, it.intTTSummitNr
                            )
                        )
                    )
                }
            }
            viewModel.doneNavigatingToDetail()
        })
        viewModel.viewModelCommentOrderWidget.sortCommentsBy.observe(viewLifecycleOwner, {sortOrder ->
            viewModel.onChangeSortOrder(sortOrder)
            commentsListdapter.notifyDataSetChanged()
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // at 'onResume' the view is created, how can it be forced to be measured?
        viewModel.viewModelCommentOrderWidget.futureVisibility.observe(viewLifecycleOwner,
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
                runBlocking { viewModel.viewModelCommentOrderWidget.setVisibility(View.VISIBLE) }
                menuRB.animate()
                    .setDuration(dur)
                    .setInterpolator(FastOutLinearInInterpolator())
                    .translationX(menuRB.width.toFloat() * -coorTO)
                    .translationY(menuRB.height.toFloat() * coorTO)
                    .alpha(alphTo)
                    .withEndAction { //in case hiding the radiobutton grid hide it at the end.
                        viewModel.viewModelCommentOrderWidget.setVisibility(it)
                    }
                    .start()
                val marginTOP = binding.listCommentsFound.marginTop.toFloat()
                binding.listCommentsFound.animate()
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
                Toast.makeText(context, "Sortieren  gedrückt!", Toast.LENGTH_LONG).show()
                viewModel.viewModelCommentOrderWidget.startAnimation()
                true
            }
            R.id.summits_menu_search -> {
                findNavController().navigate(
                    CommentsListResultFragmentDirections.actionCommentsListResultFragmentToMainSearchCollectionFragment(
                        2
                    )
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}