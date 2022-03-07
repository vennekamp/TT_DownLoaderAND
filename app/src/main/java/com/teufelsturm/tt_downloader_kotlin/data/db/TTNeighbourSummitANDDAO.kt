package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.teufelsturm.tt_downloader_kotlin.data.entity.SummitTravSalePersOrder
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTNeigbourANDTTName
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTNeigbourSummitAND
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Dao
interface TTNeighbourSummitANDDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(summit: TTNeigbourSummitAND)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(summit: TTNeigbourSummitAND)

    @Query("Select * FROM TT_NeigbourSummit_AND")
    fun getAll(): LiveData<List<TTNeigbourSummitAND>>

    @Query("Select * FROM TT_NeigbourSummit_AND WHERE _id = :id")
    fun get(id: Int): TTNeigbourSummitAND


    @Query("DELETE FROM TT_NeigbourSummit_AND WHERE _id = :id")
    fun deleteByID(id: Int)

    @Query(
        """SELECT a.*, b.strName,
                        b.strName,
                        b.dblGPS_Latitude,
                        dblGPS_Longitude FROM TT_NeigbourSummit_AND a
       JOIN TT_Summit_AND b on a.intTTNachbarGipfelNr = b.intTTGipfelNr
       WHERE  a.intTTHauptGipfelNr = :intTTHauptGipfelNr ORDER BY a.intTTNachbarGipfelNr;"""
    )
    fun getNeighbours(intTTHauptGipfelNr: Int): Flow<List<TTNeigbourANDTTName>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(summit: SummitTravSalePersOrder)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(summit: SummitTravSalePersOrder)

    @Query(
        """SELECT a.intTTGipfelNr AS intTTNachbarGipfelNr, 
                        b.strName,
                        b.dblGPS_Latitude,
                        dblGPS_Longitude
                    FROM Summit_TravSalePers_Order a 
                        JOIN TT_Summit_AND b on a.intTTGipfelNr = b.intTTGipfelNr
                    WHERE a._id BETWEEN (SELECT l._id + :from FROM Summit_TravSalePers_Order l WHERE l.intTTGipfelNr = :intTTGipfelNr)
                            AND  (SELECT u._id + :to FROM Summit_TravSalePers_Order u WHERE u.intTTGipfelNr = :intTTGipfelNr)
                    AND a.intTTGipfelNr != :intTTGipfelNr ORDER BY a._id;"""
    )
    fun getNext2AndPrev2TravSalePersNeighbours(intTTGipfelNr: Int, from: Int, to: Int): Flow<List<TTNeigbourANDTTName>>

    suspend fun getTSPSummits(intTTGipfelNr: Int): Flow<List<TTNeigbourANDTTName>> {
        var rtnList = getNext2AndPrev2TravSalePersNeighbours(intTTGipfelNr, -2, 2)
        if (rtnList.first().size == 4) return rtnList
        if (rtnList.first().size == 3) {
            rtnList = getNext2AndPrev2TravSalePersNeighbours(intTTGipfelNr, -1, 3)
            if (rtnList.first().size == 4) return rtnList
            rtnList = getNext2AndPrev2TravSalePersNeighbours(intTTGipfelNr, -3, 1)
            if (rtnList.first().size == 4) return rtnList
        }
        else {
            rtnList = getNext2AndPrev2TravSalePersNeighbours(intTTGipfelNr, 0, 4)
            if (rtnList.first().size == 4) return rtnList
            rtnList = getNext2AndPrev2TravSalePersNeighbours(intTTGipfelNr, -4, 0)
            if (rtnList.first().size == 4) return rtnList
        }
        return  getNext2AndPrev2TravSalePersNeighbours(intTTGipfelNr, -2, 2)
    }
}
