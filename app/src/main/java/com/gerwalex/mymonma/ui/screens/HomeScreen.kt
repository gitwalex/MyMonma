package com.gerwalex.mymonma.ui.screens

import androidx.compose.runtime.Composable
import com.gerwalex.mymonma.MonMaViewModel
import com.gerwalex.mymonma.ui.navigation.Destination

@Composable
fun HomeScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    AccountListScreen(viewModel, navigateTo)
}
