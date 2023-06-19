package com.gerwalex.mymonma.wptrx

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gerwalex.mymonma.database.room.DB
import com.skydoves.orchestra.spinner.Spinner
import com.skydoves.orchestra.spinner.SpinnerProperties

data class AccountBestand(

    var id: Long,
    var name: String,
    var bestand: Long = 0L,
    var verrechnungskonto: Long? = 0,
)

@Composable
fun AccountMitBestandSpinner(wpid: Long, onItemSelected: (AccountBestand) -> Unit) {
    var accountbestand = remember {
        mutableStateListOf<AccountBestand>()
    }
    LaunchedEffect(wpid) {
        DB.wpdao.getAccountBestand(wpid)
            .collect {
                if (it.isNotEmpty()) {
                    accountbestand.addAll(it)
                    onItemSelected(it[0])
                }
            }

    }
    val texte = ArrayList<String>().apply {
        accountbestand.forEach {
            add(it.name)
        }
    }
    if (accountbestand.isNotEmpty()) {
        val (selectedItem, setSelectedItem)
                = remember { mutableStateOf(accountbestand[0]) }
        Spinner(
            text = selectedItem.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
//            .align(Alignment.CenterHorizontally)
                .background(MaterialTheme.colorScheme.primaryContainer),
            itemList = texte,
            style = MaterialTheme.typography.bodyMedium,
            properties = SpinnerProperties(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center,
                showDivider = true,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                spinnerPadding = 16.dp,
                spinnerBackgroundColor = MaterialTheme.colorScheme.background,
            ),
            onSpinnerItemSelected = { index, item ->
                setSelectedItem(accountbestand[index])
                onItemSelected(accountbestand[index])
            }
        )
    }
}
