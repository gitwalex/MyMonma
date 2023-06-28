package com.gerwalex.mymonma.database.room

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.gerwalex.mymonma.database.data.GesamtVermoegen
import com.gerwalex.mymonma.database.tables.CashTrx
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.database.tables.Cat.Companion.KONTOCLASS
import com.gerwalex.mymonma.database.tables.CatClass
import com.gerwalex.mymonma.database.tables.ImportAccount
import com.gerwalex.mymonma.database.tables.Partnerstamm
import com.gerwalex.mymonma.database.tables.Partnerstamm.Companion.Undefined
import com.gerwalex.mymonma.database.tables.WPKurs
import com.gerwalex.mymonma.database.tables.WPStamm
import com.gerwalex.mymonma.database.views.AccountCashView
import com.gerwalex.mymonma.database.views.CashTrxView
import com.gerwalex.mymonma.database.views.CatView
import com.gerwalex.mymonma.database.views.TrxRegelmView
import com.gerwalex.mymonma.enums.Intervall
import kotlinx.coroutines.flow.Flow
import java.sql.Date

@Dao
abstract class Dao(val db: DB) {

    @Query(
        """
    select total(saldo) as saldo
    ,(SELECT total(marktwert) from AccountDepotView) as marktwert
    from AccountCashView a
    """
    )
    abstract fun getGesamtVermoegen(): Flow<GesamtVermoegen>

    @Query(
        """
        Select * from TrxRegelmView 
        where transferid is null 
        order by btag
        """
    )
    abstract fun getRegelmTrxList(): Flow<List<TrxRegelmView>>

    @Query("Select * from Partnerstamm")
    abstract fun getPartnerstammdaten(): Flow<List<Partnerstamm>>

    @Query("Select * from Partnerstamm where name like '%'||:filter ||'%' order by name")
    abstract fun getPartnerlist(filter: String): Flow<List<Partnerstamm>>

    @Query(
        """
        Select a.*, b.name as obercatname, 0 as saldo
         ,(select count(*) from CashTrx where catid = a.id) as cnt
        from Cat a
        join Cat b using(id)
        where a.name like '%'|| :filter||'%' 
        and not a.ausgeblendet 
        and (a.id > 10001 or a.supercatid = ${Cat.CASHKONTOCATID}) 
        order by cnt desc, a.name
        """
    )
    abstract fun getCatlist(filter: String): Flow<List<CatView>>

    @Query("Select * from CatClass where name like '%'||:filter ||'%' order by name")
    abstract fun getCatClasslist(filter: String): Flow<List<CatClass>>

    @Query("Select * from Cat where name like '%'||:filter ||'%' and catclassid = $KONTOCLASS  order by name")
    abstract fun getAccountlist(filter: String): Flow<List<Cat>>

    /**
     * Ermittlung Saldo zu CashKonto!
     */
    @Query(
        """
        select sum(amount) 
        from CashTrx a 
        where (transferid is null or isUmbuchung )
        and  accountid = :accountid
    """
    )
    abstract suspend fun getSaldo(accountid: Long): Long

    @Query(
        """
        select * from AccountCashView order by obercatid
        """
    )
    abstract fun getAccountlist(): Flow<List<AccountCashView>>

    @Query(
        "Select * from Partnerstamm a where name like '%'||:filter ||'%' " +
                "and exists (select id from wpstamm where partnerid = a.id ) " +
                "order by name"
    )
    abstract fun getWPStammlist(filter: String): Flow<List<Partnerstamm>>

    @Query(
        """ 
        select * from Cashtrxview a
        where a.accountid = :accountid 
        and (transferid is null or isUmbuchung )  
        order by btag desc, id
        """
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
        "select * from CashTrxView a " +
                "where a.id = :id or a.transferid = :id " +
                "order by a.id"
    )
    abstract fun getCashTrxFlow(id: Long): Flow<List<CashTrxView>>

    /**
     * Liest eine CashTrx.
     * Selection wie folgt:
     * Trx.id = id, Trx.transferid = id, ohne Gegenbuchungen.
     * Bevor die Trx-Liste (neu) gespeichert wird, wird die alte Liste aus der DB entfernt und
     * komplett (mit Aufbau der Gegenbuchungen) neu eingefügt.
     */
    @Query(
        "select * from CashTrxView a " +
                "where a.id = :id or a.transferid = :id " +
                "order by a.id"
    )
    protected abstract suspend fun _getCashTrx(id: Long): List<CashTrxView>

    suspend fun getCashTrx(id: Long): List<CashTrxView> {
        val list = _getCashTrx(id)
        val trxList = list.filter { !it.isUmbuchung }.sortedBy { it.id } // Ergebnisliste!!
        val umbuchungen = list.filter { it.isUmbuchung }
        trxList.forEach { trx ->
            // Umbuchung suchen
            trx.gegenbuchung = umbuchungen.firstOrNull {
                it.catid == trx.accountid
                        && it.accountid == trx.catid
                        && it.amount == -trx.amount
                        && it.memo == trx.memo
            }
        }
        return trxList
    }


    @Query("select * from Partnerstamm where id = :partnerid ")
    abstract fun getPartnerstamm(partnerid: Long): Flow<Partnerstamm>

