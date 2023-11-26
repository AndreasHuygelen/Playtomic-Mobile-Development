package com.example.playtomic_mobile_development.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.playtomic_mobile_development.MainActivity
import com.example.playtomic_mobile_development.R
import com.example.playtomic_mobile_development.SignInActivity
import com.example.playtomic_mobile_development.databinding.ActivityEditProfileBinding
import com.example.playtomic_mobile_development.model.User
import com.example.playtomic_mobile_development.model.enum.Gender
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = firebaseAuth.currentUser
        if (currentUser != null){
            currentUser?.let{
            }

        }

        if (currentUser != null){
        currentUser?.let{
            val userDocRef = firestore.collection("users").document(it.uid)
            userDocRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(User::class.java)
                    // Voer hier een breakpoint in om te controleren of 'user' null is of correcte waarden heeft
                    // Controleer of de waarden van 'user' juist worden opgehaald van Firestore
                    binding.editTextUserName.setText(user?.userName)
                    binding.editTextFirstName.setText(user?.firstName)
                    binding.editTextLastName.setText(user?.lastName)
                    binding.editTextPhoneNumber.setText(user?.phoneNumber)
                    binding.editTextGender.setText(user?.gender.toString())
                    binding.editTextDateOfBirth.setText(user?.dateOfBirth)
                    binding.editTextDescription.setText(user?.description)
                        // Voeg andere velden op dezelfde manier toe
                    binding.buttonSave.setOnClickListener {
                        val editedUserName = binding.editTextUserName.text.toString()
                        val editedFirstName = binding.editTextFirstName.text.toString()
                        val editedLastName = binding.editTextLastName.text.toString()
                        val editedPhoneNumber = binding.editTextPhoneNumber.text.toString()
                        val editedGender = binding.editTextGender.text.toString()
                        val editedDateOfBirth = binding.editTextDateOfBirth.text.toString()
                        val editedDescription = binding.editTextDescription.text.toString()
                        // Haal andere bewerkte gegevens op op dezelfde manier

                        val updatedUser = user?.let { user ->
                            User(
                                // Gebruik de UID van de huidige gebruiker van Firebase Auth
                                id = user.id,
                                userName = editedUserName,
                                firstName = editedFirstName,
                                lastName = editedLastName,
                                email = user.email,
                                phoneNumber = editedPhoneNumber,
                                gender = user.gender,
                                dateOfBirth = editedDateOfBirth,
                                description = editedDescription
                            )
                        }

                        if (updatedUser != null) {
                            if (editedGender == Gender.MALE.toString()){
                                updatedUser.gender= Gender.MALE;
                            }
                            else {
                                updatedUser.gender= Gender.FEMALE
                            };
                        }

                        if (updatedUser != null) {
                            userDocRef.set(updatedUser)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
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
            }.addOnFailureListener { exception ->
                // Behandel de fout bij het ophalen van de gegevens
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }

        }}

    }
}
