package com.gerwalex.mymonma.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.emptyPreferences
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.ext.backup
import com.gerwalex.mymonma.ext.getUniqueWorkInfoState
import com.gerwalex.mymonma.ext.rememberState
import com.gerwalex.mymonma.ext.restore
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.content.DateTimeView
import com.gerwalex.mymonma.ui.content.NoEntriesBox
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.RestoreDatabaseDest
import com.gerwalex.mymonma.ui.navigation.TopToolBar
import com.gerwalex.mymonma.ui.navigation.Up
import com.gerwalex.mymonma.workers.KursDownloadWorker
import com.gerwalex.mymonma.workers.MaintenanceWorker
import kotlinx.coroutines.launch
import java.io.File
import java.sql.Date


@Composable
fun SettingScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val settings by viewModel.dataStore.data.collectAsStateWithLifecycle(emptyPreferences())
    val lastMaintenance by rememberState(settings) { settings[PreferenceKey.LastMaintenance] ?: -1 }
    val nextKursDownload by rememberState(settings) {
        settings[PreferenceKey.NextKursDownload] ?: -1
    }
    var maintenceWorkerStatus by rememberState { "Unknown" }
    var downloadWorkerStatus by rememberState { "Unknown" }
    LaunchedEffect(Unit) {
        maintenceWorkerStatus =
            context.getUniqueWorkInfoState(MaintenanceWorker.tag)?.name ?: "No Work found"
        downloadWorkerStatus =
            context.getUniqueWorkInfoState(KursDownloadWorker.tag)?.name ?: "No Work found"
    }
    Scaffold(
        topBar = {
            TopToolBar(
                title = stringResource(id = R.string.settings),
                navigateTo = { navigateTo(it) })
        }
    ) { padding ->


        Column(modifier = Modifier.padding(padding)) {
            Text(text = stringResource(id = R.string.database), fontWeight = FontWeight.Bold)
            TextButton(onClick = {
                scope.launch { context.backup(DB.DBNAME) }
            }) {
                Text(text = stringResource(id = R.string.backupNow))
            }
            TextButton(onClick = {
                navigateTo(RestoreDatabaseDest)
            }) {
                Text(text = stringResource(id = R.string.restoreDB))
            }
            Spacer(modifier = Modifier.height(16.dp))


            Text(
                text = stringResource(id = R.string.settings),
                fontWeight = FontWeight.Bold
            )
            Text(text = stringResource(id = R.string.lastMaintenance))
            if (lastMaintenance > 0)
                DateTimeView(date = Date(lastMaintenance)) else Text("--")
            Text("($maintenceWorkerStatus)")
            Text(text = stringResource(id = R.string.nextKursDownload))
            if (nextKursDownload > 0)
                DateTimeView(date = Date(nextKursDownload)) else Text("--")
            Text("($downloadWorkerStatus)")

        }
    }
}

@Composable
fun ListFiles(extension: String = "zip", navigateTo: (Destination) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var files by rememberState { listOf<File>() }
    LaunchedEffect(extension) {
        files = context.filesDir.listFiles()?.filter { it.name.endsWith(".$extension") }
            ?: ArrayList()

    }
    Scaffold(
        topBar = {
            TopToolBar(title = context.filesDir.name, navigateTo = { navigateTo(Up) })
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (files.isEmpty()) {
                NoEntriesBox()
            } else {
                LazyColumn {
                    items(files) { file ->
                        Row(
                            modifier = Modifier.clickable {
                                scope.launch {
                                    DB.get().close()
                                    context.restore(file, DB.DBNAME)
                                    DB.createInstance(context)
                                }
                            },
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.FileOpen, contentDescription = "")
                            Text(text = file.name)

                        }
                    }
                }
            }
        }
    }
}