package com.example.proyectomusica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectomusica.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    private lateinit var btnLogin : Button
    private lateinit var btnRegister : Button
    private lateinit var btnOlvidarPassword : Button
    private lateinit var editUser : EditText
    private lateinit var editPassword : EditText
    private lateinit var binding : ActivityLoginBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

        if (isLoggedIn) {
            // Si el usuario ya está logueado, redirigir al MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Finalizamos esta actividad para no poder regresar a ella
        } else {
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.main)
            ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
            init()
            start()
        }
    }

    //Método para inicializar las referencias de los elementos del xml y FirebaseAuth
    private fun init(){
        btnLogin = binding.btnLoguear
        btnRegister = binding.btnRegistrarLogin
        btnOlvidarPassword = binding.btnOlvidarPassword
        editUser = binding.editUsuarioLogin
        editPassword = binding.editPasswordLogin
        auth = Firebase.auth
    }

    //Método que configura los botones
    //Boton login:
    // Se obtiene el correo y la contraseña
    // Se comprueba que los campos no estén vacíos.
    //Boton Registro: Lleva al activity del registro.
    //Botón para recuperar la contraseña:
    // Se obtiene el email y se le envía un correo para cambio de contraseña
    private fun start() {
        btnLogin.setOnClickListener{
            val user = editUser.text.toString()
            val pass = editPassword.text.toString()

            if (user.isNotEmpty() && pass.isNotEmpty())
                iniciarLogin(user, pass){
                        result, msg ->
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                    if (result){
                        intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            else
                Toast.makeText(this, "Algun campo vacío", Toast.LENGTH_LONG).show()
        }

        btnRegister.setOnClickListener{
            intent = Intent (this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnOlvidarPassword.setOnClickListener{
            val user = editUser.text.toString()
            if (user.isNotEmpty())
                olvidarPassword(user){
                        result, msg ->
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                    if (!result)
                        editUser.setText("")
                }
            else
                Toast.makeText(this, "Rellena el campo email", Toast.LENGTH_LONG).show()
        }
    }

    //Método para enviar un correo para cambiar contraseña.
    private fun olvidarPassword(email : String, onResult: (Boolean, String)->Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener{
                    taskResetEmail ->
                if (taskResetEmail.isSuccessful){
                    onResult (true, "Se ha enviado un email con la nueva contraseña")
                }else{
                    var msg = ""
                    try{
                        throw taskResetEmail.exception?:Exception("Error")
                    }catch (e : FirebaseAuthInvalidCredentialsException){
                        msg = "El formato del email no es válido"
                    }catch (e: Exception){
                        msg = e.message.toString()
                    }
                    onResult(false, msg)
                }
            }
    }

    //Método para iniciar sesion con el email y la contraseña
    // Se debe verificar el correo antes de loguearse.
    //Se usa sharedpreferences.
    private fun iniciarLogin(user: String, pass: String, onResult: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(user, pass)
            .addOnCompleteListener {
                    taskAssin ->
                var msg = ""
                if (taskAssin.isSuccessful){
                    val posibleUser = auth.currentUser
                    if (posibleUser?.isEmailVerified == true){
                        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("is_logged_in", true)
                        editor.apply()

                        onResult ( true, "Usuario logueado correctamente")
                    }else{
                        auth.signOut()
                        onResult (false, "Se debe verificar tu correo antes de loguearte")
                    }
                }else{
                    try {
                        throw taskAssin.exception?: Exception("Error")
                    }catch (e: FirebaseAuthInvalidUserException){
                        msg = "El usuario no existe debe haberse borrado/desabilitado"
                    }catch (e: FirebaseAuthInvalidCredentialsException){
                        msg = if (e.message?.contains("No existe ningún registro de usuario correspondiente a este identificador") == true){
                            "El usuario no existe"
                        }else "contraseña incorrecta/usuario no existe"

                    }catch (e: Exception){
                        msg = e.message.toString()
                    }

                    onResult (false, msg)
                }

            }
    }
}