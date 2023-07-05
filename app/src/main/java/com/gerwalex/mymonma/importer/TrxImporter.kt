package com.gerwalex.mymonma.importer

import android.content.Context
import android.text.format.DateUtils
import com.gerwalex.monmang.database.tables.ImportNewTrx
import com.gerwalex.monmang.database.tables.ImportTrx
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.room.DB.importdao
import com.gerwalex.mymonma.database.tables.CashTrx
import com.gerwalex.mymonma.database.tables.ImportAccount
import com.gerwalex.mymonma.database.tables.Partnerstamm.Companion.Undefined
import com.gerwalex.mymonma.ext.FileExt.copy
import com.gerwalex.mymonma.ext.instantiate
import com.gerwalex.mymonma.main.App
import kotlinx.coroutines.flow.first
import java.io.File
import java.io.IOException
import java.sql.Date
import java.util.concurrent.TimeUnit

open class TrxImporter(private val context: Context) {

    private suspend fun checkForCashTransaktion(
        verrechnungskonto: Long,
        von: Date,
        bis: Date,
        newTrx: ImportTrx
    ) {
        importdao.getCashTrx(verrechnungskonto, von, bis, newTrx.amount).onEach { cashTrxView ->
            if (cashTrxView.importTrxId == null) {
                newTrx.cashTrx = cashTrxView.cashTrx
                cashTrxView.btag = newTrx.btag
            }
        }
        if (newTrx.cashTrx == null) {
            // Bisher kein Treffer - neue Trx
            // erzeugen und ggfs. partnerid und catid aus
            // einer Cashtrans eines  bereits importierten
            // Umsatz mit gleichen Partnernamen übernehmen.
            val newCashTrx = fromImportTrx(newTrx, verrechnungskonto)
            newTrx.partnername?.let {
                importdao.getPartnerWithCatid(it)?.also { partner ->
                    val cid = partner.catid
                    if (cid == verrechnungskonto) {
                        // Bei einer Umbuchung auf gleichem Konto accountid aus c
                        // uebernehmen
                        newCashTrx.catid = partner.accountid
                    } else {
                        // ... sonst catid
                        newCashTrx.catid = cid
                    }
                    newCashTrx.partnerid = partner.partnerid
                }
                if (newCashTrx.partnerid == Undefined) {
                    // noch kein Partner gefunden...
                    // mal gucken, ob nicht doch einen gibt, ansonsten Undefined
                    newCashTrx.partnerid = importdao.getPartnerid(it) ?: Undefined
                }
                newTrx.cashTrx = newCashTrx
            }
        }
    }

    private fun fromImportTrx(importTrx: ImportTrx, verrechnungskonto: Long): CashTrx {
        return CashTrx(accountid = verrechnungskonto).apply {
            btag = importTrx.btag
            amount = importTrx.amount
            memo = importTrx.memo
            partnername = importTrx.partnername
        }

    }


    private suspend fun checkNewCashTrans(newTrxs: List<ImportNewTrx>) {
        importdao.insertNewImportTrx(newTrxs)
        importdao.getUnExistentImportTrx().also { resultlist ->
            val importTrxlist = ArrayList<ImportTrx>()
            resultlist.forEach { trx ->
                trx.cnt.let {
                    for (i in 0..it) {
                        importTrxlist.add(trx.getImportTrx())
                    }
                }
            }
            importdao.insert(importTrxlist)
        }
    }

    /**
     * Prueft auf offene Transaktionen innerhalb ImportTrx. Offen sind Trx, wenn sie noch nicht
     * einem CashUmsatz zugewiesen wurden.
     */
    private suspend fun checkOpenImportedTrx() {
        importdao.getOpenImportTrx().also { list ->
            var accountidold = -1L
            var verrechnungskonto: Long? = null
            val accounts = HashSet<Long>()
            list.forEach { newTrx ->
                if (newTrx.accountid != accountidold) {
                    val account = importdao.getImportAccount(newTrx.accountid)
                    accountidold = newTrx.accountid
                    verrechnungskonto = account.verrechnungskonto.also {
                        accounts.add(it)
                    }
                }
                verrechnungskonto?.let { vKto ->
                    if (newTrx.amount != 0L) {
                        val von = Date(newTrx.btag.time - TimeUnit.DAYS.toMillis(3))
                        val bis = Date(newTrx.btag.time + DateUtils.DAY_IN_MILLIS * 3)
                        val trxList = importdao.getCashTrx(vKto, von, bis, newTrx.amount)
                        if (trxList.isNotEmpty() && trxList[0].importTrxId == null) {
                            newTrx.cashTrx = trxList[0].cashTrx
                            trxList[0].btag = newTrx.btag
                        } else {
                            val newCashTrx = fromImportTrx(newTrx, vKto)
                            newTrx.partnername?.let {
                                importdao.getPartnerWithCatid(it)?.also { partner ->
                                    val cid = partner.catid
                                    if (cid == verrechnungskonto) {
                                        // Bei einer Umbuchung auf gleichem Konto accountid aus c
                                        // uebernehmen
                                        newCashTrx.catid = partner.accountid
                                    } else {
                                        // ... sonst catid
                                        newCashTrx.catid = cid
                                    }
                                    newCashTrx.partnerid = partner.partnerid
                                }
                                if (newCashTrx.partnerid == Undefined) {
                                    // noch kein Partner gefunden...
                                    // mal gucken, ob nicht doch einen gibt, ansonsten Undefined
                                    newCashTrx.partnerid =
                                        importdao.getPartnerid(it) ?: Undefined
                                }
                            }
                            newTrx.cashTrx = newCashTrx

                        }
                        importdao.update(newTrx)
                    }
                }
            }


        }
    }


    suspend fun executeImport() {
        var numFiles = 0
        dao.getImportAccounts().first().onEach { acc ->
            val files = App
                .getAppDownloadDir(context)
                .listFiles { dir, name -> name.startsWith((acc.fileprefix)) }
            if (files != null && files.isNotEmpty()) {
                //ok, was gefunden - erstmal alle Vormerkungen entfernen
                importdao.removeVormerkungen(acc.id!!)
                numFiles += readFiles(acc, files)
            }
            if (numFiles > 0) {
                checkOpenImportedTrx()
            }
        }
    }


    @Throws(IOException::class)
    private suspend fun readFiles(
        acc: ImportAccount,
        files: Array<File>
    ): Int {
        val clazzname = "com.gerwalex.mymonma.importer." + acc.classname
        val importClass = clazzname.instantiate(ImportClass::class.java)
        for (file in files) {
            val dest = File(App.getAppImportDir(context), file.name)
            file.copy(dest)
            val list = importClass.readCashTrx(acc, file)
            checkNewCashTrans(list)
            file.delete()
        }
        return files.size
    }

    data class PartnerCatid(
        val partnerid: Long,
        val catid: Long,
        val accountid: Long,
        val cnt: Int
    )

}

interface ImportClass {

    /**
     * Wird vom TrxImporter aufgerufen und erwartet eine Liste der eingelesenen Trx. Die Liste
     * muss auafsteigend sortiert sein - die neuesten Umsätze kommen zuletzt.
     */
    @Throws(IOException::class)
    fun readCashTrx(account: ImportAccount, file: File): List<ImportNewTrx>
}

