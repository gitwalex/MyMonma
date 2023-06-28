package com.gerwalex.mymonma.database.data

data class GesamtVermoegen(
    val saldo: Long = 0L,
    val marktwert: Long = 0L
) {
    val summe: Long
        get() {
            return saldo + marktwert
        }
}