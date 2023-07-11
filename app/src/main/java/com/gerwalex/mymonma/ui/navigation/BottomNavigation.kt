package com.gerwalex.mymonma.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomNavigation(navigateTo: (Destination) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { navigateTo(WPBestandListDest) },
            icon = { Icon(Icons.Default.List, "") },
            label = {
                Text(
                    text = "Depot",
                    style = MaterialTheme.typography.labelSmall
                )
            })

        NavigationBarItem(
            selected = false,
            onClick = { navigateTo(ReportListDest) },
            icon = { Icon(Icons.Default.List, "") },
            label = {
                Text(
                    text = "Reports",
                    style = MaterialTheme.typography.labelSmall
                )
            })
        NavigationBarItem(
            selected = false,
            onClick = { navigateTo(AccountListDest) },
            icon = { Icon(Icons.Default.List, "") },
            label = {
                Text(
                    text = "Konten",
                    style = MaterialTheme.typography.labelSmall
                )
            })
        NavigationBarItem(
            selected = false,
            onClick = { navigateTo(RegelmTrxListDest) },
            icon = { Icon(Icons.Default.List, "") },
            label = {
                Text(
                    text = "Daueraufträge",
                    style = MaterialTheme.typography.labelSmall
                )
            })
    }
}