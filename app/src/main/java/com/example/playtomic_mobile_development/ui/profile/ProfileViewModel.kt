package com.example.playtomic_mobile_development.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {


    //--------------------------------- User -------------------------------------------------------
    public val profilePic = MutableLiveData<String>().apply {
        value = "ProfilePic"
    }
    val imageProfilePic: LiveData<String> = profilePic

    public val username = MutableLiveData<String>().apply {
        value = "Username"
    }
    val textUsername: LiveData<String> = username

    public val firstname = MutableLiveData<String>().apply {
        value = "Firstname"
    }
    val textFirstname: LiveData<String> = firstname

    public val lastname = MutableLiveData<String>().apply {
        value = "Lastname"
    }
    val textLastname: LiveData<String> = lastname

    public val phonenumber = MutableLiveData<String>().apply {
        value = "Phonenumber"
    }
    val textPhonenumber: LiveData<String> = phonenumber

    public val gender = MutableLiveData<String>().apply {
        value = "Gender"
    }
    val textGender: LiveData<String> = gender

    public val dateOfBirth = MutableLiveData<String>().apply {
        value = "DateOfBirth"
    }
    val textDateOfBirth: LiveData<String> = dateOfBirth
    //--------------------------------- List Matches Followers Followed ----------------------------
    public val matches = MutableLiveData<String>().apply {
        value = "Matches"
    }
    val textMatches: LiveData<String> = matches

    public val followers = MutableLiveData<String>().apply {
        value = "Followers"
    }
    val textFollowers: LiveData<String> = followers

    public val followed = MutableLiveData<String>().apply {
        value = "Followed"
    }
    val textFollowed: LiveData<String> = followed

    //--------------------------------- Button Edit Account ----------------------------------------
    public val buttonEditAccount = MutableLiveData<String>().apply {
        value = "Edit Account"
    }
    val textButtonEditAccount: LiveData<String> = buttonEditAccount

    //--------------------------------- ViewSwitcher Activities ------------------------------------
    public val level = MutableLiveData<String>().apply {
        value = "Level progression"
    }
    val textLevel: LiveData<String> = level

    public val test2 = MutableLiveData<String>().apply {
        value = "Preferences of the player"
    }
    val text2: LiveData<String> = test2

    public val bestHand = MutableLiveData<String>().apply {
        value = "Preferences of the player"
    }
    val textBestHand: LiveData<String> = bestHand

    public val position = MutableLiveData<String>().apply {
        value = "Preferences of the player"
    }
    val textPosition: LiveData<String> = position

    public val timeOfDay = MutableLiveData<String>().apply {
        value = "Preferences of the player"
    }
    val textTimeOfDay: LiveData<String> = timeOfDay

    public val typeMatch = MutableLiveData<String>().apply {
        value = "Preferences of the player"
    }
    val textTypeMatch: LiveData<String> = typeMatch


}