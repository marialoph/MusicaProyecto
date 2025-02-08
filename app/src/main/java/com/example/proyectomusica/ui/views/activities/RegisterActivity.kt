package com.example.proyectomusica.ui.views.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectomusica.databinding.ActivityRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth

class RegisterActivity : AppCompatActivity() {
    private lateinit var btnRegister : Button
    private lateinit var btnLoguear : Button
    private lateinit var editUser : EditText
    private lateinit var editPassword : EditText
    private lateinit var editRepetirPassword : EditText
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
        start()
    }

    //Método para inicializar las referencias de los elementos del xml y FirebaseAuth
    private fun init(){
        btnRegister = binding.btnRegistrar
        btnLoguear = binding.btnLogueo
        editUser = binding.editUserRegister
        editPassword = binding.editPasswordRegister
        editRepetirPassword = binding.editRepetirPassword
        auth = Firebase.auth
    }

    //Método que configura los botones
    // El boton Registro: Se obtiene texto del correo, la contraseña y repetir coñtraseña
    // Se validan que los campos no están vacíos
    // Si el registro se hace correctamente, irá al login.
    private fun start() {
        btnRegister.setOnClickListener{
            val email = editUser.text.toString()
            val pass = editPassword.text.toString()
            val repeatPass = editRepetirPassword.text.toString()
            if (pass != repeatPass
                || email.isEmpty()
                || pass.isEmpty()
                || repeatPass.isEmpty())
                Toast.makeText(this, "Campo vacío/Contraseña incorrecta", Toast.LENGTH_LONG).show()
            else{
                registroDelUsuario (email, pass){
                        result, msg ->
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                    if (result)
                        startActivityLogin()
                }
            }
        }

        btnLoguear.setOnClickListener{
            view->
                val intent = Intent (this, LoginActivity::class.java)
                startActivity(intent)
                finish()
        }
    }

    //Método para iniciar el login
    private fun startActivityLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    //Método para registrar a un usuario en Firebase
    //Se crea el usuario, se envía el correo de verificacion para poder loguarse.
    private fun registroDelUsuario(email: String, pass: String, onResult: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this){
                    taskAssin->
                if (taskAssin.isSuccessful){
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener{
                                taskVerification ->
                            var msg = ""
                            if (taskVerification.isSuccessful)
                                msg = "Usuario registrado correctamente. Verifique su correo"
                            else
                                msg = "Usuario registrado. ${taskVerification.exception?.message}"
                            auth.signOut()
                            onResult(true, msg)
                        }
                        ?.addOnFailureListener{
                                exception->
                            Log.e("ActivityRegister", "Se ha producido un error al enviar correo de verificación: ${exception.message}")
                            onResult(false, "No se pudo enviar el correo de verificación: ${exception.message}")
                        }

                }else{
                    try{
                        throw taskAssin.exception ?:Exception ("Error desconocido")
                    } catch (e: FirebaseAuthUserCollisionException){
                        onResult (false, "Usuario ya existente")
                    }catch (e: FirebaseAuthWeakPasswordException){
                        onResult (false, "La contraseña no es segura: ${e.reason}")
                    }
                    catch (e: FirebaseAuthInvalidCredentialsException){
                        onResult (false, "El email introducido, no es correcto")
                    }
                    catch (e: Exception){
                        onResult (false, e.message.toString())
                    }

                }
            }

    }
}