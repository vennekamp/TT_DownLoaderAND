package com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util

import android.location.Location
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTNeigbourANDTTName
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTSummitAND
import de.teufelsturm.tt_downloader_ktx.R
import java.text.DecimalFormat

@BindingAdapter(
    value = ["bind:summit4NeighbourTextFormatted", "bind:neighbour4NeighbourTextFormatted"],
    requireAll = true
)
fun TextView.neighbourTextFormatted(
    summit: TTSummitAND?,
    neighbour: TTNeigbourANDTTName?
) {

    if (neighbour == null) text = context.resources.getString(R.string.no_data)
    else if (summit == null) text = neighbour.strName
    else if (nullCheckCoordinates(summit, neighbour)) text = neighbour.strName
    else {
        val mainCoordinates = Location("reverseGeocoded")
        mainCoordinates.latitude = summit.dblGPS_Latitude!!
        mainCoordinates.longitude = summit.dblGPS_Longitude!!

        val neighborCoordinate = Location("reverseGeocoded")
        neighborCoordinate.latitude = neighbour.dblGPS_Latitude!!
        neighborCoordinate.longitude = neighbour.dblGPS_Longitude!!
        val distance = mainCoordinates.distanceTo(neighborCoordinate)
        val aDecimalFormat = DecimalFormat("##")
        text = context.resources.getString(
            R.string.neighbour_text_formatted,
            neighbour.strName,
            aDecimalFormat.format(distance)
        )
    }
}

private fun nullCheckCoordinates(
    summit: TTSummitAND,
    neighbour: TTNeigbourANDTTName
) = (summit.dblGPS_Latitude == null || summit.dblGPS_Longitude == null
        || neighbour.dblGPS_Latitude == null || neighbour.dblGPS_Longitude == null)
