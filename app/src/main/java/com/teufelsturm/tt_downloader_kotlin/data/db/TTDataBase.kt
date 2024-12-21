package com.teufelsturm.tt_downloader_kotlin.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.teufelsturm.tt_downloader_kotlin.data.entity.*
// Constant representing an invalid ID, used as a default value for cases where no valid ID is available.
const val NO_ID = 0L

// The @Database annotation marks this class as a Room Database.
// It specifies the list of entities (tables) included in the database and defines the database version.
// 'exportSchema = false' indicates that Room should not export the database schema.
@Database(
    entities = [
        TTSummitAND::class,             // Represents a Summit entry in the TT system
        TTRouteAND::class,              // Represents a Route entry in the TT system
        MyTTCommentAND::class,          // Represents a personal comment on a TT route
        MyTTCommentPhotosAND::class,    // Represents photos linked to MyTT comments
        TTNeigbourSummitAND::class,     // Represents neighbor summits linked to a specific summit
        SummitTravSalePersOrder::class, // Represents travel sales or order-related data (potentially used for tracking visits)
        Comments.TTCommentAND::class    // Represents a comment linked to a route, potentially from the TT community
    ],
    version = 3,  // Version number for the database. It changes when the schema is updated.
    exportSchema = false // If true, Room will export the schema to a folder for documentation purposes.
)
abstract class TTDataBase : RoomDatabase() {

    // Abstract definitions for DAOs (Data Access Objects) to access the different tables in the database.
    // Room will automatically generate implementations for these DAOs at compile time.
    abstract val ttSummitDAO: TTSummitDAO          // DAO for accessing TTSummitAND table
    abstract val ttRouteDAO: TTRouteDAO            // DAO for accessing TTRouteAND table
    abstract val ttCommentDAO: TTCommentDAO        // DAO for accessing Comments.TTCommentAND table
    abstract val myTTCommentDAO: MyTTCommentDAO    // DAO for accessing MyTTCommentAND table
    abstract val ttNeighbourSummitANDDAO: TTNeighbourSummitANDDAO // DAO for accessing TTNeigbourSummitAND table

    companion object {
        // The Volatile annotation ensures that changes to this variable are immediately visible to all threads.
        @Volatile
        private var INSTANCE: TTDataBase? = null

        /**
         * Singleton pattern to ensure a single instance of the TTDataBase is created.
         * This method returns the existing instance of the database or creates a new one if it doesn't exist.
         *
         * @param context The context of the application, used to access the file system and assets.
         * @return The singleton instance of the TTDataBase.
         */
        fun getInstance(context: Context): TTDataBase {
            // The synchronized block ensures that only one thread can access this block at a time,
            // preventing multiple threads from creating multiple database instances.
            synchronized(this) {
                var instance = INSTANCE

                // If no instance exists, create a new database instance
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext, // Application context to ensure no memory leaks
                        TTDataBase::class.java,     // The class of the database to be created
                        "TT_DownLoader.sqlite"      // The name of the SQLite database file
                    )
                        // Allows Room to run queries on the main thread.
                        // This is not recommended for production, as it can block the UI thread.
                        .allowMainThreadQueries()

                        // If a database migration is not provided when the schema is changed,
                        // this strategy will delete the current database and recreate it.
                        // This is useful during development but risky in production as it deletes user data.
                        .fallbackToDestructiveMigration()

                        // Specifies that the initial data should be loaded from an asset file named "TT_DownLoader_AND.sqlite".
                        // This is useful when pre-populating the database with data on app installation.
                        .createFromAsset("TT_DownLoader_AND.sqlite")

                        // Builds the Room database and returns the instance.
                        .build()

                    // Cache the instance so it can be reused
                    INSTANCE = instance
                }
                // Return the database instance
                return instance
            }
        }
    }
}
