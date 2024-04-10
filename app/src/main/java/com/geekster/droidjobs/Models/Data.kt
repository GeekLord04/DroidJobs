package com.geekster.droidjobs.Models

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("data")
    val data: List<JobData>,
    @SerializedName("last_updated")
    val last_updated: String,
    @SerializedName("legal")
    val legal: String
)