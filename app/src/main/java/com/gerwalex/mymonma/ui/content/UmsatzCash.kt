package com.gerwalex.mymonma.ui.content

import android.database.Cursor
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun UmsatzCashList(c: Cursor, modifier: Modifier = Modifier) {
    val idIndex = remember { c.getColumnIndex("_id") }
    LazyColumn(modifier) {
        items(count = c.count,
            key = { position ->
                if (c.moveToPosition(position)) c.getLong(idIndex)
            }
        ) { position ->
            if (c.moveToPosition(position)) {
//                UmsatzCash(cashTrans = CashTrxView(c))
            }
        }
    }


}


