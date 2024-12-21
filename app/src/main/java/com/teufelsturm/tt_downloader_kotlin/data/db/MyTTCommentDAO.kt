package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.room.*
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTTCommentAND
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTTCommentPhotosAND
import com.teufelsturm.tt_downloader_kotlin.data.entity.Comments
import kotlinx.coroutines.flow.Flow
// @Dao annotation identifies this interface as a Data Access Object (DAO) for Room Database
@Dao
interface MyTTCommentDAO {

    // region MyTTRouteAND - Methods to handle CRUD operations for MyTT_Comment_AND table

    // Inserts a new comment into the MyTT_Comment_AND table.
    // If a conflict occurs (e.g., duplicate primary key), the IGNORE strategy will skip the insert.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(comment: MyTTCommentAND): Long

    // Updates an existing comment in the MyTT_Comment_AND table.
    // If the comment does not exist, no update occurs.
    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(comment: MyTTCommentAND): Int

    // Deletes a comment from MyTT_Comment_AND by its primary key (_id).
    // Returns the number of rows affected (should be 1 if successful, 0 if no row was found).
    @Query("DELETE FROM MyTT_Comment_AND WHERE _id = :id")
    fun deleteMyCommentById(id: Long): Int

    // Retrieves all comments related to a specific route (myIntTTWegNr) from the MyTT_Comment_AND table.
    // Returns a Flow, which allows for real-time observation of data changes.
    @Query("SELECT * FROM MyTT_Comment_AND WHERE myIntTTWegNr = :myIntTTWegNr")
    fun getMyCommentANDByRoute(myIntTTWegNr: Int): Flow<List<MyTTCommentAND>>

    // Retrieves a specific comment from MyTT_Comment_AND by its primary key (_id).
    // Returns a Flow, enabling live updates when the comment changes.
    @Query("SELECT * FROM MyTT_Comment_AND WHERE _id = :id")
    fun getMyTTCommentANDByID(id: Long): Flow<MyTTCommentAND>

    // endregion

    // region PHOTOS - Methods to handle CRUD operations for MyTT_CommentPhotos_AND table

    // Inserts a new photo into the MyTT_CommentPhotos_AND table.
    // If a conflict occurs (e.g., duplicate primary key), the IGNORE strategy will skip the insert.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(commentPhoto: MyTTCommentPhotosAND): Long

    // Updates an existing photo in the MyTT_CommentPhotos_AND table.
    // If the photo does not exist, no update occurs.
    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(commentPhoto: MyTTCommentPhotosAND): Int

    // Deletes a photo from MyTT_CommentPhotos_AND by its primary key (_id).
    @Query("DELETE FROM MyTT_CommentPhotos_AND WHERE _id = :Id")
    fun deletePhotoById(Id: Long)

    // Retrieves a specific photo from MyTT_CommentPhotos_AND by its primary key (_id).
    // Returns a Flow, enabling live updates when the photo changes.
    @Query("SELECT * FROM MyTT_CommentPhotos_AND WHERE _id = :Id")
    fun getPhotosByID(Id: Long): Flow<MyTTCommentPhotosAND>

    // Deletes all photos associated with a specific comment ID (commentID) from MyTT_CommentPhotos_AND.
    @Query("DELETE FROM MyTT_CommentPhotos_AND WHERE commentID = :commentID")
    fun deleteMyRoutePhotoByCommentId(commentID: Int)

    // Retrieves all photos associated with a specific comment ID (commentID) from MyTT_CommentPhotos_AND.
    // Returns a Flow, enabling live updates when the photos change.
    @Query("SELECT * FROM MyTT_CommentPhotos_AND WHERE commentID = :commentID")
    fun getMyTTRoutePhotosAND(commentID: Int): Flow<MyTTCommentPhotosAND>

    // endregion

    // region MyTTRouteANDWithPhotos - Queries to fetch combined data from MyTT_Comment_AND and MyTT_CommentPhotos_AND

    // Uses a @Transaction to combine multiple database operations into a single atomic unit.
    // Retrieves all comments with their associated photos by joining MyTT_Comment_AND with MyTT_CommentPhotos_AND.
    // Returns a Flow of a list of MyTTCommentANDWithPhotos, which is a data class representing the relationship.
    @Transaction
    @Query("SELECT * FROM MyTT_Comment_AND")
    fun getAllCommentWithPhoto(): Flow<List<Comments.MyTTCommentANDWithPhotos>>

    // Uses a @Transaction to combine multiple database operations into a single atomic unit.
    // Retrieves all comments and their associated photos for a specific route (myIntTTWegNr).
    // Returns a Flow of a list of MyTTCommentANDWithPhotos, which is a data class representing the relationship.
    @Transaction
    @Query("SELECT * FROM MyTT_Comment_AND WHERE myIntTTWegNr = :myIntTTWegNr")
    fun getCommentWithPhotoByRoute(myIntTTWegNr: Int): Flow<List<Comments.MyTTCommentANDWithPhotos>>

    // Uses a @Transaction to combine multiple database operations into a single atomic unit.
    // Retrieves all comments and their associated photos for a specific summit (myIntTTGipfelNr).
    // Returns a Flow of a list of MyTTCommentANDWithPhotos, which is a data class representing the relationship.
    @Transaction
    @Query("SELECT * FROM MyTT_Comment_AND WHERE myIntTTGipfelNr = :myIntTTGipfelNr")
    fun getCommentWithPhotoBySummit(myIntTTGipfelNr: Int): Flow<List<Comments.MyTTCommentANDWithPhotos>>

    // Commented out query for retrieving summit comments with photos, filtered by a specific summit.
    // This query was likely used to fetch comments for a summit where no route was provided.
    //
    // @Query("SELECT * FROM MyTT_Comment_AND WHERE myIntTTGipfelNr = :myIntTTGipfelNr AND myIntTTWegNr IS NULL")
    // fun getSummitCommentWithPhotoBySummit(myIntTTGipfelNr: Int): Flow<List<Comments.MyTTCommentANDWithPhotos>>

    // Queries the database for distinct partner names from the MyTT_Comment_AND table.
    // This query returns a list of unique names of partners who ascended a specific route.
    // It uses the LIKE operator to allow partial name matching (e.g., "John%" would match "John Doe").
    @Query("SELECT DISTINCT(a.myAscendedPartner) FROM MyTT_Comment_AND a WHERE a.myAscendedPartner LIKE :partName ORDER BY LOWER(a.myAscendedPartner)")
    fun getDistinctPartner(partName: String): List<String>

    // endregion
}
