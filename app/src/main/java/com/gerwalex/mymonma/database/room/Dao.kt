package com.gerwalex.mymonma.database.room

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class Dao(val db: DB) {
    @Query("Select * from Partnerstamm")
    abstract fun getPartnerstammdaten(): Cursor

    @Query("Select * from Partnerstamm where name like '%'||:filter ||'%' order by name")
    abstract fun getPartnerlist(filter: String): Cursor

    @Query(
        "Select * from Cat " +//
                "where name like '%'|| :filter||'%' and not ausgeblendet and supercatid != 1002 " +  //
                "order by cnt desc, name"
    )
    abstract fun getCatlist(filter: String): Cursor

    @Query("Select * from CatClass where name like '%'||:filter ||'%' order by name")
    abstract fun getCatClasslist(filter: String): Cursor

    @Query("Select * from Account where name like '%'||:filter ||'%' order by name")
    abstract fun getAccountlist(filter: String): Cursor

    @Query(
        "select * " +
                ",(select total(amount) from cashtrx where  transferid is null and a.id = accountid) " +
                " - (select total(amount) from cashtrx where catid = a.id) as saldo " +
                " from account a " +
                "order by kontotyp, name"
    )
    abstract fun getAccountlist(): Cursor

    @Query("Select * from WPStamm where name like '%'||:filter ||'%' order by name")
    abstract fun getWPStammlist(filter: String): Cursor

}