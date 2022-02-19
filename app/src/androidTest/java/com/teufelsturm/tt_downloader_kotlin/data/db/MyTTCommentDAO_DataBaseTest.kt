package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTTCommentAND
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTTCommentPhotosAND
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
        val myTTRouteAND1 = MyTTCommentAND(myIntTTGipfelNr = 100, myIntTTWegNr = 100)
        myTTRouteAND1.strMyComment = "TEST"
        myTTRouteAND1.myIntDateOfAscend =
            convertLongToDateTimeString(Calendar.getInstance().timeInMillis - 100_000)
        myTTRouteAND1.myCommentTimStamp = Calendar.getInstance().timeInMillis
        myTTRouteAND1.isAscendedType = RouteAscentType.GETEILTEFUEHRUNG.ordinal() ?: 0
        myTTRouteAND1.myAscendedPartner = "HORST"
        myTTRouteAND1.myIntTTWegNr = -123
        myTTRouteAND1.Id = runBlocking { myTTCommentDAO.insert(myTTRouteAND1) }

        val myTTRouteAND2 =
            runBlocking { myTTCommentDAO.getMyTTCommentANDByID(myTTRouteAND1.Id).first() }
        Truth.assertThat(myTTRouteAND1.Id).isEqualTo(myTTRouteAND2.Id)
        Truth.assertThat(myTTRouteAND1.strMyComment).isEqualTo(myTTRouteAND2.strMyComment)
        Truth.assertThat(myTTRouteAND1.myIntDateOfAscend).isEqualTo(myTTRouteAND2.myIntDateOfAscend)
        Truth.assertThat(myTTRouteAND1.myCommentTimStamp).isEqualTo(myTTRouteAND2.myCommentTimStamp)
        Truth.assertThat(myTTRouteAND1.isAscendedType).isEqualTo(myTTRouteAND2.isAscendedType)
        Truth.assertThat(myTTRouteAND1.myAscendedPartner).isEqualTo(myTTRouteAND2.myAscendedPartner)
        Truth.assertThat(myTTRouteAND1.myIntTTWegNr).isEqualTo(myTTRouteAND2.myIntTTWegNr)
        Truth.assertThat(myTTRouteAND1.myIntTTGipfelNr).isEqualTo(myTTRouteAND2.myIntTTGipfelNr)
        myTTCommentDAO.deleteMyCommentById(myTTRouteAND2.Id)

        repeat(15)
        {
            val myTTRouteANDIN = MyTTCommentAND(myIntTTGipfelNr = 100, myIntTTWegNr = 100)
            myTTRouteANDIN.strMyComment = createRandomString(100)
            myTTRouteANDIN.myIntDateOfAscend =
                convertLongToDateTimeString(Calendar.getInstance().timeInMillis - 100_000)
            myTTRouteANDIN.myCommentTimStamp = Calendar.getInstance().timeInMillis
            myTTRouteANDIN.isAscendedType = RouteAscentType.GETEILTEFUEHRUNG.ordinal() ?: 0
            myTTRouteANDIN.myAscendedPartner = createRandomString(10)
            myTTRouteANDIN.myIntTTWegNr = it - 50
            myTTRouteANDIN.Id = runBlocking { myTTCommentDAO.insert(myTTRouteANDIN) }


            myTTCommentDAO.deleteMyCommentById(myTTRouteANDIN.Id)
            Assert.assertNull(
                "TEST",
                runBlocking { myTTCommentDAO.getMyTTCommentANDByID(myTTRouteANDIN.Id ).first() })
        }

        val myComment = runBlocking { myTTCommentDAO.getMyCommentANDByRoute(-123).first() }[0]
        Assert.assertEquals("TEST", myComment.strMyComment)
        runBlocking { myTTCommentDAO.deleteMyCommentById(myComment.Id) }
        Assert.assertNull(
            "TEST",
            runBlocking { myTTCommentDAO.getMyTTCommentANDByID(-123).first() })

    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetMyTT_RoutePhotos_AND() {
        val myTT_RoutePhotos_AND = MyTTCommentPhotosAND()
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
    fun CheckIfAnyEntriesMyTTRouteANDWithPhotosExist() {
        val entries = runBlocking { myTTCommentDAO.getAllCommentWithPhoto().first() }
        Assert.assertNotNull(entries)
        Assert.assertEquals(69, entries.count())
    }


    @Test
    @Throws(Exception::class)
    fun CheckIfAnyEntriesMyTTRouteANDWithPhotosByRouteIDExist() {
        val entries = runBlocking { myTTCommentDAO.getCommentWithPhotoByRoute(6287).first() }
        Assert.assertNotNull(entries)
        Assert.assertEquals(2, entries.count())
        val entry1 = entries[0].myTT_comment_PhotosANDList
        Assert.assertEquals(1, entry1.count())
    }
}