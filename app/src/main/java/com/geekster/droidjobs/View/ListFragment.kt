package com.geekster.droidjobs.View

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.geekster.droidjobs.Models.JobData
import com.geekster.droidjobs.R
import com.geekster.droidjobs.Utils.Constants
import com.geekster.droidjobs.Utils.NetworkResult
import com.geekster.droidjobs.ViewModel.ListViewModel
import com.geekster.droidjobs.databinding.FragmentListBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {

    private var _binding : FragmentListBinding? = null

    private val binding get() = _binding!!

    private val listViewModel by viewModels<ListViewModel>()

    private lateinit var adapter : ListItemAdapter;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentListBinding.inflate(inflater, container, false)
        adapter = ListItemAdapter(::onItemCLicked)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObserver()

        if (isInternetAvailable()) {
            listViewModel.getList()
        } else {
            Toast.makeText(requireContext(), "No internet connection available", Toast.LENGTH_SHORT).show()
        }

        binding.listRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.listRecyclerView.adapter = adapter

        binding.swipeToRefresh.setOnRefreshListener {
            listViewModel.getList()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText)
                return false
            }

        })

        binding.listRecyclerView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                binding.searchView.clearFocus()

            }
            return@setOnTouchListener false
        }

    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    private fun bindObserver() {
        listViewModel.listResponseLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.setOriginalList(it.data?.data ?: emptyList())
                    binding.swipeToRefresh.isRefreshing = false
                }
                is NetworkResult.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeToRefresh.isRefreshing = false
                    Log.d(Constants.TAG, "MainActivity ErrorResponse: ${it.data} and ${it.message}")
                    Toast.makeText(requireContext(), "${it.data} and ${it.message}", Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    binding.swipeToRefresh.isRefreshing = true
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d(Constants.TAG, "bindObserver: Loading")
                }
                else -> {
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun onItemCLicked(jobData : JobData){
        val bundle = Bundle();
        bundle.putString("jobData", Gson().toJson(jobData))
        findNavController().navigate(R.id.action_listFragment_to_descFragment,bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}