package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTTRouteAND
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTT_RoutePhotos_AND
import com.teufelsturm.tt_downloader_kotlin.results.adapter.util.RouteAscentType
import com.teufelsturm.tt_downloader_kotlin.searches.generics.convertLongToDateTimeString
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
@LargeTest
class MyTTRouteDAO_DataBaseTest {

    // see https://stackoverflow.com/a/44991770 - synchronize async queries
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var myTTRouteDAO: MyTTRouteDAO
    private lateinit var db: TTDataBase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.databaseBuilder(
            context,
            TTDataBase::class.java,
            "TT_DownLoader_AND_CommentTest2.sqlite"
        )
            // Room.inMemoryDatabaseBuilder(context, TT_Summit_DataBase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .createFromAsset("TT_DownLoader_AND.sqlite")
            .build()
        myTTRouteDAO = db.myTTRouteDAO
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetMyComment() {
        val myTTRouteAND = MyTTRouteAND(myIntTTWegNr = 100)
        myTTRouteAND.strMyRouteComment = "TEST"
        myTTRouteAND.myIntDateOfAscendRoute =  convertLongToDateTimeString(  Calendar.getInstance().timeInMillis - 100_000)
        myTTRouteAND.myRouteCommentTimStamp = Calendar.getInstance().timeInMillis
        myTTRouteAND.isAscendedRouteType = RouteAscentType.GETEILTEFUEHRUNG.ordinal()
        myTTRouteAND.myAscendedPartner = "HORST"
        myTTRouteAND.myIntTTWegNr = -123
        runBlocking { myTTRouteDAO.insert(myTTRouteAND) }

        repeat(15)
        {
            val myTTRouteAND1 = MyTTRouteAND(myIntTTWegNr = 100)
            myTTRouteAND1.strMyRouteComment = createRandomString(100)
            myTTRouteAND1.myIntDateOfAscendRoute = convertLongToDateTimeString( Calendar.getInstance().timeInMillis - 100_000)
            myTTRouteAND1.myRouteCommentTimStamp = Calendar.getInstance().timeInMillis
            myTTRouteAND1.isAscendedRouteType = RouteAscentType.GETEILTEFUEHRUNG.ordinal()
            myTTRouteAND1.myAscendedPartner = createRandomString(10)
            myTTRouteAND1.myIntTTWegNr = it - 50
            runBlocking { myTTRouteDAO.insert(myTTRouteAND1) }
        }

        val myComment = runBlocking { myTTRouteDAO.getMyTTRouteAND(-123).first() }
        Assert.assertEquals("TEST", myComment.strMyRouteComment)
        runBlocking { myTTRouteDAO.deleteMyCommentById(-123) }
        Assert.assertNull("TEST", runBlocking { myTTRouteDAO.getMyTTRouteAND(-123).first() })

        repeat(15)
        {
            runBlocking {
                myTTRouteDAO.deleteMyCommentById((it - 50).toLong())
                Assert.assertNull("TEST", runBlocking { myTTRouteDAO.getMyTTRouteAND(it - 50).first() })
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetMyTT_RoutePhotos_AND() {
        val myTT_RoutePhotos_AND = MyTT_RoutePhotos_AND()
        myTT_RoutePhotos_AND.Id = -123L
        myTT_RoutePhotos_AND.commentID = -1023L
        myTT_RoutePhotos_AND.uri = "somewhere"
        myTT_RoutePhotos_AND.caption = "TEST"
        runBlocking { myTTRouteDAO.insert(myTT_RoutePhotos_AND) }

        val myTT_RoutePhotos_AND1 = runBlocking { myTTRouteDAO.getPhotosByID(-123).first() }
        Assert.assertEquals("TEST", myTT_RoutePhotos_AND1.caption)
        runBlocking { myTTRouteDAO.deletePhotoById(-123) }
        Assert.assertNull("TEST", runBlocking { myTTRouteDAO.getPhotosByID(-123).first() })
    }

    @Test
    @Throws(Exception::class)
    fun CheckIfAnyEntriesMyTTRouteANDWithPhotosExist(){
        val entries = runBlocking { myTTRouteDAO.getAllCommentWithPhoto().first() }
        Assert.assertNotNull(entries)
        Assert.assertEquals(4, entries.count())
    }


    @Test
    @Throws(Exception::class)
    fun CheckIfAnyEntriesMyTTRouteANDWithPhotosByRouteIDExist(){
        val entries = runBlocking { myTTRouteDAO.getCommentWithPhoto(6287).first() }
        Assert.assertNotNull(entries)
        Assert.assertEquals(2, entries.count())
        val entry1 = entries[0].myTT_Route_PhotosANDList
        Assert.assertEquals(1, entry1.count())
    }
}