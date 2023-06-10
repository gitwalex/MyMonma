package com.gerwalex.mymonma

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.Home
import kotlinx.coroutines.flow.MutableStateFlow

class MonMaViewModel(application: Application) : AndroidViewModel(application) {

    val navigateTo = MutableStateFlow<Destination>(Home)
    val accountlist = dao.getAccountlist()
    var account: Cat? = null
    var cashTrxId: Long? = null

}
