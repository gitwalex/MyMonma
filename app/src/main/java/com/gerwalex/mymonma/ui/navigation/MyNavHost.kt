package com.gerwalex.mymonma.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gerwalex.mymonma.MonMaViewModel
import com.gerwalex.mymonma.ui.screens.AddCashTrxScreen
import com.gerwalex.mymonma.ui.screens.AddRegelmTrxScreen
import com.gerwalex.mymonma.ui.screens.CashTrxList
import com.gerwalex.mymonma.ui.screens.EditCashTrxScreen
import com.gerwalex.mymonma.ui.screens.EditRegelmTrxScreen
import com.gerwalex.mymonma.ui.screens.HomeScreen
import com.gerwalex.mymonma.ui.screens.ImportScreen
import com.gerwalex.mymonma.ui.screens.RegelmTrxList
import com.gerwalex.mymonma.ui.screens.WPBestandList
import com.gerwalex.mymonma.wptrx.EinnahmenScreen

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
        composable(ImportData.name) {
            ImportScreen(navigateTo)
        }
        composable(CashTrxList.name) {
            CashTrxList(viewModel, navigateTo)
        }
        composable(RegelmTrxList.name) {
            RegelmTrxList(viewModel, navigateTo)
        }
        composable(AddCashTrx.name) {
            AddCashTrxScreen(viewModel, navigateTo)
        }
        composable(EditCashTrx.name) {
            EditCashTrxScreen(viewModel, navigateTo)
        }
        composable(AddRegelmTrx.name) {
            AddRegelmTrxScreen(viewModel, navigateTo)
        }
        composable(EditRegelmTrx.name) {
            EditRegelmTrxScreen(viewModel, navigateTo)
        }
        composable(WPBestandList.name) {
            WPBestandList(viewModel, navigateTo)
        }
        composable(Einnahmen.name) {
            EinnahmenScreen(viewModel, navigateTo)
        }

    }
}



