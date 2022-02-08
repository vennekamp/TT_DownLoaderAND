package com.teufelsturm.tt_downloader_kotlin.feature.results.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.teufelsturm.tt_downloader_kotlin.R
import com.teufelsturm.tt_downloader_kotlin.data.entity.RouteComments
import com.teufelsturm.tt_downloader_kotlin.data.order.dialogs.OrderCommentsDialogFragment
import com.teufelsturm.tt_downloader_kotlin.data.order.dialogs.ViewModel4CommentOrder
import com.teufelsturm.tt_downloader_kotlin.data.order.sortCommentsBy
import com.teufelsturm.tt_downloader_kotlin.databinding.ResultRouteDetailBinding
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.RouteDetailAdapter
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.CommentImageClickListener
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.RouteCommentsClickListener
import com.teufelsturm.tt_downloader_kotlin.feature.results.vm.RouteDetailResultViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "RouteDetailResultFrag"

@AndroidEntryPoint
class RouteDetailResultFragment : Fragment() {

    private var routeDetailAdapter: RouteDetailAdapter = RouteDetailAdapter()

    private lateinit var binding: ResultRouteDetailBinding

    private val viewModel by viewModels<RouteDetailResultViewModel>()
    private val viewModelOrder by viewModels<ViewModel4CommentOrder>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.result_route_detail,
            container,
            false
        )
        Log.i(TAG, "Called onCreateView()")

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // Retrieve the search arguments from the Bundle and initiate the query
        val args = RouteDetailResultFragmentArgs.fromBundle(requireArguments())
        args.argSearchRouteParameter.intTTWegNr?.let { intTTWegNr ->
            viewModel.queryData(
                intTTWegNr = intTTWegNr,
                intTTGipfelNr = args.argSearchRouteParameter.intTTSummitNr
            )
        }
        // Set the viewmodel for databinding - this allows the bound layout access
        // to all the data in the ViewModel
        binding.viewModel4RouteResultInSummit = viewModel

        binding.listCommentFound.adapter = routeDetailAdapter
        routeDetailAdapter.setOnClickListener(
            RouteCommentsClickListener { RouteComments ->
                this.viewModel.onClickComment(RouteComments)
            },
            CommentImageClickListener { image ->
                this.viewModel.onClickImage(image)
            }
        )


        viewModel.mTTRouteComments.observe(viewLifecycleOwner, {
            if (viewModel.showMyComments.value != true)
                it?.let { routeDetailAdapter.submitList(it.sortCommentsBy(viewModelOrder.sortCommentsBy.value)) }
        })

        viewModel.mMyTTRouteANDWithPhotos.observe(viewLifecycleOwner, {
            if (viewModel.showMyComments.value == true)
                it?.let {
                    routeDetailAdapter.submitList(it)
                }
        })

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title = "Weg"
        viewModelOrder.sortCommentsBy.observe(viewLifecycleOwner, { sortOrder ->
            viewModel.onChangeSortOrder(sortOrder)
            routeDetailAdapter.notifyDataSetChanged()
            val sortCommentsDialog =
                requireActivity().supportFragmentManager.findFragmentByTag("sort_comments_dialog")
            sortCommentsDialog?.let { dialog ->
                (dialog as DialogFragment).dismiss()
            }
        })
        viewModel.navigateToCommentInputFragment.observe(viewLifecycleOwner, { myComment ->
            myComment?.let { mRoute ->
                val action =
                    RouteDetailResultFragmentDirections.actionRouteDetailResultFragmentToCommentInputFragment(
                        mRoute.myTTRouteAND,
                        mRoute.myTT_Route_PhotosANDList.toTypedArray(),
                        viewModel.mTTRouteAND.value?.ttRouteAND?.let {
                            resources.getString(
                                R.string.formatted_route_summit_name,
                                it.WegName,
                                it.strSchwierigkeitsGrad,
                                viewModel.mTTSummitAND.value?.strName ?: ""
                            )
                        } ?: resources.getString(R.string.my_comment)
                    )
                findNavController().navigate(action)
                viewModel.doneNavigationToCommentInputFragment()
            }
        })
        viewModel.navigateToImageFragment.observe(viewLifecycleOwner, { view ->
            if (view == null) return@observe
            ViewCompat.setTransitionName(view, "small_image")
            val extras = FragmentNavigatorExtras(view to "image_big")

            val action =
                RouteDetailResultFragmentDirections.actionRouteDetailResultFragmentToZoomImageView(
                    view.getTag(R.id.imageView2*256) as String
                )

            findNavController().navigate(action, extras)
            viewModel.doneNavigationToCommentImageFragment()
        })
        return binding.root
    }

    override fun onResume() {
        restoreAnimatedState()
        viewModel.showMyComments.observe(viewLifecycleOwner, {
            if (it == null) return@observe
            val drawableID =
                if (it == true) R.drawable.ic_read_more_anim_in2 else R.drawable.ic_read_more_anim_out2
            val scaleYTo = if (it == true) 0f else 1f
            val translationYTo =
                if (it == true) -binding.editTextMyRouteCommentRoute.height.toFloat() else 0f

            binding.btnShowMyComments.icon =
                AnimatedVectorDrawableCompat.create(binding.root.context, drawableID)
            binding.editTextMyRouteCommentRoute.pivotY = 0f
            binding.listCommentFound.pivotY = 1f
            binding.listCommentFound.pivotX = binding.listCommentFound.measuredWidth.toFloat()
            animateRecyclerView()
            binding.editTextMyRouteCommentRoute.animate()
                .scaleY(scaleYTo)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setDuration(500L)
                .start()
            binding.listCommentFound.animate()
                .translationY(translationYTo)
                .setDuration(500L)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .withStartAction {
                    binding.editTextMyRouteCommentRoute.visibility = View.VISIBLE
                }
                .withEndAction {
                    if (scaleYTo == 0f) {
                        binding.editTextMyRouteCommentRoute.visibility = View.GONE
                        binding.listCommentFound.translationY = 0f
                    }
                }
                .start()

            if (binding.btnShowMyComments.icon is AnimatedVectorDrawableCompat) {
                (binding.btnShowMyComments.icon as AnimatedVectorDrawableCompat).start()
            }
        })
        super.onResume()
    }

    private fun restoreAnimatedState() {
        viewModel.showMyComments.value?.let {
            val drawableID =
                if (it) R.drawable.ic_read_more_anim_in2 else R.drawable.ic_read_more_anim_out2
            binding.btnShowMyComments.icon =
                AnimatedVectorDrawableCompat.create(binding.root.context, drawableID)
            binding.editTextMyRouteCommentRoute.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun animateRecyclerView() {
        binding.listCommentFound.animate()
            .alpha(0.2f)
            .scaleX(0.2f)
            .scaleY(0.2f)
            .setInterpolator(AccelerateInterpolator())
            .setDuration(250L)
            .withEndAction {
                binding.listCommentFound.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setInterpolator(DecelerateInterpolator()).duration = 250
                if (viewModel.showMyComments.value == true) {

                    viewModel.mMyTTRouteANDWithPhotos.value?.let { comments ->
                        val commmentsPlusAdd = mutableListOf<RouteComments>()
                        commmentsPlusAdd.addAll(comments)
                        commmentsPlusAdd.add(RouteComments.AddComment)
                        routeDetailAdapter.submitList(commmentsPlusAdd)
                    }
                } else {
                    routeDetailAdapter.submitList(viewModel.mTTRouteComments.value)
                }
                routeDetailAdapter.notifyDataSetChanged()
            }
            .start()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.route_detail_result, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.route_detail_menu_sort -> {
                val sortDialog = OrderCommentsDialogFragment(viewModelOrder)
                sortDialog.show(requireActivity().supportFragmentManager, "sort_comments_dialog")
                true
            }
            R.id.route_detail_menu_search -> {
                findNavController().navigate(
                    RouteDetailResultFragmentDirections.actionRouteDetailResultFragmentToMainSearchCollectionFragment(
                        1
                    )
                )
                true
            }
            R.id.route_detail_menu_add -> {
                viewModel.onClickComment(RouteComments.AddComment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}