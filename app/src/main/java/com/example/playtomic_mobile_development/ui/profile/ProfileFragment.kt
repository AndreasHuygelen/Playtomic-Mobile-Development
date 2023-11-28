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
import com.example.playtomic_mobile_development.model.enum.Gender
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
                            description = userDescription.toString()
                        )
                    if (updatedUser != null) {
                        if (userGender == Gender.MALE.toString()){
                            updatedUser.gender= Gender.MALE;
                        }
                        else {
                            updatedUser.gender= Gender.FEMALE
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

        val buttonActivities: TextView = binding.activities
        profileViewModel.buttonActivities.observe(viewLifecycleOwner) {
            buttonActivities.text = it
        }
        val buttonPosts: TextView = binding.posts
        profileViewModel.buttonPosts.observe(viewLifecycleOwner) {
            buttonPosts.text = it
        }

        val textView1: TextView = binding.level
        profileViewModel.level.observe(viewLifecycleOwner) {
            textView1.text = it
        }
        val textView2: TextView = binding.test2
        profileViewModel.test2.observe(viewLifecycleOwner) {
            textView2.text = it
        }
        val textView3: TextView = binding.test3
        profileViewModel.test3.observe(viewLifecycleOwner) {
            textView3.text = it
        }
        val textView4: TextView = binding.test4
        profileViewModel.test4.observe(viewLifecycleOwner) {
            textView4.text = it
        }
        val textView5: TextView = binding.test5
        profileViewModel.test5.observe(viewLifecycleOwner) {
            textView5.text = it
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewSwitch = view.findViewById(R.id.viewSwitch)

        val buttonTest1: Button = view.findViewById(R.id.posts)
        val buttonSwitch: Button = view.findViewById(R.id.activities)

        buttonTest1.setOnClickListener {
            if (!isButton1Clicked) {
                viewSwitch.showPrevious()
                isButton1Clicked = true
                isButton2Clicked = false
            }
        }

        buttonSwitch.setOnClickListener {
            if (!isButton2Clicked) {
                viewSwitch.showNext()
                isButton2Clicked = true
                isButton1Clicked = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}