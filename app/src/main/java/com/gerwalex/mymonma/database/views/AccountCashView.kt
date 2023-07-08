package com.gerwalex.mymonma.database.views

import androidx.room.DatabaseView
import com.gerwalex.mymonma.enums.Kontotyp
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

