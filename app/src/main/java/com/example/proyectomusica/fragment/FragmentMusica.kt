package com.example.proyectomusica.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectomusica.MainActivity
import com.example.proyectomusica.R
import com.example.proyectomusica.controller.Controller
import com.example.proyectomusica.databinding.FragmentMusicaBinding

class FragmentMusica : Fragment() {
    lateinit var binding: FragmentMusicaBinding
    lateinit var controller: Controller
    lateinit var activitycontext : MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activitycontext = requireActivity() as MainActivity
        binding = FragmentMusicaBinding.inflate(inflater, container, false)
        controller = Controller(activitycontext, this)
        initRecyclerView()

        binding.buttonAnnadir.setOnClickListener {
            controller.mostrarAddDialog()
        }

        return binding.root
    }

    // Este método configura el RecyclerView
    private fun initRecyclerView() {
        binding.myRecyclerView.layoutManager = LinearLayoutManager(context)
        controller.setAdapter()
    }

}
