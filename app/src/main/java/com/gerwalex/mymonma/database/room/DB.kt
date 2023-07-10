package com.gerwalex.mymonma.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gerwalex.monmang.database.tables.ImportNewTrx
import com.gerwalex.monmang.database.tables.ImportTrx
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.data.ExcludedCatClasses
import com.gerwalex.mymonma.database.data.ExcludedCats
import com.gerwalex.mymonma.database.data.GeldflussData
import com.gerwalex.mymonma.database.data.GeldflussSummenData
import com.gerwalex.mymonma.database.tables.Account
import com.gerwalex.mymonma.database.tables.CashTrx
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.database.tables.CatClass
import com.gerwalex.mymonma.database.tables.ImportAccount
import com.gerwalex.mymonma.database.tables.Partnerstamm
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import com.gerwalex.mymonma.database.tables.ReportExcludedCatClasses
import com.gerwalex.mymonma.database.tables.ReportExcludedCats
import com.gerwalex.mymonma.database.tables.TrxRegelm
import com.gerwalex.mymonma.database.tables.WPKurs
import com.gerwalex.mymonma.database.tables.WPStamm
import com.gerwalex.mymonma.database.tables.WPTrx
import com.gerwalex.mymonma.database.views.AccountCashView
import com.gerwalex.mymonma.database.views.AccountDepotView
import com.gerwalex.mymonma.database.views.CashTrxView
import com.gerwalex.mymonma.database.views.CatView
import com.gerwalex.mymonma.database.views.TrxRegelmView
import com.gerwalex.mymonma.database.views.WPStammView

@Database(
    entities = [Account::class, CashTrx::class, Cat::class, CatClass::class, ImportAccount::class,
        ImportNewTrx::class, ImportTrx::class, TrxRegelm::class, Partnerstamm::class,
        ReportBasisDaten::class, ReportExcludedCats::class, ReportExcludedCatClasses::class,
        WPStamm::class, WPKurs::class, WPTrx::class],
    version = 2,
    exportSchema = true,
    views = [AccountCashView::class, AccountDepotView::class, CatView::class,
        CashTrxView::class, GeldflussData::class, GeldflussSummenData::class,
        ExcludedCatClasses::class, ExcludedCats::class, TrxRegelmView::class,
        WPStammView::class, GeldflussData::class]
)
@TypeConverters(MyConverter::class)
abstract class DB : RoomDatabase() {
    override fun close() {
        super.close()
        INSTANCE = null
    }

    abstract val dao: Dao
    abstract val importDao: ImportDao
    abstract val reportDao: ReportDao
    abstract val wPDao: WPDao

    companion object {
        lateinit var DBNAME: String
        val dao: Dao
            get() {
                return INSTANCE!!.dao
            }
        val importdao: ImportDao
            get() {
                return INSTANCE!!.importDao
            }

        val reportdao: ReportDao
            get() {
                return INSTANCE!!.reportDao
            }

        val wpdao: WPDao
            get() {
                return INSTANCE!!.wPDao
            }


        /**
         * Instance der RoomDatabase
         */
        @Volatile
        private var INSTANCE: DB? = null
        fun createInstance(context: Context): DB? {
            if (INSTANCE == null) {
                synchronized(DB::class.java) {
                    if (INSTANCE == null) {
                        val callback = DBCreateCallback(context)
                        DBNAME = context.getString(R.string.dbname)
                        INSTANCE =
                            databaseBuilder(context.applicationContext, DB::class.java, DBNAME)
                                .addCallback(callback)
                                .addMigrations(Migration_1_2(1, 2)) //
                                .build()
                    }
                }
            }
            return INSTANCE
        }

        fun get(): RoomDatabase {
            return INSTANCE!!
        }
    }
}