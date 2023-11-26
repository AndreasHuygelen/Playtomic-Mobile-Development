package com.example.playtomic_mobile_development

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.playtomic_mobile_development.databinding.ActivitySignUpBinding
import com.example.playtomic_mobile_development.model.User
import com.example.playtomic_mobile_development.model.enum.Gender
import com.example.playtomic_mobile_development.ui.profile.CreateProfileActivity
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
                            val userId = createUserTask.result?.user?.uid ?: "" // Haal de UID van de nieuwe gebruiker op
                            val intent = Intent(this, CreateProfileActivity::class.java)
                            intent.putExtra("EMAIL", email)
                            intent.putExtra("USER_ID", userId) // Voeg de UserID toe aan de intent
                            startActivity(intent)
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