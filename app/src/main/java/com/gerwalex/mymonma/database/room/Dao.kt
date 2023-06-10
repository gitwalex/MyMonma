package com.gerwalex.mymonma.database.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.gerwalex.mymonma.database.tables.CashTrx
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.database.tables.CatClass
import com.gerwalex.mymonma.database.tables.Partnerstamm
import com.gerwalex.mymonma.ui.screens.CashTrxView
import kotlinx.coroutines.flow.Flow

@Dao
abstract class Dao(val db: DB) {
    @Query("Select * from Partnerstamm")
    abstract fun getPartnerstammdaten(): Flow<List<Partnerstamm>>

    @Query("Select * from Partnerstamm where name like '%'||:filter ||'%' order by name")
    abstract fun getPartnerlist(filter: String): Flow<List<Partnerstamm>>

    @Query(
        "Select * from Cat " +//
                "where name like '%'|| :filter||'%' and not ausgeblendet and supercatid != 1002 " +  //
                "order by cnt desc, name"
    )
    abstract fun getCatlist(filter: String): Flow<List<Cat>>

    @Query("Select * from CatClass where name like '%'||:filter ||'%' order by name")
    abstract fun getCatClasslist(filter: String): Flow<List<CatClass>>

    @Query("Select * from Cat where name like '%'||:filter ||'%' and catclassid = 2 order by name")
    abstract fun getAccountlist(filter: String): Flow<List<Cat>>

    /**
     * Ermittlung Saldo zu CashKonto!
     */
    @Query(
        "select sum(amount) " +
                "from CashTrx b " +
                "where accountid = :accountid " +
                "and (transferid is null or (SELECT catclassid from Cat where id = b.catid) =2)"
    )
    abstract fun getSaldo(accountid: Long): Long

    @Query(
        "select * from Cat where catclassid = 2"
//        "select * " +
//                ",(select total(amount) from cashtrx where  transferid is null and a.id = accountid) " +
//                " - (select total(amount) from cashtrx where catid = a.id) as saldo " +
//                " from account a " +
//                "order by kontotyp, name"
    )
    abstract fun getAccountlist(): Flow<List<Cat>>

    @Query(
        "Select * from Partnerstamm a where name like '%'||:filter ||'%' " +
                "and exists (select id from wpstamm where partnerid = a.id ) " +
                "order by name"
    )
    abstract fun getWPStammlist(filter: String): Flow<List<Partnerstamm>>

    @Query(
        "select a.*, " +
                "p.name as partnername, acc.name as accountname, c.name as catname, " +
                "c.catclassid, 0 as imported, 0 as saldo " +
                "from cashtrx a " +
                "left join Partnerstamm p on p.id = partnerid " +
                "left join Cat acc on   acc.id = accountid " +
                "left join Cat c on c.id = catid " +
                "where accountid = :accountid " +
                "and (transferid is null " + // keine Splittbuchungen
                "or c.catclassid = 2) " + // alle Gegenbuchungen
                "order by btag desc, a.id "
    )
    abstract fun getCashTrxList(accountid: Long): Flow<List<CashTrxView>>

    /**
     * Liest eine CashTrx.
     * Selection wie folgt:
     * Trx.id = id, Trx.transferid = id, ohne Gegenbuchungen.
     * Bevor die Trx-Liste (neu) gespeichert wird, wird die alte Liste aus der DB entfernt und
     * komplett (mit Aufbau der Gegenbuchungen) neu eingefügt.
     */
    @Query(
        "select a.*, " +
                "p.name as partnername, acc.name as accountname, c.name as catname, " +
                "c.catclassid, 0 as imported, 0 as saldo " +
                "from CashTrx a " +
                "left join Partnerstamm p on p.id = partnerid " +
                "left join Cat acc on   acc.id = accountid " +
                "left join Cat c on c.id = catid " +
                "where a.id = :id or a.transferid = :id " +
                "and c.catclassid != 2 " +
                "order by a.id"
    )
    abstract fun getCashTrx(id: Long): Flow<List<CashTrxView>>

    @Query("select * from Partnerstamm where id = :partnerid ")
    abstract fun getPartnerstamm(partnerid: Long): Flow<Partnerstamm>

    @Delete
    abstract suspend fun delete(trx: CashTrx)

    @Insert
    protected abstract suspend fun insert(trx: CashTrx): Long

    /**
     * Einfügen/Update einer CashTrx.
     * Zuerst entfernen der gesamten ursprünglichen Buchung.
     * Danach Einfügen der einzelnen Sätze, die Transferid ab dem zweiten Satz entspricht
     * der id des ersten Satzes.
     * Gegenbuchungen werden jewils mit aufgebaut und sind schon beim Lesen aus der DB nicht mitgekommen.
     */

    @Transaction
    open suspend fun insertCashTrxView(list: List<CashTrxView>) {
        if (list.isNotEmpty()) {
            val main = list[0]
            delete(main.getCashTrx()) // Alle weg w/referientieller Integritaet
            list.forEachIndexed { index, item ->
                if (index != 0) {
                    item.transferid = list[0].id
                }
                item.id = insert(item.getCashTrx())
                if (item.catclassid == 2L) { // Die catclassid ist die catclass der Cat.
                    insert(item.getGegenbuchung())
                }
            }
        }

    }

}