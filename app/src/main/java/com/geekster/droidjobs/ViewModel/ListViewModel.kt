package com.geekster.droidjobs.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geekster.droidjobs.Models.Data
import com.geekster.droidjobs.Repository.ListRepositoryImpl
import com.geekster.droidjobs.Utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val listRepositoryImpl: ListRepositoryImpl) : ViewModel() {

    val listResponseLiveData: LiveData<NetworkResult<Data>>
        get() = listRepositoryImpl.listLiveData

    fun getList() {
        viewModelScope.launch {
            listRepositoryImpl.getAllData()
        }
    }
}