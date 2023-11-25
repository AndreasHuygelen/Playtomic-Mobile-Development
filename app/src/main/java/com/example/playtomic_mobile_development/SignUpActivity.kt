package com.example.playtomic_mobile_development

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.playtomic_mobile_development.databinding.ActivitySignUpBinding
import com.example.playtomic_mobile_development.model.User
import com.example.playtomic_mobile_development.model.enum.Gender
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener{
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()){
                if (pass == confirmPass){
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { createUserTask ->
                        if (createUserTask.isSuccessful) {
                            val firebaseUser = firebaseAuth.currentUser

                            // Voeg hier de Firestore-code toe om de gebruiker aan Firestore toe te voegen
                            val db = Firebase.firestore
                            val user = firebaseUser?.let {
                                User(
                                    id = 0, // Geef hier een passende waarde voor de ID, bijvoorbeeld 0 of een uniek gegenereerd nummer
                                    userName = "", // Wachtwoord toewijzen
                                    firstName = "", // Voeg de voornaam van de gebruiker toe
                                    lastName = "", // Voeg de achternaam van de gebruiker toe
                                    email = email, // Het e-mailadres is al ingevuld
                                    phoneNumber = "", // Voeg het telefoonnummer van de gebruiker toe
                                    gender = Gender.MALE, // Voeg het geslacht van de gebruiker toe (bijvoorbeeld Gender.MALE of Gender.FEMALE)
                                    dateOfBirth = "", // Voeg de geboortedatum van de gebruiker toe
                                    description = "" // Voeg een beschrijving of biografie van de gebruiker toe
                                    // Voeg andere gewenste gebruikersinformatie toe
                                )
                            }

                            if (user != null) {
                                db.collection("users").document(firebaseUser.uid)
                                    .set(user)
                                    .addOnSuccessListener {
                                        // Gebruikersgegevens succesvol toegevoegd aan Firestore
                                        val intent = Intent(this, SignInActivity::class.java)
                                        startActivity(intent)
                                    }
                                    .addOnFailureListener { e ->
                                        // Fout bij toevoegen aan Firestore
                                        Toast.makeText(this, "Failed to save user data to Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            Toast.makeText(this, createUserTask.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else{
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "A field is empty!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}