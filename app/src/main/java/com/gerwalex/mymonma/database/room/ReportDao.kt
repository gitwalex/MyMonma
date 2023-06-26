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
}