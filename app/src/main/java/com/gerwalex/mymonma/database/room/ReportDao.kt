package com.gerwalex.mymonma.database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gerwalex.mymonma.database.data.ExcludedCatClasses
import com.gerwalex.mymonma.database.data.ExcludedCats
import com.gerwalex.mymonma.database.data.GeldflussData
import com.gerwalex.mymonma.database.data.GeldflussSummenData
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ReportDao(db: DB) {
    @Insert
    abstract suspend fun insert(report: ReportBasisDaten)

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
    abstract fun getReportBasisDaten(reportid: Long): Flow<ReportBasisDaten>


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
        select a.id, a.name, :reportid as reportid, a.id as catclassid,  
        (select id from ReportExcludedCatClasses b   
        where b.reportid = :reportid and b.catclassid = a.id) as excluded   
        from CatClass a   
        where id > 100   
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
        select a.reportid, a.catid, 
                (select name from Cat b 
                where b.id = a.catid) as name 
                from ReportExcludedCats a 
                where reportid = :reportid 
                order by name
    """
    )
    abstract fun getExcludedCats(reportid: Long): Flow<List<ExcludedCats>>

    @Query(
        """
        select :reportID as reportid, sum(einnahmen) as einnahmen, sum(ausgaben) as ausgaben,
            sum(vergleinnahmen) as verglEinnahmen,  sum(verglausgaben) as verglAusgaben  from (   
            select a.id   
            ,(select sum(amount) from CashTrx b where btag between von and bis   
            and catid = a.id and incomecat) as einnahmen   
            ,(select sum(amount) from CashTrx b where btag between von and bis     
            and catid = a.id and not incomecat) as ausgaben   
            ,(select sum(amount) from CashTrx b where btag between  verglVon and verglBis   
            and catid = a.id and incomecat) as vergleinnahmen   
            ,(select sum(amount) from CashTrx b where btag between verglVon and verglBis   
            and catid = a.id and not incomecat) as verglausgaben   
            from Cat a   
            left outer join ReportBasisDaten r  
            where r.id = :reportID  and
            a.id not in (select catid from ReportExcludedCats d where d.reportid = :reportID)
            and a.catclassid not in (select catclassid from ReportExcludedCatClasses d 
            where d.reportid = :reportID)   
            and catclassid > 100  )
            
        """
    )
    abstract fun getGeldflussSummendaten(reportID: Long): Flow<GeldflussSummenData>
}