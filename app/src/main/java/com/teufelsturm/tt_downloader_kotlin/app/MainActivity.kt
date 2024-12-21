package com.teufelsturm.tt_downloader_kotlin.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import de.teufelsturm.tt_downloader_ktx.R

// The @AndroidEntryPoint annotation tells Dagger Hilt to generate a component for dependency injection
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // The NavController is used to control app navigation, including navigating between fragments
    private lateinit var navController: NavController

    // The onCreate() method is called when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sets the content view of the activity to the specified layout file (main_activity.xml)
        setContentView(R.layout.main_activity)

        // Retrieves the NavHostFragment from the fragment manager using its ID.
        // The NavHostFragment is a container that hosts the navigation graph and manages fragment navigation.
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // Obtains the NavController from the NavHostFragment, which is responsible for managing app navigation
        navController = navHostFragment.findNavController()

        // Finds the Toolbar from the layout using its ID
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        // Sets up the Toolbar to act as the ActionBar for this activity, enabling action buttons and navigation
        setSupportActionBar(toolbar)

        // Links the ActionBar (Toolbar) with the NavController to synchronize the app bar title
        // and enable back navigation
        setupActionBarWithNavController(navController)
    }

    // This method is called when the "up" button (back button in the app bar) is pressed.
    // It allows the NavController to handle the back navigation.
    override fun onSupportNavigateUp(): Boolean {
        // Attempts to navigate up in the navigation stack using the NavController.
        // If the NavController cannot navigate up, it falls back to the default behavior from the parent class.
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
