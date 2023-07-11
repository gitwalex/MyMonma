package com.gerwalex.mymonma.ui.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.dashboard.CreditCardsScreen
import com.gerwalex.mymonma.ui.dashboard.GirokontenScreen
import com.gerwalex.mymonma.ui.dashboard.OrdinaryIncomeReport
import com.gerwalex.mymonma.ui.dashboard.VermoegenScreen
import com.gerwalex.mymonma.ui.dashboard.YearMonthReport
import com.gerwalex.mymonma.ui.navigation.BottomNavigation
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.DrawerNavigation
import com.gerwalex.mymonma.ui.navigation.ImportCashTrxDest
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
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
                    TopToolBar(title = stringResource(id = R.string.app_name),
                        Icons.Default.Menu,
                        actions = {
                            IconButton(
                                onClick = { navigateTo(ImportCashTrxDest) },
                                modifier = Modifier.scale(1.5f)
                            ) {
                                Icon(
                                    Icons.Default.ImportExport,
                                    stringResource(id = R.string.inportCashTrx)
                                )
                            }
                            KursDownloadIndicatorIcon(viewModel = viewModel)

                        }) {
                        scope.launch { drawerState.open() }
                    }
                },
                bottomBar = {
                    BottomNavigation(navigateTo = navigateTo)
                },
                content = {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Adaptive(420.dp),
                        modifier = Modifier.padding(it)
                    ) {
                        item { VermoegenScreen(viewModel = viewModel) }
                        item { GirokontenScreen(viewModel = viewModel, navigateTo = navigateTo) }
                        item { CreditCardsScreen(viewModel = viewModel, navigateTo = navigateTo) }
                        item { YearMonthReport(reportid = 1L) }
                        item { OrdinaryIncomeReport() }
                    }
                },
            )
        }
    )
}

@Composable
fun KursDownloadIndicatorIcon(viewModel: MonMaViewModel) {
    val scope = rememberCoroutineScope()
    Box {
        var downloadIsActive by rememberState { false }
        if (downloadIsActive) {
            CircularProgressIndicator()

        } else {
            IconButton(
                onClick = {
                    scope.launch {
                        viewModel.startDownloadKurse().collect { state ->
                            Log.d("HomeScreen", "KursDownloadIndicatorIcon: ${state}")
                            downloadIsActive = when {
                                state.progress < 0 -> false
                                state.progress in 0..99 -> true
                                else -> false
                            }
                        }

                    }


                },
                modifier = Modifier.scale(1.5f)
            ) {
                Icon(
                    Icons.Default.Download,
                    stringResource(id = R.string.event_download_kurse)
                )
            }
        }
    }
}


