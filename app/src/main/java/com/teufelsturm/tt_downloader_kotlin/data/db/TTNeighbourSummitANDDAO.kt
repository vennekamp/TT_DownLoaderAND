package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.teufelsturm.tt_downloader_kotlin.data.entity.SummitTravSalePersOrder
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTNeigbourANDTTName
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTNeigbourSummitAND
import kotlinx.coroutines.flow.*

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
        """SELECT DISTINCT(a.intTTGipfelNr) AS intTTNachbarGipfelNr, 
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
        if ( intTTGipfelNr == 353 ) return getNext2AndPrev2TravSalePersNeighbours(intTTGipfelNr, 1, 4)
        if ( intTTGipfelNr == 354 ) return getNext2AndPrev2TravSalePersNeighbours(intTTGipfelNr, -1, 3)
        if ( intTTGipfelNr == 1093 ) return getNext2AndPrev2TravSalePersNeighbours(intTTGipfelNr, -4, -1)
        if ( intTTGipfelNr == 1091 ) return getNext2AndPrev2TravSalePersNeighbours(intTTGipfelNr, -3, 1)
        /*  Blocked summits who are in the TT summit Database:
            927	    Försterlochturm
            989	    Kleiner Turm
            1008	Adlerlochturm
            1009	Wobstspitze
            1010	Schwarzschlüchteturm
            1011	Schwarze Spitze
            1024	Litfaßsäule
            1026	Hirschsuhlenturm
            1065	Slawe
         */
        return  getNext2AndPrev2TravSalePersNeighbours(intTTGipfelNr, -2, 2)
    }
}
