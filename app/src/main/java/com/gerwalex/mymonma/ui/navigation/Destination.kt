package com.gerwalex.mymonma.ui.navigation

import androidx.navigation.NavController
import com.gerwalex.mymonma.R

interface Destination {

    val title: Int
    val name: String
    fun navigate(navController: NavController)

}

object Home : Destination {
    override val title: Int = R.string.app_name
    override val name: String = "Home"

    override fun navigate(navController: NavController) {
        navController.popBackStack(name, false)
    }
}


object Up : Destination {
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String
        get() = "Up"

    override fun navigate(navController: NavController) {
        navController.navigateUp()
    }

}

object ImportData : Destination {
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "ImportData"

    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object ImportCashTrx : Destination {
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "ImportCashTrx"
    override fun navigate(navController: NavController) {
        TODO("Not yet implemented")
    }


}

object DownloadKurse : Destination {
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "DownloadKurse"

    override fun navigate(navController: NavController) {
        TODO("Not yet implemented")
    }

}

object InProgress : Destination {
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String
        get() = "InProgress"

    override fun navigate(navController: NavController) {
        TODO("Not yet implemented")
    }

}

object NotInProgress : Destination {
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String
        get() = "NotInProgress"

    override fun navigate(navController: NavController) {
        TODO("Not yet implemented")
    }

}

object CashTrxList : Destination {
    var id: Long = 0L
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "CashTrxList"


    override fun navigate(navController: NavController) {
        navController.navigate("$name/$id")
    }

}

object EditCashTrx : Destination {
    var id: Long = 0L
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "EditCashTrx"


    override fun navigate(navController: NavController) {
        navController.navigate("$name/$id")
    }

}

object AddCashTrx : Destination {
    var id = 0L
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "AddCashTrx"


    override fun navigate(navController: NavController) {
        navController.navigate("$name/$id")
    }

}

object Settings : Destination {
    override val title: Int = R.string.settings
    override val name: String = "Settings"
    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object AccountList : Destination {
    override val title: Int = R.string.accountlist
    override val name: String = "AccountList"
    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object RegelmTrxList : Destination {
    override val title: Int = R.string.trxRegelm
    override val name: String = "RegelmTrxList"


    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object AddRegelmTrx : Destination {
    var id = 0L
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "AddRegelmTrx"


    override fun navigate(navController: NavController) {
        navController.navigate("$name/$id")
    }

}

object EditRegelmTrx : Destination {
    var id = 0L
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "EditRegelmTrx"


    override fun navigate(navController: NavController) {
        navController.navigate("$name/$id")
    }
}

object ReportList : Destination {
    override val title = R.string.myReports
    override val name: String = "ReportList"


    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object AddReport : Destination {
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "AddReport"


    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object EditReport : Destination {
    var id = 0L
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "EditReport"


    override fun navigate(navController: NavController) {
        navController.navigate("$name/$id")
    }
}

object ReportDetailScreen : Destination {
    var id = 0L
    override val title = R.string.myReports
    override val name: String = "ReportDetailScreen"


    override fun navigate(navController: NavController) {
        navController.navigate("${name}/$id")
    }

}

object ReportGeldflussDetail : Destination {
    var reportid = 0L
    var catid = 0L
    override val title = R.string.myReports
    override val name: String = "ReportGeldflussDetail"


    override fun navigate(navController: NavController) {
        navController.navigate("$name/$reportid/$catid")
    }

}

object ReportGeldflussVerglDetail : Destination {
    var reportid = 0L
    var catid = 0L
    override val title = R.string.myReports
    override val name: String = "ReportGeldflussVerglDetail"


    override fun navigate(navController: NavController) {
        navController.navigate("$name/$reportid/$catid")
    }

}

object WPBestandList : Destination {
    override val title = R.string.wpbestandlist
    override val name: String = "WPBestandList"


    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object Einnahmen : Destination {
    override val title = R.string.dividende
    override val name = "Dividende"

    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}


