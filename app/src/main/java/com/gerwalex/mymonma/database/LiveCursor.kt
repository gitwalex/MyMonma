package com.gerwalex.mymonma.database

import android.database.Cursor
import androidx.room.RoomDatabase

open class LiveCursor(
    db: RoomDatabase,
    table: String,
    invalidationTracker: InvalidationListener<Cursor>,
) :
    LiveTable<Cursor>
        (db, table, invalidationTracker)