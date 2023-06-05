package com.gerwalex.monmang.ui.content

import android.database.Cursor
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.gerwalex.mymonma.MonMaViewModel
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.tables.Partnerstamm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutocompletePartner(
    viewModel: MonMaViewModel,
    onSelected: (Partnerstamm) -> Unit
) {
    val cursor by viewModel.partnerlist.observeAsState()
    cursor?.let {
        AutocompletePartner(it) {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutocompletePartner(
    c: Cursor,
    onSelected: (Partnerstamm) -> Unit
) {

    val scope = rememberCoroutineScope()
    var selectedOption by remember { mutableStateOf("") }
    var cursor by remember { mutableStateOf(c) }
    var exp by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = exp, onExpandedChange = { exp = !exp }) {
        TextField(
            modifier = Modifier.menuAnchor(),
            value = selectedOption,
            onValueChange = {
                scope.launch {
                    withContext(Dispatchers.IO) {
                        cursor = dao.getPartnerlist(it)
                    }
                }
                exp = true
            },
            label = { Text("Label") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = exp)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        if (cursor.count > 0) {
            ExposedDropdownMenu(expanded = exp, onDismissRequest = { exp = false }) {
                if (c.moveToFirst()) {
                    do {
                        val stamm = Partnerstamm(c)
                        val label = stamm.name
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                selectedOption = label
                                exp = false
                            }
                        )
                    } while (c.moveToNext())
                }
            }
        }
    }
}