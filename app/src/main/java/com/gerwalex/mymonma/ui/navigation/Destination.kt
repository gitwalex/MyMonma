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

object HomeDest : Destination {
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

object ImportDataDest : Destination {
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "ImportData"
    override val route = name

    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object ImportCashTrxDest : Destination {
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "ImportCashTrx"
    override val route = name
    override fun navigate(navController: NavController) {
        TODO("Not yet implemented")
    }


}

object DownloadKurseDest : Destination {
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

object CashTrxListDest : Destination {
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

object EditCashTrxDest : Destination {
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

object AddCashTrxDest : Destination {
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

object SettingsDest : Destination {
    override val title: Int = R.string.settings
    override val name: String = "Settings"
    override val route = name
    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object RestoreDatabaseDest : Destination {
    override val title: Int = R.string.settings
    override val name: String = "RestoreDatabase"
    override val route = name
    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}


object AccountListDest : Destination {
    override val title: Int = R.string.accountlist
    override val name: String = "AccountList"
    override val route = name
    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object RegelmTrxListDest : Destination {
    override val title: Int = R.string.trxRegelm
    override val name: String = "RegelmTrxList"


    override val route = name
    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object AddRegelmTrxDest : Destination {
    var id = 0L
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "AddRegelmTrx"


    override val route = name

    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object EditRegelmTrxDest : Destination {
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

object ReportListDest : Destination {
    override val title = R.string.myReports
    override val name: String = "ReportList"


    override val route = name
    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object AddReportDest : Destination {
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "AddReport"


    override val route = name
    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object EditReportDest : Destination {
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

object GeldflussDetailScreenDest : Destination {
    var reportid = 0L
    override val title = R.string.myReports
    override val name: String = "GeldflussDetailScreen"


    override val route = "$name/{reportid}"
    val arguments = listOf(navArgument("reportid") { type = NavType.LongType })


    override fun navigate(navController: NavController) {
        navController.navigate("${name}/$reportid")
    }

}

object PartnerDetailScreenDest : Destination {
    var reportid = 0L
    override val title = R.string.myReports
    override val name: String = "PartnerDetailScreen"


    override val route = "$name/{reportid}"
    val arguments = listOf(navArgument("reportid") { type = NavType.LongType })


    override fun navigate(navController: NavController) {
        navController.navigate("${name}/$reportid")
    }

}

object ReportGeldflussDetailDest : Destination {
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

object ReportGeldflussVerglDetailDest : Destination {
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

object PartnerGeldflussDetailsDest : Destination {
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

object WPBestandListDest : Destination {
    override val title = R.string.wpbestandlist
    override val name: String = "WPBestandList"


    override val route = name
    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object WPPaketListDest : Destination {
    var wpid: Long = 0
    override val title = R.string.dividende
    override val name = "WPPaketList"

    override val route = "$name/{wpid}"
    val arguments = listOf(
        navArgument("wpid") { type = NavType.LongType },
    )

    override fun navigate(navController: NavController) {
        navController.navigate("$name/${wpid}")
    }

}

object WPKaufDest : Destination {
    var wpid: Long = 0
    override val title = R.string.dividende
    override val name = "WPKauf"

    override val route = "$name/{wpid}"
    val arguments = listOf(
        navArgument("wpid") { type = NavType.LongType },
    )

    override fun navigate(navController: NavController) {
        navController.navigate("$name/${wpid}")
    }

}

object EinnahmenDest : Destination {
    var wpid: Long = 0
    override val title = R.string.dividende
    override val name = "Dividende"

    override val route = "$name/{wpid}"
    val arguments = listOf(
        navArgument("wpid") { type = NavType.LongType },
    )

    override fun navigate(navController: NavController) {
        navController.navigate("$name/${wpid}")
    }

}


