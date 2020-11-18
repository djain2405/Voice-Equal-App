package com.example.speakingtimer.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.example.speakingtimer.model.Result
import com.example.speakingtimer.util.SharedPreferencesUtil

class ResultsViewModel(application: Application) : AndroidViewModel(application) {

    private val _result = MutableLiveData<Result>()
    val result: LiveData<Result>
        get() = _result
    private val sharedPreferences by lazy {
        SharedPreferencesUtil(application)
    }

    init {
        val menCount = sharedPreferences.readMenCount()
        val womenCount = sharedPreferences.readWomenCount()
        val menTime = sharedPreferences.readMenTime()/1000
        val womenTime = sharedPreferences.readWomenTime()/1000
        val percentMenTime = if (menTime > 0 || womenTime > 0) {
            ((menTime * 100) / (menTime + womenTime)).toInt()
        } else 0

        val percentWomenTime = if (menTime > 0 || womenTime > 0) {
            ((womenTime * 100) / (menTime + womenTime)).toInt().coerceAtLeast(100 - percentMenTime)
        } else 0

        _result.value = Result(
            menCount = menCount,
            womenCount = womenCount,
            menTime = menTime,
            womenTime = womenTime,
            percentMenTime = percentMenTime,
            percentWomenTime = percentWomenTime
        )
    }

    class Factory(private val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ResultsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ResultsViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}