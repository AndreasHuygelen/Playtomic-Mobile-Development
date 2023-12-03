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
import com.example.playtomic_mobile_development.model.enum.BestHand
import com.example.playtomic_mobile_development.model.enum.Gender
import com.example.playtomic_mobile_development.model.enum.GenderMatch
import com.example.playtomic_mobile_development.model.enum.Position
import com.example.playtomic_mobile_development.model.enum.TimeOfDay
import com.example.playtomic_mobile_development.model.enum.TypeMatch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Collections
import java.util.UUID

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
            val court = intent.getStringExtra("court")
            val typeMatch = binding.spinnerTypeMatch.selectedItem
            val genderMatch = binding.spinnerGenderMatch.selectedItem
            val dateMatch = binding.spinnerDateMatch.selectedItem
            val timeMatch = binding.spinnerTimeMatch.selectedItem
            val matchId = UUID.randomUUID().toString()

            val match = court?.let { it1 ->
                Match(
                    id = matchId,
                    date = dateMatch.toString() + timeMatch.toString(),
                    players = playersList,
                    court = it1,
                    type = TypeMatch.FRIENDLY,
                    gender = GenderMatch.ALL_PLAYERS,


                    )
            }
                if (typeMatch == "Competitive"){

                    match?.type= TypeMatch.COMPETITIVE
                }
                else {
                    match?.type= TypeMatch.FRIENDLY;
                };
                if (genderMatch == "All players"){
                    match?.gender= GenderMatch.ALL_PLAYERS;
                }
                else if (genderMatch == "Male") {
                    match?.gender= GenderMatch.MEN_ONLY;
                }
                else if (genderMatch == "Female") {
                    match?.gender= GenderMatch.FEMALE_ONLY;
                }
                else {
                    match?.gender= GenderMatch.MIXED;
                };
            if (match != null) {
                firestore.collection("matches")
                    .document(matchId)
                    .set(match)
                    .addOnSuccessListener { documentReference ->

                        currentUser?.let{
                            val userDocRef = userId?.let { it1 ->
                                firestore.collection("users").document(
                                    it1
                                )
                            }
                            userDocRef?.get()?.addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot.exists()) {
                                    val user = documentSnapshot.toObject(User::class.java)

                                    val updatedUser = user?.let { user ->
                                        User(
                                            id = user.id,
                                            userName = user.userName,
                                            firstName = user.firstName,
                                            lastName = user.lastName,
                                            email = user.email,
                                            phoneNumber = user.phoneNumber,
                                            gender = user.gender,
                                            dateOfBirth = user.dateOfBirth,
                                            description = user.description,
                                            amountOfMatches = user.amountOfMatches+1,
                                            bestHand = user.bestHand,
                                            position = user.position,
                                            typeMatch = user.typeMatch,
                                            timeOfDay = user.timeOfDay

                                        )
                                    }
                                    if (updatedUser != null) {
                                        userDocRef.set(updatedUser)
                                            .addOnSuccessListener {
                                                Toast.makeText(this, "match made", Toast.LENGTH_SHORT).show()
                                                val intent = Intent(this, MainActivity::class.java).apply {
                                                    putExtra("mobile_navigation", R.id.navigation_profile)
                                                }
                                                startActivity(intent)
                                            }
                                            .addOnFailureListener { exception ->
                                                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                                            }

                                    }
                                }
                            }
                        }?.addOnFailureListener { exception ->
                            Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                        }






                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                    }
            }

                }

        }
    }
