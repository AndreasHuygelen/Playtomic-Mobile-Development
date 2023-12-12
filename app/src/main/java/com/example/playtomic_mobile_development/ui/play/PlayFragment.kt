package com.example.playtomic_mobile_development.ui.play

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.playtomic_mobile_development.R
import com.example.playtomic_mobile_development.databinding.FragmentPlayBinding
import com.example.playtomic_mobile_development.ui.profile.EditProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class PlayFragment : Fragment() {

    private var _binding: FragmentPlayBinding? = null
    private val binding get() = _binding

    private lateinit var firestore: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val playViewModel =
            ViewModelProvider(this).get(PlayViewModel::class.java)

        _binding = FragmentPlayBinding.inflate(inflater, container, false)
        val root: View = binding?.root ?: inflater.inflate(R.layout.fragment_play, container, false)

        storageReference = FirebaseStorage.getInstance().reference
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        firestore.collection("matches").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val matchId = document.id
                    val matchDate = document.getString("date") ?: ""
                    val matchType = document.getString("type") ?: ""
                    val matchGender = document.getString("gender") ?: ""
                    val matchCourt = document.getString("court") ?: ""
                    var matchPlayers = document.get("players") as? List<String> ?: listOf()

                    // Omhullende div voor lineaire lay-out
                    val linearLayoutWrapper = LinearLayout(requireContext())
                    linearLayoutWrapper.orientation = LinearLayout.VERTICAL

                    // Eerste XML-div voor tekstweergave
                    val textViewWrapper = LinearLayout(requireContext())
                    textViewWrapper.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    val playerTextViewWrapper = LinearLayout(requireContext())
                    playerTextViewWrapper.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    val joinViewWrapper = LinearLayout(requireContext())
                    joinViewWrapper.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    // Tweede XML-div voor afbeeldingweergave
                    val playerViewWrapper = LinearLayout(requireContext())
                    playerViewWrapper.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )






                        val textView = TextView(requireContext())
                        textView.text = "$matchDate"
                        textView.textSize = 20f
                        textViewWrapper.addView(textView)
                    val textView2 = TextView(requireContext())
                    textView2.text = "$matchType \n $matchGender"
                    textView2.textSize = 16f
                    val layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.weight = 1f
                    textView2.layoutParams = layoutParams

                    joinViewWrapper.addView(textView2)


                        for (playerId in matchPlayers) {
                            Log.d("PlayFragment", "Player ID: $playerId")

                            val imageView2 = ImageView(requireContext())
                            val imageReference2 = storageReference.child("$playerId.profile.png")
                            var imageReference3 = storageReference.child("userProfilePic.png") // Standaard referentie naar een standaardafbeelding
                            imageReference2.downloadUrl.addOnSuccessListener { uri ->
                                Picasso.get().load(uri).resize(200, 200).into(imageView2)
                                playerViewWrapper.addView(imageView2)

                            }.addOnFailureListener { exception ->
                                Log.e("CourtsFragment", "no img")
                                imageReference3.downloadUrl.addOnSuccessListener { uri ->
                                    Picasso.get().load(uri).resize(200, 200).into(imageView2)
                                    playerViewWrapper.addView(imageView2)
                                }.addOnFailureListener { exception ->
                                    Log.e("CourtsFragment", "Error loading image for court ", exception)

                                }
                            }
                        }
                    val numberOfPlayers = matchPlayers.count()
                    val missingPlayers = 4 - numberOfPlayers // Bereken het aantal ontbrekende spelers

                    if (numberOfPlayers < 4) {
                        for (i in 1..missingPlayers) {
                            val imageView2 = ImageView(requireContext())
                            // Laad de standaardafbeelding voor ontbrekende spelers
                            imageView2.setImageResource(R.drawable.add_player)
                            playerViewWrapper.addView(imageView2)
                        }
                    } else {
                        println("Aantal spelers is 4 of meer, geen ontbrekende iconen weergegeven.")
                    }



                        val matchRef = firestore.collection("matches").document(matchId)
                        matchRef.get().addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                val matchData = documentSnapshot.data
                                val matchPlayers = matchData?.get("players") as? MutableList<String> ?: mutableListOf()

                                val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
                                if (currentUserID != null && !matchPlayers.contains(currentUserID)) {
                                    val joinMatchButton = Button(requireContext())
                                    joinMatchButton.text = "Join Match"
                                    joinMatchButton.setOnClickListener {
                                        matchPlayers.add(currentUserID)
                                        matchData?.set("players", matchPlayers)

                                        if (matchData != null) {
                                            matchRef.set(matchData)
                                                .addOnSuccessListener {
                                                    Log.d("PlayFragment", "User added to match: $matchId")
                                                    Toast.makeText(requireContext(), "You joined the match", Toast.LENGTH_SHORT).show()
                                                    joinViewWrapper.removeView(joinMatchButton) // Verwijder de knop nadat de gebruiker is toegevoegd
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.e("PlayFragment", "Error updating match document", e)
                                                }
                                        }
                                    }
                                    val rightGravityLayoutParams = LinearLayout.LayoutParams(
                                        0,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    )
                                    rightGravityLayoutParams.weight = 1f
                                    rightGravityLayoutParams.gravity = Gravity.END // Rechts uitlijnen

                                    joinMatchButton.layoutParams = rightGravityLayoutParams
                                    joinViewWrapper.addView(joinMatchButton)
                                } else {
                                    Log.d("PlayFragment", "User is already in the match or currentUserID is null")
                                }
                            } else {
                                Log.e("PlayFragment", "Match document does not exist")
                            }

                        // Voeg XML-divs toe aan de omhullende lineaire lay-out
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
                        marginParams.setMargins(16, 16, 16, 16) // left, top, right, bottom
                        linearLayoutWrapper.layoutParams = marginParams
                        linearLayoutWrapper.setBackgroundResource(R.drawable.border)


                        // Voeg de omhullende lineaire lay-out toe aan de hoofdindeling
                        binding?.courtsLayout?.addView(linearLayoutWrapper)
                    }.addOnFailureListener { exception ->
                        Log.e("CourtsFragment", "Error loading image for court $matchCourt", exception)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("CourtsFragment", "Error getting courts", exception)
            }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}