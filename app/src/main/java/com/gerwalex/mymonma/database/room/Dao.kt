package com.gerwalex.mymonma.database.room

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class Dao(val db: DB) {
    @Query("Select * from Partnerstamm")
    abstract fun getPartnerstammdaten(): Cursor

    @Query("Select * from Partnerstamm where name like '%'||:filter ||'%'")
    abstract fun getPartnerlist(filter: String): Cursor

}