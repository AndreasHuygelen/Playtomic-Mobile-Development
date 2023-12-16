package com.example.playtomic_mobile_development.ui.play

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.playtomic_mobile_development.MainActivity
import com.example.playtomic_mobile_development.R
import com.example.playtomic_mobile_development.databinding.ActivityCreateBookingBinding
import com.example.playtomic_mobile_development.databinding.ActivityCreateMatchBinding
import com.example.playtomic_mobile_development.model.Booked
import com.example.playtomic_mobile_development.model.Match
import com.example.playtomic_mobile_development.model.User
import com.example.playtomic_mobile_development.model.enum.GenderMatch
import com.example.playtomic_mobile_development.model.enum.TypeMatch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Collections
import java.util.Locale
import java.util.UUID

class CreateBookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBookingBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid
        val spinnerDateMatch = binding.spinnerDateMatch

        val daysWithDates = arrayListOf<Pair<String, String>>()

        val calendar = Calendar.getInstance()

        repeat(31) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            val dateFormatDay = SimpleDateFormat("EEEE", Locale.getDefault())
            val dateFormatDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            daysWithDates.add(Pair(dateFormatDate.format(calendar.time), dateFormatDay.format(calendar.time)))
        }

        val formattedDates = daysWithDates.map { "${it.first} - ${it.second}" }

        // Creëer een adapter en stel deze in op de Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, formattedDates)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDateMatch.adapter = adapter

        binding.buttonCreateBooking.setOnClickListener {
            val court = intent.getStringExtra("court")
            val dateMatch = binding.spinnerDateMatch.selectedItem
            val timeMatch = binding.spinnerTimeMatch.selectedItem
            val bookingId = UUID.randomUUID().toString()
            val match = userId?.let { it1 ->
                if (court != null) {
                    Booked(
                        id = bookingId,
                        date = "$dateMatch - $timeMatch",
                        players = it1,
                        court = court
                    )
                }
                else{
                    null
                }
            }
            if (match != null) {
                firestore.collection("bookedcourts")
                    .document(bookingId)
                    .set(match)
                    .addOnSuccessListener { documentReference ->

                            Toast.makeText(
                                this,
                                "Court Booked",
                                Toast.LENGTH_SHORT
                            ).show()
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
}