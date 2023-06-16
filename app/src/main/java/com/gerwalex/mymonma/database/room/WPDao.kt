package com.gerwalex.mymonma.database.room

import androidx.room.Dao
import androidx.room.Query
import com.gerwalex.mymonma.database.room.MyConverter.NACHKOMMA
import com.gerwalex.mymonma.database.tables.Partnerstamm
import kotlinx.coroutines.flow.Flow
import java.sql.Date

@Dao
abstract class WPDao(val db: DB) {

    @Query(
        "select a.*" +
                "from Partnerstamm a " +
                "join WPStamm b on a.id = b.partnerid " +
                "where wpkenn = :wpkenn"
    )
    abstract fun getPartner(wpkenn: String): Flow<Partnerstamm?>

    /**
     * Marktwert aller Wertpapier/ Depots
     */
    @Query(
        "select total(marktwert) " +  //
                "from (" +  //
                "select sum(menge) / $NACHKOMMA * (SELECT kurs from WPKurs d where a.wpid = d.wpid " +
                "group by wpid having max(btag)) as marktwert " +  //
                "from WPTrx a " +  //
                "where paketid is null " +  //
                "group by wpid) "
    )
    abstract fun getWPMarktwert(): Long

    /**
     * Marktwert aller Wertpapier/ Depots zu einem Bestimmten Datum
     */
    @Query(
        "select total(marktwert) " +  //
                "from (" +  //
                "select sum(menge) / $NACHKOMMA * " +  //
                "(SELECT kurs from WPKurs d where a.wpid = d.wpid and btag <= :btag " +
                "group by wpid having max(btag)) as marktwert " +  //
                "from WPTrx a " +  //
                "where paketid is null " +  //
                "group by wpid) "
    )
    abstract fun getWPMarktwert(btag: Date): Long

}