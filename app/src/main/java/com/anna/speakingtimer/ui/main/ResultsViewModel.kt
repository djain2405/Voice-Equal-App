package com.anna.speakingtimer.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.anna.speakingtimer.model.Result
import com.anna.speakingtimer.util.SharedPreferencesUtil

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
        val menTime = sharedPreferences.readMenTime() / 1000
        val womenTime = sharedPreferences.readWomenTime() / 1000
        val menPercentCount = if (menCount > 0 || womenCount > 0) {
            (menCount * 100) / (menCount + womenCount)
        } else 0
        val womenPercentCount = if (menCount > 0 || womenCount > 0) {
            (womenCount * 100) / (menCount + womenCount)
        } else 0

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
            percentWomenTime = percentWomenTime,
            percentWomenCount = womenPercentCount,
            percentMenCount = menPercentCount
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