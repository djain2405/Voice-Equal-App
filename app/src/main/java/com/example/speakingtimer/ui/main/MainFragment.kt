package com.example.speakingtimer.ui.main

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.SystemClock
import android.text.format.DateFormat.format
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.speakingtimer.R
import com.example.speakingtimer.util.SharedPreferencesUtil
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

    private val sharedPreferencesUtil: SharedPreferencesUtil by lazy {
        SharedPreferencesUtil(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        save_count.setOnClickListener {
            dismissKeyboard()
            sharedPreferencesUtil.saveCount(
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
            this.findNavController().navigate(R.id.action_mainFragment_to_resultsFragment)
        }

        reset_results.setOnClickListener {
            edit_men_count.text.clear()
            edit_women_count.text.clear()
            sharedPreferencesUtil.clearSharedPreferences()
        }
    }

    private fun dismissKeyboard() {
        val inputMethodManager: InputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(edit_men_count.windowToken, 0)
        inputMethodManager.hideSoftInputFromWindow(edit_women_count.windowToken, 0)

    }

    @ExperimentalTime
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val womenCount = sharedPreferencesUtil.readWomenCount()
        if (womenCount > 0) {
            edit_women_count.setText(womenCount.toString())
        }

        val menCount = sharedPreferencesUtil.readMenCount()
        if (menCount > 0) {
            edit_men_count.setText(menCount.toString())
        }

    }

    private fun calculateSpokenTime(isWomen: Boolean) {
        // button clicked was for the women timer
        if (isWomen) {
            if (men_timer_button.text == resources.getString(R.string.stop)) {
                val elapsedTime = SystemClock.elapsedRealtime() - men_timer.base
                sharedPreferencesUtil.setTimeForMen(elapsedTime)
                men_timer_button.text = resources.getString(R.string.start)
                men_timer.stop()

            }
            if (women_timer_button.text == resources.getString(R.string.start)) {
                women_timer_button.text = resources.getString(R.string.stop)
                women_timer.base = SystemClock.elapsedRealtime();
                women_timer.start()
            } else {
                val elapsedTime = SystemClock.elapsedRealtime() - women_timer.base
                sharedPreferencesUtil.setTimeForWomen(elapsedTime)
                women_timer_button.text = resources.getString(R.string.start)
                women_timer.stop()
            }
        }
        // button clicked was for men timer
        else {
            if (women_timer_button.text == resources.getString(R.string.stop)) {
                val elapsedTime = SystemClock.elapsedRealtime() - women_timer.base
                sharedPreferencesUtil.setTimeForWomen(elapsedTime)
                women_timer_button.text = resources.getString(R.string.start)
                women_timer.stop()

            }
            if (men_timer_button.text == resources.getString(R.string.start)) {
                men_timer_button.text = resources.getString(R.string.stop)
                men_timer.base = SystemClock.elapsedRealtime();
                men_timer.start()
            } else {
                val elapsedTime = SystemClock.elapsedRealtime() - men_timer.base
                sharedPreferencesUtil.setTimeForMen(elapsedTime)
                men_timer_button.text = resources.getString(R.string.start)
                men_timer.stop()
            }
        }

    }

}