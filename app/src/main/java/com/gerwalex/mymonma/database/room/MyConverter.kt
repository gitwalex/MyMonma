package com.gerwalex.mymonma.database.room

import androidx.room.TypeConverter
import com.gerwalex.mymonma.enums.WPTyp
import java.math.BigDecimal
import java.sql.Date
import java.text.DateFormat
import java.text.NumberFormat
import kotlin.math.pow

object MyConverter {
    const val NACHKOMMA = 1000000L

    val currency = NumberFormat.getCurrencyInstance()
    val digits = BigDecimal(10.0.pow(currency.maximumFractionDigits.toDouble()))

    fun convertCurrencyToLong(s: String): Long {
        val value = s.replace(".", "")
            .replace(",", ".")
            .toDouble()
        return BigDecimal(value).multiply(digits).toLong()
    }

    fun convertToCurrency(amount: Long): String {
        return currency.format(BigDecimal(amount).divide(digits))
    }

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

    /**
     * Convertiert ein Date
     */
    fun convertDate(date: Date): String {
        val di = DateFormat.getDateInstance(DateFormat.DEFAULT)
        return di.format(date)
    }


}