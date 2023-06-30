package com.gerwalex.mymonma.preferences

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.sql.Date


@Composable
fun SettingScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    var lastMaintenance by rememberState { 0L }
    var nextKursDownload by rememberState { 0L }
    LaunchedEffect(Unit) {
        viewModel.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map {
                lastMaintenance = it[PreferenceKey.LastMaintenance] ?: -1
                nextKursDownload = it[PreferenceKey.NextKursDownload] ?: -1
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
            Text(text = stringResource(id = R.string.lastMaintenance))
            if (lastMaintenance > 0)
                DateView(date = Date(lastMaintenance)) else Text("--")
            Text(text = stringResource(id = R.string.nextKursDownload))
            if (nextKursDownload > 0)
                DateView(date = Date(nextKursDownload)) else Text("--")

        }
    }
}