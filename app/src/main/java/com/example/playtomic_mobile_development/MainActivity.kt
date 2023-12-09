package com.example.playtomic_mobile_development

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.playtomic_mobile_development.databinding.ActivityMainBinding
import com.example.playtomic_mobile_development.databinding.ToolbarBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.xml_toolbar)) // Hierbij ga ik ervan uit dat je een Toolbar met de id 'toolbar' hebt in je layout



        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_play, R.id.navigation_discovery, R.id.navigation_community, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)




    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        binding.btnSignout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
        val inflater = menuInflater
        inflater.inflate(R.menu.top_nav_menu,menu)
        return true
    }*/

}