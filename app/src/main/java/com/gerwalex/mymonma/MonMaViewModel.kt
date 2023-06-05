package com.gerwalex.mymonma

import android.app.Application
import android.database.Cursor
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gerwalex.mymonma.database.LiveCursor
import com.gerwalex.mymonma.database.room.DB
import com.gerwalex.mymonma.database.room.DB.dao
import com.gerwalex.mymonma.ui.navigation.Destination
import com.gerwalex.mymonma.ui.navigation.Home
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MonMaViewModel(application: Application) : AndroidViewModel(application) {

    val currentScreen = MutableLiveData<Destination>(Home)
    val partnerlist = LiveCursor(DB.get(), "Partnerstamm") {
        dao.getPartnerstammdaten()
    }

    suspend fun getPartnerlist(filter: String): Cursor {
        return withContext(Dispatchers.IO) { dao.getPartnerlist(filter) }

    }
}
