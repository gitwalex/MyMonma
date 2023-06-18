package com.gerwalex.mymonma.ui.screens

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.database.room.FileUtils
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.Home
import com.gerwalex.mymonma.ui.navigation.InProgress
import com.gerwalex.mymonma.ui.navigation.NotInProgress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream

@Composable
fun ImportScreen(navigateTo: (Destination) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val statusList = remember { mutableStateListOf<String>() }
    var finished by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            navigateTo(InProgress)
            finished = false
            scope.launch {
                finished = executeImport(context) { status ->
                    statusList.add(status)
                }
            }
            navigateTo(NotInProgress)
        }) {
            Text(text = "Import")
        }
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(statusList) { item ->
                Text(text = item)

            }
        }
        Button(onClick = { navigateTo(Home) }, enabled = finished) {
            Text(text = "zurück")
        }
    }
}

private suspend fun executeImport(context: Context, status: (String) -> Unit): Boolean {
    return withContext(Dispatchers.IO) {
        val db = DB.get().openHelper.writableDatabase
        try {
            db.beginTransaction()
            status("Lösche Daten:")
            db.execSQL("Delete from wptrx")
            db.execSQL("Delete from wpkurs")
            db.execSQL("Delete from cashtrx")
            db.execSQL("Delete from trxregelm")
            db.execSQL("Delete from wpstamm where id > 1")
            db.execSQL("Delete from importaccount")
            db.execSQL("Delete from account")
            db.execSQL("Delete from partnerstamm where id > 1")
            db.execSQL("Delete from cat where catclassid = 2")
            status("Lade csv-daten")
            var `in`: InputStream
            val am = context.resources.assets
            status("Lade Partner")
            `in` = am.open("partnerstamm.csv")
            FileUtils.loadCSVFile(`in`, db, "partnerstamm")
            status("Lade Kategorien")
            `in` = am.open("cat.csv")
            FileUtils.loadCSVFile(`in`, db, "cat")
            status("Lade WPStamm")
            `in` = am.open("wpstamm.csv")
            FileUtils.loadCSVFile(`in`, db, "WPStamm")
            status("Lade Accounts")
            `in` = am.open("account.csv")
            FileUtils.loadCSVFile(`in`, db, "account")
            status("Lade ImportAccounts")
            `in` = am.open("imprtaccount.csv")
            FileUtils.loadCSVFile(`in`, db, "account")
            status("Lade WPKurse")
            `in` = am.open("wpkurs.csv")
            FileUtils.loadCSVFile(`in`, db, "WPKurs")
            status("Lade CashTrx")
            `in` = am.open("cashtrx.csv")
            FileUtils.loadCSVFile(`in`, db, "CashTrx")
            status("Lade Daueraufträge")
            `in` = am.open("trxregelm.csv")
            FileUtils.loadCSVFile(`in`, db, "TrxRegelm")
            status("Lade WPTrx")
            `in` = am.open("wptrx.csv")
            FileUtils.loadCSVFile(`in`, db, "WPTrx")
            status("Nacharbeiten")
            executeImportStatements(db)
            db.setTransactionSuccessful()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            status("Exeception: $e")
            false
        } finally {
            db.endTransaction()
            status("Finished!")
        }
    }
}

private fun executeImportStatements(db: SupportSQLiteDatabase) {
    // Entfernen brackets bei catname
    db.execSQL("UPDATE Cat SET name=REPLACE(name,'[', '')")
    db.execSQL("UPDATE Cat SET name=REPLACE(name,']', '')")
    // Einfügen gegenbuchungen
    db.execSQL(
        "insert into CashTrx (btag, partnerid,accountid,  catid, " +
                "amount, memo, transferid, isUmbuchung)" +
                "select btag, partnerid,catid, accountid, -amount, memo, id, 1 " +
                "from CashTrx where catid between 1 and 99 "
    )

}


@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ImportScreenPreview() {
    AppTheme {
        Surface {
            ImportScreen(navigateTo = {})
        }
    }


}