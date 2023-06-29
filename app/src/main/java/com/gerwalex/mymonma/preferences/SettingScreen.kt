package com.gerwalex.mymonma.preferences

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.MyConverter
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import kotlinx.coroutines.flow.map


@Composable
fun SettingScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val settings = remember(key1 = viewModel) { viewModel.dataStore }
    var lastMaintenance by rememberState { 0L }
    LaunchedEffect(key1 = settings.data) {
        settings.data.map {
            lastMaintenance = it[PreferenceKey.LastMaintenance] ?: 0
        }
    }
    Scaffold(
        topBar = {
            TopToolBar(
                title = stringResource(id = R.string.settings),
                navigateTo = { navigateTo(it) })
        }
    ) { padding ->


        Column(modifier = Modifier.padding(padding)) {
            Text(text = stringResource(id = R.string.settings), fontWeight = FontWeight.Bold)
            if (lastMaintenance != 0L) {
                Text(
                    text = stringResource(
                        id = R.string.lastMaintenance,
                        MyConverter.convertDate(lastMaintenance)
                    )
                )
            }
        }
    }
}