package com.example.playtomic_mobile_development.ui.play

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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

    private lateinit var firestore: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    private lateinit var firebaseAuth: FirebaseAuth
    private val binding get() = _binding

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
                    var matchPlayers = document.get("players") ?: ""


                    val linearLayout = LinearLayout(requireContext())
                    linearLayout.orientation = LinearLayout.VERTICAL

                    val textView = TextView(requireContext())
                    textView.text = "Date: $matchDate \n Type: $matchType \n Gender: $matchGender \n players : $matchPlayers"
                    textView.textSize = 20f

                    linearLayout.addView(textView)

                    val imageView = ImageView(requireContext())
                    val imageReference = storageReference.child("$matchCourt.court.png")


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
                                                linearLayout.removeView(joinMatchButton) // Verwijder de knop nadat de gebruiker is toegevoegd
                                            }
                                            .addOnFailureListener { e ->
                                                Log.e("PlayFragment", "Error updating match document", e)
                                            }
                                    }
                                }
                                linearLayout.addView(joinMatchButton)
                            } else {
                                Log.d("PlayFragment", "User is already in the match or currentUserID is null")
                            }
                        } else {
                            Log.e("PlayFragment", "Match document does not exist")
                        }
                    }.addOnFailureListener { e ->
                        Log.e("PlayFragment", "Error getting match document", e)
                    }



                    imageReference.downloadUrl.addOnSuccessListener { uri ->
                        Picasso.get().load(uri).resize(200, 200).into(imageView)

                        linearLayout.addView(imageView)

                        binding?.courtsLayout?.addView(linearLayout)
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