package com.example.kotlintodopractice.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.example.kotlintodopractice.R
import com.example.kotlintodopractice.utils.model.ToDoData
import java.text.SimpleDateFormat
import java.util.*

class GraphDialogFragment(private val tasks: List<ToDoData>) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_graph)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val lineChart: LineChart = dialog.findViewById(R.id.lineChart)

        setupLineChart(lineChart)
        populateLineChart(lineChart)

        return dialog
    }

    private fun setupLineChart(lineChart: LineChart) {
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.axisRight.isEnabled = false
        lineChart.description.isEnabled = false
    }

    private fun populateLineChart(lineChart: LineChart) {
        val entries = mutableListOf<Entry>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//        for (task in tasks) {
//            val date = dateFormat.parse(task.taskDate)
//            date?.let {
//                val xValue = it.time.toFloat()
//                val yValue = task.minValue.toFloat() // or task.maxValue.toFloat()
//                entries.add(Entry(xValue, yValue))
//            }
//        }

        val dataSet = LineDataSet(entries, "Task Values")
        dataSet.color = resources.getColor(R.color.purple_500, null)
        dataSet.valueTextColor = resources.getColor(R.color.teal_200, null)
        val lineData = LineData(dataSet)
        lineChart.data = lineData

        lineChart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return dateFormat.format(Date(value.toLong()))
            }
        }
        lineChart.invalidate()
    }
}
