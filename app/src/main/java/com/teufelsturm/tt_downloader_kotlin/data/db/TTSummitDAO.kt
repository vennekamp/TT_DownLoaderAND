package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.teufelsturm.tt_downloader_kotlin.data.entity.CommentsSummit
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTSummitAND
import kotlinx.coroutines.flow.Flow

@Dao
interface TTSummitDAO {

    class TT_SummitBaseData {
        var strName: String? = null
        var strGebiet: String? = null
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(summit: TTSummitAND)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(summit: TTSummitAND)

    @Query("DELETE FROM TT_Summit_AND WHERE intTTGipfelNr = :intTTGipfelNr")
    fun deleteByTTGipfelNr(intTTGipfelNr: Int)

    @Query("SELECT strName, strGebiet from TT_Summit_AND WHERE intTTGipfelNr = :intTTGipfelNr")
    fun getBaseDataBySummit(intTTGipfelNr: Int): TT_SummitBaseData?

    @Query("SELECT * from TT_Summit_AND where intTTGipfelNr = :intTTGipfelNr")
    fun getSummit(intTTGipfelNr: Int): Flow<TTSummitAND>

    @Query("SELECT * from TT_Summit_AND where strName like :searchSummit and strGebiet in (:searchAreas)")
    fun getByName(searchSummit: String, searchAreas: String = "strGebiet"): List<TTSummitAND>?

    @Query(
        """SELECT strName from TT_Summit_AND
            where strName like :searchSummit and strGebiet in 
            (CASE WHEN length(:searchAreas) THEN (:searchAreas) ELSE (strGebiet) END) ORDER BY strName"""
    )
    fun getSummitNameForAutoText(searchSummit: String, searchAreas: String = ""): List<String>

    @Query("SELECT * from TT_Summit_AND")
    fun getAll(): LiveData<List<TTSummitAND>>

    @Query("SELECT '' AS strGebiet UNION Select Distinct strGebiet from TT_Summit_AND ORDER BY strGebiet")
    fun getDistictAreaNames(): LiveData<List<String>>

    @Query(
        """SELECT MAX(a.intAnzahlWege) FROM  TT_Summit_AND a
                        WHERE a.strGebiet = (CASE WHEN length(:searchAreas) THEN (:searchAreas) ELSE (strGebiet) END)
                           AND [a].[strName] LIKE :searchText;"""
    )
    suspend fun getMaxAnzahlDerWege(searchAreas: String = "", searchText: String = "%"): Int?

    @Query(
        """SELECT MAX(a.intAnzahlSternchenWege) from TT_Summit_AND a
                        WHERE a.strGebiet = (CASE WHEN length(:searchAreas) THEN (:searchAreas) ELSE (strGebiet) END)
                            AND [a].[strName] LIKE :searchText;"""
    )
    suspend fun getMaxAnzahlDerSternchenWege(
        searchAreas: String = "",
        searchText: String = "%"
    ): Int?

    @Query(
        """SELECT COUNT (DISTINCT(a._id))
                FROM   TT_Summit_AND a
                WHERE  a.intAnzahlWege BETWEEN :minAnzahlWege AND :maxAnzahlWege
                    AND a.intAnzahlSternchenWege BETWEEN :minAnzahlSternchenWege AND :maxAnzahlSternchenWege
                    AND a.strGebiet = (CASE WHEN length(:searchAreas) THEN (:searchAreas) ELSE (a.strGebiet) END)
                    AND a.strName LIKE :searchText;"""
    )
    suspend fun getConstrainedCount(
        minAnzahlWege: Int,
        maxAnzahlWege: Int,
        minAnzahlSternchenWege: Int,
        maxAnzahlSternchenWege: Int,
        searchAreas: String,
        searchText: String
    ): Int

    @Query(
        """SELECT COUNT (DISTINCT(a._id))
                FROM   TT_Summit_AND a, MyTT_Comment_AND b
                WHERE  a.intAnzahlWege BETWEEN :minAnzahlWege AND :maxAnzahlWege
                    AND a.intAnzahlSternchenWege BETWEEN :minAnzahlSternchenWege AND :maxAnzahlSternchenWege
                    AND a.strGebiet = (CASE WHEN length(:searchAreas) THEN (:searchAreas) ELSE (a.strGebiet) END)
                    AND a.intTTGipfelNr = b.myIntTTGipfelNr
                    AND a.strName LIKE :searchText;"""
    )
    suspend fun getConstrainedJustMineCount(
        minAnzahlWege: Int,
        maxAnzahlWege: Int,
        minAnzahlSternchenWege: Int,
        maxAnzahlSternchenWege: Int,
        searchAreas: String,
        searchText: String
    ): Int

