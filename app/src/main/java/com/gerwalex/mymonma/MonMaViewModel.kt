package com.gerwalex.mymonma

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.Home
import com.gerwalex.mymonma.ui.screens.CashTrxView
import kotlinx.coroutines.flow.MutableStateFlow

class MonMaViewModel(application: Application) : AndroidViewModel(application) {

    val navigateTo = MutableStateFlow<Destination>(Home)
    val accountlist = dao.getAccountlist()
    val account = MutableLiveData<Cat>()
    val cashTrx = MutableLiveData<CashTrxView>()

}
