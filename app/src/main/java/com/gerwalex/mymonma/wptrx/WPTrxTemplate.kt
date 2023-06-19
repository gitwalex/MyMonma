package com.gerwalex.mymonma.wptrx

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.ui.content.MengeView

sealed class WPTrxTemplate {
    abstract fun executeTransaktion()
}

/**
 * View zur Erfassung von Beträgen zu einem WP-Bestand eines Accounts
 * @param account AccountBestand
 * @param amount initialer Wert
 * @param onAmountChanged old: alter Wert, new: neuer Wert
 */
@Composable
fun AccountBestand(
    account: AccountBestand
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = account.name)
        MengeView(value = account.bestand)
    }
}

