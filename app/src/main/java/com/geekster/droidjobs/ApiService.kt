package com.geekster.droidjobs

import com.geekster.droidjobs.Models.Data
import com.geekster.droidjobs.Utils.Constants.getList
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {


    @GET(getList)
    suspend fun getData() : Response<Data>
}