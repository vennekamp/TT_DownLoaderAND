package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTNeigbourANDTTName
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTNeigbourSummitAND
import kotlinx.coroutines.flow.Flow

@Dao
interface TTNeigbourSummitANDDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(summit: TTNeigbourSummitAND)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(summit: TTNeigbourSummitAND)

    @Query("Select * FROM TT_NeigbourSummit_AND")
    fun getAll(): LiveData<List<TTNeigbourSummitAND>>

    @Query("Select * FROM TT_NeigbourSummit_AND WHERE _id = :id")
    fun get(id: Int): TTNeigbourSummitAND


    @Query("DELETE FROM TT_NeigbourSummit_AND WHERE _id = :id")
    fun deleteByID(id: Int): Unit

    @Query(
        """SELECT a.*, b.strName FROM TT_NeigbourSummit_AND a
       JOIN TT_Summit_AND b on a.intTTNachbarGipfelNr = b.intTTGipfelNr
       WHERE  a.intTTHauptGipfelNr = :intTTHauptGipfelNr ORDER BY a.intTTNachbarGipfelNr;"""
    )
    fun getNeighbours(intTTHauptGipfelNr: Int) : Flow<List<TTNeigbourANDTTName>>

}