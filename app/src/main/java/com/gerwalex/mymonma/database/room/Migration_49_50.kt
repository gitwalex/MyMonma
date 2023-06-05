package com.gerwalex.monmang.database.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration_49_50(val startVersion: Int, val endVersion: Int)
/**
 * Creates a new migration between `startVersion` and `endVersion`.
 *
 * @param startVersion The start version of the database.
 * @param endVersion   The end version of the database after this migration is applied.
 */
    : Migration(startVersion, endVersion) {
    override fun migrate(database: SupportSQLiteDatabase) {
        TODO("Not yet implemented")
    }

}
