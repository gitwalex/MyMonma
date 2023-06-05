package com.gerwalex.mymonma.ui.screens

import android.util.Log
import androidx.compose.runtime.Composable
import com.gerwalex.monmang.ui.content.AutocompletePartner
import com.gerwalex.mymonma.MonMaViewModel
import com.gerwalex.mymonma.ui.navigation.Destination

@Composable
fun HomeScreen(viewModel: MonMaViewModel, navigatTo: (Destination) -> Unit) {

    AutocompletePartner(viewModel = viewModel) {
        Log.d("HomeScreen", "HomeScreen: $it")
    }
}