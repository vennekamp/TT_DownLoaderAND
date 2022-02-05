package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.teufelsturm.tt_downloader_kotlin.data.entity.MyTTSummitAND
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
@LargeTest
class MyTTSummitDAO_DataBaseTest {

    // see https://stackoverflow.com/a/44991770 - synchronize async queries
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var myTTSummitDAO: MyTTSummitDAO
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
        myTTSummitDAO = db.myTTSummitDAO
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetMyComment() {
        val my_TT_Summit_AND = MyTTSummitAND()
        my_TT_Summit_AND.strMySummitComment = "TEST"
        my_TT_Summit_AND.myIntDateOfAscend = Calendar.getInstance().timeInMillis
        my_TT_Summit_AND.isAscendedSummit = true
        my_TT_Summit_AND.myIntTTGipfelNr = -123
        myTTSummitDAO.insert(my_TT_Summit_AND)

        repeat(15)
        {
            val my_TT_Summit_AND = MyTTSummitAND()
            my_TT_Summit_AND.strMySummitComment = createRandomString(100)
            my_TT_Summit_AND.myIntDateOfAscend = Calendar.getInstance().timeInMillis
            my_TT_Summit_AND.isAscendedSummit = true
            my_TT_Summit_AND.myIntTTGipfelNr = it - 50
            myTTSummitDAO.insert(my_TT_Summit_AND)
        }
        val myComment = myTTSummitDAO.get(-123)
        Assert.assertEquals("TEST", myComment.value?.strMySummitComment)
        myTTSummitDAO.deleteById(-123)
        Assert.assertNull("TEST", myTTSummitDAO.get(-123))
        repeat(15)
        {
            myTTSummitDAO.deleteById(it - 50)
            Assert.assertNull("TEST", myTTSummitDAO.get(it - 50))
        }
    }
}