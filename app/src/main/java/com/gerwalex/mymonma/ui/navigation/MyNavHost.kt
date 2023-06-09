package com.gerwalex.mymonma.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gerwalex.mymonma.MonMaViewModel
import com.gerwalex.mymonma.database.views.CashTrxList
import com.gerwalex.mymonma.ui.screens.CashTrxList
import com.gerwalex.mymonma.ui.screens.Destination
import com.gerwalex.mymonma.ui.screens.Home
import com.gerwalex.mymonma.ui.screens.HomeScreen

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
        composable(CashTrxList.name) {
            CashTrxList(viewModel, navigateTo)
        }
    }
}



