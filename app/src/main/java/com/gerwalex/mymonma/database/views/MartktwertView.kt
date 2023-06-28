package com.gerwalex.mymonma.database.views

import androidx.room.DatabaseView
import com.gerwalex.mymonma.database.room.MyConverter.NACHKOMMA

@DatabaseView(
    """
    select accountid, wpid,
 sum(menge) / $NACHKOMMA * 
(SELECT kurs from WPKurs d where a.wpid = d.wpid group by wpid having max(btag)) as marktwert   
 from WPTrx a   
 where paketid is null   		
 group by accountid, wpid
"""
)
/**
 * Ermittelt den aktuellen Marktwert des Bestandes je wp und depot
 */
data class MartktwertView(
    val accountid: Long,
    val wpid: Long,
    val marktwert: Long
)