package com.teufelsturm.tt_downloaderand_kotlin

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TabHost
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SearchPagerWidget.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SearchPagerWidget.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

class SearchPagerWidget : Fragment(), TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var mViewPager: ViewPager
    private lateinit var mTabHost: TabHost

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout._main_activity_pager, container, false)

        mViewPager = view.findViewById<ViewPager>(R.id.viewpager)

        // Tab Initialization
        initialiseTabHost(view)

        // Fragments and ViewPager Initialization
        val fragments = getFragments()
        val pageAdapter = MyPageAdapter(getChildFragmentManager(), fragments)

        mViewPager.adapter = pageAdapter
        mViewPager.addOnPageChangeListener(this)
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    // Manages the Tab changes, synchronizing it with Pages
    override fun onTabChanged(tag: String) {
        val pos = this.mTabHost.getCurrentTab()
        if ( pos != null ) {
            this.mViewPager.setCurrentItem(pos)
        }
    }

    override fun onPageScrollStateChanged(arg0: Int) {
        // TODO Auto-generated method stub

    }

    override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
        // TODO Auto-generated method stub
    }

    // Manages the Page changes, synchronizing it with Tabs
    override fun onPageSelected(arg0: Int) {
        val pos = this.mViewPager.getCurrentItem()
        if ( pos != null ) {
            this.mTabHost.setCurrentTab(pos)
        }
    }

    private fun getFragments(): List<android.support.v4.app.Fragment> {
        val fList = ArrayList<android.support.v4.app.Fragment>()
        val fragSummit = MainActivitySearchAbstract.newInstance(MainActivitySearchAbstract.SearchType.SUMMIT)
        val fragRoute = MainActivitySearchAbstract.newInstance(MainActivitySearchAbstract.SearchType.ROUTE)
        val fragComment = MainActivitySearchAbstract.newInstance(MainActivitySearchAbstract.SearchType.COMMENT)
        fList.add(fragSummit)
        fList.add(fragRoute)
        fList.add(fragComment)

        return fList
    }

    // Tabs Creation
    private fun initialiseTabHost(v : View) {
        mTabHost = v.findViewById(android.R.id.tabhost)
        mTabHost.setup()

        val res = resources // Resource object to get Drawables
        val activity = activity!!
        addTab(activity, mTabHost, mTabHost.newTabSpec("Gipfel")
                .setIndicator("Gipfel", res.getDrawable(R.drawable.ic_summit)))
        addTab(activity, mTabHost, mTabHost.newTabSpec("Wege")
                .setIndicator("Wege", res.getDrawable(R.drawable.ic_route)))
        addTab(activity, mTabHost, mTabHost.newTabSpec("Kommentare")
                .setIndicator("Kommentare", res.getDrawable(R.drawable.ic_comments)))
        mTabHost.setOnTabChangedListener(this)
    }

    // Method to add a TabHost
    private fun addTab(activity: FragmentActivity, tabHost: TabHost, tabSpec: TabHost.TabSpec) {
        tabSpec.setContent(MyTabFactory(activity))
        tabHost.addTab(tabSpec)
    }
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     *
//     *
//     * See the Android Training lesson [Communicating with Other Fragments]
//     * (http://developer.android.com/training/basics/fragments/communicating.html)
//     * for more information.
//     */
//    interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        fun onFragmentInteraction(uri: Uri)
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment SearchPagerWidget.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                SearchPagerWidget().apply {}
    }
}
