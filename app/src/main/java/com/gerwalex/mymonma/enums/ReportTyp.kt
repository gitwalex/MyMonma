package com.gerwalex.mymonma.enums

import com.gerwalex.mymonma.R

/**
 * Created by alex on 18.06.2015.
 */
enum class ReportTyp {
    Geldfluss {
        override val description = R.string.reportGeldflussDesc
        override val textID = R.string.reportGeldfluss
    },
    GeldflussVergl {
        override val description = R.string.reportGeldflussVerglDesc
        override val textID = R.string.reportGeldflussVergl
        override val isVergl = true

    },
    Empfaenger {
        override val description = R.string.reportEmpfaengerDesc
        override val textID = R.string.reportEmpfaenger

    }

    ;

    abstract val description: Int
    abstract val textID: Int

    /**
     * true, wenn es ein Report zu einem bestimmten Datum (== Report.bisDate() ) ist. Default =
     * false
     */
    open val isFixDay = false

    /**
     * true, wenn es ein Vergleichsreport ist. Default = false
     */
    open val isVergl = false
}