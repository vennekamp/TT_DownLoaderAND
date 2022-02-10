package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTTCommentAND
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTT_RoutePhotos_AND
import com.teufelsturm.tt_downloader_kotlin.feature.inputs.adapter.CarouselViewAdapter
import com.teufelsturm.tt_downloader_kotlin.feature.inputs.vm.CustomCarouselViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MyTTRouteWriteReadTest {


    private lateinit var db: TTDataBase
    private lateinit var myTTCommentDAO: MyTTCommentDAO

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = TTDataBase.getInstance(context)

//            Room.databaseBuilder(
//            context,
//            TTDataBase::class.java,
//            "TT_DownLoader_AND_RouteTest_RW.sqlite"
//        )
//            // Room.inMemoryDatabaseBuilder(context, TT_Summit_DataBase::class.java)
//            // Allowing main thread queries, just for testing.
//            .allowMainThreadQueries()
//            .setTransactionExecutor(Executors.newSingleThreadExecutor()) // <-- this makes all the difference for synchronous query
//            .createFromAsset("TT_DownLoader_AND.sqlite")
//            .build()
        myTTCommentDAO = db.myTTCommentDAO
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        // db.close()
    }

    @Test
    fun SimpleTestOfMyTTRouteDAOReadWrite() {
        val myTTCommentAND: MyTTCommentAND = MyTTCommentAND(myIntTTWegNr = 100).apply {
            Id = 0L
            isAscendedRouteType = 8
            myAscendedPartner = "Ein Partner"
            myIntDateOfAscend = "9.9.2021 14:35"
            strMyComment = "Tolll war es!"
        }
        val rowID = myTTCommentDAO.insert(myTTCommentAND)
        assertThat(rowID).isGreaterThan(100)
        val back1 = runBlocking { myTTCommentDAO.getMyTTRouteANDByID(rowID).first() }
        assertThat(back1.myIntTTWegNr).isEqualTo(100)
        assertThat(back1.isAscendedRouteType).isEqualTo(8)
        assertThat(back1.myAscendedPartner).isEqualTo("Ein Partner")
        assertThat(back1.myIntDateOfAscend).isEqualTo("9.9.2021 14:35")
        assertThat(back1.strMyComment).isEqualTo("Tolll war es!")

        back1.strMyComment = "Doch nicht so schön...."
        runBlocking { myTTCommentDAO.update(back1) }
        val back2 = runBlocking { myTTCommentDAO.getMyTTRouteANDByID(back1.Id).first() }
        assertThat(back2.myIntTTWegNr).isEqualTo(100)
        assertThat(back2.isAscendedRouteType).isEqualTo(8)
        assertThat(back2.myAscendedPartner).isEqualTo("Ein Partner")
        assertThat(back2.myIntDateOfAscend).isEqualTo("9.9.2021 14:35")
        assertThat(back2.strMyComment).isEqualTo("Doch nicht so schön....")
    }

    @Test
    fun TestOfMyTTRouteDAOViaCode() {

        val myTTCommentAND: MyTTCommentAND = MyTTCommentAND(myIntTTWegNr = 0).apply {
            Id = 0
            isAscendedRouteType = 9
            myAscendedPartner = "test Partner"
            myIntDateOfAscend = "Mi., 2.Feb. 2022"
            myCommentTimStamp = 1643817660625
            strMyComment = "test Kommentar "
        }
        val myTT_RoutePhotos_AND0 = MyTT_RoutePhotos_AND().apply {
            Id = 0
            caption = "DSC_0005.JPG"
            uri =
                "content://com.android.externalstorage.documents/document/primary%3ADCIM%2F100ANDRO%2FDSC_0005.JPG"
        }
        val myTT_RoutePhotos_AND1 =
            MyTT_RoutePhotos_AND(0, 0, CarouselViewAdapter.ADD_IMAGE, "Bild hinzufügen...")

        val customCarouselViewModel0 = CustomCarouselViewModel(myTT_RoutePhotos_AND0)
        val customCarouselViewModel1 = CustomCarouselViewModel(myTT_RoutePhotos_AND1)

        val carouselItemViewModels =
            mutableListOf(customCarouselViewModel0, customCarouselViewModel1)

        saveModifiedComment(myTTCommentAND, carouselItemViewModels)


    }

    private fun saveModifiedComment(
        myTTCommentAND: MyTTCommentAND,
        carouselItemViewModels: MutableList<CustomCarouselViewModel>
    ) {
        val rowID: Long =
            if (myTTCommentAND.Id == NO_ID) {
                runBlocking { myTTCommentDAO.insert(myTTCommentAND) }
            } else {
                runBlocking { myTTCommentDAO.update(myTTCommentAND) }
                myTTCommentAND.Id
            }
        carouselItemViewModels.forEach {
            if (it.getMyTT_RoutePhotos_AND().uri != CarouselViewAdapter.ADD_IMAGE) {
                val commentPhoto = it.getMyTT_RoutePhotos_AND()
                commentPhoto.commentID = rowID
                if (commentPhoto.Id == NO_ID) {
                    runBlocking { myTTCommentDAO.insert(commentPhoto) }
                } else {
                    runBlocking { myTTCommentDAO.update(commentPhoto) }
                }
            }
        }
    }

}