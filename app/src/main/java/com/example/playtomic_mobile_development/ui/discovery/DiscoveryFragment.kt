package com.example.playtomic_mobile_development.ui.discovery

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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomic_mobile_development.databinding.FragmentDiscoveryBinding
import com.example.playtomic_mobile_development.model.Court
import com.example.playtomic_mobile_development.ui.play.CreateMatchActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class DiscoveryFragment : Fragment() {

    private var _binding: FragmentDiscoveryBinding? = null

    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    private lateinit var firestore: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    private lateinit var imageView: ImageView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        val discoveryViewModel =
            ViewModelProvider(this).get(DiscoveryViewModel::class.java)

        _binding = FragmentDiscoveryBinding.inflate(inflater, container, false)
        val root: View = binding.root


        storageReference = FirebaseStorage.getInstance().reference
        firestore = FirebaseFirestore.getInstance()

        firestore.collection("courts").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val courtId = document.id
                    val courtName = document.getString("name") ?: ""
                    val courtLocation = document.getString("location") ?: ""
                    val courtCity = document.getString("city") ?: ""

                    val linearLayout = LinearLayout(requireContext())
                    linearLayout.orientation = LinearLayout.VERTICAL

                    val textView = TextView(requireContext())
                    textView.text = "Name: $courtName \n Address: $courtLocation \n City: $courtCity"
                    textView.textSize = 20f

                    linearLayout.addView(textView)

                    val imageView = ImageView(requireContext())
                    val imageReference = storageReference.child("$courtId.court.png")

                    imageReference.downloadUrl.addOnSuccessListener { uri ->
                        Picasso.get().load(uri).resize(200, 200).into(imageView)

                        linearLayout.addView(imageView)
                        val button = Button(requireContext())
                        button.text = "Create Match"
                        button.setOnClickListener {
                            val intent = Intent(requireContext(), CreateMatchActivity::class.java)
                            intent.putExtra("court", courtId)
                            startActivity(intent)
                        }

                        // Voeg de knop toe aan het LinearLayout
                        linearLayout.addView(button)
                        binding.courtsLayout.addView(linearLayout)
                    }.addOnFailureListener { exception ->
                        Log.e("CourtsFragment", "Error loading image for court $courtId", exception)
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