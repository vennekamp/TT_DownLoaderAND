package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTNeigbourSummitAND
import org.junit.*
import java.io.IOException

class TTNeighbourDataBasetest {


    // see https://stackoverflow.com/a/44991770 - synchronize async queries
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var ttNeigbourSummitANDDAO: TTNeigbourSummitANDDAO
    private lateinit var db: TTDataBase


    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.databaseBuilder(
            context,
            TTDataBase::class.java,
            "TT_DownLoader_AND_NeighbourTest.sqlite"
        )
            // Room.inMemoryDatabaseBuilder(context, TT_Summit_DataBase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .createFromAsset("TT_DownLoader_AND.sqlite")
            .build()
        ttNeigbourSummitANDDAO = db.ttNeigbourSummitANDDAO
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


    @Test
    @Throws(Exception::class)
    fun insertAndGetRoute() {
        val ttNeigbourSummitAND = TTNeigbourSummitAND()
        ttNeigbourSummitAND._id = -123
        ttNeigbourSummitAND._idTimStamp = 12345L
        ttNeigbourSummitAND.intTTHauptGipfelNr = 1
        ttNeigbourSummitAND.intTTNachbarGipfelNr = 2
        ttNeigbourSummitANDDAO.insert(ttNeigbourSummitAND)
        val neighbour : TTNeigbourSummitAND = ttNeigbourSummitANDDAO.get(-123)
        Assert.assertEquals(12345L, neighbour?._idTimStamp)
        ttNeigbourSummitANDDAO.deleteByID(-123)
        Assert.assertNull("TEST", ttNeigbourSummitANDDAO.get(-123))
    }

}