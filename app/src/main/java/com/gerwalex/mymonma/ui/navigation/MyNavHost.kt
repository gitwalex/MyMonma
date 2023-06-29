package com.gerwalex.mymonma.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.preferences.SettingScreen
import com.gerwalex.mymonma.ui.lists.CashTrxListScreen
import com.gerwalex.mymonma.ui.lists.RegelmTrxList
import com.gerwalex.mymonma.ui.lists.WPBestandList
import com.gerwalex.mymonma.ui.report.AddReportData
import com.gerwalex.mymonma.ui.report.EditReportData
import com.gerwalex.mymonma.ui.report.EmpfaengerScreen
import com.gerwalex.mymonma.ui.report.GeldflussScreen
import com.gerwalex.mymonma.ui.report.ReportListScreen
import com.gerwalex.mymonma.ui.screens.AddCashTrxScreen
import com.gerwalex.mymonma.ui.screens.AddRegelmTrxScreen
import com.gerwalex.mymonma.ui.screens.EditCashTrxScreen
import com.gerwalex.mymonma.ui.screens.EditRegelmTrxScreen
import com.gerwalex.mymonma.ui.screens.HomeScreen
import com.gerwalex.mymonma.ui.screens.ImportScreen
import com.gerwalex.mymonma.ui.wp.IncomeScreen

@Composable
fun MyNavHost(
    navController: NavHostController,
    viewModel: MonMaViewModel,
    navigateTo: (destination: Destination) -> Unit
) {
    NavHost(navController = navController, startDestination = Home.name) {
        composable(Home.name) {
            HomeScreen(viewModel = viewModel, navigateTo)
        }
        composable(Settings.name) {
            SettingScreen(viewModel, navigateTo)
        }
        composable(ImportData.name) {
            ImportScreen(navigateTo)
        }
        composable(CashTrxList.name) {
            CashTrxListScreen(CashTrxList.id, viewModel, navigateTo)
        }
        composable(RegelmTrxList.name) {
            RegelmTrxList(viewModel, navigateTo)
        }
        composable(AddCashTrx.name) {
            AddCashTrxScreen(AddCashTrx.id, viewModel, navigateTo)
        }
        composable(EditCashTrx.name) {
            EditCashTrxScreen(EditCashTrx.id, viewModel, navigateTo)
        }
        composable(AddRegelmTrx.name) {
            AddRegelmTrxScreen(AddRegelmTrx.id, viewModel, navigateTo)
        }
        composable(EditRegelmTrx.name) {
            EditRegelmTrxScreen(EditRegelmTrx.id, viewModel, navigateTo)
        }
        composable(ReportList.name) {
            ReportListScreen(viewModel, navigateTo)
        }
        composable(ReportList.name) {
            ReportListScreen(viewModel, navigateTo)
        }
        composable(EditReport.name) {
            EditReportData(EditReport.id, viewModel, navigateTo)
        }
        composable(AddReport.name) {
            AddReportData(viewModel, navigateTo)
        }
        composable(GeldflussReport.name) {
            GeldflussScreen(GeldflussReport.id, viewModel, navigateTo)
        }
        composable(EmpfaengerReport.name) {
            EmpfaengerScreen(viewModel, navigateTo)
        }
        composable(WPBestandList.name) {
            WPBestandList(viewModel, navigateTo)
        }
        composable(Einnahmen.name) {
            IncomeScreen(viewModel, navigateTo)
        }

    }
}



