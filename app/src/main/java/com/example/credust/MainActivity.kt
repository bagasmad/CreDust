package com.example.credust

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.example.credust.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var toolbar: ActionBar
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = binding.bottomNavbar
        bottomNavigation.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId)
        {
            R.id.scan ->
            {
                val cameraFragment = CameraFragment.newInstance()
                fragmentNavigate(cameraFragment)
                return true
            }
            R.id.feed ->
            {
                val homeFragment = HomeFragment.newInstance()
                fragmentNavigate(homeFragment)
                return true
            }
            R.id.explore ->
            {
                val exploreFragment = ExploreFragment.newInstance()
                fragmentNavigate(exploreFragment)
                return true

            }

        }
        return false
    }

    private fun fragmentNavigate(fragment: Fragment)
    {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}