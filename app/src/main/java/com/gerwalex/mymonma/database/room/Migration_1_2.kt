package com.gerwalex.mymonma.database.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Creates a new migration between `startVersion` and `endVersion`.
 *
 * @param startVersion The start version of the database.
 * @param endVersion   The end version of the database after this migration is applied.
 */

class Migration_1_2(val startVersion: Int, val endVersion: Int) :
    Migration(startVersion, endVersion) {
    override fun migrate(_db: SupportSQLiteDatabase) {
        try {
            _db.beginTransaction()

            _db.execSQL(
                """
            CREATE TABLE IF NOT EXISTS `WPTrxTemp` 
            (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `cashtrxid` INTEGER, `btag` TEXT NOT NULL, 
            `accountid` INTEGER NOT NULL, `wpid` INTEGER NOT NULL, `catid` INTEGER NOT NULL,
             `paketid` INTEGER, `kurs` INTEGER NOT NULL, `menge` INTEGER, `ertrag` INTEGER NOT NULL,
              `einstand` INTEGER, `zinszahl` INTEGER, `haltedauer` INTEGER, 
              FOREIGN KEY(`cashtrxid`) REFERENCES `CashTrx`(`id`) ON UPDATE CASCADE ON DELETE CASCADE , 
              FOREIGN KEY(`paketid`) REFERENCES `WPTrx`(`id`) ON UPDATE CASCADE ON DELETE CASCADE , 
              FOREIGN KEY(`accountid`) REFERENCES `Account`(`id`) ON UPDATE CASCADE ON DELETE RESTRICT ,
               FOREIGN KEY(`catid`) REFERENCES `Cat`(`id`) ON UPDATE CASCADE ON DELETE RESTRICT ,
                FOREIGN KEY(`wpid`) REFERENCES `WPStamm`(`id`) ON UPDATE CASCADE ON DELETE RESTRICT )
        """.trimIndent()
            )
            _db.execSQL(
                """
            Insert INTO WPTrxTemp (id, cashtrxid, btag, accountid,wpid, catid,paketid,kurs, menge, ertrag, einstand, zinszahl, haltedauer)
            select id, id, btag, accountid,wpid, catid,paketid,kurs, menge, ertrag, einstand, zinszahl, haltedauer from wptrx
        """.trimIndent()
            )
            _db.execSQL(
                """
            Drop Table WPTrx
        """.trimIndent()
            )
            _db.execSQL(
                """
            ALTER TABLE WPTrxTemp RENAME TO WPTrx
        """.trimIndent()
            )

            _db.execSQL("CREATE INDEX IF NOT EXISTS `index_WPTrx_cashtrxid` ON `WPTrx` (`cashtrxid`)")
            _db.execSQL("CREATE INDEX IF NOT EXISTS `index_WPTrx_accountid` ON `WPTrx` (`accountid`)")
            _db.execSQL("CREATE INDEX IF NOT EXISTS `index_WPTrx_wpid` ON `WPTrx` (`wpid`)")
            _db.execSQL("CREATE INDEX IF NOT EXISTS `index_WPTrx_catid` ON `WPTrx` (`catid`)")
            _db.execSQL("CREATE INDEX IF NOT EXISTS `index_WPTrx_paketid` ON `WPTrx` (`paketid`)")

            _db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            _db.endTransaction()
        }
    }
}
