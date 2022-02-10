package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTTCommentAND
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTT_RoutePhotos_AND
import com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util.RouteAscentType
import com.teufelsturm.tt_downloader_kotlin.feature.searches.generics.convertLongToDateTimeString
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
@LargeTest
class MyTTCommentDAO_DataBaseTest {

    // see https://stackoverflow.com/a/44991770 - synchronize async queries
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var myTTCommentDAO: MyTTCommentDAO
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
        myTTCommentDAO = db.myTTCommentDAO
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetMyComment() {
        val myTTRouteAND = MyTTCommentAND(intTTGipfelNr = 100, myIntTTWegNr = 100)
        myTTRouteAND.strMyComment = "TEST"
        myTTRouteAND.myIntDateOfAscend =  convertLongToDateTimeString(  Calendar.getInstance().timeInMillis - 100_000)
        myTTRouteAND.myCommentTimStamp = Calendar.getInstance().timeInMillis
        myTTRouteAND.isAscendedType = RouteAscentType.GETEILTEFUEHRUNG.ordinal() ?: 0
        myTTRouteAND.myAscendedPartner = "HORST"
        myTTRouteAND.myIntTTWegNr = -123
        runBlocking { myTTCommentDAO.insert(myTTRouteAND) }

        repeat(15)
        {
            val myTTRouteAND1 = MyTTCommentAND(intTTGipfelNr = 100, myIntTTWegNr = 100)
            myTTRouteAND1.strMyComment = createRandomString(100)
            myTTRouteAND1.myIntDateOfAscend = convertLongToDateTimeString( Calendar.getInstance().timeInMillis - 100_000)
            myTTRouteAND1.myCommentTimStamp = Calendar.getInstance().timeInMillis
            myTTRouteAND1.isAscendedType = RouteAscentType.GETEILTEFUEHRUNG.ordinal() ?: 0
            myTTRouteAND1.myAscendedPartner = createRandomString(10)
            myTTRouteAND1.myIntTTWegNr = it - 50
            runBlocking { myTTCommentDAO.insert(myTTRouteAND1) }
        }

        val myComment = runBlocking { myTTCommentDAO.getMyTTRouteAND(-123).first() }
        Assert.assertEquals("TEST", myComment.strMyComment)
        runBlocking { myTTCommentDAO.deleteMyCommentById(-123) }
        Assert.assertNull("TEST", runBlocking { myTTCommentDAO.getMyTTRouteAND(-123).first() })

        repeat(15)
        {
            runBlocking {
                myTTCommentDAO.deleteMyCommentById((it - 50).toLong())
                Assert.assertNull("TEST", runBlocking { myTTCommentDAO.getMyTTRouteAND(it - 50).first() })
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
        runBlocking { myTTCommentDAO.insert(myTT_RoutePhotos_AND) }

        val myTT_RoutePhotos_AND1 = runBlocking { myTTCommentDAO.getPhotosByID(-123).first() }
        Assert.assertEquals("TEST", myTT_RoutePhotos_AND1.caption)
        runBlocking { myTTCommentDAO.deletePhotoById(-123) }
        Assert.assertNull("TEST", runBlocking { myTTCommentDAO.getPhotosByID(-123).first() })
    }

    @Test
    @Throws(Exception::class)
    fun CheckIfAnyEntriesMyTTRouteANDWithPhotosExist(){
        val entries = runBlocking { myTTCommentDAO.getAllCommentWithPhoto().first() }
        Assert.assertNotNull(entries)
        Assert.assertEquals(4, entries.count())
    }


    @Test
    @Throws(Exception::class)
    fun CheckIfAnyEntriesMyTTRouteANDWithPhotosByRouteIDExist(){
        val entries = runBlocking { myTTCommentDAO.getCommentWithPhoto(6287).first() }
        Assert.assertNotNull(entries)
        Assert.assertEquals(2, entries.count())
        val entry1 = entries[0].myTT_route_PhotosANDList
        Assert.assertEquals(1, entry1.count())
    }
}