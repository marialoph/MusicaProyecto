package com.example.proyectomusica.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomusica.data.network.repository.Repository
import com.example.proyectomusica.domain.models.ListMusica
import com.example.proyectomusica.domain.models.Musica
import com.example.proyectomusica.domain.usecase.AllMusicaUseCase
import com.example.proyectomusica.domain.usecase.DeleteMusicaUseCase
import com.example.proyectomusica.domain.usecase.NewMusicaUseCase
import com.example.proyectomusica.domain.usecase.UpdateMusicaUseCase
import kotlinx.coroutines.launch

class MusicaViewModel(private val musicaRepository: Repository) : ViewModel() {

    private val getAllMusicaUseCase = AllMusicaUseCase(musicaRepository)
    private val newMusicaUseCase = NewMusicaUseCase(musicaRepository)
    private val updateMusicaUseCase = UpdateMusicaUseCase(musicaRepository)
    private val deleteMusicaUseCase = DeleteMusicaUseCase(musicaRepository)

    val musicaListData = MutableLiveData<List<Musica>>()

    // Obtiene la lista completa de música desde la api.
    fun showMusica() {
        viewModelScope.launch {
            val result = getAllMusicaUseCase()
            result.onSuccess { list ->
                Log.d("FragmentMusica", "Datos obtenidos: $list")
                ListMusica.musica = list.toMutableList()
                musicaListData.postValue(list)
            }.onFailure { error ->
                Log.e("FragmentMusica", "Error al obtener los datos: $error")
                musicaListData.postValue(emptyList())
            }
        }

    }

    // Añade una nueva música y actualiza la lista.
    fun addMusica(musica: Musica) {
        viewModelScope.launch {
            val result = newMusicaUseCase(musica)
            result.onSuccess {
                ListMusica.musica.add(musica)
                musicaListData.postValue(ListMusica.musica)
            }.onFailure { error ->
                Log.e("FragmentMusica", "Error al agregar música: $error")
            }
        }
    }

    // Elimina una música de la lista.
    fun deleteMusica(pos: Int) {
        viewModelScope.launch {
            if (pos in ListMusica.musica.indices) {
                val musicaAEliminar = ListMusica.musica[pos]
                val result = deleteMusicaUseCase(musicaAEliminar.nombre)
                result.onSuccess {
                    ListMusica.musica.removeAt(pos)
                    musicaListData.postValue(ListMusica.musica)
                }.onFailure { error ->
                    // Manejar error
                    Log.e("FragmentMusica", "Error al eliminar música: $error")
                }
            }
        }
    }
    //Actualiza la musica por nombre
    fun updateMusica(updatedMusica: Musica) {
        viewModelScope.launch {
            val result = updateMusicaUseCase(updatedMusica.nombre, updatedMusica)
            result.onSuccess {
                val index = ListMusica.musica.indexOfFirst { it.nombre == updatedMusica.nombre }
                if (index != -1) {
                    ListMusica.musica[index] = updatedMusica
                    musicaListData.postValue(ListMusica.musica)
                }
            }.onFailure { error ->
                Log.e("FragmentMusica", "Error al actualizar música: $error")
            }
        }
    }

}
