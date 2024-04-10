package com.geekster.droidjobs.Models

data class JobData(
    val apply_url: String,
    val company: String,
    val contract_type: String,
    val description: String,
    val id: Int,
    val job_title: String,
    val location: String,
    val logo: String,
    val posted_on: String,
    val remote_type: String,
    val salary_max: Int,
    val salary_min: Int,
    val slug: String,
    val tags: String
)