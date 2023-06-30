package com.gerwalex.mymonma.preferences

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.datastore.preferences.core.emptyPreferences
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.content.DateView
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import java.sql.Date


@Composable
fun SettingScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val settings by viewModel.dataStore.data.collectAsState(initial = emptyPreferences())
    var lastMaintenance by rememberState { 0L }
    var nextKursDownload by rememberState { 0L }
    lastMaintenance = settings[PreferenceKey.LastMaintenance] ?: -1
    nextKursDownload = settings[PreferenceKey.NextKursDownload] ?: -1

    Scaffold(
        topBar = {
            TopToolBar(
                title = stringResource(id = R.string.settings),
                navigateTo = { navigateTo(it) })
        }
    ) { padding ->


        Column(modifier = Modifier.padding(padding)) {
            Text(text = stringResource(id = R.string.settings), fontWeight = FontWeight.Bold)
            Text(text = stringResource(id = R.string.lastMaintenance))
            if (lastMaintenance > 0)
                DateView(date = Date(lastMaintenance)) else Text("--")
            Text(text = stringResource(id = R.string.nextKursDownload))
            if (nextKursDownload > 0)
                DateView(date = Date(nextKursDownload)) else Text("--")

        }
    }
}