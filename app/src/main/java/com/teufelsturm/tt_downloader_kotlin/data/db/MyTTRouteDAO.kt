package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.room.*
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTTRouteAND
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTT_RoutePhotos_AND
import com.teufelsturm.tt_downloader_kotlin.data.entity.RouteComments
import kotlinx.coroutines.flow.Flow

@Dao
interface MyTTRouteDAO {
    // region MyTTRouteAND
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(comment: MyTTRouteAND): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(comment: MyTTRouteAND): Int

    @Query("DELETE FROM myTT_Route_AND WHERE _id = :id")
    fun deleteMyCommentById(id: Long) : Int

    @Query("SELECT * FROM myTT_Route_AND WHERE myIntTTWegNr = :myIntTTWegNr")
    fun getMyTTRouteAND(myIntTTWegNr: Int): Flow<MyTTRouteAND>

    @Query("SELECT * FROM myTT_Route_AND WHERE _id = :id")
    fun getMyTTRouteANDByID(id: Long): Flow<MyTTRouteAND>

    // endregion
    // region PHOTOS
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(comment: MyTT_RoutePhotos_AND): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(comment: MyTT_RoutePhotos_AND): Int

    @Query("DELETE FROM MyTT_RoutePhotos_AND WHERE _id = :Id")
    fun deletePhotoById(Id: Long)

    @Query("SELECT * FROM MyTT_RoutePhotos_AND WHERE _id = :Id")
    fun getPhotosByID(Id: Long): Flow<MyTT_RoutePhotos_AND>

    @Query("DELETE FROM MyTT_RoutePhotos_AND WHERE commentID = :commentID")
    fun deletePhotoByCommentId(commentID: Int)

    @Query("SELECT * FROM MyTT_RoutePhotos_AND WHERE commentID = :comentID")
    fun getMyTTRoutePhotosAND(comentID: Int): Flow<MyTT_RoutePhotos_AND>
    // endregion

    // region MyTTRouteANDWithPhotos

    @Query("SELECT * FROM myTT_Route_AND")
    fun getAllCommentWithPhoto(): Flow<List<RouteComments.MyTTRouteANDWithPhotos>>

    @Query("SELECT * FROM myTT_Route_AND WHERE myIntTTWegNr = :myIntTTWegNr")
    fun getCommentWithPhoto(myIntTTWegNr: Int): Flow<List<RouteComments.MyTTRouteANDWithPhotos>>

    @Query("SELECT DISTINCT(a.myAscendedPartner) FROM MyTT_Route_AND a WHERE a.myAscendedPartner LIKE :partName ORDER BY LOWER(a.myAscendedPartner)")
    fun getDistinctPartner(partName: String): List<String>
    // endregion
}