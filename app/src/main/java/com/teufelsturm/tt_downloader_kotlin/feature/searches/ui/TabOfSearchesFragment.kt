package com.teufelsturm.tt_downloader_kotlin.feature.searches.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.teufelsturm.tt_downloader_kotlin.R
import com.teufelsturm.tt_downloader_kotlin.databinding.CollectionOfSearchesTabBinding
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "TabOfSearchesFragment"

@AndroidEntryPoint
class TabOfSearchesFragment : Fragment() {

    private lateinit var binding: CollectionOfSearchesTabBinding

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.collection_of_searches_tab,
            container,
            false
        )
        Log.i(TAG, "Called onCreateView()")

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = binding.viewPagerSearches

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = SearchesAdapter(this)
        viewPager.adapter = pagerAdapter

        val tabLayout = binding.tabsSearches

        generateTabLayoutMediator(tabLayout)
        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner
        consumeBundledArgs()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            // override fun onPageScrollStateChanged(state: Int) {}
            // override fun onPageScrolled(position: Int, positionOffset: Float,positionOffsetPixels: Int)

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.v(
                    TAG,
                    "viewPager.currentItem is: ${viewPager.currentItem}; onPageSelected(position: Int) -> $position"
                )
            }
        })

        // this Fragment has a ActionBar Options Menu
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.searches, menu)
    }


    private fun consumeBundledArgs(
    ) {
        // Retrieve the desired tab position from the arguments from the Bundle
        val args = arguments?.let {
            TabOfSearchesFragmentArgs.fromBundle(
                it
            )
        }
        args?.startTab?.let {
            viewPager.setCurrentItem(it, false)
            arguments = null
        }
    }

    private fun generateTabLayoutMediator(tabLayout: TabLayout) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.icon = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_baseline_terrain_24,
                        null
                    )
                    tab.text = "Gipfel"
                }
                1 -> {
                    tab.icon = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_baseline_alt_route_24,
                        null
                    )
                    tab.text = "Weg"
                }
                2 -> {
                    tab.icon = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_baseline_comment_24,
                        null
                    )
                    tab.text = "Kommentar"
                }
            }
        }.attach()
    }

    /**
     * A simple pager adapter that represents 3 SearchFragment objects, in
     * sequence.
     */
    private inner class SearchesAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> SummitSearchFragment()
                1 -> RouteSearchFragment()
                2 -> CommentSearchFragment()
                else -> throw IllegalArgumentException("No further fragments available for $position...?")
            }

        }
    }
}