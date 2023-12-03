package com.example.playtomic_mobile_development.model

import com.example.playtomic_mobile_development.model.enum.BestHand
import com.example.playtomic_mobile_development.model.enum.Gender
import com.example.playtomic_mobile_development.model.enum.Position
import com.example.playtomic_mobile_development.model.enum.TimeOfDay
import com.example.playtomic_mobile_development.model.enum.TypeMatch
import java.util.Collections

data class User(
    var id: String,
    var userName: String,
    var firstName: String,
    var lastName: String,
    var email: String,
    var phoneNumber: String,
    var gender: Gender,
    var dateOfBirth: String,
    var description: String,
    var amountOfMatches: Long = 0,
    var followers: Int = 0,
    var followed: List<Int> = Collections.emptyList(),
    var bestHand: BestHand = BestHand.NO_CHOICE,
    var position: Position = Position.NO_CHOICE,
    var typeMatch: TypeMatch = TypeMatch.NO_CHOICE,
    var timeOfDay: TimeOfDay = TimeOfDay.NO_CHOICE
){
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        Gender.MALE,
        "",
        "",
        0,
        0,
        Collections.emptyList(),
        BestHand.NO_CHOICE,
        Position.NO_CHOICE,
        TypeMatch.NO_CHOICE,
        TimeOfDay.NO_CHOICE
    )
}