package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.room.*
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTTCommentAND
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTT_RoutePhotos_AND
import com.teufelsturm.tt_downloader_kotlin.data.entity.Comments
import kotlinx.coroutines.flow.Flow

@Dao
interface MyTTCommentDAO {
    // region MyTTRouteAND
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(comment: MyTTCommentAND): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(comment: MyTTCommentAND): Int

    @Query("DELETE FROM MyTT_Comment_AND WHERE _id = :id")
    fun deleteMyCommentById(id: Long) : Int

    @Query("SELECT * FROM MyTT_Comment_AND WHERE myIntTTWegNr = :myIntTTWegNr")
    fun getMyTTRouteAND(myIntTTWegNr: Int): Flow<MyTTCommentAND>

    @Query("SELECT * FROM MyTT_Comment_AND WHERE _id = :id")
    fun getMyTTRouteANDByID(id: Long): Flow<MyTTCommentAND>

    // endregion
    // region PHOTOS
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(commentPhoto: MyTT_RoutePhotos_AND): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(commentPhoto: MyTT_RoutePhotos_AND): Int

    @Query("DELETE FROM MyTT_RoutePhotos_AND WHERE _id = :Id")
    fun deletePhotoById(Id: Long)

    @Query("SELECT * FROM MyTT_RoutePhotos_AND WHERE _id = :Id")
    fun getPhotosByID(Id: Long): Flow<MyTT_RoutePhotos_AND>

    @Query("DELETE FROM MyTT_RoutePhotos_AND WHERE commentID = :commentID")
    fun deleteMyRoutePhotoByCommentId(commentID: Int)

    @Query("SELECT * FROM MyTT_RoutePhotos_AND WHERE commentID = :comentID")
    fun getMyTTRoutePhotosAND(comentID: Int): Flow<MyTT_RoutePhotos_AND>
    // endregion

    // region MyTTRouteANDWithPhotos

    @Query("SELECT * FROM MyTT_Comment_AND")
    fun getAllCommentWithPhoto(): Flow<List<Comments.MyTTRouteANDWithPhotos>>

    @Query("SELECT * FROM MyTT_Comment_AND WHERE myIntTTWegNr = :myIntTTWegNr")
    fun getCommentWithPhoto(myIntTTWegNr: Int): Flow<List<Comments.MyTTRouteANDWithPhotos>>

    @Query("SELECT DISTINCT(a.myAscendedPartner) FROM MyTT_Comment_AND a WHERE a.myAscendedPartner LIKE :partName ORDER BY LOWER(a.myAscendedPartner)")
    fun getDistinctPartner(partName: String): List<String>
    // endregion
}