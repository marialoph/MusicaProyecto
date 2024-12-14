package com.example.proyectomusica

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectomusica.controller.Controller
import com.example.proyectomusica.databinding.ActivityMainBinding
import com.example.proyectomusica.objects_models.Repository
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var controller : Controller
    lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMainBinding.inflate( layoutInflater)
        setContentView( binding.root)
        init()

        binding.buttonAnnadir.setOnClickListener{
            controller.mostrarAddDialog()
        }

        binding.buttonLogOut.setOnClickListener{
           logout()
        }
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

    fun init(){
        initRecyclerView()
        controller = Controller(this, binding)
        controller.setAdapter()

    }

    //Este método configura el recyclerview dentro del activity
    private fun initRecyclerView() {
        binding.myRecyclerView.layoutManager = LinearLayoutManager( this)
    }



}