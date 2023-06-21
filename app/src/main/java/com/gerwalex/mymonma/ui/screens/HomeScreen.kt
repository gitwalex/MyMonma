package com.gerwalex.mymonma.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.DrawerNavigation
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.WPBestandList
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                DrawerNavigation { destination ->
                    scope.launch {
                        drawerState.close()
                        navigateTo(destination)
                    }
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopToolBar(title = stringResource(id = R.string.app_name), Icons.Default.Menu) {
                        scope.launch { drawerState.open() }
                    }
                },
                bottomBar = {
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
                    }
                },
                content = {
                    Box(modifier = Modifier.padding(it)) {
                        AccountListScreen(viewModel, navigateTo)
                    }
                },
            )
        }
    )
}


