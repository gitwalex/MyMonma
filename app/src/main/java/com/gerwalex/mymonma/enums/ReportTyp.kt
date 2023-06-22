package com.gerwalex.mymonma.enums

/**
 * Created by alex on 18.06.2015.
 */
enum class ReportTyp {
    Geldfluss {
        override val description: Int
            get() = TODO("Not yet implemented")
        override val textID: Int
            get() = TODO("Not yet implemented")
    };

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