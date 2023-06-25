package com.gerwalex.mymonma.main

import android.app.Application
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.views.WPStammView
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.Home
import kotlinx.coroutines.flow.MutableStateFlow

class DataViewModel(application: Application) : MonMaViewModel(application) {

    val navigateTo = MutableStateFlow<Destination>(Home)
    override val accountlist = dao.getAccountlist()
    override var accountid: Long = 0
    override var cashTrxId: Long = 0
    override var regelmTrxId: Long = 0
    override var reportId: Long? = null
    override var wpstamm: WPStammView? = null

    override val cashTrxList = dao.getCashTrxList(accountid)
    override val account = dao.getAccountData(accountid)

//    val tt: Flow<List<Cat>> = flowOf(listOf(Cat(), Cat()))
}
