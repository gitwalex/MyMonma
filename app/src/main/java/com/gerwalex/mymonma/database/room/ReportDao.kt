package com.gerwalex.mymonma.database.room

import androidx.room.Dao
import androidx.room.Query
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ReportDao(db: DB) {

    @Query("Select * from ReportBasisDaten")
    abstract fun getReportList(): Flow<List<ReportBasisDaten>>
}