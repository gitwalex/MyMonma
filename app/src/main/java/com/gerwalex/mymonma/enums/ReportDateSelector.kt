package com.gerwalex.mymonma.enums

import android.content.Context
import androidx.annotation.StringRes
import com.gerwalex.mymonma.R
import java.sql.Date
import java.util.Calendar
import java.util.GregorianCalendar

/**
 * Auswahl des Datums fuer Reports
 */
enum class ReportDateSelector {
    /**
     *
     */
    AktJhr {
        override val textResID = R.string.AktJhr
        override val dateSelection: VonBisDate
            get() {
                val bis = GregorianCalendar.getInstance()
                bis[Calendar.MONTH] = Calendar.DECEMBER
                bis[Calendar.DAY_OF_MONTH] = 31
                val vonDate = actualDate
                vonDate[Calendar.MONTH] = Calendar.JANUARY
                vonDate[Calendar.DAY_OF_MONTH] = 1
                return VonBisDate(vonDate, bis)
            }


    },  //
    AktMnt {
        override val textResID = R.string.AktMnt


        override val dateSelection: VonBisDate
            get() {
                val bisDate = getUltimoDate(actualDate)
                val vonDate = actualDate
                vonDate[Calendar.DAY_OF_MONTH] = 1
                return VonBisDate(vonDate, bisDate)
            }
    },  //
    AktQuart {
        override val textResID = R.string.AktQuart


        override val dateSelection: VonBisDate
            get() {
                val bisDate = quartalBeginn
                bisDate.add(Calendar.MONTH, 3)
                bisDate.add(Calendar.DAY_OF_MONTH, -1)
                val von = quartalBeginn
                return VonBisDate(von, bisDate)
            }
    },  //
    BisHeuteJhr {
        override val textResID = R.string.BisHeuteJhr
        override val dateSelection: VonBisDate
            get() {
                val bisDate = actualDate
                val vonDate = actualDate
                vonDate[Calendar.MONTH] = Calendar.JANUARY
                vonDate[Calendar.DAY_OF_MONTH] = 1
                return VonBisDate(vonDate, bisDate)
            }
    },  //
    LetzMnt {
        override val textResID = R.string.LetzMnt


        override val dateSelection: VonBisDate
            get() {
                val bisDate = getUltimoDate(actualDate)
                val vonDate = actualDate
                bisDate.add(Calendar.MONTH, -1)
                vonDate.add(Calendar.MONTH, -1)
                vonDate[Calendar.DAY_OF_MONTH] = 1
                return VonBisDate(vonDate, bisDate)
            }
    },  //
    LetztQuart {
        override val textResID = R.string.LetztQuart


        override val dateSelection: VonBisDate
            get() {
                val bisDate = quartalBeginn
                bisDate.add(Calendar.DAY_OF_MONTH, -1)
                val von = getQuartalBeginn(bisDate)
                return VonBisDate(von, bisDate)
            }
    },  //
    LztJhr {
        override val textResID = R.string.LztJhr


        override val dateSelection: VonBisDate
            get() {
                val bisDate = actualDate
                bisDate.add(Calendar.YEAR, -1)
                bisDate[Calendar.MONTH] = Calendar.DECEMBER
                bisDate[Calendar.DAY_OF_MONTH] = 31
                val vonDate = actualDate
                vonDate[Calendar.YEAR] = bisDate[Calendar.YEAR]
                vonDate[Calendar.MONTH] = Calendar.JANUARY
                vonDate[Calendar.DAY_OF_MONTH] = 1
                return VonBisDate(vonDate, bisDate)
            }
    },  //
    Ltz12Mnt {
        override val textResID = R.string.Ltz12Mnt


        override val dateSelection: VonBisDate
            get() {
                val bisDate = actualDate
                val vonDate = actualDate
                vonDate.add(Calendar.YEAR, -1)
                vonDate.add(Calendar.DAY_OF_MONTH, 1)
                return VonBisDate(vonDate, bisDate)
            }
    },  //
    Alles {
        override val textResID = R.string.allData


        override val dateSelection: VonBisDate
            get() {
                val von: Date = Date.valueOf("1970-01-01")
                val bis: Date = Date.valueOf("2399-12-31")
                return VonBisDate(von.time, bis.time)
            }
    },  //
    EigDatum {
        override val textResID = R.string.EigDatum


        /**
         * @throws UnsupportedOperationException,
         * da hier kein automatisierter Auswertungszeitraum ermittelbar ist
         */
        override val dateSelection: VonBisDate
            get() {
                // Keine Standarauswahl moeglich
                throw UnsupportedOperationException("Keine Standarauswahl moeglich")
            }
    } //
    ;

    /**
     * @return Aktuelles Datum mit Stunde, Minute, Secunde, Milisekunde = 0
     */
    val actualDate: Calendar
        get() = GregorianCalendar.getInstance()

    /**
     * Muss von jeder Enum implementiert werden.
     *
     * @return Liefert eine Klasse [VonBisDate] zuruck.
     */
    abstract val dateSelection: VonBisDate

    /**
     * @param cal Basisdatum
     * @return Liefert den Quartalsbeginn zum Basisdatum cal
     */
    fun getQuartalBeginn(cal: Calendar): Calendar {
        val quartalBeginn = cal.clone() as Calendar
        val quartal = cal[Calendar.MONTH] / 3 * 3
        quartalBeginn[Calendar.MONTH] = quartal
        quartalBeginn[Calendar.DAY_OF_MONTH] = 1
        return quartalBeginn
    }

    /**
     * @return Liefert den Quartalsbeginn zum aktuellen Datum
     */
    val quartalBeginn: Calendar
        get() {
            val quartalBeginn = Calendar.getInstance()
            val quartal = quartalBeginn[Calendar.MONTH] / 3 * 3
            quartalBeginn[Calendar.MONTH] = quartal
            quartalBeginn[Calendar.DAY_OF_MONTH] = 1
            return quartalBeginn
        }

    fun getText(context: Context): String {
        return context.getString(textResID)
    }

    /**
     * ResID des Klartextes fuer die jeweilige Enum
     *
     * @return
     */
    @StringRes
    open val textResID = R.string.EigDatum

    /**
     * Klasse zum festlegen des Auswertungszeitraumes
     */
    class VonBisDate {
        /**
         * @return Liefert das EndeDatum
         */
        val endDate: Date

        /**
         * @return Liefert das Startdatum
         */
        val startDate: Date

        internal constructor(vonDate: Calendar, bisDate: Calendar) {
            vonDate[Calendar.HOUR] = 0
            vonDate[Calendar.MINUTE] = 0
            vonDate[Calendar.SECOND] = 0
            vonDate[Calendar.MILLISECOND] = 0
            startDate = Date(vonDate.timeInMillis)
            bisDate[Calendar.HOUR] = 0
            bisDate[Calendar.MINUTE] = 0
            bisDate[Calendar.SECOND] = 0
            bisDate[Calendar.MILLISECOND] = 0
            endDate = Date(bisDate.timeInMillis)
        }

        internal constructor(vonDateInMillis: Long, bisDateInMillis: Long) {
            startDate = Date(vonDateInMillis)
            endDate = Date(bisDateInMillis)
        }
    }

    companion object {
        /**
         * Setzt ein Datum auf Ultimo des Monats in cal
         *
         * @param cal Datum
         * @return Ultimo des Monats in Date
         */
        private fun getUltimoDate(cal: Calendar): Calendar {
            cal.add(Calendar.MONTH, 1)
            cal[Calendar.DAY_OF_MONTH] = 1
            cal.add(Calendar.DAY_OF_MONTH, -1)
            return cal
        }
    }
}