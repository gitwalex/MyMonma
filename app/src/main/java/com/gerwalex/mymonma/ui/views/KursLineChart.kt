package com.gerwalex.mymonma.ui.views

import android.content.Context
import android.text.format.DateUtils
import androidx.core.content.ContextCompat
import com.gerwalex.mymonma.database.room.DB.wpdao
import com.gerwalex.mymonma.database.tables.WPStamm
import com.gerwalex.mymonma.enums.WPTyp
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Date
import java.text.DateFormat

class KursLineChart(context: Context, val wpid: Long) : LineChart(context) {
    private var wpstamm: WPStamm? = null
    private val leftYAxisColor: Int
    private var currencyFormatter: ValueFormatter = object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return value.toString()
        }
    }
    private val btagFormatter: DefaultValueFormatter = object : DefaultValueFormatter(10) {
        private val df = DateFormat.getDateInstance()
        override fun getFormattedValue(value: Float): String {
            return df.format(Date(value.toLong() * DateUtils.DAY_IN_MILLIS))
        }
    }
    private var indexFormatter: ValueFormatter = object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return ((value / 100).toLong()).toString()
        }
    }

    init {
        super.init()
        description.text = ""
        axisRight.setDrawLabels(false)
        leftYAxisColor = ContextCompat.getColor(context, android.R.color.holo_blue_dark)
        val leftaxis = getAxis(YAxis.AxisDependency.LEFT)
        leftaxis.textColor = leftYAxisColor
        isAutoScaleMinMaxEnabled = true
        val xaxis = xAxis
        xaxis.labelCount = 3
        xaxis.position = XAxis.XAxisPosition.BOTTOM
        xaxis.valueFormatter = btagFormatter
        load()
//        kursdata.observeForever { entries ->
//            val ds = LineDataSet(entries, wpstammm.wpname_wkn)
//            ds.setDrawCircles(false)
//            ds.setDrawValues(false)
//            ds.axisDependency = YAxis.AxisDependency.LEFT
//            ds.color = leftYAxisColor
//            ds.values = entries
//            data = LineData(ds)
//            if (xChartMax - xChartMin > 365) {
//                setVisibleXRangeMaximum(365f)
//            }
//        }
    }

    private fun load() {
        MainScope().launch {
            var wpstamm: WPStamm?
            val entries: MutableList<Entry> = ArrayList()
            withContext(Dispatchers.IO) {
                wpstamm = wpdao.getWPStamm(wpid)
                wpdao.getWPKurse(wpid).collect { list ->
                    list.forEach { kurs ->
                        val x = kurs.btag
                            .time
                            .toFloat() / DateUtils.DAY_IN_MILLIS
                        val y = kurs.kurs.toFloat()
                        entries.add(Entry(x, y))
                    }

                }
                wpstamm?.let {
                    axisLeft.valueFormatter =
                        if (it.wptyp !== WPTyp.Index) currencyFormatter
                        else indexFormatter

                    val ds = LineDataSet(entries, it.wpkenn)
                    ds.setDrawCircles(false)
                    ds.setDrawValues(false)
                    ds.axisDependency = YAxis.AxisDependency.LEFT
                    ds.color = leftYAxisColor
                    ds.values = entries
                    data = LineData(ds)
                }
                if (xChartMax - xChartMin > 365) {
                    setVisibleXRangeMaximum(365f)
                }
            }
        }
        animateX(500, Easing.EaseInOutQuad)
        moveViewToX(xChartMax)
    }
}