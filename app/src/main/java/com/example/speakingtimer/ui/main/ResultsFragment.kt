package com.example.speakingtimer.ui.main

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.speakingtimer.R
import com.example.speakingtimer.model.Result
import com.example.speakingtimer.util.SharedPreferencesUtil
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.results_fragment.*
import kotlinx.android.synthetic.main.results_fragment.count_pie_chart
import kotlinx.android.synthetic.main.results_fragment.time_pie_chart


class ResultsFragment : Fragment() {

    private val sharedPreferencesUtil: SharedPreferencesUtil by lazy {
        SharedPreferencesUtil(requireActivity().application)
    }

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
        return inflater.inflate(R.layout.results_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        back_button.setOnClickListener {
            findNavController().navigateUp()
        }

        restart_button.setOnClickListener {
            sharedPreferencesUtil.clearSharedPreferences()
            findNavController().navigateUp()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.result.observe(viewLifecycleOwner, Observer {
            setDataForChart(it)
            val spannableStringWomen = SpannableString("${it.percentWomenCount}% of speakers were women and they spoke ${it.percentWomenTime}% of the total time.")
            spannableStringWomen.setSpan(ForegroundColorSpan(resources.getColor(R.color.women)), 0, 3, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            spannableStringWomen.setSpan(ForegroundColorSpan(resources.getColor(R.color.women)), 42, 45, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            women_results.text = spannableStringWomen
            val spannableStringMen = SpannableString("${it.percentMenCount}% of speakers were men and they spoke ${it.percentMenTime}% of the total time.")
            spannableStringMen.setSpan(ForegroundColorSpan(resources.getColor(R.color.men)), 0, 3, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            spannableStringMen.setSpan(ForegroundColorSpan(resources.getColor(R.color.men)), 40, 43, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            men_results.text = spannableStringMen
        })
    }

    private fun setDataForChart(result: Result) {
        val pieEntryListCount: MutableList<PieEntry> = emptyList<PieEntry>().toMutableList()
        pieEntryListCount.add(PieEntry(result.percentMenCount.toFloat(), "Men"))
        pieEntryListCount.add(PieEntry(result.percentWomenCount.toFloat(), "Women"))
        val colorList: MutableList<Int> = emptyList<Int>().toMutableList()
        colorList.add(resources.getColor(R.color.men))
        colorList.add(resources.getColor(R.color.women))
        val pieDataSetCount = PieDataSet(pieEntryListCount, "")
        pieDataSetCount.setDrawIcons(false)

        pieDataSetCount.colors = colorList
        val pieDataCount = PieData(pieDataSetCount)
        pieDataCount.setValueTextSize(6f)
        pieDataCount.setValueTextColor(resources.getColor(R.color.white))
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

        pieDataSetTime.colors = colorList
        val pieDataTime = PieData(pieDataSetTime)
        pieDataTime.setValueTextSize(6f)
        pieDataTime.setValueTextColor(resources.getColor(R.color.white))
        time_pie_chart.data = pieDataTime
        time_pie_chart.setDrawEntryLabels(false)
        time_pie_chart.setUsePercentValues(true)
        time_pie_chart.description.isEnabled = false
        val timeLegend = time_pie_chart.legend
        timeLegend.direction = Legend.LegendDirection.RIGHT_TO_LEFT


    }

}