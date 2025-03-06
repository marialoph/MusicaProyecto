package com.example.proyectomusica.ui.views.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomusica.databinding.ActivityRegisterBinding

import androidx.lifecycle.lifecycleScope
import com.example.proyectomusica.data.network.InstanceRetrofit
import com.example.proyectomusica.data.network.repository.Repository
import com.example.proyectomusica.domain.usecase.RegisterUseCase
import kotlinx.coroutines.launch
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerUseCase: RegisterUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val musicaRepository = Repository(InstanceRetrofit.getInstance(this), this)
        registerUseCase = RegisterUseCase(musicaRepository)

        binding.btnRegistrar.setOnClickListener { registerUser() }
        binding.btnLogueo.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerUser() {
        val email = binding.editUserRegister.text.toString()
        val password = binding.editPasswordRegister.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_LONG).show()
            return
        }

        lifecycleScope.launch {
            val result = registerUseCase.register(email, password)
            result.onSuccess {
                Toast.makeText(this@RegisterActivity, it, Toast.LENGTH_LONG).show()
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }.onFailure {
                Toast.makeText(this@RegisterActivity, it.message ?: "Error desconocido", Toast.LENGTH_LONG).show()
            }
        }
    }
}
