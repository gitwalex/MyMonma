package com.gerwalex.mymonma.ui.subscreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gerwalex.mymonma.database.data.ExcludedCatClassesCheckBoxes
import com.gerwalex.mymonma.database.data.ExcludedCatsCheckBoxes

@Composable
fun GeldflussDrawer(reportid: Long) {

    Column {
        Box(modifier = Modifier.fillMaxHeight(.5f)) {
            ExcludedCatClassesCheckBoxes(reportid = reportid)
        }
        Divider()
        Box(modifier = Modifier.fillMaxHeight(.5f)) {
            ExcludedCatsCheckBoxes(reportid = reportid)
        }
    }
}