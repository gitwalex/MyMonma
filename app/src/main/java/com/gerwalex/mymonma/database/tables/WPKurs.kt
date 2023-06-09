package com.gerwalex.mymonma.database.tables

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.database.Cursor
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gerwalex.mymonma.database.ObservableTableRowNew
import com.gerwalex.mymonma.ui.AppTheme
import com.gerwalex.mymonma.ui.content.AmountView
import java.sql.Date

@Entity(
    foreignKeys = [ForeignKey(
        entity = WPStamm::class,
        parentColumns = ["id"],
        childColumns = ["wpid"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE,
        deferred = true
    )]
)
data class WPKurs(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    @ColumnInfo(index = true)
    var btag: Date = Date(System.currentTimeMillis()),
    @ColumnInfo(index = true)
    var wpid: Long = 0,
    var kurs: Long = 0,
    @Ignore
    var wpname: String? = "New",
) : ObservableTableRowNew() {


    @Ignore
    constructor(c: Cursor) : this(id = null) {
        fillContent(c)
        id = getAsLong("id")
        wpid = getAsLong("wpid")
        btag = getAsDate("btag")!!
        kurs = getAsLong("kurs")
        wpname = getAsString("wpname")
    }

}

@Composable
fun WPKursRow(wpkurs: WPKurs, modifier: Modifier = Modifier) {
    Row(modifier) {
        AmountView(value = wpkurs.kurs)
//        Text(text = MyConverter().convertDate(wpkurs.btag))
    }
}

@Preview(name = "Light", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun WPKursRow() {
    val wpkurs = WPKurs(kurs = 12051)
    AppTheme {
        Surface {
            WPKursRow(wpkurs)
        }
    }
}

