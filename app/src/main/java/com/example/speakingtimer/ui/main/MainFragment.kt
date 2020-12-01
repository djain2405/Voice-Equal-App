package com.example.speakingtimer.ui.main

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.speakingtimer.R
import com.example.speakingtimer.util.SharedPreferencesUtil
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.save_count
import kotlinx.android.synthetic.main.timer_fragment.*
import java.lang.String.format
import java.text.DateFormat
import java.text.SimpleDateFormat
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

enum class TimerState {
    PLAY, PAUSE
}

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
        return inflater.inflate(R.layout.timer_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        save_count.setOnClickListener {
            dismissKeyboard()
            val womenCount =
                if (edit_women_count?.text.toString()?.length > 0) edit_women_count?.text.toString()
                    .toInt() else 0
            val menCount =
                if (edit_men_count?.text.toString()?.length > 0) edit_men_count?.text.toString()
                    .toInt() else 0
            val isSaved = sharedPreferencesUtil.saveCount(
                womenCount,
                menCount
            )
            if (isSaved) {
                Toast.makeText(
                    requireContext(),
                    "Count Saved, women: $womenCount, men: $menCount",
                    Toast.LENGTH_SHORT
                ).show()
            }
            setCounterMode()
        }

        edit_counter_button.setOnClickListener {
            setEditCounterMode()
        }

        val womenStopTime = sharedPreferencesUtil.readWomenPauseTime()
        women_timer.base = SystemClock.elapsedRealtime() + womenStopTime
        val menStopTime = sharedPreferencesUtil.readMenPauseTime()
        men_timer.base = SystemClock.elapsedRealtime() + menStopTime

        women_start_timer_button.setOnClickListener {
            renderStartTimerUI()
            men_pause_button.setImageResource(R.mipmap.menplay)
            men_pause_button.tag = TimerState.PLAY
            women_pause_button.tag = TimerState.PAUSE
            changeEntireTheme(true)

        }

        men_start_timer_button.setOnClickListener {
            renderStartTimerUI()
            women_pause_button.setImageResource(R.mipmap.womenplay)
            women_pause_button.tag = TimerState.PLAY
            men_pause_button.tag = TimerState.PAUSE
            changeEntireTheme(false)

        }

        women_pause_button.setOnClickListener {
            changeEntireTheme(true)
        }

        men_pause_button.setOnClickListener {
            changeEntireTheme(false)
        }

        show_results_button.setOnClickListener {
            this.findNavController().navigate(R.id.action_mainFragment_to_resultsFragment)
        }

        reset_results.setOnClickListener {
            edit_men_count.text.clear()
            edit_women_count.text.clear()
            women_timer.base = SystemClock.elapsedRealtime()
            men_timer.base = SystemClock.elapsedRealtime()
            sharedPreferencesUtil.clearSharedPreferences()
        }
    }

    private fun changeEntireTheme(isWomen: Boolean) {
        setGenderBasedTheme(isWomen)
        changeThemeOnTimer()
        calculateSpokenTime(isWomen)
    }

    private fun setGenderBasedTheme(isWomen: Boolean) {
        if (isWomen) {
            counter_background.setBackgroundColor(resources.getColor(R.color.women))
            women_timer_layout.background = resources.getDrawable(R.drawable.women_timer_background)
            men_timer_layout.background = (resources.getDrawable(R.drawable.women_timer_background))
        } else {
            counter_background.setBackgroundColor(resources.getColor(R.color.men))
            women_timer_layout.background = resources.getDrawable(R.drawable.men_timer_background)
            men_timer_layout.background = (resources.getDrawable(R.drawable.men_timer_background))
        }

    }

    private fun changeThemeOnTimer() {
        counter_title.setTextColor(resources.getColor(R.color.white))
        women_title_text.setTextColor(resources.getColor(R.color.white))
        men_title_text.setTextColor(resources.getColor(R.color.white))
        edit_women_count.setTextColor(resources.getColor(R.color.white))
        edit_men_count.setTextColor(resources.getColor(R.color.white))
        women_timer_title.setTextColor(resources.getColor(R.color.white))
        men_timer_title.setTextColor(resources.getColor(R.color.white))
        women_timer.setTextColor(resources.getColor(R.color.white))
        men_timer.setTextColor(resources.getColor(R.color.white))
        reset_title.setTextColor(resources.getColor(R.color.white))
        total_duration_title.setTextColor(resources.getColor(R.color.white))
        total_duration.setTextColor(resources.getColor(R.color.white))
        show_results_button.isEnabled = true
        show_results_button.setTextColor(resources.getColor(R.color.white))
    }

    private fun renderStartTimerUI() {
        women_start_timer_button.visibility = View.GONE
        women_pause_button.visibility = View.VISIBLE
        men_start_timer_button.visibility = View.GONE
        men_pause_button.visibility = View.VISIBLE
    }

    private fun setEditCounterMode() {
        save_count.visibility = View.VISIBLE
        edit_counter_button.visibility = View.GONE
        counter_title.text = resources.getString(R.string.counter_title_edit)

        edit_women_count.isEnabled = true
        edit_men_count.isEnabled = true
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            edit_women_count.setBackgroundColor(resources.getColor(R.color.white, null))
            edit_men_count.setBackgroundColor(resources.getColor(R.color.white, null))
        }
    }

    private fun setCounterMode() {
        save_count.visibility = View.GONE
        edit_counter_button.visibility = View.VISIBLE
        counter_title.text = resources.getString(R.string.counter_title)

        edit_women_count.isEnabled = false
        edit_men_count.isEnabled = false
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            edit_women_count.setBackgroundColor(resources.getColor(R.color.gray, null))
            edit_men_count.setBackgroundColor(resources.getColor(R.color.gray, null))
        }
    }

    private fun dismissKeyboard() {
        edit_men_count.clearFocus()
        edit_women_count.clearFocus()
        val inputMethodManager: InputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(edit_men_count.windowToken, 0)
        inputMethodManager.hideSoftInputFromWindow(edit_women_count.windowToken, 0)

    }

    @ExperimentalTime
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val womenCount = sharedPreferencesUtil.readWomenCount()
        val menCount = sharedPreferencesUtil.readMenCount()
        if (womenCount > 0 || menCount > 0) {
            setCounterMode()
            edit_women_count.setText(womenCount.toString())
            edit_men_count.setText(menCount.toString())
        } else {
            setEditCounterMode()
        }
    }

    private fun calculateSpokenTime(isWomen: Boolean) {
        // button clicked was for the women timer
        if (isWomen) {
            if (men_pause_button.tag == TimerState.PLAY) {
                val elapsedTime = SystemClock.elapsedRealtime() - men_timer.base
                sharedPreferencesUtil.setPauseTimeForMen(men_timer.base - SystemClock.elapsedRealtime())
                sharedPreferencesUtil.setTimeForMen(elapsedTime)
                men_pause_button.tag = TimerState.PAUSE
                men_pause_button.setImageResource(R.mipmap.menplay)
                men_timer.stop()
            }
            if (women_pause_button.tag == TimerState.PAUSE) {
                women_pause_button.tag = TimerState.PLAY
                val stopTime = sharedPreferencesUtil.readWomenPauseTime()
                women_timer.base = SystemClock.elapsedRealtime() + stopTime
                women_pause_button.setImageResource(R.mipmap.womenpause)
                main_screen_layout.setBackgroundColor(resources.getColor(R.color.women))
                women_timer.start()
            } else {
                val elapsedTime = SystemClock.elapsedRealtime() - women_timer.base
                sharedPreferencesUtil.setPauseTimeForWomen(women_timer.base - SystemClock.elapsedRealtime())
                sharedPreferencesUtil.setTimeForWomen(elapsedTime)
                women_pause_button.tag = TimerState.PAUSE
                women_pause_button.setImageResource(R.mipmap.womenplay)
                women_timer.stop()
            }
        }
        // button clicked was for men timer
        else {
            if (women_pause_button.tag == TimerState.PLAY) {
                val elapsedTime = SystemClock.elapsedRealtime() - women_timer.base
                sharedPreferencesUtil.setPauseTimeForWomen(women_timer.base - SystemClock.elapsedRealtime())
                sharedPreferencesUtil.setTimeForWomen(elapsedTime)
                women_pause_button.tag = TimerState.PAUSE
                women_pause_button.setImageResource(R.mipmap.womenplay)
                women_timer.stop()

            }
            if (men_pause_button.tag == TimerState.PAUSE) {
                men_pause_button.tag = TimerState.PLAY
                val stopTime = sharedPreferencesUtil.readMenPauseTime()
                men_timer.base = SystemClock.elapsedRealtime() + stopTime
                men_pause_button.setImageResource(R.mipmap.menpause)
                main_screen_layout.setBackgroundColor(resources.getColor(R.color.men))
                men_timer.start()
            } else {
                val elapsedTime = SystemClock.elapsedRealtime() - men_timer.base
                sharedPreferencesUtil.setPauseTimeForMen(men_timer.base - SystemClock.elapsedRealtime())
                sharedPreferencesUtil.setTimeForMen(elapsedTime)
                men_pause_button.tag = TimerState.PAUSE
                men_pause_button.setImageResource(R.mipmap.menplay)
                men_timer.stop()
            }
        }

    }

}