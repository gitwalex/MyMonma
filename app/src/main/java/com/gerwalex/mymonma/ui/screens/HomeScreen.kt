package com.gerwalex.mymonma.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.dashboard.CreditCardsScreen
import com.gerwalex.mymonma.ui.dashboard.GirokontenScreen
import com.gerwalex.mymonma.ui.dashboard.OrdinaryIncomeReport
import com.gerwalex.mymonma.ui.dashboard.VermoegenScreen
import com.gerwalex.mymonma.ui.dashboard.YearMonthReport
import com.gerwalex.mymonma.ui.navigation.BottomNavigation
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.DrawerNavigation
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.clickable {
            scope.launch {
                drawerState.close()
            }
        },
        gesturesEnabled = false,
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
                    BottomNavigation(navigateTo = navigateTo)
                },
                content = {
                    Column(modifier = Modifier.padding(it)) {
                        VermoegenScreen(viewModel = viewModel)
                        GirokontenScreen(viewModel = viewModel, navigateTo = navigateTo)
                        CreditCardsScreen(viewModel = viewModel, navigateTo = navigateTo)
                        YearMonthReport(reportid = 1L)
                        OrdinaryIncomeReport()
                    }
                },
            )
        }
    )
}


