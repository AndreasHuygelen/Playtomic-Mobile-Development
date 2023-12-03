package com.example.playtomic_mobile_development.ui.play

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.playtomic_mobile_development.MainActivity
import com.example.playtomic_mobile_development.R
import com.example.playtomic_mobile_development.SignInActivity
import com.example.playtomic_mobile_development.databinding.ActivityCreateMatchBinding
import com.example.playtomic_mobile_development.model.Match
import com.example.playtomic_mobile_development.model.User
import com.example.playtomic_mobile_development.model.enum.Gender
import com.example.playtomic_mobile_development.model.enum.GenderMatch
import com.example.playtomic_mobile_development.model.enum.TypeMatch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Collections

class CreateMatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateMatchBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid
        var playersList = Collections.emptyList<String>()
        if (userId != null) {
            playersList = listOf(userId)
        }

        binding.buttonCreate.setOnClickListener {
            val typeMatch = binding.spinnerTypeMatch.selectedItem
            val genderMatch = binding.spinnerGenderMatch.selectedItem
            val dateMatch = binding.spinnerDateMatch.selectedItem
            val timeMatch = binding.spinnerTimeMatch.selectedItem

            var match = Match(
                date = dateMatch.toString() + timeMatch.toString(),
                players = playersList,
                court = "Mortsel",
                type = TypeMatch.FRIENDLY,
                gender = GenderMatch.ALL_PLAYERS,


                )
                if (typeMatch == TypeMatch.COMPETITIVE){
                    match.type= TypeMatch.COMPETITIVE;
                }
                else {
                    match.type= TypeMatch.FRIENDLY;
                };
            if (genderMatch == GenderMatch.ALL_PLAYERS){
                match.gender= GenderMatch.ALL_PLAYERS;
            }
            else if (genderMatch == GenderMatch.MEN_ONLY) {
                match.gender= GenderMatch.MEN_ONLY;
            }
            else if (genderMatch == GenderMatch.FEMALE_ONLY) {
                match.gender= GenderMatch.FEMALE_ONLY;
            }
            else {
                match.gender= GenderMatch.MIXED;
            };
            firestore.collection("matches")
                .add(match)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "match made", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
