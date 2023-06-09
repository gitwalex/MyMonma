package com.gerwalex.mymonma.database.tables

import android.database.Cursor
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.R
import com.gerwalex.mymonma.database.ObservableTableRowNew
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.enums.Kontotyp
import com.gerwalex.mymonma.ui.content.AutoCompleteTextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Date

@Entity(
    foreignKeys = [ForeignKey(
        entity = CatClass::class,
        parentColumns = ["id"],
        childColumns = ["catid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,
        deferred = true
    )]
)
data class Account(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    @ColumnInfo(index = true)
    var catid: Long = 0,
    var inhaber: String? = null,
    var currency: String? = null,
    var iban: String? = null,// IBAN, Kartnenummer, Kontonummer
    var blz: String? = null,
    var bezeichnung: String? = null, //Verwendungszweck

    var creditlimit: Long? = 0,
    var verrechnungskonto: Long? = null,
    var kontotyp: Kontotyp = Kontotyp.Giro,
    var openDate: Date? = Date(System.currentTimeMillis()),
    var bankname: String? = null,
    var bic: String? = null,
    var openamount: Long = 0,
) : ObservableTableRowNew() {


    @Ignore
    constructor(c: Cursor) : this(id = null) {
        fillContent(c)
        id = getAsLong("id")
        catid = getAsLong("catid")
        inhaber = getAsString("inhaber")
        currency = getAsString("currency")
        iban = getAsString("iban")
        bic = getAsString("bic")
        blz = getAsString("blz")
        bezeichnung = getAsString("subnumber")
        creditlimit = getAsLongOrNull("creditlimit")
        verrechnungskonto = getAsLongOrNull("verrechnungskonto")
        openDate = getAsDate("openDate")
        openamount = getAsLong("openamount")
        bankname = getAsString("bankname")
    }
}

@Composable
fun AutoCompleteAccountView(filter: String, selected: (Cat) -> Unit) {
    var account by remember { mutableStateOf(filter) }
    val cursor = MutableLiveData<Cursor>()
    val data by cursor.observeAsState()
    var count by remember { mutableStateOf(0) }
    LaunchedEffect(account) {
        withContext(Dispatchers.IO) {
            val c = DB.dao.getAccountlist(account)
            if (c.moveToFirst()) {
                selected(Cat(c))
            }
            count = c.count
            cursor.postValue(c)
        }
    }


    AutoCompleteTextView(
        modifier = Modifier.fillMaxWidth(),
        query = account,
        queryLabel = stringResource(id = R.string.categorie),
        onQueryChanged = { account = it },
        count = count,
        onClearClick = { account = "" },
        onDoneActionClick = { },
        onItemClick = { position ->
            data?.let { c ->
                if (c.moveToPosition(position)) {
                    val cat = Cat(c)
                    account = cat.name
                    count = 0
                    selected(cat)
                }
            }
        },
    ) { position ->
        data?.let { c ->
            if (c.moveToPosition(position)) {
                val cat = Cat(c)
                Text(cat.name, fontSize = 14.sp)
            }
        }
    }
}
