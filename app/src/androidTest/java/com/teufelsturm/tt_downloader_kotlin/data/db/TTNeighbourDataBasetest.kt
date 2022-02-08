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

    private lateinit var ttNeigbourSummitANDDAO: TTNeigbourSummitANDDAO
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
        ttNeigbourSummitANDDAO = db.ttNeigbourSummitANDDAO
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
            runBlocking { ttNeigbourSummitANDDAO.getTSPSummits(1).first().getIDs() }
        assertThat(neighbours1).isEqualTo(listOf(1121, 1122, 2, 3))
        val neighbours2 =
            runBlocking { ttNeigbourSummitANDDAO.getTSPSummits(353).first().getIDs() }
        assertThat(neighbours2).isEqualTo(listOf(354, 1121, 1122, 1))
        val neighbours3 =
            runBlocking { ttNeigbourSummitANDDAO.getTSPSummits(354).first().getIDs() }
        assertThat(neighbours3).isEqualTo(listOf(353, 1121, 1122, 1))
        val neighbours4 =
            runBlocking { ttNeigbourSummitANDDAO.getTSPSummits(1121).first().getIDs() }
        assertThat(neighbours4).isEqualTo(listOf(353, 354, 1122, 1))
        val neighbours5 =
            runBlocking { ttNeigbourSummitANDDAO.getTSPSummits(1093).first().getIDs() }
        assertThat(neighbours5).isEqualTo(listOf(1095, 1094, 1092, 1091))
        val neighbours6 =
            runBlocking { ttNeigbourSummitANDDAO.getTSPSummits(1092).first().getIDs() }
        assertThat(neighbours6).isEqualTo(listOf(1095, 1094, 1091, 1093))
        val neighbours7 =
            runBlocking { ttNeigbourSummitANDDAO.getTSPSummits(1091).first().getIDs() }
        assertThat(neighbours7).isEqualTo(listOf(1095, 1094, 1092, 1093))
        val neighbours8 =
            runBlocking { ttNeigbourSummitANDDAO.getTSPSummits(1110).first().getIDs() }
        assertThat(neighbours8).isEqualTo(listOf(887, 888, 812, 830))

        (1..1148).forEach {
            if (!listOf(1126).contains(it)) {
                assertThat(runBlocking {
                    "TSP-results count for $it: ${
                        ttNeigbourSummitANDDAO.getTSPSummits(
                            it
                        ).first().size
                    }"
                }).isEqualTo("TSP-results count for $it: 4")
            }
        }
    }

    private fun List<TTNeigbourANDTTName>.getIDs(): List<Int> {
        return listOf(
            this[0].intTTNachbarGipfelNr,
            this[1].intTTNachbarGipfelNr,
            this[2].intTTNachbarGipfelNr,
            this[3].intTTNachbarGipfelNr
        )
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
        val neighbour: TTNeigbourSummitAND = ttNeigbourSummitANDDAO.get(-123)
        Assert.assertEquals(12345L, neighbour._idTimStamp)
        ttNeigbourSummitANDDAO.deleteByID(-123)
        Assert.assertNull("TEST", ttNeigbourSummitANDDAO.get(-123))
    }
}