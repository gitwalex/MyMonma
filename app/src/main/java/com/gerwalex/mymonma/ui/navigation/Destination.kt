package com.gerwalex.mymonma.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.navigation.NavController
import com.gerwalex.mymonma.R

sealed class Destination {

    abstract val title: Int
    abstract val name: String
    abstract fun navigate(navController: NavController)
    open val toolbarIcon = Icons.Filled.ArrowBack

}

object Home : Destination() {
    override val title: Int = R.string.app_name
    override val name: String = "Home"

    override fun navigate(navController: NavController) {
        navController.popBackStack(name, false)
    }


}

object Up : Destination() {
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String
        get() = "Up"

    override fun navigate(navController: NavController) {
        navController.navigateUp()
    }

}

object CashTrxList : Destination() {
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "CashTrxList"


    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object EditCashTrx : Destination() {
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "EditCashTrx"


    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

object AddCashTrx : Destination() {
    override val title: Int
        get() = TODO("Not yet implemented")
    override val name: String = "AddCashTrx"


    override fun navigate(navController: NavController) {
        navController.navigate(name)
    }

}

