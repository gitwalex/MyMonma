package com.gerwalex.mymonma.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.gerwalex.mymonma.database.tables.Cat
import com.gerwalex.mymonma.database.views.CashTrxView
import com.gerwalex.mymonma.database.views.WPStammView
import kotlinx.coroutines.flow.Flow

abstract class MonMaViewModel(application: Application) : AndroidViewModel(application) {

    abstract val accountlist: Flow<List<Cat>>
    abstract var accountid: Long
    abstract var cashTrxId: Long
    abstract var regelmTrxId: Long
    abstract var reportId: Long?
    abstract var wpstamm: WPStammView?

    abstract val cashTrxList: Flow<List<CashTrxView>>
    abstract val account: Flow<Cat>
}
