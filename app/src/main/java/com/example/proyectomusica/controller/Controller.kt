package com.example.proyectomusica.controller


import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectomusica.MainActivity
import com.example.proyectomusica.adapter.AdapterMusica
import com.example.proyectomusica.dao.DaoMusica
import com.example.proyectomusica.databinding.ActivityMainBinding
import com.example.proyectomusica.dialogues.AddDialog
import com.example.proyectomusica.dialogues.EditDialog
import com.example.proyectomusica.models.Musica

class Controller (val context : Context, var binding : ActivityMainBinding){
    lateinit var listaMusica : MutableList<Musica> //lista de objetos

    init {
        initData()
    }

    fun initData(){
        listaMusica = DaoMusica. myDao.getDataMusica(). toMutableList()
    }

    // Este método configura el adaptador para el RecyclerView y lo conecta al RecyclerView de la actividad
    fun setAdapter() {
        val myActivity = context as MainActivity
        val adapterMusica = AdapterMusica(
            listaMusica,
            {pos -> delMusica(pos)},
            {pos -> mostrarEditDialog(pos)}
        )

        myActivity. binding.myRecyclerView.adapter = adapterMusica
        myActivity.binding.myRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    // Elimina la música de la lista
    // Creo una variable para que al imprimir el mensaje, se sepa que artista se ha eliminado
    // Se notifica los cambios
    // Y se actualiza las posiciones
    // Se imprimirá un mensaje de que se ha eliminado
    fun delMusica(pos: Int) {
        val nombreArtista = listaMusica[pos].nombre
        listaMusica.removeAt(pos)
        (binding.myRecyclerView.adapter as AdapterMusica).notifyItemRemoved(pos)
        (binding.myRecyclerView.adapter as AdapterMusica).notifyItemRangeChanged(pos, listaMusica.size - pos)
        Toast.makeText(context, "Se eliminó el artista: $nombreArtista", Toast.LENGTH_SHORT).show()
    }


    //Muestra la vista del AddDialog
    fun mostrarAddDialog(){
        val addDialog = AddDialog {
            musica -> actualizaMusicaNueva(musica)
        }
        val activity = context as AppCompatActivity
        addDialog.show(activity.supportFragmentManager, "AddDialog")

    }
    //Actualiza el cardiewnuevo
    private fun actualizaMusicaNueva(musica: Musica) {
        listaMusica.add(musica)
        (binding.myRecyclerView.adapter as AdapterMusica).notifyItemInserted(listaMusica.size - 1)
        Toast.makeText(context,"Nuevo artista ${musica.nombre}", Toast.LENGTH_SHORT).show()
    }



    //Muestra la vista del EditDialog para editar algun campo del artista seleccionado
    fun mostrarEditDialog(musica: Musica) {
        val editDialog = EditDialog(musica) {
            musicaEditada ->
            actualizaMusicaEditada(musicaEditada)
        }
        val activity = context as AppCompatActivity
        editDialog.show(activity.supportFragmentManager, "EditDialog")
    }

    //Actualiza el dato que se ha modificado
    fun actualizaMusicaEditada(musica: Musica) {
        val position = listaMusica.indexOfFirst {
            it.nombre == musica.nombre
        }
        if (position != -1) {
            listaMusica[position] = musica
            (binding.myRecyclerView.adapter as AdapterMusica).notifyItemChanged(position)
            Toast.makeText(context,"El artista ${musica.nombre} se ha modificado", Toast.LENGTH_SHORT).show()

        }
    }



}