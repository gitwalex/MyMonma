package com.gerwalex.mymonma.enums

import android.content.Context
import android.graphics.Color
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.tables.Cat

enum class Kontotyp {
    Giro {

        override val supercatID = Cat.CASHKONTOCATID
        override var color = Color.parseColor("#0Fffff00")
        override val catID = 1003
        override val textID = R.string.shortGiro
        override val description = R.string.shortGiro
        override val obercatID = 1003L
    },
    Kreditkarte {

        override val supercatID = Cat.CASHKONTOCATID
        override var color = Color.parseColor("#0Fffff00")
        override val textID = R.string.shortKK
        override var isVerrechnungsKontoNeeded = true
        override val catID = 1004
        override val description = R.string.shortKK
        override val obercatID = 1004L
    },
    Depot {

        override val supercatID = Cat.DEPOTCATID
        override var color = Color.parseColor("#0F0000FF")
        override var isVerrechnungsKontoNeeded = true
        override val catID = 1005
        override val textID = R.string.shortDepo
        override val description = R.string.shortDepo
        override val obercatID = 1005L
    },
    Vermoegen {

        override val supercatID = Cat.CASHKONTOCATID
        override val catID = 1006
        override val textID = R.string.shortVerm
        override val description = R.string.shortVerm
        override val obercatID = 1006L
    },
    Anlagen {

        override val supercatID = Cat.CASHKONTOCATID
        override val catID = 1007
        override val textID = R.string.shortAnlg
        override val description = R.string.shortAnlg
        override val obercatID = 1007L
    },
    Verbindlichkeiten {

        override val supercatID = Cat.CASHKONTOCATID
        override val textID = R.string.shortVerb
        override var isVerrechnungsKontoNeeded = true
        override val catID = 1008
        override val description = R.string.shortVerb
        override val obercatID = 1008L
    },
    Immobilien {

        override val supercatID = Cat.CASHKONTOCATID
        override val catID = 1009
        override val textID = R.string.longImmo
        override val description = R.string.longImmo
        override val obercatID = 1009L
    };

    abstract val catID: Int
    open var color = Color.parseColor("#0Fffff00")
    abstract val description: Int
    abstract val obercatID: Long
    abstract val supercatID: Long
    abstract val textID: Int
    open var isVerrechnungsKontoNeeded = false
    fun getText(context: Context): String {
        return context.getString(textID)
    }

    companion object {

        fun find(obercatid: Long): Kontotyp {
            for (typ in values()) {
                if (typ.obercatID == obercatid) {
                    return typ
                }
            }
            throw IllegalArgumentException("Kein Kontotyp für obercatid $obercatid")
        }
    }
}