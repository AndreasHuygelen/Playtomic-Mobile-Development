package com.example.playtomic_mobile_development.ui.play

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.playtomic_mobile_development.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow
import java.util.ArrayList


class CommunityFragment : Fragment() {

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private lateinit var mapView: MapView
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storageReference: StorageReference

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Configuration.getInstance().userAgentValue = BuildConfig.LIBRARY_PACKAGE_NAME
        checkPermissions()
        val view = inflater.inflate(R.layout.fragment_community, container, false)
        mapView = view.findViewById(R.id.mapView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        val startPoint = GeoPoint(51.2301298, 4.4117949)
        mapView.controller.setZoom(11)
        mapView.controller.setCenter(startPoint)

        storageReference = FirebaseStorage.getInstance().reference
        firestore = FirebaseFirestore.getInstance()
        val courtsCollection = firestore.collection("courts")

        courtsCollection.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val latitude = document.getDouble("lat")
                val longitude = document.getDouble("long")

                if (latitude != null && longitude != null) {
                    val marker = Marker(mapView)
                    val markerPoint = GeoPoint(latitude, longitude)
                    marker.position = markerPoint
                    mapView.overlays.add(marker)
                }
            }
        }.addOnFailureListener { exception ->
            // Hier kun je de logica toevoegen voor het geval het ophalen van gegevens mislukt
            Log.w("Firestore", "Error getting documents: ", exception)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
    private val REQUESTCODE = 100
    private fun checkPermissions() {
        val permissions: MutableList<String> = ArrayList()
        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }*/
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (permissions.isNotEmpty()) {
            val params = permissions.toTypedArray()
            requestPermissions(params, REQUESTCODE)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUESTCODE -> {
                val perms: MutableMap<String, Int> = HashMap()
                // fill with results
                var i = 0
                while(i < permissions.size) {
                    perms[permissions[i]] = grantResults[i]
                    i++
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}