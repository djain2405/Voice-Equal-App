package com.example.speakingtimer.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.speakingtimer.R
import com.example.speakingtimer.model.Result
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.new_results_fragment.*
import kotlinx.android.synthetic.main.results_fragment.*
import kotlinx.android.synthetic.main.results_fragment.count_pie_chart
import kotlinx.android.synthetic.main.results_fragment.time_pie_chart


class ResultsFragment : Fragment() {

    private val viewModel: ResultsViewModel by lazy {
        ViewModelProvider(
            this,
            ResultsViewModel.Factory(application = requireActivity().application)
        ).get(ResultsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_results_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        back_button.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.result.observe(viewLifecycleOwner, Observer {
            setDataForChart(it)
            women_results.text =
                "${it.percentWomenCount}% of speakers were women and they spoke ${it.percentWomenTime}% of the total time."
            men_results.text =
                "${it.percentMenCount}% of speakers were men and they spoke ${it.percentMenTime}% of the total time."
        })
    }

    private fun setDataForChart(result: Result) {
        val pieEntryListCount: MutableList<PieEntry> = emptyList<PieEntry>().toMutableList()
        pieEntryListCount.add(PieEntry(result.percentMenCount.toFloat(), "Men"))
        pieEntryListCount.add(PieEntry(result.percentWomenCount.toFloat(), "Women"))
        val colorList: MutableList<Int> = emptyList<Int>().toMutableList()
//        for (c in ColorTemplate.JOYFUL_COLORS) colorList.add(c)
        colorList.add(resources.getColor(R.color.men))
        colorList.add(resources.getColor(R.color.women))
        val pieDataSetCount = PieDataSet(pieEntryListCount, "")
        pieDataSetCount.setDrawIcons(false)
//        pieDataSetCount.sliceSpace = 3f;

        pieDataSetCount.colors = colorList
        val pieDataCount = PieData(pieDataSetCount)
        pieDataCount.setValueTextSize(8f)
        count_pie_chart.data = pieDataCount
        count_pie_chart.setDrawEntryLabels(false)
        count_pie_chart.setUsePercentValues(true)
        count_pie_chart.description.isEnabled = false
        val countLegend = count_pie_chart.legend
        countLegend.direction = Legend.LegendDirection.RIGHT_TO_LEFT

        // For time pie chart
        val pieEntryListTime: MutableList<PieEntry> = emptyList<PieEntry>().toMutableList()
        pieEntryListTime.add(PieEntry(result.percentMenTime.toFloat(), "Men"))
        pieEntryListTime.add(PieEntry(result.percentWomenTime.toFloat(), "Women"))
        val pieDataSetTime = PieDataSet(pieEntryListTime, "")
        pieDataSetTime.setDrawIcons(false)
//        pieDataSetTime.sliceSpace = 3f;

        pieDataSetTime.colors = colorList
        val pieDataTime = PieData(pieDataSetTime)
        pieDataTime.setValueTextSize(8f)
        time_pie_chart.data = pieDataTime
        time_pie_chart.setDrawEntryLabels(false)
        time_pie_chart.setUsePercentValues(true)
        time_pie_chart.description.isEnabled = false
        val timeLegend = time_pie_chart.legend
        timeLegend.direction = Legend.LegendDirection.RIGHT_TO_LEFT


    }

}