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
import com.gerwalex.mymonma.ui.report.GeldflussDetails
import com.gerwalex.mymonma.ui.report.PartnerGeldflussDetails
import com.gerwalex.mymonma.ui.report.ReportDetailScreen
import com.gerwalex.mymonma.ui.report.ReportGeldflussVerglDetails
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
        composable(CashTrxList.route, CashTrxList.arguments) {
            val id = it.arguments?.getLong("id") ?: -1
            CashTrxListScreen(id, viewModel, navigateTo)
        }
        composable(RegelmTrxList.name) {
            RegelmTrxList(viewModel, navigateTo)
        }
        composable(AddCashTrx.route, AddCashTrx.arguments) {
            val id = it.arguments?.getLong("id") ?: -1
            AddCashTrxScreen(id, viewModel, navigateTo)
        }
        composable(EditCashTrx.route, EditCashTrx.arguments) {
            val id = it.arguments?.getLong("id") ?: -1
            EditCashTrxScreen(id, viewModel, navigateTo)
        }
        composable(AddRegelmTrx.route, AddRegelmTrx.arguments) {
            val id = it.arguments?.getLong("id") ?: -1
            AddRegelmTrxScreen(id, viewModel, navigateTo)
        }
        composable(EditRegelmTrx.route, EditRegelmTrx.arguments) {
            val id = it.arguments?.getLong("id") ?: -1
            EditRegelmTrxScreen(id, viewModel, navigateTo)
        }
        composable(ReportList.name) {
            ReportListScreen(viewModel, navigateTo)
        }
        composable(EditReport.route, EditReport.arguments) {
            val id = it.arguments?.getLong("id") ?: -1
            EditReportData(id, viewModel, navigateTo)
        }
        composable(AddReport.name) {
            AddReportData(viewModel, navigateTo)
        }
        composable(ReportDetailScreen.route, ReportDetailScreen.arguments) {
            val reportid = it.arguments?.getLong("reportid") ?: -1
            ReportDetailScreen(reportid, viewModel, navigateTo)
        }
        composable(ReportGeldflussDetail.route, ReportGeldflussDetail.arguments) {
            val reportid = it.arguments?.getLong("reportid") ?: -1
            val catid = it.arguments?.getLong("catid") ?: -1
            GeldflussDetails(reportid, catid, viewModel, navigateTo)
        }
        composable(ReportGeldflussVerglDetail.route, ReportGeldflussVerglDetail.arguments) {
            val reportid = it.arguments?.getLong("reportid") ?: -1
            val catid = it.arguments?.getLong("catid") ?: -1
            ReportGeldflussVerglDetails(reportid, catid, viewModel, navigateTo)
        }
        composable(PartnerGeldflussDetails.route, PartnerGeldflussDetails.arguments) {
            val reportid = it.arguments?.getLong("reportid") ?: -1
            val partnerid = it.arguments?.getLong("partnerid") ?: -1
            PartnerGeldflussDetails(reportid, partnerid, viewModel, navigateTo)
        }
        composable(WPBestandList.name) {
            WPBestandList(viewModel, navigateTo)
        }
        composable(Einnahmen.route, Einnahmen.arguments) {
            val wpid = it.arguments?.getLong("wpid") ?: -1
            IncomeScreen(wpid, viewModel, navigateTo)
        }

    }
}



