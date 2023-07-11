@file:OptIn(ExperimentalMaterial3Api::class)

package com.gerwalex.mymonma.ui.navigation

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.ui.AppTheme


@Composable
fun TopToolBar(
    title: String,
    imageVector: ImageVector = Icons.Default.ArrowBack,
    @StringRes
    description: Int = R.string.back,
    actions: @Composable (RowScope.() -> Unit) = {},
    navigateTo: (Destination) -> Unit,
) {
    val myTopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer

    )

    TopAppBar(
        title = { Text(text = title, style = MaterialTheme.typography.titleLarge, maxLines = 1) },
        actions = actions,
        colors = myTopAppBarColors,
        navigationIcon = {
            IconButton(onClick = { navigateTo(Up) }) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = stringResource(id = description)
                )
            }
        })
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "de")
@Composable
fun Preview() {
    AppTheme {
        Surface {
            TopToolBar(title = stringResource(id = HomeDest.title)) {}
        }
    }
}