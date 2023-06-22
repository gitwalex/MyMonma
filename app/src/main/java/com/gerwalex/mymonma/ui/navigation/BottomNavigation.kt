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
            onClick = { navigateTo(WPBestandList) },
            icon = { Icon(Icons.Default.List, "") },
            label = {
                Text(
                    text = "Depot",
                    style = MaterialTheme.typography.labelSmall
                )
            })

        NavigationBarItem(
            selected = false,
            onClick = { navigateTo(ReportList) },
            icon = { Icon(Icons.Default.List, "") },
            label = {
                Text(
                    text = "Reports",
                    style = MaterialTheme.typography.labelSmall
                )
            })
    }
}