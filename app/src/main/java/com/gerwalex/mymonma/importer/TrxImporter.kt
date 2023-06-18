package com.gerwalex.mymonma.importer

import android.content.Context
import android.text.format.DateUtils
import com.gerwalex.monmang.database.tables.ImportNewTrx
import com.gerwalex.monmang.database.tables.ImportTrx
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.room.DB.importdao
import com.gerwalex.mymonma.database.tables.CashTrx
import com.gerwalex.mymonma.database.tables.ImportAccount
import com.gerwalex.mymonma.ext.FileExt.copy
import com.gerwalex.mymonma.ext.instantiate
import com.gerwalex.mymonma.main.App
import java.io.File
import java.io.IOException
import java.sql.Date

open class TrxImporter(private val context: Context) {

    private suspend fun checkForCashTransaktion(
        verrechnungskonto: Long,
        von: Date,
        bis: Date,
        newTrx: ImportTrx
    ) {
        importdao.getCashTrx(verrechnungskonto, von, bis, newTrx.amount).collect { list ->
            list.forEach { cashTrans ->
                if (cashTrans.importTrxID != newTrx.id) {
                    newTrx.cashTrans = cashTrans
                    cashTrans.btag = newTrx.btag
                }
                if (newTrx.cashTrans == null) {
                    // Bisher kein Treffer - neue Trx
                    // erzeugen und ggfs. partnerid und catid aus
                    // einer Cashtrans eines  bereits importierten
                    // Umsatz mit gleichen Partnernamen übernehmen.
                    val cashTrx = CashTrx(newTrx)
                    newTrx.partnername?.let {
                        importdao.getPartnerWithCatid(it)?.also { partner ->
                            val cid = partner.catid
                            if (cid == verrechnungskonto) {
                                // Bei einer Umbuchung auf gleichem Konto accountid aus c
                                // uebernehmen
                                cashTrans.catid = partner.accountid
                            } else {
                                // ... sonst catid
                                cashTrans.catid = cid
                            }
                            cashTrans.partnerid = partner.partnerid
                        }
                    }
                    newTrx.cashTrans = cashTrans
                }
            }
        }
    }

    private suspend fun checkNewCashTrans(newTrxs: List<ImportNewTrx>) {
        importdao.insertNewImportTrx(newTrxs)
        importdao.getUnExistentImportTrx().collect { resultlist ->
            val importTrxlist = ArrayList<ImportTrx>()
            resultlist.forEach { trx ->
                importTrxlist.add(trx.getImportTrx())
            }
            importdao.insert(importTrxlist)
        }
    }

    /**
     * Prueft auf offene Transaktionen innerhalb ImportTrx. Offen sind Trx, wenn sie noch nicht
     * einem CashUmsatz zugewiesen wurden.
     */
    private suspend fun checkOpenImportedTrx() {
        val newTrxList = ArrayList<ImportTrx>()
        importdao.getOpenImportTrx().collect { list ->
            var accountidold = -1L
            var verrechnungskonto: Long? = null
            list.forEach { newTrx ->
                if (newTrx.accountid != accountidold) {
                    val account = importdao.getImportAccount(newTrx.accountid)
                    accountidold = newTrx.accountid
                    verrechnungskonto = account.verrechnungskonto
                }
                verrechnungskonto?.let { vKto ->
                    if (newTrx.amount != 0L) {
                        val von = Date(newTrx.btag.time - DateUtils.DAY_IN_MILLIS * 3)
                        val bis = Date(newTrx.btag.time + DateUtils.DAY_IN_MILLIS * 3)
                        checkForCashTransaktion(vKto, von, bis, newTrx)
                        newTrxList.add(newTrx)
                    }
                }
            }
            importdao.update(newTrxList)


        }
    }

    suspend fun executeImport() {
        var numFiles = 0
        dao.getImportAccounts().collect { list ->
            list.forEach { acc ->
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
    }


    @Throws(IOException::class)
    private suspend fun readFiles(
        acc: ImportAccount,
        files: Array<File>
    ): Int {
        val clazzname = "com.gerwalex.monmang.importer." + acc.classname
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

    interface ImportClass {

        /**
         * Wird vom TrxImporter aufgerufen und erwartet eine Liste der eingelesenen Trx. Die Liste
         * muss auafsteigend sortiert sein - die neuesten Umsätze kommen zuletzt.
         */
        @Throws(IOException::class)
        fun readCashTrx(account: ImportAccount, file: File): List<ImportNewTrx>
    }

    data class PartnerCatid(
        val partnerid: Long,
        val catid: Long,
        val accountid: Long,
        val cnt: Int
    )
}