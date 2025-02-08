package com.example.proyectomusica.ui.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.proyectomusica.R
import com.example.proyectomusica.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMainBinding.inflate( layoutInflater)
        setContentView( binding.root)

        val toolbar = binding.appBarLayoutDrawer.toolbar
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.navController

        val navView = binding.myNavView

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.fragmentHome, R.id.fragmentSetting), // Destinos principales
            binding.main
        )
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        //Titulo de cada fragmento al pulsar
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fragmentHome -> updateTitulo("Inicio")
                R.id.fragmentSetting -> updateTitulo("Setting")
                R.id.fragmentMusica -> updateTitulo("Artistas")
                else -> updateTitulo("Musica")
            }
        }
    }

    //Método para actualizar el titulo al pulsar en el menu
    private fun updateTitulo(title: String) {
        supportActionBar?.title = title
    }

    // Eliminar el estado de login de SharedPreferences
    private fun logout() {
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_logged_in", false)
        editor.apply()

        val auth = FirebaseAuth.getInstance()
        auth.signOut()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


    override fun onSupportNavigateUp(): Boolean{
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }


    //Navegación del menú de opciones.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fragmentHome -> {
                navController.navigate(R.id.fragmentHome)
                updateTitulo("Inicio")
                true
            }

            R.id.fragmentSetting -> {
                navController.navigate(R.id.fragmentSetting)
                updateTitulo("Setting")
                true
            }

            R.id.fragmentLogout -> {
                logout()
                true
            }

            R.id.fragmentMusica -> {
                navController.navigate(R.id.fragmentMusica)
                updateTitulo("Artistas")
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}