package com.gerwalex.mymonma.ui.navigation

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.ui.AppTheme


@Composable
fun TopToolBar(
    current: Destination,
    navigateTo: (Destination) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val imageVector =
        if (current == Home && drawerState.isClosed)
            Icons.Default.Menu else Icons.Default.ArrowBack

    TopAppBar(
        title = {
            Text(
                text = stringResource(id = current.title),
            )
        },
        elevation = 1.dp,
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .clickable { navigateTo(Up) }
                    .padding(8.dp),
                imageVector = imageVector,
                contentDescription =
                if (current == Home)
                    stringResource(id = androidx.compose.ui.R.string.navigation_menu)
                else stringResource(id = R.string.back)
            )
        })
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "de")
@Composable
fun Preview() {
    AppTheme {
        Surface {
            TopToolBar(current = Home) {}
        }
    }
}