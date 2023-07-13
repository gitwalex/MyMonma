package com.gerwalex.mymonma.database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gerwalex.mymonma.database.data.CatYearMonth
import com.gerwalex.mymonma.database.data.ExcludedCatClasses
import com.gerwalex.mymonma.database.data.ExcludedCats
import com.gerwalex.mymonma.database.data.GeldflussData
import com.gerwalex.mymonma.database.data.GeldflussSummenData
import com.gerwalex.mymonma.database.data.OrdinaryIncome
import com.gerwalex.mymonma.database.data.PartnerdatenReport
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import com.gerwalex.mymonma.database.views.CashTrxView
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ReportDao(db: DB) {
    @Insert
    abstract suspend fun insert(report: ReportBasisDaten): Long

    @Update
    abstract suspend fun update(report: ReportBasisDaten)

    @Query("Select * from ReportBasisDaten")
    abstract fun getReportList(): Flow<List<ReportBasisDaten>>

    @Query(
        """
        select * from GeldflussData where reportid = :reportid
    """
    )
    abstract fun getReportGeldflussData(reportid: Long): Flow<List<GeldflussData>>

    @Query("Select * from Reportbasisdaten where id = :reportid")
    abstract suspend fun getReportBasisDaten(reportid: Long): ReportBasisDaten?

    @Query("Select * from Reportbasisdaten where id = :reportid")
    abstract fun getReportBasisDatenAsFlow(reportid: Long): Flow<ReportBasisDaten>

    @Query(
        """
        REPLACE INTO ReportExcludedCatClasses (reportid, catclassid)  
        values (:reportID,  :catclassID)
    """
    )
    abstract suspend fun insertExcludedCatClass(reportID: Long, catclassID: Long)

    @Query(
        """
        DELETE FROM ReportExcludedCatClasses where reportid = :reportID and  catclassid = :catclassID
    """
    )
    abstract suspend fun deleteExcludedCatClass(reportID: Long, catclassID: Long)

    @Query(
        """
        select * from ExcludedCatClasses
        where reportid = :reportid
        order by name
    """
    )
    abstract fun getExcludedCatClasses(reportid: Long): Flow<List<ExcludedCatClasses>>

    // ReportExcludedCat
    @Query(
        "REPLACE INTO ReportExcludedCats (reportid, catid) " +  //
                "values (:reportID,  :catID)"
    )
    abstract suspend fun insertExcludedCat(reportID: Long, catID: Long)

    @Query(
        "DELETE FROM ReportExcludedCats " +  //
                "where reportid = :reportID and catid = :catID"
    )
    abstract suspend fun deleteExcludedCat(reportID: Long, catID: Long)

    @Query(
        """
        select * from ExcludedCats
                where reportid = :reportid 
                order by name
    """
    )
    abstract fun getExcludedCats(reportid: Long): Flow<List<ExcludedCats>>

    @Query(
        """
        select * from GeldflussSummenData where reportid = :reportID
            
        """
    )
    abstract fun getGeldflussSummendaten(reportID: Long): Flow<GeldflussSummenData>

    @Query(
        """
        select a.*
        from CashTrxView a
        left outer join ReportBasisDaten b
        where catid = :catid
        and btag between von and bis
        and b.id = :reportid
        order by btag DESC
    """
    )
    abstract fun catGeldflussDetails(reportid: Long, catid: Long): Flow<List<CashTrxView>>

    @Query(
        """
        select a.*
        from CashTrxView a
        left outer join ReportBasisDaten b
        where catid = :catid
        and btag between verglVon and verglBis
        and b.id = :reportid
        order by btag DESC
    """
    )
    abstract fun catGeldflussVergleichDetails(
        reportid: Long,
        catid: Long
    ): Flow<List<CashTrxView>>

    @Query(
        """
        select r.id as reportid, a.id as partnerid, a.name   
        ,(select sum(b.amount) from CashTrx b   
        where b.partnerid = a.id and b.btag between von and bis) as amount   
        ,(select count(b.amount) from CashTrx b   
        where b.partnerid = a.id and b.btag between von and bis) as repcnt   
        from Partnerstamm a   
        left outer join ReportBasisDaten r
        where reportid = :reportid and a.name like '%'||:filter ||'%'
        group by reportid, partnerid having repcnt > 0
        order by a.name
    """
    )
    abstract suspend fun getPartnerdaten(reportid: Long, filter: String): List<PartnerdatenReport>

    @Query(
        """
        select a.*
        from CashTrxView a
        left outer join ReportBasisDaten b
        where partnerid = :partnerid
        and btag between von and bis
        and b.id = :reportid
        order by btag DESC
    """
    )
    abstract fun partnerGeldflussDetails(reportid: Long, partnerid: Long): Flow<List<CashTrxView>>

    @Query(
        """
        select b.id, strftime('%Y', btag) as year, strftime('%m', btag) as month, 
        sum(amount) as amount 
        from Cat a 
        left outer join CashTrx b on (a.id = b.catid) 
        where catid > 9000 
        and a.id not in (select catid from ReportExcludedCats 
        where reportid = :reportid) 
        and catclassid not in (select catclassid from ReportExcludedCatClasses 
        where reportid = :reportid) 
        group by year, month 
        order by year, month  
    """
    )
    /**
     * Erstellt einen KategorieSummenreport gruppiert nach Year/Month.
     * Ausgeschlossen werden die cat/catclasses der reportid
     */
    abstract suspend fun getYearMonthReport(reportid: Long): List<CatYearMonth>

    /**
     * Summen der ordentlichen Erträge nach Jahr
     */
    @Query(
        """
                select strftime('%Y', btag) as year, sum(amount) as amount   
                from CashTrx   
                where catid between 2100 and 2199   
                group by year

    """
    )
    abstract fun getOrdinaryIncome(): Flow<List<OrdinaryIncome>>

}