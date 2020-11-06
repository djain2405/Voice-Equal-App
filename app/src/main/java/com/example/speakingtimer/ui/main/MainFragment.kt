package com.example.speakingtimer.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.speakingtimer.R
import kotlinx.android.synthetic.main.main_fragment.*

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

    }

}