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
import com.example.playtomic_mobile_development.R
import com.example.playtomic_mobile_development.databinding.FragmentDiscoveryBinding
import com.example.playtomic_mobile_development.model.Court
import com.example.playtomic_mobile_development.ui.community.MapActivity
import com.example.playtomic_mobile_development.ui.play.CreateBookingActivity
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
                    val courtLat = document.getDouble("lat") ?: ""
                    val courtLong = document.getDouble("long") ?: ""

                    val linearLayout = LinearLayout(requireContext())
                    linearLayout.orientation = LinearLayout.VERTICAL


                    val textViewWrapper = LinearLayout(requireContext())
                    textViewWrapper.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    val joinViewWrapper = LinearLayout(requireContext())
                    joinViewWrapper.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    val textView = TextView(requireContext())
                    textView.text = "Name: $courtName \n Address: $courtLocation \n City: $courtCity"
                    textView.textSize = 20f


                    val imageView = ImageView(requireContext())
                    val imageReference = storageReference.child("$courtId.court.png")

                    imageReference.downloadUrl.addOnSuccessListener { uri ->
                        Picasso.get().load(uri).resize(200, 200).into(imageView)

                        textViewWrapper.addView(imageView)
                        val button = Button(requireContext())
                        button.text = "Create Match"
                        button.setOnClickListener {
                            val intent = Intent(requireContext(), CreateMatchActivity::class.java)
                            intent.putExtra("court", courtId)
                            startActivity(intent)
                        }
                        val buttonBook = Button(requireContext())
                        buttonBook.text = "Book the court"
                        buttonBook.setOnClickListener {
                            val intent = Intent(requireContext(), CreateBookingActivity::class.java)
                            intent.putExtra("court", courtId)
                            startActivity(intent)
                        }
                        val buttonMap = Button(requireContext())
                        buttonMap.text = "Map"
                        buttonMap.setOnClickListener {
                            val intent = Intent(requireContext(), MapActivity::class.java)
                            intent.putExtra("lat", courtLat)
                            intent.putExtra("long", courtLong)
                            startActivity(intent)
                        }


                        val rightGravityLayoutParams = LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        rightGravityLayoutParams.weight = 1f
                        rightGravityLayoutParams.gravity = Gravity.END

                        buttonBook.layoutParams = rightGravityLayoutParams
                        joinViewWrapper.addView(button)
                        joinViewWrapper.addView(buttonBook)
                        joinViewWrapper.addView(buttonMap)
                        textViewWrapper.addView(textView)
                        linearLayout.addView(textViewWrapper)
                        linearLayout.addView(joinViewWrapper)

                        binding.courtsLayout.addView(linearLayout)

                        val marginParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        marginParams.setMargins(16, 16, 16, 16)
                        linearLayout.layoutParams = marginParams
                        linearLayout.setBackgroundResource(R.drawable.border)
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