package com.example.mostafapharmacyproject.Activities

import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isEmpty
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.example.mostafapharmacyproject.Models.Customer
import com.example.mostafapharmacyproject.R
import com.example.mostafapharmacyproject.databinding.ActivityMainBinding
import com.example.mostafapharmacyproject.databinding.DrawerHeaderBinding
import com.example.mostafapharmacyproject.dp.PharmacyMVVM
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    val pharmacyMVVM: PharmacyMVVM by viewModels()
    lateinit var preferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = getSharedPreferences("Customers", MODE_PRIVATE)


        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)


        binding.drawerbar.getHeaderView(0).findViewById<AppCompatImageView>(R.id.signOutHeader)
            .setOnClickListener {
                preferences.edit().putString("cur_email", null).apply()
                finish()
            }

        preferences.getString("cur_email", "")?.let {
            pharmacyMVVM.getCustomer(it)?.subscribe {
                binding.drawerbar.getHeaderView(0)
                    .findViewById<AppCompatTextView>(R.id.name).text = it.name
                binding.drawerbar.getHeaderView(0)
                    .findViewById<AppCompatTextView>(R.id.email).text = it.Email
            }
        }

        binding.drawerbar.setNavigationItemSelectedListener {
            binding.drawerLayout.close()
            NavigationUI.onNavDestinationSelected(it, navController) || super.onOptionsItemSelected(
                it)
        }


    }

    private fun tintIcon(overflowIcon: Drawable?, res: Int): Drawable {
        overflowIcon?.let { DrawableCompat.wrap(it) }
        DrawableCompat.setTint(overflowIcon!!, ResourcesCompat.getColor(resources, res, theme))
        return overflowIcon
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.PrescriptionFragment -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}