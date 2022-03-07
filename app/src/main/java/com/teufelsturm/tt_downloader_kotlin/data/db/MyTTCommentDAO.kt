package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.room.*
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTTCommentAND
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTTCommentPhotosAND
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
    fun getMyCommentANDByRoute(myIntTTWegNr: Int): Flow<List<MyTTCommentAND>>

    @Query("SELECT * FROM MyTT_Comment_AND WHERE _id = :id")
    fun getMyTTCommentANDByID(id: Long): Flow<MyTTCommentAND>

    // endregion
    // region PHOTOS
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(commentPhoto: MyTTCommentPhotosAND): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(commentPhoto: MyTTCommentPhotosAND): Int

    @Query("DELETE FROM MyTT_CommentPhotos_AND WHERE _id = :Id")
    fun deletePhotoById(Id: Long)

    @Query("SELECT * FROM MyTT_CommentPhotos_AND WHERE _id = :Id")
    fun getPhotosByID(Id: Long): Flow<MyTTCommentPhotosAND>

    @Query("DELETE FROM MyTT_CommentPhotos_AND WHERE commentID = :commentID")
    fun deleteMyRoutePhotoByCommentId(commentID: Int)

    @Query("SELECT * FROM MyTT_CommentPhotos_AND WHERE commentID = :comentID")
    fun getMyTTRoutePhotosAND(comentID: Int): Flow<MyTTCommentPhotosAND>
    // endregion

    // region MyTTRouteANDWithPhotos
    @Query("SELECT * FROM MyTT_Comment_AND")
    fun getAllCommentWithPhoto(): Flow<List<Comments.MyTTCommentANDWithPhotos>>

    @Query("SELECT * FROM MyTT_Comment_AND WHERE myIntTTWegNr = :myIntTTWegNr")
    fun getCommentWithPhotoByRoute(myIntTTWegNr: Int): Flow<List<Comments.MyTTCommentANDWithPhotos>>

    @Query("SELECT * FROM MyTT_Comment_AND WHERE myIntTTGipfelNr = :myIntTTGipfelNr")
    fun getCommentWithPhotoBySummit(myIntTTGipfelNr: Int): Flow<List<Comments.MyTTCommentANDWithPhotos>>

//    @Query("SELECT * FROM MyTT_Comment_AND WHERE myIntTTGipfelNr = :myIntTTGipfelNr AND myIntTTWegNr IS NULL")
//    fun getSummitCommentWithPhotoBySummit(myIntTTGipfelNr: Int): Flow<List<Comments.MyTTCommentANDWithPhotos>>

    @Query("SELECT DISTINCT(a.myAscendedPartner) FROM MyTT_Comment_AND a WHERE a.myAscendedPartner LIKE :partName ORDER BY LOWER(a.myAscendedPartner)")
    fun getDistinctPartner(partName: String): List<String>
    // endregion
}