package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTTCommentAND
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
@LargeTest
class MyTTCommentDAO_DataBaseTest4Summit {

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
        val myTTCommentAND = MyTTCommentAND(
            myIntTTGipfelNr = -100
        )
        myTTCommentAND.strMyComment = "TEST"
        myTTCommentAND.myCommentTimStamp = Calendar.getInstance().timeInMillis
        myTTCommentAND.isAscendedType = 1
        myTTCommentAND.Id = myTTCommentDAO.insert(myTTCommentAND)


        val myComment = runBlocking {   myTTCommentDAO.getMyTTCommentANDByID(myTTCommentAND.Id).first()}
        Assert.assertEquals("TEST", myComment.strMyComment)
        myTTCommentDAO.deleteMyCommentById(myComment.Id)
        Assert.assertNull("TEST", runBlocking {  myTTCommentDAO.getMyTTCommentANDByID(myTTCommentAND.Id).first()})

        repeat(15)
        {
            val myTTCommentAND_IN = MyTTCommentAND(
                myIntTTGipfelNr = -99
            )
            myTTCommentAND_IN.strMyComment = createRandomString(100)
            myTTCommentAND_IN.myCommentTimStamp = Calendar.getInstance().timeInMillis
            myTTCommentAND_IN.isAscendedType = 0
            myTTCommentAND_IN.Id = myTTCommentDAO.insert(myTTCommentAND_IN)

            val myTTCommentAND_OUT = runBlocking {   myTTCommentDAO.getMyTTCommentANDByID(myTTCommentAND_IN.Id).first()}
            myTTCommentDAO.deleteMyCommentById(myTTCommentAND_OUT.Id)
            Assert.assertNull("TEST", runBlocking {   myTTCommentDAO.getMyTTCommentANDByID(myTTCommentAND_IN.Id).first()})

        }
    }
}