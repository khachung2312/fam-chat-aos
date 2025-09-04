package com.example.famchat.model.reponse


data class JobResponse(
    val form: String,
    val data: List<JobItem>
)
data class JobItem(
    val jobTitle: String,
    val jobDescription: String,
    val jobRequirements: String,
    val jobBenefits: String,
    val jobSalaryMin: Int,
    val jobSalaryMax: Int,
    val jobSalaryCurrency: String,
    val jobLocation: String,
    val jobType: String,
    val id: String
)