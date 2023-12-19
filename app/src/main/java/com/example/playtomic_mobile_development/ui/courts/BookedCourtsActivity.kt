package com.example.playtomic_mobile_development.ui.courts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.playtomic_mobile_development.MainActivity
import com.example.playtomic_mobile_development.R
import com.example.playtomic_mobile_development.databinding.ActivityBookedCourtsBinding
import com.example.playtomic_mobile_development.databinding.ActivityYourMatchesBinding
import com.example.playtomic_mobile_development.ui.community.MapActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class BookedCourtsActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityBookedCourtsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storageReference = FirebaseStorage.getInstance().reference
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid
        binding = ActivityBookedCourtsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        if (userId != null) {
            firestore.collection("bookedcourts")
                .whereEqualTo("players", userId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.e("BookedCourtsActivity", "getting matches 123")
                        val matchId = document.id
                        val matchDate = document.getString("date") ?: ""
                        val matchCourt = document.getString("court") ?: ""

                        val linearLayoutWrapper = LinearLayout(this)
                        linearLayoutWrapper.orientation = LinearLayout.VERTICAL

                        val courtViewWrapper = LinearLayout(this)
                        courtViewWrapper.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        val textViewWrapper = LinearLayout(this)
                        textViewWrapper.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )


                        val textView = TextView(this)
                        textView.text = "$matchDate"
                        textView.textSize = 20f
                        textViewWrapper.addView(textView)








                        court(matchCourt, courtViewWrapper)


                        linearLayoutWrapper.addView(courtViewWrapper)
                        linearLayoutWrapper.addView(textViewWrapper)
                        val marginParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        marginParams.setMargins(16, 16, 16, 16)
                        linearLayoutWrapper.layoutParams = marginParams
                        linearLayoutWrapper.setBackgroundResource(R.drawable.border)


                        binding?.bookedLayout?.addView(linearLayoutWrapper)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("BookedCourtsActivity", "Error getting matches 123: ${exception.message}", exception)
                }
        }
    }
    private fun court(matchCourt:String, courtViewWrapper: LinearLayout){
        val textView4 = TextView(this)
        textView4.textSize = 20f


        val courtsCollection = firestore.collection("courts")
        val courtDocument = courtsCollection.document(matchCourt)

        courtDocument.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val linearLayout = LinearLayout(this)
                linearLayout.orientation = LinearLayout.HORIZONTAL
                linearLayout.gravity = Gravity.CENTER_VERTICAL

                val courtName = documentSnapshot.getString("name")
                val courtLat = documentSnapshot.getDouble("lat") ?: ""
                val courtLong = documentSnapshot.getDouble("long") ?: ""

                textView4.text = "$courtName"

                val buttonMap = Button(this)
                buttonMap.text = "map"
                buttonMap.setOnClickListener {
                    val intent = Intent(this, MapActivity::class.java)
                    intent.putExtra("lat", courtLat)
                    intent.putExtra("long", courtLong)
                    startActivity(intent)
                }

                linearLayout.addView(textView4)
                linearLayout.addView(buttonMap)

                courtViewWrapper.addView(linearLayout)
            } else {
                Log.e("BookedCourtsActivity", "Court not found")
            }
        }.addOnFailureListener { exception ->
            Log.e("BookedCourtsActivity", "error 123: ${exception.message}")
        }
    }
}