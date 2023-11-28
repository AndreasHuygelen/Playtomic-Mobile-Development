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

    //--------------------------------- Button Posts -----------------------------------------------
    public val buttonPosts = MutableLiveData<String>().apply {
        value = "Posts"
    }
    val textButtonPosts: LiveData<String> = buttonPosts

    //--------------------------------- Button Activities ------------------------------------------
    public val buttonActivities = MutableLiveData<String>().apply {
        value = "Activities"
    }
    val textButtonActivities: LiveData<String> = buttonActivities
    //--------------------------------- ViewSwitcher Activities ------------------------------------
    public val level = MutableLiveData<String>().apply {
        value = "Level progression"
    }
    val textLevel: LiveData<String> = level

    public val test2 = MutableLiveData<String>().apply {
        value = "Preferences of the player"
    }
    val text2: LiveData<String> = test2

    public val test3 = MutableLiveData<String>().apply {
        value = "My groeps"
    }
    val text3: LiveData<String> = test3

    public val test4 = MutableLiveData<String>().apply {
        value = "Ranking"
    }
    val text4: LiveData<String> = test4

    //--------------------------------- ViewSwitcher Posts -----------------------------------------
    public val test5 = MutableLiveData<String>().apply {
        value = "This is a nice profile"
    }
    val text5: LiveData<String> = test5
}