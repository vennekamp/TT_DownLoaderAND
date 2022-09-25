package com.teufelsturm.tt_downloader_kotlin.data.db

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTSummitAND
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
@LargeTest
class TTSummitDataBaseTest {

    private lateinit var tt_Summit_DAO: TTSummitDAO
    private lateinit var db: TTDataBase

    // see https://stackoverflow.com/a/44991770 - synchronize async queries
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.databaseBuilder(
            context,
            TTDataBase::class.java,
            "TT_DownLoader_AND_SummitTest.sqlite"
        )
            //db = Room.inMemoryDatabaseBuilder(context, TTDataBase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .setTransactionExecutor(Executors.newSingleThreadExecutor()) // <-- this makes all the difference for synchronous query
            .createFromAsset("TT_DownLoader_AND.sqlite")
            .build()
        tt_Summit_DAO = db.ttSummitDAO
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetSummit() {
        runBlocking {
            val tt_Summit_AND = TTSummitAND()
            tt_Summit_AND.strName = "TEST"
            tt_Summit_AND.intTTGipfelNr = -123
            tt_Summit_DAO.insert(tt_Summit_AND)
        }
        runBlocking {
            val summit = tt_Summit_DAO.getSummit(-123).first()
            Assert.assertEquals("TEST", summit.strName)
        }
        runBlocking {
            tt_Summit_DAO.deleteByTTGipfelNr(-123)
        }
        Assert.assertNull("TEST",runBlocking { tt_Summit_DAO.getSummit(-123).first() } )
    }

    @Test
    fun getSummitByID() {
        runBlocking {
            val tt_Summit_AND = tt_Summit_DAO.getSummit(12).first()
            Assert.assertNotNull("No summit object received.", tt_Summit_AND)
            Assert.assertEquals("Riegelkopf", tt_Summit_AND.strName)
        }
    }

    @Test
    fun getAllSummits() {
        val _tt_Summit_AND = runBlocking { tt_Summit_DAO.getAll().first() }

        // val data = com.teufelsturm.tt_downloader_kotlin.data.db_neu.getValueBlocking()

        Assert.assertNotNull("No List<TT_Summit_AND> object received.", _tt_Summit_AND)
        Assert.assertEquals(1147, _tt_Summit_AND.size)
    }

    @Test
    fun getSummitNameForAutoText() {
        val allSummitNames: List<String>? =
            tt_Summit_DAO.getSummitNameForAutoText("%bä%")
        Assert.assertNotNull("No List<TT_Summit_AND> object received.", allSummitNames)
        Assert.assertEquals(15, allSummitNames?.size)
        Assert.assertTrue(allSummitNames?.contains("Bärensteinnadel") ?: false)
        Assert.assertTrue(allSummitNames?.contains("Bärfangkegel") ?: false)
    }

    @Test
    @Throws(Exception::class)
    fun getSummitByAreaNameForAutoText() {
        val allSummitNames: List<String>? =
            tt_Summit_DAO.getSummitNameForAutoText("%Bä%", "Gebiet der Steine")
        Assert.assertNotNull("No List<TT_Summit_AND> object received.", allSummitNames)
        Assert.assertEquals(7, allSummitNames?.size)
        Assert.assertTrue(allSummitNames?.contains("Bärensteinnadel") ?: false)
        Assert.assertFalse(allSummitNames?.contains("Bärfangkegel") ?: false)
    }

    @Test
    @Throws(Exception::class)
    fun getSummitWithMySummitCommentTest() {
        val summitWithMySummitComment =
            runBlocking { tt_Summit_DAO.getSummitsListWithMySummitComment().first() }

        // val data = com.teufelsturm.tt_downloader_kotlin.data.db_neu.getValueBlocking()

        Assert.assertNotNull("TEST", summitWithMySummitComment)
        Assert.assertEquals(1147, summitWithMySummitComment.size)
    }

    @Test
    @Throws(Exception::class)
    fun loadConstrainedSummitsAndMyCommentsTest() {
        val summitWithMySummitComment = runBlocking {
            tt_Summit_DAO.loadConstrainedSummitsAndMyComments(
                minAnzahlWege = 0,
                maxAnzahlWege = 94,
                minAnzahlSternchenWege = 0,
                maxAnzahlSternchenWege = 34,
                searchAreas = "",
                searchText = "%Bärensteinwächter%"
            )
        }

        val data = runBlocking { summitWithMySummitComment.first() }

        Assert.assertNotNull("loadConstrainedSummitsAndMyCommentsTest", data)
        Assert.assertEquals(1, data.size)

        Log.d(
            "loadConstrainedSummitsAndMyCommentsTest",
            data[0].ttSummitAND.strName.toString()
        )
        Log.d(
            "loadConstrainedSummitsAndMyCommentsTest",
            data[0].ttSummitAND.strGebiet.toString()
        )
//        data[0].myTTCommentANDWithPhotos.forEach {
//            Log.d(
//                "loadConstrainedSummitsAndMyCommentsTest",
//                "comment: " + it.myTTCommentAND.strMyComment
//            )
//            Log.d(
//                "loadConstrainedSummitsAndMyCommentsTest",
//                "comment: " + it.myTTCommentAND.strMyComment
//            )
//        }

    }

}