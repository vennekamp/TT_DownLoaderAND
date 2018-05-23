package com.teufelsturm.tt_downloaderand_kotlin

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.RelativeLayout.LayoutParams
import android.widget.SeekBar.OnSeekBarChangeListener
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumSachsenSchwierigkeitsGrad
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.EnumTT_WegBewertung

class MainActivitySearchComment : MainActivitySearchAbstract(), OnClickListener, OnSeekBarChangeListener {
    private var mySpinner: Spinner? = null

    private var listener: OnFragmentInteractionListener? = null

    //    public static MainActivitySearchAbstract newInstance() {
    //    	MainActivitySearchAbstract instance = new MainActivitySearchComment();
    //        return instance;
    //    }

    override fun getAutoCompleteCursor(constraint: CharSequence?): Cursor {
        return myAutoCompleteDbAdapter
                .getAllComments(constraint?.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        MainActivitySearchAbstract.myViewID = R.layout._main_activity__comment
        MainActivitySearchAbstract.myEditTextSuchtextID = R.id.editTextSuchtextKommentare
        MainActivitySearchAbstract.from = arrayOf("strAutoCompleteText")
        val view = super.onCreateView(inflater, container, savedInstanceState)
        // ***************************************************************************************
        // Define Action Listener
        // Spinner
        mySpinner = view!!.findViewById(R.id.spinnerAreaComment)
        loadSpinnerData(this.activity, mySpinner)
        mySpinner!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>, arg1: View,
                                        arg2: Int, arg3: Long) {
                MainActivitySearchAbstract.strGebiet = mySpinner!!.selectedItem.toString()
                Log.e(MainActivitySearchAbstract::class.java.simpleName,
                        "mySpinner.getSelectedItem()" + MainActivitySearchAbstract.strGebiet)

            }

            override fun onNothingSelected(arg0: AdapterView<*>) {
                // TODO Auto-generated method stub

            }

        }
        // SEARCH Button
        val buttonSearchComment = view.findViewById<Button>(R.id.buttonSearchComments)
        buttonSearchComment.setOnClickListener(this)
        // ***************************************************************************************
        // create RangeSeekBar as Integer range between 0 and 125 (Route Grading
        // in Comment)
        val seekBarLimitsForCommentGrade = RangeSeekBar(
                intMinLimitsForScale, intMaxLimitsForScale, activity)
        seekBarLimitsForCommentGrade
                .setOnRangeSeekBarChangeListener { bar, minValue, maxValue ->
                    Log.v(TAG,
                            "writeLimitsForScale(minValue, maxValue); --> "
                                    + minValue + "  " + maxValue)
                    writeLimitsForScale(view, minValue, maxValue)
                }
        // ***************************************************************************************
        // add RangeSeekBar to predefined layout
        val layout_activity_seekBarLimitsForScale = view.findViewById<ViewGroup>(R.id.includeLimitsForScale4CommentSearch)
        val layoutParamsSeekBarAnzahlDerWege = LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParamsSeekBarAnzahlDerWege
                .addRule(RelativeLayout.ALIGN_PARENT_LEFT)
        layoutParamsSeekBarAnzahlDerWege.addRule(RelativeLayout.BELOW,
                R.id.textViewLimitsForScale)
        seekBarLimitsForCommentGrade.layoutParams = layoutParamsSeekBarAnzahlDerWege
        layout_activity_seekBarLimitsForScale
                .addView(seekBarLimitsForCommentGrade)
        // ***************************************************************************************
        // alter the SeekBars (standard) in the layout
        val seekBarMinGradingInCommet = view.findViewById<SeekBar>(R.id.seekBarMinGradingInComment)

        seekBarMinGradingInCommet.max = EnumTT_WegBewertung.getMaxInteger() - EnumTT_WegBewertung.getMinInteger()
        seekBarMinGradingInCommet.setOnSeekBarChangeListener(this)
        // ***************************************************************************************
        // Refresh the text above the seekbar (Range)
        writeLimitsForScale(view, intMinLimitsForScale, intMaxLimitsForScale)
        // Refresh the text above the seekbar (Standard)
        writeLimitsForSeekBar(
                view.findViewById<View>(R.id.TextViewCommmentGrading) as TextView, 0,
                R.string.strMinGradingInComment)
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

