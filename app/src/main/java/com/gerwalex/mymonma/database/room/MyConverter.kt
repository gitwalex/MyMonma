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
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.pow

object MyConverter {
    const val NACHKOMMA = 1000000L
    private val df = DateFormat.getDateInstance()
    private val currency: NumberFormat = NumberFormat.getCurrencyInstance()
    private val digits = BigDecimal(10.0.pow(currency.maximumFractionDigits.toDouble()))

    /**
     *
     * @return yyyy-MM-dd HH:mm:ss formate date as string
     */
    val currentTimeStamp: String?
        get() {
            return try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH_mm_ss", Locale.getDefault())
                dateFormat.format(Date(System.currentTimeMillis()))
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

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
     * Konvertiert Datum im Format 'yyyy-MM-dd' in ein Date
     */
    @JvmStatic
    @TypeConverter
    fun toDate(date: String?): Date? {
        return date?.let { Date.valueOf(date) }
    }

    /**
     * Konvertiert Date in einen String im Format 'yyyy-MM-dd'
     */
    @JvmStatic
    @TypeConverter
    fun toString(date: Date?): String? {
        return date?.toString()
    }

}