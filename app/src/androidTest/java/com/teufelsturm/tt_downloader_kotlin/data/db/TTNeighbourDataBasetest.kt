package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTNeigbourANDTTName
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTNeigbourSummitAND
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.*
import java.io.IOException

class TTNeighbourDataBasetest {


    // see https://stackoverflow.com/a/44991770 - synchronize async queries
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var ttNeighbourSummitANDDAO: TTNeighbourSummitANDDAO
    private lateinit var db: TTDataBase


    @Before
    fun createDb() {
        db = TTDataBase.getInstance(InstrumentationRegistry.getInstrumentation().targetContext)
        val context = InstrumentationRegistry.getInstrumentation().targetContext
//        // Using an in-memory database because the information stored here disappears when the
//        // process is killed.
//        db = Room.databaseBuilder(
//            context,
//            TTDataBase::class.java,
//            "TT_DownLoader_AND_NeighbourTest.sqlite"
//        )
//            // Room.inMemoryDatabaseBuilder(context, TTDataBase::class.java)
//            // Allowing main thread queries, just for testing.
//            .allowMainThreadQueries()
//            .createFromAsset("TT_DownLoader_AND_Neighbour.sqlite")
//            .build()
        ttNeighbourSummitANDDAO = db.ttNeighbourSummitANDDAO
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun getNext2AndPrev2TravSalePersNeighbours() {
        val neighbours1 =
            runBlocking { ttNeighbourSummitANDDAO.getTSPSummits(353).first().getIDs() }
        assertThat(neighbours1).isEqualTo(listOf(354, 1121, 1122, 1))
        val neighbours2 =
            runBlocking { ttNeighbourSummitANDDAO.getTSPSummits(1).first().getIDs() }
        assertThat(neighbours2).isEqualTo(listOf(1121, 1122, 2, 3))
        val neighbours3 =
            runBlocking { ttNeighbourSummitANDDAO.getTSPSummits(354).first().getIDs() }
        assertThat(neighbours3).isEqualTo(listOf(353, 1121, 1122, 1))
        val neighbours4 =
            runBlocking { ttNeighbourSummitANDDAO.getTSPSummits(1121).first().getIDs() }
        assertThat(neighbours4).isEqualTo(listOf(353, 354, 1122, 1))
        val neighbours5 =
            runBlocking { ttNeighbourSummitANDDAO.getTSPSummits(1093).first().getIDs() }
        assertThat(neighbours5).isEqualTo(listOf(1095, 1094, 1092, 1091))
        val neighbours6 =
            runBlocking { ttNeighbourSummitANDDAO.getTSPSummits(1092).first().getIDs() }
        assertThat(neighbours6).isEqualTo(listOf(1095, 1094, 1091, 1093))
        val neighbours7 =
            runBlocking { ttNeighbourSummitANDDAO.getTSPSummits(1091).first().getIDs() }
        assertThat(neighbours7).isEqualTo(listOf(1095, 1094, 1092, 1093))
        val neighbours8 =
            runBlocking { ttNeighbourSummitANDDAO.getTSPSummits(1110).first().getIDs() }
        assertThat(neighbours8).isEqualTo(listOf(887, 888, 812, 811))

        (1..1148).forEach {
            if (!listOf(927,989,1008,1009,1010,1011,1024,1026,1065,1126).contains(it)) {
                assertThat(runBlocking {
                    "TSP-results count for $it: ${
                        ttNeighbourSummitANDDAO.getTSPSummits(
                            it
                        ).first().size
                    }"
                }).isEqualTo("TSP-results count for $it: 4")
            }
        }
    }

    private fun List<TTNeigbourANDTTName>.getIDs(): List<Int> {
        val rtnList = ArrayList<Int>()
        for (item in this)
        {
            rtnList.add(item.intTTNachbarGipfelNr)
        }
        return rtnList
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetRoute() {
        val ttNeigbourSummitAND = TTNeigbourSummitAND()
        ttNeigbourSummitAND._id = -123
        ttNeigbourSummitAND._idTimStamp = 12345L
        ttNeigbourSummitAND.intTTHauptGipfelNr = 1
        ttNeigbourSummitAND.intTTNachbarGipfelNr = 2
        ttNeighbourSummitANDDAO.insert(ttNeigbourSummitAND)
        val neighbour: TTNeigbourSummitAND = ttNeighbourSummitANDDAO.get(-123)
        Assert.assertEquals(12345L, neighbour._idTimStamp)
        ttNeighbourSummitANDDAO.deleteByID(-123)
        Assert.assertNull("TEST", ttNeighbourSummitANDDAO.get(-123))
    }
}