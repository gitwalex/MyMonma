package com.gerwalex.mymonma

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.views.CashTrxView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
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
            dao.insertCashTrxView(ArrayList<CashTrxView>().apply { add(trx) })
            val inserted = dao.getCashTrx(trx.id!!).first()
            dao.delete(trx.getCashtrx())
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
        val list = TestData.createSplittCashTrx(2)
        runBlocking {
            dao.insertCashTrxView(list)
            list.forEach {
                Log.d("DBTest", "inserted: $it ")
            }
            val inserted = dao.getCashTrx(list[0].id!!).flowOn(Dispatchers.IO).first()
            dao.delete(list[0].getCashtrx())
            inserted.forEach {
                Log.d("DBTest", "inserted: $it ")
            }
            assert(inserted.size == list.size)
        }
    }
}

