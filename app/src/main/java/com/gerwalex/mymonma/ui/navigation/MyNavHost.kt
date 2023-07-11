package com.gerwalex.mymonma.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.preferences.ListFiles
import com.gerwalex.mymonma.preferences.SettingScreen
import com.gerwalex.mymonma.ui.lists.AccountListScreen
import com.gerwalex.mymonma.ui.lists.CashTrxListScreen
import com.gerwalex.mymonma.ui.lists.RegelmTrxList
import com.gerwalex.mymonma.ui.lists.WPBestandList
import com.gerwalex.mymonma.ui.lists.WPPaketList
import com.gerwalex.mymonma.ui.report.AddReportData
import com.gerwalex.mymonma.ui.report.EditReportData
import com.gerwalex.mymonma.ui.report.GeldflussDetailScreen
import com.gerwalex.mymonma.ui.report.GeldflussDetails
import com.gerwalex.mymonma.ui.report.PartnerDetailScreen
import com.gerwalex.mymonma.ui.report.PartnerGeldflussDetails
import com.gerwalex.mymonma.ui.report.ReportGeldflussVerglDetails
import com.gerwalex.mymonma.ui.report.ReportListScreen
import com.gerwalex.mymonma.ui.screens.AddCashTrxScreen
import com.gerwalex.mymonma.ui.screens.AddRegelmTrxScreen
import com.gerwalex.mymonma.ui.screens.EditCashTrxScreen
import com.gerwalex.mymonma.ui.screens.EditRegelmTrxScreen
import com.gerwalex.mymonma.ui.screens.HomeScreen
import com.gerwalex.mymonma.ui.screens.ImportScreen
import com.gerwalex.mymonma.ui.wp.IncomeScreen
import com.gerwalex.mymonma.ui.wp.WPKaufScreen

@Composable
fun MyNavHost(
    navController: NavHostController,
    viewModel: MonMaViewModel,
    navigateTo: (destination: Destination) -> Unit
) {
    NavHost(navController = navController, startDestination = HomeDest.name) {
        composable(HomeDest.name) {
            HomeScreen(viewModel = viewModel, navigateTo)
        }
        composable(SettingsDest.name) {
            SettingScreen(viewModel, navigateTo)
        }
        composable(RestoreDatabaseDest.name) {
            ListFiles("zip", navigateTo = navigateTo)
        }
        composable(AccountListDest.name) {
            AccountListScreen(viewModel, navigateTo)
        }
        composable(ImportDataDest.name) {
            ImportScreen(navigateTo)
        }
        composable(CashTrxListDest.route, CashTrxListDest.arguments) {
            val id = it.arguments?.getLong("id") ?: -1
            CashTrxListScreen(id, viewModel, navigateTo)
        }
        composable(RegelmTrxListDest.name) {
            RegelmTrxList(viewModel, navigateTo)
        }
        composable(AddCashTrxDest.route, AddCashTrxDest.arguments) {
            val id = it.arguments?.getLong("id") ?: -1
            AddCashTrxScreen(id, viewModel, navigateTo)
        }
        composable(EditCashTrxDest.route, EditCashTrxDest.arguments) {
            val id = it.arguments?.getLong("id") ?: -1
            EditCashTrxScreen(id, viewModel, navigateTo)
        }
        composable(AddRegelmTrxDest.route) {
            AddRegelmTrxScreen(viewModel, navigateTo)
        }
        composable(EditRegelmTrxDest.route, EditRegelmTrxDest.arguments) {
            val id = it.arguments?.getLong("id") ?: -1
            EditRegelmTrxScreen(id, viewModel, navigateTo)
        }
        composable(ReportListDest.name) {
            ReportListScreen(viewModel, navigateTo)
        }
        composable(EditReportDest.route, EditReportDest.arguments) {
            val id = it.arguments?.getLong("id") ?: -1
            EditReportData(id, viewModel, navigateTo)
        }
        composable(AddReportDest.name) {
            AddReportData(viewModel, navigateTo)
        }
        composable(GeldflussDetailScreenDest.route, GeldflussDetailScreenDest.arguments) {
            val reportid = it.arguments?.getLong("reportid") ?: -1
            GeldflussDetailScreen(reportid, navigateTo)
        }
        composable(PartnerDetailScreenDest.route, PartnerDetailScreenDest.arguments) {
            val reportid = it.arguments?.getLong("reportid") ?: -1
            PartnerDetailScreen(reportid, navigateTo)
        }
        composable(ReportGeldflussDetailDest.route, ReportGeldflussDetailDest.arguments) {
            val reportid = it.arguments?.getLong("reportid") ?: -1
            val catid = it.arguments?.getLong("catid") ?: -1
            GeldflussDetails(reportid, catid, viewModel, navigateTo)
        }
        composable(ReportGeldflussVerglDetailDest.route, ReportGeldflussVerglDetailDest.arguments) {
            val reportid = it.arguments?.getLong("reportid") ?: -1
            val catid = it.arguments?.getLong("catid") ?: -1
            ReportGeldflussVerglDetails(reportid, catid, viewModel, navigateTo)
        }
        composable(PartnerGeldflussDetailsDest.route, PartnerGeldflussDetailsDest.arguments) {
            val reportid = it.arguments?.getLong("reportid") ?: -1
            val partnerid = it.arguments?.getLong("partnerid") ?: -1
            PartnerGeldflussDetails(reportid, partnerid, viewModel, navigateTo)
        }
        composable(WPBestandListDest.name) {
            WPBestandList(viewModel, navigateTo)
        }
        composable(WPPaketListDest.route, WPPaketListDest.arguments) {
            val wpid = it.arguments?.getLong("wpid") ?: -1
            WPPaketList(wpid, navigateTo)
        }
        composable(WPKaufDest.route, WPKaufDest.arguments) {
            val wpid = it.arguments?.getLong("wpid") ?: -1
            WPKaufScreen(wpid, viewModel, navigateTo)
        }
        composable(EinnahmenDest.route, EinnahmenDest.arguments) {
            val wpid = it.arguments?.getLong("wpid") ?: -1
            IncomeScreen(wpid, viewModel, navigateTo)
        }

    }
}



