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
            view->
            intent = Intent(this, LoginActivity::class.java).apply{
                putExtra("name", "maria")
            }
            startActivity(intent)
        }
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