    @Query(
        """
    select total(amount) from CashTrx where accountid = :accountid and btag <= :btag and (transferid is null or isUmbuchung)
"""
    )
    abstract suspend fun getSaldo(accountid: Long, btag: Date): Long


    @Query("select * from partnerstamm where name = :partnername")
    abstract suspend fun getPartnerstamm(partnername: String): Partnerstamm?

    /**
     * Einfügen/Update einer CashTrx.
     * Zuerst entfernen der gesamten ursprünglichen Buchung.
     * Danach Einfügen der einzelnen Sätze, die Transferid ab dem zweiten Satz entspricht
     * der id des ersten Satzes.
     */

    @Transaction
    open suspend fun insertCashTrx(list: List<CashTrx>): List<CashTrx> {
        if (list.isNotEmpty()) {
            val main = list[0]
            delete(main) // Alle weg w/referientieller Integritaet
            list.forEachIndexed { index, item ->
                if (index != 0) {
                    item.partnerid = main.partnerid
                    item.transferid = main.id
                    item.accountid = main.accountid
                    item.btag = main.btag
                }
                item.id = insert(item)
                item.gegenbuchung?.let { umbuchung ->
                    umbuchung.transferid = main.id
                    umbuchung.isUmbuchung = true
                    umbuchung.id = insert(umbuchung)
                }
                Log.d("Dao", "insertCashTrxView: [$index]:$item")
            }
        }
        return list

    }

    @Query("select * from cat where id = :accountid and catclassid = $KONTOCLASS")
    abstract fun getAccountData(accountid: Long): Flow<Cat>

    @Query("Select * from WPStamm where wpkenn = :wpkenn")
    abstract fun getWPStammdaten(wpkenn: String): Flow<WPStamm?>


    @Transaction
    open suspend fun insert(wpstamm: WPStamm): Long {
        val partner = Partnerstamm(name = wpstamm.wpname!!)
        wpstamm.partnerid = insert(partner)
        return _insert(wpstamm)
    }

    /**
     * Liefert alle bis Btag fälligen Daueraufträge
     */
    @Query(
        """
        SELECT * 
        from TrxRegelmView
        where btag  < :btag AND  transferID IS NULL
    """
    )
    abstract suspend fun getNextRegelmTrx(btag: Date): List<TrxRegelmView>

    @Query(
        """
        SELECT * 
        from TrxRegelmView
        where id = :id or transferid = :id 
        order by id
    """
    )
    abstract suspend fun getTrxRegelm(id: Long): MutableList<TrxRegelmView>

    //    @Transaction
    open suspend fun execute(trx: TrxRegelmView) {
        trx.id?.let { id ->
            DB.dao.getTrxRegelm(id).also { item ->
                val cashTrxList = ArrayList<CashTrx>()
                item.forEach { trx ->
                    val cashTrx = trx.cashTrx
                    if (trx.catclassid == KONTOCLASS) {
                        trx.gegenbuchung = cashTrx.copy(
                            id = null,
                            transferid = null,
                            accountid = cashTrx.catid,
                            catid = cashTrx.accountid,
                            amount = -cashTrx.amount
                        )
                    }
                    cashTrxList.add(cashTrx)
                }
                DB.dao.insertCashTrx(cashTrxList)
                when (trx.intervall) {
                    Intervall.Einmalig -> {
                        deleteRegelmTrx(id)
                    }

                    else -> {
                        trx.btag = trx.nextBtag()
                        updateNextBtag(id, trx.btag)
                    }
                }
            }
        }
    }

    @Query(
        "Update TrxRegelm set btag = :btag " +
                "where id = :trxRegelmId or transferid = :trxRegelmId"
    )
    protected abstract suspend fun updateNextBtag(trxRegelmId: Long, btag: Date)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertKurs(kursList: MutableList<WPKurs>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(wpkurs: WPKurs)

    suspend fun insert(cashTrx: CashTrx): Long {
        with(cashTrx) {
            if (partnerid == Undefined) {
                partnername?.let {// neuer Partner!!
                    val partner = getPartnerstamm(it) ?: Partnerstamm(name = it).apply {
                        id = insert(this)
                    }
                    partnerid = partner.id!!
                }
            }
            return _insert(this)
        }
    }

    @Insert
    abstract suspend fun _insert(trx: CashTrx): Long

    @Insert
    abstract suspend fun insert(partner: Partnerstamm): Long

    @Insert
    protected abstract suspend fun _insert(wpstamm: WPStamm): Long

    @Update
    abstract suspend fun update(wpstamm: WPStamm)

    @Update
    abstract suspend fun update(partner: Partnerstamm)

    @Update
    abstract suspend fun update(cashTrx: CashTrx)

    @Delete
    abstract suspend fun delete(trx: CashTrx)

    @Query("Delete from CashTrx where id = :cashTrxId")
    abstract suspend fun deleteCashTrx(cashTrxId: Long)

    @Query("Delete from TrxRegelm where id = :trxRegelmId or transferid = :trxRegelmId ")
    abstract suspend fun deleteRegelmTrx(trxRegelmId: Long)

    @Query("Select * from ImportAccount")
    abstract fun getImportAccounts(): Flow<List<ImportAccount>>

    @Query(
        """
            select * from AccountCashView where id = :accountid
        """
    )
    abstract suspend fun getCashAccount(accountid: Long): AccountCashView?

}