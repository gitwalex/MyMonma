package com.gerwalex.mymonma.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.gerwalex.mymonma.R

interface Destination {

    val title: Int
    val name: String
    val route: String
    fun navigate(navController: NavController)

}

object Home : Destination {
    override val title: Int = R.string.app_name
    override val name: String = "Home"
    override val route = name

    override fun navigate(navController: NavController) {
        navController.popBackStack(name, false)
    }
}


object Up : Destination {
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name = "Up"
    override val route = name

    override fun navigate(navController: NavController) {
        navController.navigateUp()
    }

}

object ImportData : Destination {
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "ImportData"
    override val route = name

    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object ImportCashTrx : Destination {
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "ImportCashTrx"
    override val route = name
    override fun navigate(navController: NavController) {
        TODO("Not yet implemented")
    }


}

object DownloadKurse : Destination {
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "DownloadKurse"

    override val route = name
    override fun navigate(navController: NavController) {
        TODO("Not yet implemented")
    }

}

object InProgress : Destination {
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name = "InProgress"

    override val route = name
    override fun navigate(navController: NavController) {
        TODO("Not yet implemented")
    }

}

object NotInProgress : Destination {
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name = "NotInProgress"
    override val route = name

    override fun navigate(navController: NavController) {
        TODO("Not yet implemented")
    }

}

object CashTrxList : Destination {
    var id: Long = 0L
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "CashTrxList"
    override val route = "$name/{id}"
    val arguments = listOf(navArgument("id") { type = NavType.LongType })

    override fun navigate(navController: NavController) {
        navController.navigate("$name/$id")
    }

}

object EditCashTrx : Destination {
    var id: Long = 0L
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "EditCashTrx"


    override val route = "$name/{id}"
    val arguments = listOf(navArgument("id") { type = NavType.LongType })

    override fun navigate(navController: NavController) {
        navController.navigate("$name/$id")
    }

}

object AddCashTrx : Destination {
    var id = 0L
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "AddCashTrx"

    override val route = "$name/{id}"
    val arguments = listOf(navArgument("id") { type = NavType.LongType })

    override fun navigate(navController: NavController) {
        navController.navigate("$name/$id")
    }

}

object Settings : Destination {
    override val title: Int = R.string.settings
    override val name: String = "Settings"
    override val route = name
    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object AccountList : Destination {
    override val title: Int = R.string.accountlist
    override val name: String = "AccountList"
    override val route = name
    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object RegelmTrxList : Destination {
    override val title: Int = R.string.trxRegelm
    override val name: String = "RegelmTrxList"


    override val route = name
    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object AddRegelmTrx : Destination {
    var id = 0L
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "AddRegelmTrx"


    override val route = "$name/{id}"
    val arguments = listOf(navArgument("id") { type = NavType.LongType })

    override fun navigate(navController: NavController) {
        navController.navigate("$name/$id")
    }

}

object EditRegelmTrx : Destination {
    var id = 0L
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "EditRegelmTrx"


    override val route = "$name/{id}"
    val arguments = listOf(navArgument("id") { type = NavType.LongType })

    override fun navigate(navController: NavController) {
        navController.navigate("$name/$id")
    }
}

object ReportList : Destination {
    override val title = R.string.myReports
    override val name: String = "ReportList"


    override val route = name
    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object AddReport : Destination {
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "AddReport"


    override val route = name
    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object EditReport : Destination {
    var id = 0L
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "EditReport"


    override val route = "$name/{id}"
    val arguments = listOf(navArgument("id") { type = NavType.LongType })

    override fun navigate(navController: NavController) {
        navController.navigate("$name/$id")
    }
}

object ReportDetailScreen : Destination {
    var id = 0L
    override val title = R.string.myReports
    override val name: String = "ReportDetailScreen"


    override val route = "$name/{reportid}"
    val arguments = listOf(navArgument("reportid") { type = NavType.LongType })


    override fun navigate(navController: NavController) {
        navController.navigate("${name}/$id")
    }

}

object ReportGeldflussDetail : Destination {
    var reportid = 0L
    var catid = 0L
    override val title = R.string.myReports
    override val name: String = "ReportGeldflussDetail"


    override val route = "$name/{reportid}/{catid}"
    val arguments = listOf(
        navArgument("reportid") { type = NavType.LongType },
        navArgument("catid") { type = NavType.LongType },
    )

    override fun navigate(navController: NavController) {
        navController.navigate("$name/$reportid/$catid")
    }

}

object ReportGeldflussVerglDetail : Destination {
    var reportid = 0L
    var catid = 0L
    override val title = R.string.myReports
    override val name: String = "ReportGeldflussVerglDetail"


    override val route = "$name/{reportid}/{catid}"
    val arguments = listOf(
        navArgument("reportid") { type = NavType.LongType },
        navArgument("catid") { type = NavType.LongType },
    )

    override fun navigate(navController: NavController) {
        navController.navigate("$name/$reportid/$catid")
    }

}

object PartnerGeldflussDetails : Destination {
    var reportid = 0L
    var partnerid = 0L
    override val title = R.string.myReports
    override val name: String = "PartnerdatenReportScreen"


    override val route = "$name/{reportid}/{partnerid}"
    val arguments = listOf(
        navArgument("reportid") { type = NavType.LongType },
        navArgument("partnerid") { type = NavType.LongType },
    )

    override fun navigate(navController: NavController) {
        navController.navigate("$name/$reportid/$partnerid")
    }

}

object WPBestandList : Destination {
    override val title = R.string.wpbestandlist
    override val name: String = "WPBestandList"


    override val route = name
    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object Einnahmen : Destination {
    override val title = R.string.dividende
    override val name = "Dividende"

    override val route = name
    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}


