package com.gerwalex.mymonma.preferences

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey

object PreferenceKey {
    /**
     * Daueraufträge-Ausführung: Anzahl der Tage vor Fälligkeit (default: 3)
     */
    val AnzahlTage = intPreferencesKey("AnzahlTage")
    val LastMaintenance = longPreferencesKey("LastMaintenance")
    val NextKursDownload = longPreferencesKey("LastMaintenance")
}