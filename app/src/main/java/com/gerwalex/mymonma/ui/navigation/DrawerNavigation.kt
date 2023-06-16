package com.gerwalex.mymonma.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.ui.AppTheme

fun gradientColors(): List<Color> {
    return listOf(
        Color(0x2033B5E5),
        Color(0x20FFBB33),
        Color(0x2099CC00),
    )
}


@Composable
fun DrawerNavigation(
    navigateTo: (Destination) -> Unit
) {
    val navigationItemsList = prepareNavigationItems()
    val settingsItemsList = prepareSettingsItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .background(brush = Brush.verticalGradient(colors = gradientColors())),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 36.dp)
    ) {

        item {

            // user's image
            Image(
                modifier = Modifier
                    .defaultMinSize(minHeight = 48.dp)
                    .size(120.dp)
                    .clip(shape = CircleShape),
                painter = painterResource(id = R.mipmap.euro_symbol),
                contentDescription = stringResource(id = R.string.app_name)
            )

            // user's name
            Text(
                modifier = Modifier
                    .defaultMinSize(minHeight = 48.dp)
                    .clickable { }
                    .padding(top = 12.dp),
                text = stringResource(id = R.string.app_name),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
            )

            //
            Text(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 30.dp)
                    .defaultMinSize(minHeight = 48.dp),
                text = stringResource(id = R.string.contact_mail),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            )
//            account?.let {
//                Text(
//                    modifier = Modifier
//                        .padding(top = 8.dp, bottom = 30.dp),
//                    text = it.displayName ?: "logged In via Google",
//                    fontWeight = FontWeight.Normal,
//                    fontSize = 16.sp,
//                )
//
//            }
            Divider()
        }
        items(navigationItemsList.size) {
            NavigationListItem(item = navigationItemsList[it]) {
                navigateTo(navigationItemsList[it].destination)
            }

        }

        item {
            Divider(
            )
            Text(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 30.dp)
                    .clickable { navigateTo(Settings) },
                text = stringResource(id = R.string.settings),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            )
        }
        items(settingsItemsList.size) {
            NavigationListItem(item = settingsItemsList[it]) {
                navigateTo(settingsItemsList[it].destination)
            }

        }
    }
}


@Composable
private fun NavigationListItem(
    item: NavigationDrawerItem,
    itemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                itemClick()
            }
            .padding(horizontal = 24.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // icon and unread bubble
        Box {

            Icon(
                modifier = Modifier
                    .padding(all = 2.dp)
                    .size(size = 28.dp),
                imageVector = item.imageVector,
                contentDescription = null,
            )

            // unread bubble
        }

        // label
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = item.label,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun prepareSettingsItems(): List<NavigationDrawerItem> {
    val itemsList = arrayListOf<NavigationDrawerItem>()

    itemsList.add(
        NavigationDrawerItem(
            imageVector = Icons.Filled.Settings,
            label = stringResource(id = R.string.settings),
            destination = Settings
        )
    )
    return itemsList
}

@Composable
private fun prepareNavigationItems(): List<NavigationDrawerItem> {
    val itemsList = arrayListOf<NavigationDrawerItem>()
    itemsList.add(
        NavigationDrawerItem(
            imageVector = Icons.Filled.List,
            label = stringResource(id = R.string.trxRegelm),
            destination = RegelmTrxList,
        )
    )
    itemsList.add(
        NavigationDrawerItem(
            imageVector = Icons.Filled.ImportExport,
            label = stringResource(id = R.string.importData),
            destination = ImportData,
        )
    )
    itemsList.add(
        NavigationDrawerItem(
            imageVector = Icons.Filled.Download,
            label = stringResource(id = R.string.event_download_kurse),
            destination = DownloadKurse,
        )
    )

//    itemsList.add(
//        NavigationDrawerItem(
//            imageVector = Icons.Filled.ShowChart,
//            label = stringResource(id = R.string.verlauf),
//            destination = ShowChart
//        )
//    )
    return itemsList
}

data class NavigationDrawerItem(
    val imageVector: ImageVector,
    val label: String,
    val destination: Destination,
)

@Preview
@Composable
fun DrawerPreview() {
    AppTheme(false) {
        Surface {
            DrawerNavigation {}
        }
    }
}
