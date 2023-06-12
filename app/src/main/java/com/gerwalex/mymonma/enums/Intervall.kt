package com.gerwalex.mymonma.enums

import android.content.Context
import com.gerwalex.mymonma.R

enum class Intervall {
    Monatlich {

        override val intervallNameTextResID = R.string.intervall_monat
        override val ausfuehrung = "0|0|1|0"
    },
    Einmalig {

        override val intervallNameTextResID = R.string.intervall_einmalig
        override val ausfuehrung = "0|0|0|0"
    },
    Zweimonatlich {

        override val intervallNameTextResID = R.string.intervall_alle2monate
        override val ausfuehrung = "0|0|2|0"
    },
    Halbjaehrlich {

        override val intervallNameTextResID = R.string.intervall_halbjaehrlich
        override val ausfuehrung = "0|0|6|0"
    },
    vierteljaehrlich {

        override val intervallNameTextResID = R.string.intervall_quartal
        override val ausfuehrung = "0|0|3|0"
    },
    Jaehrlich {

        override val intervallNameTextResID = R.string.intervall_jahr
        override val ausfuehrung = "0|0|0|1"
    },
    Woechentlich {

        override val intervallNameTextResID = R.string.intervall_woechentlich
        override val ausfuehrung = "1|0|0|0"
    };

    fun getIntervallName(context: Context): String {
        return context.getString(intervallNameTextResID)
    }

    abstract val intervallNameTextResID: Int

    /**
     * Intervall der Ausfuehrung: Aufbau a|b|c|d mit Anzahl a=Tage, b=Wochen,
     * c=Monate,d=Jahre. null: Einmalig. Beispiel 0|1|2|1 = naechste Ausführung
     * in 0 Tagen + 1 Woche + 2 Monate + 1 Jahr
     */
    abstract val ausfuehrung: String
}