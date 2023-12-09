package com.example.playtomic_mobile_development.ui.profile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.playtomic_mobile_development.R
import com.example.playtomic_mobile_development.databinding.FragmentProfileBinding
import com.example.playtomic_mobile_development.model.User
import com.example.playtomic_mobile_development.model.enum.BestHand
import com.example.playtomic_mobile_development.model.enum.Gender
import com.example.playtomic_mobile_development.model.enum.Position
import com.example.playtomic_mobile_development.model.enum.TimeOfDay
import com.example.playtomic_mobile_development.model.enum.TypeMatch
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineStart

class ProfileFragment : Fragment() {

    private lateinit var storageReference: StorageReference

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storageReference = Firebase.storage.reference

        val currentUser = firebaseAuth.currentUser

        currentUser?.let { user ->
            val userId = user.uid

            val userDocRef = firestore.collection("users").document(userId)

            userDocRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userId = documentSnapshot.getString("id")
                    val userUserName = documentSnapshot.getString("userName")
                    val userFirstName = documentSnapshot.getString("firstName")
                    val userLastName = documentSnapshot.getString("lastName")
                    val userEmail = documentSnapshot.getString("email")
                    val userPhoneNumber = documentSnapshot.getString("phoneNumber")
                    val userGender = documentSnapshot.getString("gender")
                    val userDateOfBirth = documentSnapshot.getString("dateOfBirth")
                    val userDescription = documentSnapshot.getString("description")
                    val userBestHand = documentSnapshot.getString("bestHand")
                    val userPosition = documentSnapshot.getString("position")
                    val userTypeMatch = documentSnapshot.getString("typeMatch")
                    val userTimeOfDay = documentSnapshot.getString("timeOfDay")
                    val userMatches = documentSnapshot.get("amountOfMatches")

                    val updatedUser = User(
                            id = userId.toString(),
                            userName = userUserName.toString(),
                            firstName = userFirstName.toString(),
                            lastName = userLastName.toString(),
                            email = userEmail.toString(),
                            phoneNumber = userPhoneNumber.toString(),
                            gender = Gender.MALE,
                            dateOfBirth = userDateOfBirth.toString(),
                            description = userDescription.toString(),
                            bestHand = BestHand.NO_CHOICE,
                            position = Position.NO_CHOICE,
                            typeMatch = TypeMatch.NO_CHOICE,
                            timeOfDay = TimeOfDay.NO_CHOICE,

                        )
                    if (userMatches != null) {
                        val matches = userMatches
                        if (matches != null) {
                            updatedUser.amountOfMatches = matches as Long
                        }
                    }

                    if (updatedUser != null) {
                        if (userGender == Gender.MALE.toString()){
                            updatedUser.gender= Gender.MALE;
                        }
                        else {
                            updatedUser.gender= Gender.FEMALE
                        };
                        if (userBestHand == BestHand.BACKHAND.toString()){
                            updatedUser.bestHand = BestHand.BACKHAND
                        }
                        else if (userBestHand == BestHand.FOREHAND.toString()){
                            updatedUser.bestHand = BestHand.FOREHAND
                        }
                        else if (userBestHand == BestHand.BOTH.toString()){
                            updatedUser.bestHand = BestHand.BOTH
                        }
                        else{
                            updatedUser.bestHand= BestHand.NO_CHOICE
                        };

                        if (userPosition == Position.RIGHT.toString()){
                            updatedUser.position = Position.RIGHT
                        }
                        else if (userPosition == Position.LEFT.toString()){
                            updatedUser.position = Position.LEFT
                        }
                        else if (userPosition == Position.BOTH.toString()){
                            updatedUser.position = Position.BOTH
                        }
                        else{
                            updatedUser.position= Position.NO_CHOICE
                        };

                        if (userTimeOfDay == TimeOfDay.MORNING.toString()){
                            updatedUser.timeOfDay = TimeOfDay.MORNING
                        }
                        else if (userTimeOfDay == TimeOfDay.NOON.toString()){
                            updatedUser.timeOfDay = TimeOfDay.NOON
                        }
                        else if (userTimeOfDay == TimeOfDay.EVENING.toString()){
                            updatedUser.timeOfDay = TimeOfDay.EVENING
                        }
                        else if (userTimeOfDay == TimeOfDay.HOLE_DAY.toString()){
                            updatedUser.timeOfDay = TimeOfDay.HOLE_DAY
                        }
                        else{
                            updatedUser.timeOfDay= TimeOfDay.NO_CHOICE
                        };

                        if (userTypeMatch == TypeMatch.COMPETITIVE.toString()){
                            updatedUser.typeMatch = TypeMatch.COMPETITIVE
                        }
                        else if (userTypeMatch == TypeMatch.FRIENDLY.toString()){
                            updatedUser.typeMatch = TypeMatch.FRIENDLY
                        }
                        else if (userTypeMatch == TypeMatch.BOTH.toString()){
                            updatedUser.typeMatch = TypeMatch.BOTH
                        }
                        else{
                            updatedUser.typeMatch= TypeMatch.NO_CHOICE
                        };
                    }
                    val profileViewModel =
                        ViewModelProvider(this).get(ProfileViewModel::class.java)
                    val textViewUserName: TextView = binding.username
                    profileViewModel.username.observe(viewLifecycleOwner) {
                        textViewUserName.text = updatedUser.userName
                    }
                    val textViewFirstName: TextView = binding.firstname
                    profileViewModel.username.observe(viewLifecycleOwner) {
                        textViewFirstName.text = updatedUser.firstName
                    }
                    val textViewLastName: TextView = binding.lastname
                    profileViewModel.username.observe(viewLifecycleOwner) {
                        textViewLastName.text = updatedUser.lastName
                    }
                    val textViewPhoneNumber: TextView = binding.phonenumber
                    profileViewModel.username.observe(viewLifecycleOwner) {
                        textViewPhoneNumber.text = updatedUser.phoneNumber
                    }
                    val textViewGender: TextView = binding.gender
                    profileViewModel.username.observe(viewLifecycleOwner) {
                        textViewGender.text = updatedUser.gender.toString()
                    }
                    val textViewDateOfBirth: TextView = binding.dateOfBirth
                    profileViewModel.username.observe(viewLifecycleOwner) {
                        textViewDateOfBirth.text = updatedUser.dateOfBirth
                    }

                    val textBestHand: TextView = binding.bestHand
                    profileViewModel.bestHand.observe(viewLifecycleOwner) {
                        textBestHand.text = "Best hand: " + updatedUser.bestHand.toString().lowercase()
                    }

                    val textPosition: TextView = binding.position
                    profileViewModel.position.observe(viewLifecycleOwner) {
                        textPosition.text = "Position: " + updatedUser.position.toString().lowercase()
                    }

                    val textTimeOfDay: TextView = binding.timeOfDay
                    profileViewModel.timeOfDay.observe(viewLifecycleOwner) {
                        textTimeOfDay.text = "Time of day: " + updatedUser.timeOfDay.toString().lowercase()
                    }

                    val textTypeMatch: TextView = binding.typeMatch
                    profileViewModel.typeMatch.observe(viewLifecycleOwner) {
                        textTypeMatch.text = "Type match: " + updatedUser.typeMatch.toString().lowercase()
                    }

                    val textViewMatches: TextView = binding.matches
                    profileViewModel.matches.observe(viewLifecycleOwner) {
                        textViewMatches.text = it + "\n" + updatedUser.amountOfMatches.toString()
                    }
                    val textViewFollowers: TextView = binding.followers
                    profileViewModel.followers.observe(viewLifecycleOwner) {
                        textViewFollowers.text = it + "\n" + updatedUser.followers.toString()
                    }
                    val textViewFollowed: TextView = binding.followed
                    profileViewModel.followed.observe(viewLifecycleOwner) {
                        textViewFollowed.text = it + "\n" + updatedUser.followed.count()
                    }


                    var imageReference = storageReference.child("userProfilePic.jpg") // Standaard referentie naar een standaardafbeelding

                    val specificImageRef = storageReference.child("${updatedUser.id}.profile.png")
                    val storage =storageReference.child("${updatedUser.id}.profile.png")
                    storage.metadata
                        .addOnSuccessListener { metadata ->
                            specificImageRef.downloadUrl.addOnSuccessListener { uri ->
                                Picasso.get().load(uri.toString()).into(binding.profilePic)
                            }.addOnFailureListener { exception ->
                                Log.e("firebase", exception.toString())
                            }
                        }
                        .addOnFailureListener { exception ->
                            imageReference.downloadUrl.addOnSuccessListener { uri ->
                                Picasso.get().load(uri.toString()).into(binding.profilePic)
                            }.addOnFailureListener { exception ->
                                Log.e("firebase", exception.toString())

                            }
                        }


                }
            }.addOnFailureListener { exception ->
                // Er is een fout opgetreden bij het ophalen van de gebruikersgegevens
                // Behandel de fout hier
            }
        }


        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val buttonEditAccount: Button = binding.editAccount
        profileViewModel.buttonEditAccount.observe(viewLifecycleOwner) {
            buttonEditAccount.text = it

            buttonEditAccount.setOnClickListener {
                val intent = Intent(requireContext(), EditProfileActivity::class.java)
                startActivity(intent)
            }
        }



        val textView2: TextView = binding.test2
        profileViewModel.test2.observe(viewLifecycleOwner) {
            textView2.text = it
        }

        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}