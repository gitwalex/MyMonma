package com.gerwalex.mymonma.database.room

import androidx.room.Dao
import androidx.room.Query
import com.gerwalex.mymonma.database.data.GeldflussData
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import kotlinx.coroutines.flow.Flow
import java.sql.Date

@Dao
abstract class ReportDao(db: DB) {

    @Query("Select * from ReportBasisDaten")
    abstract fun getReportList(): Flow<List<ReportBasisDaten>>

    @Query(
        """
            select :reportid as reportid, id as catid, name    
            ,(select sum(b.amount) from CashTrx b   
            where b.catid = a.id and b.btag between :from and :to) as amount   
            ,(select count(b.amount) from CashTrx b   
            where b.catid = a.id and b.btag between :from and :to) as repcnt   
            from Cat a   
            where catclassid > 100   
            and a.catclassid not in (select catclassid from ReportExcludedCatClasses d   
            where d.reportid = :reportid)   
            and a.id not in (select catid from ReportExcludedCats d   
            where d.reportid = :reportid)   
            group by name having amount is not null   
            order by name
    """
    )
    abstract fun getReportGeldflussData(
        reportid: Long,
        from: Date,
        to: Date
    ): Flow<List<GeldflussData>>

    @Query("Select * from Reportbasisdaten where id = :reportid")
    abstract suspend fun getReportBasisDaten(reportid: Long): ReportBasisDaten?

}