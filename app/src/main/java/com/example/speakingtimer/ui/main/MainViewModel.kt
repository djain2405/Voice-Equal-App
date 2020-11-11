package com.example.speakingtimer.ui.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

const val DEFAULT_SHARED_PREFERENCES = "default_count_shared_preferences"
const val WOMEN_COUNT_KEY = "women_count_key"
const val MEN_COUNT_KEY = "men_count_key"
const val WOMEN_TIME_MILLISECONDS_KEY = "women_time_ms_key"
const val MEN_TIME_MILLISECONDS_KEY = "men_time_ms_key"

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences by lazy {
        application?.getSharedPreferences(DEFAULT_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    private fun setCountForWomen(womenCount: Int) {
        with(sharedPreferences.edit()) {
            putInt(WOMEN_COUNT_KEY, womenCount).apply()
        }
    }

    private fun setCountForMen(menCount: Int) {
        with(sharedPreferences.edit()) {
            putInt(MEN_COUNT_KEY, menCount).apply()

        }
    }

    fun setTimeForWomen(time: Long) {
        val savedTime = sharedPreferences.getLong(WOMEN_TIME_MILLISECONDS_KEY, 0)
        with(sharedPreferences.edit()) {
            putLong(WOMEN_TIME_MILLISECONDS_KEY, time + savedTime).apply()
        }
    }

    fun setTimeForMen(time: Long) {
        val savedTime = sharedPreferences.getLong(MEN_TIME_MILLISECONDS_KEY, 0)
        with(sharedPreferences.edit()) {
            putLong(MEN_TIME_MILLISECONDS_KEY, time + savedTime).apply()
        }
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

    fun saveCount(womenCount: Int, menCount: Int) {
        setCountForWomen(womenCount)
        setCountForMen(menCount)
    }


    class Factory(private val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}