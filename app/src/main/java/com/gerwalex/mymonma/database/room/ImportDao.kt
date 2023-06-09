package com.gerwalex.mymonma.database.room

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.gerwalex.monmang.database.tables.ImportNewTrx
import com.gerwalex.monmang.database.tables.ImportTrx
import com.gerwalex.mymonma.database.room.DB.Companion.dao
import com.gerwalex.mymonma.database.tables.ImportAccount
import com.gerwalex.mymonma.database.views.CashTrxView
import com.gerwalex.mymonma.importer.TrxImporter
import java.sql.Date

@Dao
abstract class ImportDao(val db: DB) {
    /**
     * Entfernt alle Vormerkungsumsätze zu einem account
     */
    @Query("delete from importtrx where accountid = :accountid and vormerkung")
    abstract fun removeVormerkungen(accountid: Long)


    @Query("delete from ImportNewTrx")
    abstract suspend fun clearTableImportNewTrx()

    @Insert
    abstract suspend fun insert(trx: ImportNewTrx): Long

    @Insert
    abstract suspend fun insert(trx: ImportTrx): Long

    @Transaction
    open suspend fun insertNewImportTrx(newTrxs: List<ImportNewTrx>) {
        clearTableImportNewTrx()
        for (trx in newTrxs) {
            trx.id = insert(trx)
        }
    }

    @Transaction
    open suspend fun insert(trxs: List<ImportTrx>) {
        for (trx in trxs) {
            trx.id = insert(trx)
        }
    }

    /**
     * Ermittelt Trx in ImportNewTrx, die nicht oder nicht alle in ImportTrx vorhanden sind. In der
     * ersten Spalte (== cnt) ist die Anzahl der Trx, die noch nicht in ImportTRx vorhanden sind.
     */
    @Query(
        """
                select count(*) -(select count(*) from ImportTrx b 
                where a.accountid = b.accountid and a.btag = b.btag 
                and a.amount = b. amount and a.partnername = b.partnername)   
                as cnt , *   
                from ImportNewTrx a   
                group by accountid, btag, amount, partnername having cnt > 0

    """
    )
    abstract suspend fun getUnExistentImportTrx(): List<ImportNewTrx>

    /**
     * Liste aller ImportTransaktionen, die (noch) nicht einem echten CashUmsatz zugeordnet sind
     */
    @Query(
        "SELECT * FROM IMPORTTRX  where  umsatzid is null and amount != 0 " +  //
                "order by accountid, btag, id desc"
    )
    abstract suspend fun getOpenImportTrx(): List<ImportTrx>

    @Update
    abstract suspend fun _update(trx: ImportTrx)

    @Query("Select * from ImportAccount where id = :accountid")
    abstract suspend fun getImportAccount(accountid: Long): ImportAccount

    /**
     * Update aller in der Liste enthaltenen ImportTrx wenn die entsprechende cashTrans belegt idt.
     * Dann wird die umsatzid der ImportTrx mit der id der cashTrans versorgt und cashTranss erhält
     * den Status onlinUnsatz
     */
    @Transaction
    open suspend fun update(importTrx: ImportTrx) {
        try {
            importTrx.cashTrx?.let { cashTrx ->
                if (cashTrx.id == null)
                    cashTrx.id = dao.insert(cashTrx) else dao.update(cashTrx)

                importTrx.umsatzid = cashTrx.id
                _update(importTrx)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("ImportDao", "importTrx=$importTrx, cashtrans=${importTrx.cashTrx} ")
        }
    }

    /**
     * Liefert Transaktion zu einem Konto mit bestimmtem Betrag, welche zwischen von und bis liegt
     */
    @Query(
        """
            select a.id, a.btag, a.accountid, partnerid, a.amount, a.memo, transferid,  catid,
            accountname, a.partnername,catname,catclassid,isUmbuchung,
            b.id as importTrxId
            from CashTrxView a   
            left outer join ImportTrx b on (b.umsatzid = a.id)   
            where a.accountid = :accountid   
            and a.btag between :von and :bis   
            and a.amount = :amount 
        """
    )
    abstract suspend fun getCashTrx(
        accountid: Long,
        von: Date,
        bis: Date,
        amount: Long
    ): List<CashTrxView>

    /**
     * Ermittelt eine partnerid aus Cashtrans, die zu einem bereits importierten Umsatz mit dem
     * gleichen Partnernamen passt.
     */
    @Query(
        """
                select partnerid, catid, b.accountid, count(catid) as cnt   
                from ImportTrx a   
                left outer join Cashtrx b on a.umsatzid = b.id 
                where upper(partnername) = upper(:importpartnername)    
                and partnerid != 0   
                and catid != 1000   
                group by catid   
                order by cnt desc

    """
    )
    abstract suspend fun getPartnerWithCatid(importpartnername: String): TrxImporter.PartnerCatid?

    @Query(
        """
        select id from partnerstamm where name = :partnername
    """
    )
    abstract suspend fun getPartnerid(partnername: String): Long?

}