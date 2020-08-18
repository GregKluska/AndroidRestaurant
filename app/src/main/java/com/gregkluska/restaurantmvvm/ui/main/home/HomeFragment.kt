package com.gregkluska.restaurantmvvm.ui.main.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.gregkluska.restaurantmvvm.R
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "AppDebug"

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel.getDishes().observe(viewLifecycleOwner, Observer { resource ->
            Log.d(TAG, "onCreateView: ${resource.data}")
        })
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

}