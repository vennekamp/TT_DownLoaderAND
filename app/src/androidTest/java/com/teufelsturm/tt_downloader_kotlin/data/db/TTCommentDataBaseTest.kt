package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.test.platform.app.InstrumentationRegistry
import com.teufelsturm.tt_downloader_kotlin.data.entity.Comments
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

class TTCommentDataBaseTest {


    private lateinit var ttCommentDAO: TTCommentDAO
    private lateinit var db: TTDataBase


    @Before
    fun createDb() {
        db = TTDataBase.getInstance(InstrumentationRegistry.getInstrumentation().targetContext)

//        val context = InstrumentationRegistry.getInstrumentation().targetContext
//        // Using an in-memory database because the information stored here disappears when the
//        // process is killed.
//        db = Room.databaseBuilder(
//            context,
//            TTDataBase::class.java,
//            "TT_DownLoader_AND_CommentTest2.sqlite"
//        )
//            // Room.inMemoryDatabaseBuilder(context, TT_Summit_DataBase::class.java)
//            // Allowing main thread queries, just for testing.
//            .allowMainThreadQueries()
//            .createFromAsset("TT_DownLoader_AND_Comment.sqlite")
//            .build()
        ttCommentDAO = db.ttCommentDAO
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetComment() {
        val tt_Comment_AND = Comments.TTCommentAND()
        tt_Comment_AND.strEntryKommentar = "TEST"
        tt_Comment_AND._id = -123
        ttCommentDAO.insert(tt_Comment_AND)
        val summit = ttCommentDAO.get(-123)
        Assert.assertEquals("TEST", summit?.strEntryKommentar)
        ttCommentDAO.deleteById(-123)
        Assert.assertNull("TEST", ttCommentDAO.get(-123))
    }
}