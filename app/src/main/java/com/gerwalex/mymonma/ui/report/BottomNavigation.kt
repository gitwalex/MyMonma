package com.gerwalex.mymonma.ui.report

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.ui.AppTheme


enum class ExcludedValuesSheet {
    Classes, Cats
}

@Composable
fun BottomNavigationBar(open: (ExcludedValuesSheet) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { open(ExcludedValuesSheet.Classes) },
            icon = { Icon(Icons.Default.DateRange, "") },
            label = {
                Text(
                    text = stringResource(id = R.string.zeitraum),
                    style = MaterialTheme.typography.labelSmall
                )
            })
        NavigationBarItem(
            selected = false,
            onClick = { open(ExcludedValuesSheet.Classes) },
            icon = { Icon(Icons.Default.DateRange, "") },
            label = {
                Text(
                    text = stringResource(id = R.string.vergleich),
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1
                )
            })
        NavigationBarItem(
            selected = false,
            onClick = { open(ExcludedValuesSheet.Classes) },
            icon = { Icon(Icons.Default.List, "") },
            label = {
                Text(
                    text = stringResource(id = R.string.catClasses),
                    style = MaterialTheme.typography.labelSmall
                )
            })

        NavigationBarItem(
            selected = false,
            onClick = { open(ExcludedValuesSheet.Cats) },
            icon = { Icon(Icons.Default.List, "") },
            label = {
                Text(
                    text = stringResource(id = R.string.cats),
                    style = MaterialTheme.typography.labelSmall
                )
            })
    }
}


@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun BarPreview() {
    AppTheme {
        Surface {
            BottomNavigationBar {}
        }
    }
}
