package com.example.proyectomusica.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.proyectomusica.R
import com.example.proyectomusica.databinding.FragmentSettingBinding


class FragmentSetting : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private val sharedPreferences by lazy {
        requireContext().getSharedPreferences("SettingsPrefs", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        pasarTemaClaroOscuro()

        pulsarBoton()

        return binding.root

    }
    private fun pasarTemaClaroOscuro() {
        val tema = sharedPreferences.getBoolean("theme_dark", false)
        binding.rgTheme.check(if (tema) R.id.rb_dark else R.id.rb_light)
    }

    private fun pulsarBoton() {
        binding.btnSave.setOnClickListener {
            guardarSetting()
        }
    }

    private fun guardarSetting() {
        val darkTema = binding.rbDark.isChecked
        sharedPreferences.edit().putBoolean("theme_dark", darkTema).apply()
        val nuevoTema = if (darkTema) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(nuevoTema)
        Toast.makeText(requireContext(), "Configuraciones guardadas", Toast.LENGTH_SHORT).show()
    }

}