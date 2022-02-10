package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTTSummitAND

@Dao
interface MyTTSummitDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(comment: MyTTSummitAND): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(comment: MyTTSummitAND)

    @Query("DELETE FROM MyTT_Summit_AND WHERE myIntTTGipfelNr = :myIntTTGipfelNr")
    fun deleteById(myIntTTGipfelNr: Int)

    @Query("SELECT * FROM MyTT_Summit_AND WHERE myIntTTGipfelNr = :myIntTTGipfelNr")
    fun get(myIntTTGipfelNr: Int): LiveData<MyTTSummitAND>


//    // region PHOTOS
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun insert(commentPhoto: MyTT_SummitPhotos_AND): Long
//
//    @Update(onConflict = OnConflictStrategy.IGNORE)
//    fun update(commentPhoto: MyTT_SummitPhotos_AND): Int
//
//    @Query("DELETE FROM MyTT_RoutePhotos_AND WHERE _id = :Id")
//    fun deleteSummitPhotoById(Id: Long)
//
//    @Query("SELECT * FROM MyTT_SummitPhotos_AND WHERE _id = :Id")
//    fun getSummitPhotosByID(Id: Long): Flow<MyTT_SummitPhotos_AND>
//    // endregion

}