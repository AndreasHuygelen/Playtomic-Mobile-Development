package com.example.playtomic_mobile_development.model

import com.example.playtomic_mobile_development.model.enum.GenderMatch
import com.example.playtomic_mobile_development.model.enum.TypeMatch

data class Match (
    var id: String? = null,
    var date: String,
    var players: List<String>,
    var court: String,
    var type: TypeMatch,
    var gender: GenderMatch

)
