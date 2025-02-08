package com.example.proyectomusica.ui.views.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectomusica.ui.views.activities.MainActivity
import com.example.proyectomusica.data.repository.MusicaRepository
import com.example.proyectomusica.databinding.FragmentMusicaBinding
import com.example.proyectomusica.domain.models.Musica
import com.example.proyectomusica.ui.adapter.AdapterMusica
import com.example.proyectomusica.ui.viewmodel.MusicaViewModel
import com.example.proyectomusica.ui.views.dialogues.AddDialog
import com.example.proyectomusica.ui.views.dialogues.DeleteDialog
import com.example.proyectomusica.ui.views.dialogues.EditDialog

class FragmentMusica : Fragment() {

    lateinit var binding: FragmentMusicaBinding
    lateinit var activitycontext: MainActivity
    lateinit var adapterMusica: AdapterMusica
    val musicaRepository = MusicaRepository()
    private val musicaViewModel: MusicaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMusicaBinding.inflate(inflater, container, false)
        return binding.root
    }

    //Se configura el recyclerview, el adapter y se observa los cambios en los datos
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myRecyclerView.layoutManager = LinearLayoutManager(activity)
        setAdapter(mutableListOf())
        setObserver() // Observa los cambios en los datos
        musicaViewModel.showMusica() // Muestra todos los datos

        //Configuro el boton para mostrar el dialogo añadir
        binding.buttonAnnadir.setOnClickListener {
            btnAddOnClickListener()
        }
    }

    //Método que muestra el dialogo y lo actualiza
    private fun btnAddOnClickListener() {
        val dialog = AddDialog() { musica ->
            musicaViewModel.addMusica(musica)
        }
        dialog.show(requireActivity().supportFragmentManager, "Añadir un nuevo artista")
    }

    //Método que muestra el listado de musica
    private fun setAdapter(musics: MutableList<Musica>) {
        adapterMusica = AdapterMusica(
            musics,
            { musica -> delMusica(musica) },
            { musica -> updateMusica(musica) }
        )
        binding.myRecyclerView.adapter = adapterMusica
    }

    //Muestra el dialogo para editar
    //Se obtiene el indice de la musica a editar
    //Se actualiza
    private fun updateMusica(musica: Musica) {
        val editDialog = EditDialog(musica) { editMusica ->
            // Obtiene el índice de la música a actualizar
            val pos = musicaViewModel.musicaListData.value?.indexOfFirst { it.nombre == musica.nombre } ?: -1
            if (pos != -1) {
                musicaViewModel.updateMusica(editMusica, pos)
            } else {
                Log.e("Edit", "Música no encontrada para editar.")
            }
        }
        editDialog.show(requireActivity().supportFragmentManager, "Editar un artista")
    }


    //Muestra el diálogo para eliminar
    //Se obtiene el indice de la musica a eliminar
    //Se actualiza
    private fun delMusica(musica: Musica) {
        val dialog = DeleteDialog(musica.nombre) {
            // Obtiene el índice de la música a eliminar
            val pos = musicaViewModel.musicaListData.value?.indexOfFirst { it.nombre == musica.nombre } ?: -1
            if (pos != -1) {
                musicaViewModel.deleteMusica(pos)
            } else {
                Log.e("Delete", "Música no encontrada para eliminar.")
            }
        }
        dialog.show(requireActivity().supportFragmentManager, "Eliminar un artista")
    }

    //Se observan los cambios de la lista
    private fun setObserver() {
        musicaViewModel.musicaListData.observe(viewLifecycleOwner) { musics ->
            setAdapter(musics.toMutableList())
        }
    }

}
