package com.gerwalex.mymonma.ui.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gerwalex.mymonma.database.data.GeldflussData
import com.gerwalex.mymonma.database.data.GeldflussDataItem
import com.gerwalex.mymonma.database.tables.ReportBasisDaten
import com.gerwalex.mymonma.main.MonMaViewModel
import com.gerwalex.mymonma.ui.navigation.Destination


@Composable
fun GeldflussVerglScreen(viewModel: MonMaViewModel, navigateTo: (Destination) -> Unit) {
}

@Composable
fun GeldflussVerglDetailScreen(
    reportBasisDaten: ReportBasisDaten,
    list: List<GeldflussData>,
    onSelected: (GeldflussData) -> Unit

) {
    Column {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(list, key = { it.catid }) { item ->
                GeldflussDataItem(trx = item, onClicked = {
                    onSelected(item)
                })
            }
        }
    }

}

