package com.gerwalex.mymonma.database.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.room.DatabaseView
import com.gerwalex.mymonma.enums.Kontotyp
import com.gerwalex.mymonma.ext.rememberState
import java.sql.Date

@DatabaseView(
    """
        select a.*, b.*, c.name as obercatname
        ,(select total(amount) from CashTrx where accountid = a.id and (transferid is null or isUmbuchung)) as saldo
        from cat a
        join Cat c on(a.obercatid = c.id)
        join Account b using(id)
        where a.supercatid = 1001
        and a.catclassid != 1
            """
)
data class AccountCashView(
    var id: Long = -1,
    var catid: Long = -1,
    var name: String = "",
    var description: String? = null,
    var saldo: Long = 0,
    var obercatid: Long = 0,
    var supercatid: Long = 0,
    var catclassid: Long = 0,
    var incomecat: Boolean? = null,
    var ausgeblendet: Boolean = false,
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
    var obercatname: String = ""
)

@Composable
fun AccountCashSpinner(
    accountid: Long,
    accounts: List<AccountCashView>,
    selected: (AccountCashView) -> Unit
) {
    var myAccount by rememberState { AccountCashView() }
    LaunchedEffect(Unit) {
        accounts.forEach {
            if (it.id == accountid) {
                myAccount = it
            }
        }
    }
    var isExpanded by remember { mutableStateOf(false) }
    Box(contentAlignment = Alignment.Center) {
        Text(
            text = myAccount.name,
            modifier = Modifier.clickable {
                isExpanded = !isExpanded
            })
        DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
            accounts
                .forEach { account ->
                    DropdownMenuItem(
                        text = { Text(text = account.name) },
                        onClick = {
                            myAccount = account
                            selected(account)
                            isExpanded = false
                        })
                }

        }
    }
}


