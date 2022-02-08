package com.teufelsturm.tt_downloader_kotlin.feature.searches.generics

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import java.util.*


private const val TAG = "SummitAutoCompleteAdp"

class AutoCompleteAdapter
constructor
    (
    private val _application: Application,
    private val findFunc: (String) -> List<String>,
    resource: Int
) :
    ArrayAdapter<String>(_application.applicationContext, resource), Filterable {
    private var resultList: List<String> = ArrayList()
    override fun getCount(): Int {
        return resultList.size
    }

    override fun getItem(index: Int): String {
        return resultList[index]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var _convertView = convertView
        if (convertView == null) {
            val inflater = _application
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            _convertView = inflater.inflate(android.R.layout.select_dialog_item, parent, false)
            (_convertView!!.findViewById<View>(android.R.id.text1) as TextView).setTextColor(
                Color.parseColor(
                    "#0aad3f"
                )
            )
        }
        (_convertView!!.findViewById<View>(android.R.id.text1) as TextView).text =
            getItem(position)
        return _convertView
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {
                    val summits = findFunc("%$constraint%")

                    // Assign the data to the FilterResults
                    filterResults.values = summits
                    filterResults.count = summits.size
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                if (results.count > 0) {
                    resultList = results.values as List<String>
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }
}