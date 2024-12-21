package com.teufelsturm.tt_downloader_kotlin.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.teufelsturm.tt_downloader_kotlin.data.entity.SummitTravSalePersOrder
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTNeigbourANDTTName
import com.teufelsturm.tt_downloader_kotlin.data.entity.TTNeigbourSummitAND
import kotlinx.coroutines.flow.*
// The @Dao annotation marks this interface as a Data Access Object (DAO) for Room Database.
// This DAO provides methods for interacting with the TT_NeigbourSummit_AND and Summit_TravSalePers_Order tables.
@Dao
interface TTNeighbourSummitANDDAO {

    /**
     * Inserts a new TTNeigbourSummitAND entry into the TT_NeigbourSummit_AND table.
     * If a conflict occurs (like a duplicate primary key), the existing entry is replaced.
     *
     * @param summit The TTNeigbourSummitAND object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(summit: TTNeigbourSummitAND)

    /**
     * Updates an existing TTNeigbourSummitAND entry in the TT_NeigbourSummit_AND table.
     * If a conflict occurs, the existing entry is replaced.
     *
     * @param summit The TTNeigbourSummitAND object to be updated.
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(summit: TTNeigbourSummitAND)

    /**
     * Retrieves all TTNeigbourSummitAND entries from the TT_NeigbourSummit_AND table.
     * This method returns a LiveData object, which allows for automatic UI updates when data changes.
     *
     * @return LiveData containing a list of all TTNeigbourSummitAND entries.
     */
    @Query("SELECT * FROM TT_NeigbourSummit_AND")
    fun getAll(): LiveData<List<TTNeigbourSummitAND>>

    /**
     * Retrieves a specific TTNeigbourSummitAND entry from the TT_NeigbourSummit_AND table by its ID.
     *
     * @param id The ID of the TTNeigbourSummitAND entry to retrieve.
     * @return The TTNeigbourSummitAND object with the specified ID.
     */
    @Query("SELECT * FROM TT_NeigbourSummit_AND WHERE _id = :id")
    fun get(id: Int): TTNeigbourSummitAND

    /**
     * Deletes a TTNeigbourSummitAND entry from the TT_NeigbourSummit_AND table by its ID.
     *
     * @param id The ID of the TTNeigbourSummitAND entry to delete.
     */
    @Query("DELETE FROM TT_NeigbourSummit_AND WHERE _id = :id")
    fun deleteByID(id: Int)

    /**
     * Retrieves all neighboring summits of a specific main summit (intTTHauptGipfelNr) from the TT_NeigbourSummit_AND table.
     * Uses a JOIN to fetch additional information from the TT_Summit_AND table, such as the name and GPS coordinates.
     *
     * @param intTTHauptGipfelNr The main summit number to find its neighboring summits.
     * @return A Flow of a list of TTNeigbourANDTTName objects with neighboring summit information.
     */
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH) // Suppresses warnings related to mismatched cursor columns.
    @RewriteQueriesToDropUnusedColumns // Optimizes the query to remove unused columns from the cursor.
    @Query(
        """SELECT a.*, 
                  b.strName, 
                  b.dblGPS_Latitude, 
                  b.dblGPS_Longitude 
           FROM TT_NeigbourSummit_AND a 
           JOIN TT_Summit_AND b 
           ON a.intTTNachbarGipfelNr = b.intTTGipfelNr 
           WHERE a.intTTHauptGipfelNr = :intTTHauptGipfelNr 
           ORDER BY a.intTTNachbarGipfelNr;"""
    )
    fun getNeighbours(intTTHauptGipfelNr: Int): Flow<List<TTNeigbourANDTTName>>

    /**
     * Inserts a new SummitTravSalePersOrder entry into the Summit_TravSalePers_Order table.
     * If a conflict occurs, the existing entry is replaced.
     *
     * @param summit The SummitTravSalePersOrder object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(summit: SummitTravSalePersOrder)

    /**
     * Updates an existing SummitTravSalePersOrder entry in the Summit_TravSalePers_Order table.
     * If a conflict occurs, the existing entry is replaced.
     *
     * @param summit The SummitTravSalePersOrder object to be updated.
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(summit: SummitTravSalePersOrder)

    /**
     * Retrieves neighboring summits based on the range of Summit_TravSalePers_Order IDs around a given summit.
     * This method uses a subquery to calculate the range of IDs relative to the given summit.
     * It excludes the given summit from the results and orders them by their ID.
     *
     * @param intTTGipfelNr The ID of the current summit.
     * @param from The offset to select records before the summit.
     * @param to The offset to select records after the summit.
     * @return A Flow of a list of TTNeigbourANDTTName objects with neighboring summit information.
     */
    @Query(
        """SELECT DISTINCT(a.intTTGipfelNr) AS intTTNachbarGipfelNr, 
                  b.strName, 
                  b.dblGPS_Latitude, 
                  b.dblGPS_Longitude 
           FROM Summit_TravSalePers_Order a 
           JOIN TT_Summit_AND b 
           ON a.intTTGipfelNr = b.intTTGipfelNr 
           WHERE a._id BETWEEN (SELECT l._id + :from FROM Summit_TravSalePers_Order l WHERE l.intTTGipfelNr = :intTTGipfelNr)  
           AND (SELECT u._id + :to FROM Summit_TravSalePers_Order u WHERE u.intTTGipfelNr = :intTTGipfelNr) 
           AND a.intTTGipfelNr != :intTTGipfelNr 
           ORDER BY a._id;"""
    )
    fun getNext2AndPrev2TravSalePersNeighbours(intTTGipfelNr: Int, from: Int, to: Int): Flow<List<TTNeigbourANDTTName>>

    /**
     * Determines the appropriate neighboring summits for specific summit IDs (intTTGipfelNr).
     * Depending on the summit, it calls `getNext2AndPrev2TravSalePersNeighbours` with different "from" and "to" ranges.
     *
     * This method also accounts for blocked summits that may exist in the database but should be excluded from results.
     *
     * @param intTTGipfelNr The ID of the summit for which neighboring summits are to be retrieved.
     * @return A Flow of a list of TTNeigbourANDTTName objects with neighboring summit information.
     */
    suspend fun getTSPSummits(intTTGipfelNr: Int): Flow<List<TTNeigbourANDTTName>> {
        if (intTTGipfelNr == 353) return getNext2AndPrev2TravSalePersNeighbours(intTTGipfelNr, 1, 4)
        if (intTTGipfelNr == 354) return getNext2AndPrev2TravSalePersNeighbours(intTTGipfelNr, -1, 3)
        if (intTTGipfelNr == 1093) return getNext2AndPrev2TravSalePersNeighbours(intTTGipfelNr, -4, -1)
        if (intTTGipfelNr == 1091) return getNext2AndPrev2TravSalePersNeighbours(intTTGipfelNr, -3, 1)

        // List of blocked summits in the TT summit database (not to be processed or displayed)
        /*
            Blocked Summits:
            927  Försterlochturm
            989  Kleiner Turm
            1008 Adlerlochturm
            1009 Wobstspitze
            1010 Schwarzschlüchteturm
            1011 Schwarze Spitze
            1024 Litfaßsäule
            1026 Hirschsuhlenturm
            1065 Slawe
        */

        // For other summits, use a default range of -2 to +2 from the current summit.
        return getNext2AndPrev2TravSalePersNeighbours(intTTGipfelNr, -2, 2)
    }
}
