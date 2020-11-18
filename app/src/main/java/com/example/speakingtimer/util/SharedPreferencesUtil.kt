package com.example.speakingtimer.util

import android.app.Application
import android.content.Context

const val DEFAULT_SHARED_PREFERENCES = "default_count_shared_preferences"
const val WOMEN_COUNT_KEY = "women_count_key"
const val MEN_COUNT_KEY = "men_count_key"
const val WOMEN_TIME_MILLISECONDS_KEY = "women_time_ms_key"
const val MEN_TIME_MILLISECONDS_KEY = "men_time_ms_key"
const val WOMEN_PAUSE_TIME_KEY = "women_pause_time_key"
const val MEN_PAUSE_TIME_KEY = "men_pause_time_key"

class SharedPreferencesUtil(private val application: Application) {
    private val sharedPreferences by lazy {
        application?.getSharedPreferences(DEFAULT_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    private fun setCountForWomen(womenCount: Int): Boolean {
        with(sharedPreferences.edit()) {
            return putInt(WOMEN_COUNT_KEY, womenCount).commit()
        }
    }

    private fun setCountForMen(menCount: Int): Boolean {
        with(sharedPreferences.edit()) {
            return putInt(MEN_COUNT_KEY, menCount).commit()

        }
    }

    fun setPauseTimeForWomen(time: Long) {
        with(sharedPreferences.edit()) {
            putLong(
                WOMEN_PAUSE_TIME_KEY,
                time
            ).apply()
        }
    }

    fun setPauseTimeForMen(time: Long) {
        with(sharedPreferences.edit()) {
            putLong(
                MEN_PAUSE_TIME_KEY,
                time
            ).apply()
        }
    }

    fun setTimeForWomen(time: Long) {
        with(sharedPreferences.edit()) {
            putLong(
                WOMEN_TIME_MILLISECONDS_KEY,
                time
            ).apply()
        }
    }

    fun setTimeForMen(time: Long) {
        with(sharedPreferences.edit()) {
            putLong(
                MEN_TIME_MILLISECONDS_KEY,
                time
            ).apply()
        }
    }

    fun readWomenPauseTime(): Long {
        return sharedPreferences.getLong(WOMEN_PAUSE_TIME_KEY, 0)
    }

    fun readMenPauseTime(): Long {
        return sharedPreferences.getLong(MEN_PAUSE_TIME_KEY, 0)
    }

    fun readWomenTime(): Long {
        return sharedPreferences.getLong(WOMEN_TIME_MILLISECONDS_KEY, 0)
    }

    fun readMenTime(): Long {
        return sharedPreferences.getLong(MEN_TIME_MILLISECONDS_KEY, 0)
    }

    fun readWomenCount(): Int {
        return sharedPreferences.getInt(WOMEN_COUNT_KEY, 0)
    }

    fun readMenCount(): Int {
        return sharedPreferences.getInt(MEN_COUNT_KEY, 0)
    }

    fun saveCount(womenCount: Int, menCount: Int): Boolean {
        val womenSaved = setCountForWomen(womenCount)
        val menSaved = setCountForMen(menCount)

        return womenSaved && menSaved
    }

    fun clearSharedPreferences() {
        sharedPreferences.edit().clear().apply()
    }
}