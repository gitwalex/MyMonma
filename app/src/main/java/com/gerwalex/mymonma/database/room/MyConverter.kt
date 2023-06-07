package com.gerwalex.mymonma.database.room

import androidx.room.TypeConverter
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

}