package com.example.playtomic_mobile_development.model

import com.example.playtomic_mobile_development.model.enum.Gender

data class User(
    var id: String,
    var userName: String,
    var firstName: String,
    var lastName: String,
    var email: String,
    var phoneNumber: String,
    var gender: Gender,
    var dateOfBirth: String,
    var description: String
){
    constructor() : this("", "", "", "", "", "", Gender.MALE, "", "")
}