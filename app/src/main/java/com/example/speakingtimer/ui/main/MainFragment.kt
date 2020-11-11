package com.example.speakingtimer.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.SystemClock
import android.text.format.DateFormat.format
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.speakingtimer.R
import kotlinx.android.synthetic.main.main_fragment.*
import java.lang.String.format
import java.text.DateFormat
import java.text.SimpleDateFormat
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    @ExperimentalTime
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this, MainViewModel.Factory(requireActivity().application)).get(
                MainViewModel::class.java
            )
        val womenCount = viewModel.readWomenCount()
        if (womenCount > 0) {
            edit_women_count.setText(womenCount.toString())
        }

        val menCount = viewModel.readMenCount()
        if (menCount > 0) {
            edit_men_count.setText(menCount.toString())
        }

        save_count.setOnClickListener {
            viewModel.saveCount(
                edit_women_count?.text.toString().toInt(),
                edit_men_count?.text.toString().toInt()
            )
        }

        women_timer_button.setOnClickListener {
            calculateSpokenTime(true)
        }


        men_timer_button.setOnClickListener {
            calculateSpokenTime(false)
        }

        show_results_button.setOnClickListener {
            val menTime = viewModel.readMenTime()
            val womenTime = viewModel.readWomenTime()
            Toast.makeText(
                this.requireContext(),
                "Men: $menTime, Women: $womenTime",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    private fun calculateSpokenTime(isWomen: Boolean) {
        // button clicked was for the women timer
        if (isWomen) {
            if (men_timer_button.text == resources.getString(R.string.stop)) {
                val elapsedTime = SystemClock.elapsedRealtime() - men_timer.base
                viewModel.setTimeForMen(elapsedTime)
                men_timer_button.text = resources.getString(R.string.start)
                men_timer.stop()

            }
            if (women_timer_button.text == resources.getString(R.string.start)) {
                women_timer_button.text = resources.getString(R.string.stop)
                women_timer.base = SystemClock.elapsedRealtime();
                women_timer.start()
            } else {
                val elapsedTime = SystemClock.elapsedRealtime() - women_timer.base
                viewModel.setTimeForWomen(elapsedTime)
                women_timer_button.text = resources.getString(R.string.start)
                women_timer.stop()
            }
        }
        // button clicked was for men timer
        else {
            if (women_timer_button.text == resources.getString(R.string.stop)) {
                val elapsedTime = SystemClock.elapsedRealtime() - women_timer.base
                viewModel.setTimeForWomen(elapsedTime)
                women_timer_button.text = resources.getString(R.string.start)
                women_timer.stop()

            }
            if (men_timer_button.text == resources.getString(R.string.start)) {
                men_timer_button.text = resources.getString(R.string.stop)
                men_timer.base = SystemClock.elapsedRealtime();
                men_timer.start()
            } else {
                val elapsedTime = SystemClock.elapsedRealtime() - men_timer.base
                viewModel.setTimeForMen(elapsedTime)
                men_timer_button.text = resources.getString(R.string.start)
                men_timer.stop()
            }
        }

    }

}