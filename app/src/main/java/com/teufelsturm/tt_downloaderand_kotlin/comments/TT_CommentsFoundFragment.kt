package com.teufelsturm.tt_downloaderand_kotlin.comments

import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.ListView
import android.widget.Toast
import com.teufelsturm.tt_downloaderand_kotlin.MainActivitySearchComment
import com.teufelsturm.tt_downloaderand_kotlin.OnFragmentInteractionListener
import com.teufelsturm.tt_downloaderand_kotlin.R
import com.teufelsturm.tt_downloaderand_kotlin.TT_DB_Helper.DataBaseHelper
import com.teufelsturm.tt_downloaderand_kotlin.tt_objects.TT_Comment_AND


// the fragment initialization parameters, e.g. TAG
private const val TAG = "TT_CommentsFound"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TT_CommentsFoundFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TT_CommentsFoundFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class TT_CommentsFoundFragment : Fragment() {


    private lateinit var lstTTCommentAND: MutableList<TT_Comment_AND>
    private lateinit var newDB: SQLiteDatabase
    private lateinit var tts: TextToSpeech

    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.comments_activity_found_lv_list, container, false)

        // query all routes to this summit
        openAndQueryDatabase()
        val listenAdapter = TT_Comment_ANDAdapter(activity, lstTTCommentAND, true)
        Log.i(TAG, "(ListView) findViewById(R.id.list_routes);")
        val meinListView = view.findViewById<ListView>(R.id.list_comments)
        meinListView.setAdapter(listenAdapter)
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
        //Close the Text to Speech Library
        tts.stop()
        tts.shutdown()
        Log.d(TAG, "TTS Destroyed")
        super.onDestroy()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TT_CommentsFoundFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() : TT_CommentsFoundFragment =
                TT_CommentsFoundFragment().apply {
                }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.summits_found, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //**************************
    private fun openAndQueryDatabase() {
        Log.i(TAG, "Neuer openAndQueryDatabase... ")

        try {
            val dbHelper = DataBaseHelper(activity?.getApplicationContext())
            newDB = dbHelper.writableDatabase
            val cursor: Cursor?
            val QueryString1: String
            val strTextSuchtext = MainActivitySearchComment
                    .getStrTextSuchtext()
            val strGebiet = MainActivitySearchComment.getStrtextViewGebiet()
            val intMinSchwierigkeit = MainActivitySearchComment
                    .getEnumMinLimitsForScale()
            val intMaxSchwierigkeit = MainActivitySearchComment
                    .getEnumMaxLimitsForScale()
            val intMinMinCommentInComment = MainActivitySearchComment
                    .getIntMinCommentInComment()
            //			SELECT a.[intTTWegNr], a.[strEntryKommentar], b.[WegName], b.[strSchwierigkeitsGrad], c.[strName],  b.[intTTGipfelNr], a.[entryBewertung], a.[strEntryUser], a.[entryDatum]
            //				      FROM  [TT_Summit_AND] c, [TT_Route_AND] b, [TT_RouteComment_AND] a
            //				      WHERE a.[entryBewertung] >= 0
            //				      AND a.[strEntryKommentar] like '%%'
            //				      AND a.[intTTWegNr] = b.[intTTWegNr]
            //				      AND c.[strGebiet] = 'Bielatal'
            //				      AND c.[intTTGipfelNr] = b.[intTTGipfelNr]
            //				      AND coalesce(b.[sachsenSchwierigkeitsGrad],
            //							b.[ohneUnterstützungSchwierigkeitsGrad],
            //							b.[rotPunktSchwierigkeitsGrad]
            //							, b.[intSprungSchwierigkeitsGrad] ) BETWEEN 1 AND 15
            //				     ORDER BY b.[intTTGipfelNr] LIMIT 250
            QueryString1 = StringBuilder().append("SELECT a.[intTTWegNr], a.[strEntryKommentar], ")
                    .append("b.[WegName], b.[strSchwierigkeitsGrad]")
                    .append(" , c.[strName], ")
                    .append("b.[intTTGipfelNr], a.[entryBewertung], a.[strEntryUser], a.[entryDatum]")
                    .append("      FROM  [TT_Summit_AND] c, [TT_Route_AND] b, [TT_RouteComment_AND] a")
                    .append("      WHERE a.[entryBewertung] >= ")
                    .append(intMinMinCommentInComment).append("      AND a.[strEntryKommentar] like ")
                    .append(DatabaseUtils.sqlEscapeString("%$strTextSuchtext%"))
                    .append("     AND a.[intTTWegNr] = b.[intTTWegNr]")
                    .append(if (strGebiet == this.getString(R.string.strAll))
                        "       AND c.[strGebiet] != \"\" "
                    else
                        "       AND c.[strGebiet] = '$strGebiet'")
                    .append("      AND c.[intTTGipfelNr] = b.[intTTGipfelNr]")
                    .append("      AND coalesce(b.[sachsenSchwierigkeitsGrad],")
                    .append("			b.[ohneUnterstützungSchwierigkeitsGrad],")
                    .append("			b.[rotPunktSchwierigkeitsGrad]")
                    .append("			, b.[intSprungSchwierigkeitsGrad] ) ")
                    .append(" BETWEEN ").append(intMinSchwierigkeit).append(" AND ")
                    .append(intMaxSchwierigkeit).append("     ORDER BY c.[strName] LIMIT ")
                    .append(resources.getInteger(R.integer.MaxNoItemQuerxy)).toString()
            Log.i(TAG,
                    "Neue Kommentarr zum Suche finden:\r\n$QueryString1")

            cursor = newDB.rawQuery(QueryString1, null)
            Log.i(TAG,
                    "Neuen Kommentar zum Weg suchen:\t c != null'" + (cursor != null))

            if (cursor != null) {
                var iCounter = 0
                if (cursor.moveToFirst()) {
                    do {
                        val intTTWegNr = cursor.getInt(
                                cursor.getColumnIndex("intTTWegNr"))
                        Log.i(TAG, " -> intTTWegNr..... $intTTWegNr")
                        val strEntryKommentar = cursor.getString(
                                cursor.getColumnIndex("strEntryKommentar"))
                        Log.i(TAG, " -> strEntryKommentar..... $strEntryKommentar")
                        val strWegName = (cursor.getString(cursor
                                .getColumnIndex("WegName")) + " ("
                                + cursor.getString(cursor
                                .getColumnIndex("strSchwierigkeitsGrad")) + ")")
                        val strName = cursor.getString(cursor
                                .getColumnIndex("strName"))
                        val intTTGipfelNr = cursor.getInt(cursor
                                .getColumnIndex("intTTGipfelNr"))
                        Log.i(TAG,
                                " -> strWegName..... $strWegName")
                        val intEntryBewertung = cursor.getInt(cursor
                                .getColumnIndex("entryBewertung"))
                        Log.i(TAG,
                                " -> intEntryBewertung..... $intEntryBewertung")
                        val strEntryUser = cursor.getString(cursor
                                .getColumnIndex("strEntryUser"))
                        Log.i(TAG,
                                " -> strEntryUser..... $strEntryUser")
                        val longEntryDatum = cursor.getLong(cursor
                                .getColumnIndex("entryDatum"))
                        Log.i(TAG,
                                " -> longEntryDatum..... $longEntryDatum")

                        lstTTCommentAND.add(TT_Comment_AND(intTTWegNr,
                                strEntryKommentar, strWegName, strName, intTTGipfelNr, intEntryBewertung,
                                strEntryUser, longEntryDatum))
                        Log.i(TAG, (++iCounter).toString()
                                + " -> Neuer Kommentar... " + strEntryUser)
                    } while (cursor.moveToNext())
                    cursor.close()
                }
            }
        } catch (se: SQLiteException) {
            Log.e(TAG,
                    "Could not create or Open the database")
        } finally {

            newDB.close()
            Toast.makeText(activity, lstTTCommentAND.size.toString() + " Kommentare gefunden"
                    + if (lstTTCommentAND.size == resources.getInteger(R.integer.MaxNoItemQuerxy))
                " (Maximalanzahl an Ergebnissen erreicht)"
            else
                "", Toast.LENGTH_LONG)
                    .show()
        }
    }
}
