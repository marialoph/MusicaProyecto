package com.example.proyectomusica.ui.views.activities


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyectomusica.data.network.InstanceRetrofit
import com.example.proyectomusica.data.network.repository.Repository
import com.example.proyectomusica.databinding.ActivityLoginBinding
import com.example.proyectomusica.domain.usecase.LoginUseCase
import kotlinx.coroutines.launch
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginUseCase: LoginUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val musicaRepository = Repository(InstanceRetrofit.getInstance(this), this)
        loginUseCase = LoginUseCase(musicaRepository)

        binding.btnLoguear.setOnClickListener { loginUser() }
        binding.btnRegistrarLogin.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginUser() {
        val email = binding.editUsuarioLogin.text.toString().trim()
        val password = binding.editPasswordLogin.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_LONG).show()
            return
        }

        binding.btnLoguear.isEnabled = false // Deshabilita el botón mientras carga, para evitar que si pincho dos veces se me generen diferentes token

        lifecycleScope.launch {
            val result = loginUseCase.login(email, password)
            result.onSuccess {
                Toast.makeText(this@LoginActivity, "Login exitoso", Toast.LENGTH_LONG).show()
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }.onFailure { exception ->
                Toast.makeText(
                    this@LoginActivity,
                    exception.message ?: "Error al iniciar sesión",
                    Toast.LENGTH_LONG
                ).show()
            }
            binding.btnLoguear.isEnabled = true
        }
    }
}
