package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.teufelsturm.tt_downloader_kotlin.data.entity.Comments
import com.teufelsturm.tt_downloader_kotlin.data.entity.RouteWithMyCommentWithSummit
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTRouteAND
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
@LargeTest
class TTRoute_DataBaseTest {

    // see https://stackoverflow.com/a/44991770 - synchronize async queries
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var tt_Route_DAO: TTRouteDAO
    private lateinit var db: TTDataBase


    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.databaseBuilder(
            context,
            TTDataBase::class.java,
            "TT_DownLoader_AND_RouteTest2.sqlite"
        )
            // Room.inMemoryDatabaseBuilder(context, TT_Summit_DataBase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .setTransactionExecutor(Executors.newSingleThreadExecutor()) // <-- this makes all the difference for synchronous query
            .createFromAsset("TT_DownLoader_AND.sqlite")
            .build()
        tt_Route_DAO = db.ttRouteDAO
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


    @Test
    @Throws(Exception::class)
    fun insertAndGetRoute() {
        val tt_Route_AND = TTRouteAND()
        tt_Route_AND.WegName = "TEST"
        tt_Route_AND.intTTWegNr = -123
        runBlocking { tt_Route_DAO.insert(tt_Route_AND) }
        val route1 = runBlocking { tt_Route_DAO.getByRouteId(-123) }
        val data1 = runBlocking { route1.first() }
        Assert.assertEquals("TEST", data1.WegName)
        runBlocking { tt_Route_DAO.deleteByTTWegNr(-123) }
        val route2 = runBlocking { tt_Route_DAO.getByRouteId(-123) }
        Assert.assertNull("TEST", runBlocking { route2.first() })
    }

    @Test
    @Throws(Exception::class)
    fun getRouteByID() {
        val tt_Route_AND = runBlocking { tt_Route_DAO.getByRouteId(215) }
        val data = runBlocking { tt_Route_AND.first() }
        Assert.assertNotNull("No route object received.", data)
        Assert.assertEquals("Nordwestweg", data.WegName)
    }

    @Test
    @Throws(Exception::class)
    fun getAllRoute() {
        val allTT_Route_AND: LiveData<List<TTRouteAND>> = runBlocking { tt_Route_DAO.getAll() }
        val data = allTT_Route_AND.getValueBlocking()
        Assert.assertNotNull("No List<TT_Route_AND> object received.", data)
        Assert.assertEquals(12820, data?.size)
    }

    @Test
    @Throws(Exception::class)
    fun getRouteNameForAutoText() {
        val allRouteNames: List<String> =
            tt_Route_DAO.getRouteNameForAutoText("%bä%")
        Assert.assertNotNull("No List<TT_Summit_AND> object received.", allRouteNames)
        Assert.assertEquals(20, allRouteNames.size)
        Assert.assertTrue(allRouteNames.contains("Bärensiegel"))
        Assert.assertTrue(allRouteNames.contains("Bärenhunger"))
    }

    @Test
    @Throws(Exception::class)
    fun getSummitByAreaNameForAutoText() {
        val allRoutesNames: List<String> =
            tt_Route_DAO.getRouteNameForAutoText("%Bä%", "Gebiet der Steine")
        Assert.assertNotNull("No List<TT_Summit_AND> object received.", allRoutesNames)
        Assert.assertEquals(14, allRoutesNames.size)
        Assert.assertTrue(allRoutesNames.contains("Bärentanz"))
        Assert.assertFalse(allRoutesNames.contains("Bärenhunger"))
    }

    @Test
    @Throws(Exception::class)
    fun getRouteWithMyCommen() {
        val ttRouteANDList1: Comments.RouteWithMyComment =
            runBlocking { tt_Route_DAO.getRouteWithMySummitCommentByRoute(12).first() }
        Assert.assertNotNull("No List<RouteWithMyRouteComment> object received.", ttRouteANDList1)

        val ttRouteANDList2: List<Comments.RouteWithMyComment> =
            runBlocking { tt_Route_DAO.getRouteWithMySummitCommentBySummit(12).first() }

        Assert.assertNotNull("No List<RouteWithMyRouteComment> object received.", ttRouteANDList2)
        Assert.assertEquals(8, ttRouteANDList2.size)
    }


    @Test
    @Throws(Exception::class)
    fun getRouteWithMyCommentWithSummit() {
        val ttRouteANDList1: LiveData<RouteWithMyCommentWithSummit> =
            runBlocking { tt_Route_DAO.getRouteWithMyCommentWithSummit(6287) }
        val data1 = ttRouteANDList1.getValueBlocking()
        Assert.assertNotNull("No List<RouteWithMyCommentWithSummit> object received.", data1)
        Assert.assertNotNull(
            "No RouteWithMyCommentWithSummit.TTRouteAND object received.",
            data1?.ttRouteAND
        )
        Assert.assertNotNull(
            "No RouteWithMyCommentWithSummit.TTSummitAND object received.",
            data1?.ttSummitAND
        )
        Assert.assertNotNull(
            "No List<RouteWithMyCommentWithSummit> object received.",
            data1?.myTTCommentANDList
        )
        Assert.assertEquals(2, data1?.myTTCommentANDList?.size)
    }

    @Test
    @Throws(Exception::class)
    fun getRouteWithMyCommentWithSummitByRoute() {
        val ttRouteANDList1: LiveData<List<RouteWithMyCommentWithSummit>> =
            runBlocking { tt_Route_DAO.getRouteListWithMyCommentWithSummit(4) }
        val data1 = ttRouteANDList1.getValueBlocking()
        Assert.assertNotNull("No List<RouteWithMyCommentWithSummit> object received.", data1)
        Assert.assertNotNull(
            "No RouteWithMyCommentWithSummit.TTRouteAND object received.", data1
        )

        Assert.assertEquals(6, data1?.size)
        Assert.assertNotNull(
            "No List<RouteWithMyCommentWithSummit> object received.",
            data1?.get(0)?.myTTCommentANDList
        )
        Assert.assertEquals(2, data1?.get(0)?.myTTCommentANDList?.size)
    }
}