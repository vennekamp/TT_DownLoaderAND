package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTTSummitAND

@Dao
interface MyTTSummitDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(comment: MyTTSummitAND) : Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(comment: MyTTSummitAND)

    @Query("DELETE FROM MyTT_Summit_AND WHERE myIntTTGipfelNr = :myIntTTGipfelNr")
    fun deleteById(myIntTTGipfelNr: Int)

    @Query("SELECT * FROM MyTT_Summit_AND WHERE myIntTTGipfelNr = :myIntTTGipfelNr")
    fun get(myIntTTGipfelNr: Int): LiveData<MyTTSummitAND>


}