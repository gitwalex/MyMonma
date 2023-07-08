package com.gerwalex.mymonma.ui.spinner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gerwalex.mymonma.database.views.AccountCashView
import com.gerwalex.mymonma.ext.rememberState


@Composable
fun AccountCashSpinner(
    accountid: Long,
    accounts: List<AccountCashView>,
    selected: (AccountCashView) -> Unit
) {
    if (accounts.isNotEmpty()) {
        var myAccount by rememberState { accounts[0] }
        LaunchedEffect(Unit) {
            accounts.forEach {
                if (it.id == accountid) {
                    myAccount = it
                }
            }
        }
        var isExpanded by rememberState { false }
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = myAccount.name,
                modifier = Modifier.clickable {
                    isExpanded = !isExpanded
                })
            DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                accounts
                    .forEach { account ->
                        DropdownMenuItem(
                            text = { Text(text = account.name) },
                            onClick = {
                                myAccount = account
                                selected(account)
                                isExpanded = false
                            })
                    }

            }
        }
    }
}
