package com.gerwalex.mymonma.database.room

import android.text.format.DateUtils
import androidx.room.TypeConverter
import com.gerwalex.mymonma.enums.WPTyp
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.math.BigDecimal
import java.sql.Date
import java.text.DateFormat
import java.text.NumberFormat
import kotlin.math.pow

object MyConverter {
    const val NACHKOMMA = 1000000L
    private val df = DateFormat.getDateInstance()
    val currency = NumberFormat.getCurrencyInstance()
    val digits = BigDecimal(10.0.pow(currency.maximumFractionDigits.toDouble()))
    private val dateformatter = DateFormat.getDateInstance(DateFormat.DEFAULT)


    val currencyFormatter: ValueFormatter = object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return convertToCurrency(value.toLong())
        }
    }
    val btagFormatter: DefaultValueFormatter = object : DefaultValueFormatter(10) {
        override fun getFormattedValue(value: Float): String {
            return df.format(Date(value.toLong() * DateUtils.DAY_IN_MILLIS))
        }
    }
    val indexFormatter: ValueFormatter = object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return ((value / 100).toLong()).toString()
        }
    }

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

    /**
     * Convertiert ein Date
     */
    fun convertDate(date: Long): String {
        val di = DateFormat.getDateInstance(DateFormat.DEFAULT)
        return di.format(Date(date))
    }


}