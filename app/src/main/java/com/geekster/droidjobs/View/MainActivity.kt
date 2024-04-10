package com.geekster.droidjobs.View

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.geekster.droidjobs.R
import com.geekster.droidjobs.Utils.Constants
import com.geekster.droidjobs.Utils.Constants.TAG
import com.geekster.droidjobs.Utils.NetworkResult
import com.geekster.droidjobs.ViewModel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

//    private var binding : ActivityMainBinding ?= null
//
    private val listViewModel by viewModels<ListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding!!.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);



//        listViewModel.getList()
//        binding!!.buttonFetch.setOnClickListener {
//
//            bindObserver()
//        }


    }

    private fun bindObserver() {
        listViewModel.listResponseLiveData.observe(this, Observer {
            when(it){
                is NetworkResult.Success -> {
                    for (i in it.data!!.data){
                        Log.d(TAG, "MainActivity Response: ${i.company}")
                    }
                }
                is NetworkResult.Error -> {
                    Log.d(TAG, "MainActivity ErrorResponse: ${it.data} and ${it.message}")
                }
                is NetworkResult.Loading -> {
                    Log.d(TAG, "bindObserver: Loading")
                }
            }
        })
    }

}