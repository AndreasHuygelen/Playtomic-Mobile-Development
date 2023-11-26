package com.example.playtomic_mobile_development.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.playtomic_mobile_development.SignInActivity
import com.example.playtomic_mobile_development.databinding.ActivityCreateProfileBinding
import com.example.playtomic_mobile_development.model.User
import com.example.playtomic_mobile_development.model.enum.Gender
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.buttonCreate.setOnClickListener {
            // Haal de gegevens uit de invoervelden
            val id = intent.getStringExtra("USER_ID")
            val userName = binding.editTextUserName.text.toString()
            val firstName = binding.editTextFirstName.text.toString()
            val lastName = binding.editTextLastName.text.toString()
            val email = intent.getStringExtra("EMAIL")
            val phoneNumber = binding.editTextPhoneNumber.text.toString()
            val gender = binding.editTextGender.text.toString()
            val dateOfBirth = binding.editTextDateOfBirth.text.toString()
            val description = binding.editTextDescription.text.toString()


            var user = email?.let { email ->
                User(
                    id = "0",
                    userName = userName,
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    phoneNumber = phoneNumber,
                    gender = Gender.MALE,
                    dateOfBirth = dateOfBirth,
                    description = description

                )
            }
            if (id != null) {
                if (user != null) {
                    user.id = id
                }
            }
            if (user != null) {
                if (gender == Gender.MALE.toString()){
                    user.gender= Gender.MALE;
                }
                else {
                    user.gender= Gender.FEMALE
                };
            }

            // Voeg de nieuwe gebruiker toe aan Firestore
            if (user != null) {
                if (id != null) {
                    firestore.collection("users")
                        .document(id)
                        .set(user)
                        .addOnSuccessListener { documentReference ->
                            Toast.makeText(this, "User made", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, SignInActivity::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }
}
