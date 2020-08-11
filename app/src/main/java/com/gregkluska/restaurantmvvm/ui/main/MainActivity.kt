package com.gregkluska.restaurantmvvm.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gregkluska.restaurantmvvm.R
import com.gregkluska.restaurantmvvm.util.BottomNavController
import com.gregkluska.restaurantmvvm.util.BottomNavController.*
import com.gregkluska.restaurantmvvm.util.setUpNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    NavGraphProvider,
    OnNavigationGraphChanged,
    OnNavigationReselectedListener
{

    private lateinit var bottomNavigationView: BottomNavigationView

    private val bottomNavController by lazy(LazyThreadSafetyMode.NONE) {
        BottomNavController(
            this,
            R.id.main_nav_host_fragment,
            R.id.nav_home,
            this,
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar()
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setUpNavigation(bottomNavController, this)
        if (savedInstanceState == null) {
            bottomNavController.onNavigationItemSelected()
        }

    }

    override fun getNavGraphId(itemId: Int) = when(itemId) {
        R.id.nav_home -> {
            R.navigation.nav_home
        }
        R.id.nav_menu -> {
            R.navigation.nav_menu
        }
        else -> {
            R.navigation.nav_home
        }
    }

    override fun onGraphChange() {
//        TODO("Not yet implemented")
    }

    override fun onReselectNavItem(navController: NavController, fragment: Fragment) = when(fragment) {
        else -> {
//        TODO("Not yet implemented")
        }
    }

    override fun onBackPressed() {
        bottomNavController.onBackPressed()
    }

    private fun setupActionBar(){
        setSupportActionBar(tool_bar)
    }

}