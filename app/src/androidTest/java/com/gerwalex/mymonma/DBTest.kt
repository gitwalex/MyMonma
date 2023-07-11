package com.gerwalex.mymonma

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.database.room.DB.Companion.dao
import com.gerwalex.mymonma.database.room.DB.Companion.wpdao
import com.gerwalex.mymonma.database.room.MyConverter.NACHKOMMA
import com.gerwalex.mymonma.database.tables.CashTrx
import com.gerwalex.mymonma.database.tables.WPTrx
import com.gerwalex.mymonma.database.views.AccountCashView
import com.gerwalex.mymonma.database.views.CashTrxView
import com.gerwalex.mymonma.database.views.WPStammView
import com.gerwalex.mymonma.ui.wp.insertKauf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.sql.Date

@RunWith(AndroidJUnit4::class)
class DBTest {
    private lateinit var db: DB

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, DB::class.java
        ).build()

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeCashTrxAndReadInList() {
        val trx = TestData.createSimpleCashTrx(2)
        runBlocking {
            dao.insertCashTrx(ArrayList<CashTrx>().apply { add(trx) })
            val inserted = dao.getCashTrx(trx.id!!)
            dao.delete(trx)
            assert(trx.id != null)
            assert(inserted.size == 1)
            with(inserted.first()) {
                assert(trx.partnerid == partnerid)
                assert(trx.amount == amount)
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun writeSplittedCashTrxAndReadInList() {
        val account1 = 2L
        val account2 = 11L
        val testList = TestData.createSplittCashTrx(account1, account2)
        runBlocking {
            val saldoAlt1 = dao.getSaldo(account1)
            val saldoAlt2 = dao.getSaldo(account2)

            val insertedList = CashTrxView.insert(testList)
            insertedList.forEach {
                Log.d("DBTest", "inserted: $it ")
            }
            dao.getCashTrx(insertedList[0].id!!).apply {
                assert(this.size == testList.size)
                val saldoNeu1 = dao.getSaldo(account1)
                val saldoNeu2 = dao.getSaldo(account2)
                Log.d("DBTest", "SaldoAlt: $saldoAlt1 | $saldoAlt2 ")
                Log.d("DBTest", "SaldoNeu: $saldoNeu1 | $saldoNeu2 ")
                dao.delete(testList[0].cashTrx)
                val cat1 = dao.getCashAccount(account1)
                assert(cat1?.saldo == saldoNeu1)
                val cat2 = dao.getCashAccount(account2)
                assert(cat2?.saldo == saldoNeu2)
                assert(saldoAlt1 + 1000_00 == saldoNeu1)
                assert(saldoAlt2 - 300_00 == saldoNeu2)
                assert(get(0).partnerid == insertedList[0].partnerid)
                assert(get(0).partnerid > 0)
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun executeWPKauf() {
        val wp: WPStammView
        val date = Date(System.currentTimeMillis())
        val menge = 100 * NACHKOMMA
        val kurs = 50_00L
        val ausmBetrag = 5005_55L
        val gebuehren = 5_55L
        val wptrx: WPTrx
        val cashAccount: AccountCashView
        runBlocking {
            val list = wpdao.getWPBestandListe(date).first().filter { it.id == 20L }
            wp = list[0]
            val depot = wpdao.getDepot(26)
            cashAccount = dao.getCashAccount(depot.verrechnungskonto!!)!!

            wptrx = insertKauf(
                date = date,
                menge = menge,
                kurs = kurs,
                ausmBetrag = ausmBetrag,
                gebuehren = gebuehren,
                wp = wp,
                depot = depot
            )
        }
        runBlocking {
            wpdao.getWPBestandListe(date).first().filter { it.id == 20L }.apply {
                val neuWP = this[0]
                Log.d("DBTest", "WPNeu: $neuWP ")
                Log.d("DBTest", "WPAlt: $wp ")
                assert(wp.bestand + menge == neuWP.bestand)
                assert(wp.anzahlkauf + 1 == neuWP.anzahlkauf)
                assert(neuWP.lastums.toString() == date.toString())

            }
            dao.getCashAccount(cashAccount.id).also {
                assert(cashAccount.saldo - ausmBetrag == it?.saldo)
            }
            dao.deleteCashTrx(wptrx.id!!)

        }
    }
}