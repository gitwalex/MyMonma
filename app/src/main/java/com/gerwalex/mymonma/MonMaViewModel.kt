package com.gerwalex.mymonma

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gerwalex.mymonma.database.LiveCursor
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.Home

class MonMaViewModel(application: Application) : AndroidViewModel(application) {

    val accountlist = LiveCursor(DB.get(), "Account") {
        dao.getAccountlist()
    }
    val currentScreen = MutableLiveData<Destination>(Home)

}
