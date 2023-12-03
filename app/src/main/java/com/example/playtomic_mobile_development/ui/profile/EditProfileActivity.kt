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
import com.example.playtomic_mobile_development.model.enum.BestHand
import com.example.playtomic_mobile_development.model.enum.Gender
import com.example.playtomic_mobile_development.model.enum.Position
import com.example.playtomic_mobile_development.model.enum.TimeOfDay
import com.example.playtomic_mobile_development.model.enum.TypeMatch
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
                    binding.editTextUserName.setText(user?.userName)
                    binding.editTextFirstName.setText(user?.firstName)
                    binding.editTextLastName.setText(user?.lastName)
                    binding.editTextPhoneNumber.setText(user?.phoneNumber)
                    binding.editTextDateOfBirth.setText(user?.dateOfBirth)
                    binding.editTextDescription.setText(user?.description)
                    binding.buttonSave.setOnClickListener {
                        val editedUserName = binding.editTextUserName.text.toString()
                        val editedFirstName = binding.editTextFirstName.text.toString()
                        val editedLastName = binding.editTextLastName.text.toString()
                        val editedPhoneNumber = binding.editTextPhoneNumber.text.toString()
                        val selectedGender = binding.spinnerGender.selectedItem
                        val selectedBestHand = binding.spinnerBestHand.selectedItem
                        val selectedPosition = binding.spinnerPosition.selectedItem
                        val selectedTypeMatch = binding.spinnerTypeMatch.selectedItem
                        val selectedTimeOfDay = binding.spinnerTimeOfDay.selectedItem
                        val editedDateOfBirth = binding.editTextDateOfBirth.text.toString()
                        val editedDescription = binding.editTextDescription.text.toString()

                        val updatedUser = user?.let { user ->
                            User(
                                id = user.id,
                                userName = editedUserName,
                                firstName = editedFirstName,
                                lastName = editedLastName,
                                email = user.email,
                                phoneNumber = editedPhoneNumber,
                                gender = user.gender,
                                dateOfBirth = editedDateOfBirth,
                                description = editedDescription,
                                bestHand = user.bestHand,
                                position = user.position,
                                typeMatch = user.typeMatch,
                                timeOfDay = user.timeOfDay

                            )
                        }


                        if (updatedUser != null) {
                            if (selectedGender == "Male"){
                                    updatedUser.gender = Gender.MALE
                            }
                            else{
                                updatedUser.gender= Gender.FEMALE
                            };

                            if (selectedBestHand == "Right"){
                                updatedUser.bestHand = BestHand.RIGHT
                            }
                            else if (selectedBestHand == "Left"){
                                updatedUser.bestHand = BestHand.LEFT
                            }
                            else if (selectedBestHand == "Both"){
                                updatedUser.bestHand = BestHand.BOTH
                            }
                            else{
                                updatedUser.bestHand= BestHand.NO_CHOICE
                            };

                            if (selectedPosition == "Backhand"){
                                updatedUser.position = Position.BACKHAND
                            }
                            else if (selectedPosition == "Forehand"){
                                updatedUser.position = Position.FOREHAND
                            }
                            else if (selectedPosition == "Both"){
                                updatedUser.position = Position.BOTH
                            }
                            else{
                                updatedUser.position= Position.NO_CHOICE
                            };

                            if (selectedTimeOfDay == "Morning"){
                                updatedUser.timeOfDay = TimeOfDay.MORNING
                            }
                            else if (selectedTimeOfDay == "Noon"){
                                updatedUser.timeOfDay = TimeOfDay.NOON
                            }
                            else if (selectedTimeOfDay == "Evening"){
                                updatedUser.timeOfDay = TimeOfDay.EVENING
                            }
                            else if (selectedTimeOfDay == "Hole day"){
                                updatedUser.timeOfDay = TimeOfDay.HOLE_DAY
                            }
                            else{
                                updatedUser.timeOfDay= TimeOfDay.NO_CHOICE
                            };

                            if (selectedTypeMatch == "Competitive"){
                                updatedUser.typeMatch = TypeMatch.COMPETITIVE
                            }
                            else if (selectedTypeMatch == "Friendly"){
                                updatedUser.typeMatch = TypeMatch.FRIENDLY
                            }
                            else if (selectedTypeMatch == "Both"){
                                updatedUser.typeMatch = TypeMatch.BOTH
                            }
                            else{
                                updatedUser.typeMatch= TypeMatch.NO_CHOICE
                            };
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
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }

        }}

    }
}
