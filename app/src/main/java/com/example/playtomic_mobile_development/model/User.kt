package com.example.playtomic_mobile_development.model

import com.example.playtomic_mobile_development.model.enum.Gender

data class User(
    val id: Int,
    val userName: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val gender: Gender,
    val dateOfBirth: String,
    val description: String
)