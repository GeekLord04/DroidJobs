package com.geekster.droidjobs.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.geekster.droidjobs.ApiService
import com.geekster.droidjobs.Models.Data
import com.geekster.droidjobs.Utils.Constants.TAG
import com.geekster.droidjobs.Utils.NetworkResult
import javax.inject.Inject

class ListRepositoryImpl @Inject constructor(private val apiService: ApiService) : ListRepository {

    private val _listLiveData = MutableLiveData<NetworkResult<Data>>()
    val listLiveData : LiveData<NetworkResult<Data>>
        get() = _listLiveData

    override suspend fun getAllData() {

        _listLiveData.postValue(NetworkResult.Loading())
        val response = apiService.getData()
        if (response.isSuccessful && response.body()!=null){
            _listLiveData.postValue(NetworkResult.Success(response.body()!!))
        }
        else if (response.errorBody() != null){
            _listLiveData.postValue(NetworkResult.Error(response.errorBody().toString()))
        }
        else{
            _listLiveData.postValue(NetworkResult.Error("Something went wrong!"))
        }
        //Log.d(TAG, "respository Response: ${response.body()}")
    }
}