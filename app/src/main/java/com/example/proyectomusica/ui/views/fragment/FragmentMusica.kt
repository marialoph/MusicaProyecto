package com.example.proyectomusica.ui.views.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectomusica.data.network.InstanceRetrofit
import com.example.proyectomusica.data.network.repository.Repository
import com.example.proyectomusica.databinding.FragmentMusicaBinding
import com.example.proyectomusica.domain.models.Musica
import com.example.proyectomusica.ui.adapter.AdapterMusica
import com.example.proyectomusica.ui.viewmodel.MusicaViewModel
import com.example.proyectomusica.ui.views.dialogues.AddDialog
import com.example.proyectomusica.ui.views.dialogues.DeleteDialog
import com.example.proyectomusica.ui.views.dialogues.EditDialog

class FragmentMusica : Fragment() {
    private lateinit var binding: FragmentMusicaBinding
    private lateinit var viewModel: MusicaViewModel
    private lateinit var adapter: AdapterMusica

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMusicaBinding.inflate(inflater, container, false)
        val context = requireContext()
        val apiService = InstanceRetrofit.getInstance(context)
        val musicaRepository = Repository(apiService, context)
        viewModel = MusicaViewModel(musicaRepository)

        adapter = AdapterMusica(mutableListOf(), { musica ->
            val pos = adapter.listaMusica.indexOf(musica)
            if (pos != -1) {
                showDeleteDialog(musica,pos)
            }
        }, { musica ->
                showEditDialog(musica)
        })

        binding.myRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.myRecyclerView.adapter = adapter

        binding.buttonAnnadir.setOnClickListener {
            showAddDialog()
        }


        viewModel.musicaListData.observe(viewLifecycleOwner) { lista ->
            if (lista.isNotEmpty()) {
                adapter.listaMusica = lista.toMutableList()  // Actualizo la lista en el Adapter
                adapter.notifyDataSetChanged()  // Notifico al Adapter de los cambios
                binding.myRecyclerView.visibility = View.VISIBLE
            } else {
                binding.myRecyclerView.visibility = View.GONE
            }
        }

        // Cargo la lista de música desde el backend
        // Llamo a la función que obtiene los datos de la API
        viewModel.showMusica()
        return binding.root
    }

    //Método para mostrar el diálogo añadir
    private fun showAddDialog() {
        val dialog = AddDialog { nuevaMusica ->
            viewModel.addMusica(nuevaMusica)
        }
        dialog.show(childFragmentManager, "AddDialog")    }

    //Método para mostrar el diálogo editar
    private fun showEditDialog(musica: Musica) {
        val dialog = EditDialog(musica) { updatedMusica ->
            viewModel.updateMusica(updatedMusica)
        }
        dialog.show(childFragmentManager, "EditDialog")
    }

    // Método para mostrar el diálogo de eliminar
    private fun showDeleteDialog(musica: Musica, pos: Int) {
        val dialog = DeleteDialog(musica) {
            viewModel.deleteMusica(pos)
        }
        dialog.show(childFragmentManager, "DeleteDialog")
    }
}
