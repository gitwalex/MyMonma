package com.gerwalex.mymonma.database.room

import androidx.room.TypeConverter
import com.gerwalex.mymonma.enums.WPTyp
import java.math.BigDecimal

object MyConverter {
    @TypeConverter
    fun convertToBigDecimal(value: String): BigDecimal {
        return BigDecimal(value)
    }

    @TypeConverter
    fun convertFromBigDecimal(value: BigDecimal): String {
        return value.toString()
    }

    @TypeConverter
    fun convertWPTyp(wptyp: WPTyp): String {
        return wptyp.name
    }

    @TypeConverter
    fun convertWPTyp(wptyp: String): WPTyp {
        return WPTyp.valueOf(wptyp)
    }

}