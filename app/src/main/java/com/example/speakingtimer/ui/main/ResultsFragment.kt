package com.example.speakingtimer.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.speakingtimer.R
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.results_fragment.*

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
        return inflater.inflate(R.layout.results_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataForChart()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.result.observe(viewLifecycleOwner, Observer {
            women_results_text.text =
                "${it.percentWomenCount}% of the speakers were women and they spoke ${it.percentWomenTime}% of the time."
            men_results_text.text =
                "${it.percentMenCount}% of the speakers were men and they spoke ${it.percentMenTime}% of the time."
        })
    }

    private fun setDataForChart() {
        val pieEntryList: MutableList<PieEntry> = emptyList<PieEntry>().toMutableList()
        pieEntryList.add(PieEntry(40F, "Value 1"))
        pieEntryList.add(PieEntry(25F, "Value 2"))
        pieEntryList.add(PieEntry(15F, "Value 3"))
        pieEntryList.add(PieEntry(20F, "Value 4"))
        val colorList: MutableList<Int> = emptyList<Int>().toMutableList()
        for (c in ColorTemplate.JOYFUL_COLORS) colorList.add(c)
        val pieDataSet = PieDataSet(pieEntryList, "Test Sample Data")
        pieDataSet.setDrawIcons(false)
        pieDataSet.sliceSpace = 3f;
        pieDataSet.colors = colorList
        val pieData = PieData(pieDataSet)
        count_pie_chart.data = pieData
    }

}