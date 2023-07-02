package com.gerwalex.mymonma.ui.dashboard

import android.view.ViewGroup
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.gerwalex.mymonma.database.room.DB.reportdao
import com.gerwalex.mymonma.database.room.MyConverter
import com.gerwalex.mymonma.ext.rememberState
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

@Composable
fun YearMonthReport(reportid: Long) {
    val data by reportdao.getYearMonthReport(reportid).collectAsState(initial = emptyList())
    var bardata by rememberState { BarData() }
    LaunchedEffect(key1 = data) {
        val entries = ArrayList<BarEntry>()
        data.forEach {
            val x = it.year * 12 + it.month - 1
            val y = -it.amount
            entries.add(BarEntry(x, y))
        }
        val dataSet =
            BarDataSet(entries, "entries: ${entries.size}").apply {
                setDrawValues(true)
                color = android.R.color.holo_blue_dark
                axisDependency = YAxis.AxisDependency.LEFT
                valueFormatter = MyConverter.currencyFormatter
            }
        bardata = BarData(dataSet).apply {
            barWidth = 200f
        }

    }
    YearMonthReport(bardata)
}

@Composable
fun YearMonthReport(
    data: BarData
) {
    val color = android.R.color.holo_blue_dark
    Box(
        modifier = Modifier
            .padding(4.dp)
            .height(200.dp)
            .border(
                1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RectangleShape
            )
    ) {
        AndroidView(update = { chart ->
            chart.data = data
            if (chart.xChartMax - chart.xChartMin > 10) {
                chart.setVisibleXRangeMaximum(10f)
            }
            chart.animateX(500, Easing.EaseInOutQuad)
            chart.moveViewToX(chart.xChartMax)
            chart.invalidate()

        },
            factory = {
                BarChart(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    axisRight.setDrawLabels(false)
                    getAxis(YAxis.AxisDependency.LEFT).apply {
                        valueFormatter = MyConverter.currencyFormatter
                        textColor = color
                    }
                    isScaleYEnabled = false
                    isAutoScaleMinMaxEnabled = true
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val year = (value / 12).toInt()
                            val month = (value % 12).toInt() + 1
                            return "$month/$year"
                        }
                    }
                }
            })
    }
}
