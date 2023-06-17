package com.gerwalex.mymonma.database.room

import androidx.room.Dao
import androidx.room.Query
import com.gerwalex.mymonma.database.room.MyConverter.NACHKOMMA
import com.gerwalex.mymonma.database.tables.Partnerstamm
import com.gerwalex.mymonma.database.tables.WPKurs
import com.gerwalex.mymonma.database.tables.WPStamm
import com.gerwalex.mymonma.database.views.WPStammView
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

    /**
     * Liefert die Liste der Wertpapiere, die zu einem bestimmten Zeitpunkt Bestände aufwiesen
     *
     * @param btag Datum, zu dem die Liste erstellt sein soll
     */
    @Query(
        "select a.*, name, total(menge) as bestand, total(einstand) as einstand " +
                ",(select total(einstand) from wptrx where wpid = a.id " +
                "and catid between 2001 and 2049) as gesamteinkaufpreis " +
                ",(select count(einstand) from wptrx where wpid = a.id " +
                "and catid between 2001 and 2049) as anzahlkauf " +
                ",(select total(amount) from CashTrx c where a.id = c.partnerid and btag <= :btag " +
                "and catid between 2101 and 2199) as income " +
                ",(select min(btag) from wptrx where wpid = a.id) as firstums " +
                ",(select max(btag) from wptrx where wpid = a.id) as lastums " +
                ",(select total(amount) from CashTrx  c where a.id = c.partnerid " +  //
                "and catid between 2001 and 2049) as gesamtkauf  " +  //
                ",(select total(amount) from CashTrx c where a.id = c.partnerid and btag <= :btag " +
                "and catid in(2201)) as kursverlust " +
                ",(select total(amount) from CashTrx c where a.id = c.partnerid and btag <= :btag " +
                "and catid in(2200)) as kursgewinn " +  //
                ",(SELECT kurs from WPKurs d where a.id = d.wpid and btag <= :btag " +
                "group by wpid having max(btag)) as lastkurs  " +  //
                ",(SELECT btag from WPKurs d  where a.id = d.wpid and btag <= :btag " +
                "group by wpid having max(btag)) as lastbtag  " +  //
                "from WPStamm a  " +  //
                "join Partnerstamm p on (p.id = a.partnerid) " +  //
                "left outer join WPTrx b on (b.wpid = a.id) " +  //
                "where paketid is null  " +  //
                "and btag <= :btag " +  //
                "group by wpid having bestand > 0 " +  //
                "order by name"
    )
    abstract fun getWPBestandListe(btag: Date): Flow<List<WPStammView>>

    /**
     * Liefert eine filterbare Liste aller Wertpapiere.
     */
    @Query(
        "select a.*, name, total(menge) as bestand, total(einstand) as einstand  " +  //
                ",(select total(einstand) from wptrx where wpid = a.id " +
                "and catid between 2001 and 2049) as gesamteinkaufpreis " +
                ",(select count(einstand) from wptrx where wpid = a.id " +
                "and catid between 2001 and 2049) as anzahlkauf " +
                ",(select min(btag) from wptrx where wpid = a.id) as firstums " +
                ",(select max(btag) from wptrx where wpid = a.id) as lastums " +
                ",(select total(amount) from CashTrx  c where a.id = c.partnerid " +  //
                "and catid between 2001 and 2049) as gesamtkauf  " +  //
                ",(select total(amount) from CashTrx c where a.id = c.partnerid " +  //
                "and catid in(2201)) as kursverlust " +  //
                ",(select total(amount) from CashTrx c where a.id = c.partnerid " +  //
                "and catid in(2200)) as kursgewinn " +  //
                ",(select total(amount) from CashTrx c where a.id = c.partnerid " +  //
                "and catid between 2101 and 2199) as income  " +  //
                ",(SELECT kurs from WPKurs d where a.id = d.wpid " + "group by wpid having max(btag)) as lastkurs  " +  //
                "from WPStamm a   " +  //
                "join Partnerstamm p on (p.id = a.partnerid) " +  //
                "left outer join WPTrx b on (b.wpid = a.id)  " +  //
                "where paketid is null  " +  //
                "and (name like '%' || :search || '%' or  wpkenn like '%' || :search || '%')  " +  //
                "and wptyp in (:filter)" +  //
                "group by wpid " +  //
                "order by name "
    )
    abstract fun getWPStammListe(search: String, filter: List<String>): List<WPStammView>

    @Query(
        "Select * from wpkurs where wpid = :wpid " +
                "order by btag "
    )
    abstract fun getWPKurse(wpid: Long): Flow<List<WPKurs>>

    @Query(
        "Select a.*  from wpstamm a " +
//                "join partnerstamm p on (a.partnerid = p.id) " +
                "where a.id = :wpid"
    )

    abstract suspend fun getWPStamm(wpid: Long): WPStamm

}