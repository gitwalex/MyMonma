package com.gerwalex.mymonma.ui.views

import android.text.format.DateUtils
import android.util.Log
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.gerwalex.mymonma.database.room.DB.wpdao
import com.gerwalex.mymonma.database.room.MyConverter
import com.gerwalex.mymonma.database.views.WPStammView
import com.gerwalex.mymonma.enums.WPTyp
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun KursLineChart(wpStamm: WPStammView) {
    val kurse by wpdao.getWPKurse(wpStamm.id!!).collectAsState(initial = emptyList())
    val context = LocalContext.current



    Box {
        AndroidView(
            update = { view ->
                val entries = ArrayList<Entry>().apply {
                    kurse.forEach { kurs ->
                        val x = kurs.btag
                            .time
                            .toFloat() / DateUtils.DAY_IN_MILLIS
                        val y = kurs.kurs.toFloat()
                        add(Entry(x, y))
                    }
                }
                if (entries.size > 0) {

                    Log.d("KursLineChart", "update ${wpStamm.name}, entries:${entries.size}")
                    val set = LineDataSet(entries, wpStamm.wpkenn).apply {
                        setDrawCircles(false)
                        setDrawValues(false)
                        axisDependency = YAxis.AxisDependency.LEFT
                        val leftYAxisColor =
                            ContextCompat.getColor(context, android.R.color.holo_blue_dark)
                        color = leftYAxisColor
                        values = entries
                    }
                    view.apply {
                        data = LineData(set)
                        if (xChartMax - xChartMin > 365) {
                            setVisibleXRangeMaximum(365f)
                        }
                        animateX(500, Easing.EaseInOutQuad)
                        resetViewPortOffsets()
                        moveViewToX(xChartMax)
                        invalidate()
                    }
                }
        },

            factory = {
                LineChart(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    description.text = ""
                    axisRight.setDrawLabels(false)
                    val leftYAxisColor = ContextCompat.getColor(
                        context,
                        android.R.color.holo_blue_dark
                    )
                    val leftaxis = getAxis(YAxis.AxisDependency.LEFT)
                    leftaxis.textColor = leftYAxisColor
                    isAutoScaleMinMaxEnabled = true
                    val xaxis = xAxis
                    xaxis.labelCount = 3
                    xaxis.position = XAxis.XAxisPosition.BOTTOM
                    xaxis.valueFormatter = MyConverter.btagFormatter
                    axisLeft.valueFormatter =
                        if (wpStamm.wptyp !== WPTyp.Index) MyConverter.currencyFormatter
                        else MyConverter.indexFormatter

                }
            })
    }
}

