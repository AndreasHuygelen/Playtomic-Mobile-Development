package com.example.playtomic_mobile_development.model

import com.example.playtomic_mobile_development.model.enum.GenderMatch
import com.example.playtomic_mobile_development.model.enum.TypeMatch

data class Booked (
    var id: String? = null,
    var date: String,
    var players: String,
    var court: String,
)