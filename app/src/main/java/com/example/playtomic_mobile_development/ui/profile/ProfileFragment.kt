package com.example.playtomic_mobile_development.ui.profile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineStart

class ProfileFragment : Fragment() {

    private lateinit var viewSwitch: ViewSwitcher
    private var isButton1Clicked = true
    private var isButton2Clicked = false

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        //val storage = Firebase.storage

        // Check of de gebruiker is ingelogd
        val currentUser = firebaseAuth.currentUser
        /*var storageRef = storage.reference
        val fileReference = storageRef.child("userProfilePic.jpg")
        val gsReference = storage.getReferenceFromUrl("gs://bucket/images/stars.jpg")*/

        var updatedUser = ""
        currentUser?.let { user ->
            val userId = user.uid

            // Verwijzing naar de documentlocatie van de gebruiker in Firestore
            val userDocRef = firestore.collection("users").document(userId)

            // Haal de gegevens van de gebruiker op uit Firestore
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
                    // Toon het e-mailadres in een TextView met id 'textViewEmail' (vervang met de daadwerkelijke id van je TextView)
                    val updatedUser = User(
                            // Gebruik de UID van de huidige gebruiker van Firebase Auth
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
                            timeOfDay = TimeOfDay.NO_CHOICE
                        )
                    if (updatedUser != null) {
                        if (userGender == Gender.MALE.toString()){
                            updatedUser.gender= Gender.MALE;
                        }
                        else {
                            updatedUser.gender= Gender.FEMALE
                        };
                        if (userBestHand == BestHand.RIGHT.toString()){
                            updatedUser.bestHand = BestHand.RIGHT
                        }
                        else if (userBestHand == BestHand.LEFT.toString()){
                            updatedUser.bestHand = BestHand.LEFT
                        }
                        else if (userBestHand == BestHand.BOTH.toString()){
                            updatedUser.bestHand = BestHand.BOTH
                        }
                        else{
                            updatedUser.bestHand= BestHand.NO_CHOICE
                        };

                        if (userPosition == Position.BACKHAND.toString()){
                            updatedUser.position = Position.BACKHAND
                        }
                        else if (userPosition == Position.FOREHAND.toString()){
                            updatedUser.position = Position.FOREHAND
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
                    /*val imageProfilePic: ImageView = binding.profilePic
                    profileViewModel.username.observe(viewLifecycleOwner) {
                        val decodedBytes: ByteArray = Base64.decode(gsReference.path,
                            Base64.DEFAULT
                        )
                        val bitmap: Bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                        imageProfilePic.setImageBitmap(bitmap)
                    }*/
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
        val textViewMatches: TextView = binding.matches
        profileViewModel.matches.observe(viewLifecycleOwner) {
            textViewMatches.text = it
        }
        val textViewFollowers: TextView = binding.followers
        profileViewModel.followers.observe(viewLifecycleOwner) {
            textViewFollowers.text = it
        }
        val textViewFollowed: TextView = binding.followed
        profileViewModel.followed.observe(viewLifecycleOwner) {
            textViewFollowed.text = it
        }
        val buttonEditAccount: Button = binding.editAccount
        profileViewModel.buttonEditAccount.observe(viewLifecycleOwner) {
            buttonEditAccount.text = it

            buttonEditAccount.setOnClickListener {
                val intent = Intent(requireContext(), EditProfileActivity::class.java)
                startActivity(intent)
            }
        }


        val textView1: TextView = binding.level
        profileViewModel.level.observe(viewLifecycleOwner) {
            textView1.text = it
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