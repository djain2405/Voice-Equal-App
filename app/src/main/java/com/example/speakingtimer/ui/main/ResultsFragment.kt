package com.example.speakingtimer.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.speakingtimer.R

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.result.observe(viewLifecycleOwner, Observer {
            Toast.makeText(this.requireContext(), "Result: $it", Toast.LENGTH_LONG).show()

        })
    }
}