package com.teufelsturm.tt_downloader_kotlin.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.teufelsturm.tt_downloader_kotlin.data.entity.*

const val NO_ID = 0L

@Database(
    entities = [
        TTSummitAND::class,
        // MyTTSummitAND::class,
        // MyTT_SummitPhotos_AND::class,

        TTRouteAND::class,
        MyTTCommentAND::class,
        MyTTCommentPhotosAND::class,

        TTNeigbourSummitAND::class,
        SummitTravSalePersOrder::class,
        Comments.TTCommentAND::class
    ], version = 2, exportSchema = false
)
abstract class TTDataBase : RoomDatabase() {

    abstract val ttSummitDAO: TTSummitDAO
    abstract val ttRouteDAO: TTRouteDAO
    abstract val ttCommentDAO: TTCommentDAO
    abstract val myTTCommentDAO: MyTTCommentDAO
    abstract val ttNeighbourSummitANDDAO: TTNeighbourSummitANDDAO

    companion object {
        @Volatile
        private var INSTANCE: TTDataBase? = null

        fun getInstance(context: Context): TTDataBase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TTDataBase::class.java,
                        "TT_DownLoader.sqlite"
                    )
                        .allowMainThreadQueries()
                        // .fallbackToDestructiveMigration()
                        .createFromAsset("TT_DownLoader_AND.sqlite")
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}