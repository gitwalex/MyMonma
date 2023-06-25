package com.gerwalex.mymonma.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.database.views.WPStammView
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.Home
import kotlinx.coroutines.flow.MutableStateFlow

class MonMaViewModel(application: Application) : AndroidViewModel(application) {

     val navigateTo = MutableStateFlow<Destination>(Home)
     val accountlist = DB.dao.getAccountlist()
     var accountid: Long = 0
     var cashTrxId: Long = 0
     var regelmTrxId: Long = 0
     var reportId: Long? = null
     var wpstamm: WPStammView? = null

     val cashTrxList = DB.dao.getCashTrxList(accountid)
     val account = DB.dao.getAccountData(accountid)
}
