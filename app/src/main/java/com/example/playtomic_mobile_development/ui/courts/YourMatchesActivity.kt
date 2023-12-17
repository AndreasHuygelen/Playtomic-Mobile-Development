package com.example.playtomic_mobile_development.ui.courts

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.playtomic_mobile_development.R
import com.example.playtomic_mobile_development.databinding.ActivityCreateProfileBinding
import com.example.playtomic_mobile_development.databinding.ActivityYourMatchesBinding
import com.example.playtomic_mobile_development.databinding.FragmentPlayBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class YourMatchesActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityYourMatchesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_matches)
        storageReference = FirebaseStorage.getInstance().reference
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid
        binding = ActivityYourMatchesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (userId != null) {
            firestore.collection("matches")
                .whereArrayContains("players", userId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.e("YourMatchesActivity", "getting matches 123")
                        val matchId = document.id
                        val matchDate = document.getString("date") ?: ""
                        val matchType = document.getString("type") ?: ""
                        val matchGender = document.getString("gender") ?: ""
                        val matchCourt = document.getString("court") ?: ""
                        var matchPlayers = document.get("players") as? List<String> ?: listOf()

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

                        val playerTextViewWrapper = LinearLayout(this)
                        playerTextViewWrapper.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        val joinViewWrapper = LinearLayout(this)
                        joinViewWrapper.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        val playerViewWrapper = LinearLayout(this)
                        playerViewWrapper.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )


                        val textView = TextView(this)
                        textView.text = "$matchDate"
                        textView.textSize = 20f
                        textViewWrapper.addView(textView)

                        val textView2 = TextView(this)
                        textView2.text = "$matchType \n $matchGender"
                        textView2.textSize = 16f
                        val layoutParams = LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )



                        layoutParams.weight = 1f
                        textView2.layoutParams = layoutParams

                        joinViewWrapper.addView(textView2)



                        court(matchCourt, courtViewWrapper)

                        players(matchPlayers, playerViewWrapper)

                        noPlayers(matchPlayers, playerViewWrapper)


                        linearLayoutWrapper.addView(courtViewWrapper)
                        linearLayoutWrapper.addView(textViewWrapper)
                        linearLayoutWrapper.addView(playerTextViewWrapper)
                        linearLayoutWrapper.addView(playerViewWrapper)
                        linearLayoutWrapper.addView(joinViewWrapper)
                        val marginParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        playerViewWrapper.orientation = LinearLayout.HORIZONTAL

                        playerViewWrapper.gravity = Gravity.CENTER_HORIZONTAL
                        marginParams.setMargins(16, 16, 16, 16)
                        linearLayoutWrapper.layoutParams = marginParams
                        linearLayoutWrapper.setBackgroundResource(R.drawable.border)


                        binding?.matchesLayout?.addView(linearLayoutWrapper)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("YourMatchesActivity", "Error getting matches 123: ${exception.message}", exception)
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
                val courtName = documentSnapshot.getString("name")

                textView4.text = "$courtName"
                courtViewWrapper.addView(textView4)
            } else {
                Log.e("Playfragment", "Court not found")
            }
        }.addOnFailureListener { exception ->
            Log.e("Playfragment", "error 123: ${exception.message}")
        }
    }

    private fun players (matchPlayers:List<String>, playerViewWrapper :LinearLayout){
        for (playerId in matchPlayers) {
            Log.d("PlayFragment", "Player ID: $playerId")

            val imageView2 = ImageView(this)

            val imageReference2 = storageReference.child("$playerId.profile.png")
            var imageReference3 = storageReference.child("userProfilePic.png") // Standaard referentie naar een standaardafbeelding
            imageReference2.downloadUrl.addOnSuccessListener { uri ->
                Picasso.get().load(uri).resize(200, 200).into(imageView2)
                playerViewWrapper.addView(imageView2)

            }.addOnFailureListener { exception ->
                Log.e("Playfragment", "no img")
                imageReference3.downloadUrl.addOnSuccessListener { uri ->
                    Picasso.get().load(uri).resize(200, 200).into(imageView2)
                    playerViewWrapper.addView(imageView2)
                }.addOnFailureListener { exception ->
                    Log.e("Playfragment", "Error loading image for court 123", exception)

                }
            }

            imageView2.setOnClickListener {
                val usersCollection = firestore.collection("users")
                val userDocument = usersCollection.document(playerId)

                userDocument.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val username = documentSnapshot.getString("userName")

                        username?.let { playerName ->
                            val alertDialogBuilder = AlertDialog.Builder(this)
                            alertDialogBuilder.setTitle("User")
                            alertDialogBuilder.setMessage("UserName: $playerName")

                            alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                            }

                            val alertDialog = alertDialogBuilder.create()
                            alertDialog.show()
                        }
                    } else {
                        Log.e("Playfragment", "user not found")
                    }
                }.addOnFailureListener { exception ->
                    Log.e("Playfragment", "error 123: ${exception.message}")
                }
            }


        }
    }
    private fun noPlayers(matchPlayers:List<String>, playerViewWrapper:LinearLayout){
        val numberOfPlayers = matchPlayers.count()
        val missingPlayers = 4 - numberOfPlayers
        if (numberOfPlayers < 4) {
            for (i in 1..missingPlayers) {
                val imageView2 = ImageView(this)
                imageView2.setImageResource(R.drawable.add_player)
                playerViewWrapper.addView(imageView2)
            }
        } else {
            println("Amount of players is 4")
        }
    }
}