    private fun writeLimitsForScale(v: View, minValue: Int?, maxValue: Int?) {
        // handle changed range values
        val strUpdate = (getString(R.string.strLimitForScale) + "\n("
                + EnumSachsenSchwierigkeitsGrad.toStringFromSkaleOrdinal(minValue!!)
                + " bis "
                + EnumSachsenSchwierigkeitsGrad.toStringFromSkaleOrdinal(maxValue!!) + ")")
        // Log.v(MainActivitySearchRoute.class.getSimpleName(),
        // "2");
        intMinLimitsForScale = minValue
        intMaxLimitsForScale = maxValue
        val viewLimitsForScale = v.findViewById<View>(R.id.includeLimitsForScale4CommentSearch)
        (viewLimitsForScale
                .findViewById<View>(R.id.textViewLimitsForScale) as TextView).text = strUpdate
    }

    private fun writeLimitsForSeekBar(textView: TextView, progress: Int?,
                                      intRstringID: Int?) {
        // handle changed range values
        intMinCommentInComment = progress!! + EnumTT_WegBewertung.getMinInteger()
        Log.v(TAG, "writeLimitsForSeekBar --> "
                + intMinCommentInComment + "  progress --> " + progress
                + "  EnumTT_WegBewertung.getMinInteger() --> "
                + EnumTT_WegBewertung.getMinInteger())
        val strUpdate = (getString(intRstringID!!) + " "
                + intMinCommentInComment)
        textView.text = strUpdate
    }

    // ***************************************************************************************
    // SeekBar action handler
    override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                   fromUser: Boolean) {
        // TODO Auto-generated method stub
        writeLimitsForSeekBar(
                view!!.findViewById<View>(R.id.TextViewCommmentGrading) as TextView,
                progress, R.string.strMinGradingInComment)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        // TODO Auto-generated method stub

    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        // TODO Auto-generated method stub

    }

    override fun onClick(v: View) {
        // TODO Auto-generated method stub
        Toast.makeText(activity, "Diese Suche kann etwas dauern...", Toast.LENGTH_SHORT)
                .show()
        MainActivitySearchAbstract.myEditTextSuchtextID = R.id.editTextSuchtextKommentare
        MainActivitySearchAbstract.myAutoCompleteTextViewText = (v.findViewById<View>(MainActivitySearchAbstract.myEditTextSuchtextID) as AutoCompleteTextView).text.toString()
        listener?.onFragmentInteraction(MainActivity.URI_COMMENT)
        //		startActivity(new Intent(getActivity(), TT_CommentsFoundActivity.class));
    }

    override fun onResume() {
        MainActivitySearchAbstract.myViewID = R.layout._main_activity__comment
        // loadSpinnerData(this.getActivity(), mySpinner);
        MainActivitySearchAbstract.myEditTextSuchtextID = R.id.editTextSuchtextKommentare
        super.onResume()
    }

    companion object {
        fun getStrTextSuchtext(): String {
            return ""
        }

        fun getStrtextViewGebiet(): String {
            return ""
        }

        private val TAG = MainActivitySearchComment::class.java.simpleName

        private var intMinLimitsForScale = EnumSachsenSchwierigkeitsGrad
                .getMinInteger()
        private var intMaxLimitsForScale = EnumSachsenSchwierigkeitsGrad
                .getMaxInteger()

        private var intMinCommentInComment = EnumTT_WegBewertung.getMinInteger()

        val enumMinLimitsForScale: Int
            get() = EnumSachsenSchwierigkeitsGrad.values()[intMinLimitsForScale].value!!
        val enumMaxLimitsForScale: Int
            get() = EnumSachsenSchwierigkeitsGrad.values()[intMaxLimitsForScale].value!!
    }
}
