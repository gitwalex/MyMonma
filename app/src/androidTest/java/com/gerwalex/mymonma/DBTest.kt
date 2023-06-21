package com.gerwalex.mymonma

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.tables.CashTrx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

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
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertCashTrx(ArrayList<CashTrx>().apply { add(trx) })
            val inserted = dao.getCashTrx(trx.id!!).first()
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
        val list = TestData.createSplittCashTrx(account1, account2)
        runBlocking {
            val saldoAlt1 = dao.getSaldo(account1)
            val saldoAlt2 = dao.getSaldo(account2)
            dao.insertCashTrx(list)
            list.forEach {
                Log.d("DBTest", "inserted: $it ")
            }
            val newList = dao.getCashTrx(list[0].id!!).first()
            val saldoNeu1 = dao.getSaldo(account1)
            val saldoNeu2 = dao.getSaldo(account2)
            Log.d("DBTest", "SaldoAlt: $saldoAlt1 | $saldoAlt2 ")
            Log.d("DBTest", "SaldoNeu: $saldoNeu1 | $saldoNeu2 ")
            dao.delete(list[0])
            newList.forEach {
                Log.d("DBTest", "inserted: $it ")
            }
            val cat1 = dao.getCat(account1)
            assert(cat1?.saldo == saldoNeu1)
            val cat2 = dao.getCat(account2)
            assert(cat2?.saldo == saldoNeu2)
            assert(saldoAlt1 + 1000_00 == saldoNeu1)
            assert(saldoAlt2 - 300_00 == saldoNeu2)
            assert(newList[0].partnerid == list[0].partnerid)
            assert(newList[0].partnerid > 0)
            assert(newList.size == list.size)
        }
    }
}
