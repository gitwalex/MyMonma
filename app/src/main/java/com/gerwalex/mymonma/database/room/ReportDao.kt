package com.gerwalex.mymonma.database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gerwalex.mymonma.database.data.ExcludedCatClasses
import com.gerwalex.mymonma.database.data.ExcludedCats
import com.gerwalex.mymonma.database.data.GeldflussData
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import kotlinx.coroutines.flow.Flow
import java.sql.Date

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
    abstract fun insertExcludedCat(reportID: Long, catID: Long)

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

}