    @Transaction
    @Query("SELECT * FROM TT_Summit_AND")
    fun getSummitsListWithMySummitComment(): LiveData<List<CommentsSummit.SummitWithMySummitComment>>

    @Query(
        """SELECT a.* FROM TT_Summit_AND a
                WHERE a.intAnzahlWege BETWEEN :minAnzahlWege AND :maxAnzahlWege
                    AND a.intAnzahlSternchenWege BETWEEN :minAnzahlSternchenWege AND :maxAnzahlSternchenWege
                    AND a.strGebiet = (CASE WHEN length(:searchAreas) THEN (:searchAreas) ELSE (a.strGebiet) END)
                    AND a.strName LIKE :searchText;"""
    )
    fun getConstrained(
        minAnzahlWege: Int,
        maxAnzahlWege: Int,
        minAnzahlSternchenWege: Int,
        maxAnzahlSternchenWege: Int,
        searchAreas: String,
        searchText: String
    ): LiveData<List<TTSummitAND>>

    @Query(
        """SELECT 
                    DISTINCT(a._id),
                      a._idTimeStamp, 
                      a.intTTGipfelNr, 
                      a.strName, 
                      a.dblGPS_Latitude, 
                      a.dblGPS_Longitude, 
                      a.strGebiet, 
                      a.intKleFuGipfelNr, 
                      a.intAnzahlWege, 
                      a.intAnzahlSternchenWege, 
                      a.strLeichtesterWeg, 
                      a.fltGPS_Altitude, 
                      a.osm_type, 
                      a.osm_ID, 
                      a.osm_display_name
                    FROM TT_Summit_AND a
                WHERE a.intAnzahlWege BETWEEN :minAnzahlWege AND :maxAnzahlWege
                    AND a.intAnzahlSternchenWege BETWEEN :minAnzahlSternchenWege AND :maxAnzahlSternchenWege
                    AND a.strGebiet = (CASE WHEN length(:searchAreas) THEN (:searchAreas) ELSE (a.strGebiet) END)
                    AND a.strName LIKE :searchText;"""
    )
    fun loadConstrainedSummitsAndMyComments(
        minAnzahlWege: Int,
        maxAnzahlWege: Int,
        minAnzahlSternchenWege: Int,
        maxAnzahlSternchenWege: Int,
        searchAreas: String,
        searchText: String
    ): Flow<List<CommentsSummit.SummitWithMySummitComment>>
    @Query(
        """SELECT 
                    DISTINCT(a._id),
                      a._idTimeStamp, 
                      a.intTTGipfelNr, 
                      a.strName, 
                      a.dblGPS_Latitude, 
                      a.dblGPS_Longitude, 
                      a.strGebiet, 
                      a.intKleFuGipfelNr, 
                      a.intAnzahlWege, 
                      a.intAnzahlSternchenWege, 
                      a.strLeichtesterWeg, 
                      a.fltGPS_Altitude, 
                      a.osm_type, 
                      a.osm_ID, 
                      a.osm_display_name
                    FROM TT_Summit_AND a, MyTT_Comment_AND b
                WHERE a.intAnzahlWege BETWEEN :minAnzahlWege AND :maxAnzahlWege
                    AND a.intAnzahlSternchenWege BETWEEN :minAnzahlSternchenWege AND :maxAnzahlSternchenWege
                    AND a.strGebiet = (CASE WHEN length(:searchAreas) THEN (:searchAreas) ELSE (a.strGebiet) END)
                    AND a.intTTGipfelNr = b.myIntTTGipfelNr
                    AND a.strName LIKE :searchText;"""
    )
    fun loadConstrainedSummitsAndMyCommentsJustMine(
        minAnzahlWege: Int,
        maxAnzahlWege: Int,
        minAnzahlSternchenWege: Int,
        maxAnzahlSternchenWege: Int,
        searchAreas: String,
        searchText: String
    ): Flow<List<CommentsSummit.SummitWithMySummitComment>>
}