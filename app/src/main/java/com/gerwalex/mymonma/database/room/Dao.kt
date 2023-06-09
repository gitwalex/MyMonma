package com.gerwalex.mymonma.database.room

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Query
import com.gerwalex.mymonma.database.tables.CashTrx
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.database.tables.Partnerstamm
import com.gerwalex.mymonma.ui.screens.CashTrxView
import kotlinx.coroutines.flow.Flow

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

    @Query("Select * from Cat where name like '%'||:filter ||'%' and catclassid = 2 order by name")
    abstract fun getAccountlist(filter: String): Cursor

    @Query(
        "select * from Cat where catclassid = 2"
//        "select * " +
//                ",(select total(amount) from cashtrx where  transferid is null and a.id = accountid) " +
//                " - (select total(amount) from cashtrx where catid = a.id) as saldo " +
//                " from account a " +
//                "order by kontotyp, name"
    )
    abstract fun getAccountlist(): Flow<List<Cat>>

    @Query(
        "Select * from Partnerstamm a where name like '%'||:filter ||'%' " +
                "and exists (select id from wpstamm where partnerid = a.id ) " +
                "order by name"
    )
    abstract fun getWPStammlist(filter: String): Cursor

    @Query(
        "select a.id, btag, p.name as partnername, acc.name as accountname, c.name as catname," +
                "amount, memo, transferid, " +
                "0 as imported, 0 as saldo " +
                "from cashtrx a " +
                "left join Partnerstamm p on p.id = partnerid " +
                "left join Cat acc on   acc.id = accountid " +
                "left join Cat c on c.id = catid " +
                "where accountid = :accountid " +
                "order by btag desc, a.id "
    )
    abstract fun getCashTrxList(accountid: Long): Flow<List<CashTrxView>>

    @Query("select * from CashTrx where id = :id")
    abstract fun getCashTrx(id: Long): Flow<CashTrx>

    @Query("select * from Partnerstamm where id = :partnerid ")
    abstract fun getPartnerstamm(partnerid: Long): Flow<Partnerstamm>
}