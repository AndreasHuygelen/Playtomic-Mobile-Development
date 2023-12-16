package com.example.playtomic_mobile_development.ui.play

import android.app.AlertDialog
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
import com.example.playtomic_mobile_development.MainActivity
import com.example.playtomic_mobile_development.R
import com.example.playtomic_mobile_development.databinding.FragmentPlayBinding
import com.example.playtomic_mobile_development.model.User
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
        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid

        firestore.collection("matches").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val matchId = document.id
                    val matchDate = document.getString("date") ?: ""
                    val matchType = document.getString("type") ?: ""
                    val matchGender = document.getString("gender") ?: ""
                    val matchCourt = document.getString("court") ?: ""
                    var matchPlayers = document.get("players") as? List<String> ?: listOf()

                    val linearLayoutWrapper = LinearLayout(requireContext())
                    linearLayoutWrapper.orientation = LinearLayout.VERTICAL

                    val courtViewWrapper = LinearLayout(requireContext())
                    courtViewWrapper.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

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



                    court(matchCourt, courtViewWrapper)

                    players(matchPlayers, playerViewWrapper)

                    noPlayers(matchPlayers, playerViewWrapper)






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
                                                    joinViewWrapper.removeView(joinMatchButton)
                                                    currentUser?.let{
                                                        val userDocRef = userId?.let { it1 ->
                                                            firestore.collection("users").document(
                                                                it1
                                                            )
                                                        }
                                                        userDocRef?.get()?.addOnSuccessListener { documentSnapshot ->
                                                            if (documentSnapshot.exists()) {
                                                                val user = documentSnapshot.toObject(
                                                                    User::class.java)

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
                                                                            Log.d("PlayFragment", "match made")

                                                                        }
                                                                        .addOnFailureListener { exception ->
                                                                            Log.d("PlayFragment", exception.message, exception)
                                                                        }

                                                                }
                                                            }
                                                        }
                                                    }?.addOnFailureListener { exception ->
                                                        Log.d("PlayFragment","current user not found", exception)
                                                    }
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


                        binding?.courtsLayout?.addView(linearLayoutWrapper)
                    }.addOnFailureListener { exception ->
                        Log.e("Playfragment", "Error loading image for court $matchCourt", exception)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Playfragment", "Error getting courts", exception)
            }

        return root
    }

    private fun court(matchCourt:String, courtViewWrapper: LinearLayout){
        val textView4 = TextView(requireContext())
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
            Log.e("Playfragment", "error : ${exception.message}")
        }
    }

    private fun players (matchPlayers:List<String>, playerViewWrapper :LinearLayout){
        for (playerId in matchPlayers) {
            Log.d("PlayFragment", "Player ID: $playerId")

            val imageView2 = ImageView(requireContext())

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
                    Log.e("Playfragment", "Error loading image for court ", exception)

                }
            }

            imageView2.setOnClickListener {
                val usersCollection = firestore.collection("users")
                val userDocument = usersCollection.document(playerId)

                userDocument.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val username = documentSnapshot.getString("userName")

                        username?.let { playerName ->
                            val alertDialogBuilder = AlertDialog.Builder(requireContext())
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
                    Log.e("Playfragment", "error: ${exception.message}")
                }
            }


        }
    }
    private fun noPlayers(matchPlayers:List<String>, playerViewWrapper:LinearLayout){
        val numberOfPlayers = matchPlayers.count()
        val missingPlayers = 4 - numberOfPlayers
        if (numberOfPlayers < 4) {
            for (i in 1..missingPlayers) {
                val imageView2 = ImageView(requireContext())
                imageView2.setImageResource(R.drawable.add_player)
                playerViewWrapper.addView(imageView2)
            }
        } else {
            println("Amount of players is 4")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}