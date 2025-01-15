package com.example.proyectomusica.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.proyectomusica.R
import com.example.proyectomusica.databinding.FragmentHomeBinding


class FragmentHome : Fragment() {
    lateinit var binding : FragmentHomeBinding
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater,container, false)

        Glide.with(this)
            .asGif()
            .load(R.drawable.musicnote)
            .into(binding.gifImageView)
        return binding.root;
    }


}